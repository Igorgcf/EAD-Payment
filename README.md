# EAD-Payment

O EAD-Payment é um microservice responsável pelo gerenciamento do fluxo de pagamentos da plataforma de Ensino a Distância (EAD).

O serviço faz parte de uma arquitetura baseada em microservices, utilizando o ecossistema Spring e seguindo princípios de separação de responsabilidades, comunicação assíncrona e integração entre serviços.

O microservice é responsável pelo gerenciamento de solicitações de pagamento, consulta de pagamentos, processamento das informações relacionadas às transações e comunicação com outros microservices da arquitetura.

Além da API REST, o serviço utiliza RabbitMQ para comunicação assíncrona por meio de Consumers e Publishers, JWT para autenticação e autorização, Spring Data JPA para persistência e integração com H2 data base.

O microservice também se conecta com a plataforma [Stripe](https://stripe.com/br) através de suas API's para realizar o fluxo de pagamentos 

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
* The API provides the following endpoints:

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
            "id": "3fc957bb-a76a-458c-b4fa-89b34946bf74",
            "paymentControl": "EFFECTED",
            "paymentRequestDate": "2026-08-18T19:01:16Z",
            "paymentCompletionDate": "2026-08-18T19:01:18Z",
            "paymentExpirationDate": "2026-09-17T19:01:16Z",
            "lastDigitsCreditCard": "4444",
            "valuePaid": 77.77,
            "paymentMessage": "Payment effected successfully - payment intent Id: pi_3U5sGTDDQ2Tbmtuv066dwXLI",
            "recurrence": false
        }
    ]
```

**GET PAYMENT/ID**
```markdown
GET /ead-payment/users/{userId}/payments/{paymentId} - Retrieve a single payment by id.
```

```json
{
    "id": "3fc957bb-a76a-458c-b4fa-89b34946bf74",
    "paymentControl": "EFFECTED",
    "paymentRequestDate": "2026-08-18T19:01:16Z",
    "paymentCompletionDate": "2026-08-18T19:01:18Z",
    "paymentExpirationDate": "2026-09-17T19:01:16Z",
    "lastDigitsCreditCard": "4444",
    "valuePaid": 77.77,
    "paymentMessage": "Payment effected successfully - payment intent Id: pi_3U5sGTDDQ2Tbmtuv066dwXLI",
    "recurrence": false
}
```

**DELETE PAYMENTS**
```markdown
DELETE /ead-payment/users/{userId}/payments/{paymentId} - Delete a payment by id.

Return HTTP status: 204 No Content
Body: empty

```
#### User Controller (API Rest):
* The API provides the following endpoints:

**GET USERS**

```markdown
GET /ead-payment/users - Retrieve a pagination of all users (Endpoint exclusive to administrators).
```
```json
{
    "content": [
        {
            "id": "75b42b83-9878-4a77-9a04-774ca10dec67",
            "username": "andressa",
            "email": "andressa@gmail.com.br",
            "fullName": "Andressa Lima",
            "userStatus": "ACTIVE",
            "userType": "STUDENT",
            "phoneNumber": "+55 11 91009-0806",
            "cpf": "606.394.863-55",
            "paymentStatus": "PAYING",
            "paymentExpirationDate": "2026-09-17T19:01:18Z",
            "firstPaymentDate": "2026-08-18T19:01:18Z",
            "lastPaymentDate": "2026-08-18T19:01:18Z"
        },
        {
            "id": "254bf827-307d-45a4-a7c6-d51ae7eba64e",
            "username": "anderson",
            "email": "anderson@gmail.com.br",
            "fullName": "Anderson Andrade",
            "userStatus": "ACTIVE",
            "userType": "ADMIN",
            "phoneNumber": "+55 11 77777-7777",
            "cpf": "994.999.678-34",
            "paymentStatus": "NOTSTARTED"
        }
    ]
}
```
**GET USERS/ID**
```markdown
GET /ead-payment/users/{id} -  Retrieve a single payment by id.
```
```json
{
    "id": "75b42b83-9878-4a77-9a04-774ca10dec67",
    "username": "andressa",
    "email": "andressa@gmail.com.br",
    "fullName": "Andressa Lima",
    "userStatus": "ACTIVE",
    "userType": "STUDENT",
    "phoneNumber": "+55 11 91009-0806",
    "cpf": "606.394.863-55",
    "paymentStatus": "PAYING",
    "paymentExpirationDate": "2026-09-17T19:01:18Z",
    "firstPaymentDate": "2026-08-18T19:01:18Z",
    "lastPaymentDate": "2026-08-18T19:01:18Z"
}
```

## Database

This application uses [H2 Data base](https://www.h2database.com/html/quickstart.html) as the default database.

## Technologies Used

- Java version 17
- Spring Boot
- Maven
- H2 database
- RabbitMQ
- Plataforma Stripe
- IntelliJ IDEA
- Postman

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

![image](https://ismg-cdn.nyc3.cdn.digitaloceanspaces.com/articles/springshell-spring-cloud-function-bugs-need-urgent-patching-showcase_image-4-a-18822.jpg)
