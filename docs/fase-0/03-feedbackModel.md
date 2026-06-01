# Modelo de Feedback

## Objetivo

Padronizar todos os feedbacks produzidos pelo Whisker.

---

# Estrutura

```json
{
  "id": "uuid",
  "analysisId": "uuid",
  "category": "QUALITY",
  "severity": "WARNING",
  "title": "Método muito longo",
  "description": "Método possui 120 linhas.",
  "recommendation": "Divida em métodos menores.",
  "learningTopics": ["Clean Code", "SRP"],
  "filePath": "src/UserService.java",
  "line": 45
}
```

---

# Categorias

```java
QUALITY
ARCHITECTURE
PERFORMANCE
SECURITY
TESTABILITY
LEARNING
SUCCESS
```

---

# Severidades

```java
INFO
SUCCESS
WARNING
CRITICAL
```

---

# Exemplo

```json
{
  "id": "fb-001",
  "analysisId": "an-001",
  "category": "ARCHITECTURE",
  "severity": "WARNING",
  "title": "Dependências excessivas",
  "description": "Classe possui 8 dependências.",
  "recommendation": "Avalie separar responsabilidades.",
  "learningTopics": ["SOLID", "Dependency Injection"],
  "filePath": "src/UserService.java",
  "line": 10
}
```

---

# Fontes de Feedback

## Rule Engine

Produz:

- QUALITY
- ARCHITECTURE
- PERFORMANCE

---

## Git Analyzer

Produz:

- LEARNING

---

## AI Analyzer

Produz:

- QUALITY
- ARCHITECTURE
- SECURITY
- TESTABILITY
- LEARNING
