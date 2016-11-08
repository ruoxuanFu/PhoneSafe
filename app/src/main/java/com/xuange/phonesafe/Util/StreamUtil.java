package com.xuange.phonesafe.Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Xuange on 2016-10-17.
 */
public class StreamUtil {

    /**
     * @param is 流对象
     * @return 把流转换成字符串，返回null表示异常
     */
    public static String stream2String(InputStream is) {

        //在读取的过程中，将读取的内容存储在缓存中，然后一次性转换为字符串返回
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        //一直读，读到没有为止
        byte[] buffer = new byte[1024];
        //记录读取内容的循环变量
        int temp = -1;
        try {
            while ((temp = is.read(buffer)) != -1) {

                bos.write(buffer, 0 , temp);

            }

            return bos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;

    }

}
