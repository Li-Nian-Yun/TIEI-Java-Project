package project;
import java.io.*;
import java.util.*;

public class HuffMan {
    private static final int READ_BUFFER_SIZE = 1024;
    private static final Map<Byte,String> huffmanCodes = new HashMap<>();
    private static final StringBuilder stringBuilder=new StringBuilder();//used to concatenate strings
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
        byte[] linearizationHuffmanTree = new byte[linearArray.size()];
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

    /*
        generate a long string of binary Huffman codes based on the Huffman encoding map
        using the byte [] array corresponding to the string, and convert it to byte[]
    */
    public static byte[] compress(File filePath,Map<Byte,String>huffmanCodes){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            byte[] buffer = new byte[READ_BUFFER_SIZE];
            while (fileInputStream.read(buffer)!= -1) {
                for(byte b:buffer){
                    //obtain complete binary Huffman encoding
                    if(b != 0){
                        stringBuilder.append(huffmanCodes.get(b));
                    }
                }
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //calculate the number of bits to convert binary Huffman encoding to byte []
        int len;
        if(stringBuilder.length() % 8 == 0){
            len = stringBuilder.length() / 8;
        }else{
            len = stringBuilder.length() / 8 + 1;
        }
        dataSize = stringBuilder.length();
        //create a compressed byte [] array
        int index = 0;
        byte[] huffmanCodesByte = new byte[len];
        for (int i = 0; i < stringBuilder.length(); i=i+8) {
            String curString;
            if(stringBuilder.length() < i + 8){
                curString = stringBuilder.substring(i);
            }else{
                curString = stringBuilder.substring(i,i + 8);
            }
            huffmanCodesByte[index] = (byte)Integer.parseInt(curString,2);
            index++;
        }
        return huffmanCodesByte;
    }

}

