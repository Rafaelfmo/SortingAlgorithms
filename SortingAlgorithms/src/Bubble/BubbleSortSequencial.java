import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BubbleSortSequencial {

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

    public static int[] generateRandomArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = (int) (Math.random() * 1000);
        }
        return array;
    }

    public static void main(String[] args) throws Exception {

        ArrayList<String> results = new ArrayList<>();
        results.add("Tamanho,Tempo");

        int sizes[] = { 500, 2000, 5000, 7500, 10000 };

        for (int size : sizes) {
            int[] arraySerial = generateRandomArray(size);

            long startTime = System.nanoTime();
            indexedBubbleSort(arraySerial, 0, arraySerial.length);
            long endTime = System.nanoTime();
            long serialTime = endTime - startTime;

            results.add(size + "," + serialTime);

        }

        writeResultsToCSV(results);
    }

    public static void writeResultsToCSV(ArrayList<String> results) {
        try (FileWriter writer = new FileWriter("bubble_sort_serial.csv")) {
            for (String result : results) {
                writer.append(result);
                writer.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
