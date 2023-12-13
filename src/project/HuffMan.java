package project;

import java.io.*;
import java.util.*;

public class HuffMan {
    public static void main(String[] args) {
        //测试压缩文件
        String srcFile = "C:\\Users\\DELL\\Desktop\\java\\TIEI-Java-Project\\src\\files\\word.txt";
        String dstFile = "C:\\Users\\DELL\\Desktop\\java\\TIEI-Java-Project\\src\\files\\word.txt.hu";
        zipFile(srcFile, dstFile);
        System.out.println("压缩文件ok~~");

    }

    static Map<Byte,String> huffmanCodes=new HashMap<>();//如a->1001
    static StringBuilder stringBuilder=new StringBuilder();//用来拼接字符串
    static TreeNode root;
    static int dataSize = 0;
    static int huffmanNodeSize = 0;

    // 将一个文件进行压缩
    public static void zipFile(String srcFile, String dstFile) {
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
            byte[] huffmanTree = linearizationOfHummanTree(root);
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
    public static byte[] huffmanZip(String FilePath){
        List<TreeNode> contentList = getList(FilePath);//将字节数组转换为Node组成的List
        root = createHuffmanTree(contentList);
        Map<Byte,String> huffmanCodes = getCodes(root);
        byte[] zipCodes = zip(FilePath,huffmanCodes);
        return zipCodes;
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

    public static byte[] numberOfDataBits(){
        // 计算所需的字节数
        ArrayList<Byte> byteList = new ArrayList<>();
        int data = dataSize;
        while (data >= 128) {
            byteList.add((byte) (data % 128));
            data /= 128;
        }
        byteList.add((byte) data);
        // 将List<Byte>转换为byte[]数组
        byte[] byteArray = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            byteArray[i] = byteList.get(i);
        }
        return byteArray;
    }

    public static List<TreeNode> getList(String filePath){
        ArrayList<TreeNode> nodes=new ArrayList<>();
        Map<Byte,Integer>counts=new HashMap<>();
        try {
            // 创建文件输入流
            FileInputStream fileInputStream = new FileInputStream(filePath);
            // 创建一个字节数组来存储读取的数据
            byte[] buffer = new byte[1024];
            int bytesRead;
            // 循环读取文件内容
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                for(byte item:buffer){
                    if(item != 0){
                        Integer count=counts.get(item);
                        if(count==null){
                            counts.put(item,1);
                        }else {
                            counts.put(item,count+1);
                        }
                    }
                }
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //遍历Map
        for(Map.Entry<Byte,Integer>entry:counts.entrySet()){
            nodes.add(new TreeNode(entry.getKey(),entry.getValue()));
        }
        huffmanNodeSize = nodes.size();
        return nodes;
    }


    //构建霍夫曼树
    public static TreeNode createHuffmanTree(List<TreeNode> nodes){
        while(nodes.size() >  1){
            Collections.sort(nodes);
            TreeNode leftNode=nodes.get(0);
            TreeNode rightNode=nodes.get(1);
            TreeNode parentNode=new TreeNode(null,leftNode.value+rightNode.value);//非叶子节点只有权重，没有data
            parentNode.left=leftNode;
            parentNode.right=rightNode;
            nodes.remove(0);
            nodes.remove(0);
            nodes.add(parentNode);
        }
        return nodes.get(0);
    }

    public static void linearizationOfHummanTree(TreeNode node, ArrayList<Byte> linearization){
        if(node!=null){
            if(node.data==null){
                linearization.add((byte) 0);
                linearizationOfHummanTree(node.left,linearization);
                linearizationOfHummanTree(node.right,linearization);
            }else{
                linearization.add((byte) 1);
                linearization.add(node.data);
            }
        }
    }


    public static byte[] linearizationOfHummanTree(TreeNode node){
        ArrayList<Byte> linearization = new ArrayList<>();
        if(node!=null){
            if(node.data==null){
                linearization.add((byte) 0);
                linearizationOfHummanTree(node.left,linearization);
                linearizationOfHummanTree(node.right,linearization);
            }else{
                linearization.add((byte) 1);
                linearization.add(node.data);
            }
        }
        byte[] c = new byte[linearization.size()];
        int index = 0;
        for (byte item:linearization) {
            c[index++] = item;
        }
        return  c;
    }

    // 将霍夫曼树转为霍夫曼编码并存入Map中
    public static void getCodes(TreeNode node,String currentCode,StringBuilder stringBuilder){
        StringBuilder stringBuilder2=new StringBuilder(stringBuilder);
        stringBuilder2.append(currentCode);
        if(node!=null){
            if(node.data==null){
                getCodes(node.left,"0",stringBuilder2);
                getCodes(node.right,"1",stringBuilder2);
            }else{
                huffmanCodes.put(node.data,stringBuilder2.toString());
            }

        }
    }

    // 将getCodes函数重载(使得引用形式更为简洁)
    public static Map<Byte,String> getCodes(TreeNode node){
        if(node==null){
            return null;
        }
        getCodes(node.left,"0",stringBuilder);
        getCodes(node,"1",stringBuilder);
        return huffmanCodes;
    }

    // 将字符串对应的byte[]数组，根据霍夫曼编码Map,生成一长串二进制霍夫曼编码，并将其转换为byte[]
    public static byte[] zip(String filePath,Map<Byte,String>huffmanCodes){
        StringBuilder stringBuilder=new StringBuilder();
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                for(byte b:buffer){
                    //获得完整的二进制霍夫曼编码
                    if(b != 0){
                        stringBuilder.append(huffmanCodes.get(b));
                    }
                }
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println("编码得到的二进制字符串"+stringBuilder.toString());
        //计算将二进制霍夫曼编码转为byte[]的位数
        int len;
        if(stringBuilder.length() % 8 == 0){
            len = stringBuilder.length() / 8;
        }else{
            len = stringBuilder.length() / 8 + 1;//可能不能被整除
        }
        dataSize = stringBuilder.length();
        //创建压缩好的byte[]数组
        int index=0;
        byte[] huffmanCodesByte=new byte[len];
        for (int i = 0; i <stringBuilder.length(); i=i+8) {
            String curString;
            if(stringBuilder.length()<i+8){
                curString = stringBuilder.substring(i);
            }else{
                curString = stringBuilder.substring(i,i+8);//substring坐标索引前包后不包
            }
            huffmanCodesByte[index]=(byte)Integer.parseInt(curString,2);//默认curString为二进制的格式，将其转化为十进制的Int,再类型转换为byte
            index++;
        }
        return huffmanCodesByte;
    }
}

