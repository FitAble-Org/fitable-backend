# 경증 장애인을 위한 맞춤형 운동 및 시설 추천 어플리케이션

**_Empowering Lives Through Personalized Fitness and Accessibility_**


---

## 🌟 **프로젝트 개요**

**FitAble**은 경증 장애인을 대상으로 한 맞춤형 운동 및 시설 추천 서비스입니다.  
사용자의 장애 유형, 등급, 성별, 나이를 고려하여 **가정 운동 추천**, **시설 및 강좌 추천**, **스케줄 관리**를 제공합니다.  
AI 기반의 솔루션과 공공 데이터를 활용하여 운동 루틴을 최적화하고,  
사용자에게 가장 적합한 운동 및 접근 가능한 시설을 추천합니다.

---
<img src="https://github.com/user-attachments/assets/ca4db322-5c94-49ad-bea5-7b6a2af98fbc" width="800">
<img src="https://github.com/user-attachments/assets/205a1e3e-5be6-423f-b1bc-7c9c3d1cb05b" width="400">


## 🏆 **주요 기능**

### 1. **회원가입**
- 사용자 정보를 입력받아 맞춤형 서비스를 제공합니다:
  - **기본 정보**: 나이, 성별
  - **장애 정보**: 유형, 등급
  - **계정 정보**: 아이디, 비밀번호

---

### 2. **홈 화면**
- **운동 기록 관리**:
  - 달력을 통해 날짜별 운동 기록 확인
  - 실내/실외 운동 구분 및 운동 시간 표시

---

### 3. **가정 운동 페이지**
- 사용자 맞춤형 **운동 추천**:
  - **GPT 기반** 응답을 통해 실시간 맞춤 운동 솔루션 제공
  - **장애인 체력 측정 데이터 API**를 활용한 운동 추천  
    [API 링크](https://www.bigdata-culture.kr/bigdata/user/data_market/detail.do?id=37c48c00-151f-11ec-bbc0-d7035fffebeb)
  - 운동 이름, 세부 단계(준비, 본 운동, 마무리), 운동 순위 정보 제공
- 운동 기록 추가
- **유튜브 참고 영상** 옵션 제공

---

### 4. **시설 운동 페이지**
- **스포츠 강좌 및 시설 추천**:
  - 반경 내 추천 시설 검색
  - **GPT 기반** 응답으로 사용자 선호도와 요구에 따른 시설 추출
  - **KakaoMap API**를 활용한 지도 표시  
    - 지도에 마커로 시설 정보 표시  
    - 마커 터치 시 시설 정보 및 강좌 내용 제공  
    - KakaoMap 웹뷰를 통한 **길찾기 기능** 제공  
    [KakaoMap API](https://developers.kakao.com/docs/latest/ko/local/dev-guide)
- **스포츠 강좌 이용권 정보 API** 활용  
  [API 링크](https://www.bigdata-culture.kr/bigdata/user/data_market/detail.do?id=35f861b0-2594-11eb-af9a-4b03f0a582d6)
- **주변 장애인 교통시설 정보 API** 활용 (옵션)  
  [API 링크](https://www.bigdata-culture.kr/bigdata/user/data_market/detail.do?id=914ac658-d64b-4fc9-add5-9773393bbe51)

---

## 🚀 **기술 스택**

| **분류**             | **기술 및 도구**                                                                                     |
|-----------------------|-----------------------------------------------------------------------------------------------------|
| **Languages**         | `Java`, `JavaScript`                                                                               |
| **Backend Frameworks**| `Spring Boot`, `Spring MVC`, `Spring Data JPA`, `Spring Security`             |
| **Frontend Frameworks**| `Vue.js`                                                                                          |
| **Database**          | `MySQL`, `Redis`, `AWS RDS`                                                                        |
| **DevOps & Cloud**    | `Jenkins`, `Docker`, `AWS EC2`, `AWS RDS`                                                          |

---

## 📊 **API 활용**

| **API 이름**                    | **설명**                                                                                 | **링크**                                                                                   |
|----------------------------------|-----------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------|
| **장애인 체력측정 추천 데이터**  | 장애인 체력 측정 데이터를 활용한 운동 추천                                              | [API 링크](https://www.bigdata-culture.kr/bigdata/user/data_market/detail.do?id=37c48c00-151f-11ec-bbc0-d7035fffebeb) |
| **스포츠 강좌 정보 API**         | 스포츠 강좌 및 시설 데이터 제공                                                         | [API 링크](https://www.bigdata-culture.kr/bigdata/user/data_market/detail.do?id=35f861b0-2594-11eb-af9a-4b03f0a582d6) |
| **장애인 교통시설 정보 API**     | 반경 내 장애인 교통시설 정보 제공                                                       | [API 링크](https://www.bigdata-culture.kr/bigdata/user/data_market/detail.do?id=914ac658-d64b-4fc9-add5-9773393bbe51) |
| **KakaoMap API**                 | 시설 위치와 길찾기, 지도 정보 제공                                                      | [API 링크](https://developers.kakao.com/docs/latest/ko/local/dev-guide)                   |


