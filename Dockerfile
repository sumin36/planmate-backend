# 자바 실행 환경(JDK) 가져오기
FROM eclipse-temurin:17-jdk-alpine

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 jar 파일을 상자 안으로 복사
COPY build/libs/*.jar app.jar

# 앱 실행 시 사용할 포트 번호
EXPOSE 8080

# 실행 명령 (java -jar app.jar)
ENTRYPOINT ["java", "-jar", "app.jar"]