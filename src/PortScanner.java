import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PortScanner implements Scanner {

    private final String host;
    private final int[] ports;

    public PortScanner(String host, int[] ports) {
        this.host = host;
        this.ports = ports;
    }

    @Override
    public List<String> scan() {
        List<String> openPorts = new ArrayList<>();
        for (int port : ports) {
            try (Socket socket = new Socket(host, port)) {
                openPorts.add(String.valueOf(port));
                System.out.println("Port " + port + " is open on " + host);
            } catch (IOException e) {
                // Port is closed or unreachable
            }
        }
        return openPorts;
    }
}
