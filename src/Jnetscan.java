import java.util.List;
import java.util.Scanner;


public class Jnetscan {

    public static void main(String[] args) {
        // Print Logo
        System.out.println( " ┬┌─┐┬  ┬┌─┐  ┌┐┌┌─┐┌┬┐┬ ┬┌─┐┬─┐┬┌─  ┌─┐┌─┐┌─┐┌┐┌┌┐┌┌─┐┬─┐\n" +
                            " │├─┤└┐┌┘├─┤  │││├┤  │ ││││ │├┬┘├┴┐  └─┐│  ├─┤││││││├┤ ├┬┘\n" +
                            "└┘┴ ┴ └┘ ┴ ┴  ┘└┘└─┘ ┴ └┴┘└─┘┴└─┴ ┴  └─┘└─┘┴ ┴┘└┘┘└┘└─┘┴└─");

        // Sample subnet and ports
        System.out.println("Enter The Subnet: E.g 192.168.1.");

        Scanner scanner = new Scanner(System.in);
        String subnet = scanner.nextLine();  // Read user input
        int timeout = 1000;
        int[] ports = { 21, 22, 23, 80, 443 };

        // Network Scanning
        System.out.println("Started the Network Scanner");
        NetworkScanner networkScanner = new NetworkScanner(subnet, timeout);
        List<String> activeHosts = networkScanner.scan();

        // Port Scanning and Service Detection
        for (String host : activeHosts) {

            PortScanner portScanner = new PortScanner(host, ports);
            List<String> openPorts = portScanner.scan();

            for (String port : openPorts) {
                System.out.println("Started the Port Service Scanner");
                ServiceScanner serviceDetector = new ServiceScanner(host,Integer.valueOf(port));
                serviceDetector.scan();
            }
        }

        scanner.close();
    }
}
