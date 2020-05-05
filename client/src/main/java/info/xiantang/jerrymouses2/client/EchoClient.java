package info.xiantang.jerrymouses2.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class EchoClient implements AutoCloseable {
    private final Socket client;
    private final DataOutputStream os;
    private final BufferedReader is;

    public EchoClient(String host, int port) throws IOException {
        client = new Socket(host, port);
        os = new DataOutputStream(client.getOutputStream());
        is = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }


    public void close() throws IOException {
        sendReceive("QUIT");
        is.close();
        os.close();
    }

    public String sendReceive(String message) throws IOException {
        send(message);
        return is.readLine();

    }

    public void send(String message) throws IOException {

        System.out.println(message);

        os.writeBytes(message + "\n");
        os.flush();
    }
}