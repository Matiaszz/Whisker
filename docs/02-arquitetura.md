# Arquitetura do Whisker

## Visão Geral

O Whisker foi projetado seguindo princípios de arquitetura orientada a eventos (Event-Driven Architecture), onde os componentes do sistema se comunicam através de mensagens assíncronas.

O objetivo dessa abordagem é promover:

- Baixo acoplamento.
- Alta coesão.
- Escalabilidade.
- Facilidade de manutenção.
- Independência entre componentes.

Cada aplicação possui uma responsabilidade bem definida e pode evoluir independentemente das demais.

---

# Arquitetura Geral

```text
                   RabbitMQ
                        ▲
                        │
                        ▼

               ┌──────────────┐
               │ Whisker Core │
               └──────────────┘

               ├─ WatchService
               ├─ Git Analyzer
               ├─ Rule Engine
               ├─ AI Analyzer
               ├─ Scheduler
               └─ Event Publisher

                        ▲
                        │
                        ▼

                   RabbitMQ

                 ▲         ▲
                 │         │

                 ▼         ▼

          ┌──────────┐ ┌──────────┐
          │   CLI    │ │ Desktop  │
          └──────────┘ └──────────┘
```

---

# Componentes

## Whisker Core

O Core é o cérebro do sistema.

Toda a lógica de negócio está concentrada neste módulo.

### Responsabilidades

- Monitorar alterações em arquivos.
- Observar mudanças no Git.
- Gerenciar filas internas de processamento.
- Executar regras locais.
- Integrar com IA.
- Publicar eventos.
- Coordenar análises.

### Tecnologias

- Java 21
- Maven
- JGit
- WatchService
- RabbitMQ Client
- Gemini API / Gemini CLI
- SLF4J
- Logback

---

## Whisker CLI

Aplicação de terminal responsável por exibir feedbacks e permitir interação rápida com o sistema.

### Responsabilidades

- Exibir notificações.
- Mostrar relatórios.
- Solicitar análises.
- Consumir eventos.

### Tecnologias

- Java
- Picocli
- Lanterna
- RabbitMQ Client

---

## Whisker Desktop

Aplicação desktop responsável pela experiência visual.

### Responsabilidades

- Dashboard.
- Histórico de análises.
- Configurações.
- Notificações locais.
- Mascote virtual.

### Tecnologias

- Flutter Desktop
- Dart
- RabbitMQ Client
- flutter_local_notifications

---

# Arquitetura Orientada a Eventos

O Whisker utiliza RabbitMQ como mecanismo de comunicação.

Nenhum componente se comunica diretamente com outro.

Todas as interações acontecem através de eventos.

---

## Exemplo

Ao solicitar uma análise:

```text
Desktop
   │
   ▼

REQUEST_ANALYSIS

   │
   ▼

RabbitMQ

   │
   ▼

Core

   │
   ▼

IA

   │
   ▼

ANALYSIS_RESULT

   │
   ▼

RabbitMQ

   │
   ▼

Desktop + CLI
```

Essa abordagem permite adicionar novos consumidores futuramente sem modificar o Core.

---

# Exchanges

A comunicação será organizada através de Exchanges.

## analysis.exchange

Responsável pelos eventos de análise.

### Eventos

```text
analysis.request
analysis.started
analysis.finished
analysis.failed
```

---

## feedback.exchange

Responsável pelos feedbacks produzidos.

### Eventos

```text
feedback.warning
feedback.learning
feedback.success
feedback.architecture
feedback.security
feedback.performance
```

---

## system.exchange

Responsável por eventos internos.

### Eventos

```text
system.started
system.stopped
system.error
system.notification
```

---

# Modelo de Eventos

Todos os eventos devem possuir uma estrutura padronizada.

## Estrutura Base

```json
{
  "eventId": "uuid",
  "eventType": "feedback.warning",
  "timestamp": "2026-06-01T10:00:00Z",
  "source": "whisker-core",
  "payload": {}
}
```

