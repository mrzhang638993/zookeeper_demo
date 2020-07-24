package cn.itcast.zookeeper_api;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class HdfsTest {

    /**
     * 根据url进行操作
     */
    public static void urlGet(String url, String filePath) throws IOException {
        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
        InputStream inputStream = new URL(url).openStream();
        FileOutputStream fos = new FileOutputStream(new File(filePath));
        IOUtils.copy(inputStream, fos);
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(fos);
    }

    /**
     * 使用文件系统进行资源访问,使用分布式文件系统进行访问
     */
    public static void fsget(String url, String filePath) throws IOException {
        //  获取文件系统
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://node01:8020");
        FileSystem fs = FileSystem.get(conf);
        copyFiles(url, filePath, fs);
    }

    /**
     * 使用文件系统的方式二：
     */
    public static void getFsByMethod2(String url, String filePath) throws URISyntaxException, IOException {
        FileSystem fs = FileSystem.get(new URI("hdfs://node01:8020"), new Configuration());
        copyFiles(url, filePath, fs);
    }

    /**
     * 使用文件系统的方式三：
     */
    public static void getFsByMethod3(String url, String filePath) throws IOException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://node01:8020");
        FileSystem fs = FileSystem.newInstance(conf);
        copyFiles(url, filePath, fs);
    }

    /**
     * 实现文件拷贝和下载
     */
    private static void copyFiles(String url, String filePath, FileSystem fs) throws IOException {
        Path path = new Path(url);
        Path path1 = new Path(filePath);
        fs.copyToLocalFile(path, path1);
        fs.close();
    }

    /**
     * 使用方法四：获取文件系统的方式4
     */
    public static void getFsByMethod4(String url, String filePath) throws URISyntaxException, IOException {
        FileSystem fs = FileSystem.newInstance(new URI("hdfs://node01:8020"), new Configuration());
        copyFiles(url, filePath, fs);
    }

    /**
     * 遍历hdfs的所有的文件系统
     */
    public static void getAllFiles() throws URISyntaxException, IOException {
        FileSystem fs = FileSystem.newInstance(new URI("hdfs://node01:8020"), new Configuration());
        RemoteIterator<LocatedFileStatus> it = fs.listFiles(new Path("/"), true);
        while (it.hasNext()) {
            LocatedFileStatus file = it.next();
            System.out.println(file.getPath());
            BlockLocation[] blockLocations = file.getBlockLocations();
            System.out.println(blockLocations.length);
        }
        fs.close();
    }

    /**
     * hdfs 创建文件夹
     */
    public static void createDir(String path) throws URISyntaxException, IOException {
        FileSystem fs = FileSystem.newInstance(new URI("hdfs://node01:8020"), new Configuration());
        boolean result = fs.mkdirs(new Path(path));
        //  创建文件
        FSDataOutputStream fsDataOutputStream = fs.create(new Path("/test/a.txt"));
        //当父目录不存在的时候，对应的会创建父目录进行操作的。
        fsDataOutputStream.write("many world".getBytes());
        System.out.println(result);
        fs.close();
    }

    /**
     * hdfs文件的上传操作
     */
    public static void fileUpload(String from, String toPath) throws IOException, URISyntaxException {
        FileSystem fs = FileSystem.newInstance(new URI("hdfs://node01:8020"), new Configuration());
        Path fromPath = new Path(from);
        Path toPaths = new Path(toPath);
        fs.copyFromLocalFile(fromPath, toPaths);
        fs.close();
    }

    /**
     * hdfs文件的下载操作
     */
    public static void downLoad(String path, String destPath) throws IOException, URISyntaxException {
        FileSystem fs = FileSystem.newInstance(new URI("hdfs://node01:8020"), new Configuration());
        FSDataInputStream open = fs.open(new Path(path));
        FileOutputStream fos = new FileOutputStream(new File(destPath));
        IOUtils.copy(open, fos);
        fs.close();
        IOUtils.closeQuietly(open);
        IOUtils.closeQuietly(fos);
    }

    /**
     * 更加简单的文件下载方式和操作
     */
    public static void downLoadFromFile(String url, String filePath) throws IOException, URISyntaxException, InterruptedException {
        //  调用方法实现文件的下载操作,可以指定用户的
        FileSystem fs = FileSystem.get(new URI("hdfs://node01:8020"), new Configuration(), "root");
        //  权限控制问题，需要对应的权限进行控制，对应的需要给出相关的用户和权限的。
        copyFiles(url, filePath, fs);
    }

    /**
     * 小文件的合并操作,小文件合并为大文件之后进行上传操作
     */
    public static void mergeFile(String url, String filePath) throws URISyntaxException, IOException, InterruptedException {
        // 执行小文件的合并操作和实现逻辑
        FileSystem fs = FileSystem.get(new URI("hdfs://node01:8020"), new Configuration(), "root");
        FSDataOutputStream fos = fs.create(new Path(filePath));
        // 获取本地文件系统内保存的小文件信息
        FileSystem localFs = FileSystem.getLocal(new Configuration());
        FileStatus[] fileStatuses = localFs.listStatus(new Path(url));
        for (FileStatus fileStatus : fileStatuses) {
            // 实现相关的数据的查找和逻辑操作
            FSDataInputStream os = localFs.open(fileStatus.getPath());
            IOUtils.copy(os, fos);
            IOUtils.closeQuietly(os);
        }
        //  最终实现文件的上传操作
        IOUtils.closeQuietly(fos);
        localFs.close();
        fs.close();
    }


    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        String url = "hdfs://node01:8020/a.txt";
        String filePath = "E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\hello.txt";
        //urlGet(url,filePath);
        //fsget(url,filePath);
        //getFsByMethod2();
        //getFsByMethod3(url,filePath);
        //getFsByMethod4(url,filePath);
        //getAllFiles();
        //createDir("/test");
        //downLoad("/test/dir1",filePath);
        //downLoadFromFile("/test/dir1",filePath);
        mergeFile("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\files\\", "/bigfile.txt");
    }
}
