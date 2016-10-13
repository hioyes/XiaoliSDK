package com.xiaoli.library.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * zip工具类
 * xiaokx
 * hioyes@qq.com
 * 2016-6-19
 */
public class ZipUtils {
    public ZipUtils(){

    }

    /**
     * 解压zip文件
     * param zipFilePath  文件路径 /storage/emulated/0/zip/my.zip
     * param outFileDirectory  解压输出目录 /storage/emulated/0/upzip/
     * throws Exception
     */
    public static void UnZipFolder(String zipFilePath, String outFileDirectory) throws Exception {
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry zipEntry;
        String szName = "";
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                // get the folder name of the widget
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outFileDirectory + File.separator + szName);
                folder.mkdirs();
            } else {

                File file = new File(outFileDirectory + File.separator + szName);
                file.createNewFile();
                // get the output stream of the file
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // read (len) bytes into buffer
                while ((len = inZip.read(buffer)) != -1) {
                    // write (len) byte from buffer at the position 0
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        inZip.close();
    }

    /**
     * 将文件夹添加到压缩文件中
     * param srcFileDirectory   需要压缩的文件目录 /storage/emulated/0/my/
     * param zipFilePath   压缩文件输出路径 /storage/emulated/0/zip/my.zip
     * throws Exception
     */
    public static void ZipFolder(String srcFileDirectory, String zipFilePath)throws Exception {
        //create ZIP
        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFilePath));
        //create the file
        File file = new File(srcFileDirectory);
        //compress
        ZipFiles(file.getParent()+File.separator, file.getName(), outZip);
        //finish and close
        outZip.finish();
        outZip.close();
    }

    /**
     * 压缩文件
     * param folderDirectory 需要压缩的目录
     * param fileName
     * param zipOutputSteam 压缩输出流
     * throws Exception
     */
    private static void ZipFiles(String folderDirectory, String fileName, ZipOutputStream zipOutputSteam)throws Exception{
        if(zipOutputSteam == null)
            return;
        File file = new File(folderDirectory+fileName);
        if (file.isFile()) {
            ZipEntry zipEntry =  new ZipEntry(fileName);
            FileInputStream inputStream = new FileInputStream(file);
            zipOutputSteam.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[4096];
            while((len=inputStream.read(buffer)) != -1)
            {
                zipOutputSteam.write(buffer, 0, len);
            }
            zipOutputSteam.closeEntry();
        }
        else {
            String fileList[] = file.list();
            if (fileList.length <= 0) {
                ZipEntry zipEntry =  new ZipEntry(fileName+File.separator);
                zipOutputSteam.putNextEntry(zipEntry);
                zipOutputSteam.closeEntry();
            }
            for (int i = 0; i < fileList.length; i++) {
                ZipFiles(folderDirectory, fileName+java.io.File.separator+fileList[i], zipOutputSteam);
            }
        }
    }


    /**
     * 返回zip包中的文件或文件夹列表
     * param zipFilePath     需要解析的zip文件 /storage/emulated/0/zip/my.zip
     * param containFolder    是否包含文件夹
     * param containFile      是否包含文件
     * return
     * throws Exception
     */
    public static List<File> GetFileList(String zipFilePath, boolean containFolder, boolean containFile)throws Exception {
        List<File> fileList = new ArrayList<File>();
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry zipEntry;
        String szName = "";
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                // get the folder name of the widget
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(szName);
                if (containFolder) {
                    fileList.add(folder);
                }

            } else {
                File file = new File(szName);
                if (containFile) {
                    fileList.add(file);
                }
            }
        }
        inZip.close();
        return fileList;
    }
}
