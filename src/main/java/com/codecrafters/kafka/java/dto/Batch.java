package com.codecrafters.kafka.java.dto;

import java.util.List;

public class Batch {

    private long base_offset;
    private int batchLength;
    private int partition_leader_epoch;
    private int magic;
    private long crc;
    private int attributes;
    private int last_offset_delta;
    private long base_timestamp;
    private long max_timestamp;
    private long producer_id;
    private int producer_epoch;
    private int base_sequence;
    private int recordLength;
    private List<Record> records;

    public long getBase_offset() {
        return base_offset;
    }

    public void setBase_offset(long base_offset) {
        this.base_offset = base_offset;
    }

    public int getPartition_leader_epoch() {
        return partition_leader_epoch;
    }

    public void setPartition_leader_epoch(int partition_leader_epoch) {
        this.partition_leader_epoch = partition_leader_epoch;
    }

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public long getCrc() {
        return crc;
    }

    public void setCrc(long crc) {
        this.crc = crc;
    }

    public int getAttributes() {
        return attributes;
    }

    public void setAttributes(int attributes) {
        this.attributes = attributes;
    }

    public int getLast_offset_delta() {
        return last_offset_delta;
    }

    public void setLast_offset_delta(int last_offset_delta) {
        this.last_offset_delta = last_offset_delta;
    }

    public long getBase_timestamp() {
        return base_timestamp;
    }

    public void setBase_timestamp(long base_timestamp) {
        this.base_timestamp = base_timestamp;
    }

    public long getMax_timestamp() {
        return max_timestamp;
    }

    public void setMax_timestamp(long max_timestamp) {
        this.max_timestamp = max_timestamp;
    }

    public long getProducer_id() {
        return producer_id;
    }

    public void setProducer_id(long producer_id) {
        this.producer_id = producer_id;
    }

    public int getProducer_epoch() {
        return producer_epoch;
    }

    public void setProducer_epoch(int producer_epoch) {
        this.producer_epoch = producer_epoch;
    }

    public int getBase_sequence() {
        return base_sequence;
    }

    public void setBase_sequence(int base_sequence) {
        this.base_sequence = base_sequence;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public int getBatchLength() {
        return batchLength;
    }

    public void setBatchLength(int batchLength) {
        this.batchLength = batchLength;
    }

    public int getRecordLength() {
        return recordLength;
    }

    public void setRecordLength(int recordLength) {
        this.recordLength = recordLength;
    }

    @Override
    public String toString() {
        return "Batch{" +
                "base_offset=" + base_offset +
                ", batchLength=" + batchLength +
                ", partition_leader_epoch=" + partition_leader_epoch +
                ", magic=" + magic +
                ", crc=" + crc +
                ", attributes=" + attributes +
                ", last_offset_delta=" + last_offset_delta +
                ", base_timestamp=" + base_timestamp +
                ", max_timestamp=" + max_timestamp +
                ", producer_id=" + producer_id +
                ", producer_epoch=" + producer_epoch +
                ", base_sequence=" + base_sequence +
                ", recordLength=" + recordLength +
                ", records=" + records +
                '}';
    }
}
