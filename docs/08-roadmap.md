# Roadmap do Whisker

## Visão Geral

O desenvolvimento do Whisker será realizado de forma incremental, priorizando primeiro a construção da infraestrutura principal do sistema e posteriormente adicionando recursos avançados de análise, aprendizado e experiência do usuário.

Cada fase possui objetivos técnicos e objetivos de aprendizado específicos.

---

# Fase 0 — Planejamento

## Objetivo

Definir arquitetura, escopo e estrutura inicial do projeto.

---

## Entregáveis

- Estrutura do monorepo.
- Documentação inicial.
- Definição dos eventos.
- Definição das filas RabbitMQ.
- Modelo de feedbacks.
- Roadmap do projeto.

---

## Conceitos Estudados

- Arquitetura orientada a eventos.
- RabbitMQ.
- Event Storming.
- Monorepos.

---

# Fase 1 — MVP Local

## Objetivo

Criar a primeira versão funcional do Whisker sem IA.

---

## Entregáveis

### Whisker Core

- Monitoramento de arquivos.
- Scheduler.
- Rule Engine simples.
- Geração de feedbacks locais.

### CLI

- Exibição de feedbacks.
- Dashboard básico.
- Comandos iniciais.

---

## Funcionalidades

### Monitoramento

Detectar:

- Arquivos criados.
- Arquivos modificados.
- Arquivos removidos.

### Regras

Detectar:

- Métodos longos.
- Classes grandes.
- Muitos parâmetros.
- Comentários TODO.

### Relatórios

Gerar:

```text
reports/

2026-06-01.md
```

---

## Conceitos Estudados

- WatchService.
- Java NIO.
- Multithreading.
- Executors.
- File I/O.

---

# Fase 2 — Mensageria

## Objetivo

Transformar a comunicação do sistema em arquitetura orientada a eventos.

---

## Entregáveis

### RabbitMQ

- Exchanges.
- Queues.
- Routing Keys.

### Core

- Event Publisher.
- Event Consumer.

### CLI

- Consumo de eventos.

---

## Eventos

```text
analysis.request

analysis.started

analysis.finished

analysis.failed

feedback.generated
```

---

## Conceitos Estudados

- RabbitMQ.
- Producers.
- Consumers.
- Exchanges.
- Queues.
- DLQ.
- Retry.

---

# Fase 3 — Integração com Git

## Objetivo

Analisar apenas alterações relevantes.

---

## Entregáveis

### Git Analyzer

- Git Diff.
- Histórico.
- Hotspots.

---

## Funcionalidades

### Contexto

Identificar:

- Arquivos alterados.
- Linhas modificadas.
- Histórico recente.

---

## Conceitos Estudados

- JGit.
- Git Internals.
- Diff Analysis.

---

# Fase 4 — Primeira Integração com IA

## Objetivo

Adicionar análises inteligentes.

---

## Entregáveis

### AI Analyzer

- Integração com Gemini.
- Prompt Builder.
- Feedback Builder.

---

## Funcionalidades

### Análises

Detectar:

- Possíveis bugs.
- Problemas arquiteturais.
- Sugestões de melhoria.

### Aprendizado

Gerar:

- Recomendações de estudo.
- Conceitos relacionados.

---

## Conceitos Estudados

- Prompt Engineering.
- APIs de IA.
- Context Management.
- Token Optimization.

---

# Fase 5 — Desktop MVP

## Objetivo

Criar a primeira interface gráfica.

---

## Entregáveis

### Dashboard

- Feedbacks.
- Histórico.
- Métricas.

### Configurações

- Intervalo de análise.
- Notificações.
- IA.

---

## Conceitos Estudados

- Flutter Desktop.
- State Management.
- Comunicação com RabbitMQ.

---

# Fase 6 — Mascote Whisker

## Objetivo

Introduzir personalidade ao sistema.

---

## Entregáveis

### Estados

- Feliz.
- Curioso.
- Alerta.
- Pensando.
- Dormindo.

### Mensagens

- Feedback humanizado.
- Curiosidades.
- Elogios.

---

## Funcionalidades

### Notificações Visuais

```text
😺 Miau!

Encontrei algo interessante.
```

### Notificações Sonoras

```text
*meow*
```

---

## Conceitos Estudados

- UX.
- Gamificação.
- Design de interação.

---

# Fase 7 — Análise Arquitetural

## Objetivo

Avaliar a estrutura global dos projetos.

---

## Entregáveis

### Análises

- Acoplamento.
- Coesão.
- Dependências circulares.
- Violação de camadas.

---

## Conceitos Estudados

- AST.
- Engenharia de Software.
- Arquitetura de Sistemas.

---

# Fase 8 — Perfil Técnico

## Objetivo

Construir um perfil de aprendizado do desenvolvedor.

---

## Funcionalidades

### Identificação de Tendências

Exemplo:

```text
Você demonstra domínio em:

- APIs REST
- Spring Boot

Próximos tópicos:

- Testes
- Concorrência
```

---

### Recomendações

- Livros.
- Artigos.
- Temas de estudo.

---

## Conceitos Estudados

- Sistemas de recomendação.
- Perfilamento.
- Análise comportamental.

---

# Fase 9 — Dashboard de Evolução

## Objetivo

Acompanhar crescimento ao longo do tempo.

---

## Métricas

### Qualidade

- Warnings.
- Duplicações.
- Acoplamento.

### Aprendizado

- Temas estudados.
- Sugestões concluídas.

### Consistência

- Dias ativos.
- Análises realizadas.

---

## Conceitos Estudados

- Analytics.
- Métricas.
- Data Visualization.

---

# Fase 10 — Integrações

## Objetivo

Expandir o ecossistema.

---

## Integrações Planejadas

### IDEs

- VS Code.
- IntelliJ IDEA.

### Plataformas

- GitHub.
- GitLab.

### Comunicação

- Discord.
- Slack.

---

## Conceitos Estudados

- Plugins.
- Webhooks.
- APIs externas.

---

# Fase 11 — Whisker 1.0

## Objetivo

Primeira versão completa do produto.

---

## Requisitos

### Core

- Estável.
- Escalável.
- Documentado.

### CLI

- Completo.

### Desktop

- Completo.

### IA

- Integrada.

### Histórico

- Persistente.

### Perfil Técnico

- Funcional.

### Dashboard

- Funcional.

---

# Backlog Futuro

## IA Local

Suporte para:

- Ollama.
- Qwen.
- DeepSeek.
- Llama.

---

## Vetorização

Busca semântica sobre histórico.

---

## Multi-Projetos

Monitoramento simultâneo de múltiplos repositórios.

---

## Aplicativo Mobile

Dashboard mobile para acompanhamento remoto.

---

## Agente Autônomo

Capacidade de executar análises periódicas sem intervenção do usuário.

---

# Meta Final

Construir uma plataforma capaz de combinar monitoramento contínuo, Inteligência Artificial, análise arquitetural e aprendizado personalizado, servindo simultaneamente como ferramenta prática para desenvolvedores e laboratório avançado para estudo de Java, concorrência, mensageria, sistemas distribuídos e engenharia de software.
