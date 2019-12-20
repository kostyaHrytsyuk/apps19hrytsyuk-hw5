package ua.edu.ucu;

import ua.edu.ucu.stream.*;

public class StreamApp {

    // 1. Що мається на увазі під термінальним методом?
    // 2. Чи можливо створити метод який би приймав Action і обходив масив відповідно
    // 3. Що краще використати як внутрішню колекцію? ArrayList, Queue?
    // 4. Як уникнути дублювання коду? Приклад з першого завдання.
    // 5. Я хочу використати разом з Ітератором ще й Декоратор, щоб реалізувати "ліниві обчислення". Чи це не занадто багато патернів?
    // 6. Чи Of також робити "лінивим"?
    // 7. Що поверне filter якщо вся колекція не відповідає предикату?

    public static int streamOperations(IntStream intStream) {
        IntStream fltr = intStream.filter(x -> x > 0); // 1, 2, 3
        IntStream mppd = fltr.map(x -> x * x); // 1, 4, 9
        IntStream fltmppd = mppd.flatMap(x -> AsIntStream.of(x - 1, x, x + 1)); // 0, 1, 2, 3, 4, 5, 8, 9, 10
        int res = fltmppd.sum();

                //.reduce(0, (sum, x) -> sum += x); // 42
        return 42;
    }

    public static int[] streamToArray(IntStream intStream) {        
        int[] intArr = intStream.toArray();
        return intArr;
    }

    public static String streamForEach(IntStream intStream) {        
        StringBuilder str = new StringBuilder();
        intStream.forEach(x -> str.append(x));
        return str.toString();
    }

    public static void main(String[] args) {
        IntStream intStream = AsIntStream.of(-1, 0, 1, 2, 3); // input values
        streamOperations(intStream);
    }
}
