# Astarchia (아스타키아) ⭐

> 기록들이 담긴 공간

##  프로젝트 소개

**Astarchia**는 개인 워크스페이스와 발행 블로그가 결합된 하이브리드 플랫폼입니다.

### 주요 특징
-  마크다운 기반 글쓰기
-  임시저장 / 발행 시스템
-  카테고리, 태그, 시리즈 관리
-  공개/비공개 설정
- ️ 이미지 업로드

---

## 🛠 기술 스택

### Backend
- **Language**: Java 21
- **Framework**: Spring Boot 3.x
- **ORM**: Spring Data JPA
- **Database**: MySQL (개발: H2)
- **Build Tool**: Gradle
- **Authentication**: JWT

### 패키지 구조
```
도메인 중심 설계 (Domain-Driven Design)
- domain/user
- domain/post
- domain/category
- domain/series
- domain/tag
- global (공통 기능)
```

---

##  데이터베이스 설계

### ERD 구조 (7개 테이블)
1. **Users** - 회원
2. **Post** - 게시글
3. **Category** - 카테고리
4. **Series** - 시리즈
5. **Tag** - 태그
6. **PostTag** - 게시글-태그 중간 테이블
7. **Image** - 이미지

### 주요 관계
- User → Post (1:N)
- User → Category (1:N)
- User → Series (1:N)
- Post ↔ Tag (N:M via PostTag)

 상세 ERD: [ERD JSON 파일](./erd.json)

---

## 🎯 개발 로드맵

### 1단계: 개인 블로그 (MVP) - **현재 진행 중**
- [x] ERD 설계
- [x] Entity 클래스 작성
- [x] Repository 인터페이스 작성
- [ ] Service 계층 구현
- [ ] Controller & REST API
- [ ] JWT 인증/인가
- [ ] 게시글 CRUD
- [ ] 임시저장/발행 시스템
- [ ] 카테고리, 태그, 시리즈
- [ ] 검색 기능
- [ ] 이미지 업로드

### 2단계: 소셜 기능
- [ ] 팔로우/팔로워
- [ ] 댓글 시스템
- [ ] 좋아요/반응
- [ ] 알림
- [ ] 피드

### 3단계: 네트워킹 (커피챗)
- [ ] 커피챗 신청/관리
- [ ] 실시간 채팅 (WebSocket)
- [ ] 일정 관리
- [ ] 후기 시스템

---

## 📂 프로젝트 구조

```
src/main/java/com/astarchia/
│
├── domain/                    # 도메인별 구분
│   ├── user/
│   │   ├── entity/           # User.java
│   │   ├── dto/              # DTO (Request/Response)
│   │   ├── repository/       # UserRepository.java
│   │   ├── service/          # UserService.java
│   │   └── controller/       # UserController.java
│   │
│   ├── post/
│   ├── category/
│   ├── series/
│   ├── tag/
│   └── image/
│
├── global/                    # 공통 기능
│   ├── config/               # 설정 (Security, JPA)
│   ├── exception/            # 예외 처리
│   └── util/                 # 유틸리티 (JWT 등)
│
└── AstarchiaApplication.java
```

---

## ✅ 완료된 작업

### Entity 설계 (7개)
```java
✅ Users.java
✅ Post.java
✅ Category.java
✅ Series.java
✅ Tag.java
✅ PostTag.java
✅ Image.java
```

### Repository (7개)
```java
✅ UserRepository.java
✅ PostRepository.java
✅ CategoryRepository.java
✅ SeriesRepository.java
✅ TagRepository.java
✅ PostTagRepository.java
✅ ImageRepository.java
```

### Enum 타입
```java
✅ PostStatus (DRAFT, PUBLISHED)
✅ Visibility (PUBLIC, PRIVATE)
```

---

## 🚀 다음 작업

### Phase 1: 기본 세팅
1. application.yml 설정
2. H2 Database 연결 확인
3. 테이블 자동 생성 확인

### Phase 2: 인증 시스템
1. Spring Security 설정
2. JWT 유틸리티 클래스
3. UserService (회원가입, 로그인)
4. UserController (API)

### Phase 3: 게시글 기능
1. PostService (CRUD)
2. PostController (REST API)
3. 임시저장/발행 로직
4. Postman 테스트

---

## 🎓 주요 기술 결정

### Entity 설계 원칙
- **Builder 패턴** 사~~용 (`@Builder`)
- **생성자 접근 제한** (`@NoArgsConstructor(PROTECTED)`)
- **연관관계**는 객체 참조 (FK가 아닌 Entity 객체)
  ```java
  // ❌ private Long userId;
  // ✅ private User author;
  ```

### DTO 설계
- **Request/Response 분리**
    - `UserCreateRequestDTO`
    - `UserResponseDTO`

### 날짜/시간
- `LocalDateTime` 사용 (날짜+시간)
- `@CreationTimestamp` (자동 생성일)
- `@UpdateTimestamp` (자동 수정일)

---

##  참고 문서

- [개발 가이드](./docs/DEVELOPMENT.md)
- [ERD 상세](./docs/ERD.md)
- [학습 노트](./docs/LEARNING.md)

---~~

##  개발자

**성민 (sungmin)**
- Backend Developer
- Spring Boot, JPA 학습 중

---

##  메모

### 프로젝트 이름 의미
- **Astar** (astrum, 별) - 영원히 빛나는
- **archi** (archive/architecture) - 기록보관실
- **ia** (국명 명사어미) - 공간

→ "별처럼 영원히 빛나는 기록들이 담긴 공간"

---

**Last Updated**: 2025-11-10