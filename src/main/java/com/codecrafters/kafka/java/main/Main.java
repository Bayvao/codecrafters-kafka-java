package com.codecrafters.kafka.java.main;

import com.sun.net.httpserver.Request;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Main {
    public static void main(String[] args){


        int port = 9092;
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(true);
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {


                    InputStream inputStream = clientSocket.getInputStream();
                    OutputStream outputStream = clientSocket.getOutputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();

                    // Request Header v2 => request_api_key request_api_version correlation_id client_id TAG_BUFFER
                    //      request_api_key => INT16
                    //      request_api_version => INT16
                    //      correlation_id => INT32
                    //      client_id => NULLABLE_STRING

                    byte[] length = inputStream.readNBytes(4);  // request size 4 bytes
                    byte[] apiKey = inputStream.readNBytes(2); // REQ header api key 16bit
                    byte[] apiVersionBytes = inputStream.readNBytes(2); // api version 16bit
                    short apiVersion = ByteBuffer.wrap(apiVersionBytes).getShort();
                    byte[] correlationId = inputStream.readNBytes(4); // correlation id 32bit
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
                }
            }

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
