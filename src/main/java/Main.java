import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static void main(String[] args){
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.err.println("Logs from your program will appear here!");

     ServerSocket serverSocket = null;
     Socket clientSocket = null;
     int port = 9092;
    BufferedReader bufferedReader;
    OutputStream outputStream;

     try {
       serverSocket = new ServerSocket(port);
       serverSocket.setReuseAddress(true);
       clientSocket = serverSocket.accept();

       bufferedReader = new BufferedReader(
               new InputStreamReader(clientSocket.getInputStream()));

       outputStream = clientSocket.getOutputStream();
       outputStream.write(new byte[] {0, 1, 2, 3});
       outputStream.write(new byte[] {0, 0, 0, 7});

       System.out.println("Got from cli: " + bufferedReader.readLine());
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
