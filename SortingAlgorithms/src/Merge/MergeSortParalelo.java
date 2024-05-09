import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MergeSortParalelo {

    public static void merge(int[] a, int[] l, int[] r, int left, int right) {
        int i = 0, j = 0, k = 0;
        while (i < left && j < right) {
            if (l[i] <= r[j]) {
                a[k++] = l[i++];
            }
            else {
                a[k++] = r[j++];
            }
        }
        while (i < left) {
            a[k++] = l[i++];
        }
        while (j < right) {
            a[k++] = r[j++];
        }
    }

    public static void mergeSort(int[] v, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;
            int[] leftArray = new int[middle - left + 1];
            int[] rightArray = new int[right - middle];
    
            for (int i = 0; i < leftArray.length; i++) {
                leftArray[i] = v[left + i];
            }
    
            for (int i = 0; i < rightArray.length; i++) {
                rightArray[i] = v[middle + i + 1];
            }
    
            ExecutorService executor = Executors.newFixedThreadPool(2);
            executor.submit(() -> mergeSort(leftArray, 0, leftArray.length - 1));
            executor.submit(() -> mergeSort(rightArray, 0, rightArray.length - 1));
            executor.shutdown();
    
            merge(v, leftArray, rightArray, leftArray.length, rightArray.length);
        }
    }

    public static void main(String[] args) throws IOException {
        Random random = new Random();
        ExecutorService executor = Executors.newCachedThreadPool();
        final StringBuilder csvData = new StringBuilder();
        int numRuns = 8;
        int maxThreads = 8;
    
        csvData.append("Tamanho,Threads,Tempo\n");
    
        int[] sizes = {100,500,1000,3500,5000};
    
        for (int size : sizes) {
            for (int i = 0; i < numRuns; i++) {
                for (int k = 1; k <= maxThreads; k++){
                    int[] v = new int[size];
                    for (int j = 0; j < size; j++) {
                        v[j] = random.nextInt(1000);
                    }
                    long startTime = System.nanoTime();
                    executor.submit(() -> mergeSort(v, 0, v.length - 1));
                    long endTime = System.nanoTime();
        
                    long duration = (endTime - startTime);
                    csvData.append(size).append(",").append(k).append(",").append(duration).append("\n");
                }
            }
        }
    
        FileWriter csvWriter = new FileWriter("merge_sort_paralelo.csv");
        csvWriter.write(csvData.toString());
        csvWriter.close();
    }
}
