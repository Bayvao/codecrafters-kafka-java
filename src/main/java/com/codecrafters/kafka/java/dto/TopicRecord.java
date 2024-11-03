package com.codecrafters.kafka.java.dto;

import java.util.UUID;

public class TopicRecord {
    private int length;
    private String topicName;

    private UUID topicUUID;

    private int taggedFieldsCount;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public UUID getTopicUUID() {
        return topicUUID;
    }

    public void setTopicUUID(UUID topicUUID) {
        this.topicUUID = topicUUID;
    }

    public int getTaggedFieldsCount() {
        return taggedFieldsCount;
    }

    public void setTaggedFieldsCount(int taggedFieldsCount) {
        this.taggedFieldsCount = taggedFieldsCount;
    }

    @Override
    public String toString() {
        return "TopicRecord{" +
                "length=" + length +
                ", topicName='" + topicName + '\'' +
                ", topicUUID=" + topicUUID +
                '}';
    }
}

