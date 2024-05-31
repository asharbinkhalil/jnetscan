import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NetworkScanner implements Scanner {

    private final String subnet;
    private final int timeout; // Timeout in milliseconds

    public NetworkScanner(String subnet, int timeout) {
        this.subnet = subnet;
        this.timeout = timeout;
    }

    @Override
    public List<String> scan() {
        List<String> activeHosts = new ArrayList<>();
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(50); // Adjust thread pool size as needed

        for (int i = 1; i < 255; i++) {
            String host = subnet + i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    InetAddress inetAddress = InetAddress.getByName(host);
                    if (inetAddress.isReachable(timeout)) { // Set individual host timeout
                        synchronized (activeHosts) {
                            activeHosts.add(host);
                        }
                        System.out.println(host + " is reachable");
                    }
                } catch (IOException e) {
                    System.err.println("Error reaching host: " + host);
                }
            }, executor);
            futures.add(future);
        }

        // Wait for all futures to complete
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        try {
            allOf.get(10, TimeUnit.SECONDS); // Set total scanning timeout
        } catch (Exception e) {
            System.err.println("Timeout occurred while scanning the network.");
        }

        executor.shutdown();
        return activeHosts;
    }
}
