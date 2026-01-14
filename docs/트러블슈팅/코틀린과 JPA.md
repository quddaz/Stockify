# 코틀린과 JPA

저는 이번 프로젝트를 진행하면서 **코틀린과 JPA 간의 호환성 문제**를 경험했습니다. <br>
이 문서에서는 **코틀린과 JPA를 함께 사용할 때 발생할 수 있는 문제점들과 그 해결 방법**에 대해 설명하겠습니다.

---

## 왜 문제가 발생했나?

### JPA와 코틀린의 언어 철학

코틀린과 JPA가 안 맞는 이유는
**코틀린의 현대적인 언어 철학(`null` 안전성, 불변성 등)** 과
**JPA의 전통적인 ORM 설계 방식(가변성, 상속, 프록시 기반)** 이 **충돌하기 때문**입니다. <br>

예를 들어,

* **JPA**는 엔티티 클래스에 **기본 생성자**, **가변(`var`) 필드**, **open 클래스**를 요구하지만
* **코틀린**은 **불변(`val`)**, **final 클래스**, **명시적 초기화**를 선호합니다.

즉, JPA는 런타임 시점의 프록시 생성과 리플렉션 기반 접근을 위해 <br> 
**객체의 가변성(mutability)** 과 **상속 가능성(open class)** 을 요구하지만, <br>
코틀린은 컴파일 타임에 **타입 안정성(type safety)** 과 **불변 데이터 구조(immutability)** 를 강하게 보장하려는 언어입니다.

그렇기 때문에 이 두 철학의 차이로 인해 문제들이 발생합니다. 물론 JPA를 사용하지 않고 JDBC나 다른 ORM을 사용한다면 이런 문제는 발생하지 않습니다. <br>
그러나 JPA는 자바 진영에서 가장 널리 쓰이는 ORM이기 때문에, <br> 
많은 코틀린 개발자들이 JPA와 함께 사용할 때 이 문제에 직면하게 됩니다.

---


## 1. Null 안전성 문제

자바와 코틀린의 가장 큰 차이점 중 하나는 **Null 안정성(Null Safety)** 입니다.

자바는 `NullPointerException`을 방지하기 위해 명시적인 예외 처리나 `Optional` 클래스를 사용해야 합니다.
Java 8 이후에는 `Optional<T>`을 통해 Null 값을 감싸는 방식을 사용하지만, 런타임에서 여전히 NPE가 발생할 수 있습니다.

반면 코틀린은 **컴파일 타임에서 Null 가능성을 검사**하여, `NullPointerException` 발생 가능성을 근본적으로 줄입니다.

```Kotlin
@Entity
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    var name: String // null 불가
)
```

하지만 JPA는 엔티티의 필드에 `null` 값을 허용하는 경우가 많습니다.
이때 코틀린에서는 해당 필드를 **nullable(`String?`)** 혹은 `lateinit`으로 선언해야 합니다.
그렇지 않으면 컴파일러가 초기화되지 않은 프로퍼티로 판단하여 오류를 발생시킵니다.

---

#### 해결 방법

**1) `nullable` 타입(`?`) 사용**

JPA에서 `null`을 허용하는 필드는 `?`를 붙여 nullable 타입으로 선언합니다.

```kotlin
@Entity
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String? = null // null 허용
)
```

이 경우, 코틀린은 `null` 체크를 강제하므로 런타임에서의 NPE 위험을 줄일 수 있습니다.
단, 조회 시 `user.name?.length`와 같이 **세이프 콜(`?.`)** 을 반드시 사용해야 합니다.

---

**2) `lateinit` 키워드 사용**

초기화 시점이 늦는 JPA 필드(예: 연관관계 매핑 필드)는 `lateinit`으로 선언할 수 있습니다.

```kotlin
@Entity
class Post(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    lateinit var title: String,

    @ManyToOne(fetch = FetchType.LAZY)
    lateinit var author: User
)
```

`lateinit`을 사용하면 `null` 타입을 강제하지 않으면서도 JPA의 지연 초기화를 자연스럽게 처리할 수 있습니다.

---

**3) @Column(nullable = false) + 기본값 지정**

Null을 허용하지 않는 필드는 `@Column(nullable = false)`를 명시하고, 코틀린 쪽에서는 기본값을 지정하는 것이 좋습니다.

```kotlin
@Entity
class Account(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var balance: Long = 0L
)
```
이렇게 하면 JPA는 DB에 `null`을 허용하지 않으며, 코틀린에서는 기본값이 설정되어 NPE를 방지할 수 있습니다.


## 2. 기본 생성자 문제

