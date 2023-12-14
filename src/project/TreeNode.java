package project;


import com.sun.source.tree.Tree;

//构建节点类
class TreeNode implements Comparable<TreeNode>{
    Byte data;//存放的数据
    int value;//节点权值
    TreeNode left;//左子节点
    TreeNode right;//右子节点

    public TreeNode(Byte data, int value) {
        this.data = data;
        this.value = value;
    }

    public TreeNode(Byte data, int value, TreeNode left, TreeNode right) {
        this.data = data;
        this.value = value;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "treeNode{" +
                "data=" + data +
                ", value=" + value +
                '}';
    }

    @Override
    public int compareTo(TreeNode o) {
        //表示从小到大排序
        return this.value-o.value;
    }

    //前序遍历
    public void preOreder(){
        System.out.println(this);
        if(this.left!=null){
            this.left.preOreder();
        }
        if(this.right!=null){
            this.right.preOreder();
        }

    }
}
