package project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class test {
    public static void main(String[] args) throws FileNotFoundException {
<<<<<<< HEAD
        String filePath = "./files/image.png.hu";

        try {
            // 创建文件输入流
            FileInputStream fileInputStream = new FileInputStream(filePath);
            // 创建一个字节数组来存储读取的数据
            byte[] buffer = new byte[1024];
            int bytesRead;
            // 循环读取文件内容
            fileInputStream.read(buffer);
            for(byte item:buffer){
                System.out.println(item);
            }
//            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
//                for(byte item:buffer){
//                    System.out.println(item);
//                }
//            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
=======
        String filePath = "./src/files/archive.tar.hu";
        File inputFile = new File(filePath);
        UncompressFile cf = new UncompressFile(123456789);
        cf.uncompress(inputFile);
>>>>>>> 3a85ffbd86350db75e2ee9bda8838caa61550a86
    }
}
