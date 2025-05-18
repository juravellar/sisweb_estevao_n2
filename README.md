
# sisweb_estevao_n2

Sistema Web desenvolvido como trabalho da disciplina **Desenvolvimento Web**.  
O objetivo é disponibilizar uma API REST e um front-end simples para demonstrar:

* Arquitetura em camadas seguindo o padrão MVC (Controllers ⇢ Services ⇢ Repositories) com Spring Boot 3**  
* Persistência com **Hibernate / JPA**  
* Banco de dados **PostgreSQL** rodando em contêiner **Docker**  
* Gerenciamento de dependências via **Maven**  
* Empacotamento em imagem única (Dockerfile) e orquestração via **docker-compose**  
* Validações, tratamento global de erros e logs estruturados
* Autenticação (JWT) e proteção de rotas usando **Spring Security**, garantindo acesso apenas a usuários autorizados

---
## Índice

1. [Diagrama de alto nível](#diagrama-de-alto-nível)  
2. [Requisitos](#requisitos)  
3. [Subindo o projeto](#subindo-o-projeto)  
4. [Coleção de rotas](#coleção-de-rotas)  
5. [Estrutura de pastas](#estrutura-de-pastas)  
6. [Variáveis de ambiente](#variáveis-de-ambiente)  
7. [Scripts úteis](#scripts-úteis)  
8. [Roadmap](#roadmap)  
9. [Contribuição](#contribuição)  
10. [Licença](#licença)

---

## Requisitos

| Ferramenta   | Versão sugerida | Observação                                        |
| ------------ | --------------- | ------------------------------------------------- |
| **Docker**   | >= 24           | Necessário apenas para rodar com `docker compose` |
| **Java JDK** | 21 LTS          | Obrigatório se quiser rodar local sem contêiner   |
| **Maven**    | 3.9+            | Compilação/empacotamento                          |
| **Git**      | Qualquer        | Clonar o repositório                              |

---

## Subindo o projeto

### 1. Com Docker (recomendado)

```bash
git clone https://github.com/juravellar/sisweb_estevao_n2.git
cd sisweb_estevao_n2
docker compose up --build
```

| Serviço | URL padrão                                                                     | Credenciais padrão                  |
| ------- | ------------------------------------------------------------------------------ | ----------------------------------- |
| API     | [http://localhost:8080/api](http://localhost:8080/api)                         | —                                   |
| Banco   | `localhost:5432`                                                               | `user: postgres` / `pass: postgres` |
| Swagger | [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) | —                                   |

Os logs da aplicação ficam disponíveis em tempo real no console do Docker Compose.

### 2. Sem Docker (ambiente local)

1. Crie um banco PostgreSQL chamado **siswebdb** e atualize `src/main/resources/application.yml`.
2. Compile o projeto:

```bash
./mvnw clean spring-boot:run
```

---

## Coleção de rotas

| Método | Endpoint          | Descrição                                                        |
| ------ | ----------------- | ---------------------------------------------------------------- |
| POST   | /cadastroConta    | Criar uma nova conta de usuário                                  |
| POST   | /login            | Realizar login do usuário                                        |
| GET    | /telaInicial      | Tela inicial do usuário logado                                   |
| GET    | /telaInicialAdmin | Tela inicial para administrador (admin)                          |
| POST   | /cadastroProduto  | Cadastrar um novo produto                                        |
| GET    | /produtos         | Buscar/listar produtos com filtros                               |
| POST   | /produto/editar   | Editar produto existente                                         |
| POST   | /produto/deletar  | "Excluir" produto (marcar como inativo)                          |
| GET    | /carrinho         | Mostrar o carrinho do usuário                                    |
| POST   | /carrinho         | Manipular itens no carrinho (adicionar/remover/finalizar pedido) |


> A documentação interativa (Swagger/OpenAPI) é gerada automaticamente e pode ser acessada em **/swagger-ui.html**.

---

## Estrutura de pastas

```
.
src
└── main
    ├── java
    │   └── api
    │       ├── config
    │       ├── controllers
    │       ├── dao
    │       ├── models
    │       └── DatabaseConnection.java
    ├── resources
    │   ├── application.properties
    │   ├── hibernate.cfg.xml
    │   └── schema.sql
    └── webapp
        ├── assets
        └── WEB-INF
            ├── cadastroConta.jsp
            ├── carrinho.jsp
            ├── login.jsp
            ├── produtos.jsp
            ├── telaInicial.jsp
            └── telaInicialAdmin.jsp
```

---

## Variáveis de ambiente

| Nome                    | Padrão                             | Descrição        |
| ----------------------- | ---------------------------------- | ---------------- |
| `POSTGRES_USER`         | `postgres`                         | Usuário do banco |
| `POSTGRES_PASSWORD`     | `postgres`                         | Senha do banco   |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://db:5432/sisweb` | URL JDBC         |

Você pode sobrescrever qualquer variável executando:

```bash
POSTGRES_PASSWORD=meusegredo docker compose up
```

---

## Scripts úteis

| Comando                                   | O que faz                               |
| ----------------------------------------- | --------------------------------------- |
| `mvn test`                                | Executa a suíte de testes unitários     |
| `docker compose down -v`                  | Remove contêineres **e** volumes        |
| `./mvnw spring-boot:build-image`          | Gera imagem OCI nativa pelo Spring Boot |
| `mvn versions:display-dependency-updates` | Verifica dependências desatualizadas    |

---

## Roadmap

* [ ] Autenticação JWT
* [ ] Implantação em ambiente cloud (Railway/Render)
* [ ] Integração CI/CD no GitHub Actions
* [ ] Front-end React com Chakra UI
* [ ] Cobertura de testes > 80 %

---

## Contribuição

1. Faça um fork
2. Crie uma branch: `git checkout -b feat/minha-feature`
3. Commit: `git commit -m 'feat: Minha nova feature'`
4. Push: `git push origin feat/minha-feature`
5. Abra um Pull Request 🚀

---

## Licença

Distribuído sob a licença **MIT**. Leia o arquivo [`LICENSE`](LICENSE) para mais informações.

