import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class QuickSortParalelo {
    public static void quickSortParallel(int[] arr, int numThreads) {
        if (numThreads <= 0) {
            numThreads = Runtime.getRuntime().availableProcessors();
        }

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        quickSort(arr, 0, arr.length - 1, executor);
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pivot = partition(arr, low, high);
            quickSort(arr, low, pivot - 1);
            quickSort(arr, pivot + 1, high);
        }
    }


    private static void quickSort(int[] arr, int low, int high, ExecutorService executor) {
        if (low < high) {
            int pivot = partition(arr, low, high);
            executor.submit(() -> quickSort(arr, low, pivot - 1));
            executor.submit(() -> quickSort(arr, pivot + 1, high));
        }
    }

   
    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static int[] generateRandomArray(int size){
        int[] array = new int[size];
        for (int i = 0; i < size; i++){
            array[i] = (int) (Math.random() * 1000);
        }
        return array;
    }

    public static void main(String[] args) {
        ArrayList<String> results = new ArrayList<>();
        results.add("Tamanho,Threads,Tempo");  
        int[] sizes = {100,500,1000,3500,5000};

        for (int size : sizes) {
            int[] arraySerial = generateRandomArray(size);
            int[] arrayParallel = null;

            long startTime = System.nanoTime();
            
            for (int numThreads = 1; numThreads <= 8; numThreads++) {
                for (int i = 0; i < 8; i++) {
                    arrayParallel = Arrays.copyOf(arraySerial, arraySerial.length);
                    startTime = System.nanoTime();
                    quickSortParallel(arrayParallel, numThreads);
                    long endTime = System.nanoTime();
                    long parallelTime = endTime - startTime;

                    results.add(size + "," + numThreads + "," + parallelTime);
                }
            }
        }

        writeResultsToCSV(results);
    }

    public static void writeResultsToCSV(ArrayList<String> results) {
        try (FileWriter writer = new FileWriter("quick_sort_paralelo.csv")) {
            for (String result : results) {
                writer.append(result);
                writer.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
