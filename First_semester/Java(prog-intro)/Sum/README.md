## Домашнее задание 2. Сумма чисел

Модификации
 * *Double* (32, 33)
    * Входные данные являются 64-битными числами с формате с плавающей точкой
    * Класс должен иметь имя `SumDouble`
 * *DoubleSpace* (34, 35)
    * Входные данные являются 64-битными числами с формате с плавающей точкой
    * Класс должен иметь имя `SumDoubleSpace`
    * Числа разделяются [пробелами-разделителями](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Character.html#SPACE_SEPARATOR)
 * *LongSpace* (36, 37)
    * Входные данные являются 64-битными целыми числами
    * Класс должен иметь имя `SumLongSpace`
    * Числа разделяются [пробелами-разделителями](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Character.html#SPACE_SEPARATOR)
 * *BigIntegerSpace* (38, 39)
    * Входные данные помещаются в тип [BigInteger](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/math/BigInteger.html)
    * Класс должен иметь имя `SumBigIntegerSpace`
    * Числа разделяются [пробелами-разделителями](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Character.html#SPACE_SEPARATOR)


Для того, чтобы протестировать программу:

 1. Скачайте откомпилированные тесты ([SumTest.jar](First_semester/Java(prog-intro)/Sum/SumTest.jar))
 1. Откомпилируйте `Sum.java`
 1. Проверьте, что создался `Sum.class`
 1. В каталоге, в котором находится `Sum.class`, выполните команду
    ```
       java -ea -jar <путь к SumTest.jar> Base
    ```
    * Например, если `SumTest.jar` находится в текущем каталоге, выполните команду
    ```
        java -ea -jar SumTest.jar Base
    ```
 1. Для ускорени отладки рекомендуется сделать скрипт, выполняющий шаги 2−4.

Исходный код тестов:

* [SumTest.java](java/sum/SumTest.java)
* [SumTester.java](java/sum/SumTester.java)
* [Базовые классы](java/base/)
