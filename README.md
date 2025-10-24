# RER-DPG - Gateway

## Sobre o Módulo

O **Gateway** é o ponto de entrada unificado do sistema RER-DPG, baseado em Spring Cloud Gateway. Responsável pelo roteamento básico entre os diferentes microsserviços com configurações de proxy e gerenciamento de CORS.

**Principais características:**

- 🚚 Gateway de API baseado em Spring Cloud Gateway
- 🔄 Roteamento básico entre microsserviços
- 🌐 Ponto de entrada único para todas as APIs
- 🔧 Configuração de proxy HTTP opcional
- 🌍 Gerenciamento de CORS
- 🔄 Suporte a Circuit Breaker (Resilience4j)
- 🔄 Reescrita de cabeçalhos de resposta

---

## Pré-requisitos

- **Docker** versão 24+ ([instalação](https://docs.docker.com/engine/install/))
- **Docker Compose** versão 2.20 ou superior ([instalação](https://docs.docker.com/compose/install/linux/#install-using-the-repository))
- **Java 17** ([instalação](https://jdk.java.net/17/))
- **Git** ([instalação](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git))

---

## Instalação e Execução

### Execução Integrada

Este módulo é executado automaticamente como parte do sistema RER-DPG principal. Para executar o sistema completo:

1. **No diretório principal do projeto:**
   ```bash
   ./start.sh
   ```

### Desenvolvimento Local

#### Build com Gradle

```bash
./gradlew build
```

#### Build com Maven

```bash
./mvnw clean package
```

#### Build da Imagem Docker

```bash
docker build -t car-dpg-gateway .
```

### Execução Standalone

Para executar apenas o Gateway:

```bash
docker-compose up
```

---

## Acesso aos Serviços

O Gateway atua como proxy reverso, roteando requisições para os microsserviços apropriados (considerando as variáveis de ambiente definidas nas [configurações](../config/Main/environment/.env.example)):

- **Gateway Principal:** http://localhost:8080
- **Frontend Principal:** http://localhost/<BASE_URL>
- **Authentication Frontend:** http://localhost/<BASE_URL>/<AUTHENTICATION_FRONTEND_CONTEXT_PATH>
- **Keycloak Admin:** http://localhost/<BASE_URL>/<AUTHENTICATION_BASE_KEYCLOAK_BASE_URL>
- **Core-Backend API:** http://localhost/<BASE_URL>/<CORE_BACKEND_API_CONTEXT_PATH>
- **Calculation-Engine:** http://localhost/<BASE_URL>/<CALCULATION_ENGINE_CONTEXT_PATH>
- **Authentication Backend:** http://localhost/<BASE_URL>/<AUTHENTICATION_BACKEND_CONTEXT_PATH>

---

## Funcionalidades Implementadas

### Roteamento de Requisições

O Gateway roteia requisições baseado em:

- **Path-based routing:** Roteamento baseado no caminho da URL
- **Reescrita de URLs:** Modificação de caminhos e cabeçalhos
- **StripPrefix:** Remove prefixos de URLs antes do roteamento

### Filtros Configurados

- **CORS Global:** Permite requisições de qualquer origem
- **Header Injection:** Adiciona cabeçalhos X-Forwarded-Host e X-Forwarded-Proto
- **Response Header Rewriting:** Reescreve cabeçalhos Location nas respostas
- **Proxy HTTP:** Configuração opcional via variáveis de ambiente

### Tecnologias

- Spring Cloud Gateway
- Spring Boot 3.x
- Spring WebFlux (Reactive)
- Resilience4j (Circuit Breaker)
- Netty Server
- Gradle/Maven
- Docker

---

## Estrutura do Projeto

```
Gateway/
├── src/
│   └── main/
│       ├── java/
│       │   └── gateway/
│       │       ├── Application.java        # Aplicação principal
│       │       └── ProxyConfig.java        # Configuração de proxy
│       └── resources/
│           └── application.yml             # Configurações de rotas
├── gradle/                                 # Wrapper do Gradle
├── .mvn/                                   # Wrapper do Maven
├── build.gradle                            # Build Gradle
├── pom.xml                                 # Build Maven
├── docker-compose.yml                      # Orquestração local
├── Dockerfile                              # Imagem Docker
└── makefile                                # Comandos make
```

---

## Configurações

### Rotas Configuradas

As rotas são configuradas dinamicamente através de variáveis de ambiente definidas no projeto principal:

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: <AUTHENTICATION_FRONTEND_SERVICE_NAME>
          uri: http://<AUTHENTICATION_FRONTEND_SERVICE_NAME>
          predicates:
            - Path=/<BASE_URL>/<AUTHENTICATION_FRONTEND_KEYCLOAK_BASE_URL>/**

        - id: <CALCULATION_ENGINE_API_SERVICE_NAME>
          uri: http://<CALCULATION_ENGINE_API_SERVICE_NAME>:8080
          predicates:
            - Path=/<BASE_URL>/<CALCULATION_ENGINE_API_BASE_URL>/**
          filters:
            - StripPrefix=2

        - id: keycloak
          uri: http://<AUTHENTICATION_BASE_KEYCLOAK_SERVICE_NAME>:8080
          predicates:
            - Path=/<BASE_URL>/<AUTHENTICATION_BASE_KEYCLOAK_BASE_URL>/**

        - id: <CORE_BACKEND_API_SERVICE_NAME>
          uri: http://<CORE_BACKEND_API_SERVICE_NAME>:8080
          predicates:
            - Path=/<BASE_URL>/<CORE_BACKEND_API_CONTEXT_PATH>/**

        - id: <CORE_FRONTEND_SERVICE_NAME>
          uri: http://<CORE_FRONTEND_SERVICE_NAME>
          predicates:
            - Path=/**
```

> As variáveis `<BASE_URL>`, `<AUTHENTICATION_FRONTEND_KEYCLOAK_BASE_URL>`, `<CORE_BACKEND_API_CONTEXT_PATH>`, etc. são definidas nas [configurações](../config/Main/environment/.env.example) do ambiente.

### Variáveis de Ambiente

#### Configuração de Rotas

**Contextos de Roteamento:**

- `BASE_URL` - URL base do sistema
- `AUTHENTICATION_FRONTEND_KEYCLOAK_BASE_URL` - Contexto do frontend de autenticação
- `AUTHENTICATION_BASE_KEYCLOAK_BASE_URL` - Contexto do Keycloak
- `CORE_BACKEND_API_CONTEXT_PATH` - Contexto da API do Core-Backend
- `CALCULATION_ENGINE_API_BASE_URL` - Contexto do motor de cálculos
- `AUTHENTICATION_BACKEND_KEYCLOAK_BASE_URL` - Contexto do backend de autenticação

**Serviços de Destino:**

- `AUTHENTICATION_FRONTEND_SERVICE_NAME` - Nome do serviço frontend de autenticação
- `AUTHENTICATION_BASE_KEYCLOAK_SERVICE_NAME` - Nome do serviço Keycloak
- `CORE_BACKEND_API_SERVICE_NAME` - Nome do serviço Core-Backend
- `CALCULATION_ENGINE_API_SERVICE_NAME` - Nome do serviço Calculation-Engine
- `CORE_FRONTEND_SERVICE_NAME` - Nome do serviço Core-Frontend

#### Configuração de Proxy (Opcional)

- `PROXY_HOST` - Host do proxy HTTP (opcional)
- `PROXY_PORT` - Porta do proxy HTTP (opcional)

> Se ambas as variáveis de proxy estiverem definidas, o Gateway usará o proxy configurado para requisições externas.

---

## Monitoramento

### Logs

O Gateway está configurado com log level INFO para Spring Cloud Gateway:

```bash
# Visualizar logs em tempo real
docker-compose logs -f gateway

# Verificar se o Gateway está respondendo
curl -f http://localhost:8080
```

> **Nota:** O projeto não inclui Spring Boot Actuator, portanto não há endpoints de health check ou métricas disponíveis.

---

## Gerenciamento de Containers

### Verificar Status

```bash
docker-compose ps
```

### Parar Serviços

```bash
docker-compose down
```

### Rebuild

```bash
docker-compose up --build
```

---

## Desenvolvimento

### Testes Locais

```bash
# Com Gradle
./gradlew test

# Com Maven
./mvnw test
```

### Execução Local (sem Docker)

```bash
# Com Gradle
./gradlew bootRun

# Com Maven
./mvnw spring-boot:run
```

---

## Limitações Atuais

- Não possui balanceamento de carga automático
- Não implementa autenticação ou autorização
- Não possui rate limiting
- Não inclui métricas ou health checks (sem Actuator)
- Configuração de rotas estática (não dinâmica)

---

## Licença

Este projeto é distribuído sob a [Licença MIT](https://opensource.org/license/mit).

---

## Contribuição

Contribuições são bem-vindas! Para contribuir:

1. Faça um fork do repositório
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

---

**Desenvolvido pela Superintendência de Inteligência Artificial e Inovação da Dataprev**
