import java.io.IOException;
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

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        executor.submit(() -> pythonScript("serial"));
        executor.submit(() -> pythonScript("paralelo"));

        executor.shutdown();
    }
}