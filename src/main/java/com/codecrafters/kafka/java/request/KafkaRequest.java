package com.codecrafters.kafka.java.request;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class KafkaRequest {

//    public static void main(String[] args) {
//
//        final String FILE_PATH = "C:\\Users\\baverma\\Downloads\\Learning\\Java Projects\\Spring Boot\\CodeCrafters\\metadata.log";
//
//        try (InputStream in = Files.newInputStream(Paths.get(FILE_PATH));
//             BufferedInputStream reader = new BufferedInputStream(in)) {
//
//            byte[] baseOffsetBytes = reader.readNBytes(8); // base offset
//            byte[] batchLengthBytes = reader.readNBytes(4); // batch length
//            int batchLength = ByteBuffer.wrap(batchLengthBytes).getInt();
//            System.out.println("Request body length: " + batchLength);
//            byte[] requestBody = reader.readNBytes(batchLength);
//            ByteBuffer batchReqBuffer = ByteBuffer.allocate(batchLength).put(requestBody).rewind();
//            int partitionLeaderEpoch = batchReqBuffer.getInt();
//            short magicByte = batchReqBuffer.get();
//            int crc = batchReqBuffer.getInt();
//            short attributes = batchReqBuffer.getShort();
//            int lastOffsetData = batchReqBuffer.getInt();
//            long baseTimestamp = batchReqBuffer.getLong();
//            long maxTimestamp = batchReqBuffer.getLong();
//            long producerId = batchReqBuffer.getLong();
//            short producerEpoch = batchReqBuffer.getShort();
//            int baseSequence = batchReqBuffer.getInt();
//            int recordsLength = batchReqBuffer.getInt();
//            int length = batchReqBuffer.get();
//            int attribute = batchReqBuffer.get();
//            int timestampDelta = batchReqBuffer.get();
//            int offsetDelta = batchReqBuffer.get();
//            int keyLength = batchReqBuffer.get();
//            String key = null;
//            int valueLength = batchReqBuffer.get();
//            int frameVersion = batchReqBuffer.get();
//            int type = batchReqBuffer.get();
//            int version = batchReqBuffer.get();
//            int nameLength = batchReqBuffer.get();
//            String name = new String(getNBytes(batchReqBuffer, nameLength - 1), StandardCharsets.UTF_8);
//            short featureLevel = batchReqBuffer.getShort();
//            int taggedField = batchReqBuffer.get();
//            int headerArrCount = batchReqBuffer.get();
//
//
//
//
//        } catch (IOException x) {
//            System.err.println(x);
//        }
//
//    }
//
//    private static byte[] getNBytes(ByteBuffer buffer, int n) {
//        byte[] bytes = new byte[n];
//        for (int i = 0; i < n; i++) {
//            bytes[i] = buffer.get();
//        }
//        return bytes;
//    }
//

}
