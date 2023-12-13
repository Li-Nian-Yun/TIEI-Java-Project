package project;

import java.io.*;
import java.nio.Buffer;

/**
 * A class for file uncompression
 */
public class UncompressFile {

    private final int READ_BUFFER_SIZE = 1024;
    private final byte[] Buffer = new byte[READ_BUFFER_SIZE];
    private static int MAGIC_NUMBER = 123456789;

    public UncompressFile(int magicNumber) {
        MAGIC_NUMBER = magicNumber;
    }

    /**
     * Uncompress a file
     */
    public void uncompress(File input) {
        //判断文件是否存在
        if (!input.exists()) {
            System.out.println("file does not exist");
            return;
        }
        //判断文件是否为.hu文件
        if (!input.getName().endsWith(".hu")) {
            System.out.println("file is not a .hu file");
            return;
        }

    }
}
