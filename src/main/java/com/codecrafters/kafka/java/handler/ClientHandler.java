package com.codecrafters.kafka.java.handler;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
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
        BufferedInputStream bufferedInputStream = null;
        OutputStream outputStream = null;
        ByteArrayOutputStream bos = null;
        while (!clientSocket.isClosed()) {

            try {
                bufferedInputStream = new BufferedInputStream(clientSocket.getInputStream());
                outputStream = clientSocket.getOutputStream();
                bos = new ByteArrayOutputStream();

                // Request Header v2 => request_api_key request_api_version correlation_id client_id TAG_BUFFER
                //      request_api_key => INT16
                //      request_api_version => INT16
                //      correlation_id => INT32
                //      client_id => NULLABLE_STRING

                byte[] length = bufferedInputStream.readNBytes(4);  // request size 4 bytes
                byte[] apiKey = bufferedInputStream.readNBytes(2); // REQ header api key 16bit
                byte[] apiVersionBytes = bufferedInputStream.readNBytes(2); // api version 16bit
                short apiVersion = ByteBuffer.wrap(apiVersionBytes).getShort();
                byte[] correlationId = bufferedInputStream.readNBytes(4); // correlation id 32bit
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
            } catch (IOException ex) {
                System.out.println("Exception occurred: " + ex.getMessage());

            } finally {
                if (bufferedInputStream != null) {
                    try {
                        bufferedInputStream.close();
                    } catch (IOException ex) {
                        System.out.println("Exception occurred: " + ex.getMessage());
                    }
                }

                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException ex) {
                        System.out.println("Exception occurred: " + ex.getMessage());
                    }
                }

                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException ex) {
                        System.out.println("Exception occurred: " + ex.getMessage());
                    }
                }

                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    System.out.println("Exception occurred: " + ex.getMessage());
                }
            }
        }
    }

    private static void sendErrorResponse(OutputStream out, byte[] correlationId)
            throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(correlationId); // Correlation ID
        bos.write(new byte[] {0, UNSUPPORTED_VERSION_ERROR_CODE}); // Error code (35)

        int size = bos.size();
        out.write(ByteBuffer.allocate(4).putInt(size).array()); // Message size
        out.write(bos.toByteArray());                           // Payload
        out.flush();
    }

    private static void sendAPIVersionsResponse(OutputStream out, byte[] correlationId) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bos.write(correlationId);                               // Correlation ID
        bos.write(new byte[] {0, (byte)NO_ERROR_CODE});               // No error
        bos.write(2);                                        // Number of API keys
        bos.write(new byte[] {0, (byte)API_VERSIONS_KEY});            // API key (API_VERSIONS_KEY)
        bos.write(new byte[] {0, (byte)SUPPORTED_API_VERSION_MIN});   // Min version
        bos.write(new byte[] {0, (byte)SUPPORTED_API_VERSION_MAX});   // Max version
        bos.write(0);                                        // tagged fields
        bos.write(new byte[] {0, 0, 0, 0});                     // Throttle time

        // All requests and responses will end with a tagged field buffer.  If
        // there are no tagged fields, this will only be a single zero byte.
        bos.write(0);                       // Tagged fields end byte

        int size = bos.size();
        out.write(ByteBuffer.allocate(4).putInt(size).array()); // Message size
        out.write(bos.toByteArray());                           // Payload
        out.flush();
        System.err.printf(
                "Correlation ID: %d - Sent APIVersions response with no error.%n",
                correlationId);
    }

}
