# Whisker Core

## Visão Geral

O Whisker Core é o componente central do sistema.

Toda a lógica de negócio, processamento de eventos, monitoramento de arquivos, integração com Git, execução de regras locais e comunicação com modelos de Inteligência Artificial são executados neste módulo.

Seu principal objetivo é transformar alterações realizadas em um projeto de software em feedbacks técnicos relevantes, distribuídos posteriormente para os consumidores do ecossistema Whisker.

---

# Responsabilidades

O Core é responsável por:

- Monitorar alterações em arquivos.
- Detectar mudanças em projetos.
- Coletar contexto através do Git.
- Executar análises locais.
- Coordenar chamadas para IA.
- Produzir feedbacks.
- Publicar eventos.
- Gerenciar cache de análises.
- Controlar ciclos de execução.
- Persistir relatórios e histórico.

---

# Objetivos

O Core foi projetado para atender aos seguintes objetivos:

## Eficiência

Evitar chamadas desnecessárias para modelos de IA.

## Escalabilidade

Permitir múltiplos consumidores e futuras integrações.

## Baixo Acoplamento

Não depender diretamente de CLI, Desktop ou futuras interfaces.

## Extensibilidade

Permitir adição de novas regras e mecanismos de análise sem alterações estruturais.

## Observabilidade

Manter logs e histórico de execução para auditoria e depuração.

---

# Arquitetura Interna

```text
                    Whisker Core

 ┌─────────────────────────────────────────────┐
 │                                             │
 │ File Watcher                                │
 │ Scheduler                                   │
 │ Git Analyzer                                │
 │ Rule Engine                                 │
 │ AI Analyzer                                 │
 │ Event Publisher                             │
 │ Persistence Layer                           │
 │ Cache Layer                                 │
 │                                             │
 └─────────────────────────────────────────────┘
```

---

# Módulos Internos

## File Watcher

Responsável por monitorar alterações em arquivos.

### Tecnologias

- WatchService

### Responsabilidades

- Detectar criação de arquivos.
- Detectar remoção de arquivos.
- Detectar modificações.
- Registrar eventos internamente.

### Eventos Produzidos

```text
FILE_CREATED
FILE_MODIFIED
FILE_DELETED
```

---

## Scheduler

Responsável por controlar quando análises devem ocorrer.

### Objetivo

Evitar chamadas para IA a cada modificação.

### Funcionamento

As alterações são agrupadas em janelas de tempo.

Exemplo:

```text
14:00 -> alteração
14:05 -> alteração
14:11 -> alteração

14:30 -> análise executada
```

### Configuração

```yaml
analysisInterval: 30m
```

---

## Git Analyzer

Responsável por coletar contexto das alterações.

### Tecnologias

- JGit

### Funções

- Obter arquivos modificados.
- Executar diffs.
- Identificar contexto das mudanças.
- Obter histórico recente.

### Exemplo

Ao invés de enviar:

```text
UserService.java
1200 linhas
```

O sistema envia:

```text
Método calculateRisk alterado.

Linhas adicionadas:
+ ...

Linhas removidas:
- ...
```

---

## Rule Engine

Responsável pelas análises locais.

### Objetivo

Executar verificações rápidas sem depender de IA.

### Benefícios

- Custo zero.
- Resposta imediata.
- Menor utilização de tokens.

### Exemplos de Regras

#### Estrutura

```text
Método muito longo.
```

#### Complexidade

```text
Excesso de condicionais.
```

#### Acoplamento

```text
Classe com muitas dependências.
```

#### Nomeação

```text
Variáveis pouco descritivas.
```

#### Arquitetura

```text
Controller contendo regra de negócio.
```

---

# Sistema de Análise em Camadas

O Core utiliza três níveis de análise.

---

## Nível 1 - Análise Local

Executada imediatamente.

### Utiliza

- Rule Engine
- Git Analyzer

### Objetivo

Detectar problemas simples rapidamente.

### Custo

```text
Zero.
```

---

## Nível 2 - Análise Assistida por IA

Executada periodicamente.

### Utiliza

- AI Analyzer

### Objetivo

Avaliar contexto das alterações.

### Entrada

