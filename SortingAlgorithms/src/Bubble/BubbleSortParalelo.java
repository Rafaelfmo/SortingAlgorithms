import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BubbleSortParalelo {

    public static void indexedBubbleSort(int[] array, int start, int end) {
        int temp;
        for (int i = start; i < end; i++) {
            for (int j = i + 1; j < end; j++) {
                if (array[i] > array[j]) {
                    temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
        }
    }

    public static void parallelBubbleSort(int[] array, int numThreads) {
        if (numThreads <= 0) {
            numThreads = Runtime.getRuntime().availableProcessors();
        }
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        int chunkSize = array.length / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize;
            int end = (i == numThreads - 1) ? array.length : (i + 1) * chunkSize;
            executor.submit(() -> indexedBubbleSort(array, start, end));
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.NANOSECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static int[] generateRandomArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = (int) (Math.random() * 1000);
        }
        return array;
    }

    public static void main(String[] args) throws Exception {

        ArrayList<String> results = new ArrayList<>();
        results.add("Tamanho,Threads,Tempo");
        int sizes[] = { 500, 2000, 5000, 7500, 10000 };

        for (int size : sizes) {
            int[] arraySerial = generateRandomArray(size);
            int[] arrayParallel = null;

            long startTime = System.nanoTime();

            for (int numThreads = 1; numThreads <= 8; numThreads++) {
                for (int i = 0; i < 8; i++) {
                    arrayParallel = Arrays.copyOf(arraySerial, arraySerial.length);
                    startTime = System.nanoTime();
                    parallelBubbleSort(arrayParallel, numThreads);
                    long endTime = System.nanoTime();
                    long parallelTime = endTime - startTime;

                    results.add(size + "," + numThreads + "," + parallelTime);
                }
            }
        }

        writeResultsToCSV(results);
    }

    public static void writeResultsToCSV(ArrayList<String> results) {
        try (FileWriter writer = new FileWriter("bubble_sort_paralelo.csv")) {
            for (String result : results) {
                writer.append(result);
                writer.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
