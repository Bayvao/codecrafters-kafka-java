package com.codecrafters.kafka.java.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class RequestParser {

    private static final int UNSUPPORTED_VERSION_ERROR_CODE = 35;
    private static final int NO_ERROR_CODE = 0;
    private static final int API_VERSIONS_KEY = 18;
    private static final int SUPPORTED_API_VERSION_MIN = 0;
    private static final int SUPPORTED_API_VERSION_MAX = 4;

    public void parse(BufferedInputStream bufferedInputStream, OutputStream outputStream) {
        try {
            byte[] requestLengthBytes = bufferedInputStream.readNBytes(4);  // request size 4 bytes
            int requestLength = ByteBuffer.wrap(requestLengthBytes).getInt();
            System.out.println("Request body length: " + requestLength);
            byte[] requestBody = bufferedInputStream.readNBytes(requestLength);
            ByteBuffer reqBuffer = ByteBuffer.allocate(requestLength).put(requestBody).rewind();
            short apiKey = reqBuffer.getShort();
            short apiVersion = reqBuffer.getShort();
            int correlationId = reqBuffer.getInt();
            System.out.println("CorrelationId: " + correlationId);
            System.out.println(apiKey);

            switch (apiKey) {
                case 18 :
                    sendAPIVersionsResponse(outputStream, apiVersion, correlationId);
                    break;
                case 75:
                    describeTopicPartionsHandler(reqBuffer, outputStream, correlationId);
                    break;
                default:
                    System.out.println("Unknown API Key: " + apiKey);
                    break;
            }
        }catch (IOException ex) {
            // do nothing
        }
    }

    private void sendAPIVersionsResponse(OutputStream out, short apiVersion, int correlationId)
            throws IOException {
        ByteBuffer resBuffer = ByteBuffer.allocate(1024);
        short error = 0;
        if (apiVersion < 0 || apiVersion > 4) {
            error = 35;
        }
        short API_KEY = 18;
        short MIN_VERSION = 0;
        short MAX_VERSION = 4;
        resBuffer = resBuffer.putInt(correlationId)
                .putShort(error)
                .put((byte) 3) // array length + 1
                .putShort(API_KEY)
                .putShort(MIN_VERSION)
                .putShort(MAX_VERSION)
                .put((byte) 0) // tagged_fields
                .putShort((short) 75)
                .putShort((short) 0)
                .putShort((short) 0)
                .put((byte) 0) // tagged_fields
                .putInt(0)    // throttle time
                .put((byte) 0) // tagged_fields
                .flip();
        byte[] res = new byte[resBuffer.remaining()];
        resBuffer.get(res);
        out.write(ByteBuffer.allocate(4).putInt(res.length).array());
        out.write(res);
        out.flush();
    }

    private  void describeTopicPartionsHandler(ByteBuffer reqBuffer,
                                                     OutputStream out,
                                                     int correlationId)
            throws IOException {
        System.out.println("Handling DescribeTopicPartition...");
        short clientIdLength = reqBuffer.getShort();
        String clientId = new String(getNBytes(reqBuffer, clientIdLength),
                StandardCharsets.UTF_8);
        System.out.println("ClientId: " + clientId);
        reqBuffer.get();
        byte topicArrayLength = reqBuffer.get();
        byte topicNameLength = reqBuffer.get();
        byte[] topicName = getNBytes(reqBuffer, topicNameLength);
        byte partitionsArray = 1;
        byte[] topicAuthorizedOperations = {0, 0, 0, 0, 1, 1, 0, 1,
                0, 1, 1, 1, 1, 0, 0, 0};
        final byte TAG_BUFFER = 0;
        final short ERROR_CODE = 3;
        byte[] topicId = new byte[16];
        byte isInternal = 0;
        ByteBuffer resBuffer = ByteBuffer.allocate(1024);
        resBuffer.putInt(correlationId)
                .put(TAG_BUFFER)
                .putInt(0)            // throttle_time
                .put((byte)2)         // array length + 1
                .putShort(ERROR_CODE) // error code for topic
                .put(topicNameLength)
                .put(topicName)
                .put(topicId)
                .put(isInternal)
                .put(partitionsArray)
                .put(topicAuthorizedOperations)
                .put(TAG_BUFFER)
                .put((byte)0xff) // Next Cursor: A nullable field that can be used for
                // pagination.
                .put(TAG_BUFFER)
                .flip();
        byte[] res = new byte[resBuffer.remaining()];
        resBuffer.get(res);
        out.write(ByteBuffer.allocate(4).putInt(res.length).array());
        out.write(res);
    }

    private byte[] getNBytes(ByteBuffer buffer, int n) {
        byte[] bytes = new byte[n];
        for (int i = 0; i < n; i++) {
            bytes[i] = buffer.get();
        }
        return bytes;
    }
}
