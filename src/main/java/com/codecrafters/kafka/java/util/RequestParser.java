package com.codecrafters.kafka.java.util;

import com.codecrafters.kafka.java.dto.Batch;
import com.codecrafters.kafka.java.dto.MetadataFileDTO;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RequestParser {

    private static final int UNSUPPORTED_VERSION_ERROR_CODE = 35;
    private static final int NO_ERROR_CODE = 0;
    private static final byte TAG_BUFFER = 0;
    private static final short ERROR_CODE = 3;

    private static final short API_KEY = 18;
    private static final short MIN_VERSION = 0;
    private static final short MAX_VERSION = 4;
    public static final short DESCRIBE_TOPIC_PARTITION_KEY = 75;
    public static final int PAGINATION_FIELD = 0xff;
    public static final int ARRAY_LEN_2 = 2;
    public static final int EMPTY_FIELDS = 0;
    public static final int ARRAY_LEN_3 = 3;
    public static final int TROTTLE_TIME = 0;

    private static final String FILE_PATH = "/tmp/kraft-combined-logs/__cluster_metadata-0/00000000000000000000.log";

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
        short error = NO_ERROR_CODE;
        if (apiVersion < 0 || apiVersion > 4) {
            error = UNSUPPORTED_VERSION_ERROR_CODE;
        }

        resBuffer = resBuffer.putInt(correlationId)
                .putShort(error)
                .put((byte) ARRAY_LEN_3) // array length + 1
                .putShort(API_KEY)
                .putShort(MIN_VERSION)
                .putShort(MAX_VERSION)
                .put(TAG_BUFFER) // tagged_fields
                .putShort(DESCRIBE_TOPIC_PARTITION_KEY)
                .putShort((short) EMPTY_FIELDS)
                .putShort((short) EMPTY_FIELDS)
                .put(TAG_BUFFER) // tagged_fields
                .putInt(TROTTLE_TIME)    // throttle time
                .put(TAG_BUFFER) // tagged_fields
                .flip();
        byte[] res = new byte[resBuffer.remaining()];
        resBuffer.get(res);
        out.write(ByteBuffer.allocate(4).putInt(res.length).array());
        out.write(res);
        out.flush();
    }

    private  void describeTopicPartionsHandler(ByteBuffer reqBuffer,
                                                     OutputStream out,
                                                     int correlationId) throws IOException {
        System.out.println("Handling DescribeTopicPartition...");

//        try (InputStream in = Files.newInputStream(Paths.get(FILE_PATH));
//             BufferedInputStream reader = new BufferedInputStream(in)) {
//            byte[] data = reader.readAllBytes();
//            ByteBuffer batchReqBuffer = ByteBuffer.allocate(data.length).put(data).rewind();
//
//            long baseOffset = batchReqBuffer.getLong(); // base offset
//            System.out.println("baseOffset: " + baseOffset);
//            int batchLength = batchReqBuffer.getInt(); // batch length
//            System.out.println("Request body length: " + batchLength);
//            int partitionLeaderEpoch = batchReqBuffer.getInt();
//            System.out.println("partitionLeaderEpoch: " + partitionLeaderEpoch);
//            short magicByte = batchReqBuffer.get();
//            System.out.println("magicByte: " + magicByte);
//            int crc = batchReqBuffer.getInt();
//            System.out.println("crc: " + crc);
//            short attributes = batchReqBuffer.getShort();
//            System.out.println("attributes: " + attributes);
//            int lastOffsetData = batchReqBuffer.getInt();
//            System.out.println("lastOffsetData: " + lastOffsetData);
//            long baseTimestamp = batchReqBuffer.getLong();
//            System.out.println("baseTimestamp: " + baseTimestamp);
//            long maxTimestamp = batchReqBuffer.getLong();
//            System.out.println("maxTimestamp: " + maxTimestamp);
//            long producerId = batchReqBuffer.getLong();
//            System.out.println("producerId: " + producerId);
//            short producerEpoch = batchReqBuffer.getShort();
//            System.out.println("producerEpoch: " + producerEpoch);
//            int baseSequence = batchReqBuffer.getInt();
//            System.out.println("baseSequence: " + baseSequence);
//            int recordsLength = batchReqBuffer.getInt();
//            System.out.println("recordsLength: " + recordsLength);
//            int length = batchReqBuffer.get();
//            System.out.println("length: " + length);
//            int attribute = batchReqBuffer.get();
//            System.out.println("attribute: " + attribute);
//            int timestampDelta = batchReqBuffer.get();
//            System.out.println("timestampDelta: " + timestampDelta);
//            int offsetDelta = batchReqBuffer.get();
//            System.out.println("offsetDelta: " + offsetDelta);
//            int keyLength = batchReqBuffer.get();
//            System.out.println("keyLength: " + keyLength);
//            String key = null;
//            int valueLength = batchReqBuffer.get();
//            System.out.println("valueLength: " + valueLength);
//            int frameVersion = batchReqBuffer.get();
//            System.out.println("frameVersion: " + frameVersion);
//            int type = batchReqBuffer.get();
//            System.out.println("type: " + type);
//            int version = batchReqBuffer.get();
//            System.out.println("version: " + version);
//            int nameLength = batchReqBuffer.get();
//            String name = new String(getNBytes(batchReqBuffer, nameLength - 1), StandardCharsets.UTF_8);
//            System.out.println("name: " + name);
//            short featureLevel = batchReqBuffer.getShort();
//            System.out.println("featureLevel: " + featureLevel);
//            int taggedField = batchReqBuffer.get();
//            System.out.println("taggedField: " + taggedField);
//            int headerArrCount = batchReqBuffer.get();
//
//
//
//
//        } catch (IOException x) {
//            System.err.println(x);
//        }

        MetadataFileDTO metadataFileDTO = new MetadataFileDTO();
        MetadataLogFileParser metadataLogFileParser = new MetadataLogFileParser();
        metadataLogFileParser.parseMetadataLogFile(metadataFileDTO);

        System.out.println(metadataFileDTO);


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

        byte[] topicId = new byte[16];
        byte isInternal = 0;
        ByteBuffer resBuffer = ByteBuffer.allocate(1024);
        resBuffer.putInt(correlationId)
                .put(TAG_BUFFER)
                .putInt(0)            // throttle_time
                .put((byte) ARRAY_LEN_2)         // array length + 1
                .putShort(ERROR_CODE) // error code for topic
                .put(topicNameLength)
                .put(topicName)
                .put(topicId)
                .put(isInternal)
                .put(partitionsArray)
                .put(topicAuthorizedOperations)
                .put(TAG_BUFFER)
                .put((byte) PAGINATION_FIELD) // Next Cursor: A nullable field that can be used for
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
