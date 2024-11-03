package com.codecrafters.kafka.java.dto;

public class Record {

    private int length;
    private int attributes;
    private long timestampDelta;
    private long offsetDelta;
    private int keyLength;
    private String key;
    private int valueLength;
    private int recordFrameVersion;
    private int recordType;
    private int recordVersion;

    private int headersArrCount;

    private FeatureLevelRecord featureLevelRecord;
    private PartitionRecord partitionRecord;
    private TopicRecord topicRecord;

    public int getAttributes() {
        return attributes;
    }

    public void setAttributes(int attributes) {
        this.attributes = attributes;
    }

    public long getTimestampDelta() {
        return timestampDelta;
    }

    public void setTimestampDelta(long timestampDelta) {
        this.timestampDelta = timestampDelta;
    }

    public long getOffsetDelta() {
        return offsetDelta;
    }

    public void setOffsetDelta(long offsetDelta) {
        this.offsetDelta = offsetDelta;
    }

    public int getKeyLength() {
        return keyLength;
    }

    public void setKeyLength(int keyLength) {
        this.keyLength = keyLength;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getValueLength() {
        return valueLength;
    }

    public void setValueLength(int valueLength) {
        this.valueLength = valueLength;
    }

    public int getRecordFrameVersion() {
        return recordFrameVersion;
    }

    public void setRecordFrameVersion(int recordFrameVersion) {
        this.recordFrameVersion = recordFrameVersion;
    }

    public int getRecordType() {
        return recordType;
    }

    public void setRecordType(int recordType) {
        this.recordType = recordType;
    }

    public int getRecordVersion() {
        return recordVersion;
    }

    public void setRecordVersion(int recordVersion) {
        this.recordVersion = recordVersion;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getHeadersArrCount() {
        return headersArrCount;
    }

    public void setHeadersArrCount(int headersArrCount) {
        this.headersArrCount = headersArrCount;
    }

    public FeatureLevelRecord getFeatureLevelRecord() {
        return featureLevelRecord;
    }

    public void setFeatureLevelRecord(FeatureLevelRecord featureLevelRecord) {
        this.featureLevelRecord = featureLevelRecord;
    }

    public PartitionRecord getPartitionRecord() {
        return partitionRecord;
    }

    public void setPartitionRecord(PartitionRecord partitionRecord) {
        this.partitionRecord = partitionRecord;
    }

    public TopicRecord getTopicRecord() {
        return topicRecord;
    }

    public void setTopicRecord(TopicRecord topicRecord) {
        this.topicRecord = topicRecord;
    }

    @Override
    public String toString() {
        return "Record{" +
                "length=" + length +
                ", attributes=" + attributes +
                ", timestampDelta=" + timestampDelta +
                ", offsetDelta=" + offsetDelta +
                ", keyLength=" + keyLength +
                ", key='" + key + '\'' +
                ", valueLength=" + valueLength +
                ", recordFrameVersion=" + recordFrameVersion +
                ", recordType=" + recordType +
                ", recordVersion=" + recordVersion +
                ", headersArrCount=" + headersArrCount +
                ", featureLevelRecord=" + featureLevelRecord +
                ", partitionRecord=" + partitionRecord +
                ", topicRecord=" + topicRecord +
                '}';
    }
}