```text
Git Diff
+
Contexto relevante
```

### Saída

```text
Feedback técnico.
```

---

## Nível 3 - Análise Profunda

Executada sob demanda.

### Comando

```bash
whisker analyze --deep
```

### Objetivo

Analisar:

- Arquivos completos.
- Módulos.
- Estrutura arquitetural.
- Fluxos de negócio.

### Casos de Uso

- Pull Requests.
- Refatorações.
- Revisões técnicas.

---

# AI Analyzer

Responsável pela comunicação com modelos generativos.

---

## Objetivos

- Detectar code smells.
- Identificar potenciais bugs.
- Avaliar arquitetura.
- Gerar feedback contextual.
- Recomendar estudos.

---

## Estratégia de Consumo

A IA nunca recebe automaticamente o projeto completo.

O Core prioriza:

```text
Git Diff
+
Trechos relevantes
+
Contexto mínimo necessário
```

---

## Benefícios

- Menor custo.
- Menor latência.
- Melhor precisão.
- Escalabilidade.

---

# Cache de Análises

## Objetivo

Evitar análises repetidas.

---

## Funcionamento

Cada arquivo recebe um hash.

Exemplo:

```text
SHA-256
```

---

### Fluxo

```text
Arquivo alterado
      │
      ▼

Hash calculado
      │
      ▼

Já analisado?
      │
 ┌────┴────┐
 │         │
Sim       Não
 │         │
 ▼         ▼

Ignora   Analisa
```

---

# Event Publisher

Responsável por publicar eventos no RabbitMQ.

---

## Eventos de Sistema

```text
analysis.started
analysis.finished
analysis.failed
```

---

## Eventos de Feedback

```text
feedback.warning
feedback.success
feedback.learning
feedback.architecture
feedback.performance
feedback.security
```

---

# Persistência

Responsável por armazenar informações produzidas pelo sistema.

---

## Histórico

Armazena:

- Data.
- Arquivo.
- Tipo de feedback.
- Resultado.

---

## Relatórios

Exemplo:

```text
reports/

2026-06-01.md
2026-06-02.md
```

---

# Multithreading

O Core utiliza múltiplas threads para maximizar desempenho.

---

## FileWatcherThread

Responsável por:

- WatchService.
- Eventos de arquivo.

---

## SchedulerThread

Responsável por:

- Agrupamento de alterações.
- Disparo das análises.

---

## AIWorkerPool

Responsável por:

- Chamadas para IA.
- Processamento paralelo.

Configuração inicial:

```yaml
aiWorkers: 2
```

---

## MessagingThread

Responsável por:

- RabbitMQ.
- Publicação e consumo.

---

## PersistenceThread

Responsável por:

- Escrita de relatórios.
- Histórico.
- Cache.

---

# Fluxo Completo

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

Git Analyzer
        │
        ▼

Rule Engine
        │
        ▼

AI Analyzer
        │
        ▼

Feedback Builder
        │
        ▼

RabbitMQ
        │
        ▼

CLI / Desktop
```

---

# Configurações

Exemplo inicial:

```yaml
projectPath: C:/Projetos/smartDoc

analysisInterval: 30m

aiWorkers: 2

enableAI: true

enableNotifications: true

deepAnalysisEnabled: true

cacheEnabled: true
```

---

# Futuras Evoluções

## Priorização Inteligente

Arquivos críticos poderão receber análises imediatas.

Exemplos:

- Segurança.
- Autenticação.
- Pagamentos.

---

## Aprendizado de Perfil

O sistema poderá adaptar suas análises conforme o perfil do desenvolvedor.

---

## Análise Arquitetural Global

Avaliação de:

- Módulos.
- Dependências.
- Fluxos.
- Acoplamento geral.

---

## Suporte a Múltiplos Modelos

Possibilidade de alternar entre diferentes provedores de IA sem alterar a arquitetura principal.

---

# Objetivo do Core

Transformar alterações de código em conhecimento técnico acionável, utilizando uma combinação de regras locais, processamento assíncrono, mensageria e Inteligência Artificial para fornecer feedback contínuo e auxiliar no desenvolvimento profissional do usuário.
