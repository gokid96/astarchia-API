# Astarchia 개발 가이드

## 현재 진행 상황

###  완료된 작업

#### 1. ERD 설계 (2025-11-06)
- 7개 테이블 설계 완료
- 관계 설정 (1:N, N:M)
- ERD JSON 파일 저장

#### 2. Entity 클래스 작성 (2025-11-07)
**도메인별 Entity 구현:**
- [x] `domain/user/entity/Users.java`
- [x] `domain/post/entity/Post.java`
- [x] `domain/category/entity/Category.java`
- [x] `domain/series/entity/Series.java`
- [x] `domain/tag/entity/Tag.java`
- [x] `domain/tag/entity/PostTag.java`
- [x] `domain/image/entity/Image.java`

**Enum 타입:**
- [x] `domain/post/entity/PostStatus.java` (DRAFT, PUBLISHED)
- [x] `domain/category/entity/Visibility.java` (PUBLIC, PRIVATE)

#### 3. Repository 인터페이스 (2025-11-07)
- [x] `UserRepository`
- [x] `PostRepository`
- [x] `CategoryRepository`
- [x] `SeriesRepository`
- [x] `TagRepository`
- [x] `PostTagRepository`
- [x] `ImageRepository`

---

###  진행 중

**없음** - 다음 단계 준비 중

---

###  다음 작업 계획

#### Phase 1: 기본 환경 설정 (1일 예상)
```
□ application.yml 작성
  - H2 Database 설정
  - JPA 설정
  - 로깅 설정

□ 프로젝트 실행 확인
  - Spring Boot 앱 실행
  - H2 Console 접속
  - 테이블 자동 생성 확인

□ Git 초기 설정
  - .gitignore 설정
  - 첫 커밋
```

#### Phase 2: 인증 시스템 (3-4일 예상)
```
□ Spring Security 기본 설정
  - SecurityConfig.java
  - CORS 설정

□ JWT 유틸리티
  - JwtUtil.java (토큰 생성/검증)
  - JwtAuthenticationFilter

□ User 도메인
  - UserService (회원가입, 로그인)
  - UserController (REST API)
  - DTO (Request/Response)
  
□ 테스트
  - Postman으로 회원가입/로그인 테스트
```

#### Phase 3: 게시글 CRUD (3-4일 예상)
```
□ Post 도메인
  - PostService (CRUD, 임시저장/발행)
  - PostController
  - DTO 설계

□ 연관 기능
  - Category 관리
  - Tag 관리
  - Series 관리

□ 테스트
  - 게시글 작성/조회/수정/삭제
  - 임시저장 → 발행 전환
```

---

##  각 단계별 세부 작업

### Phase 2 상세: 인증 시스템

#### 1. Spring Security 설정
```java
// SecurityConfig.java 작성
- HTTP Basic 비활성화
- CSRF 비활성화
- JWT 필터 추가
- 특정 경로 인증 제외 (/api/auth/**, /h2-console/**)
```

#### 2. JWT 유틸리티
```java
// JwtUtil.java
- generateToken(String username)
- validateToken(String token)
- getUsernameFromToken(String token)
```

#### 3. UserService
```java
// 회원가입
- 이메일 중복 체크
- 비밀번호 암호화 (BCrypt)
- User 저장

// 로그인
- 이메일/비밀번호 검증
- JWT 토큰 발급
```

#### 4. UserController
```
POST /api/auth/signup  - 회원가입
POST /api/auth/login   - 로그인
GET  /api/users/me     - 내 정보 (인증 필요)
```

---

### Phase 3 상세: 게시글 기능

#### 1. PostService
```java
// 기본 CRUD
- createPost (임시저장)
- publishPost (발행)
- updatePost
- deletePost
- getPost
- getPostsByUser

// 상태 전환
- draftToPublished (임시저장 → 발행)
```

#### 2. PostController
```
POST   /api/posts          - 게시글 작성 (임시저장)
POST   /api/posts/{id}/publish - 발행
GET    /api/posts/{id}     - 조회
PUT    /api/posts/{id}     - 수정
DELETE /api/posts/{id}     - 삭제
GET    /api/posts          - 목록 조회
```

---

## 개발 환경

### 필수 도구
- IntelliJ IDEA
- Java 21
- MySQL (or H2 for 개발)
- Postman (API 테스트)
- Git

### Gradle Dependencies
```gradle
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- H2 Database (개발)
- MySQL Connector (운영)
- Lombok
- JWT (jjwt)
```

---

## 코딩 컨벤션

### 패키지 네이밍
```
com.astarchia.domain.{도메인명}.{레이어}

예시:
com.astarchia.domain.user.entity
com.astarchia.domain.user.repository
com.astarchia.domain.user.service
```

### 클래스 네이밍
```
Entity:      User, Post, Category
Repository:  UserRepository, PostRepository
Service:     UserService, PostService
Controller:  UserController, PostController
DTO:         UserCreateRequestDTO, UserResponseDTO
```

### 메서드 네이밍
```
- findById, findAll, findBy{속성명}
- create, update, delete
- existsBy{속성명}
- countBy{속성명}
```

---

##  트러블슈팅 기록

### 문제 1: Entity의 FK 설계
**문제:**
```java
 private Long userId;  // FK를 숫자로
```

**해결:**
```java
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "user_id")
   private User author;  // 객체 참조
```

**교훈:** JPA는 객체 지향. FK는 @JoinColumn으로 처리하고, 필드는 객체로 선언.

---

### 문제 2: LocalTime vs LocalDateTime
**문제:**
```java
  private LocalTime createdAt;  // 시간만 저장
```

**해결:**
```java
  private LocalDateTime createdAt;  // 날짜+시간
```

**교훈:** 날짜와 시간이 모두 필요하면 LocalDateTime 사용.

---

## 📚 학습 자료

### Spring Boot
- [Spring Boot 공식 문서](https://spring.io/projects/spring-boot)
- [Spring Data JPA 가이드](https://spring.io/guides/gs/accessing-data-jpa/)

### JWT
- [JWT 공식 사이트](https://jwt.io/)
- [Spring Security + JWT 가이드](https://www.baeldung.com/spring-security-jwt)

### JPA
- [JPA 기본 개념](https://docs.oracle.com/javaee/7/tutorial/persistence-intro.htm)
- [연관관계 매핑](https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#associations)

---

##  메모

### 중요한 결정 사항
1. **도메인 중심 패키지 구조** 선택
    - 장점: 도메인별로 응집도 높음
    - 단점: 레이어별 구조에 익숙한 사람은 적응 필요

2. **H2 Database 우선 사용**
    - 개발 단계에서 빠른 프로토타이핑
    - 추후 MySQL로 전환 예정

3. **DTO Request/Response 분리**
    - 입력/출력 명확히 구분
    - 보안 (비밀번호 등 민감정보 제외)

---

##  관련 문서
- [README.md](../README.md) - 프로젝트 개요
- [ERD.md](./ERD.md) - 데이터베이스 설계
- [LEARNING.md](./LEARNING.md) - 학습 내용 정리

---

**Last Updated**: 2025-11-10