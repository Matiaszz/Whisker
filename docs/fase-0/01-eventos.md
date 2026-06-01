# Eventos do Whisker

## Objetivo

Definir todos os eventos publicados e consumidos pelo sistema.

---

# Estrutura Base

```json
{
  "eventId": "uuid",
  "eventType": "analysis.requested",
  "timestamp": "2026-06-01T10:00:00Z",
  "source": "whisker-core",
  "payload": {}
}
```

---

# Eventos de Análise

## analysis.requested

Solicita uma análise.

```json
{
  "analysisId": "uuid",
  "projectPath": "C:/Projetos/demo",
  "type": "INCREMENTAL"
}
```

---

## analysis.started

Análise iniciada.

```json
{
  "analysisId": "uuid"
}
```

---

## analysis.completed

Análise concluída.

```json
{
  "analysisId": "uuid",
  "feedbackCount": 12
}
```

---

## analysis.failed

Falha na análise.

```json
{
  "analysisId": "uuid",
  "error": "Gemini timeout"
}
```

---

# Eventos de Feedback

## feedback.generated

Novo feedback gerado.

```json
{
  "feedbackId": "uuid",
  "analysisId": "uuid"
}
```

---

# Eventos de Sistema

## system.started

Sistema iniciado.

---

## system.stopped

Sistema encerrado.

---

## system.error

Erro interno detectado.

```json
{
  "message": "RabbitMQ disconnected"
}
```
