package com.xiaoli.library.task;


import com.xiaoli.library.C;
import com.xiaoli.library.utils.FileUtils;

/**
 *日志写入线程实现类
 *  xiaokx
 *  hioyes@qq.com
 *  2014-11-6
 */
public class LogThread implements Runnable {

    /**
     * 存储目录 E:\\test\\
     */
    private String directory;

    /**
     *存储文件名 user.txt
     */
    private String fileName;

    /**
     *存储内容
     */
    private String fileContent;

    /**
     *
     * @param _fileName 存储文件名
     * @param _fileContent 存储内容
     */
    public LogThread(String _fileName, String _fileContent){
        this.directory = C.ROOT_CATALOG+"log/";
        this.fileName = _fileName;
        this.fileContent = _fileContent;
    }

    /**
     *
     * @param _directory 存储目录
     * @param _fileName 存储文件名
     * @param _fileContent 存储内容
     */
    public LogThread(String _directory,String _fileName, String _fileContent){
        this.directory = _directory;
        this.fileName = _fileName;
        this.fileContent = _fileContent;
    }
    @Override
    public void run() {
        FileUtils.writeAppend(this.directory,this.fileName,fileContent);
    }
}
