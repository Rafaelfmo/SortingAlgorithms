import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MergeSortParalelo {

    private static void merge(int[] array, int[] temp, int left, int mid, int right) {
        System.arraycopy(array, left, temp, left, right - left);

        int i = left, j = mid, k = left;
        while (i < mid && j < right) {
            if (temp[i] <= temp[j]) {
                array[k++] = temp[i++];
            } else {
                array[k++] = temp[j++];
            }
        }

        System.arraycopy(temp, i, array, k, mid - i);
    }

    public static void main(String[] args) throws IOException {
        Random random = new Random();
        final StringBuilder csvData = new StringBuilder();
        int numRuns = 8;
        int maxThreads = 8;

        csvData.append("Tamanho,Threads,Tempo\n");

        int[] sizes = { 100, 500, 1000, 3500, 5000 };

        for (int size : sizes) {
            for (int i = 0; i < numRuns; i++) {
                for (int k = 1; k <= maxThreads; k++) {
                    int[] v = new int[size];

                    for (int j = 0; j < size; j++) {
                        v[j] = random.nextInt(1000);
                    }
                    long startTime = System.nanoTime();

                    ForkJoinPool pool = new ForkJoinPool(k);
                    pool.invoke(new MergeSortTask(v, new int[v.length], 0, v.length));
                    pool.shutdown();
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

    private static class MergeSortTask extends RecursiveAction {
        private int[] array;
        private int[] temp;
        private int left;
        private int right;

        MergeSortTask(int[] array, int[] temp, int left, int right) {
            this.array = array;
            this.temp = temp;
            this.left = left;
            this.right = right;
        }

        @Override
        protected void compute() {
            if (right - left < 2) {
                return;
            }

            int mid = (left + right) / 2;
            invokeAll(new MergeSortTask(array, temp, left, mid),
                    new MergeSortTask(array, temp, mid, right));
            merge(array, temp, left, mid, right);
        }
    }
}
