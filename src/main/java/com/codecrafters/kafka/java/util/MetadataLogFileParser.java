package com.codecrafters.kafka.java.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MetadataLogFileParser {

    Long b = 1726045943832L;

    private static final String FILE_PATH = "/tmp/kraft-combined-logs/__cluster_metadata-0/00000000000000000000.log";
    public void parseMetadataLogFile() {


        try (InputStream in = Files.newInputStream(Paths.get(FILE_PATH));
             BufferedInputStream reader = new BufferedInputStream(in)) {

            byte[] data = reader.readAllBytes();
            ByteBuffer batchReqBuffer = ByteBuffer.allocate(data.length).put(data).rewind();

            long baseOffset = batchReqBuffer.getLong(); // base offset
            System.out.println("baseOffset: " + baseOffset);
            int batchLength = batchReqBuffer.getInt(); // batch length
            System.out.println("Request body length: " + batchLength);
            int partitionLeaderEpoch = batchReqBuffer.getInt();
            System.out.println("partitionLeaderEpoch: " + partitionLeaderEpoch);
            short magicByte = batchReqBuffer.get();
            System.out.println("magicByte: " + magicByte);
            int crc = batchReqBuffer.getInt();
            System.out.println("crc: " + crc);
            short attributes = batchReqBuffer.getShort();
            System.out.println("attributes: " + attributes);
            int lastOffsetData = batchReqBuffer.getInt();
            System.out.println("lastOffsetData: " + lastOffsetData);
            long baseTimestamp = batchReqBuffer.getLong();
            System.out.println("baseTimestamp: " + baseTimestamp);
            long maxTimestamp = batchReqBuffer.getLong();
            System.out.println("maxTimestamp: " + maxTimestamp);
            long producerId = batchReqBuffer.getLong();
            System.out.println("producerId: " + producerId);
            short producerEpoch = batchReqBuffer.getShort();
            System.out.println("producerEpoch: " + producerEpoch);
            int baseSequence = batchReqBuffer.getInt();
            System.out.println("baseSequence: " + baseSequence);
            int recordsLength = batchReqBuffer.getInt();
            System.out.println("recordsLength: " + recordsLength);
            int length = batchReqBuffer.get();
            System.out.println("length: " + length);
            int attribute = batchReqBuffer.get();
            System.out.println("attribute: " + attribute);
            int timestampDelta = batchReqBuffer.get();
            System.out.println("timestampDelta: " + timestampDelta);
            int offsetDelta = batchReqBuffer.get();
            System.out.println("offsetDelta: " + offsetDelta);
            int keyLength = batchReqBuffer.get();
            System.out.println("keyLength: " + keyLength);
            String key = null;
            int valueLength = batchReqBuffer.get();
            System.out.println("valueLength: " + valueLength);
            int frameVersion = batchReqBuffer.get();
            System.out.println("frameVersion: " + frameVersion);
            int type = batchReqBuffer.get();
            System.out.println("type: " + type);
            int version = batchReqBuffer.get();
            System.out.println("version: " + version);
            int nameLength = batchReqBuffer.get();
            String name = new String(getNBytes(batchReqBuffer, nameLength - 1), StandardCharsets.UTF_8);
            System.out.println("name: " + name);
            short featureLevel = batchReqBuffer.getShort();
            System.out.println("featureLevel: " + featureLevel);
            int taggedField = batchReqBuffer.get();
            System.out.println("taggedField: " + taggedField);
            int headerArrCount = batchReqBuffer.get();




        } catch (IOException x) {
            System.err.println(x);
        }
    }

    private byte[] getNBytes(ByteBuffer buffer, int n) {
        byte[] bytes = new byte[n];
        for (int i = 0; i < n; i++) {
            bytes[i] = buffer.get();
        }
        return bytes;
    }
}
