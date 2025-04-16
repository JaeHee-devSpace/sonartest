# TechSemina - sonarQube를 이용한 자동 코드 테스트


## 1. 프로젝트 개요


### 🔹 주제 선정 이유

- 동적 테스트 도구에만 익숙해 있지만, 정적 코드 분석 도구인 **SonarQube**도 코드 품질 관리에 중요하다는 점을 소개하고자 함
    
- **코드 리뷰 자동화 및 품질 진단 효율성 향상**을 통해 개발 시간 절감
    
- **CI/CD 파이프라인에 정적 분석 도구를 연동하는 방안**을 제안하여, 테스트 부담을 줄이고 인프라 구축에 집중할 수 있는 개발 환경 조성
    

### 🔹 기대 효과

- 코드 품질 지표의 정량적 관리
    
- 보안 취약점 사전 예방
    
- 코드 리뷰 효율성 향상 및 자동화 기반 확보


---

## 🔎 2. SonarQube란?

**SonarQube**는 정적 코드 분석 툴로, **코드 품질을 자동으로 검사하고 시각화**해주는 도구입니다. 개발자가 작성한 코드의 다음과 같은 요소들을 자동으로 분석합니다:

### ✅ 주요 기능

| 항목                             | 설명                   | 예시                                      |
| ------------------------------ | -------------------- | --------------------------------------- |
| **버그 (Bugs)**                  | 실행 시 에러를 유발할 수 있는 코드 | `String s = null; s.length();`          |
| **코드 스멜 (Code Smells)**        | 유지보수가 어려운 코드 구조      | 너무 긴 메소드, 중복된 조건문                       |
| **보안 취약점 (Vulnerabilities)**   | 보안 위협이 있는 코드         | `String pwd = "admin123";` (하드코딩된 비밀번호) |
| **보안 핫스팟 (Security Hotspots)** | 검토가 필요한 보안 관련 코드     | 사용자 입력을 직접 로그에 출력하거나 SQL 쿼리에 삽입         |
| **중복 코드 (Duplications)**       | 코드 중복률               | 여러 클래스에 같은 유틸리티 메소드가 반복됨                |
| **테스트 커버리지 (Coverage)**        | 테스트 커버리지             | 전체 100줄 중 60줄만 테스트 → 60% 커버리지           |



### 🧠 SonarQube가 왜 필요할까?

1. **코드 품질을 자동으로 점검**해주기 때문에 실수나 문제를 사전에 발견할 수 있어요.
    
2. **지속적인 품질 관리(Clean Code)** → CI/CD 파이프라인에 넣으면, 배포 전에 코드 품질을 자동으로 확인 가능.
    
3. 팀 전체의 **코딩 표준 유지**, 리뷰 효율성 향상.
    
4. 오픈소스 or 엔터프라이즈 환경 모두 지원.
    


### 🛠️ 지원 언어 및 도구

- Java, Python, JavaScript, TypeScript, C#, Kotlin 등 다수
    
- Gradle, Maven, GitHub Actions, Jenkins 등과 연동 가능
    


---

## 3. 🖥️ 기본 구성 요소

- **SonarQube Server**: 웹 UI와 분석 결과 저장
    
- **Scanner (분석기)**: 실제 코드를 검사해서 결과를 전송
    
- **Database**: 분석 결과 저장소 (PostgreSQL 등)

### 1. **SonarQube Server**

- 📌 **무엇인가요?**  
    SonarQube의 **중심이 되는 서버**로, 웹 UI를 제공하며 분석 결과를 시각화하고 저장합니다.
    
- 🔧 **어떻게 쓰이나요?**
    
    - 개발자는 웹 브라우저를 통해 **분석 결과, 커버리지, 코드 스멜, 버그 등**을 확인
        
    - 관리자나 팀원들은 여기서 **품질 기준 설정, 프로젝트 관리**를 수행
        
    - REST API도 제공되어 외부 툴과 연동 가능 (예: Slack, Jenkins 알림 등)
        

### 2. **Scanner (분석기)**

- 📌 **무엇인가요?**  
    실제로 코드를 읽고 분석한 다음, 그 결과를 SonarQube Server에 전송하는 **클라이언트 도구**입니다.
    
