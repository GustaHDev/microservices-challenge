# Sistema Hospitalar com Microsserviços

## Sobre o Projeto

Este projeto é uma solução de arquitetura de microsserviços para o gerenciamento de um hospital, abrangendo agendamentos, procedimentos médicos e diagnósticos clínicos. O sistema foi desenvolvido focando em desacoplamento, consistência eventual via mensageria e segurança.

## Índice

- [Arquitetura e Tecnologias](https://www.notion.so/Sistema-Hospitalar-com-Microsservi-os-2d4368cecbb0807fb37bc4f67fd29e20?pvs=21)
- [Estrutura dos Serviços](https://www.notion.so/Sistema-Hospitalar-com-Microsservi-os-2d4368cecbb0807fb37bc4f67fd29e20?pvs=21)
- [Segurança - Roles e permissões](https://www.notion.so/Sistema-Hospitalar-com-Microsservi-os-2d4368cecbb0807fb37bc4f67fd29e20?pvs=21)
- [Como Rodar o Projeto](https://www.notion.so/Sistema-Hospitalar-com-Microsservi-os-2d4368cecbb0807fb37bc4f67fd29e20?pvs=21)
- [Configuração do Keycloak](https://www.notion.so/Sistema-Hospitalar-com-Microsservi-os-2d4368cecbb0807fb37bc4f67fd29e20?pvs=21)
- [Documentação da API (Swagger)](https://www.notion.so/Sistema-Hospitalar-com-Microsservi-os-2d4368cecbb0807fb37bc4f67fd29e20?pvs=21)
- [Fluxos de Negócio Implementados](https://www.notion.so/Sistema-Hospitalar-com-Microsservi-os-2d4368cecbb0807fb37bc4f67fd29e20?pvs=21)
- [Autor](https://www.notion.so/Sistema-Hospitalar-com-Microsservi-os-2d4368cecbb0807fb37bc4f67fd29e20?pvs=21)

## Arquitetura e Tecnologias

O projeto segue uma arquitetura distribuída onde cada serviço possui seu próprio banco de dados, comunicando-se de forma híbrida (HTTP Síncrono para operações de escrita dependentes e Mensageria Assíncrona para atualizações de estado).

- **Linguagem:** Java 21
- **Framework:** Spring Boot 3.4.0
- **Comunicação Síncrona:** OpenFeign (REST)
- **Comunicação Assíncrona:** RabbitMQ (AMQP)
- **Banco de Dados:** MySQL (Instâncias isoladas por serviço)
- **Segurança:** OAuth2 / OIDC com Keycloak
- **Documentação:** SpringDoc OpenAPI (Swagger UI)
- **Containerização:** Docker & Docker Compose

## Estrutura dos serviços

O sistema é composto por 3 microsserviços principais:

1. **Agendamento Service (`:8081`)**
    - Orquestrador principal.
    - Gerencia pacientes e agenda horários.
    - Valida conflitos de horários e regras de complexidade de exames.
    - Escuta eventos de finalização para atualizar registros.
    - Envia eventos de cancelamento para atualizar históricos.
    - *Destaque:* Implementa lógica de pré-geração de IDs para garantir consistência entre microsserviços antes da persistência final.
2. **Clínica Service (`:8082`)**
    - Responsável pela inteligência médica.
    - Recebe sintomas e retorna possíveis diagnósticos (Doenças).
    - Escuta eventos de cancelamento para atualizar históricos.
    - Envia eventos de finalização para atualizar registros.
3. **Procedimento Service (`:8083`)**
    - Gerencia o catálogo de exames e procedimentos.
    - Escuta eventos de cancelamento para atualizar históricos.
    - Envia eventos de finalização para atualizar registros.
    - Valida a autoridade da requisição via Header seguro (`X-Request-Origin`) para impedir agendamentos indevidos de alta complexidade.

## Segurança – Roles e Permissões

Segurança implementada via Keycloak com papéis configurados:

Role 

USER

MEDICO

ADMIN

Permissões

Pode cadastrar e visualizar apenas exames e consultas do seu CPF

Pode consultar pacientes e solicitar exames complexos

CRUD completo em todos os serviços

O API Gateway filtra chamadas e autentica via Bearer Token, impedindo acesso direto aos microserviços sem validação.

## Como Rodar o Projeto

A maneira mais simples de executar o ambiente completo é utilizando o Docker Compose.

1. **Clone o repositório:**
    
    ```bash
    git clone https://git.gft.com/gohv/desafio-microservices.git
    cd desafio-microservices
    ```
    
2. **Suba os containers:**
    
    ```bash
    docker-compose up -d --build
    ```
    
    *Isso iniciará: MySQL (x3), RabbitMQ, Keycloak e os 3 Microsserviços.*
    
3. **Aguarde a inicialização:**
Os serviços dependem do Keycloak e do RabbitMQ estarem prontos. Aguarde alguns minutos até que todos os logs estabilizem.

## Configuração do Keycloak

Para acessar os endpoints, é necessário um token JWT válido.

1. Acesse o painel do Keycloak: `http://localhost:8084` (admin/admin123).
2. **Crie um Realm:** Nome `auth-realm`.
3. **Crie um Client:**
    - Client ID: `hospital-client`.
4. **Crie um Usuário:**
    - Vá em Users -> Add User.
    - Crie um usuário (ex: `paciente`).
    - Defina uma senha na aba "Credentials" (desmarque "Temporary").

**Obtendo o Token (via Postman):**

- **POST** `http://localhost:8084/realms/auth-realm/protocol/openid-connect/token`
- **Body (x-www-form-urlencoded):**
    - `client_id`: hospital-client
    - `grant_type` : password
    - `username`: paciente
    - `password`: <sua-senha>

Utilize o `access_token` retornado no Header `Authorization: Bearer <token>` de todas as requisições.

---

## Documentação da API (Swagger)

Com os serviços rodando, acesse a documentação interativa para testar os endpoints:

- **Agendamento:** [http://localhost:8081/swagger-ui/index.html](https://www.google.com/search?q=http://localhost:8081/swagger-ui/index.html)
- **Clínica:** [http://localhost:8082/swagger-ui/index.html](https://www.google.com/search?q=http://localhost:8082/swagger-ui/index.html)
- **Procedimento:** [http://localhost:8083/swagger-ui/index.html](https://www.google.com/search?q=http://localhost:8083/swagger-ui/index.html)

## Fluxos de Negócio Implementados

### 1. Agendamento de Consulta (Síncrono)

- O usuário envia um POST para o **Agendamento Service**.
- O serviço persiste o agendamento localmente com status `AGUARDANDO`.
- Comunica-se via Feign Client com o **Clínica Service** enviando o ID gerado.
- Após confirmação da Clínica, o status é atualizado para `AGENDADO`.

### 2. Validação de Procedimentos (Regra de Negócio)

- Procedimentos de **Alta Complexidade** não podem ser agendados diretamente pelo endpoint público de agendamento.
- O **Procedimento Service** valida a origem da requisição. Se vier do Agendamento e for Alta Complexidade, retorna `400 Bad Request`.

### 3. Finalização de Consulta/Procedimento (Assíncrono / Event-Driven)

- Ao finalizar uma consulta no **clinica-service ou no procedimento-service**, uma mensagem é publicada no **RabbitMQ** (Exchange: `exchange.consultas`).
- O **agendamento-service** escuta a fila `consulta.finalizada.queue`.
- Ao receber a mensagem, a api atualiza seu registro local, garantindo consistência eventual entre os sistemas.

### 4. Diagnóstico Automático

- Ao cadastrar/atualizar uma consulta na Clínica, o sistema analisa a lista de sintomas enviados.
- Cruza os dados com a base de doenças e retorna automaticamente as possíveis patologias associadas.

---

### **Desenvolvido como parte do Desafio de Microsserviços Start7 da empresa GFT.**

## Autor

Gustavo Henrique Silva - Desenvolvedor Back-End Java