---

# Request / Reply Pattern

O sistema utilizará Request/Reply para solicitações de análise.

---

## Requisição

```json
{
  "requestId": "123",
  "projectPath": "C:/Projetos/smartDoc",
  "analysisType": "FULL"
}
```

---

## Resposta

```json
{
  "requestId": "123",
  "status": "SUCCESS",
  "warnings": 4,
  "suggestions": 2
}
```

---

# Multithreading

O Core utilizará múltiplas threads para evitar bloqueios e melhorar desempenho.

---

## Thread de Monitoramento

Responsável por:

- WatchService.
- Observação de alterações.

```text
FileWatcherThread
```

---

## Thread de Scheduler

Responsável por:

- Agrupamento de alterações.
- Agendamento das análises.

```text
SchedulerThread
```

---

## Thread Pool de IA

Responsável por:

- Processamento das análises.
- Comunicação com modelos de IA.

```text
AIWorkerPool
```

Configuração inicial:

```yaml
aiWorkers: 2
```

---

## Thread de Mensageria

Responsável por:

- Consumo de eventos.
- Publicação de mensagens.

```text
MessagingThread
```

---

## Thread de Persistência

Responsável por:

- Histórico.
- Relatórios.
- Cache.

```text
PersistenceThread
```

---

# Fluxo de Monitoramento

```text
Arquivo alterado
       │
       ▼

WatchService

       │
       ▼

Fila Interna

       │
       ▼

Scheduler

       │
       ▼

Git Diff

       │
       ▼

Rule Engine

       │
       ▼

AI Analyzer

       │
       ▼

RabbitMQ

       │
       ▼

CLI / Desktop
```

---

# Fluxo de Análise

## Passo 1

Arquivo modificado.

---

## Passo 2

WatchService registra alteração.

---

## Passo 3

Alteração é armazenada em uma fila interna.

---

## Passo 4

Scheduler agrupa alterações.

---

## Passo 5

Git Diff gera contexto mínimo.

---

## Passo 6

Rule Engine executa análises locais.

---

## Passo 7

IA executa análise contextual.

---

## Passo 8

Feedbacks são produzidos.

---

## Passo 9

Eventos são publicados no RabbitMQ.

---

## Passo 10

CLI e Desktop recebem os resultados.

---

# Escalabilidade

A arquitetura foi projetada para permitir expansão futura.

Novos consumidores podem ser adicionados sem alterações no Core.

Exemplos:

- Discord Bot.
- VS Code Extension.
- IntelliJ Plugin.
- Dashboard Web.
- API Pública.
- Aplicativo Mobile.

Todos consumiriam os mesmos eventos já publicados.

---

# Estrutura do Monorepo

```text
whisker/

├── docs/
│
├── Core/
│
├── CLI/
│
├── Desktop/
│
├── docker-compose.yaml
│
└── scripts/
```

---

# Decisões Arquiteturais

## Por que Java no Core?

- Excelente suporte a concorrência.
- Ecossistema maduro.
- Facilidade para integração com RabbitMQ.
- Bom desempenho em aplicações de longa duração.

---

## Por que Flutter no Desktop?

- Desenvolvimento multiplataforma.
- Interface moderna.
- Facilidade para dashboards.
- Reaproveitamento de conhecimento.

---

## Por que RabbitMQ?

- Curva de aprendizado adequada.
- Excelente para estudar mensageria.
- Suporte a Request/Reply.
- Suporte a Retry e DLQ.
- Facilidade de integração com Java.

---

# Objetivo Arquitetural

A arquitetura do Whisker busca combinar monitoramento contínuo, Inteligência Artificial, processamento assíncrono e mensageria em uma plataforma desacoplada e escalável, servindo simultaneamente como produto funcional e ambiente de aprendizado para conceitos avançados de engenharia de software.