JPA는 엔티티 객체를 **리플렉션(Reflection)** 으로 생성합니다.
즉, 개발자가 직접 `new`로 객체를 만드는 것이 아니라,
**JPA 내부에서 기본 생성자(`no-args constructor`)를 호출**하여 객체를 생성하고 필드를 주입합니다.

하지만 코틀린은 다음과 같은 이유로 이 부분에서 문제가 발생합니다

---

#### 문제 원인

코틀린에서는

* 모든 생성자를 **명시적으로 정의**해야 하며,
* **기본 생성자(default constructor)** 가 자동으로 만들어지지 않습니다.
* 또한 `val`(불변 프로퍼티)은 생성 시점에 반드시 초기화되어야 합니다.

그렇기 때문에 다음과 같은 코드는 **컴파일 에러**를 발생시킵니다.

```kotlin
@Entity
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,  // 기본 생성자에서 초기화 불가
    var name: String
)
```

JPA는 리플렉션으로 기본 생성자를 호출하려 하지만,
코틀린은 **기본 생성자가 없거나 초기화되지 않은 필드**가 있으면 객체 생성을 막기 때문입니다.

---

#### 해결 방법 1 — `kotlin-noarg` 플러그인 사용 (권장)

가장 깔끔하고 실무에서 자주 쓰는 방법은
**Kotlin의 No-Arg 플러그인**을 적용하는 것입니다.

이 플러그인은 `@Entity`, `@Embeddable`, `@MappedSuperclass` 같은 클래스에
자동으로 **기본 생성자**를 추가해줍니다.

```kotlin
plugins {
    kotlin("plugin.jpa") version "1.9.25"
}
```

이렇게 설정하면 다음과 같이 기본 생성자를 따로 작성하지 않아도 됩니다

```kotlin
@Entity
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var name: String
)
```

>  **플러그인 적용 시 장점**
>
> * 불필요한 boilerplate 코드 제거
> * JPA 리플렉션 오류 방지
> * 코틀린 불변성(`val`) 유지 가능
> 
> **플러그인 적용 시 단점** 
> 
> * 빌드 설정에 플러그인 추가 필요합니다.
> * 코틀린의 장점을 JPA 사용을 위해 일부 포기해야 할 수도 있습니다.
---

#### 해결 방법 2 — 직접 기본 생성자 정의

플러그인을 사용하지 않는 경우,
직접 **기본 생성자**를 만들어야 합니다.

```kotlin
@Entity
class User() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    var name: String? = null

    constructor(name: String) : this() {
        this.name = name
    }
}
```

> **단점:** 코드가 장황해지고,
> 코틀린의 불변성을 유지하기 어렵습니다.
> 따라서 가능하면 `noarg` 플러그인 사용을 권장합니다.

---

## 3. 프록시 및 상속 문제
JPA는 **지연 로딩(Lazy Loading)** 을 위해 **프록시(Proxy)** 객체를 사용합니다.
이는 런타임에 엔티티 클래스를 상속받아 동적으로 프록시 클래스를 생성하는 방식입니다.
코틀린의 클래스는 기본적으로 `final`이기 때문에, JPA가 프록시 클래스를 생성하지 못하는 문제가 발생합니다.

#### 해결 방법
코틀린 클래스와 메서드를 `open`으로 선언하여 JPA가 프록시를 생성할 수 있도록 해야 합니다.
    
```kotlin
@Entity
open class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long? = null,
        
    open var name: String
)
```

또는 `kotlin-jpa` 플러그인을 사용하여 자동으로 `open` 클래스를 생성할 수도 있습니다.
    
```kotlin
plugins {
    kotlin("plugin.jpa") version "1.9.25"
}
```

이 플러그인은 JPA 엔티티에 대해 자동으로 `open` 클래스를 생성하여 프록시 문제를 해결합니다.

---

### 3. @Entity와 data class 충돌 문제
코틀린의 `data class`는 자동으로 `equals()`, `hashCode()`, `toString()` 메서드를 생성합니다. <br>
하지만 JPA에서 **지연 로딩**을 사용하는 경우, 프록시 객체와 실제 엔티티 객체 간의 비교가 올바르게 작동하지 않을 수 있습니다. <br>
따라서 JPA 엔티티에는 `data class`를 사용하지 않는 것이 좋습니다.
---

## 결론
코틀린과 JPA를 함께 사용할 때 발생하는 주요 문제들은 **Null 안전성, 기본 생성자, 프록시 및 상속 문제**입니다. <br>
이 문제는 Java와 Kotlin의 언어 철학 차이에서 기인합니다. <br>
적절한 플러그인 사용과 코틀린의 기능을 이해함으로써 이러한 문제들을 효과적으로 해결할 수 있습니다. <br>
또한 적절한 val, var 사용과 기본 생성자 정의를 통해 JPA와 코틀린의 조화를 이룰 수 있습니다.
