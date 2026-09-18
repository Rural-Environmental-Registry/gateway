# Gateway

## Stack
- Java 21, Spring Cloud Gateway
- Maven
- Reactive (WebFlux)

## Comandos
```bash
# Build
./mvnw clean package
# Test
./mvnw test
# Run local
./mvnw spring-boot:run
```

## Estrutura
```
src/main/java/...  # código
src/main/resources/application.yml  # rotas
src/test/...       # testes
Dockerfile         # container
pom.xml            # dependências
```

## Convenções
- Java: PascalCase classes, camelCase métodos
- Commits: conventional commits (feat/fix/chore)
- Branches: develop → release/dev → release/qa → release/prd → main
