package com.codecrafters.kafka.java.handler;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ClientHandler {

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
                bos.write(correlationId);
                if (apiVersion < 0 || apiVersion > 4) {
                    bos.write(new byte[]{0, 35});
                } else {

                    // error code 16 bit
                    // api_key => INT16
                    // min_version => INT16
                    // max_version => INT16
                    // throttle_time_ms => INT32
                    bos.write(new byte[]{0, 0});  // error code
                    bos.write(2);               // array size + 1
                    bos.write(new byte[]{0, 18}); // api_key
                    bos.write(new byte[]{0, 3});  // min version
                    bos.write(new byte[]{0, 4});  // max version
                    bos.write(0);               // tagged fields
                    bos.write(new byte[]{0, 0, 0, 0}); // throttle time
                    // All requests and responses will end with a tagged field buffer.  If
                    // there are no tagged fields, this will only be a single zero byte.
                    bos.write(0); // tagged fields
                }

                int size = bos.size();
                byte[] sizeBytes = ByteBuffer.allocate(4).putInt(size).array();
                var response = bos.toByteArray();
                System.out.println(Arrays.toString(sizeBytes));
                System.out.println(Arrays.toString(response));
                outputStream.write(sizeBytes);
                outputStream.write(response);
                outputStream.flush();

            } catch (IOException ex) {
                System.out.println("Exception occurred: " + ex.getMessage());

            }
        }
    }

}
