import java.io.IOException;
import java.io.Serial;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunPythonScript {

    public static void pythonScript(String type) {
        try {
            String pythonScriptPath = "SortingAlgorithms/src/" + type + ".py";

            ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath);

            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            int exitCode = process.waitFor();

            System.out.println("\nExited with error code : " + exitCode);
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Starting Bubble");
        BubbleSortParalelo.main(args);
        BubbleSortSequencial.main(args);
        System.out.println("Starting Insertion");
        ParallelInsertionSort.main(args);
        InsertionSort.main(args);
        System.out.println("Starting Merge");
        MergeSortParalelo.main(args);
        MergeSortSequencial.main(args);
        System.out.println("Starting Quick");
        QuickSortParalelo.main(args);
        QuickSortSequencial.main(args);
        System.out.println("All algorithms done! Starting python scripts...");
        ExecutorService executor = Executors.newFixedThreadPool(8);
        executor.submit(() -> pythonScript("serial"));
        executor.submit(() -> pythonScript("paralelo"));

        executor.shutdown();
    }
}