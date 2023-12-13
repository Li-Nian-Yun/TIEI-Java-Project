package project;

/**
 * A class for file compression
 */
public class CompressFile {
    /**
     * Makes a file compressor
     */
    public CompressFile() {

    }

    /**
     * Compress a file
     */
    public void compress() {


    	System.out.println("file compression");
    }

    public static byte[] magicNumber(){
        byte[] magicNumber = new byte[5];
        int number = 123456789;
        magicNumber[0] = 4;
        magicNumber[1] = (byte) (number >> 24); // 第一个字节
        magicNumber[2] = (byte) (number >> 16); // 第二个字节
        magicNumber[3] = (byte) (number >> 8);  // 第三个字节
        magicNumber[4] = (byte) (number);       // 第四个字节
        return magicNumber;
    }


}
