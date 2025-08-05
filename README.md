# API de Gestão Escolar

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Build](https://img.shields.io/badge/Build-Maven-C71A36.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

API RESTful para gestão de equipes e controle de presença em um ambiente escolar, construída com as melhores práticas de desenvolvimento de software e Arquitetura Hexagonal.

## Sumário
1.  [Visão do Projeto](#1-visão-do-projeto)
2.  [Tecnologias e Arquitetura](#2-tecnologias-e-arquitetura)
3.  [Funcionalidades Implementadas](#3-funcionalidades-implementadas)
4.  [Estrutura do Projeto](#4-estrutura-do-projeto)
5.  [Como Executar](#5-como-executar)
6.  [Como Testar com Insomnia](#6-como-testar-com-insomnia)
7.  [Endpoints da API](#7-endpoints-da-api)

## 1. Visão do Projeto
> Este projeto nasceu da necessidade de modernizar e digitalizar processos manuais em um ambiente escolar. Onde antes havia papel, planilhas e falta de visibilidade, agora buscamos uma solução centralizada, segura e em tempo real. A API "Presente" é a espinha dorsal dessa transformação, empoderando Diretores, Inspetores e Professores com ferramentas digitais que otimizam a gestão de equipes e o registro de presença, garantindo eficiência, transparência e confiabilidade dos dados.

## 2. Tecnologias e Arquitetura

Este projeto foi cuidadosamente desenhado para ser robusto, escalável e de fácil manutenção.

- **Arquitetura Principal**: **Arquitetura Hexagonal (Portas e Adaptadores)**, garantindo o desacoplamento total entre as regras de negócio e a infraestrutura.
- **Linguagem**: **Java 17**
- **Framework Principal**: **Spring Boot 3.2.x**
- **Segurança**: **Spring Security 6** com autenticação e autorização baseada em Tokens **JWT (JSON Web Tokens)**.
- **Banco de Dados**: **H2 (em memória)** para o ambiente de desenvolvimento.
- **Mapeamento de Objetos**: **MapStruct** para conversões eficientes e seguras entre DTOs e Modelos de Domínio.
- **Gerenciador de Dependências**: **Maven**

## 3. Funcionalidades Implementadas

A API possui um sistema de controle de acesso baseado em 3 papéis (Roles), cada um com suas permissões específicas.

### ✅ Diretor (`ROLE_DIRETOR`)
- [x] Cadastrar novos Inspetores no sistema.
- [x] Desativar (demitir) Inspetores existentes.
- [x] Listar todos os inspetores ativos.
- [x] Visualizar a lista de presença de Inspetores em uma data específica.
- [x] Visualizar a lista de presença de Professores em uma data específica.

### ✅ Inspetor (`ROLE_INSPETOR`)
- [x] Cadastrar novos Professores no sistema.
- [x] Desativar (remover) Professores existentes.
- [x] Registrar sua própria presença (limitado a uma vez por dia).
- [x] Visualizar seu histórico completo de presenças.
- [x] Visualizar a lista de presença de Professores em uma data específica.

### ✅ Professor (`ROLE_PROFESSOR`)
- [x] Registrar sua própria presença (limitado a uma vez por dia).
- [x] Visualizar seu histórico completo de presenças.

## 4. Estrutura do Projeto

O projeto segue estritamente os princípios da **Arquitetura Hexagonal**, separando claramente as responsabilidades em três camadas principais: `application`, `domain`, e `infrastructure`.

- **`com.projeto`** - Pacote raiz do projeto.
  - **`ProjetoApplication.java`** - Ponto de entrada da aplicação Spring Boot (`@SpringBootApplication`).
  - **`DataInitializer.java`** - Componente que popula o banco de dados com dados iniciais para desenvolvimento.

  - **`application/`** - Camada dos "Adaptadores Primários" (Driving Adapters). É a porta de entrada da aplicação, responsável por interagir com o mundo externo (como requisições HTTP).
    - **`rest/`** - Implementação da interface com o usuário via API REST.
        - `controller/` - Controladores que recebem as requisições web.
        - `dto/` - Objetos de Transferência de Dados, usados para definir os "contratos" da API.
        - `mapper/` - Interfaces do MapStruct para converter DTOs em modelos de domínio e vice-versa.
    - `config/` - Classes de configuração do Spring (ex: Spring Security).
    - `exception/` - Handler global para tratamento customizado de exceções.
  
  - **`domain/`** - O **coração da aplicação**. Contém toda a lógica de negócio e é completamente isolado de qualquer tecnologia ou framework externo.
    - `model/` - As entidades e objetos de valor do negócio (ex: `Inspetor`, `Professor`).
    - `port/` - As "Portas" da arquitetura, que são as interfaces que definem os contratos de comunicação.
        - `in/` - Portas de entrada, que definem os casos de uso que a aplicação oferece.
        - `out/` - Portas de saída, que definem as necessidades do domínio para com o mundo externo (ex: "preciso salvar um usuário").
    - `service/` - A implementação dos casos de uso definidos nas portas de entrada.

  - **`infrastructure/`** - Camada dos "Adaptadores Secundários" (Driven Adapters). Contém as implementações concretas das tecnologias que servem ao domínio.
    - `repository/` - Implementação da persistência de dados.
        - `jpa/` - Interfaces do Spring Data JPA.
        - `adapter/` - Classes que implementam as portas de saída do domínio, "adaptando" a tecnologia (JPA) à necessidade do negócio.



## 5. Como Executar

**Pré-requisitos:**
- Java 17 (JDK)
- Apache Maven 3.8+

**Passos:**
1.  Clone o repositório:
    ```bash
    git clone <https://github.com/pro-pedropaulo/school-project>
    ```
2.  Compile o projeto usando o Maven:
    ```bash
    mvn clean install
    ```
3.  Execute a aplicação:
    ```bash
    mvn spring-boot:run
    ```
4.  A API estará disponível em `http://localhost:8080`. Um usuário `diretor` com senha `password` será criado automaticamente para o primeiro uso.

## 6. Como Testar com Insomnia

Para facilitar os testes de todos os endpoints, uma collection completa do Insomnia está disponível dentro do projeto.

- **Localização:** `cd\school\src\test\collection`

**Guia Rápido de Testes:**
1.  Abra o Insomnia.
2.  Importe a collection a partir do arquivo.
3.  Com a aplicação rodando, execute a requisição `1. Autenticação > Realizar Login` com as credenciais do `diretor` para obter um token de acesso.
4.  As requisições na pasta `2. Diretor` usarão este token automaticamente.
5.  Para testar os outros papéis, crie um Inspetor e um Professor através dos endpoints adequados. Em seguida, use a requisição `Realizar Login` com as novas credenciais para obter os tokens específicos de cada papel e configure-os manualmente nas abas `Auth` das requisições correspondentes.

## 7. Endpoints da API

#### Autenticação
| Método | URI | Acesso | Descrição |
| :--- | :--- | :--- | :--- |
| `POST` | `/auth/login` | Público | Realiza a autenticação e retorna um token JWT. |

#### Diretor
| Método | URI | Acesso | Descrição |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/diretor/inspetores` | `DIRETOR` | Cadastra um novo inspetor. |
| `DELETE` | `/api/diretor/inspetores/{id}` | `DIRETOR` | Desativa um inspetor. |
| `GET` | `/api/diretor/inspetores` | `DIRETOR` | Lista todos os inspetores ativos. |
| `GET` | `/api/diretor/presencas/inspetores` | `DIRETOR` | Lista presenças de inspetores em uma data. |
| `GET` | `/api/diretor/presencas/professores` | `DIRETOR` | Lista presenças de professores em uma data. |

#### Inspetor
| Método | URI | Acesso | Descrição |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/inspetor/professores` | `INSPETOR` | Cadastra um novo professor. |
| `DELETE` | `/api/inspetor/professores/{id}` | `INSPETOR` | Desativa um professor. |
| `POST` | `/api/inspetor/presenca` | `INSPETOR` | Registra a própria presença. |
| `GET` | `/api/inspetor/presencas` | `INSPETOR` | Lista o próprio histórico de presenças. |
| `GET` | `/api/inspetor/presencas/professores` | `INSPETOR` | Lista presenças de professores em uma data. |

#### Professor
| Método | URI | Acesso | Descrição |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/professor/presenca` | `PROFESSOR` | Registra a própria presença. |
| `GET` | `/api/professor/presencas` | `PROFESSOR` | Lista o próprio histórico de presenças. |

---
**Autor:**
Pedro Paulo Silva
<https://www.linkedin.com/in/pedropaulobc/>

**Licença:**
Este projeto está licenciado sob a Licença MIT.
