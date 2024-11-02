package com.codecrafters.kafka.java.handler;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ClientHandler {

    private static final int UNSUPPORTED_VERSION_ERROR_CODE = 35;
    private static final int NO_ERROR_CODE = 0;
    private static final int API_VERSIONS_KEY = 18;
    private static final int SUPPORTED_API_VERSION_MIN = 0;
    private static final int SUPPORTED_API_VERSION_MAX = 4;
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void handleClients() {
        while (!clientSocket.isClosed()) {

            try {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(clientSocket.getInputStream());
                OutputStream outputStream = clientSocket.getOutputStream();

                // Request Header v2 => request_api_key request_api_version correlation_id client_id TAG_BUFFER
                //      request_api_key => INT16
                //      request_api_version => INT16
                //      correlation_id => INT32
                //      client_id => NULLABLE_STRING

                byte[] length = bufferedInputStream.readNBytes(4);  // request size 4 bytes
                byte[] apiKeyBytes = bufferedInputStream.readNBytes(2); // REQ header api key 16bit
               // int apiKey = ByteBuffer.wrap(apiKeyBytes).getInt();
                byte[] apiVersionBytes = bufferedInputStream.readNBytes(2); // api version 16bit
                short apiVersion = ByteBuffer.wrap(apiVersionBytes).getShort();
                System.out.println("api version: " + apiVersion);
                byte[] correlationId = bufferedInputStream.readNBytes(4); // correlation id 32bit
                byte[] clientIDBytes = bufferedInputStream.readNBytes(2); // correlation id 32bit
                short clientIdLength = ByteBuffer.wrap(clientIDBytes).getShort();
                byte[] clientId = bufferedInputStream.readNBytes(clientIdLength);
                System.out.println("Client ID: " + new String(clientId));
                byte[] tagBuffer = bufferedInputStream.readNBytes(1);
                System.out.println("here 1");
                byte[] arrayLengthBytes = bufferedInputStream.readNBytes(1);
                System.out.println("here 2 len: " + new String(arrayLengthBytes));
                int arrLen = ByteBuffer.wrap(arrayLengthBytes).getShort();
                System.out.println("here 3");
                System.out.println("Array Length: " + arrLen);
                System.out.println("here 4");
                for (int i = 0; i < arrLen - 1; i++) {
                    System.out.println("here 5");
                   byte[] topicNameLengthBytes = bufferedInputStream.readNBytes(4);
                    int topicNameLen = ByteBuffer.wrap(topicNameLengthBytes).getShort();
                   byte[] topicNameBytes = bufferedInputStream.readNBytes(topicNameLen);
                    System.out.println("Topic Name: " + new String(topicNameBytes));
                }
                bufferedInputStream.readNBytes(1);
                byte[] respPartitionLimBytes = bufferedInputStream.readNBytes(4);
                int respPartitionLim = ByteBuffer.wrap(respPartitionLimBytes).getShort();
                System.out.println("Response partition Limit: " + respPartitionLim);
                byte[] cursorBytes = bufferedInputStream.readNBytes(1);
                byte[] taggedBytes = bufferedInputStream.readNBytes(1);
                System.out.println("Completed request transformation...");
                // client_id nullable string
                // tagged fields nullable

                // response
                if (apiVersion < 0 || apiVersion > 4) {
                   sendErrorResponse(outputStream, correlationId);
                } else {
                    // error code 16 bit
                    // api_key => INT16
                    // min_version => INT16
                    // max_version => INT16
                    // throttle_time_ms => INT32
                    sendAPIVersionsResponse(outputStream, correlationId);
                }

            } catch (BufferUnderflowException ex) {
                // do nothing
            } catch (IOException ex) {
                System.out.println("Exception occurred: " + ex.getMessage());

            }
        }
    }

    private void sendErrorResponse(OutputStream out, byte[] correlationId) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bos.write(correlationId); // Correlation ID
        bos.write(new byte[] {0, (byte)UNSUPPORTED_VERSION_ERROR_CODE}); // Error code (35)
        int size = bos.size();
        out.write(ByteBuffer.allocate(4).putInt(size).array()); // Message size
        out.write(bos.toByteArray());                           // Payload
        out.flush();
    }

    private void sendAPIVersionsResponse(OutputStream out, byte[] correlationId) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bos.write(correlationId); // Correlation ID
        bos.write(new byte[] {0, (byte)NO_ERROR_CODE});            // No error
        bos.write(3); // Number of API keys
        bos.write(new byte[] {0, (byte)API_VERSIONS_KEY}); // API key (API_VERSIONS_KEY)
        bos.write(new byte[] {0, (byte)SUPPORTED_API_VERSION_MIN}); // Min version
        bos.write(new byte[] {0, (byte)SUPPORTED_API_VERSION_MAX}); // Max version
        bos.write(0);
        bos.write(new byte[] {0, 75}); // API key (API_VERSIONS_KEY)
        bos.write(new byte[] {0, 0}); // Min version
        bos.write(new byte[] {0, 0}); // Max version
        bos.write(0);
        bos.write(new byte[] {0, 0, 0, 0}); // Throttle time
        bos.write(0);                       // Tagged fields end byte
        int size = bos.size(); // tagged fields
        out.write(ByteBuffer.allocate(4).putInt(size).array()); // Message size
        out.write(bos.toByteArray());                           // Payload
        out.flush();
    }

}
