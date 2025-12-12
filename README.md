# 하루새김 (Harusaegim)

AI를 활용해 하루를 기록하고, 감정 흐름을 리포트로 확인할 수 있는 개인 일기 서비스입니다.

---

## 🔍 프로젝트 소개

**하루새김**은 사용자가 하루의 장소, 활동, 감정을 입력하면  
AI가 이를 바탕으로 자연스러운 일기를 생성하고,  
주간/월간 감정 점수 리포트를 제공하는 개인 기록 서비스입니다.

- 하루 1회 날짜 기준 일기 생성
- 감정 텍스트 기반 감정 점수 산출
- AI 기반 일기 자동 생성
- 감정 점수 기반 리포트 제공

---

## 🛠 기술 스택

- **Backend**: Java 17, Spring Boot 3
- **Frontend**: React Native
- **Persistence**: Spring Data JPA
- **Database**: MySQL
- **Auth**: Token 기반 인증
- **AI**: OpenAI API
- **Build**: Gradle

---

## ✨ 주요 기능

### 📔 일기
- 날짜 기준 하루 1회 일기 생성
- 장소 / 활동 / 감정 입력 기반 AI 일기 생성
- 일기 조회, 수정, 삭제

### 🤖 AI 일기 생성 ✨
- 날짜·장소·한 일·기분을 입력값으로 받아 OpenAI API와 연동해 일기 자동 생성
- AI 응답 실패 시에도 일기가 생성되도록 fallback 문장 처리

### 📊 감정 리포트 ✨
- 감정 텍스트에 포함된 키워드를 기반으로 가중치를 적용해 0~20 범위의 감정 점수 계산
- 일기 데이터를 날짜 기준으로 집계해 주간 / 월간 감정 점수 추이 제공

### 👤 사용자
- 회원가입 / 로그인
- 내 정보 조회 및 수정

---

## 📱 앱 화면 (UI)

### 🔐 로그인 / 회원가입
<p align="center">
  <img src="https://github.com/user-attachments/assets/8afd3d3b-8772-4482-b71d-ed856e9458d9" width="280" />
  <img src="https://github.com/user-attachments/assets/0166ec53-d00a-4ba3-abb9-69e637504f6a" width="280" />
</p>

- 이메일과 비밀번호를 이용한 기본 로그인 및 회원가입 화면

### 🏠 홈
<p align="center">
  <img src="https://github.com/user-attachments/assets/ab2083f8-117c-4faf-b1bc-003702adca9b" width="280" />
</p>

- 오늘 일기 작성 여부에 따라 안내 문구와 주요 기능 버튼 제공

### ✍️ 기록하기
<p align="center">
  <img src="https://github.com/user-attachments/assets/d9ae1370-bc00-45d6-9d36-48eb0b2e9918" width="280" />
</p>

- 날짜, 장소, 한 일, 기분을 입력해 하루를 기록하고 AI 일기 생성 요청

### 📔 일기
<p align="center">
  <img src="https://github.com/user-attachments/assets/baefdec1-309c-4060-8ab7-2a6beb4586cf" width="280" />
</p>

- 날짜별로 작성된 일기를 목록으로 확인하고 감정 점수를 함께 표시

### 📊 감정 리포트
<p align="center">
  <img src="https://github.com/user-attachments/assets/d493dfc5-6c74-484b-8f93-8b39341808a3" width="280" />
</p>

- 7일 / 30일 기준 감정 점수 변화를 간단한 그래프로 확인

