# Monitoramento de Prensas Hidráulicas

Integrantes: Carlos Elian Lopes Vieira, Pedro Bobella Meneghini, Vinicius Rinas Begnini.

## Descrição do Problema

Uma metalúrgica que produz peças para a indústria automotiva está enfrentando paradas não planejadas em suas prensas hidráulicas. A falha inesperada de vedações e mangueiras causa interrupções na produção, atrasos nas entregas e altos custos com manutenção corretiva.

## Modelo Conceitual da Solução Proposta

A solução envolve a utilização de telemetria para coleta de dados em tempo real, armazenamento estruturado, e aplicação de modelos de Inteligência Artificial para prever falhas e otimizar a manutenção preventiva.

### Como Coletar os Dados

Utilização de telemetria por meio dos seguintes sensores:

- Sensor de nível (booleano)
- Sensor de pressão hidráulica (Bar - float)
- Sensor de temperatura do óleo (Graus Celsius - float)
- Sensor de ciclos de operação (Quantidade - inteiro)
- Sensor de vibração (mm/s - float)
- Data e hora da coleta (timestamp)
- Nome do sensor (string)
- Máquina ligada (booleano)

### Quais Dados Coletar

**Telemetria:**
- Nível do reservatório de óleo hidráulico
- Pressão hidráulica
- Entrada e saída de óleo do cilindro
- Próximo à válvula reguladora de pressão
- Temperatura no repositório de óleo
- Contador de ciclos de operação
- Vibração (próximo ao cilindro e motor da bomba)
- Timestamp
- Falha operacional

**Histórico de Manutenção:**
- ID da máquina
- Tipo de manutenção
- Ação realizada
- Data e hora da manutenção
- Tempo de manutenção em minutos
- ID da falha

**Histórico de Falhas:**
- ID da máquina
- Tipo de falha
- Data da falha

## Modelo Lógico do Banco de Dados
![Logic database model for hidraulic-press](https://github.com/user-attachments/assets/db7cf9b6-b9e9-4ca6-b2b5-943be4badb64)

### Periodicidade de Coleta

- A cada minuto coleta os dados de todos os sensores e insere em telemetria, apenas quando a máquina estiver ligada. Essa coleta não foi implementada, pois o sistema atual é focado na visualização dos dados, para isso se faz necessário a implementação de um dispositivo in loco conectado as máquinas. Para simulação foram inseridos dados gerados manualmente.
- Os dados de manutenção e falha são inseridos quando ocorre algum evento relacionado na fábrica.

### Escolha do Banco de Dados

**PostgreSQL:**
- Banco relacional robusto
- Consultas cruzadas simplificadas
- Facilidade de uso

### Escolha da Plataforma de Nuvem

**Google Cloud:**
- Custo beneficio
- Facilidade de gerencialmente 
- Free tier robusto

### Tecnologias Utilizadas no Desenvolvimento do Projeto

**Linguagem de Programação**
- Java e Spring Boot: Devido a sua performance e conhecimento dos integrantes do grupo

**Biblioteca**
- Chart.js: Devido facilidade de utilização para a visualização dos dados

### Modelo de Previsão – Machine Learning / Inteligência Artificial

Objetivos do modelo:
- Estimar dias restantes até a troca de mangueiras
- Alertar sobre comportamentos fora do padrão
- Prever chance de falha nas próximas 24h


### Passo a Passo de Implementação do Modelo de Regressão

1. **Preparar os Dados:**
  - Realizar limpeza e tratamento dos dados coletados.
  - Normalizar os valores numéricos para facilitar a convergência do modelo.

2. **Selecionar Variáveis Relevantes:**
  - Analisar a correlação dos dados para identificar as variáveis mais influentes.

3. **Treinar o Modelo:**
  - Utilizar um modelo de regressão.
  - Dividir os dados em treino e teste.
  - Realizar treinamento e ajustar hiperparâmetros utilizando validação cruzada.

4. **Validar e Avaliar o Modelo:**
  - Avaliar o modelo com métricas como RMSE e MAE.
  - Ajustar o modelo para melhorar o desempenho caso necessário.

5. **Integrar com o Sistema:**
  - Hospedar o modelo treinado em ambiente Google Cloud.
  - Implementar uma API RESTful para integrar o modelo com a aplicação web.

6. **Monitorar e Atualizar o Modelo:**
  - Monitorar constantemente o desempenho do modelo.
  - Realizar treinamentos periódicos com novos dados para manter o modelo atualizado e preciso.

## Fluxograma da Aplicação
![flowchart  hydraulic press app](https://github.com/user-attachments/assets/4b42ccf5-445b-40b0-ac21-1c18eb26f175)

---

Desenvolvido para otimizar operações e reduzir custos com manutenção corretiva em prensas hidráulicas através de soluções tecnológicas e inteligência artificial.



