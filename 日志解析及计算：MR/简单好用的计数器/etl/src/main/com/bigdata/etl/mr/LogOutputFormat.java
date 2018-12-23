package com.bigdata.etl.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.OutputCommitter;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.TaskID;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;

public class LogOutputFormat<K, V> extends TextOutputFormat<K, V> {

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();

    static {
        NUMBER_FORMAT.setMinimumIntegerDigits(5);
        NUMBER_FORMAT.setGroupingUsed(false);
    }

    private RecordWriter writer = null;
    @Override
    public RecordWriter<K, V> getRecordWriter(TaskAttemptContext job) throws IOException, InterruptedException {
        if (writer == null) {
            writer = new MultiRecordWriter(job, getTaskOutputPath(job));
        }
        return writer;
    }

    private Path getTaskOutputPath(TaskAttemptContext conf) throws IOException {
        Path taskOutputPath;
        OutputCommitter committer = getOutputCommitter(conf);
        if (committer instanceof FileOutputCommitter) {
            taskOutputPath = ((FileOutputCommitter) committer).getWorkPath();
        } else {
            Path outputPaht = getOutputPath(conf);
            if (outputPaht == null) {
                throw new IOException("Undefined job output path.");
            }
            taskOutputPath = outputPaht;
        }
        return taskOutputPath;
    }

    public class MultiRecordWriter extends RecordWriter<K, V> {
        private HashMap<String, RecordWriter<K, V>> recordWriter;
        private TaskAttemptContext job;
        private Path outputPath;

        public MultiRecordWriter(TaskAttemptContext job, Path outputPath) {
            super();
            this.job = job;
            this.recordWriter = new HashMap<String, RecordWriter<K, V>>();
            this.outputPath = outputPath;
        }

        private String getFileBaseName(K key, String name) {
            return new StringBuilder(60).append(key.toString()).append("-").append(name).toString();
        }

        public void write(K key, V value) throws IOException, InterruptedException {
            TaskID taskID = job.getTaskAttemptID().getTaskID();
            int partition = taskID.getId();
            String baseName = getFileBaseName(key, NUMBER_FORMAT.format(partition));
            RecordWriter<K, V> rw = this.recordWriter.get(baseName);
            if (rw == null) {
                rw = getBaseRecordWriter(job, baseName);
                this.recordWriter.put(baseName, rw);
            }
            rw.write(null, value);
        }

        public void close(TaskAttemptContext context) throws IOException, InterruptedException {
            Iterator<RecordWriter<K, V>> values = this.recordWriter.values().iterator();
            while (values.hasNext()) {
                values.next().close(context);
            }
            this.recordWriter.clear();

        }

        private RecordWriter<K, V> getBaseRecordWriter(TaskAttemptContext job, String baseName) throws IOException {
            RecordWriter<K, V> recordWriter;
            boolean isCompressed = getCompressOutput(job);
            Configuration conf = job.getConfiguration();

            if (isCompressed) {
                Class<? extends CompressionCodec > codecClass = getOutputCompressorClass(job, GzipCodec.class);
                CompressionCodec codec = ReflectionUtils.newInstance(codecClass, conf);

                Path file = new Path(outputPath, baseName + codec.getDefaultExtension());
                FSDataOutputStream fileOut = file.getFileSystem(conf).create(file, false);
                recordWriter = new LineRecordWriter<K, V>(new DataOutputStream(codec.createOutputStream(fileOut)));
            } else {
                Path file = new Path(outputPath, baseName);
                FSDataOutputStream fileOut = file.getFileSystem(conf).create(file, false);
                recordWriter = new LineRecordWriter<K, V>(fileOut);
            }

            return recordWriter;
        }
    }
}
