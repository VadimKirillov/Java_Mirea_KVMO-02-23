package pr3.task2;

import io.reactivex.rxjava3.core.Observable;

public class Main3 {
    // Дан поток из случайного количества случайных чисел. Сформировать поток, содержащий только последнее число.
    public static void main(String[] args) {
        Observable.range(1, 10)
                .map(i -> getRandomNumber(0, 10))
                .takeLast(1)
                .subscribe(System.out::println);
    }

    private static int getRandomNumber(int min, int max) {
        return (int) Math.round(Math.random() * (max - min) + min);
    }
}