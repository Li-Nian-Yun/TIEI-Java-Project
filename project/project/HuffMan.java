package project;

import java.io.*;
import java.util.*;

public class HuffMan {
    public static void main(String[] args) {
        //测试压缩文件
        String srcFile = "C://Users//DELL//Desktop//java project//project//files//words.txt";
        String dstFile = "C://Users//DELL//Desktop//java project//project//files//w.txt";
        zipFile(srcFile, dstFile);
        System.out.println("压缩文件ok");
    }

    static Map<Byte,String> huffmanCodes =new HashMap<>();//如a->1001
    static StringBuilder stringBuilder=new StringBuilder();//用来拼接字符串
    static int dataSize = 0;

    // 将一个文件进行压缩
    public static void zipFile(String srcFile, String dstFile) {
        //创建文件输入流
        FileInputStream is=null;
        //创建文件输出流
        OutputStream os=null;
        try {
            is=new FileInputStream(srcFile);
            byte[] b=new byte[is.available()];//创建一个与文件大小相同的byte[]数组
            is.read(b);//读取文件内容，拷贝到b中
            byte[] magicNumber = {9,1,2,3,4,5,6,7,8,9};
//            byte[] huffmanTree;
            byte[] numberOfData;//把int转化成byte
            byte[] compressedFile = huffmanZip(b);//得到完整霍夫曼二进制长字符串转化为用于传输的byte形式

            os=new FileOutputStream(dstFile);//创建文件的输出流, 存放压缩文件
            os.write(magicNumber);
            //存放HuffmanTree
            Set keySet = huffmanCodes.keySet(); //获取到所有值
            for(Object key : keySet ){
                String value = huffmanCodes.get(key);
                for(int i = 0; i < value.length(); i++){//好像搞错了，应该转化成byte？
                    os.write((byte) (value.charAt(i) - '0'));
                }
                os.write((byte)key);
                System.out.println(value);
            }
            numberOfData = computeSizeOfData();
            os.write(numberOfData.length);
            os.write(numberOfData);
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
    public static byte[] huffmanZip(byte[] contentbytes){
        List<TreeNode> contentlist=getList(contentbytes);//将字节数组转换为Node组成的List
        TreeNode root=createhuffmantree(contentlist);
        root.preOreder();
        Map<Byte,String> huffmanCodes = getCodes(root);
        byte[] zipCodes = zip(contentbytes,huffmanCodes);
        return zipCodes;
    }

    public static byte[] computeSizeOfData(){
        // 计算所需的字节数
        List<Byte> byteList = new ArrayList<>();
        while (dataSize >= 128) {
            byteList.add((byte) (dataSize % 128));
            dataSize /= 128;
        }
        byteList.add((byte) dataSize);
        // 将List<Byte>转换为byte[]数组
        byte[] byteArray = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            byteArray[i] = byteList.get(i);
        }
        return byteArray;
    }

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

    // 将霍夫曼树转为霍夫曼编码并存入Map中
    public static void getCodes(TreeNode node,String currentCode,StringBuilder stringBuilder){
        StringBuilder stringBuilder2= new StringBuilder(stringBuilder);
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
    public static byte[] zip(byte[] bytes,Map<Byte,String>huffmanCodes){
        //获得完整的二进制霍夫曼编码
        StringBuilder stringBuilder = new StringBuilder();
        for(byte b:bytes){
            stringBuilder.append(huffmanCodes.get(b));
        }
//        System.out.println("编码得到的二进制字符串"+stringBuilder.toString());

        //计算将二进制霍夫曼编码转为byte[]的位数
        int len = 0;
        if(stringBuilder.length() % 8 == 0){
            len = stringBuilder.length() / 8;
        }else{
            len = stringBuilder.length() / 8 + 1;//可能不能被整除
        }
        dataSize = len;
        System.out.println(stringBuilder.length());
        System.out.println(dataSize);
        //创建压缩好的byte[]数组
        int index=0;
        byte[] huffmanCodesByte = new byte[len];
        for (int i = 0; i < stringBuilder.length(); i = i + 8) {
            String curString;
            if(stringBuilder.length() < i + 8){
                curString=stringBuilder.substring(i);
            }else{
                curString=stringBuilder.substring(i, i + 8);//substring坐标索引前包后不包
            }
            huffmanCodesByte[index]=(byte)Integer.parseInt(curString,2);//默认curString为二进制的格式，将其转化为十进制的Int,再类型转换为byte
            index++;
        }
        return huffmanCodesByte;
    }
}

