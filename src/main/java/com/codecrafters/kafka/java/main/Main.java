package com.codecrafters.kafka.java.main;

import com.codecrafters.kafka.java.handler.ClientHandler;
import com.sun.net.httpserver.Request;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args){

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        int port = 9092;
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(true);
            while (true) { // Accept client connections in a loop

                try {
                    Socket clientSocket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(clientSocket);

                    executor.execute(clientHandler::handleClients);

                } catch (IOException e) {
                    System.out.println("IOException: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

}
