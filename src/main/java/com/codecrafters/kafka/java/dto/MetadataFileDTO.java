package com.codecrafters.kafka.java.dto;

import java.util.List;

public class MetadataFileDTO {
    List<Batch> batches;

    public List<Batch> getBatches() {
        return batches;
    }

    public void setBatches(List<Batch> batches) {
        this.batches = batches;
    }

    @Override
    public String toString() {
        return "MetadataFileDTO{" +
                "batches=" + batches +
                '}';
    }
}