- 🔧 **어떻게 쓰이나요?**
    
    - Gradle/Maven/NPM/Jenkins 등에서 분석 명령을 실행할 때 작동
        
        bash
        
        복사편집
        
        `./gradlew sonarqube`
        
    - 이 명령이 Scanner를 실행 → **소스코드, 테스트, 커버리지 리포트, 코드 이슈** 등을 분석
        
    - 결과는 Server로 전송되어 UI에 표시됨
        

### 3. **Database**

- 📌 **무엇인가요?**  
    SonarQube가 분석한 모든 데이터와 설정을 저장하는 **중앙 저장소**입니다.  
    주로 PostgreSQL, MySQL, Oracle 등을 사용합니다.
    
- 🔧 **어떻게 쓰이나요?**
    
    - **프로젝트 분석 기록, 사용자 설정, 품질 프로파일, 대시보드 데이터** 등을 저장
        
    - Server가 실행될 때 이 DB를 참조하여 모든 정보를 불러오고 갱신함
        
    - 장기적으로 여러 프로젝트 품질의 추이를 관리할 수 있음

### 🏆 SonarQube에서 PostgreSQL을 주로 사용하는 이유

- 공식 권장 (Recommended by SonarSource)

- SonarQube 공식 문서에서 PostgreSQL을 **가장 먼저**, **기본 예제로** 소개
    
- 버전 간 호환성, 성능, 유지보수 이슈가 적어서 신뢰도 높음
    


## ⚖️ 비교: PostgreSQL vs MySQL vs Oracle

|항목|**PostgreSQL**|**MySQL**|**Oracle**|
|---|---|---|---|
|🔧 **SonarQube 지원**|✅ 공식 지원 & 권장|✅ 공식 지원|✅ 공식 지원 (Enterprise에서 주로 사용)|
|🚀 **성능**|고성능, 대용량 처리에 강함|읽기 성능 우수, 쓰기는 상대적으로 약함|최고 성능, 하지만 무겁고 비쌈|
|📐 **표준 SQL 호환성**|✅ 매우 높음|❌ 낮음 (제약 많음)|✅ 높음|
|🧠 **기능성**|트리거, JSON, 윈도우 함수 등 고급 기능 풍부|상대적으로 단순|모든 기능 가능 (비용도 큼)|
|💰 **라이선스**|오픈소스 (무료)|오픈소스 (GPL)|유료|
|🔧 **설치 및 구성**|간단 (Docker로 바로 가능)|간단|복잡, 설치 어려움|
|🧩 **확장성**|높은 편|중간|매우 높음|
|🔄 **커뮤니티 & 자료**|활발|



---

## ✅ 주요 정적 분석 도구 비교

| 항목               | **SonarQube**           | **Checkstyle** | **PMD**          | **FindBugs / SpotBugs** | **ESLint**      | **Coverity**     |
| ---------------- | ----------------------- | -------------- | ---------------- | ----------------------- | --------------- | ---------------- |
| **언어 지원**        | 다수 (Java, JS, Python 등) | Java 중심        | Java, JS, Apex 등 | Java 중심                 | JS/TS           | C/C++, Java 등    |
| **분석 범위**        | 버그, 보안, 스멜, 커버리지        | 코드 스타일         | 코드 스멜, 버그        | 버그 탐지                   | 코드 스타일 + 오류     | 보안, 결함           |
| **UI 제공**        | ✅ 웹 UI 있음               | ❌ CLI          | ❌ CLI            | ❌ CLI                   | 일부 (IDE 플러그인)   | ✅ 웹 UI           |
| **CI/CD 연동**     | ✅ 매우 쉬움                 | 제한적            | 제한적              | 제한적                     | Jenkins 등 연동 가능 | ✅ 지원             |
| **확장성**          | 매우 높음                   | 낮음             | 중간               | 낮음                      | 플러그인 기반 확장 가능   | 매우 높음            |
| **학습 곡선**        | 중간                      | 쉬움             | 쉬움               | 쉬움                      | 쉬움              | 높음               |
| **실시간 분석 (IDE)** | SonarLint 이용            | IDE 플러그인       | 없음               | 없음                      | ✅               | 일부 IDE 지원        |
| **라이선스**         | 오픈소스 / 유료               | 오픈소스           | 오픈소스             | 오픈소스                    | 오픈소스            | 상용 (무료 플랜 일부 있음) |



## 🏁 어떤 도구를 언제 쓰면 좋을까?

