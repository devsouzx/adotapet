# AdotaPet API

API REST para gestão de abrigos, pets, adotantes e adoções, com autenticação JWT, busca por filtros, geolocalização de abrigos e documentação interativa via Swagger/OpenAPI.

## Visão geral

O AdotaPet é uma solução backend em Java/Spring Boot para apoiar processos de adoção de pets. A aplicação permite:

- cadastro e autenticação de abrigos;
- gestão de pets disponíveis para adoção;
- cadastro e consulta de adotantes;
- registro, atualização e encerramento de adoções;
- busca por filtros e proximidade geográfica;
- recuperação de senha por e-mail;
- documentação da API com Swagger UI.

## Stack tecnológica

- Java 23
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Data JPA
- Spring Security + JWT
- PostgreSQL
- Flyway
- Redis
- Kafka
- Maven
- Swagger/OpenAPI

## Estrutura do projeto

```text
adotapet/
├── src/
│   ├── main/
│   │   ├── java/com/devsouzx/adotapet/
│   │   │   ├── controller/
│   │   │   ├── domain/
│   │   │   ├── dto/
│   │   │   ├── exception/
│   │   │   ├── infra/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   ├── properties/
│   │   │   ├── util/
│   │   │   └── AdotapetApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/
│   └── test/
├── docs/
├── pom.xml
├── .gitignore
└── README.md
```

## Requisitos

Antes de executar o projeto, certifique-se de ter instalado:

- JDK 23+
- Maven 3.9+
- PostgreSQL em execução
- Redis em execução
- Kafka (opcional, caso queira usar os recursos de mensageria integrados)

## Configuração do ambiente

O projeto usa `application.properties` para configurar banco, Redis e segurança. Ajuste as variáveis conforme seu ambiente local.

Exemplo:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/adotapet
spring.datasource.username=postgres
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=org.postgresql.Driver

redis.host=localhost
redis.port=6379

api.security.token.secret=sua_chave_secreta_jwt
```

Observações:

- O banco deve existir antes da aplicação iniciar.
- O Flyway aplica as migrações automaticamente ao subir a aplicação.
- A secret JWT deve ser forte e mantida fora do repositório em ambientes de produção.

## Execução

### 1) Clonar o repositório

```bash
git clone https://github.com/devsouzx/adotapet.git
cd adotapet
```

### 2) Iniciar dependências locais

Certifique-se de que PostgreSQL e Redis estejam ativos.

Exemplo com Docker:

```bash
docker run --name adotapet-postgres -e POSTGRES_DB=adotapet -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=sua_senha -p 5433:5432 -d postgres:16

docker run --name adotapet-redis -p 6379:6379 -d redis:7-alpine
```

### 3) Rodar a aplicação

```bash
mvn spring-boot:run
```

A aplicação normalmente sobe em:

```text
http://localhost:8080
```

### Frontend

O frontend React está em `frontend/`, separado do código Java. Com o backend em execução:

```powershell
cd frontend
npm install
Copy-Item .env.example .env
npm run dev
```

O Vite usa um proxy local para o backend em `http://localhost:8080`. Consulte [frontend/README.md](frontend/README.md) para o mapeamento dos endpoints, contratos, regras e limitações identificados.

## Documentação da API

A API possui documentação automatizada via Swagger/OpenAPI.

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Autenticação

A autenticação do sistema é feita com JWT.

### Endpoints públicos

- `POST /auth/login` — autenticar abrigo e obter token
- `POST /auth/register` — cadastrar abrigo
- `POST /auth/request-password-reset` — solicitar redefinição de senha
- `POST /auth/resetpassword/` — redefinir senha

### Endpoints protegidos

Os endpoints abaixo exigem token JWT válido no cabeçalho:

```http
Authorization: Bearer <token>
```

## Principais recursos

### Abrigos

- `GET /abrigo` — consultar perfil do abrigo autenticado
- `GET /abrigo/{identifier}` — consultar abrigo por ID
- `PUT /abrigo/editar` — atualizar dados do abrigo
- `GET /abrigo/proximos?latitude=...&longitude=...&raio=...` — localizar abrigos próximos

### Pets

- `GET /pet` — listar pets
- `POST /pet/novo` — cadastrar pet
- `GET /pet/{identifier}` — consultar pet por ID
- `PUT /pet/{identifier}/editar` — atualizar pet
- `DELETE /pet/{identifier}/remover` — remover pet
- `GET /pet/filtros` — buscar com filtros (nome, espécie, raça, idade, peso, status, sexo, porte)

### Adotantes

- `GET /adotante` — listar adotantes
- `POST /adotante/novo` — cadastrar adotante
- `GET /adotante/{adotanteId}` — consultar adotante
- `PUT /adotante/{adotanteId}` — atualizar adotante
- `DELETE /adotante/{adotanteId}` — excluir adotante

### Adoções

- `GET /adocao` — listar adoções do abrigo autenticado
- `GET /adocao/{id}` — consultar adoção
- `POST /adocao` — registrar adoção
- `PUT /adocao/{id}` — atualizar adoção
- `PATCH /adocao/{id}/encerrar` — encerrar adoção
- `DELETE /adocao/{id}` — excluir adoção

## Funcionalidades adicionais

- validação de entrada com Bean Validation;
- tratamento global de erros via `@RestControllerAdvice`;
- migrações de banco com Flyway;
- cache/armazenamento em Redis;
- integração com Kafka para eventos e mensageria;
- suporte a busca geoespacial por proximidade.

## Segurança

A aplicação aplica regras de autorização por endpoint. Em resumo:

- `/auth/**` e Swagger/OpenAPI são públicos;
- `/pet` e `/abrigo/*` têm leitura pública para alguns endpoints;
- operações de escrita e adoções exigem autenticação JWT;
- o acesso não autorizado retorna respostas padronizadas com status 401/403.

## Contribuição

1. Faça um fork do projeto.
2. Crie uma branch para sua feature: `git checkout -b feature/nova-funcionalidade`
3. Commit das mudanças: `git commit -m "feat: adiciona nova funcionalidade"`
4. Push para o repositório: `git push origin feature/nova-funcionalidade`
5. Abra um Pull Request.
