# ============================
# 1) Dependencies Stage
# ============================
FROM maven:3.9.8-eclipse-temurin-21-alpine AS dependencies

# Atualizar pacotes para corrigir CVEs
RUN apk update && apk upgrade --no-cache


ENV http_proxy= \
  https_proxy= \
  no_proxy=

WORKDIR /app
COPY pom.xml .
COPY settings.xml /root/.m2/settings.xml

# Download dependencies com cache - esta camada será reutilizada
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn dependency:go-offline -B

# ============================
# 2) Build Stage
# ============================
FROM dependencies AS build

COPY . .

# Build com cache otimizado
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn clean package -DskipTests

# ============================
# 3) Runtime Stage
# ============================
FROM eclipse-temurin:21-jre-alpine AS runtime

# Configurar diretório de trabalho
WORKDIR /app

# Copiar JAR do estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Configurações JVM otimizadas
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8080

# Comando de execução
CMD ["sh", "-c", "java  -jar app.jar"]
