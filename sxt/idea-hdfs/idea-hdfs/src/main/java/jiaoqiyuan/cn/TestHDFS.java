package jiaoqiyuan.cn;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class TestHDFS {

    private static final String HDFS_URI = "hdfs://mycluster";

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        Configuration conf = new Configuration(true);
        FileSystem fs = FileSystem.get(new URI(HDFS_URI), conf, "root");
        Path ifile = new Path("/user/root/jiao");
        if (fs.exists(ifile)) {
            fs.delete(ifile, true);
        }
        fs.mkdirs(ifile);
        fs.close();
    }
}
