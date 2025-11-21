
<img width="1200" height="2320" alt="ppt" src="https://github.com/user-attachments/assets/db693755-08d9-4d23-8a04-12360548b0fb" />

---
# 핵심 기여
- 실시간 주식 데이터 처리를 위해 웹소켓 + 메시지 브로커(STOMP + RabbitMQ) 기반 설계를 완성
- 실제 거래 환경의 동시성 문제를 메시지 큐로 해결
- JPA 성능 개선 및 쿼리 최적화를 통해 데이터 처리 안정성 강화

# Stokify 기술스택
### Frontend
<img src="https://img.shields.io/badge/react-61DAFB?style=flat-square&logo=react&logoColor=black"> <img src="https://img.shields.io/badge/styled--components-DB7093?style=flat-square&logo=styled-components&logoColor=white"> <img src="https://img.shields.io/badge/axios-5A29E4?style=flat-square&logo=axios&logoColor=white"> <img src="https://img.shields.io/badge/sockjs-000000?style=flat-square&logo=socket.io&logoColor=white"> <img src="https://img.shields.io/badge/recharts-22B5BF?style=flat-square&logo=apache-echarts&logoColor=white">


### Backend

#### FrameWork & Database
<img src="https://img.shields.io/badge/java 21-007396?style=flat-square&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=flat-square&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/spring data jpa-6DB33F?style=flat-square&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/mysql-4479A1?style=flat-square&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/h2-003B57?style=flat-square&logo=h2&logoColor=white"> <img src="https://img.shields.io/badge/querydsl-007ACC?style=flat-square&logo=datadog&logoColor=white">

#### Security & Network
<img src="https://img.shields.io/badge/spring security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white"> <img src="https://img.shields.io/badge/JWT-black?style=flat-square&logo=JSON%20web%20tokens&logoColor=white"> <img src="https://img.shields.io/badge/OAuth2.0-EB5424?style=flat-square&logo=auth0&logoColor=white"> <img src="https://img.shields.io/badge/spring cache-6DB33F?style=flat-square&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/STOMP-000000?style=flat-square&logo=socket.io&logoColor=white"> <img src="https://img.shields.io/badge/rabbitmq-FF6600?style=flat-square&logo=rabbitmq&logoColor=white">

#### Testing & Tools
<img src="https://img.shields.io/badge/junit5-25A162?style=flat-square&logo=junit5&logoColor=white"> <img src="https://img.shields.io/badge/mockito-3D7699?style=flat-square&logo=mockito&logoColor=white"> <img src="https://img.shields.io/badge/swagger-85EA2D?style=flat-square&logo=swagger&logoColor=black">


## [요구사항 명세서](https://github.com/quddaz/Stockify/blob/main/docs/%EC%84%A4%EA%B3%84/%EC%9A%94%EA%B5%AC%EC%82%AC%ED%95%AD_%EB%AA%85%EC%84%B8%EC%84%9C.md)

---

## 시스템 설계
- [ERD 설계서](https://github.com/quddaz/Stockify/blob/main/docs/%EC%84%A4%EA%B3%84/ERD_%EC%84%A4%EA%B3%84.md)
- [확장 설계서](https://github.com/quddaz/Stockify/blob/main/docs/%EC%84%A4%EA%B3%84/%ED%99%95%EC%9E%A5%EC%84%A4%EA%B3%84.md)

---

## Kotlin 학습
- [Kotlin 학습 - 자바와 비교한 코틀린 기본 학습](https://quddnd.tistory.com/309)
- [Kotlin 학습 - 자바와 비교한 코틀린 심화 학습](https://quddnd.tistory.com/310)

---

## 트러블슈팅
- [코틀린과 JPA의 패러다임 불일치](https://github.com/quddaz/Stockify/blob/main/docs/%ED%8A%B8%EB%9F%AC%EB%B8%94%EC%8A%88%ED%8C%85/%EC%BD%94%ED%8B%80%EB%A6%B0%EA%B3%BC%20JPA.md)
- [스케줄러 구현의 여정](https://github.com/quddaz/Stockify/blob/main/docs/%ED%8A%B8%EB%9F%AC%EB%B8%94%EC%8A%88%ED%8C%85/%EC%8A%A4%EC%BC%80%EC%A4%84%EB%9F%AC%EA%B5%AC%ED%98%84%EC%9D%98%20%EC%97%AC%EC%A0%95.md)
- [주식 매수/매도 동시성과 메시지 큐의 양립](https://github.com/quddaz/Stockify/blob/main/docs/%ED%8A%B8%EB%9F%AC%EB%B8%94%EC%8A%88%ED%8C%85/%EC%A3%BC%EC%8B%9D%EB%A7%A4%EC%88%98%EB%A7%A4%EB%8F%84%20%EB%8F%99%EC%8B%9C%EC%84%B1%EA%B3%BC%20%EB%A9%94%EC%8B%9C%EC%A7%80%20%ED%81%90.md)
- [WebSocket과 Filter 처리](https://github.com/quddaz/Stockify/blob/main/docs/%ED%8A%B8%EB%9F%AC%EB%B8%94%EC%8A%88%ED%8C%85/WebSocket%EA%B3%BC%20Filter.md)

---

## 시연 영상

https://github.com/user-attachments/assets/a531b267-436a-44a9-a2c4-95a469fa458c

## 프로젝트 구조
