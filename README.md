# 🏍️ Mottu API – Sistema de Gerenciamento de Motos

## Descrição do projeto
A Mottu API é uma aplicação web construída com Java + Spring Boot para gestão de motos, sensores e pátios. O projeto foi desenvolvido como parte do desafio da disciplina Java Advanced e fornece CRUDs, busca com filtros, paginação, autenticação básica e uma interface web com Thymeleaf.

## Links rápidos
- Documentação do projeto: `docs/`
- Troubleshooting: `docs/troubleshooting.md`
- Docker + docker-compose: `docker-compose.yml`

## Desenvolvedores
- Carlos Eduardo R C Pacheco – RM: 557323     
- Pedro Augusto Costa Ladeira – RM: 558514

## Tecnologias
Java 17, Spring Boot 3.2.5, Spring Data JPA, Thymeleaf, H2 (dev), Oracle (prod), Maven, Docker

## Pré-requisitos
- JDK 17
- Maven
- Docker (opcional para deploy via compose)
- (Para produção) Oracle Database ou outra fonte configurada via `application-prod.properties`

## Quickstart (modo dev com H2)
Abra um terminal (cmd / PowerShell / IntelliJ terminal) no diretório do projeto e rode:

1) Build do artefato:

    mvn -f mottu-api clean package -DskipTests

2) Rodar com perfil H2 (opção 1 — jar):

    java -jar mottu-api\target\mottu-api-1.0.0.jar --spring.profiles.active=h2

Ou (opção 2 — maven run):

    mvn -f mottu-api spring-boot:run "-Dspring-boot.run.profiles=h2"

Depois abra `http://localhost:8080` e `http://localhost:8080/swagger-ui/index.html`.

## Rodando com Oracle (prod)
1) Se você tem o driver `ojdbc11.jar` local, instale no repositório local:

    mvn install:install-file -Dfile=mottu-api\lib\ojdbc11.jar -DgroupId=com.oracle.database.jdbc -DartifactId=ojdbc11 -Dversion=23.3.0 -Dpackaging=jar

2) Build com profile Oracle e execução:

    mvn -f mottu-api clean package -DskipTests -Poracle
    mvn -f mottu-api spring-boot:run -Poracle

> Observação: não comite credenciais em `application.properties`. Use variáveis de ambiente ou arquivos `application-*.properties` que não sejam versionados.

## Docker / docker-compose
Para rodar a aplicação em container (ajuste `.env` com suas credenciais ou use `SPRING_PROFILES_ACTIVE=h2` para dev):

    # build + run via compose
    docker-compose up --build

Use o arquivo `.env.example` como exemplo.

## Endpoints principais
- `GET /api/motos` — listar motos
- `POST /api/motos` — criar moto
- `PUT /api/motos/{id}` — atualizar
- `DELETE /api/motos/{id}` — deletar

## Documentação e entregáveis
Todos os artefatos exigidos pela sprint estão sendo organizados na pasta `docs/` (checklist, roteiro do vídeo, diagramas, canvas). Antes da entrega finalize:
- Diagrama ER e arquitetura (em `docs/diagrams/`)
- Roteiro do vídeo (em `docs/video/script.md`)
- Canvas do projeto (em `docs/canvas/project_canvas.md`)

## Testes
Execute testes com:

    mvn -f mottu-api test

## Contato
Abra uma issue ou fale com um dos desenvolvedores listados acima para dúvidas.

## Alterações recentes (automatizadas)
No processo de organização do repositório eu adicionei os seguintes arquivos para ajudar na entrega final e na automação:

- `docs/checklist_final.md` — checklist de entrega final
- `docs/video/script.md` — roteiro do vídeo (15 min)
- `docs/canvas/project_canvas.md` — projeto canvas
- `docs/diagrams/README.md` — instruções para diagramas
- `docs/troubleshooting.md` — troubleshooting (moved from README)
- `.env.example` — exemplo de variáveis de ambiente para `docker-compose`
- `.gitignore` — atualizações para ignorar `target/` e artefatos
- `.github/workflows/ci.yml` — pipeline CI (build + test)

Se alguma dessas alterações não estiver de acordo com o que você espera, eu posso reverter ou ajustar — diga qual arquivo prefere que eu edite manualmente.

---
