

/**
 * 图节点（顶点）
 * 2022-09-09 8:32
 */

public class GraphNode<N, V> {
    // 当前点
    private N node;
    // 前置点
    private N preNode;
    // 成本
    private V cost;
    // 成本是否确认
    private boolean visited = false;

    public N getNode() {
        return node;
    }

    public void setNode(N node) {
        this.node = node;
    }

    public N getPreNode() {
        return preNode;
    }

    public void setPreNode(N preNode) {
        this.preNode = preNode;
    }

    public V getCost() {
        return cost;
    }

    public void setCost(V cost) {
        this.cost = cost;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
