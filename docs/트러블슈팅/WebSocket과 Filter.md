# WebSocket과 Filter

## 1. 개요

WebSocket은 클라이언트와 서버 간의 양방향 통신을 가능하게 하는 프로토콜입니다.
이번 프로젝트에서는 STOMP 기반 WebSocket을 도입하여 실시간 주가 변동, 거래 이벤트 알림 등을 구현했습니다.
이를 통해 사용자는 주식 시장의 변동 상황을 지연 없이 확인할 수 있게 되었으며, 서비스의 역동성과 사용자 경험을 높일 수 있었습니다.

이 문서에서는 WebSocket과 JWT 인증을 함께 적용하면서 발생했던 문제와 해결 과정, 그리고 구현 방법을 정리합니다.

---

## 2. WebSocket 구현

### 2.1 WebSocket 설정

Spring Boot에서는 `@EnableWebSocketMessageBroker`를 통해 WebSocket 환경을 쉽게 구성할 수 있습니다.

```kotlin
@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {
    override fun configureMessageBroker(config: MessageBrokerRegistry) { 
        config.enableSimpleBroker("/topic") // 메시지 브로커 경로 설정
        config.setApplicationDestinationPrefixes("/app") // 애플리케이션 메시지 경로 접두사
    }
    
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws") // WebSocket 연결 엔드포인트
            .setAllowedOriginPatterns("*") // CORS 설정
            .withSockJS() // SockJS 지원
    }
}
```

### 2.2 메시지 발행

실시간 주가 업데이트는 `SimpMessagingTemplate`을 통해 간단히 발행할 수 있습니다.

```kotlin
@Service
class StockUpdatePublisher(
    private val messagingTemplate: SimpMessagingTemplate
) {
    fun publishStockUpdate(stockUpdate: StockUpdate) {
        messagingTemplate.convertAndSend("/topic/stock-updates", stockUpdate)
    }
}
```

### 2.3 클라이언트 연결

클라이언트는 SockJS와 Stomp.js를 이용해 서버와 연결합니다.

```javascript
const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/stock-updates', function(message) {
        const stockUpdate = JSON.parse(message.body);
        console.log('Stock Update:', stockUpdate);
    });
});
```

---

## 3. WebSocket에서 JWT 인증 문제

### 3.1 초기 생각

처음 설계했을 때는
“HTTP 요청과 동일하게, WebSocket 연결 요청도 JWT 필터를 통과시키면 되지 않을까?”
라고 생각했습니다.

연결 과정은 처음에는 HTTP 기반으로 이루어지기에,
JWT 필터를 통해 토큰을 검증하는 데 문제가 없었습니다.

### 3.2 문제 발생

하지만 WebSocket 연결이 **수립된 이후**에는 상황이 달라집니다.

* WebSocket 통신은 HTTP 요청이 아니라 WebSocket Frame을 통해 이루어짐
* 따라서 **Spring Security에서 사용하는 HTTP Filter가 더 이상 작동하지 않음**

이로 인해 다음 문제가 생겼습니다:

* 연결 이후 클라이언트가 보내는 메시지는 인증 정보를 갖고 있지 않음
* 특정 유저에게 1:1 메시지를 보내야 할 때 사용자 정보를 알 수 없음
* 특정 권한이 필요한 메시지 처리 시 인증 데이터를 사용할 수 없음

즉, **연결만 인증되고 이후 상태가 유지되지 않는 문제**가 발생했습니다.

---

## 4. 해결 방법

### 4.1 WebSocket 연결 시 JWT 토큰 전달

WebSocket connect 시점에 아래와 같은 방식으로 JWT를 함께 전송하도록 변경했습니다.

```javascript
stompClient.connect(
    { Authorization: token },
    onConnected,
    onError
);
```

이는 HTTP Header와 동일한 역할을 하며,
서버에서 WebSocket Handshake 단계에서 읽어낼 수 있습니다.

### 4.2 HandshakeInterceptor에서 토큰 검증

Spring에서는 Handshake 단계에서 토큰을 추출하여 검증할 수 있습니다.

```kotlin
@Component
class WebSocketAuthInterceptor(
    private val jwtProvider: JwtProvider
) : HandshakeInterceptor {

    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {

        val token = request.headers.getFirst("Authorization")
        val userId = jwtProvider.validateAndGetUserId(token)

        attributes["userId"] = userId
        return true
    }

    override fun afterHandshake(...) { }
}
```

이를 WebSocket 설정에 등록하면 됩니다.

```kotlin
override fun registerStompEndpoints(registry: StompEndpointRegistry) {
    registry.addEndpoint("/ws")
        .addInterceptors(webSocketAuthInterceptor)
        .setAllowedOriginPatterns("*")
        .withSockJS()
}
```

### 4.3 WebSocket 핸들러에서 인증 정보 사용

Handshake에서 저장한 데이터를 Session에서 꺼내 사용하면 됩니다.

```kotlin
@MessageMapping("/trade")
fun processTrade(
    message: TradeMessage,
    headerAccessor: SimpMessageHeaderAccessor
) {
    val userId = headerAccessor.sessionAttributes?.get("userId") as Long
    tradeService.executeTrade(userId, message)
}
```

이제 인증 정보가 필터 없이도 WebSocket 메시지 처리 단계에서 사용 가능합니다.

---

## 5. 결론

* WebSocket 연결은 초기 한 번만 HTTP를 거치므로
  **연결 이후에는 기존 Security Filter가 적용되지 않음**
* 따라서 JWT 기반 인증을 사용하려면
  **Handshake 단계에서 토큰을 검증하고 세션에 저장하는 방식이 필요**
* 이를 통해 이후 메시지 처리에서도
  사용자 정보를 일관되게 사용할 수 있었음

WebSocket과 JWT를 함께 사용하면서
HTTP 기반 인증과의 차이를 직접 경험할 수 있었고,
실시간 서비스에서 인증 상태 유지의 중요성을 배우는 계기가 되었습니다.
