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
            byte[] apiVersionBytes = inputStream.readNBytes(2);
            short apiVersion = ByteBuffer.wrap(apiVersionBytes).getShort();
            byte[] correlationId = inputStream.readNBytes(4);

            outputStream.write(correlationId);

            if (apiVersion < 0 || apiVersion > 4) {
                outputStream.write(new byte[] {0, 35});
            } else {
                outputStream.write(new byte[] {0, 0});  // error code
                outputStream.write(2);               // array size + 1
                outputStream.write(new byte[] {0, 18}); // api_key
                outputStream.write(new byte[] {0, 3});  // min version
                outputStream.write(new byte[] {0, 4});  // max version
                outputStream.write(0);               // tagged fields
                outputStream.write(new byte[] {0, 0, 0, 0}); // throttle time
                // All requests and responses will end with a tagged field buffer.  If
                // there are no tagged fields, this will only be a single zero byte.
                outputStream.write(0); // tagged fields
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
