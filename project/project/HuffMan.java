package project;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class HuffMan {
    public static void main(String[] args) {
//        String content="i like like like java do you like a java";
//        byte[] contentbytes=content.getBytes();//将字符串转为字节数组
//        byte[] zipCodes=huffmanZip(contentbytes);//进行霍夫曼编码
//        byte[] finalBytes=decode(huffmanCodes,zipCodes);//解码
//        System.out.println("最终的解码得到字符为"+(new String(finalBytes)));
        //测试压缩文件
//        String srcFile = "C://Users//DELL//Desktop//java project//files//words.txt";
//        String dstFile = "C://Users//DELL//Desktop//java project//files//words.zip";
//        zipFile(srcFile, dstFile);
//        System.out.println("压缩文件ok~~");
        //测试解压文件
        String srcFile2 = "C://Users//DELL//Desktop//java project//files//words.zip";
        String dstFile2 = "C://Users//DELL//Desktop//java project//files//words2.txt";
        unZipFile(srcFile2,dstFile2);
        System.out.println("解压文件ok~~");
    }
    static Map<Byte,String>huffmanCodes=new HashMap<>();//如a->1001
    /*
     * 功能描述:将霍夫曼编码过程封装起来，便于调用
     * @param: contentbytes 待传输内容对应的字节数组
     *  @return byte[] 完整霍夫曼二进制长字符串转化为用于传输的byte形式
     */
    public static byte[] huffmanZip(byte[] contentbytes){
        List<TreeNode> contentlist=getList(contentbytes);//将字节数组转换为Node组成的List
        TreeNode root=createhuffmantree(contentlist);
        root.preOreder();
        huffmanCodes=getCodes(root);
        System.out.println("各个字符对应的霍夫曼编码："+huffmanCodes);
        byte[] zipCodes=zip(contentbytes,huffmanCodes);
        System.out.println("完整霍夫曼字符串转化为用于传输的byte形式：");
        System.out.println(Arrays.toString(zipCodes));
        return zipCodes;
    }

    /**
     *编写方法，将一个文件进行压缩
     * @param srcFile 你传入的希望压缩的文件的全路径
     * @param dstFile 我们压缩后将压缩文件放到哪个目录
     */
    public static void zipFile(String srcFile, String dstFile) {
        //创建文件输入流
        FileInputStream is=null;
        //创建文件输出流
        OutputStream os=null;
        ObjectOutputStream oos=null;
        try {
            is=new FileInputStream(srcFile);
            byte[] b=new byte[is.available()];//创建一个与文件大小相同的byte[]数组
            is.read(b);//读取文件内容，拷贝到b中
            byte[] huffmanBytes=huffmanZip(b);//得到完整霍夫曼二进制长字符串转化为用于传输的byte形式
            os=new FileOutputStream(dstFile);//创建文件的输出流, 存放压缩文件
            oos=new ObjectOutputStream(os);//创建一个和文件输出流关联的ObjectOutputStream
            oos.writeObject(huffmanBytes);//以对象流的形式写入
            oos.writeObject(huffmanCodes);//这里一定要注意，要把霍夫曼编码映射表传进去，否则无法解压

        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            try {
                oos.close();//关流的顺序应该和建流的顺序相反
                os.close();
                is.close();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
    /**
     *编写一个方法，完成对压缩文件的解压
     * @param zipFile 准备解压的文件
     * @param dstFile 将文件解压到哪个路径
     */
    public static void unZipFile(String zipFile, String dstFile) {
        InputStream is=null;
        ObjectInputStream ois=null;
        OutputStream os=null;
        try{
            is=new FileInputStream(zipFile);
            ois=new ObjectInputStream(is);
            byte[] huffmanBytes=(byte[]) ois.readObject();
            Map<Byte,String>huffmanMap=(Map<Byte,String>)ois.readObject();
            byte[] finalBytes=decode(huffmanMap,huffmanBytes);//解码
            os=new FileOutputStream(dstFile);
            os.write(finalBytes);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            try {
                os.close();
                ois.close();
                is.close();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static List<TreeNode> getList(byte[] contentbytes){
        ArrayList<TreeNode> nodes=new ArrayList<TreeNode>();
        //遍历bytes，统计每个byte出现的次数
        Map<Byte,Integer>counts=new HashMap<>();
        for(byte item:contentbytes){
            Integer count=counts.get(item);//寻找Map中item对应的value
            if(count==null){//说明这个count还不存在
                counts.put(item,1);
            }else {
                counts.put(item,count+1);
            }
        }
        //把每对键值转化为treeNode并存入nodes集合中
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

    static Map<Byte,String>huffmancodes=new HashMap<>();//如a->1001
    static StringBuilder stringBuilder=new StringBuilder();//用来拼接字符串

    /*
     * 功能描述:将霍夫曼树转为霍夫曼编码并存入Map中
     * @param: node:传入节点
     * @param: curcode:当前节点的路径
     * @param: stringBuilder
     *  @return void
     */
    public static void getCodes(TreeNode node,String curcode,StringBuilder stringBuilder){
        StringBuilder stringBuilder2=new StringBuilder(stringBuilder);
        stringBuilder2.append(curcode);
        if(node!=null){//如果node为空则不处理
            //判断是否为叶子节点
            if(node.data==null){//说明为非叶子节点
                getCodes(node.left,"0",stringBuilder2);
                getCodes(node.right,"1",stringBuilder2);
            }else{//是叶子节点
                huffmancodes.put(node.data,stringBuilder2.toString());
            }

        }
    }
    /*
     * 功能描述:将getcdes函数重载(使得引用形式更为简洁)
     * @param: node 传入根节点
     * @return Map<Byte,String> 编码Map
     */
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
    /*
     * 功能描述:
     * @param: huffmanCodes  霍夫曼编码映射表
     * @param: huffmanCodesByte 霍夫曼编码得到的字节数组
     *  @return byte[] 原来的字符串对应的数组
     */
    public static byte[] decode(Map<Byte,String>huffmanCodes,byte[] huffmanCodesByte){
        //1、先得到huffmanCodesByte对应的二进制字符串
        StringBuilder stringBuilder1=new StringBuilder();
        for (int i = 0; i <huffmanCodesByte.length; i++) {
            byte b=huffmanCodesByte[i];
            boolean flag=(i==huffmanCodesByte.length-1);
            stringBuilder1.append(byteToBitString(!flag,b));
        }
//        System.out.println("解码后得到的二进制字符串"+stringBuilder1.toString());
        System.out.println("解码后得到的二进制字符串");
        //把字符串按照霍夫曼映射表进行解码
        //1、将霍夫曼编码映射表调换，因为需要进行反向查询
        Map<String,Byte>map=new HashMap<String,Byte>();//key是二进制字符串，value是对应解码后字符的byte
        for(Map.Entry<Byte,String>entry:huffmanCodes.entrySet()){
            map.put(entry.getValue(),entry.getKey());
        }
        //创建集合存放byte
        List<Byte> list=new ArrayList<>();
        for (int i = 0; i <stringBuilder1.length() ; ) {
            int count=0;//内部计数器，因为每次扫描字符对应的霍夫曼位数不确定，每次检索成功，计数器清零
            boolean flag=true;
            while(flag){
                String search=stringBuilder1.substring(i,i+count);
                Byte b=map.get(search);
                if(b==null){
                    count++;
                }else{
                    flag=false;
                    list.add(b);
                }
            }
            i=i+count;
        }
        //把list中的数据存入到byte[]中
        byte[] listToByte=new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            listToByte[i]=list.get(i);
        }
        return listToByte;
    }
}

