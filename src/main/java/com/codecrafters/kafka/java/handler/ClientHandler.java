package com.codecrafters.kafka.java.handler;

import com.codecrafters.kafka.java.util.RequestParser;

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
    private final RequestParser requestParser;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        requestParser = new RequestParser();
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

                requestParser.parse(bufferedInputStream, outputStream);

            } catch (BufferUnderflowException ex) {
                // do nothing
            } catch (IOException ex) {
                System.out.println("Exception occurred: " + ex.getMessage());

            }
        }
    }

}
