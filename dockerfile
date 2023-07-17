FROM openjdk:11
COPY ./TeamPlanner-BE-0.0.1-SNAPSHOT.jar application.jar
ENV TZ=Asia/Seoul
EXPOSE 8080


ENTRYPOINT ["java","-jar","/application.jar"]
# ENTRYPOINT ["java","-jar", "-Djasypt.encryptor.password=${JASYPT_KEY}", "/application.jar"]