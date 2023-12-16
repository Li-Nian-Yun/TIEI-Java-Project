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
    private final int BYTE_MAX = 128;
    private static int MAGIC_NUMBER;
    /**
     * Makes a file compressor
     */
    public CompressFile(int magicNumber) {
        MAGIC_NUMBER = magicNumber;
    }

    /**
     * Compress a file
     */
    public void compress(File inputFile) {
        File outputFile =  new File(inputFile.toString()+".hua");
        zipFile(inputFile,outputFile);
        System.out.println("file compression");
    }

    public void zipFile(File inputFile, File outputFile) {
        FileInputStream is = null;
        OutputStream os = null;
        try {
            is=new FileInputStream(inputFile);
            os=new FileOutputStream(outputFile);
            byte[] compressedFile;
            compressedFile = huffmanCompress(inputFile);
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

    // package the Huffman encoding process for easy calling
    public byte[] huffmanCompress(File FilePath){
        List<TreeNode> contentList = HuffMan.getCounts(FilePath);//Convert byte arrays into a List composed of Nodes
        HuffMan.root = HuffMan.createHuffmanTree(contentList);
        Map<Byte,String> huffmanCodes = HuffMan.getCodes(HuffMan.root);
        return HuffMan.compress(FilePath,huffmanCodes);
    }

    public byte[] magicNumber(){
        int data = MAGIC_NUMBER;
        int length = 1;
        while (data >= BYTE_MAX) {
            data /= BYTE_MAX;
            length++;
        }
        byte[] magicNumber = new byte[length+1];
        magicNumber[0] = (byte) length;
        for(int i = 1; i < length+1; i++){
            magicNumber[i] = (byte) (MAGIC_NUMBER >> (length - i) * 8);
        }
        return magicNumber;
    }

    public byte[] numberOfDataBits(){
        int data = HuffMan.dataSize;
        int length = 1;
        while (data >= BYTE_MAX) {
            data /= BYTE_MAX;
            length++;
        }
        byte[] dataBits = new byte[length];
        for(int i = 0; i < length; i++){
            dataBits[i] = (byte) (HuffMan.dataSize >> (length - i - 1) * 8);
        }
        return dataBits;
    }
}
