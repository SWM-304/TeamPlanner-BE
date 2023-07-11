FROM openjdk:11
ARG JASYPT_PW

COPY ./TeamPlanner-BE-0.0.1-SNAPSHOT.jar application.jar

ENV TZ=Asia/Seoul

EXPOSE 8080

ENTRYPOINT ["java","-jar", \
"-Djasypt.encryptor.password=1234zzzz", \
"/application.jar"]
