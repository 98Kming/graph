import com.google.common.graph.ValueGraph;

import java.math.BigDecimal;
import java.util.*;

/**
 * 图相关操作
 * 目前实现了dijkstra算法
 * ·起始点到其他点的最短路径
 * ·起始点到指定点的最短路径
 * 2022-09-09
 */

public class GraphPlus<N, V> {
    private final ValueGraph<N, V> graph;
    private final N startNode;
    private final Map<N, GraphNode<N, V>> nodeMap;

    // 默认的成本比较方法
    private Comparator<V> defaultComparator = (v1, v2) -> {
        BigDecimal a = new BigDecimal(v1.toString());
        BigDecimal b = new BigDecimal(v2.toString());
        return a.compareTo(b);
    };

    // 默认的成本计算方法
    private Sum<V> defaultSum = (v1, v2) -> {
        BigDecimal a = new BigDecimal(v1.toString());
        BigDecimal b = new BigDecimal(v2.toString());
        return (V)a.add(b);
    };

    public GraphPlus(ValueGraph<N, V> graph, N startNode) {
        this.graph = graph;
        this.startNode = startNode;
        nodeMap = init();
    }

    /*
     * 初始化图节点map,起始点的相邻点初始化前置点和成本
     * @return java.util.Map<N,com.example.demo1.GraphNode<N,V>>
    **/
    private Map<N, GraphNode<N, V>> init(){
        Set<N> nodeSet = graph.nodes();
        Map<N, GraphNode<N, V>> nodeMap = new HashMap<>(nodeSet.size());
        for (N n : nodeSet){
            V value = graph.edgeValueOrDefault(startNode, n, null);
            GraphNode<N, V> graphNode = new GraphNode<>();
            graphNode.setNode(n);
            if (value != null){
                graphNode.setPreNode(startNode);
                graphNode.setCost(value);
            }
            nodeMap.put(n, graphNode);
        }
        nodeMap.get(startNode).setVisited(true);
        return nodeMap;
    }

    /**
     * 从候选节点池中取成本最小的点
     * @param candidateSet 候选节点池
     * @return com.example.demo1.GraphNode<N,V>
    **/
    public N getMinNode(Set<N> candidateSet){
        return candidateSet.stream()
                .min((n1, n2) -> defaultComparator.compare(nodeMap.get(n1).getCost(), nodeMap.get(n2).getCost())).orElse(null);
    }

    public List<GraphNode<N, V>> get(N endNode){
        Deque<GraphNode<N, V>> deque = new ArrayDeque<>(nodeMap.size());
        deque.addFirst(nodeMap.get(endNode));
        N node = endNode;
        while (nodeMap.get(node).getPreNode()!=null){
            deque.addFirst(nodeMap.get(node = nodeMap.get(node).getPreNode()));
        }
        return new ArrayList<>(deque);
    }

    public List<GraphNode<N, V>> dijkstra(){
        return dijkstra(null);
    }

    public List<GraphNode<N, V>> dijkstra(N endNode){
        GraphNode<N, V> endGraphNode = nodeMap.get(endNode);
        if (endGraphNode != null && endGraphNode.isVisited()){
            return get(endNode);
        }
        // 初始化候选节点池为起始点的相邻点
        Set<N> candidateSet = new HashSet<>(graph.successors(startNode));
        // 取候选节点池中成本最小的点
        N minNode = getMinNode(candidateSet);
        // minNode成本已确定，候选节点池移除minNode
        GraphNode<N, V> minGraphNode = nodeMap.get(minNode);
        minGraphNode.setVisited(true);
        if (minNode.equals(endNode)){
            return get(endNode);
        }
        // candidateSet.remove(minNode);
        while (!candidateSet.isEmpty()){
            // 成本最小的点的相邻点集合
            Set<N> nodeSet = graph.successors(minNode);
            for (N n : nodeSet){
                GraphNode<N, V> graphNode = nodeMap.get(n);
                // 如果n点成本已确定则忽略
                if (graphNode.isVisited()){
                    continue;
                }
                // 起始点到minNode成本 + minNode到n的成本
                V value = defaultSum.add(minGraphNode.getCost(), graph.edgeValue(minNode, n).get());
                // n点成本为null或n点成本小于value则进行更新
                if (graphNode.getCost() == null || defaultComparator.compare(graphNode.getCost(), value) > 0){
                    graphNode.setPreNode(minNode);
                    graphNode.setCost(value);
                }
                // 将n加入候选节点池
                candidateSet.add(n);
            }
            candidateSet.remove(minNode);
            // 重新取候选节点池中成本最小的点
            minNode = getMinNode(candidateSet);
            if (minNode == null) {
                continue;
            }
            // minNode成本已确定，候选节点池移除minNode
            minGraphNode = nodeMap.get(minNode);
            minGraphNode.setVisited(true);
            if (minNode.equals(endNode)){
                return get(endNode);
            }
            // candidateSet.remove(minNode);
        }
        return new ArrayList<>(nodeMap.values());
    }

    public void printAll(){
        dijkstra();
        nodeMap.forEach((k, v) -> {
            StringJoiner sj = new StringJoiner(" -> ");
            Deque<N> queue = new ArrayDeque<>(nodeMap.size());
            queue.addFirst(k);
            N node = k;
            while (nodeMap.get(node).getPreNode()!=null){
                queue.addFirst((node = nodeMap.get(node).getPreNode()));
            }
            queue.forEach(n -> sj.add(n.toString()));
            System.out.println(sj + " 成本:"+nodeMap.get(k).getCost());
        });
    }

    public static <N, V>void print(List<GraphNode<N, V>> list){
        StringJoiner sj = new StringJoiner(" -> ");
        list.forEach(n -> sj.add(n.getNode().toString()));
        System.out.println(sj + " 成本:" + list.get(list.size()-1).getCost());
    }

    @FunctionalInterface
    public interface Sum<V>{
        V add(V v1, V v2);
    }
}
