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
        File filePath = new File("./files/words.zip.hu");
        HuffmanModel model = new HuffmanModel();
        model.uncompress(filePath);
    }
}
