import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MergeSortParalelo {

    public static void merge(int[] v, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; i++)
            L[i] = v[left + i];
        for (int j = 0; j < n2; j++)
            R[j] = v[middle + 1 + j];

        int i = 0, j = 0;
        int k = left;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                v[k] = L[i];
                i++;
            } else {
                v[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            v[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            v[k] = R[j];
            j++;
            k++;
        }
    }

    public static void mergeSort(int[] v, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;

            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    mergeSort(v, left, middle);
                }
            });

            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    mergeSort(v, middle + 1, right);
                }
            });

            t1.start();
            t2.start();

            merge(v, left, middle, right);
        }
    }

    public static void main(String[] args) throws IOException {
        Random random = new Random();
        ExecutorService executor = Executors.newCachedThreadPool();
        final StringBuilder csvData = new StringBuilder();
        int numRuns = 8;
    
        csvData.append("Tamanho,Threads,Tempo\n");
    
        int[] sizes = {5, 10, 50, 100, 1000};
    
        for (int size : sizes) {
            int numThreads = 1;
            for (int k = 0; k < 8; k++) {
                for (int i = 0; i < numRuns; i++) {
                    int[] vetor = new int[size];
                    for (int j = 0; j < size; j++) {
                        vetor[j] = random.nextInt();
                    }
    
                    executor = Executors.newFixedThreadPool(numThreads);
                    long startTime = System.nanoTime();
                    executor.submit(() -> mergeSort(vetor, 0, vetor.length - 1));
                    executor.shutdown();
    
                    long endTime = System.nanoTime();
                    csvData.append(size + "," + numThreads + "," + (endTime - startTime) + "\n");
        
                    executor = Executors.newCachedThreadPool();
                }
                numThreads++;
            }
        }
    
        FileWriter csvWriter = new FileWriter("merge_sort_paralelo.csv");
        csvWriter.write(csvData.toString());
        csvWriter.close();
    }
}
