package com.taosdata.jdbc.ws.tmq.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.taosdata.jdbc.tmq.TopicPartition;
import com.taosdata.jdbc.ws.entity.Payload;

public class PositionReq extends Payload {

    @JSONField(name = "topic_vgroup_ids")
    private TopicPartition[] topicPartitions;

    public TopicPartition[] getTopicPartitions() {
        return topicPartitions;
    }

    public void setTopicPartitions(TopicPartition[] topicPartitions) {
        this.topicPartitions = topicPartitions;
    }
}
