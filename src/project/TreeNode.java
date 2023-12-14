package project;


//构建节点类
class TreeNode implements Comparable<TreeNode>{
    Byte data;//存放的数据
    int weight;//节点权值
    TreeNode left;//左子节点
    TreeNode right;//右子节点

    public TreeNode(Byte data, int value) {
        this.data = data;
        this.weight = value;
    }

    @Override
    public int compareTo(TreeNode o) {
        //表示从小到大排序
        return this.weight-o.weight;
    }
}
