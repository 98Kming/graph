import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

/**
 * 2022-09-09 11:40
 */
public class Demo {
    public static void main(String[] args) {
        MutableValueGraph<String, Integer> graph = ValueGraphBuilder.undirected().build();
        graph.putEdgeValue("A", "B", 4);
        graph.putEdgeValue("A", "C", 5);
        graph.putEdgeValue("B", "D", 9);
        graph.putEdgeValue("B", "E", 2);
        graph.putEdgeValue("C", "D", 1);
        graph.putEdgeValue("C", "F", 9);
        graph.putEdgeValue("D", "F", 3);
        graph.putEdgeValue("D", "E", 1);
        graph.putEdgeValue("E", "G", 2);
        graph.putEdgeValue("F", "G", 7);

        GraphPlus<String, Integer> graphPlus = new GraphPlus<>(graph, "A");
        System.out.println("A到G最短路径：");
        GraphPlus.print(graphPlus.dijkstra("G"));
        System.out.println("A到其他点最短路径：");
        graphPlus.printAll();
    }
}
