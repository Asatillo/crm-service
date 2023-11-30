FROM maven:3.8.5-openjdk-17

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/CRM-0.0.1-SNAPSHOT.jar"]
