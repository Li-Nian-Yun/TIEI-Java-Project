package project;

import java.io.*;

import static project.HuffMan.delinearization;

/**
 * A class for file uncompression
 */
public class UncompressFile {

    private final int READ_BUFFER_SIZE = 1024;
    private final byte[] Buffer = new byte[READ_BUFFER_SIZE];
    private static int MAGIC_NUMBER = 123456789;

    private int currentByte;
    private int bitsRemaining;
    private Long fileBitSize;

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
            // 创建一个字节数组来存储读取的数据
            if (!verifyMagicNumber(fileInputStream)) {
                System.out.println("Magic number is not correct.");
                return;
            }
            // 读取huffman树
            TreeNode root = delinearization(fileInputStream);

            // 读取文件大小
            this.fileBitSize = getCompressedFileSize(fileInputStream);

            // 创建文件输出流,在原始目录下创建一个新的文件
            String outputFileName = input.getName().substring(0, input.getName().length() - 3);
            FileOutputStream fileOutputStream = new FileOutputStream(input.getParent() + "/" + outputFileName);

            // 读取文件内容并且做映射写入
            unCompressedFile(fileInputStream, fileOutputStream, root);

            fileInputStream.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    private void unCompressedFile(FileInputStream inputStream, FileOutputStream outputStream, TreeNode root) {
        try {
            TreeNode current = root;
            int nextBit;
            while ((nextBit = readNextBit(inputStream)) != -1) {
                // 根据读取的位移动到左或右
                current = (nextBit == 0) ? current.left : current.right;

                // 如果是叶子节点，则写入数据并重置到根节点
                if (current.left == null && current.right == null) {
                    outputStream.write(current.data);
                    current = root;
                }
                this.fileBitSize--;
                if (this.fileBitSize == 0) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 从输入流中读取下一个位
    public int readNextBit(FileInputStream inputStream) throws IOException {
        if (bitsRemaining == 0) {
            currentByte = inputStream.read();
            if (currentByte == -1) {
                return -1;
            }
            bitsRemaining = 8;
        }
        bitsRemaining--;
        return (currentByte >> bitsRemaining) & 1;
    }

    private Long getCompressedFileSize(FileInputStream fileInputStream) throws IOException {
        return getNumber(fileInputStream);
    }

    private boolean verifyMagicNumber(FileInputStream fileInputStream) throws IOException {
        // Read the magic number bytes
        return getNumber(fileInputStream) == MAGIC_NUMBER;
    }

    private Long getNumber(FileInputStream fileInputStream) throws IOException {
        // Read the first byte to determine the size of the magic number
        int size = fileInputStream.read();
        if (size <= 0) {
            // If the size is not positive, return false
            throw new IOException("File is corrupted.");
        }

        // Create a byte array to hold the number bytes
        byte[] buffer = new byte[size];

        // Read the number bytes
        int bytesRead = fileInputStream.read(buffer);
        if (bytesRead != size) {
            // If the correct number of bytes were not read, return false
            throw new IOException("File is corrupted.");
        }

        // Convert the bytes to an integer
        long readNumber = 0;
        for (int i = size - 1; i >= 0; i--) {
            readNumber = (readNumber << 8) | (buffer[i] & 0xFF);
        }
        return readNumber;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();

        for (byte b : bytes) {
            // 将byte转换为无符号并转换为十六进制字符串
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                // 保证每个字节转换后都是两位数
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
