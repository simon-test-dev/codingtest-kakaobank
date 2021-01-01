# Project Convention

## Environment
1. Java version: 11.0+
    * [jenv](https://www.jenv.be/) 사용
2. Default Encoding: UTF-8
3. Default File System: Linux
4. Open API Key Required
    * `-Dopenapi.kakao.key=kakaoKey`, `-Dopenapi.naver.client.id=naverClientId`, `-Dopenapi.naver.client.secret=naverClientSecret`

---- 

# Build

- gradle wrapper 사용 `./gradlew {task}`

## Testing

1. Unit Test
```bash
./gradlew test 
```

2. test all, checkstyle, spotbugs, pmd, jacoco coverage

```bash
./gradlew check
```

## Packaging

###                            

```bash
./gradlew clean devSnapshot
```

----

### 고민

1. Architecture
    - DDD 와 Uncle Bob 에 Clean Architecture 를 차용
    - 해당 문제에 대해서 과한 모델링이나 도메인이 커지고 복잡해 질것을 감안함
2. 대용량 트래픽
    - 검색 히스토리를 DB 로 쌓는건 올지 않다고 생각함
    - 비동기로 로그처리하는 방법이 있겠으나, 구현 안함
    - 성능을 뽑으려면 DB 구조에 유리한 스키마를 작성해야 하나 제약사항이 JPA 로 한정하여, 구현 안함
    - JPA OneToMany 관계른 데이터 많아졌을때 배치사이즈 등 튜닝을 한다해도, 종극에는 비정규화해야 해서 검색 로그 테이블을 따로 구성함
    - 이것도 성능을 더 뽑으려면 테이블 파티셔닝과 통계 테이블을 활용하는 방법이 좋아보임
3. DTO 많음
    - 서비스가 커지고 오래되면 domain 모델이 외부로 노출되는것 보다 Interface Layer DTO(ViewModel) 는 필수로 생각함
4. JPA 모델링
    - 도메인 객체에 JPA 모델링을 하게되면 DB 에 의한 설계로 변질됨. SRP 위반
    - L2 Cache 로 Caffeine cache 처리해야 하나 설정만 함
    - 간단한 구현 위해 EntityManager 를 직접쓰거나 하지 않고 이름이 중복이지만 JpaRepository 를 씀
5. Domain Model
    - 회원은 명확히 Aggregate Root 로 보였으나, 장소는 현재 조건에서는 Value Object 로 색각함
    - 회원 id 같은경우 db 사용시 generate value 를 쓰는게 성능상 유리하나, 회원 id 의 생성을 db 에 맞기는건 선호하지 않아서 uuid 로 처리함.

### 요구사항

1. 회원가입/로그인
    - spring security 를 쓸까 하다가, rest api 설계라 disable 코드가 더 많을거 같아서 사용 안함
    - 토큰 방식으로 jwt 인증 처리 하였으며, url 별로 사용자가 필요한 경우만 인증처리 하기 위해서 `Interceptor` 보다는 `HandlerMethodArgumentResolver` 를 사용하여 간단히 구현
    - 토큰을 관리하거나 인증 복잡해지는 문제는 현재 없으므로 추후 고민거리
    - 인증 처리는 `SecureTokenHandlerImpl` 와 `AuthenticatedUserMethodArgumentResolver` 참조
    - 회원 가입/로그인 처리는 `SignInInteractor`, `SignInInteractor` 참조
    - DB 저장시 암호화는 `spring security` 의 `PasswordEncoder` 를 사용
2. 장소 검색
    - API 는 Feign 을 이용하여 구현
    - Feign 사용시 호출 이 카카오, 네이버 모두 장애일경우, 오류 처리함(문제에 Fallback 처리 딱히 없어서)
    - 각 API 사용시 `Hystrix` 로 Circuit Breaker 를 달았고, timeout thread pool 설정등은 기본 default 로 사용
    - `SearchPlaceInteractor` 에 로직 구현, 참조 `SearchPlaceInteractorTest`
    - endpoint 에 부하가 커졌을 경우
        1. DB 부하가 발생으로 커졌을경우 로그를 후처리(비동기 혹은 로그를 남겨 ELK 같은 solution 으로 처리) 해야함. 구현 안함
        2. 대부분 외부호출 지연이나 장애로 처리 못해서
        3. 검색호출이 반복 키워드가 많을 경우, 외부 호출 부분을 Caching 처리하고 있어서 Memory size 를 늘려준다
        4. Memory size 늘릴 수 없을경우 cache 구현채를 redis 혹은 기타 remote cache 로 처리할경우 capacity 가 늘어남
3. 내 검색 히스토리
    - 모든 히스토리를 남기고, 다음 검색 히스토리와 비슷하게 동일 키워드 입력 시 날짜를 update 하는 방식으로 구현(일단위)
4. 인기 키워드 목록
    - 호출시점에 정확한 데이터를 남기는 목적이 아니라면, 1초 cache 라도 처리해야 대량 트래픽 방지 할 수 있어서, caching 처리
    - 개인화 영역이 아니어서 인증 처리 안함
5. 테스트
    - 직접 테스트 할경우 HTTP Request file 은 `test.md` 에 명시 함
    - application 띄울때 반드시 property 로 key 세팅 필요 위 문서 참조
    - `./gradlew check` 로 cli 에서 처리하면, test, checkstyle, pmd, spotbugs, code coverage 까지 돌려볼 수 있음
6. 외부 라이브러리
    - Spring Eco System
        - [Spring Cloud Netflix Hystrix](https://github.com/spring-cloud/spring-cloud-netflix)
          > Circuit Breaker
        - [Spring Cloud Open Feign](https://github.com/spring-cloud/spring-cloud-openfeign)
          > REST API Client
    - [lombok](https://github.com/rzwitserloot/lombok)
      > Syntax Sugar
    - [problem-spring-web](https://github.com/zalando/problem-spring-web)
      > Exception Handling
    - [jjwt](https://github.com/jwtk/jjwt)
      > JWT token
