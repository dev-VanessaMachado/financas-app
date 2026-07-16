```markdown
# 💰 Painel de Finanças Pessoais

Uma aplicação full-stack moderna e reativa para controle de finanças pessoais, desenvolvida com o objetivo de aplicar as melhores práticas de arquitetura de software, design de interface de alta fidelidade e pirâmide de testes automatizados (unitários, integração e ponta a ponta).

---

## 🖥️ Demonstração Visual

O painel conta com uma interface minimalista com tema escuro (Dark/Emerald), inspirada nas fintechs mais modernas do mercado.

![Demonstração do Painel](./financas-front/assets/dashboard.png)

* **Cards de Resumo:** Exibição dinâmica de saldo atual com alertas visuais baseados no status da conta (positivo/negativo).
* **Formulário Inteligente:** Inputs modernos com estados de foco interativos e validação de tipo de transação (Receita/Despesa).
* **Extrato Reativo:** Atualização instantânea da lista de transações em tempo real sem a necessidade de recarregar a página.


## 🛠️ Tecnologias & Ferramentas Utilizadas

### **Backend**
* **Java (JDK 21):** Linguagem robusta e fortemente tipada para o núcleo de negócios.
* **Javalin:** Microframework web leve para construção de APIs REST rápidas e eficientes.
* **PostgreSQL:** Banco de dados relacional de alta performance para persistência confiável das transações.
* **JUnit & Mockito:** Testes de unidade e de integração para validação do comportamento da API.

### **Frontend**
* **Angular:** Framework SPA estruturado para garantir escalabilidade e componentização.
* **TypeScript:** Garantia de segurança de tipos e melhor legibilidade no desenvolvimento cliente.
* **Jasmine & Karma:** Framework de testes e executor de testes para a camada de visualização e serviços.
* **Cypress:** Ferramenta líder de testes E2E (End-to-End) para simulação de fluxo real de usuário.
* **Google Fonts & CSS Custom Properties:** Estilização moderna e limpa focada em consistência visual.

---

## 📐 Arquitetura do Projeto

O sistema foi arquitetado de forma totalmente desacoplada, utilizando a abordagem de **Client-Server Architecture**:


```

[ Frontend (Angular) ]  <--- HTTP / JSON --->  [ Backend (Javalin API) ]  <--- JDBC --->  [ PostgreSQL ]
|                                                 |

* Jasmine/Karma (Unit)                            - JUnit (Unit/Integration)
* Cypress (E2E)

```

---

## 🧪 Estratégia de Testes (A Pirâmide Completa!)

Este projeto foi construído sob um rigoroso fluxo de garantia de qualidade, cobrindo todas as camadas de testes:

### **1. Testes de Unidade e Integração (Backend)**
Garantem que as rotas da API, regras de negócio de saldo e transações estejam computando e respondendo com os códigos HTTP corretos (ex: `201 Created`, `200 OK`).

### **2. Testes Unitários de Componente e Serviço (Frontend - Jasmine/Karma)**
* `transacao.service.spec.ts`: Intercepta chamadas HTTP usando `HttpTestingController` para testar o comportamento de requisições de forma isolada.
* `app.component.spec.ts`: Valida a criação segura do componente principal e integridade das injeções de dependências.

### **3. Testes Ponta a Ponta (E2E - Cypress)**
Simula o comportamento de um usuário real na interface gráfica:
1. O robô visita o endereço local.
2. Preenche o formulário com uma nova receita.
3. Clica no botão de salvar.
4. Valida se o valor correto apareceu renderizado na tabela do extrato físico e se a requisição de API persistiu o registro com sucesso.

---

## ⚙️ Como Executar o Projeto Localmente

### **Pré-requisitos**
* Java JDK 21 ou superior instalado
* Node.js (versão LTS recomendada)
* PostgreSQL rodando localmente (ou Docker)

### **Passo 1: Iniciando o Backend**
1. Navegue até a pasta do backend:
   ```bash
   cd financas-app-backend

```

2. Configure as credenciais do seu banco de dados no arquivo de propriedades/configuração.
3. Execute a aplicação (usando Maven ou Gradle de sua preferência):
```bash
mvn clean install
mvn exec:java

```


*O servidor iniciará na porta `8080` por padrão.*

### **Passo 2: Iniciando o Frontend**

1. Navegue até a pasta do frontend:
```bash
cd financas-front

```


2. Instale as dependências necessárias:
```bash
npm install

```


3. Inicie o servidor de desenvolvimento do Angular:
```bash
npm start

```


*Acesse a aplicação em [http://localhost:4200](http://localhost:4200).*

---

## 🏃 Refém dos Testes? Execute-os!

Para rodar os testes unitários do frontend com o executor Karma:

```bash
npx ng test

```

Para abrir a interface gráfica do Cypress e rodar os testes de ponta a ponta interativamente:

```bash
npx cypress open

```



