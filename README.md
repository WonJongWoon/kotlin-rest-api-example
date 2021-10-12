# Spring Boot Kotlin Example

사용한 기술

- Kotlin (언어)
- Kotest (코틀린스러운 테스트 코드 작성을 도와주는 테스트 프레임워크)
- Spring Boot (스프링 프레임워크를 기반으로 한 스프링 라이브러리)
- Spring Rest Docs (테스트 코드 기반으로 API 문서를 쉽게 만들어주는 라이브러리)
- Kolint (Kotlin Coding Convention을 지키게 해주는 Lint)
- Kotlin DSL (Build Tools)

Kotlin Lint - Ktlint 설정 방법 - [링크](https://wonjongwoon.github.io/kotlin/ktlint/)

Spring Rest Docs + Kotest + Kotlin 적용 설명 - [링크](https://wonjongwoon.github.io/spring/kotlin/spring-rest-doc/)

데이터베이스 설정 - `src/resources/application.yml`

```
spring:
    datasource:
        driver-class-name: org.h2.Driver
        username: sa 
        password: 
        url: jdbc:h2:tcp://localhost/~/study_kotlin
```
