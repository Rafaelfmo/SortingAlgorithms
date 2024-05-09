import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class MergeSortSequencial {

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
    
        mergeSort(leftArray, 0, leftArray.length - 1);
        mergeSort(rightArray, 0, rightArray.length - 1);
    
        merge(v, leftArray, rightArray, leftArray.length, rightArray.length);
    }
  }

  public static void main(String[] args) throws IOException {
    Random random = new Random();
    StringBuilder csvData = new StringBuilder();
    int numRuns = 8;

    csvData.append("Tamanho,Tempo\n");

    int[] sizes = {100,500,1000,3500,5000};

    for (int size : sizes) {
      long totalTime = 0;
      for (int i = 0; i < numRuns; i++) {
        int[] v = new int[size];
        for (int j = 0; j < size; j++) {
          v[j] = random.nextInt(100);
        }

        long startTime = System.nanoTime();
        mergeSort(v, 0, v.length - 1);
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        totalTime += duration;
      }

      double averageTime = (double) totalTime / numRuns;
      csvData.append(size + "," + averageTime + "\n");
    }

    FileWriter csvWriter = new FileWriter("merge_sort_serial.csv");
    csvWriter.write(csvData.toString());
    csvWriter.close();
  }
}
