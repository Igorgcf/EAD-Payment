# EAD-Payment

O EAD-Payment é um microservice responsável pelo gerenciamento do fluxo de pagamentos da plataforma de Ensino a Distância (EAD).

O serviço faz parte de uma arquitetura baseada em microservices, utilizando o ecossistema Spring e seguindo princípios de separação de responsabilidades, comunicação assíncrona e integração entre serviços.

O microservice é responsável pelo gerenciamento de solicitações de pagamento, consulta de pagamentos, processamento das informações relacionadas às transações e comunicação com outros microservices da arquitetura.

Além da API REST, o serviço utiliza RabbitMQ para comunicação assíncrona por meio de Consumers e Publishers, JWT para autenticação e autorização, Spring Data JPA para persistência e integração com H2 data base.

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Database](#database)
- [Technologies Used](#technologies-used)
- [Contributing](#contributing)

## Installation

1. Clone the repository:

```bash
git clone https://github.com/Igorgcf/EAD-Payment.git
```

2. Install dependencies with Maven

## Usage

1. Start the application with Maven
2. The API will be accessible at http://localhost:8086
3. Attention!!! To carry out unit and integration tests of the application, the H2 database must be used.

## API Endpoints

#### Payment Controller (API Rest):
The API provides the following endpoints:

< POST /ead-payment/users/{userId}/payments >
```markdown
POST /ead-payment/users/{userId}/payments **
```
```json
{
    "valuePaid": 77.77,
    "cardHolderFullName": "Igor Freitas",
    "cardHolderCpf": "578.607.400-52",
    "creditCardNumber" : "5555555555554444",
    "expirationDate": "07/2027",
    "cvvCode": 147

}
```
**GET PAYMENTS**

Optional: Use pagination parameters and/or advanced filtering parameters (already contained in Collections.json provided above).

Opcional: Use parâmetros de paginação e/ou parâmetros de filtragem avançados (já contidos em Collections.json fornecido acima).

```markdown
GET /ead-payment/users/{userId}/payments - Retrieve a pagination of all payments.
```
```json
"content": [
        {
            "id": "8380b83a-f08f-4977-adf1-650b7682fed8",
            "username": "BrunoSilva",
            "email": "bruno@gmail.com.br",
            "fullName": "Bruno Silva Ferreira Melo",
            "phoneNumber": "+55 11 91009-0807",
            "cpf": "123-321-144-7",
            "userStatus": "ACTIVE",
            "userType": "STUDENT",
            "creationDate": "2025-01-07T00:35:02Z",
            "lastUpdateDate": "2025-01-07T00:35:02Z",
            "links": [
                {
                    "rel": "self",
                    "href": "http://localhost:8087/users/8380b83a-f08f-4977-adf1-650b7682fed8"
                }
            ]
        }
    ]
```

**GET PAYMENT/ID**
```markdown
GET /ead-payment/users/{userId}/payments/{paymentId} - Retrieve a single payment by id.
```

```json
{
    
}
```

**DELETE PAYMENTS**
```markdown
DELETE /ead-payment/users/{userid}/payments/{paymentId} - Delete a user by id.

Return HTTP status: 200 OK
Body: "Payment deleted successfully!"

```
#### User Controller (API Rest):
The API provides the following endpoints:
*GET USERS**

```markdown
GET /ead-payment/users - Retrieve a pagination of all payments (Endpoint exclusive to administrators).
```
```json
"content": [
        {
            "id": "8380b83a-f08f-4977-adf1-650b7682fed8",
            "username": "BrunoSilva",
            "email": "bruno@gmail.com.br",
            "fullName": "Bruno Silva Ferreira Melo",
            "phoneNumber": "+55 11 91009-0807",
            "cpf": "123-321-144-7",
            "userStatus": "ACTIVE",
            "userType": "STUDENT",
            "creationDate": "2025-01-07T00:35:02Z",
            "lastUpdateDate": "2025-01-07T00:35:02Z",
            "links": [
                {
                    "rel": "self",
                    "href": "http://localhost:8087/users/8380b83a-f08f-4977-adf1-650b7682fed8"
                }
            ]
        }
    ]
```
**GET USERS/ID**
```markdown
GET /ead-payment/users/{id} -  Retrieve a single payment by id.
```

```json
{
    
}
```


## Database

This application uses [H2 Data base](https://www.h2database.com/html/quickstart.html) as the default database.

To perform the unit and integration tests of the application, the H2 database must be used.

## Technologies Used

- Java version 17
- Spring Boot
- Maven
- H2 database
- IntelliJ IDEA
- Postman
---

> ## ⚠️ Atenção
> Conforme a arquitetura do projeto evoluiu e amadureceu, foi necessário criar uma nova branch principal chamada `main-v2`.
>
> Para garantir o funcionamento completo e eficiente da arquitetura, os microservices `Authuser` e `Course` devem ser executados utilizando a branch `main-v2`.
>
> ## Microservices da Arquitetura

Para o funcionamento completo da arquitetura em ambiente local, todos os microservices do ecossistema devem estar em execução utilizando o profile ativo `dev`.

Os microservices que compõem a arquitetura são:

- [Authuser Microservice](https://github.com/Igorgcf/EAD-Authuser)
- [Course Microservice](https://github.com/Igorgcf/EAD-Course)
- [API Gateway](https://github.com/Igorgcf/EAD-API-Gateway)
- [Service Discovery (Eureka Server)](https://github.com/Igorgcf/EAD-Service-Registry)
- [Config Server](https://github.com/Igorgcf/EAD-Config-Server)
- Execute [Notification Microservice](https://github.com/Igorgcf/EAD-Notification)
  ou
  [Notification-Hex Microservice](https://github.com/Igorgcf/EAD-Notification-Hexagonal)

> ℹ️ Certifique-se de executar todos os serviços com o profile `dev` ativo para garantir a comunicação correta entre os microservices durante o desenvolvimento local.
>
> ## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request to the repository.

When contributing to this project, please follow the existing code style, [commit conventions](https://www.conventionalcommits.org/en/v1.0.0/), and submit your changes in a separate branch.

Contribuições são bem-vindas! Se você encontrar algum problema ou tiver sugestões de melhorias, abra um problema ou envie uma solicitação pull ao repositório.

Ao contribuir para este projeto, siga o estilo de código existente, [convenções de commit](https://medium.com/linkapi-solutions/conventional-commits-pattern-3778d1a1e657), e envie suas alterações em uma branch separada.
