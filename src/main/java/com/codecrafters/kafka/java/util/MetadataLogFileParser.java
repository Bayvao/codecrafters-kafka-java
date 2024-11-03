package com.codecrafters.kafka.java.util;

import com.codecrafters.kafka.java.dto.Batch;
import com.codecrafters.kafka.java.dto.FeatureLevelRecord;
import com.codecrafters.kafka.java.dto.MetadataFileDTO;
import com.codecrafters.kafka.java.dto.Record;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MetadataLogFileParser {

    Long b = 1726045943832L;

    private final String FILE_PATH = "/tmp/kraft-combined-logs/__cluster_metadata-0/00000000000000000000.log";
    public void parseMetadataLogFile(MetadataFileDTO metadataFileDTO) {

        List<Batch> batches = new ArrayList<>();

        try (InputStream in = Files.newInputStream(Paths.get(FILE_PATH));
             BufferedInputStream reader = new BufferedInputStream(in)) {

            byte[] data = reader.readAllBytes();
            ByteBuffer batchReqBuffer = ByteBuffer.allocate(data.length).put(data).rewind();

            while (batchReqBuffer.hasRemaining()) {
                Batch batch = new Batch();
                List<Record> records = new ArrayList<>();

                long baseOffset = batchReqBuffer.getLong(); // base offset
                batch.setBase_offset(baseOffset);
                System.out.println("baseOffset: " + baseOffset);
                int batchLength = batchReqBuffer.getInt(); // batch length
                System.out.println("Request body length: " + batchLength);
                batch.setBatchLength(batchLength);
                int partitionLeaderEpoch = batchReqBuffer.getInt();
                System.out.println("partitionLeaderEpoch: " + partitionLeaderEpoch);
                batch.setPartition_leader_epoch(partitionLeaderEpoch);
                short magicByte = batchReqBuffer.get();
                batch.setMagic(magicByte);
                System.out.println("magicByte: " + magicByte);
                long crc = Integer.toUnsignedLong(batchReqBuffer.getInt());
                batch.setCrc(crc);
                System.out.println("crc: " + crc);
                short attributes = batchReqBuffer.getShort();
                batch.setAttributes(attributes);
                System.out.println("attributes: " + attributes);
                int lastOffsetData = batchReqBuffer.getInt();
                batch.setLast_offset_delta(lastOffsetData);
                System.out.println("lastOffsetData: " + lastOffsetData);
                long baseTimestamp = batchReqBuffer.getLong();
                batch.setBase_timestamp(baseTimestamp);
                System.out.println("baseTimestamp: " + baseTimestamp);
                long maxTimestamp = batchReqBuffer.getLong();
                batch.setMax_timestamp(maxTimestamp);
                System.out.println("maxTimestamp: " + maxTimestamp);
                long producerId = batchReqBuffer.getLong();
                batch.setProducer_id(producerId);
                System.out.println("producerId: " + producerId);
                short producerEpoch = batchReqBuffer.getShort();
                batch.setProducer_epoch(producerEpoch);
                System.out.println("producerEpoch: " + producerEpoch);
                int baseSequence = batchReqBuffer.getInt();
                batch.setBase_sequence(baseSequence);
                System.out.println("baseSequence: " + baseSequence);
                int recordsLength = batchReqBuffer.getInt();
                System.out.println("recordsLength: " + recordsLength);
                batch.setRecordLength(recordsLength);

                while (recordsLength > 0) {
                    records.add(recordParser(batchReqBuffer));
                    recordsLength--;
                }

                batch.setRecords(records);
                batches.add(batch);
            }

            metadataFileDTO.setBatches(batches);

        } catch (IOException x) {
            System.err.println(x);
        }
    }

    private Record recordParser(ByteBuffer batchReqBuffer) {

        Record recordData = new Record();

        int length = batchReqBuffer.get();
        System.out.println("length: " + length);
        recordData.setLength(length);
        int attribute = batchReqBuffer.get();
        System.out.println("attribute: " + attribute);
        recordData.setAttributes(attribute);
        int timestampDelta = batchReqBuffer.get();
        System.out.println("timestampDelta: " + timestampDelta);
        recordData.setTimestampDelta(timestampDelta);
        int offsetDelta = batchReqBuffer.get();
        System.out.println("offsetDelta: " + offsetDelta);
        recordData.setOffsetDelta(offsetDelta);
        int keyLength = batchReqBuffer.get();
        System.out.println("keyLength: " + keyLength);
        recordData.setKeyLength(keyLength);
        String key = keyLength - 1 > 0 ? new String(getNBytes(batchReqBuffer, keyLength - 1), StandardCharsets.UTF_8) : null;
        System.out.println("key: " + key);
        recordData.setKey(key);
        int valueLength = batchReqBuffer.get();
        System.out.println("valueLength: " + valueLength);
        recordData.setValueLength(valueLength);
        int frameVersion = batchReqBuffer.get();
        System.out.println("frameVersion: " + frameVersion);
        recordData.setRecordFrameVersion(frameVersion);
        int recordType = batchReqBuffer.get();
        System.out.println("type: " + recordType);
        recordData.setRecordType(recordType);
        int recordVersion = batchReqBuffer.get();
        System.out.println("version: " + recordVersion);
        recordData .setRecordVersion(recordVersion);

        switch (recordType) {
            case 2:
                recordData.setFeatureLevelRecord(getfeatureLevelRecord(batchReqBuffer));
                break;
            case 3:
                break;
            case 12:
                break;
            default:
                throw new RuntimeException("Illegal record type: " + recordType);

        }

        int headerArrCount = batchReqBuffer.get();
        System.out.println("headerArrCount: " + headerArrCount);
        recordData.setHeadersArrCount(headerArrCount);

        return recordData;
    }

    private byte[] getNBytes(ByteBuffer buffer, int n) {
        byte[] bytes = new byte[n];
        for (int i = 0; i < n; i++) {
            bytes[i] = buffer.get();
        }
        return bytes;
    }

    private FeatureLevelRecord getfeatureLevelRecord(ByteBuffer batchReqBuffer) {

        FeatureLevelRecord featureLevelRecord = new FeatureLevelRecord();

        int nameLength = batchReqBuffer.get();
        System.out.println("nameLength: " + nameLength);
        featureLevelRecord.setNameLength(nameLength);
        String name = new String(getNBytes(batchReqBuffer, nameLength - 1), StandardCharsets.UTF_8);
        featureLevelRecord.setName(name);
        System.out.println("name: " + name);
        short featureLevel = batchReqBuffer.getShort();
        System.out.println("featureLevel: " + featureLevel);
        featureLevelRecord.setFeatureLevel(featureLevel);
        int taggedField = batchReqBuffer.get();
        System.out.println("taggedField: " + taggedField);
        featureLevelRecord.setTaggedFieldsCount(taggedField);

        return featureLevelRecord;
    }
}
