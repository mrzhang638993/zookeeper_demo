自定义inputFormat和实现小文件的合并操作,
合并的原理是如下的：主要的方法是nextKeyValue，每一个文件对应的会读取多次的。会创建多个对象的。
  /**
     *  该方法用于获取k1,v1。是很关键的
     *  k1 LongWritable
     *  v1 BytesWritable
     *  boolean  代表的是文件是否读取完成,会在程序读取文件的时候每一个文件读取2次的(一次是false执行操作，一次是true跳出的)。
     * */
    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        //  文件没有读取完成
        if (!processed){
            //  获取源文件的字节输入流
             fs=FileSystem.newInstance(conf);
            //  获取文件的输入流信息
             fopen = fs.open(fileSplit.getPath());
            // 1. 获取源文件的文件系统
            // 2.通过 fileSystem获取文件的字节输入流
            //  读取源文件数据到普通的字节数组
            // 将小文件的数据一定性的读取到字节数组中
            byte[] bytes=new byte[(int) fileSplit.getLength()];
            //  文件中的数据读取到字节数组中来进行操作
            IOUtils.readFully(fopen,bytes,0, (int) fileSplit.getLength());
            //  将普通字节数组封装到BytesWritable字节数组中
            by.set(bytes,0, (int) fileSplit.getLength());
            processed=true;
            return  true;
        }
        return false;
    }

