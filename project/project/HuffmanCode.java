package project;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class HuffmanCode {
    public static void main(String[] args) {

    }
    static Map<Byte,String>huffmancodes=new HashMap<>();//如a->1001
    static StringBuilder stringBuilder=new StringBuilder();//用来拼接字符串

    // 统计每个byte出现的次数
    public static List<TreeNode> getList(byte[] contentbytes){
        ArrayList<TreeNode> nodes=new ArrayList<TreeNode>();
        Map<Byte,Integer>counts=new HashMap<>();
        for(byte item:contentbytes){
            Integer count=counts.get(item);
            if(count==null){
                counts.put(item,1);
            }else {
                counts.put(item,count+1);
            }
        }
        //遍历Map
        for(Map.Entry<Byte,Integer>entry:counts.entrySet()){
            nodes.add(new TreeNode(entry.getKey(),entry.getValue()));
        }
        return nodes;

    }

    //构建霍夫曼树
    public static TreeNode createhuffmantree(List<TreeNode> nodes){
        while(nodes.size()>1){
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

    /*
     * 功能描述:将霍夫曼树转为霍夫曼编码并存入Map中
     * @param: node:传入节点
     * @param: curcode:当前节点的路径
     * @param: stringBuilder
     *  @return void
     */
    public static void getCodes(TreeNode node,String currentCode,StringBuilder stringBuilder){
        StringBuilder stringBuilder2=new StringBuilder(stringBuilder);
        stringBuilder2.append(currentCode);
        if(node!=null){
            if(node.data==null){
                getCodes(node.left,"0",stringBuilder2);
                getCodes(node.right,"1",stringBuilder2);
            }else{
                huffmancodes.put(node.data,stringBuilder2.toString());
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
        return huffmancodes;
    }

    /*
     * 功能描述:将字符串对应的byte[]数组，根据霍夫曼编码Map,生成一长串二进制霍夫曼编码，并将其转换为byte[]
     * @param: bytes  即contentbytes
     * @param: huffmanCodes 每个字符的byte对应的映射表
     * @return byte[] 拼接好的二进制霍夫曼编码转化为byte[]
     */
    public static byte[] zip(byte[] bytes,Map<Byte,String>huffmanCodes){
        //获得完整的二进制霍夫曼编码
        StringBuilder stringBuilder=new StringBuilder();
        for(byte b:bytes){
            stringBuilder.append(huffmanCodes.get(b));
        }
//        System.out.println("编码得到的二进制字符串"+stringBuilder.toString());

        //计算将二进制霍夫曼编码转为byte[]的位数
        int len=0;
        if(stringBuilder.length()%8==0){
            len=stringBuilder.length()/8;
        }else{
            len=stringBuilder.length()/8+1;//可能不能被整除
        }

        //创建压缩好的byte[]数组
        int index=0;
        byte[] huffmanCodesByte=new byte[len];
        for (int i = 0; i <stringBuilder.length(); i=i+8) {
            String curString;
            if(stringBuilder.length()<i+8){
                curString=stringBuilder.substring(i);
            }else{
                curString=stringBuilder.substring(i,i+8);//substring坐标索引前包后不包
            }
            huffmanCodesByte[index]=(byte)Integer.parseInt(curString,2);//默认curString为二进制的格式，将其转化为十进制的Int,再类型转换为byte
            index++;
        }
        return huffmanCodesByte;
    }
    /*
     * 功能描述:将一个byte转成一个二进制字符串
     * @param: flag 标志是否需要补高位，如果是true，要补，false不补。如果是最后一个字节，不用补高位
     * @param: b
     *  @return java.lang.String 是b对应的二进制字符串（注意按补码返回）
     */
    public static String byteToBitString(boolean flag,byte b){
        int temp=b;//将b转为int
        if (flag) {
            temp |=256;
        }
        String str=Integer.toBinaryString(temp);//Integer.toBinaryString返回的是temp的二进制补码！！
        if(flag){
            return str.substring(str.length()-8);
        }else{
            return str;
        }
    }
}

