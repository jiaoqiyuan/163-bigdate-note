package com.bigdata.etl.mr;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class TextLongGroupComparator extends WritableComparator {

    public TextLongGroupComparator() {
        super(TextLongWritable.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        TextLongWritable textLongA = (TextLongWritable) a;
        TextLongWritable textLongB = (TextLongWritable) b;

        return textLongA.getText().compareTo(textLongB.getText());

    }
}
