package com.bigdata.etl.mr;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Partitioner;

public class TextLongPartition extends Partitioner<TextLongWritable, Writable> {

    public int getPartition(TextLongWritable textLongWritable, Writable writable, int numPartitions) {
        int hash = textLongWritable.getText().hashCode();
        return (hash & Integer.MAX_VALUE) % numPartitions;
    }

}
