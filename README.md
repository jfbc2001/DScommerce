# DScommerce

![Java](https://img.shields.io/badge/Java-25-ED8B00?logo=openjdk&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F?logo=springboot&logoColor=white) ![Maven](https://img.shields.io/badge/Maven-Wrapper-C71A36?logo=apachemaven&logoColor=white) ![Status](https://img.shields.io/badge/status-conclu%C3%ADdo-brightgreen)

**DScommerce** é uma API REST de comércio eletrônico desenvolvida em **Java e Spring Boot**. O projeto implementa um catálogo de produtos, categorias, autenticação de usuários e gerenciamento de pedidos, com controle de acesso por perfil.

O foco é aplicar práticas de desenvolvimento backend, incluindo arquitetura em camadas, persistência com JPA, DTOs, validação, tratamento de exceções e segurança com OAuth2 e JWT.

## Funcionalidades

- Consulta pública de produtos, com **paginação e filtro por nome**.
- Consulta pública de categorias.
- Cadastro, atualização e exclusão de produtos, restritos a administradores.
- Autenticação de usuários com emissão de token JWT.
- Consulta dos dados do usuário autenticado.
- Consulta de pedidos: clientes acessam seus próprios pedidos; administradores podem consultar pedidos de outros usuários.
- Criação de pedidos com produtos e quantidades informados no corpo da requisição.
- Associação automática do novo pedido ao usuário autenticado, com data/hora atual e status inicial `WAITING_PAYMENT`.
- Tratamento centralizado de erros e respostas de validação.

## Tecnologias

| Tecnologia | Utilização |
|---|---|
| Java 25 | Linguagem principal |
| Spring Boot 4.0.6 | Framework da aplicação |
| Spring Web MVC | Endpoints REST |
| Spring Data JPA / Hibernate | Persistência e mapeamento objeto-relacional |
| Spring Security | Autenticação e autorização |
| Spring Authorization Server / OAuth2 | Emissão de tokens |
| JWT | Autenticação das requisições protegidas |
| H2 Database | Banco de dados em memória no perfil `test` |
| Jakarta Bean Validation | Validação de dados |
| Maven Wrapper | Gerenciamento de dependências e execução |

## Arquitetura

O código está organizado em camadas:

```text
src/main/java/com/devsuperior/dscommerce/
├── config/                 # Segurança, OAuth2 e autenticação
├── controllers/            # Endpoints REST e tratamento de exceções
├── dto/                    # Objetos de transferência de dados
├── entities/               # Entidades JPA e enumerações
├── repositories/           # Interfaces de acesso ao banco
├── services/               # Regras de negócio
└── DscommerceApplication.java
```

**Fluxo típico:** `Controller → Service → Repository → Database`.

## Endpoints

Base URL local: `http://localhost:8080`

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| `GET` | `/products` | Listar produtos com paginação e filtro | Público |
| `GET` | `/products/{id}` | Consultar produto por ID | Público |
| `POST` | `/products` | Cadastrar produto | ADMIN |
| `PUT` | `/products/{id}` | Atualizar produto | ADMIN |
| `DELETE` | `/products/{id}` | Excluir produto | ADMIN |
| `GET` | `/categories` | Listar categorias | Público |
| `POST` | `/oauth2/token` | Obter token de acesso | Credenciais válidas |
| `GET` | `/users/me` | Consultar usuário autenticado | Autenticado |
| `GET` | `/orders/{id}` | Consultar pedido por ID | Dono do pedido ou ADMIN |
| `POST` | `/orders` | Criar pedido | Autenticado |

### Exemplos

**Buscar produtos por nome:**

```http
GET /products?name=Macbook&page=0&size=10
```

**Criar pedido:**

```http
POST /orders
Authorization: Bearer SEU_TOKEN
Content-Type: application/json
```

```json
{
  "items": [
    { "productId": 1, "quantity": 2 },
    { "productId": 3, "quantity": 1 }
  ]
}
```

O servidor identifica o cliente pelo token, utiliza os preços cadastrados dos produtos e retorna `201 Created` com o pedido criado.

## Autenticação e autorização

A API utiliza **OAuth2 com concessão de senha customizada**, configurada para fins de estudo, e **JWT Bearer Token** para acessar rotas protegidas.

No Postman, para obter um token:

1. Crie uma requisição `POST http://localhost:8080/oauth2/token`.
2. Em **Authorization**, selecione **Basic Auth** e informe o ID e o segredo do cliente OAuth2.
3. Em **Body → x-www-form-urlencoded**, envie:

| Campo | Valor de exemplo |
|---|---|
| `grant_type` | `password` |
| `username` | `maria@gmail.com` |
| `password` | `123456` |

4. Copie o `access_token` retornado e utilize **Authorization → Bearer Token** nas rotas protegidas.

O projeto diferencia dois perfis:

- **`ROLE_CLIENT`**: consulta e cria seus próprios pedidos.
- **`ROLE_ADMIN`**: administra produtos e pode consultar pedidos de outros usuários.

> **Nota de segurança:** o fluxo de senha customizado e as credenciais de exemplo destinam-se ao ambiente de aprendizado. Para uso em produção, recomenda-se adotar um fluxo OAuth2 apropriado, gerenciar segredos de forma segura e utilizar um banco persistente.

## Como executar

### Pré-requisitos

- **JDK 25** instalado e configurado.
- Git (opcional, para clonar o repositório).
- Postman ou outro cliente HTTP para testar endpoints autenticados.

Clone o projeto:

```bash
git clone https://github.com/jfbc2001/DScommerce.git
cd DScommerce
```

No **Windows (PowerShell)**:

```powershell
.\mvnw.cmd spring-boot:run
```

No **Linux, macOS ou GitHub Codespaces**:

```bash
./mvnw spring-boot:run
```

A aplicação utiliza o perfil `test` por padrão, com **H2 em memória** e dados iniciais carregados pelo `import.sql`.

O console H2 está configurado em `http://localhost:8080/h2-console`, com:

```text
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (em branco)
```

O acesso ao console pode depender das regras de segurança configuradas na aplicação.

### Configurações de autenticação

O arquivo `application.properties` aceita as variáveis de ambiente abaixo:

| Variável | Valor padrão para desenvolvimento |
|---|---|
| `CLIENT_ID` | `myclientid` |
| `CLIENT_SECRET` | `myclientsecret` |
| `JWT_DURATION` | `86400` segundos |

Evite usar os valores padrão em ambientes públicos.

## Status do projeto

O DScommerce está **concluído como projeto de estudo e portfólio**, contemplando as funcionalidades planejadas de gerenciamento de produtos, categorias, usuários e pedidos, além de autenticação OAuth2/JWT e controle de acesso por perfis.

A aplicação pode ser executada localmente, utilizando banco de dados H2 e as instruções de configuração descritas neste repositório. O projeto tem finalidade educacional e não representa uma solução de comércio eletrônico pronta para produção.

## Contexto e autoria

Projeto desenvolvido como atividade prática de aprendizado em **Java e Spring Boot**, com base nos conteúdos da **DevSuperior / Java Spring Professional**.

**Desenvolvedor:** João Felipe Bianchi Curcio  
**GitHub:** [@jfbc2001](https://github.com/jfbc2001)  
**Repositório:** [DScommerce](https://github.com/jfbc2001/DScommerce)
