package info.xiantang.jerrymouses2.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.stream.Stream;

public class EchoClient implements AutoCloseable {
    private final Socket client;
    private final DataOutputStream os;
    private final BufferedReader is;

    public EchoClient(String host, int port) throws IOException {
        client = new Socket(host, port);
        os = new DataOutputStream(client.getOutputStream());
        is = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }

    public String sendReceive(String message) throws IOException {

        os.writeBytes(message + "\n");
        os.flush();
        // keep on reading from/to the socket till we receive the "Ok" from Server,
        // once we received that we break.
        String responseLine = is.readLine();
        if (responseLine != null) {
            System.out.println("Server Sent: " + responseLine);
        } else {
            System.out.println("Server Sent: No Response");
        }
        return responseLine;

    }

    public void close() throws IOException {
        sendReceive("QUIT");
        is.close();
        os.close();
    }

    public static void main(String[] args) {
        int port = 10393;
        String host = "localhost";
        int totalClients = 4;
        Stream.iterate(1, x -> x + 1).limit(totalClients).forEach(id -> new Thread(() -> {
            try (EchoClient client = new EchoClient(host, port)) {
                client.sendReceive("HELO" + id);
                Thread.sleep(2000);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }).start());
    }
}