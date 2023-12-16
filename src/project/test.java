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
        File filePath = new File("./files/w1.txt.hua");
        Map<Byte,Integer> counts=new HashMap<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            byte[] buffer = new byte[3096];
            while ( fileInputStream.read(buffer) != -1) {
                System.out.println(buffer);
                for(byte item:buffer){
                    if(item!=0){
                        System.out.println(item);
                    }
                }
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
