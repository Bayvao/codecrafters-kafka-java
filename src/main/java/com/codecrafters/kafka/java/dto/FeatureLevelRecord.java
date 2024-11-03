package com.codecrafters.kafka.java.dto;

public class FeatureLevelRecord {
    private int nameLength;
    private String name;
    private int featureLevel;

    private int taggedFieldsCount;

    public int getNameLength() {
        return nameLength;
    }

    public void setNameLength(int nameLength) {
        this.nameLength = nameLength;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFeatureLevel() {
        return featureLevel;
    }

    public void setFeatureLevel(int featureLevel) {
        this.featureLevel = featureLevel;
    }

    public int getTaggedFieldsCount() {
        return taggedFieldsCount;
    }

    public void setTaggedFieldsCount(int taggedFieldsCount) {
        this.taggedFieldsCount = taggedFieldsCount;
    }

    @Override
    public String toString() {
        return "FeatureLevelRecord{" +
                "nameLength=" + nameLength +
                ", name='" + name + '\'' +
                ", featureLevel=" + featureLevel +
                ", taggedFieldsCount=" + taggedFieldsCount +
                '}';
    }
}
