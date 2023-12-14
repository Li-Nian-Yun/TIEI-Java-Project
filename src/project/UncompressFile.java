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

        try {
            // 创建文件输入流
            FileInputStream fileInputStream = new FileInputStream(input);
//            byte[] buffer = new byte[READ_BUFFER_SIZE];
//            int bytesRead;
//            // 循环读取文件内容
//            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
//                for (byte item : buffer) {
//                    System.out.println(item);
//                }
//            }
            // 创建一个字节数组来存储读取的数据
            if(!verifyMagicNumber(fileInputStream)) {
                System.out.println("Magic number is not correct.");
                return;
            }

            fileInputStream.close();
        } catch (IOException e) {
            System.out.println("读取文件发生错误");
        }

    }

    private boolean verifyMagicNumber(FileInputStream fileInputStream) throws IOException {
        // Read the first byte to determine the size of the magic number
        int size = fileInputStream.read();
        if (size <= 0) {
            // If the size is not positive, return false
            return false;
        }

        // Create a byte array to hold the magic number bytes
        byte[] buffer = new byte[size];

        // Read the magic number bytes
        int bytesRead = fileInputStream.read(buffer);
        if (bytesRead != size) {
            // If the correct number of bytes were not read, return false
            return false;
        }

        // Convert the bytes to an integer
        int readMagicNumber = 0;
        for (int i = 0; i < size; i++) {
            readMagicNumber = (readMagicNumber << 8) | (buffer[i] & 0xFF);
        }

        // Compare the read magic number with the MAGIC_NUMBER
        return readMagicNumber == MAGIC_NUMBER;
    }

    private

}
