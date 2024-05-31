import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class ServiceScanner implements Scanner {

    private final String host;
    private final int port;

    public ServiceScanner(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public List<String> scan() {
        List<String> banners = new ArrayList<>();
        int retryAttempts = 3; // Number of retry attempts

        for (int attempt = 1; attempt <= retryAttempts; attempt++) {
            try (Socket socket = new Socket(host, port);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                socket.setSoTimeout(2000); // Set timeout for reading
                String banner = reader.readLine();
                if (banner != null) {
                    banners.add(banner);
                    System.out.println("Service on port " + host + ":" + port + ": " + banner);
                    break; // Exit the loop if successful
                }
            } catch (SocketTimeoutException e) {
                System.err.println("Timeout occurred while detecting service on port " + port + " of host " + host + " (Attempt " + attempt + ")");
            } catch (IOException e) {
                System.err.println("Error detecting service on port " + port + " of host " + host + " (Attempt " + attempt + "): " + e.getMessage());
            }
        }
        return banners;
    }
}
