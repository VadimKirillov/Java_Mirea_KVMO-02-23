// Вариант 2 - поиск минимума

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;


public class Task1 {
    private static final int ARRAY_SIZE = 10000;
    private static final int MAX_ELEMENT_VALUE = 1000;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int[] array = generateArray();

        // Последовательный поиск
        long startTime = System.currentTimeMillis();
        int minSequential = findMinimumSequential(array);
        long endTime = System.currentTimeMillis();
        long sequentialTime = endTime - startTime;
        System.out.println("Минимальный элемент (последовательный): " + minSequential);
        System.out.println("Время выполнения (последовательный): " + sequentialTime + " мс");
        System.out.println("Использование памяти (последовательный): " + getMemoryUsage() + " байт");
        System.out.println();

        // Поиск с использованием многопоточности (с классом Future)
        startTime = System.currentTimeMillis();
        int minMultiThreaded = findMinimumMultiThreaded(array);
        endTime = System.currentTimeMillis();
        long multiThreadedTime = endTime - startTime;
        System.out.println("Минимальный элемент (многопоточность - Future): " + minMultiThreaded);
        System.out.println("Время выполнения (многопоточность - Future): " + multiThreadedTime + " мс");
        System.out.println("Использование памяти (многопоточность - Future): " + getMemoryUsage() + " байт");
        System.out.println();

        // Поиск с использованием ForkJoin
        startTime = System.currentTimeMillis();
        int minForkJoin = findMinimumForkJoin(array);
        endTime = System.currentTimeMillis();
        long forkJoinTime = endTime - startTime;
        System.out.println("Минимальный элемент (ForkJoin): " + minForkJoin);
        System.out.println("Время выполнения (ForkJoin): " + forkJoinTime + " мс");
        System.out.println("Использование памяти (ForkJoin): " + getMemoryUsage() + " байт");
    }

    // Создание массива
    private static int[] generateArray() {
        Random random = new Random();
        int[] array = new int[ARRAY_SIZE];

        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = random.nextInt(MAX_ELEMENT_VALUE);
        }

        return array;
    }

    // Последовательный поиск
    private static int findMinimumSequential(int[] array) {
        int min = array[0];

        for (int i = 1; i < ARRAY_SIZE; i++) {
            sleepOneMillisecond();
            if (array[i] < min) {
                min = array[i];
            }
        }

        return min;
    }

    // Поиск с использованием многопоточности (с классом Future)
    private static int findMinimumMultiThreaded(int[] array) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<Integer>> futures = new ArrayList<>();

        for (int i = 0; i < ARRAY_SIZE; i++) {
            final int index = i;
            futures.add(executorService.submit(() -> {
                sleepOneMillisecond();
                return array[index];
            }));
        }

        int min = Integer.MAX_VALUE;
        for (Future<Integer> future : futures) {
            int value = future.get();
            if (value < min) {
                min = value;
            }
        }

        executorService.shutdown();

        return min;
    }


    // Поиск с использованием ForkJoin
    private static int findMinimumForkJoin(int[] array) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        MinimumFinder minimumFinder = new MinimumFinder(array, 0, ARRAY_SIZE - 1);
        return forkJoinPool.invoke(minimumFinder);
    }

    // Функция для задержки
    private static void sleepOneMillisecond() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static long getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    private static class MinimumFinder extends RecursiveTask<Integer> {
        private final int[] array;
        private final int start;
        private final int end;

        public MinimumFinder(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - start <= 1) {
                return Math.min(array[start], array[end]);
            }

            int mid = (start + end) / 2;

            MinimumFinder leftFinder = new MinimumFinder(array, start, mid);
            MinimumFinder rightFinder = new MinimumFinder(array, mid + 1, end);

            leftFinder.fork();
            int rightMin = rightFinder.compute();
            int leftMin = leftFinder.join();

            return Math.min(leftMin, rightMin);
        }
    }
}
