import java.io.IOException;

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
        // pythonScript("serial");
        pythonScript("paralelo");
    }
}