# Astarchia 학습 노트

프로젝트를 진행하면서 배운 내용을 정리합니다.

---

##  JPA (Java Persistence API)

### Entity 설계 핵심 개념

#### 1. 연관관계 매핑

**@ManyToOne (N:1 관계)**
```java
// Post 입장: 게시글(N) → 작성자(1)
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id")  // DB 컬럼명
private User author;            // Java 객체
```

**핵심 포인트:**
- FK를 `Long userId`가 아닌 **객체 `User author`**로 선언
- `@JoinColumn(name = "user_id")`로 실제 DB 컬럼명 지정
- `fetch = FetchType.LAZY`: 실제 사용할 때까지 로딩 지연 (성능 최적화)

**@OneToMany (1:N 관계) - 역방향 (선택)**
```java
// User 입장: 사용자(1) → 게시글(N)
@OneToMany(mappedBy = "author")
private List<Post> posts;
```
- 보통 **단방향 (@ManyToOne만)** 사용 권장
- 양방향은 복잡도 증가

---

#### 2. N:M 관계 (다대다)

**문제:** Post와 Tag는 서로 여러 개 연결 가능
- 한 게시글에 여러 태그
- 한 태그가 여러 게시글에 사용

**해결:** 중간 테이블 (PostTag) 생성
```java
// PostTag.java
@ManyToOne
@JoinColumn(name = "post_id")
private Post post;

@ManyToOne
@JoinColumn(name = "tag_id")
private Tag tag;
```

**왜 중간 테이블?**
- 관계 자체에 추가 정보 저장 가능 (예: 태그 추가 시간)
- 명시적 관계 관리

---

#### 3. FetchType (로딩 전략)

**LAZY (지연 로딩) - 추천**
```java
@ManyToOne(fetch = FetchType.LAZY)
private User author;
```
- 실제 사용할 때까지 로딩 안 함
- 성능 좋음
- **주의:** N+1 문제 발생 가능

**EAGER (즉시 로딩)**
```java
@ManyToOne(fetch = FetchType.EAGER)
private User author;
```
- Entity 조회 시 즉시 로딩
- 불필요한 조인 발생 가능
- 웬만하면 사용 X

---

### Builder 패턴

**왜 Builder를 쓰나?**
```java
//  생성자: 순서 헷갈림, 가독성 나쁨
User user = new User(
    null, "test@test.com", "password", 
    "login123", "닉네임", null, null
);

//  Builder: 명확하고 가독성 좋음
User user = User.builder()
    .email("test@test.com")
    .password("password")
    .loginId("login123")
    .nickname("닉네임")
    .build();
```

**Lombok 어노테이션:**
```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA용
@AllArgsConstructor(access = AccessLevel.PRIVATE)   // Builder용
@Builder
public class User {
    // ...
}
```

**왜 생성자 접근 제한?**
- `PROTECTED`: JPA가 내부적으로 사용
- `PRIVATE`: 외부에서 직접 생성 불가 → Builder로만 생성

---

### 날짜/시간 처리

**LocalDateTime vs LocalTime vs LocalDate**
```java
LocalDate date = LocalDate.now();         // 2025-11-10
LocalTime time = LocalTime.now();         // 14:30:00
LocalDateTime dateTime = LocalDateTime.now(); // 2025-11-10T14:30:00
```

**Entity에서 사용:**
```java
@CreationTimestamp  // 생성 시 자동 설정
@Column(updatable = false)
private LocalDateTime createdAt;

@UpdateTimestamp    // 수정 시 자동 업데이트
private LocalDateTime updatedAt;
```

---

##  Spring Security

### JWT (JSON Web Token)

**왜 JWT를 쓰나?**
- **Stateless**: 서버에 세션 저장 안 함
- **확장 가능**: 여러 서버에서 사용 가능
- **모바일 친화적**: 쿠키 없이 사용

**JWT 구조:**
```
Header.Payload.Signature
```

**JWT 흐름:**
```
1. 로그인 성공 → JWT 발급
2. 클라이언트: 요청마다 Header에 JWT 포함
   Authorization: Bearer {token}
3. 서버: JWT 검증 → 인증 완료
```

---

### BCrypt 암호화

**왜 암호화?**
- 비밀번호를 평문으로 DB 저장하면 위험
- BCrypt: 단방향 해시 (복호화 불가)

**사용법:**
```java
// 암호화
String encodedPassword = passwordEncoder.encode("원본비밀번호");

// 검증
boolean matches = passwordEncoder.matches("입력비밀번호", "저장된해시");
```

---

##  DTO (Data Transfer Object)

### Request/Response 분리

**왜 분리?**
1. **보안**: Entity에 있는 민감 정보 노출 방지
2. **유연성**: API 응답 형식 자유롭게 변경
3. **검증**: 입력값 검증 어노테이션 추가

**Request DTO:**
```java
@Data
public class UserCreateRequestDTO {
    @NotBlank
    @Email
    private String email;
    
    @NotBlank
    @Size(min = 8)
    private String password;
    
    @NotBlank
    private String nickname;
}
```

