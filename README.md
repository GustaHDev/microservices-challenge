# ENGLISH 

# Hospital System with Microservices

## About the Project

This project is a **microservices architecture solution** for hospital management, covering scheduling, medical procedures, and clinical diagnoses. The system was developed focusing on **decoupling, eventual consistency through messaging, and security**.

## Index

-   [Architecture and Technologies](#architecture-and-technologies)
-   [Service Structure](#service-structure)
-   [Security -- Roles and Permissions](#security----roles-and-permissions)
-   [How to Run the Project](#how-to-run-the-project)
-   [Keycloak Configuration](#keycloak-configuration)
-   [API Documentation (Swagger)](#api-documentation-swagger)
-   [Implemented Business Flows](#implemented-business-flows)
-   [Author](#author)

## Architecture and Technologies

The project follows a **distributed architecture** where each service has its own database, communicating in a hybrid way (**synchronous HTTP for dependent write operations and asynchronous messaging for state updates**).

-   **Language:** Java 21
-   **Framework:** Spring Boot 3.4.0
-   **Synchronous Communication:** OpenFeign (REST)
-   **Asynchronous Communication:** RabbitMQ (AMQP)
-   **Database:** MySQL (isolated instances per service)
-   **Security:** OAuth2 / OIDC with Keycloak
-   **Documentation:** SpringDoc OpenAPI (Swagger UI)
-   **Containerization:** Docker & Docker Compose

## Service Structure

The system is composed of **three main microservices**:

### 1. Agendamento Service (`:8081`)

-   Main orchestrator.
-   Manages patients and schedules appointments.
-   Validates schedule conflicts and exam complexity rules.
-   Listens to completion events to update records.
-   Sends cancellation events to update histories.
-   **Highlight:** Implements **ID pre-generation logic** to guarantee consistency between microservices before final persistence.

### 2. Clinica Service (`:8082`)

-   Responsible for medical intelligence.
-   Receives symptoms and returns possible diagnoses (diseases).
-   Listens to cancellation events to update histories.
-   Sends completion events to update records.

### 3. Procedimento Service (`:8083`)

-   Manages the catalog of exams and procedures.
-   Listens to cancellation events to update histories.
-   Sends completion events to update records.
-   Validates request authority via a secure header (`X-Request-Origin`) to prevent unauthorized scheduling of high-complexity procedures.

## Security -- Roles and Permissions

Security is implemented using **Keycloak** with configured roles:

  -----------------------------------------------------------------------
  Role                                Permissions
  ----------------------------------- -----------------------------------
  USER                                Can register and view only exams
                                      and consultations linked to their
                                      CPF

  MEDICO                              Can view patients and request
                                      complex exams

  ADMIN                               Full CRUD access across all
                                      services
  -----------------------------------------------------------------------

The **API Gateway** filters requests and authenticates via **Bearer Token**, preventing direct access to microservices without validation.

## How to Run the Project

The simplest way to run the full environment is using **Docker Compose**.

### 1. Clone the repository

``` bash
git clone https://git.gft.com/gohv/desafio-microservices.git
cd desafio-microservices
```

### 2. Start the containers

``` bash
docker-compose up -d --build
```

This will start:

-   MySQL (x3)
-   RabbitMQ
-   Keycloak
-   The 3 microservices

### 3. Wait for initialization

The services depend on **Keycloak and RabbitMQ** being ready. Wait a few minutes until all logs stabilize.

## Keycloak Configuration

To access the endpoints, a **valid JWT token** is required.

1.  Access the Keycloak panel: `http://localhost:8084` (login: `admin/admin123`)

2.  **Create a Realm**

    -   Name: `auth-realm`

3.  **Create a Client**

    -   Client ID: `hospital-client`

4.  **Create a User**

    -   Go to **Users → Add User**
    -   Create a user (example: `paciente`)
    -   Set a password in the **Credentials** tab (uncheck "Temporary")

### Getting the Token (via Postman)

**POST**
`http://localhost:8084/realms/auth-realm protocol/openid-connect/token`

**Body (x-www-form-urlencoded)**

-   `client_id`: hospital-client
-   `grant_type`: password
-   `username`: paciente
-   `password`: <your-password>

Use the returned **access_token** in the header:
    `Authorization: Bearer <token>`
for all requests.

## API Documentation (Swagger)

With the services running, access the interactive documentation to test
the endpoints:

-   **Agendamento Service:** [http://localhost:8081/swagger-ui/index.html](https://www.google.com/search?q=http://localhost:8081/swagger-ui/index.html)

-   **Clinica Service:** [http://localhost:8082/swagger-ui/index.html](https://www.google.com/search?q=http://localhost:8082/swagger-ui/index.html)

-   **Procedimento Service:** [http://localhost:8083/swagger-ui/index.html](https://www.google.com/search?q=http://localhost:8083/swagger-ui/index.html)

## Implemented Business Flows

### 1. Appointment Scheduling (Synchronous)

-   The user sends a **POST request to the Agendamento Service**.
-   The service persists the appointment locally with status `WAITING`.
-   It communicates with the **Clinica Service** via Feign Client sending the generated ID.
-   After clinic confirmation, the status is updated to `SCHEDULED`.

### 2. Procedure Validation (Business Rule)

-   **High Complexity** procedures cannot be scheduled directly through the public scheduling endpoint.
-   The **Procedimento Service** validates the origin of the request.
-   If it comes from Agendamento Service and is high complexity, it returns `400 Bad Request`.

### 3. Consultation/Procedure Completion (Asynchronous / Event-Driven)

-   When a consultation is completed in **clinica-service or procedimento-service**, a message is published to **RabbitMQ** (Exchange: `exchange.consultas`).
-   The **agendamento-service** listens to the queue `consulta.finalizada.queue`.
-   Upon receiving the message, the API updates its local record, ensuring **eventual consistency** across systems.

### 4. Automatic Diagnosis

-   When creating or updating a consultation in the Clinic Service, the system analyzes the list of symptoms provided.
-   It cross-references the data with the disease database and automatically returns possible associated pathologies.

------------------------------------------------------------------------

### Developed as part of the **Start7 Microservices Challenge by GFT**.

## Author

**Gustavo Henrique Silva** Back-End Java Developer

------------------------------------------------------------------------

# PORTUGUÊS

# Sistema Hospitalar com Microsserviços

## Sobre o Projeto

Este projeto é uma **solução de arquitetura de microsserviços** para o gerenciamento de um hospital, abrangendo agendamentos, procedimentos médicos e diagnósticos clínicos. O sistema foi desenvolvido focando em **desacoplamento, consistência eventual via mensageria e segurança**.

## Índice

-   [Arquitetura e Tecnologias](#arquitetura-e-tecnologias)
-   [Estrutura dos Serviços](#estrutura-dos-serviços)
-   [Segurança - Roles e permissões](#segurança--roles-e-permissões)
-   [Como Rodar o Projeto](#como-rodar-o-projeto)
-   [Configuração do Keycloak](#configuração-do-keycloak)
-   [Documentação da API (Swagger)](#documentação-da-api-swagger)
-   [Fluxos de Negócio Implementados](#fluxos-de-negócio-implementados)
-   [Autor](#autor)

## Arquitetura e Tecnologias

O projeto segue uma **arquitetura distribuída** onde cada serviço possui seu próprio banco de dados, comunicando-se de forma híbrida (**HTTP Síncrono para operações de escrita dependentes e Mensageria Assíncrona para atualizações de estado**).

-   **Linguagem:** Java 21
-   **Framework:** Spring Boot 3.4.0
-   **Comunicação Síncrona:** OpenFeign (REST)
-   **Comunicação Assíncrona:** RabbitMQ (AMQP)
-   **Banco de Dados:** MySQL (Instâncias isoladas por serviço)
-   **Segurança:** OAuth2 / OIDC com Keycloak
-   **Documentação:** SpringDoc OpenAPI (Swagger UI)
-   **Containerização:** Docker & Docker Compose

## Estrutura dos serviços

O sistema é composto por **três microsserviços principais**:

### 1. Agendamento Service (`:8081`)

-   Orquestrador principal.
-   Gerencia pacientes e agenda horários.
-   Valida conflitos de horários e regras de complexidade de exames.
-   Escuta eventos de finalização para atualizar registros.
-   Envia eventos de cancelamento para atualizar históricos.
-   **Destaque:** Implementa **lógica de pré-geração de IDs** para garantir consistência entre microsserviços antes da persistência final.

### 2. Clinica Service (`:8082`)

-   Responsável pela inteligência médica.
-   Recebe sintomas e retorna possíveis diagnósticos (Doenças).
-   Escuta eventos de cancelamento para atualizar históricos.
-   Envia eventos de finalização para atualizar registros.

### 3. Procedimento Service (`:8083`)

-   Gerencia o catálogo de exames e procedimentos.
-   Escuta eventos de cancelamento para atualizar históricos.
-   Envia eventos de finalização para atualizar registros.
-   Valida a autoridade da requisição via Header seguro (`X-Request-Origin`) para impedir agendamentos indevidos de alta complexidade.

## Segurança – Roles e Permissões

Segurança implementada via **Keycloak** com roles configurados:

  -----------------------------------------------------------------------
  Role                                Permissões
  ----------------------------------- -----------------------------------
  USER                                Pode cadastrar e visualizar apenas exames
                                      e consultas ligadas ao seu
                                      CPF

  MEDICO                              Pode visualizar pacientes e solicitar
                                      exames complexos

  ADMIN                               CRUD completo em todos os
                                      serviços
  -----------------------------------------------------------------------

O **API Gateway** filtra chamadas e autentica via **Bearer Token**, impedindo acesso direto aos microserviços sem validação.

## Como Rodar o Projeto

A maneira mais simples de executar o ambiente completo é utilizando o **Docker Compose**.

### 1. Clone o repositório:
    
``` bash
git clone https://github.com/GustaHDev/microservices-challenge.git
cd desafio-microservices
```
    
### 2. Suba os containers:
    
``` bash
docker-compose up -d --build
```
    
Isso iniciará:

-   MySQL (x3)
-   RabbitMQ
-   Keycloak
-   os 3 Microsserviços.
    
### 3. Aguarde a inicialização:

Os serviços dependem do **Keycloak e do RabbitMQ** estarem prontos. Aguarde alguns minutos até que todos os logs estabilizem.

## Configuração do Keycloak

Para acessar os endpoints, é necessário um **token JWT válido**.

1.  Acesse o painel do Keycloak: `http://localhost:8084` (login: `admin/admin123`)

2.  **Crie um Realm:**

    -   Nome `auth-realm`

3.  **Crie um Client:**

    -   Client ID: `hospital-client`.

4. **Crie um Usuário:**

    -   Vá em **Users -> Add User.**
    -   Crie um usuário (ex: `paciente`).
    -   Defina uma senha na aba **Credentials** (desmarque "Temporary").

### Obtendo o Token (via Postman):

**POST** 
`http://localhost:8084/realms/auth-realm/protocol/openid-connect/token`

**Body (x-www-form-urlencoded):**
-   `client_id`: hospital-client
-   `grant_type` : password
-   `username`: paciente
-   `password`: <sua-senha>

Utilize o **access_token** retornado no Header:
    `Authorization: Bearer <token>`
de todas as requisições.

## Documentação da API (Swagger)

Com os serviços rodando, acesse a documentação interativa para testar os endpoints:

-   **Agendamento Service:** [http://localhost:8081/swagger-ui/index.html](https://www.google.com/search?q=http://localhost:8081/swagger-ui/index.html)

-   **Clinica Service:** [http://localhost:8082/swagger-ui/index.html](https://www.google.com/search?q=http://localhost:8082/swagger-ui/index.html)

-   **Procedimento Service:** [http://localhost:8083/swagger-ui/index.html](https://www.google.com/search?q=http://localhost:8083/swagger-ui/index.html)

## Fluxos de Negócio Implementados

### 1. Agendamento de Consulta (Síncrono)

-   O usuário envia um **POST para o Agendamento Service**.
-   O serviço persiste o agendamento localmente com status `AGUARDANDO`.
-   Comunica-se via Feign Client com o **Clínica Service** enviando o ID gerado.
-   Após confirmação da Clínica, o status é atualizado para `AGENDADO`.

### 2. Validação de Procedimentos (Regra de Negócio)

-   Procedimentos de **Alta Complexidade** não podem ser agendados diretamente pelo endpoint público de agendamento.
-   O **Procedimento Service** valida a origem da requisição. 
-   Se vier do Agendamento e for Alta Complexidade, retorna `400 Bad Request`.

### 3. Finalização de Consulta/Procedimento (Assíncrono / Event-Driven)

-   Ao finalizar uma consulta no **clinica-service ou no procedimento-service**, uma mensagem é publicada no **RabbitMQ** (Exchange: `exchange.consultas`).
-   O **agendamento-service** escuta a fila `consulta.finalizada.queue`.
-   Ao receber a mensagem, a api atualiza seu registro local, garantindo **consistência eventual** entre os sistemas.

### 4. Diagnóstico Automático

-   Ao cadastrar/atualizar uma consulta na Clínica, o sistema analisa a lista de sintomas enviados.
-   Cruza os dados com a base de doenças e retorna automaticamente as possíveis patologias associadas.

------------------------------------------------------------------------

### Desenvolvido como parte do Desafio de Microsserviços Start#7 da GFT.

## Autor

**Gustavo Henrique Silva** - Back-End Java Developer