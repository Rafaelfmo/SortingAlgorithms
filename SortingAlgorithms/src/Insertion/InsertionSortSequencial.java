import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class InsertionSortSequencial {
    public void sort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            int key = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }

    static void printExecutionTimes(int[] sizes) {
        try {
            FileWriter writer = new FileWriter("serial_insertion_sort.csv");
            writer.append("Tamanho,Tempo\n");

            for (int size : sizes) {
                long totalTime = 0;

                int[] arr = new int[size];

                for (int j = 0; j < size; j++) {
                    arr[j] = new Random().nextInt(1000);
                }

                InsertionSortSequencial ob = new InsertionSortSequencial();

                long startTime = System.nanoTime();
                ob.sort(arr);
                long endTime = System.nanoTime();

                totalTime += (endTime - startTime);

                writer.append(size + "," + totalTime + "\n");
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        int[] sizes = { 100, 500, 1000, 3500, 5000 };

        printExecutionTimes(sizes);

    }
}