**Response DTO:**
```java
@Data
public class UserResponseDTO {
    private Long userId;
    private String email;
    private String nickname;
    // password 제외! (보안)
    
    // Entity → DTO 변환 메서드
    public static UserResponseDTO from(User user) {
        return UserResponseDTO.builder()
            .userId(user.getUserId())
            .email(user.getEmail())
            .nickname(user.getNickname())
            .build();
    }
}
```

---

##  아키텍처 패턴

### Layered Architecture

```
Controller (표현 계층)
    ↓ 요청/응답
Service (비즈니스 로직)
    ↓ 데이터 처리
Repository (데이터 접근)
    ↓ SQL
Database
```

**각 계층의 역할:**
- **Controller**: HTTP 요청 받아서 Service 호출, 응답 반환
- **Service**: 비즈니스 로직 처리, 트랜잭션 관리
- **Repository**: DB CRUD

---

##  실수 기록 & 해결

###  실수 1: FK를 Long으로 선언
```java
// 잘못된 방법
@Entity
public class Post {
    private Long userId;  // ❌
}
```

**문제점:**
- JPA 연관관계 기능 사용 불가
- 객체 지향적이지 않음

**올바른 방법:**
```java
@Entity
public class Post {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;  // 
}
```

**교훈:** JPA는 객체 지향 → FK도 객체로!

---

###  실수 2: LocalTime으로 날짜+시간 저장
```java
private LocalTime createdAt;  // ❌ 시간만 저장됨
```

**문제점:**
- `14:30:00`만 저장, 날짜 없음

**올바른 방법:**
```java
private LocalDateTime createdAt;  // 2025-11-10T14:30:00
```

**교훈:** 날짜+시간 필요하면 LocalDateTime!

---

###  실수 3: Entity 직접 반환
```java
@GetMapping("/users/{id}")
public User getUser(@PathVariable Long id) {
    return userService.findById(id);  // ❌
}
```

**문제점:**
- 비밀번호 등 민감 정보 노출
- Entity 구조 변경 시 API 영향

**올바른 방법:**
```java
@GetMapping("/users/{id}")
public UserResponseDTO getUser(@PathVariable Long id) {
    User user = userService.findById(id);
    return UserResponseDTO.from(user);  // ✅
}
```

**교훈:** Entity는 절대 직접 반환하지 말고 DTO 사용!

---

##  용어 정리

### JPA 관련
- **Entity**: DB 테이블과 매핑되는 클래스
- **Repository**: DB CRUD 담당 인터페이스
- **JoinColumn**: FK 컬럼 지정
- **FetchType**: 연관 Entity 로딩 시점 설정
- **Cascade**: 연관 Entity도 함께 저장/삭제

### Spring 관련
- **Bean**: Spring이 관리하는 객체
- **DI (Dependency Injection)**: 의존성 주입
- **IoC (Inversion of Control)**: 제어의 역전
- **AOP (Aspect Oriented Programming)**: 관점 지향 프로그래밍

### 디자인 패턴
- **Builder**: 객체 생성 패턴
- **DTO**: 계층 간 데이터 전달 객체
- **Repository**: 데이터 접근 추상화

---

##  유용한 자료

### 공식 문서
- [Spring Boot 공식](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Hibernate (JPA 구현체)](https://hibernate.org/)

### 추천 강의/블로그
- 김영한 - 스프링 부트와 JPA
- Baeldung - Spring 튜토리얼
- 인프런 - Spring Boot 강의

### 도구
- [H2 Console](http://localhost:8080/h2-console)
- [Postman](https://www.postman.com/) - API 테스트
- [ERD Cloud](https://www.erdcloud.com/) - ERD 설계

---

##  학습 중 질문들

### Q1: @ManyToOne에서 왜 fetch = LAZY를 쓰나요?
**A:** 성능 최적화 때문입니다.
- Post를 조회할 때 User도 항상 필요한 건 아님
- 필요할 때만 조회하면 불필요한 쿼리 줄일 수 있음
- 단, N+1 문제 주의 (fetch join으로 해결)

### Q2: Builder와 생성자 차이는?
**A:**
- 생성자: 순서 지켜야 함, 파라미터 많으면 헷갈림
- Builder: 명확한 이름으로 값 설정, 선택적 파라미터 가능

### Q3: Entity에 Setter를 안 쓰는 이유?
**A:** 불변성 유지 때문
- Setter 있으면 아무데서나 값 변경 가능
- 의도치 않은 변경 방지
- 비즈니스 로직을 Entity 메서드로 캡슐화

---

##  다음에 학습할 것

### 우선순위 높음
- [ ] Spring Security 동작 원리
- [ ] JWT 상세 구조
- [ ] JPA N+1 문제 & 해결 (fetch join)
- [ ] 트랜잭션 (@Transactional)

### 나중에
- [ ] QueryDSL (복잡한 쿼리)
- [ ] Redis (캐싱)
- [ ] Docker (배포)
- [ ] CI/CD

---

**Last Updated**: 2025-11-10