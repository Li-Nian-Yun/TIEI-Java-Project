package project;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class test {
    public static void main(String[] args) {
        File file = new File("C://Users//DELL//Desktop//java project//project//files//w.txt");

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] byteArray = new byte[(int) file.length()]; // 创建与文件大小相同的byte数组
            fis.read(byteArray); // 读取文件内容到byte数组

            // 打印byte数组内容
            for (byte b : byteArray) {
                System.out.print(b + " ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
