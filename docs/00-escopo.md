# Escopo do Projeto - Whisker

## 1. Descrição do Projeto

O Whisker é uma plataforma de análise técnica assistida por Inteligência Artificial voltada para desenvolvedores de software.

O sistema monitora alterações realizadas em projetos de desenvolvimento, identifica potenciais problemas, boas práticas, oportunidades de melhoria e temas de estudo relevantes, fornecendo feedback contínuo ao usuário.

A solução é composta por múltiplos módulos desacoplados que se comunicam através de arquitetura orientada a eventos, permitindo escalabilidade, extensibilidade e independência entre componentes.

Além de servir como ferramenta de apoio ao desenvolvimento de software, o projeto possui caráter educacional, auxiliando programadores em sua evolução técnica por meio de recomendações contextualizadas e acompanhamento contínuo.

---

# 2. Objetivo Geral

Desenvolver uma plataforma capaz de monitorar projetos de software e gerar feedback técnico automatizado utilizando Inteligência Artificial, mensageria, processamento assíncrono e arquitetura orientada a eventos.

---

# 3. Objetivos Específicos

- Monitorar alterações em projetos de software.
- Detectar modificações relevantes através de integração com Git.
- Processar eventos de forma assíncrona.
- Utilizar modelos de Inteligência Artificial para análise contextual de código.
- Produzir feedbacks técnicos estruturados.
- Recomendar tópicos de estudo relacionados às alterações realizadas.
- Exibir resultados em interfaces CLI e Desktop.
- Registrar histórico de análises.
- Acompanhar a evolução técnica do usuário.
- Explorar conceitos avançados de Java, concorrência e mensageria.

---

# 4. Público-Alvo

O sistema é destinado a:

- Estudantes de programação.
- Desenvolvedores iniciantes.
- Desenvolvedores back-end.
- Desenvolvedores full-stack.
- Profissionais que desejam receber feedback contínuo sobre seu código.
- Usuários interessados em acelerar seu aprendizado técnico.

---

# 5. Escopo Funcional

## 5.1 Monitoramento de Projetos

O sistema deverá permitir:

- Selecionar diretórios para monitoramento.
- Detectar alterações em arquivos.
- Detectar criação de arquivos.
- Detectar exclusão de arquivos.
- Agrupar alterações para análise posterior.

---

## 5.2 Integração com Git

O sistema deverá:

- Identificar arquivos modificados.
- Obter diferenças (diffs) entre versões.
- Identificar hotspots de alteração.
- Utilizar histórico recente para contextualização.

---

## 5.3 Análise Local

O sistema deverá realizar análises sem utilização de IA para:

- Métodos muito longos.
- Classes muito grandes.
- Dependências excessivas.
- Complexidade elevada.
- Possíveis violações arquiteturais.
- Problemas de nomeação.

---

## 5.4 Análise Assistida por IA

O sistema deverá utilizar Inteligência Artificial para:

- Detectar potenciais bugs.
- Identificar code smells.
- Avaliar decisões arquiteturais.
- Sugerir melhorias técnicas.
- Recomendar tópicos de estudo.
- Produzir feedback contextualizado.

---

## 5.5 Sistema de Feedbacks

O sistema deverá gerar feedbacks classificados em:

- Problemas Potenciais.
- Arquitetura.
- Qualidade de Código.
- Performance.
- Segurança.
- Testabilidade.
- Boas Práticas.
- Oportunidades de Aprendizado.
- Evolução Técnica.

---

## 5.6 Interface CLI

O sistema deverá disponibilizar:

- Visualização de feedbacks em tempo real.
- Dashboard textual.
- Solicitação manual de análises.
- Consulta de relatórios.
- Monitoramento do estado do sistema.

---

## 5.7 Interface Desktop

O sistema deverá disponibilizar:

- Dashboard visual.
- Histórico de análises.
- Métricas de evolução.
- Sistema de notificações.
- Configurações do sistema.
- Interação com mascote virtual.

---

## 5.8 Histórico

O sistema deverá armazenar:

- Feedbacks produzidos.
- Datas das análises.
- Métricas históricas.
- Evolução técnica do usuário.

---

# 6. Escopo Técnico

## Linguagens

- Java 21
- Dart

---

## Frameworks e Bibliotecas

### Core

- Maven
- JGit
- RabbitMQ Client
- SLF4J
- Logback

### CLI

- Picocli
- Lanterna

### Desktop

- Flutter Desktop
- Hive
- fl_chart
- flutter_local_notifications

---

## Mensageria

- RabbitMQ

---

## Inteligência Artificial

- Gemini API

---

## Controle de Versão

- Git
- GitHub

---

# 7. Requisitos Não Funcionais

## Desempenho

O sistema deve evitar chamadas excessivas à IA através de mecanismos de agrupamento de alterações e cache.

---

## Escalabilidade

Os componentes devem ser desacoplados e comunicarem-se através de eventos.

---

## Manutenibilidade

A arquitetura deve permitir inclusão de novos tipos de análise sem alterações significativas nos módulos existentes.

---

## Observabilidade

O sistema deve registrar logs e eventos relevantes para auditoria e depuração.

---

## Extensibilidade

Novos consumidores, interfaces e provedores de IA poderão ser adicionados futuramente.

---

# 8. Restrições do Projeto

- O sistema não realizará modificações automáticas no código-fonte.
- O sistema não executará correções automáticas.
- O sistema não substituirá ferramentas de revisão humana.
- O sistema não analisará projetos completos continuamente devido a restrições de custo computacional.
- O sistema priorizará análise incremental baseada em alterações.

---

# 9. Resultados Esperados

Ao final do projeto espera-se obter uma plataforma capaz de:

- Monitorar alterações em projetos de software.
- Produzir feedback técnico automatizado.
- Auxiliar desenvolvedores em seu processo de aprendizado.
- Demonstrar o uso prático de Inteligência Artificial aplicada ao desenvolvimento.
- Explorar conceitos avançados de Java, mensageria, concorrência e arquitetura orientada a eventos.
- Fornecer uma experiência diferenciada através de interfaces CLI e Desktop integradas.

---

# 10. Fora do Escopo

As funcionalidades abaixo não fazem parte da versão inicial do projeto:

- Correção automática de código.
- Geração automática de Pull Requests.
- Integração com IDEs.
- Dashboard Web.
- Aplicativo Mobile.
- Execução local de modelos de IA.
- Suporte multiusuário.
- Integração com plataformas de CI/CD.
- Integração com GitHub, Discord ou Slack.

Essas funcionalidades poderão ser consideradas em versões futuras do sistema.
