import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Main {
    public static void main(String[] args){
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        int port = 9092;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            clientSocket = serverSocket.accept();

            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();

            byte[] length = inputStream.readNBytes(4);
            byte[] apiKey = inputStream.readNBytes(2);
            byte[] apiVersion = inputStream.readNBytes(2);
            short shortApiVersion = ByteBuffer.wrap(apiVersion).getShort();
            byte[] correlationId = inputStream.readNBytes(4);

            outputStream.write(length);
            outputStream.write(correlationId);

            if (shortApiVersion < 0 || shortApiVersion > 4) {
                outputStream.write(new byte[] {0, 35});
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }
}
