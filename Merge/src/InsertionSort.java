import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class InsertionSort {
    // Método para ordenar um array usando insertion sort
    public void sort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            int key = arr[i];
            int j = i - 1;

            // Mova os elementos de arr[0..i-1], que são maiores que key, para uma posição à frente
            // de sua posição atual
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }

    // Método utilitário para imprimir elementos de um array
    static void printArray(int arr[]) {
        int n = arr.length;
        for (int i = 0; i < n; ++i)
            System.out.print(arr[i] + " ");

        System.out.println();
    }

    // Método para imprimir os tempos de execução em um arquivo CSV
    static void printExecutionTimes(int[] sizes, int numIterations) {
        try {
            FileWriter writer = new FileWriter("serial_insertion_sort.csv");
            writer.append("Size,Time (ns)\n");

            for (int size : sizes) {
                long totalTime = 0;

                for (int i = 0; i < numIterations; i++) {
                    int[] arr = new int[size];

                    for (int j = 0; j < size; j++) {
                        arr[j] = new Random().nextInt(1000);
                    }

                    InsertionSort ob = new InsertionSort();

                    long startTime = System.nanoTime();
                    ob.sort(arr);
                    long endTime = System.nanoTime();

                    totalTime += (endTime - startTime);

                    long averageTime = totalTime / numIterations;
                    writer.append(size + "," + averageTime + "\n");
                }

            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        int[] sizes = {1000, 10000, 100000};
        int maxExecutions = 5;

        printExecutionTimes(sizes, maxExecutions);

    }
}
