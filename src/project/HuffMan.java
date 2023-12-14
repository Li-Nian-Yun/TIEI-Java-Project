package project;
import java.io.*;
import java.util.*;

public class HuffMan {
    private static int READ_BUFFER_SIZE = 1024;
    private static Map<Byte,String> huffmanCodes = new HashMap<>();
    private static StringBuilder stringBuilder=new StringBuilder();//used to concatenate strings
    public static TreeNode root;
    public static int dataSize = 0;
    public static int huffmanNodeSize = 0;

    public static List<TreeNode> getCounts(File filePath){
        ArrayList<TreeNode> nodes=new ArrayList<>();
        Map<Byte,Integer> counts=new HashMap<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            byte[] buffer = new byte[READ_BUFFER_SIZE];
            while ( fileInputStream.read(buffer) != -1) {
                for(byte item:buffer){
                    if(item != 0){
                        counts.merge(item, 1, Integer::sum);
                    }
                }
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Map.Entry<Byte,Integer>entry:counts.entrySet()){
            nodes.add(new TreeNode(entry.getKey(),entry.getValue()));
        }
        huffmanNodeSize = nodes.size();
        return nodes;
    }

    public static TreeNode createHuffmanTree(List<TreeNode> nodes){
        while(nodes.size() >  1){
            Collections.sort(nodes);
            TreeNode leftNode=nodes.get(0);
            TreeNode rightNode=nodes.get(1);
            TreeNode parentNode=new TreeNode(null,leftNode.weight+rightNode.weight);//非叶子节点只有权重，没有data
            parentNode.left=leftNode;
            parentNode.right=rightNode;
            nodes.remove(0);
            nodes.remove(0);
            nodes.add(parentNode);
        }
        return nodes.get(0);
    }

    public static void linearization(TreeNode node, ArrayList<Byte> linearArray){
        if(node!=null){
            if(node.data==null){
                linearArray.add((byte) 0);
                linearization(node.left,linearArray);
                linearization(node.right,linearArray);
            }else{
                linearArray.add((byte) 1);
                linearArray.add(node.data);
            }
        }
    }


    public static byte[] linearization(TreeNode node){
        ArrayList<Byte> linearArray = new ArrayList<>();
        if(node!=null){
            if(node.data==null){
                linearArray.add((byte) 0);
                linearization(node.left,linearArray);
                linearization(node.right,linearArray);
            }else{
                linearArray.add((byte) 1);
                linearArray.add(node.data);
            }
        }
        byte[] linearizationHuffmanTree= new byte[linearArray.size()];
        int index = 0;
        for (byte item:linearArray) {
            linearizationHuffmanTree[index++] = item;
        }
        return  linearizationHuffmanTree;
    }

    // Convert the Huffman tree to Huffman encoding
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

    // Override the getCodes function (making the reference form more concise)
    public static Map<Byte,String> getCodes(TreeNode node){
        if(node==null){
            return null;
        }
        getCodes(node.left,"0",stringBuilder);
        getCodes(node,"1",stringBuilder);
        return huffmanCodes;
    }

    // 将字符串对应的byte[]数组，根据霍夫曼编码Map,生成一长串二进制霍夫曼编码，并将其转换为byte[]
    public static byte[] compress(File filePath,Map<Byte,String>huffmanCodes){
        StringBuilder stringBuilder=new StringBuilder();
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            byte[] buffer = new byte[READ_BUFFER_SIZE];
            while (fileInputStream.read(buffer)!= -1) {
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