| 상황                            | 추천 도구            |
| ----------------------------- | ---------------- |
| CI/CD에 통합하고 전체 품질을 관리하고 싶다    | ✅ **SonarQube**  |
| Java 코드 스타일만 간단히 체크하고 싶다      | ✅ **Checkstyle** |
| Java 프로젝트의 고전적 코드 스멜을 점검하고 싶다 | ✅ **PMD**        |
| JS/TS의 문법 오류, 코드 스타일을 분석하고 싶다 | ✅ **ESLint**     |
| 보안, 안정성이 중요한 대형 프로젝트          | ✅ **Coverity**   |



## 🔍 SonarQube Server,SonarCloud,SonarLint

| 항목             | **SonarQube Server**        | **SonarCloud**                    | **SonarLint**                         |
|------------------|-----------------------------|------------------------------------|----------------------------------------|
| 호스팅 위치       | 자체 서버 / 클라우드         | SonarSource 클라우드               | 로컬 IDE 내부                          |
| 설치 필요         | ✅ 필요함                    | ❌ 없음                             | ❌ 없음 (IDE 플러그인 설치만 필요)     |
| 유지보수          | 직접 관리                   | 자동 관리                          | 별도 유지보수 없음                     |
| 비용             | 커뮤니티 무료, 상위 유료     | 오픈소스 무료, 비공개 유료         | 무료                                   |
| 보안             | 내부에서 직접 통제           | 클라우드로 코드 전송               | 코드 외부 전송 없음                    |
| 시작 속도         | 느림                         | 빠름                                | 매우 빠름                              |
| GitHub 등과 통합 | 수동 설정                   | 자동 연동 지원                     | Git 연동 아님 (IDE 내부 분석)         |
| 사용 목적         | CI에서 전체 코드 품질 관리    | 클라우드 기반 지속적 품질 관리     | 개발 중 실시간 코드 피드백             |
| 분석 시점         | 코드 푸시 후 (ex. Jenkins)  | 코드 푸시 후 클라우드에서 분석     | IDE에서 저장 또는 작성 시점에 분석     |

📝 어떤 상황에 어떤 도구?
- SonarLint: 개발 중 IDE에서 빠르게 코드 품질 피드백 받고 싶을 때 (개인 개발자, 초기 단계)

- SonarQube Server: 내부망 또는 조직 전용 환경에서 코드 품질을 관리하고 싶을 때

- SonarCloud: 빠르게 시작하고 GitHub 등 클라우드와 연동하여 품질 관리 자동화하고 싶을 때



---

## 4. SonarQube 아키텍처

