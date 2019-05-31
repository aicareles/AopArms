package cn.com.superLei.aoparms.common.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

/**
 * description $desc$
 * created by jerry on 2019/5/29.
 */
public class IOUtils {

    private final static int BUFFER_SIZE = 0x400; // 1024

    /**
     * 从输入流读取数据
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int len = 0;
        while( (len = inStream.read(buffer)) !=-1 ){
            if (len!=0) {
                outSteam.write(buffer, 0, len);
            }
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }

    /**
     * 从输入流读取数据
     * @param inStream
     * @return
     * @throws Exception
     */
    public static String inputStream2String(InputStream inStream) throws IOException{

        return new String(readInputStream(inStream), "UTF-8");
    }

    /**
     *
     * @param is
     * @param os
     * @throws IOException
     */
    public static void copyStream(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        for (;;) {
            int count = is.read(bytes, 0, BUFFER_SIZE);
            if (count == -1)
                break;
            os.write(bytes, 0, count);
        }
    }

    /**
     * 文件拷贝
     * @param src source {@link File}
     * @param dst destination {@link File}
     * @throws IOException
     */
    public static void copyFile(File src, File dst) throws IOException {
        FileInputStream in = new FileInputStream(src);
        FileOutputStream out = new FileOutputStream(dst);
        FileChannel inChannel = in.getChannel();
        FileChannel outChannel = out.getChannel();

        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }

        in.close();
        out.close();
    }

    /**
     * 将流写入文件
     * @param in
     * @param target
     * @throws IOException
     */
    public static void writeToFile(InputStream in, File target) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(target));
        int count;
        byte data[] = new byte[BUFFER_SIZE];
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
            bos.write(data, 0, count);
        }
        bos.close();
    }

    /**
     * 安全关闭io流
     * @param closeable
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 安全关闭io流
     * @param closeables
     */
    public static void closeQuietly(Closeable... closeables) {

        if (Preconditions.isNotBlank(closeables)) {

            for(Closeable closeable:closeables) {
                closeQuietly(closeable); // 系统先匹配确定参数的方法，没有再去匹配调用不定项参数的方法
            }
        }
    }
}
