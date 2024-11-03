package com.codecrafters.kafka.java.dto;

import java.util.UUID;

public class PartitionRecord {

    private int partitionId;
    private UUID topicUUID;
    private int replicaArrLength;
    private int replicas;
    private int inSyncReplicaLength;
    private int inSyncReplicas;
    private int removingReplicaArrLength;
    private int addingReplicaArrLength;
    private int leader;
    private int leaderEpoch;
    private int partitionEpoch;
    private int directoryArrLength;
    private UUID directories;
    private int taggedField;


    public int getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(int partitionId) {
        this.partitionId = partitionId;
    }

    public UUID getTopicUUID() {
        return topicUUID;
    }

    public void setTopicUUID(UUID topicUUID) {
        this.topicUUID = topicUUID;
    }

    public int getReplicaArrLength() {
        return replicaArrLength;
    }

    public void setReplicaArrLength(int replicaArrLength) {
        this.replicaArrLength = replicaArrLength;
    }

    public int getReplicas() {
        return replicas;
    }

    public void setReplicas(int replicas) {
        this.replicas = replicas;
    }

    public int getInSyncReplicaLength() {
        return inSyncReplicaLength;
    }

    public void setInSyncReplicaLength(int inSyncReplicaLength) {
        this.inSyncReplicaLength = inSyncReplicaLength;
    }

    public int getInSyncReplicas() {
        return inSyncReplicas;
    }

    public void setInSyncReplicas(int inSyncReplicas) {
        this.inSyncReplicas = inSyncReplicas;
    }

    public int getRemovingReplicaArrLength() {
        return removingReplicaArrLength;
    }

    public void setRemovingReplicaArrLength(int removingReplicaArrLength) {
        this.removingReplicaArrLength = removingReplicaArrLength;
    }

    public int getAddingReplicaArrLength() {
        return addingReplicaArrLength;
    }

    public void setAddingReplicaArrLength(int addingReplicaArrLength) {
        this.addingReplicaArrLength = addingReplicaArrLength;
    }

    public int getLeader() {
        return leader;
    }

    public void setLeader(int leader) {
        this.leader = leader;
    }

    public int getLeaderEpoch() {
        return leaderEpoch;
    }

    public void setLeaderEpoch(int leaderEpoch) {
        this.leaderEpoch = leaderEpoch;
    }

    public int getPartitionEpoch() {
        return partitionEpoch;
    }

    public void setPartitionEpoch(int partitionEpoch) {
        this.partitionEpoch = partitionEpoch;
    }

    public int getDirectoryArrLength() {
        return directoryArrLength;
    }

    public void setDirectoryArrLength(int directoryArrLength) {
        this.directoryArrLength = directoryArrLength;
    }

    public UUID getDirectories() {
        return directories;
    }

    public void setDirectories(UUID directories) {
        this.directories = directories;
    }

    public int getTaggedField() {
        return taggedField;
    }

    public void setTaggedField(int taggedField) {
        this.taggedField = taggedField;
    }

    @Override
    public String toString() {
        return "PartitionRecord{" +
                "partitionId=" + partitionId +
                ", topicUUID=" + topicUUID +
                ", replicaArrLength=" + replicaArrLength +
                ", replicas=" + replicas +
                ", inSyncReplicaLength=" + inSyncReplicaLength +
                ", inSyncReplicas=" + inSyncReplicas +
                ", removingReplicaArrLength=" + removingReplicaArrLength +
                ", addingReplicaArrLength=" + addingReplicaArrLength +
                ", leader=" + leader +
                ", leaderEpoch=" + leaderEpoch +
                ", partitionEpoch=" + partitionEpoch +
                ", directoryArrLength=" + directoryArrLength +
                ", directories=" + directories +
                ", taggedField=" + taggedField +
                '}';
    }
}