![image](https://github.com/user-attachments/assets/e4c16b6b-49d0-4d59-b230-f4e05bca892a)

Sonarqube 자동화 동작 과정
1. 개발자가 작성한 코드를 GitHub에 push
    
2. GitHub Webhook이 Jenkins로 트리거 전송
    
3. Jenkins가 Pipeline 실행
    
4. `./gradlew build sonarqube` 로 빌드 + 정적 분석 실행
    
5. Jenkins 내부 Sonar Scanner가 코드를 분석하고 결과를 SonarQube Server에 전송
    
6. SonarQube Server는 분석 결과를 **룰셋 기반으로 분류**하고, **대시보드 구성**
    
7. 최종 분석 및 분류된 결과를 **SonarQube Database**에 저장


---

## 5. 실습

### 환경설정

### 1. docker ✅
```
# docker 설치
# --cask : docker-compose + GUI 같이 설치
brew install --cask docker

#docker 버전 확인
docker --version

# docker 실행
open /Applications/Docker.app
```

### 2. Jenkins,SonarQube,PostgresDB ✅

*container 실행*
```
# docker-compose.yml 파일 생성 및 추가
vi docker-compose.yml

# docker-compose 실행
docker-compose up -d

# jenkins pw 확인 docker exec -it jenkins cat /var/jenkins_home/secrets/initialAdminPassword

```

*실행 확인*
<img width="1001" alt="image" src="https://github.com/user-attachments/assets/c0e20983-fdbc-429a-849a-96017ead7576" />



### 3. Jenkins-SonarQube 연동 ✅

*1. SonarQube Token 발급*
![image](https://github.com/user-attachments/assets/22d1175b-9d58-419c-b610-69ab8398fbac)
<img width="1572" alt="image" src="https://github.com/user-attachments/assets/cfeb48e4-c7c2-4f2f-85d5-8eeb3a986b5a" />

  
*2. Jenkins에 플러그인 설치*
- Sonar Scanner 플러그인 
![image](https://github.com/user-attachments/assets/117cc584-a7bb-4ad7-80ce-e23f0aee966f)


*3. Jenkins에 SonarQube 서버 등록*
- Jenkins에 토큰 등록 (Credentials)
![image](https://github.com/user-attachments/assets/6a72f2e0-a664-4d1c-a223-edbd078221d6)
![image](https://github.com/user-attachments/assets/952abd75-1d2f-4da4-93aa-999bd3852897)

- SonarQube 서버 등록
![image](https://github.com/user-attachments/assets/4a481faf-e862-4dd5-a96f-1002f085791c)
*Dashboard - jenkins 관리 - System - SonarQube servers에 값 입력*
- **Name** :  
    → 예: `sonarqube` (나중에 `withSonarQubeEnv('sonarqube')` 할 때 이 이름 씀)
    
- **Server URL**:  
    → **중요**! Docker 환경에 따라 아래 중 택 1
    
    |상황|URL|
    |---|---|
    |Docker로 SonarQube 실행 중 (Jenkins도 Docker)|`http://sonarqube:9000`|
    |Jenkins는 로컬 실행, SonarQube는 Docker|`http://localhost:9000`|
    |둘 다 Docker인데 Jenkins는 docker-compose로 실행 중|`http://host.docker.internal:9000` |



### 3. Jenkins-Github 연동 ✅
*1. Github Token 생성*
![image](https://github.com/user-attachments/assets/e41ae67c-1ca6-4638-af1a-acba8dd872f2)
1 **"Generate new token (classic)"** 클릭
2.**Scope(권한)** 설정

|영역|권한|설명|
|---|---|---|
|`repo`|✅ 전체|private/public 저장소 접근|
|`admin:repo_hook`|✅|webhook 읽기/쓰기|

*2. Github webhook설정*
- ngrok 설치 및 실행
```
#ngrok 설치
brew install --cask ngrok

#ngrok 설치 확인
ngrok version

#토큰 등록
ngrok config add-authtoken $YOUR_AUTHTOKEN

#ngrok 실행
ngrok http 8080
```
![image](https://github.com/user-attachments/assets/b5b144b8-de09-4a1b-a1e3-0bf1e80e1bc2)

*3. Github-Webhooks 설정*
![image](https://github.com/user-attachments/assets/a3cbd7ef-66bf-45df-a9e8-136a56d0bc70)
- Payload URL : ngrok으로 터널링된 URL/github-webhook/

### 4. Jenkins에 등록 ✅
- Jenkins에 토큰 등록 (Credentials)
![image](https://github.com/user-attachments/assets/3d84dc73-56dc-47af-8300-01d2b970b6ff)


### 5. Jenkins pipeline 구성
*1. Pipeline 생성*
![image](https://github.com/user-attachments/assets/09989279-d7ee-483f-86f1-a9d20c5e787b)

*2. GitHub hook trigger for GITScm polling 클릭 및 script 추가*
![image](https://github.com/user-attachments/assets/dfe61b9a-dedb-40d4-b75a-54ad925c2661)

*Pipeline script*
```
pipeline {
    agent any  // Jenkins의 어떤 Agent(노드)에서도 실행 가능

    environment {
        SONARQUBE_ENV = 'sonarqube'  // Jenkins > Global Tool Configuration에 설정한 SonarQube 서버 이름
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/JaeHee-devSpace/sonartest.git', branch: 'main'  // GitHub에서 코드 클론
                sh 'ls -al'  // 워크스페이스(루트 디렉토리) 확인용 출력
            }
        }

        stage('Build & Test') {
            steps {
                sh 'chmod +x gradlew'            // gradlew 실행 권한 부여
                sh './gradlew clean build test'  // 프로젝트 클린 후 빌드 및 테스트 수행
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv("${SONARQUBE_ENV}") {  // SonarQube 분석 환경 설정 (token, URL 등 자동 세팅됨)
                    sh './gradlew sonarqube'            // Gradle로 SonarQube 분석 실행
                }
            }
        }
    }

    post {
        success {
            echo '✅ 분석 성공! SonarQube에서 결과를 확인하세요.'  // 빌드 성공 시 메시지 출력
        }
        failure {
            echo '❌ 분석 실패. 로그를 확인하세요.'  // 빌드 실패 시 메시지 출력
        }
    }
}

```
