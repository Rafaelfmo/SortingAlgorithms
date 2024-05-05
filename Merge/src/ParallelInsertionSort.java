import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelInsertionSort extends RecursiveAction {
    private int[] arr;
    private int start, end;
    private int threshold; // Limite para a quantidade de dados por thread

    public ParallelInsertionSort(int[] arr, int start, int end, int threshold) {
        this.arr = arr;
        this.start = start;
        this.end = end;
        this.threshold = threshold;
    }

    @Override
    protected void compute() {
        if (end - start <= threshold) {
            insertionSort(arr, start, end);
            return;
        }

        int mid = (start + end) / 2;
        ParallelInsertionSort left = new ParallelInsertionSort(arr, start, mid, threshold);
        ParallelInsertionSort right = new ParallelInsertionSort(arr, mid + 1, end, threshold);
        invokeAll(left, right);
        merge(arr, start, mid, end);
    }

    private void insertionSort(int[] arr, int start, int end) {
        for (int i = start + 1; i <= end; ++i) {
            int key = arr[i];
            int j = i - 1;
            while (j >= start && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }

    private void merge(int[] arr, int start, int mid, int end) {
        // Calculate sizes of two subarrays to be merged
        int n1 = mid - start + 1;
        int n2 = end - mid;
    
        // Create temp arrays
        int L[] = new int[n1];
        int R[] = new int[n2];
    
        //Copy data to temp arrays
        for (int i = 0; i < n1; ++i)
            L[i] = arr[start + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[mid + 1 + j];
    
        // Merge the temp arrays back into arr[start..end]
        int i = 0, j = 0;
        int k = start;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }
    
        // Copy remaining elements of L[] if any
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }
    
        // Copy remaining elements of R[] if any
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    private static void printExecutionTimes(int size, int numThreads, long time) {
        try {
            FileWriter writer = new FileWriter("parallel_insertion_sort.csv", true);
            PrintWriter printWriter = new PrintWriter(writer);

            printWriter.println(size + "," + numThreads + "," + time);

            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) throws InterruptedException {
        
        int[] sizes = {1000, 10000, 100000, 1000000};
        int maxThreads = 8;
        int maxExecutions = 5;
        
        for (int size : sizes) {
            for (int i = 1; i <= maxThreads; i++) {
                for (int k = 0; k < maxExecutions; k++) {
                long startTime = System.nanoTime();
    
                int[] arr = new int[size];
    
                for (int j = 0; j < size; j++) {
                    arr[j] = new Random().nextInt(1000);
                }
                
                System.out.println("\nArray size: " + size);
    
                int numberOfThreads = i; // Número de threads a ser usado
        
                if (numberOfThreads <= 0) {
                    numberOfThreads = Runtime.getRuntime().availableProcessors();
                }
        
                int threshold = arr.length / numberOfThreads; // Limite para divisão do trabalho por thread
                ForkJoinPool pool = new ForkJoinPool(numberOfThreads);
        
                try {
                    pool.invoke(new ParallelInsertionSort(arr, 0, arr.length - 1, threshold));
        
                    // Wait for the pool to complete
                    pool.shutdown();
        
                    pool.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.NANOSECONDS);
                } finally {
                    pool.shutdownNow();

                }
                
                System.out.println("Tempo: " + (System.nanoTime() - startTime) + "ns");
    
    
                printExecutionTimes(size, numberOfThreads, System.nanoTime() - startTime);

                }
            }
        }
        
    }
}
