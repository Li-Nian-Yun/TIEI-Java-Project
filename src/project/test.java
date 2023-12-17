package project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class test {
    public static void main(String[] args) throws IOException {
//        File filePath = new File("./files/archive.tar.hu");
//        HuffmanModel model = new HuffmanModel();
//        model.uncompress(filePath);
        System.out.println(compareFiles("./groundTruthFiles/archive.tar", "./files/archive.tar"));
    }

    public static boolean compareFiles(String file1Path, String file2Path) throws IOException {
        try (FileInputStream fis1 = new FileInputStream(file1Path);
             FileInputStream fis2 = new FileInputStream(file2Path)) {

            int byte1, byte2;

            // 比较每个字节，直到文件结束
            while ((byte1 = fis1.read()) != -1) {
                byte2 = fis2.read();
                if (byte1 != byte2) {
                    return false; // 字节不匹配
                }
            }

            // 检查第二个文件是否还有更多内容
            return fis2.read() == -1;
        }
    }
}
