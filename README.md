# Desafio Backend - Requisitos

## 1. Validações

Você deve ajustar as entidades (model e sql) de acordo com as regras abaixo: 

- `Product.name` é obrigatório, não pode ser vazio e deve ter no máximo 100 caracteres.
- `Product.description` é opcional e pode ter no máximo 255 caracteres.
- `Product.price` é obrigatório deve ser > 0.
- `Product.status` é obrigatório.
- `Product.category` é obrigatório.
- `Category.name` deve ter no máximo 100 caracteres.
- `Category.description` é opcional e pode ter no máximo 255 caracteres.

## 2. Otimização de Performance
- Analisar consultas para identificar possíveis gargalos.
- Utilizar índices e restrições de unicidade quando necessário.
- Implementar paginação nos endpoints para garantir a escala conforme o volume de dados crescer.
- Utilizar cache com `Redis` para o endpoint `/auth/context`, garantindo que a invalidação seja feita em caso de alteração dos dados.

## 3. Logging
- Registrar logs em arquivos utilizando um formato estruturado (ex.: JSON).
- Implementar níveis de log: DEBUG, INFO, WARNING, ERROR, CRITICAL.
- Utilizar logging assíncrono.
- Definir estratégias de retenção e compressão dos logs.

## 4. Refatoração
- Atualizar a entidade `Product`:
  - Alterar o atributo `code` para o tipo inteiro.
- Versionamento da API:
  - Manter o endpoint atual (v1) em `/api/products` com os códigos iniciados por `PROD-`.
  - Criar uma nova versão (v2) em `/api/v2/products` onde `code` é inteiro.

## 5. Integração com Swagger
- Documentar todos os endpoints com:
  - Descrições detalhadas.
  - Exemplos de JSON para requisições e respostas.
  - Listagem de códigos HTTP e mensagens de erro.

## 6. Autenticação e Gerenciamento de Usuários
- Criar a tabela `users` com as colunas:
  - `id` (chave primária com incremento automático)
  - `name` (obrigatório)
  - `email` (obrigatório, único e com formato válido)
  - `password` (obrigatório)
  - `role` (obrigatório e com valores permitidos: `admin` ou `user`)
- Inserir um usuário admin inicial:
  - Email: `contato@simplesdental.com`
  - Password: `KMbT%5wT*R!46i@@YHqx`
- Endpoints:
  - `POST /auth/login` - Realiza login.
  - `POST /auth/register` - Registra novos usuários (se permitido).
  - `GET /auth/context` - Retorna `id`, `email` e `role` do usuário autenticado.
  - `PUT /users/password` - Atualiza a senha do usuário autenticado.

## 7. Permissões e Controle de Acesso
- Usuários com `role` admin podem criar, alterar, consultar e excluir produtos, categorias e outros usuários.
- Usuários com `role` user podem:
  - Consultar produtos e categorias.
  - Atualizar apenas sua própria senha.
  - Não acessar ou alterar dados de outros usuários.

## 8. Testes
- Desenvolver testes unitários para os módulos de autenticação, autorização e operações CRUD.

---

# Perguntas

1. **Se tivesse a oportunidade de criar o projeto do zero ou refatorar o projeto atual, qual arquitetura você utilizaria e por quê?**
2. **Qual é a melhor estratégia para garantir a escalabilidade do código mantendo o projeto organizado?**  
3. **Quais estratégias poderiam ser utilizadas para implementar multitenancy no projeto?**
4. **Como garantir a resiliência e alta disponibilidade da API durante picos de tráfego e falhas de componentes?**
5. **Quais práticas de segurança essenciais você implementaria para prevenir vulnerabilidades como injeção de SQL e XSS?**
5. **Qual a abordagem mais eficaz para estruturar o tratamento de exceções de negócio, garantindo um fluxo contínuo desde sua ocorrência até o retorno da API?**
5. **Considerando uma aplicação composta por múltiplos serviços, quais componentes você considera essenciais para assegurar sua robustez e eficiência?**
6. **Como você estruturaria uma pipeline de CI/CD para automação de testes e deploy, assegurando entregas contínuas e confiáveis?**

Obs: Forneça apenas respostas textuais; não é necessário implementar as perguntas acima.


# Respostas: 
Arquitetura ideal para o projeto:
Utilizaria Clean Architecture com DDD (Domain-Driven Design) porque:
Separa claramente as responsabilidades (camadas)
Facilita a manutenção e testabilidade
Permite evolução independente das camadas
Estrutura proposta:

└── src/
        ├── domain/         # Regras de negócio e entidades
        ├── application/    # Casos de uso e serviços
        ├── infrastructure/ # Implementações técnicas
        └── interfaces/     # Controllers e DTOs



# Estratégia para escalabilidade e organização:
Implementaria:
- Módulos independentes por domínio
- Princípios SOLID
- Microsserviços para funcionalidades específicas
- Event-driven architecture para desacoplamento
- Cache distribuído (já implementado com Redis)
- Paginação e filtros eficientes
- Documentação clara e padronizada
- Estratégias para multitenancy:
- Abordagens possíveis:
- Database per tenant:

#  CREATE SCHEMA tenant_{id};

#  Discriminator column

  @Entity
    @TenantDiscriminator
    public class Product {
        private String tenantId;
    }


- Separate schemas
- Implementaria filtros automáticos por tenant em nível de aplicação


