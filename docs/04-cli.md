# Whisker CLI

## Visão Geral

O Whisker CLI é a interface de linha de comando do ecossistema Whisker.

Seu objetivo é fornecer uma experiência rápida, leve e acessível para interação com o sistema, permitindo que desenvolvedores acompanhem análises, recebam feedbacks e solicitem operações diretamente pelo terminal.

Diferentemente do Desktop App, o CLI prioriza produtividade e velocidade, tornando-se ideal para usuários que passam grande parte do tempo em terminais, SSHs, ambientes Linux ou workflows automatizados.

---

# Objetivos

O CLI foi projetado para:

- Exibir feedbacks em tempo real.
- Solicitar análises ao Core.
- Visualizar relatórios.
- Receber notificações.
- Acompanhar o status do sistema.
- Operar sem dependência da interface gráfica.

---

# Responsabilidades

## Consumo de Eventos

Consumir eventos produzidos pelo Whisker Core.

## Exibição de Feedbacks

Apresentar resultados das análises de forma clara.

## Solicitação de Análises

Permitir que o usuário solicite análises manuais.

## Visualização de Relatórios

Exibir relatórios históricos diretamente no terminal.

## Monitoramento

Acompanhar o estado do sistema em tempo real.

---

# Tecnologias

## Linguagem

- Java 21

## Framework CLI

- Picocli

## Interface Terminal

- Lanterna

## Mensageria

- RabbitMQ Client

## Logging

- SLF4J
- Logback

---

# Arquitetura

```text
          RabbitMQ
               ▲
               │
               ▼

        ┌─────────────┐
        │ Whisker CLI │
        └─────────────┘

        ├─ Command Handler
        ├─ Event Consumer
        ├─ Notification Center
        ├─ Dashboard Renderer
        └─ Report Viewer
```

---

# Módulos Internos

## Command Handler

Responsável pelo processamento dos comandos do usuário.

### Exemplos

```bash
whisker start
```

```bash
whisker analyze
```

```bash
whisker report
```

```bash
whisker status
```

```bash
whisker config
```

---

## Event Consumer

Responsável por consumir eventos publicados pelo Core.

### Eventos Consumidos

```text
feedback.warning

feedback.success

feedback.learning

feedback.performance

feedback.security

feedback.architecture

analysis.started

analysis.finished

analysis.failed
```

---

## Dashboard Renderer

Responsável por renderizar painéis e informações no terminal.

### Exemplo

```text
╔══════════════════════════════╗
║          WHISKER            ║
╠══════════════════════════════╣
║ Warnings: 3                 ║
║ Sugestões: 5                ║
║ Boas práticas: 12           ║
║ Última análise: 14:30       ║
╚══════════════════════════════╝
```

---

## Notification Center

Responsável por exibir notificações em tempo real.

### Exemplo

```text
⚠ Método muito longo detectado

Arquivo:
UserService.java

Sugestão:
Considere extrair responsabilidades.
```

---

## Report Viewer

Responsável pela visualização de relatórios gerados pelo Core.

### Exemplo

```bash
whisker report today
```

Saída:

```text
Relatório - 01/06/2026

Warnings: 4

Boas práticas: 8

Sugestões de estudo: 3
```

---

# Fluxo de Comunicação

## Solicitação de Análise

```text
Usuário
    │
    ▼

whisker analyze

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

RabbitMQ

    │
    ▼

CLI
```

---

## Recebimento de Feedback

```text
Core
   │
   ▼

RabbitMQ

   │
   ▼

CLI

   │
   ▼

Terminal
```

---

# Sistema de Notificações

O CLI deve ser capaz de informar eventos importantes sem interromper o fluxo do usuário.

---

## Tipos de Notificação

### Sucesso

```text
✓ Nenhum problema relevante encontrado.
```

---

### Informação

```text
ℹ Nova análise concluída.
```

---

### Aviso

```text
⚠ Complexidade elevada detectada.
```

---

### Erro

```text
✖ Falha ao processar análise.
```

---

# Mascote no Terminal

O CLI poderá exibir uma versão simplificada do mascote Whisker.

---

## Estado Neutro

```text
 /\_/\\
( o.o )
 > ^ <
```

---

## Estado Feliz

```text
 /\_/\\
( ^.^ )
 > ^ <
```

---

## Estado de Alerta

```text
 /\_/\\
( o_o )
 > ^ <
```

---

## Estado de Erro

```text
 /\_/\\
( x.x )
 > ^ <
```

---

# Sons e Alertas

Funcionalidade opcional.

Quando habilitada, o CLI poderá emitir sons para:

- Problemas críticos.
- Conclusão de análises.
- Sugestões importantes.

Configuração:

```yaml
enableSounds: true
```

---

# Comandos Planejados

## Sistema

```bash
whisker start
```

Inicializa o cliente.

---

```bash
whisker stop
```

Finaliza o cliente.

---

```bash
whisker status
```

Exibe informações do sistema.

---

## Análises

```bash
whisker analyze
```

Solicita análise padrão.

---

```bash
whisker analyze --deep
```

Solicita análise profunda.

---

```bash
whisker analyze --file UserService.java
```

Solicita análise específica.

---

## Relatórios

```bash
whisker report
```

Último relatório.

---

```bash
whisker report today
```

Relatório diário.

---

```bash
whisker report week
```

Relatório semanal.

---

## Configuração

```bash
whisker config
```

Visualiza configurações.

---

```bash
whisker config set analysisInterval 30m
```

Altera configurações.

---

# Modo Watch

Modo contínuo de observação.

Comando:

```bash
whisker watch
```

Nesse modo, o CLI permanece conectado ao RabbitMQ exibindo eventos em tempo real.

Exemplo:

```text
[14:32]

⚠ Método muito longo detectado.

Arquivo:
UserService.java
```

---

# Futuras Evoluções

## Tema Customizável

Suporte a diferentes estilos visuais.

---

## Dashboard Interativo

Navegação por teclado.

---

## Filtros

Exibir apenas:

- Segurança.
- Performance.
- Arquitetura.
- Aprendizado.

---

## Integração com Git

Exibir feedbacks associados ao commit atual.

---

# Objetivo do CLI

Fornecer uma interface rápida, eficiente e produtiva para interação com o ecossistema Whisker, permitindo que desenvolvedores recebam feedback contínuo, acompanhem sua evolução técnica e utilizem os recursos da plataforma sem depender de interfaces gráficas.
