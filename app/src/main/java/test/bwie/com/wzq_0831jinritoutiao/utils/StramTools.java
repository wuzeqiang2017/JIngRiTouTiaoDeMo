package test.bwie.com.wzq_0831jinritoutiao.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 移动1507D  武泽强
 * 2017/9/1.
 * 作用：
 */

public class StramTools {
    public static String GetRead(InputStream is){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len=0;
            byte[] arr= new byte[1024];
            while((len=is.read(arr))!=-1){
                baos.write(arr,0,len);
            }
            return baos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
