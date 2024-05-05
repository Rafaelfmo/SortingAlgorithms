import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class MergeSortSequencial {

  public void merge(int[] v, int left, int middle, int right) {
    int n1 = middle - left + 1;
    int n2 = right - middle;

    int[] L = new int[n1];
    int[] R = new int[n2];

    for (int i = 0; i < n1; i++) {
      L[i] = v[left + i];
    }
    for (int j = 0; j < n2; j++) {
      R[j] = v[middle + 1 + j];
    }

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

  public void mergeSort(int[] v, int left, int right) {
    if (left < right) {
      int middle = (left + right) / 2;

      mergeSort(v, left, middle);
      mergeSort(v, middle + 1, right);

      merge(v, left, middle, right);
    }
  }

  public static void main(String[] args) throws IOException {
    Random random = new Random();
    MergeSortSequencial mergeSort = new MergeSortSequencial();
    StringBuilder csvData = new StringBuilder();
    int numRuns = 8;

    csvData.append("Tamanho,Tempo\n");

    int[] sizes = {5, 10, 50, 100, 1000};

    for (int size : sizes) {
      long totalTime = 0;
      for (int i = 0; i < numRuns; i++) {
        int[] v = new int[size];
        for (int j = 0; j < size; j++) {
          v[j] = random.nextInt(100);
        }

        long startTime = System.nanoTime();
        mergeSort.mergeSort(v, 0, v.length - 1);
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
