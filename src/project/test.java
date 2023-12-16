package project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class test {
    public static void main(String[] args) throws FileNotFoundException {
        File filePath = new File("./files/image.png.hu");
        Map<Byte,Integer> counts=new HashMap<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            byte[] buffer = new byte[3096];
            while ( fileInputStream.read(buffer) != -1) {
                for(byte item:buffer){
                    System.out.println(item);
                }
//                fileInputStream.close();
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
