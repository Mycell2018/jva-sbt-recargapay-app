# Etapa 1: build da aplicação
FROM maven:3.9.5-eclipse-temurin-22 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: imagem final
FROM eclipse-temurin:22-jdk
WORKDIR /app
COPY --from=build /app/target/jva-sbt-recargapay-app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