# Resiliência e alta disponibilidade:
- Implementaria:
- Circuit Breaker (usando Resilience4j)
- Load Balancing
- Rate Limiting
- Cache distribuído (Redis)
- Monitoramento com Prometheus/Grafana
- Health checks
- Retry policies
- Fallback mechanisms

# Práticas de segurança:
- Implementações necessárias:
- Prepared Statements (já usado com JPA)
- Input validation
- Output encoding
- CORS configurado corretamente
- Headers de segurança (HSTS, CSP)
- Rate limiting
- JWT com rotação de chaves
- Auditoria de acessos

# Tratamento de exceções:

@ControllerAdvice
  public class GlobalExceptionHandler {
      
      @ExceptionHandler(BusinessException.class)
      public ResponseEntity<ErrorResponse> handleBusinessException(...)
      
      @ExceptionHandler(ValidationException.class)
      public ResponseEntity<ErrorResponse> handleValidationException(...)
  }



- Logs estruturados
- Rastreamento de erros
- Respostas padronizadas
# Componentes essenciais para múltiplos serviços:
# Necessários:
- API Gateway
- Service Discovery (Eureka)
- Config Server
- Message Broker (RabbitMQ/Kafka)
- Distributed Tracing (Jaeger)
- Monitoring (Prometheus/Grafana)
- Log Aggregation (ELK Stack)
- Cache distribuído (Redis)

 # Pipeline CI/CD: 


 # Exemplo de pipeline
![image](https://github.com/user-attachments/assets/e7253e2b-ab72-4c53-8b85-3069a0567e95)







# Justificativas para as escolhas atuais do projeto:

# Spring Boot:
- Rápido desenvolvimento
- Ampla comunidade
- Configuração automática
- Integração fácil com outros componentes
# PostgreSQL:
- Confiável e robusto
- Suporte a JSON
- Bom desempenho
- Transações ACID
# Redis:
- Cache distribuído
- Alta performance
- Suporte a diferentes estruturas de dados
- Fácil integração

# JWT:
- Stateless
- Escalável
- Seguro
- Fácil implementação

# Flyway:
- Controle de versão do banco
- Migrations automatizadas
- Rollback facilitado
- Histórico de mudanças
# Swagger/OpenAPI:
- Documentação automática
- Interface interativa
- Facilita testes
- Padrão da indústria
# Estas escolhas formam uma base sólida para o projeto, permitindo:
- Escalabilidade
- Manutenibilidade
- Segurança
- Performance
- Facilidade de desenvolvimento





#INSTRUÇÕES PARA RODAR O PROJETO LOCALMENTE: 

Projeto CRUD de Produtos - Simples Dental

🚀 Como executar o projeto
- Pré-requisitos
- Java 17
- Maven 3.6.3+
- PostgreSQL 14+
- Redis 7.0+

🎲 Configurando o ambiente

# Clone este repositório
- git clone <url-do-repositorio>

# Acesse a pasta do projeto
- cd produto

# Crie o banco de dados PostgreSQL
- createdb -U postgres produto

# Certifique-se que o Redis está rodando na porta 6379
- redis-cli ping

🔧 Configuração
O arquivo application.properties já está configurado, mas caso necessite alterar:

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/produto
spring.datasource.username=postgres
spring.datasource.password=postgres

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379

▶️ Executando o projeto: 

# Instalar dependências
- mvn clean install

# Executar o projeto
- mvn spring-boot:run


📝 Documentação da API
Após iniciar o projeto, acesse:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI: http://localhost:8080/v3/api-docs

👤 Usuário Inicial

{
  "email": "contato@simplesdental.com",
  "password": "KMbT%5wT*R!46i@@YHqx"
}

🔐 Endpoints Principais: 

# Autenticação
- POST /auth/login
- POST /auth/register
- GET /auth/context

# Produtos
- GET /api/products
- POST /api/products
- PUT /api/products/{id}
- DELETE /api/products/{id}

# Categorias
- GET /api/categories
- POST /api/categories
- PUT /api/categories/{id}
- DELETE /api/categories/{id}

🔍 Verificação de Serviços: 

# PostgreSQL
- psql -U postgres -d produto -c "\dt"

# Redis
- redis-cli ping

❗ Solução de Problemas Comuns
Portas necessárias:
- 8080: API
- 5432: PostgreSQL
- 6379: Redis
Erros comuns:
- Banco não existe: createdb -U postgres produto
- Redis não conecta: Verifique se está rodando redis-cli ping
- Erro de compilação: mvn clean install -U
🔒 Segurança
- Autenticação via JWT
- Roles: ADMIN, USER
Header: Authorization: Bearer {token}


📋 Funcionalidades
- [x] CRUD de Produtos
- [x] CRUD de Categorias
- [x] Autenticação JWT
- [x] Cache com Redis
- [x] Documentação Swagger
- [x] Migrations com Flyway
- [x] Validações
- [x] Logs estruturados
🛠 Tecnologias
- Spring Boot 3.2.5
- Spring Security
- Spring Data JPA
- PostgreSQL
- Redis
- Flyway
- Swagger/OpenAPI
- JWT


📊 Estrutura do Projeto: 

![image](https://github.com/user-attachments/assets/2c6f9827-0dc3-4259-b3e3-e3f9219990ff)


📝 Licença
- N/A.







