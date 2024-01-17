# camel-account-management
Projeto de camel para realizar a integração com a api desenvolvida como parte do desafio: https://github.com/vieiraitalo/Back-End-Challenge

## Tecnologias utilizadas
- **Spring Boot**
- **Java 17**
- **Camel**
- **Junit/Mockito**
- **Lombok**
- **Maven**

## Rodar Localmente

Para rodar o projeto Localmente é necessário ter a versão 17 do Java e o Maven instalados e configurados na máquina.

Também é necessário já estar com o backend account-management (https://github.com/azevedolh/account-management) rodando na porta 8080

Por padrão o camel está configurado para rodar na porta 8081 e facilitar o teste integrado com o backend.

Execute a classe **CamelAccountManagementApplication** através de uma IDE ou utilize o comando do maven abaixo na raiz do projeto:
```
mvn spring-boot:run 
```

## Documentação
Após rodar o projeto localmente é possível acessar a documentação através do swagger no link:
```
http://localhost:8081/account_management/v1/swagger-ui
```

Uma Collection do postman de exemplo pode ser encontrada no diretório abaixo:
```
https://github.com/azevedolh/camel-account-management/tree/master/src/main/resources/docs
```

## Detalhamento Endpoints

Pode ser encontrado no arquivo readme do projeto do backend (https://github.com/azevedolh/account-management). O camel segue os mesmos contratos.

## Pontos de melhoria
Foram identificados alguns pontos de melhoria que podem ir sendo implementados para melhorar o projeto:
- Configurar Autenticação.
- Utilizar criptografia nos dados sensíveis.
- Acrescentar mais cenários de teste