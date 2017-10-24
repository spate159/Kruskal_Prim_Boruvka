import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Kruskals {

	Graph mstGraph;
	ArrayList<Edge> minSpanningTreeEdges = new ArrayList<>();

	public int findMst(Graph inputGraph, boolean print) {

		ArrayList<Edge>	sortedEdges = sortEdges((ArrayList<Edge>) inputGraph.getGraphEdges());

		// sortedEdges.forEach(System.out::println);

		// check for cycle and create MST
		UnionFind uf = new UnionFind(
				new ArrayList<Vertex>(inputGraph.getGraphVertices()));
		int weight = 0;
		for (Edge edge : sortedEdges) {
			if (uf.union(edge.getVertex1(), edge.getVertex2())) {
				minSpanningTreeEdges.add(edge);
				weight = weight + edge.getWeight();
			} else {
				// cycle i.e. same parent
			}
		}
		// minSpanningTreeEdges.forEach(System.out::println);

		if (print) {
			mstGraph = new Graph(minSpanningTreeEdges, Graph.EDGES);
			System.out.println("\nKruskal's MST\n");
			System.out.println(mstGraph);
			System.out.println("Total MST weight =" + weight);
		}
		return weight;

		// System.out.println(mstGraph.getGraphEdges().size());

	}

	private ArrayList<Edge> sortEdges(ArrayList<Edge> list) {

		return new ArrayList<Edge>((List<Edge>) Arrays
				.asList(QuickSort.sort(list.toArray(new Edge[list.size()]))));

	}

}
