## Особености
- Легковесная реализация реактивных потоков
- Поддержка операторов преобразования данных
- Гибкая система планировщиков (schedulers)
- Чистый Java без внешних зависимостей

## Структура проекта

```
└── src
    ├── main
    │   └── java
    │       └── rx
    │           ├── DataSubscriber.java # Интерфейс подписчика
    │           ├── ReactiveStream.java # Реактивный поток
    │           ├── Scheduler.java # Базовый планировщик
    │           ├── Subscription.java # Подписка на поток
    │           ├── operators
    │           │   ├── ElementTransformer.java # Преобразование элементов
    │           │   ├── SelectionFilter.java # Фильтрация элементов
    │           │   └── StreamFlattener.java # "Уплощение" потока
    │           └── schedulers
    │               ├── Computation.java # Планировщик для вычислений
    │               ├── IOTaskExecutor.java # Планировщик для IO операций
    │               └── Single.java # Однопоточный планировщик
    ├── pom.xml
    └── test
        └── java
            └── rx
                ├── ReactiveStreamTest.java # Тесты реактивных потоков
                ├── SchedulerTest.java # Тесты планировщиков
                └── StreamOperatorsTest.java # Тесты операторов

```
## Сборка проекта
``` 
mvn clean install
```
Пример использования
```
ReactiveStream.of(1, 2, 3, 4, 5)
    .filter(x -> x % 2 == 0)
    .map(x -> x * 10)
    .subscribeOn(Schedulers.computation())
    .subscribe(
        data -> System.out.println("Received: " + data),
        error -> System.err.println("Error: " + error),
        () -> System.out.println("Completed!")
    );
```






