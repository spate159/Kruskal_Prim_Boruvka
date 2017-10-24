import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class Graph {

	private HashMap<Integer, Vertex> vertices;
	private HashMap<Integer, Edge> edges;
	public final static int EDGES = 1;
	public final static int VERTICES = 2;

	public Graph(ArrayList<Vertex> vertices) {
		this.vertices = new HashMap<Integer, Vertex>();
		this.edges = new HashMap<Integer, Edge>();
		for (Vertex vertex : vertices) {
			this.vertices.put(vertex.hashCode(), vertex);
		}
	}

	// creates a new copy of variables duplicates used by MST.. can be confusing
	public Graph(List<Edge> edges, int intializeType) {
		this.vertices = new HashMap<Integer, Vertex>();
		this.edges = new HashMap<Integer, Edge>();
		for (Edge edge : edges) {

			Vertex v1 = new Vertex(edge.getVertex1().getName());
			Vertex v2 = new Vertex(edge.getVertex2().getName());

			if (vertices.get(v1.hashCode()) == null) {
				v1.addEdge(edge);
				this.vertices.put(v1.hashCode(), v1);
			} else {
				vertices.get(v1.hashCode()).addEdge(edge);
			}
			if (vertices.get(v2.hashCode()) == null) {
				v2.addEdge(edge);
				this.vertices.put(v2.hashCode(), v2);
			} else {
				vertices.get(v2.hashCode()).addEdge(edge);
			}
			Edge newEdge = new Edge(vertices.get(v1.hashCode()),
					vertices.get(v2.hashCode()), edge.getWeight());
			this.edges.put(newEdge.hashCode(), newEdge);
		}
	}
	public boolean addEdge(Vertex vt1, Vertex vt2, int weight) {

		Vertex v1 = vertices.get(vt1.hashCode());
		Vertex v2 = vertices.get(vt2.hashCode());
		if (v1 == null) {
			System.err.println("vertex " + vt1.toString()
					+ " not created hence not creating edge");
			return false;
		}
		if (v2 == null) {
			System.err.println("vertex " + vt2.toString()
					+ " not created hence not creating edge");
			return false;
		}

		Edge edge = new Edge(v1, v2, weight);
		Edge sameEdgeExist = this.edges.get(edge.hashCode());
		// if (edges.containsKey(edge.hashCode())) {
		if (sameEdgeExist != null) {
			if (sameEdgeExist.getWeight() != edge.getWeight()) {
			System.err.println("hashcodes " + edge.hashCode()
					+ " same edge hash code" + sameEdgeExist.hashCode());

			System.err.println(edge + "conflict same edge " + sameEdgeExist);
				System.err.println(
						"duplicate edge found with same vertices and different weight. Not adding edge "
								+ edge.toString());
			}
			return false;
		}
		edges.put(edge.hashCode(), edge);
		v1.addEdge(edge);
		v2.addEdge(edge);
		return true;
	}
	/*
	 * // adds the edge to the graph and also the the vertices public boolean
	 * addEdge(Edge addEdge) { if
	 * (!vertices.containsKey((addEdge.getVertex1()).hashCode())) { return
	 * false; } if (!vertices.containsKey((addEdge.getVertex2()).hashCode())) {
	 * return false; } Edge edge = addEdge; if
	 * (edges.containsKey(edge.hashCode())) { return false; }
	 * edges.put(edge.hashCode(), edge); edge.getVertex1().addEdge(edge);
	 * edge.getVertex2().addEdge(edge); return true; }
	 */

	public void removeEdge(Edge edge) {
		this.edges.remove(edge.hashCode());
		edge.getVertex1().removeEdge(edge);
		edge.getVertex2().removeEdge(edge);
	}

	public boolean addVertex(Vertex vertex) {

		if (this.vertices.get(vertex.hashCode()) != null)
			return false;
		this.vertices.put(vertex.hashCode(), vertex);
		return true;
	}

	// removes vertices along with edges.
	public void removeVertex(Vertex vertex) {

		Vector<Edge> edges = vertex.getEdges();
		while (edges.size() > 0) {
			Edge edge = edges.get(0);
			removeEdge(edge);
		}

		vertices.remove(vertex.hashCode());

	}

	public List<Vertex> getGraphVertices() {
		List<Vertex> list = new ArrayList<Vertex>(vertices.values());
		return list;
	}

	public List<Edge> getGraphEdges() {
		List<Edge> list = new ArrayList<Edge>(edges.values());
		return list;
	}

	@Override
	public String toString() {
		boolean isDisjoint = false;
		String graphString = "";
		for (Vertex vertex : vertices.values()) {
			graphString += vertex.toString() + " " + "(";
			for (Edge edge : vertex.getEdges()) {
				graphString += "(" + edge.toStringAdjListForm(vertex) + "),";
			}
			graphString = graphString.substring(0, graphString.length() - 1)
					+ ")\n";
			if (vertex.noOfEdges() == 0) {
				isDisjoint = true;
			}

		}
		if (isDisjoint) {
			System.err.println(
					"*This is a disjoint Graph, MST algorithm does not allow disjoint graphs.");
		}
		return graphString;
	}

	public HashMap<Integer, Vertex> getVertices() {
		return vertices;
	}

	public HashMap<Integer, Edge> getEdges() {
		return edges;
	}

	// Multi edges not supported
	public Edge getEdge(Vertex v1, Vertex v2) {
		
		if (v1 == null || v2 == null) {
			// throw new NullPointerException("vertex v1 or v2 is null");
			return null;
		}
		return edges.get((new Edge(v1, v2, 0)).hashCode());
	}
}
