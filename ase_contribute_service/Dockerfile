FROM openjdk:17-jdk as build

WORKDIR /app

COPY . .

RUN chmod +x ./mvnw

RUN ./mvnw clean package -pl ase_contribute_service -am -DskipTests

FROM openjdk:17-jdk

WORKDIR /app

COPY --from=build /app/ase_contribute_service/target/*.jar /app/app.jar

COPY --from=build /app/ase_contribute_service/src/main/resources/application-prod.yml /app/application.yml
EXPOSE 8082

CMD ["java", "-jar", "app.jar","--server.error.include-message=always"]

