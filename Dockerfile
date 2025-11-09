# Stage 1: builder (usa imagem Maven que já inclui o JDK e mvn)
FROM maven:3.9.5-eclipse-temurin-17 AS builder
WORKDIR /app

# Copia pom primeiro para aproveitar cache do Docker (dependências são baixadas somente se pom mudar)
COPY mottu-api/pom.xml pom.xml
# opcional: se houver settings.xml, .mvn, etc, copie-os antes
# COPY mottu-api/.mvn .mvn
# COPY settings.xml /root/.m2/settings.xml

# baixa dependências (go-offline) para acelerar rebuilds posteriores
RUN mvn -B -U dependency:go-offline

# copia o código-fonte e builda (sem testes para imagens de CI; em produção você pode não pular)
COPY mottu-api/src ./src
COPY mottu-api/src/main/resources ./src/main/resources
RUN mvn -B -DskipTests package

# Stage 2: runtime mais enxuto
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Cria um usuário não-root para rodar a aplicação
RUN useradd --create-home --shell /bin/bash appuser && chown -R appuser:appuser /app

# Copia o jar gerado
COPY --from=builder /app/target/*.jar /app/app.jar
# Ajusta permissões
RUN chown appuser:appuser /app/app.jar

USER appuser
EXPOSE 8080

# Não defina profiles de BD sensíveis em build-time. Deve ser fornecido via env no deploy.
ENV JAVA_OPTS=""
ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS -jar /app/app.jar"]
