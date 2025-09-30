# Simulador de Rede de Filas Generalizado

## Visão Geral

Este projeto implementa um simulador de rede de filas generalizado que suporta múltiplas filas com roteamento probabilístico entre elas. O simulador foi desenvolvido como parte do curso de Métodos Analíticos da PUCRS.

## Principais Melhorias Implementadas

### 1. **ArrayList para Gerenciamento Dinâmico de Filas**
- Substituição do array fixo `Queue[]` por `List<Queue>` (ArrayList)
- Permite adicionar qualquer número de filas dinamicamente
- Mantém compatibilidade com código existente através de construtor sobrecarregado

### 2. **Generalização de Eventos**
- Adição de campos `sourceQueueId` e `destinationQueueId` na classe `Event`
- Uso do índice `-1` para representar o "exterior" (fila imaginária)
- Rastreamento completo do fluxo de clientes entre filas

### 3. **Roteamento Probabilístico**
- Implementação de roteamento baseado em probabilidades configuráveis
- Método `determineRoutingDestination()` que usa números aleatórios para decidir destino
- Validação automática de que probabilidades somam 1.0

### 4. **Configuração Flexível de Filas**
- Construtor expandido da classe `Queue` com parâmetros de chegada e serviço
- Método `setRouting()` para configurar roteamento probabilístico
- Suporte a filas que não recebem chegadas do exterior

## Estrutura das Classes

### Event.java
```java
public class Event {
    public double time;
    public EventType type; 
    public int queueId;
    public int sourceQueueId;      // -1 para exterior
    public int destinationQueueId; // -1 para exterior
}
```

### Queue.java
```java
public class Queue {
    // ... campos existentes ...
    
    // Parâmetros de chegada
    public double arrivalMinTime;
    public double arrivalMaxTime;
    
    // Parâmetros de serviço
    public double serviceMinTime;
    public double serviceMaxTime;
    
    // Roteamento probabilístico
    public int[] routingDestinations;
    public double[] routingProbabilities;
    
    public void setRouting(int[] destinations, double[] probabilities);
}
```

### Simulator.java
```java
public class Simulator {
    private List<Queue> queues;  // ArrayList em vez de array fixo
    
    private int determineRoutingDestination(Queue queue);
    // ... métodos generalizados para processar eventos
}
```

## Exemplo de Uso

### Rede de Filas com Roteamento Probabilístico

```java
// Criar filas
List<Queue> queues = new ArrayList<>();

// Fila 1: recebe chegadas do exterior
Queue fila1 = new Queue(0, 3, 1, 1.0, 2.0, 2.0, 3.0);

// Fila 2: não recebe chegadas do exterior
Queue fila2 = new Queue(1, 4, 2, 0.0, 0.0, 3.0, 5.0);

// Fila 3: não recebe chegadas do exterior
Queue fila3 = new Queue(2, 5, 3, 0.0, 0.0, 2.0, 4.0);

// Configurar roteamento probabilístico
// Fila 1: 20% → Fila 2, 30% → Fila 3, 50% → Exterior
fila1.setRouting(new int[]{1, 2, -1}, new double[]{0.2, 0.3, 0.5});

// Fila 2: 100% → Exterior
fila2.setRouting(new int[]{-1}, new double[]{1.0});

// Fila 3: 100% → Exterior
fila3.setRouting(new int[]{-1}, new double[]{1.0});

// Adicionar filas e executar simulação
queues.add(fila1);
queues.add(fila2);
queues.add(fila3);

Simulator simulator = new Simulator(queues, numberGenerator);
simulator.run();
```

## Algoritmo de Roteamento Probabilístico

O roteamento é implementado usando o método da distribuição cumulativa:

1. Gera um número aleatório entre 0 e 1
2. Acumula as probabilidades até encontrar o intervalo correspondente
3. Retorna o ID da fila de destino (ou -1 para exterior)

```java
private int determineRoutingDestination(Queue queue) {
    double randomValue = numberGenerator.nextRandom();
    double cumulativeProbability = 0.0;

    for (int i = 0; i < queue.routingProbabilities.length; i++) {
        cumulativeProbability += queue.routingProbabilities[i];
        if (randomValue <= cumulativeProbability) {
            return queue.routingDestinations[i];
        }
    }
    return queue.routingDestinations[queue.routingDestinations.length - 1];
}
```

## Compatibilidade

O simulador mantém compatibilidade total com código existente:
- Construtor original do `Simulator` ainda funciona
- Construtor original da classe `Queue` ainda funciona
- Todos os exemplos anteriores continuam funcionando

## Execução

### Exemplo Original (2 filas em tandem)
```bash
java -cp target/classes com.metodosanaliticos.app.App
```

### Exemplo Generalizado (rede de filas)
```bash
java -cp target/classes com.metodosanaliticos.app.NetworkExample
```

### Compilação
```bash
mvn compile
```

## Resultados da Simulação

O simulador gera relatórios detalhados incluindo:
- Tempo global de simulação
- Total de números aleatórios utilizados
- Para cada fila:
  - Total de perdas
  - Tempo acumulado em cada estado
  - Probabilidades de estado

## Benefícios da Generalização

1. **Flexibilidade**: Suporte a qualquer número de filas
2. **Roteamento Complexo**: Roteamento probabilístico entre filas
3. **Configuração Dinâmica**: Parâmetros configuráveis por fila
4. **Rastreabilidade**: Rastreamento completo do fluxo de clientes
5. **Escalabilidade**: Fácil adição de novas filas e configurações

## Considerações Técnicas

- Uso de `ArrayList` para gerenciamento dinâmico de filas
- Validação de probabilidades de roteamento
- Tratamento especial do "exterior" com índice -1
- Preservação da compatibilidade com código existente
- Otimização do algoritmo de roteamento probabilístico
