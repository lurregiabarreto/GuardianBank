# Guardian Bank

Projeto Final Estrelas Fora da Caixa 🌟

Aplicação que simula uma API de venda de crédito, baseado em BAAS (banking as a service) e microsserviços.

## Regras de Negócio

- A data da proposta deve ser validada para que não seja anterior a 3 meses da data atual;
- Para que a liberação de crédito seja feita, é necessário que o status do credit score seja APROVADO;
- Os únicos produtos financeiros aceitos são: CRÉDITO PESSOAL (juros 119% a.a) e CONSIGNADO (juros 22% a.a);
- A parcela não pode passar de 40% do valor do salário do cliente;
- O cliente pode ter apenas 3 opções de parcelamento para 30 dias: 4, 8 e 12x;
- O cliente tem uma opção de parcelamento para 45 dias: 4x;

## Como Rodar a API localmente

> Pré-requisitos:

- JAVA JDK;
- Spring Boot;
- Maven;
- MariaDB/MySQL;
- Postman.

[Link para instalar o Maven](https://maven.apache.org/download.cgi)

Após instalar as dependências através do terminal na pasta do projeto voce deve agora ser capaz de iniciar a aplicação na IDE.

> Dependências necessárias:

- JPA;
- Validation;
- Web;
- Security;
- JJWT;
- MySQL;
- Lombok;
- Test;
- Modelmapper;
- Thymeleaf;
- Springfox.

Será possível testar a aplicaçao em: localhost:8080

- /usuario
- /login
- /infos
- /propostas

## Tecnologias utilizadas

- JAVA 11
- Springboot
- JPA
- Hibernate
- Swagger
- Maven

## SERVIÇOS

Funcionalidades documentadas via Swagger e Postman Collection.

- Swagger:  localhost:8080/swagger-ui.html

- Postman collection: [Guardian Bank.postman_collection](https://www.getpostman.com/collections/db8d0913d182e7d82bde)

