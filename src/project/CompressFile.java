package project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

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
    public void compress(File inputFile) {
        File outputFile =  new File(inputFile.toString()+".hu");
        zipFile(inputFile,outputFile);
    	System.out.println("file compression");
    }

    public  void zipFile(File srcFile, File dstFile) {
        FileInputStream is = null;
        OutputStream os = null;
        try {
            is=new FileInputStream(srcFile);
            os=new FileOutputStream(dstFile);

            byte[] compressedFile;//得到完整霍夫曼二进制长字符串转化为用于传输的byte形式
            compressedFile = huffmanZip(srcFile);

            //存放magic number
            os.write(magicNumber());

            //存放huffman tree
            byte[] huffmanTree = HuffMan.linearization(HuffMan.root);
            os.write(huffmanTree);

            //存放number of data bits
            byte[] dataBits = numberOfDataBits();
            os.write(dataBits.length);
            os.write(dataBits);

            //存放compressed file
            os.write(compressedFile);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            try {
                os.close();
                is.close();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    // 将霍夫曼编码过程封装起来，便于调用
    public byte[] huffmanZip(File FilePath){
        List<TreeNode> contentList = HuffMan.getCounts(FilePath);//将字节数组转换为Node组成的List
        HuffMan.root = HuffMan.createHuffmanTree(contentList);
        Map<Byte,String> huffmanCodes = HuffMan.getCodes(HuffMan.root);
        byte[] zipCodes = HuffMan.zip(FilePath,huffmanCodes);
        return zipCodes;
    }

    public byte[] magicNumber(){
        byte[] magicNumber = new byte[5];
        int number = 123456789;
        magicNumber[0] = 4;
        magicNumber[1] = (byte) (number >> 24); // 第一个字节
        magicNumber[2] = (byte) (number >> 16); // 第二个字节
        magicNumber[3] = (byte) (number >> 8);  // 第三个字节
        magicNumber[4] = (byte) (number);       // 第四个字节
        return magicNumber;
    }

    public byte[] numberOfDataBits(){
        int data = HuffMan.dataSize;
        int length = 1;
        while (data >= 128) {
            data /= 128;
            length++;
        }
        System.out.println(length);
        byte[] dataBits = new byte[length];
        for(int i = 0; i < length; i++){
            dataBits[i] = (byte) (HuffMan.dataSize >> (length - i - 1) * 8);
        }
        return dataBits;
    }

}
