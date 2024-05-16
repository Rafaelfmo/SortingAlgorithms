import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class InsertionSortParalelo extends RecursiveAction {
    private int[] arr;
    private int start, end;
    private int threshold;

    public InsertionSortParalelo(int[] arr, int start, int end, int threshold) {
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
        InsertionSortParalelo left = new InsertionSortParalelo(arr, start, mid, threshold);
        InsertionSortParalelo right = new InsertionSortParalelo(arr, mid + 1, end, threshold);
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
        int n1 = mid - start + 1;
        int n2 = end - mid;

        int L[] = new int[n1];
        int R[] = new int[n2];

        for (int i = 0; i < n1; ++i)
            L[i] = arr[start + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[mid + 1 + j];

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

        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        final StringBuilder csvData = new StringBuilder();
        int[] sizes = { 500, 2000, 5000, 7500, 10000 };

        int maxThreads = 8;
        int maxExecutions = 8;

        csvData.append("Tamanho,Threads,Tempo\n");

        for (int size : sizes) {
            for (int i = 1; i <= maxThreads; i++) {
                for (int k = 0; k < maxExecutions; k++) {
                    long startTime = System.nanoTime();

                    int[] arr = new int[size];

                    for (int j = 0; j < size; j++) {
                        arr[j] = new Random().nextInt(1000);
                    }
                    int numberOfThreads = i;

                    if (numberOfThreads <= 0) {
                        numberOfThreads = Runtime.getRuntime().availableProcessors();
                    }

                    int threshold = arr.length / numberOfThreads;
                    ForkJoinPool pool = new ForkJoinPool(numberOfThreads);

                    try {
                        pool.invoke(new InsertionSortParalelo(arr, 0, arr.length - 1, threshold));

                        pool.shutdown();

                        pool.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.NANOSECONDS);
                    } finally {
                        pool.shutdownNow();

                    }

                    csvData.append(size).append(",").append(numberOfThreads).append(",")
                            .append(System.nanoTime() - startTime).append("\n");

                }
            }
        }

        FileWriter csvWriter = new FileWriter(
                "parallel_insertion_sort.csv");
        csvWriter.write(csvData.toString());
        csvWriter.close();
    }
}
