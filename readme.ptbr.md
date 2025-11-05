# RER - Gateway

[![Spring Cloud Gateway](https://img.shields.io/badge/Spring%20Cloud%20Gateway-4.1-green.svg)](https://spring.io/projects/spring-cloud-gateway) [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot) [![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/) [![Docker](https://img.shields.io/badge/Docker-24+-blue.svg)](https://www.docker.com/)

## 📑 Índice

- [Sobre o Módulo](#sobre-o-módulo)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e Execução](#instalação-e-execução)
- [Acesso aos Serviços](#acesso-aos-serviços)
- [Funcionalidades Implementadas](#funcionalidades-implementadas)
- [Tecnologias](#tecnologias)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Configuração](#configuração)
- [Monitoramento](#monitoramento)
- [Desenvolvimento](#desenvolvimento)
- [Licença](#licença)
- [Contribuição](#contribuição)

---

## Sobre o Módulo

O Gateway é o ponto de entrada unificado do sistema RER, baseado em Spring Cloud Gateway. É responsável pelo roteamento básico entre diferentes microsserviços com configurações de proxy e gerenciamento de CORS.

### Principais Características

- 🚚 Gateway de API baseado em Spring Cloud Gateway
- 🔄 Roteamento básico entre microsserviços
- 🌐 Ponto de entrada único para todas as APIs
- 🔧 Configuração de proxy HTTP opcional
- 🌍 Gerenciamento de CORS
- 🔄 Suporte a Circuit Breaker (Resilience4j)
- 🔄 Reescrita de cabeçalhos de resposta

---

## Pré-requisitos

- Docker versão 24+
- Docker Compose versão 2.20+
- Java 17
- Git

---

## Instalação e Execução

### Execução Integrada

Este módulo é executado automaticamente como parte do sistema RER principal. Para executar o sistema completo:

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

#### Execução Standalone
```bash
docker-compose up
```

---

## Acesso aos Serviços

O Gateway atua como proxy reverso, roteando requisições para os microsserviços apropriados:

- **Gateway Principal**: `http://localhost:8080`
- **Frontend Principal**: `http://localhost/<BASE_URL>`
- **Authentication Frontend**: `http://localhost/<BASE_URL>/<AUTHENTICATION_FRONTEND_CONTEXT_PATH>`
- **Keycloak Admin**: `http://localhost/<BASE_URL>/<AUTHENTICATION_BASE_KEYCLOAK_BASE_URL>`
- **backend API**: `http://localhost/<BASE_URL>/<CORE_BACKEND_API_CONTEXT_PATH>`
- **Calculation Engine**: `http://localhost/<BASE_URL>/<CALCULATION_ENGINE_CONTEXT_PATH>`
- **Authentication Backend**: `http://localhost/<BASE_URL>/<AUTHENTICATION_BACKEND_CONTEXT_PATH>`

---

## Funcionalidades Implementadas

### Roteamento de Requisições

- Roteamento baseado em caminho (Path-based routing)
- Reescrita de URLs
- StripPrefix

### Filtros Configurados

- CORS Global
- Injeção de Cabeçalhos
- Reescrita de Cabeçalhos de Resposta
- Proxy HTTP (opcional)

---

## Tecnologias

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
├── gradle/                                 # Gradle wrapper
├── .mvn/                                   # Maven wrapper
├── build.gradle                            # Arquivo de build Gradle
├── pom.xml                                 # Arquivo de build Maven
├── docker-compose.yml                      # Orquestração local
├── Dockerfile                              # Imagem Docker
└── makefile                                # Comandos make
```

---

## Configuração

### Rotas Configuradas

As rotas são configuradas dinamicamente através de variáveis de ambiente:

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

---

## Variáveis de Ambiente

### Contextos de Roteamento

- `BASE_URL`
- `AUTHENTICATION_FRONTEND_KEYCLOAK_BASE_URL`
- `AUTHENTICATION_BASE_KEYCLOAK_BASE_URL`
- `CORE_BACKEND_API_CONTEXT_PATH`
- `CALCULATION_ENGINE_API_BASE_URL`
- `AUTHENTICATION_BACKEND_KEYCLOAK_BASE_URL`

### Serviços de Destino

- `AUTHENTICATION_FRONTEND_SERVICE_NAME`
- `AUTHENTICATION_BASE_KEYCLOAK_SERVICE_NAME`
- `CORE_BACKEND_API_SERVICE_NAME`
- `CALCULATION_ENGINE_API_SERVICE_NAME`
- `CORE_FRONTEND_SERVICE_NAME`

### Configuração de Proxy (Opcional)

- `PROXY_HOST`
- `PROXY_PORT`

---

## Monitoramento

### Logs

```bash
# Visualizar logs em tempo real
docker-compose logs -f gateway

# Verificar se o Gateway está respondendo
curl -f http://localhost:8080
```

Nota: Spring Boot Actuator não está incluído.

---

## Gerenciamento de Containers

```bash
# Verificar status
docker-compose ps

# Parar serviços
docker-compose down

# Rebuild
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
- Não inclui métricas ou health checks
- Configuração de rotas estática

---

## Licença

Este projeto é distribuído sob a [GPL-3.0](https://github.com/Rural-Environmental-Registry/core/blob/main/LICENSE).

---

## Contribuição

Contribuições são bem-vindas! Para contribuir:

1. Faça um fork do repositório
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

Ao submeter um pull request ou patch, você afirma que é o autor do código e que concorda em licenciar sua contribuição sob os termos da Licença Pública Geral GNU v3.0 (ou posterior) deste projeto. Você também concorda em ceder os direitos autorais da sua contribuição ao Ministério da Gestão e Inovação em Serviços Públicos (MGI), titular deste projeto.

---

## Suporte

Para suporte técnico ou dúvidas sobre o projeto:

- **Documentação:** Consulte os READMEs individuais de cada submódulo
- **Issues:** Reporte problemas através do sistema de issues do repositório
 
---

Copyright (C) 2024-2025 Ministério da Gestão e Inovação em Serviços Públicos (MGI), Governo do Brasil.

Este programa foi desenvolvido pela Dataprev como parte de um contrato com o Ministério da Gestão e Inovação em Serviços Públicos (MGI).