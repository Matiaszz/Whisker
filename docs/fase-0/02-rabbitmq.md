# RabbitMQ

## Objetivo

Realizar a comunicação assíncrona entre os componentes do Whisker.

---

# Exchanges

## analysis.exchange

Responsável pelos eventos de análise.

Routing Keys:

```text
analysis.requested
analysis.started
analysis.completed
analysis.failed
```

---

## feedback.exchange

Responsável pelos feedbacks.

Routing Keys:

```text
feedback.generated
```

---

## system.exchange

Responsável pelos eventos internos.

Routing Keys:

```text
system.started
system.stopped
system.error
```

---

# Queues

## analysis.queue

Consumida pelo Core.

Eventos:

```text
analysis.requested
```

---

## feedback.cli.queue

Consumida pelo CLI.

Eventos:

```text
feedback.generated
```

---

## feedback.desktop.queue

Consumida pelo Desktop.

Eventos:

```text
feedback.generated
```

---

## system.cli.queue

Consumida pelo CLI.

Eventos:

```text
system.*
```

---

## system.desktop.queue

Consumida pelo Desktop.

Eventos:

```text
system.*
```

---

# Fluxo

```text
CLI/Desktop
      │
      ▼

analysis.exchange

      │
      ▼

analysis.queue

      │
      ▼

Whisker Core

      │
      ▼

feedback.exchange

      │
      ├────► feedback.cli.queue
      │
      └────► feedback.desktop.queue
```
