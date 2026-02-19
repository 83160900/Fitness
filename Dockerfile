# Etapa de dependências para cache
FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app

ENV MAVEN_OPTS="-Dfile.encoding=UTF-8"

# Copia apenas o pom.xml para baixar as dependências primeiro (cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B -Dmaven.wagon.http.retryHandler.count=3 -Dhttps.protocols=TLSv1.2

# Copia o código fonte e compila
COPY src ./src
RUN mvn clean package -DskipTests -B -Dmaven.wagon.http.retryHandler.count=3 -Dhttps.protocols=TLSv1.2 -Dfile.encoding=UTF-8

# Etapa final (JRE)
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copia o JAR gerado
COPY --from=build /app/target/*.jar app.jar

# Configurações de execução
EXPOSE 8080
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "app.jar"]
