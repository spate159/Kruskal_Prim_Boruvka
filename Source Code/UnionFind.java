import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Vector;
// not very memory efficient as it creates duplicate entries
public class UnionFind {

	private UFGraph disjointGraph;

	public UnionFind(ArrayList<Vertex> vertices) {
		this.disjointGraph = new UFGraph(vertices);
	}
	public boolean union(Vertex v1, Vertex v2) {
		// same parent so no union
		if (find(v1).equals(find(v2))) {
			// System.err.println("same parent union, cycle, not merging");
			return false;
		}

		int maxHeightV1 = find(disjointGraph.vertices.get(v1.hashCode()))
				.getMaxHeight();
		int maxHeightV2 = find(disjointGraph.vertices.get(v2.hashCode()))
				.getMaxHeight();
		UFVertex parentRoot;
		UFVertex childRoot;

		if (maxHeightV1 >= maxHeightV2) {
			parentRoot = find(disjointGraph.vertices.get(v1.hashCode()));
			childRoot = find(disjointGraph.vertices.get(v2.hashCode()));
		} else {
			parentRoot = find(disjointGraph.vertices.get(v2.hashCode()));
			childRoot = find(disjointGraph.vertices.get(v1.hashCode()));
		}

		if (maxHeightV1 == maxHeightV2) {
			parentRoot.increaseMaxHeight();
			// System.out.println("max height " + parentRoot.getMaxHeight());
		}
		return disjointGraph.addEdge(parentRoot, childRoot, 1);
	}

	// Returns the name of the vertex make sure that names of two vertex are not
	// same.
	public String find(Vertex v1) {
		return find(disjointGraph.vertices.get(v1.hashCode())).toString();

	}

	private UFVertex find(UFVertex v1) {

		UFVertex vertex = disjointGraph.vertices.get(v1.hashCode());

		UFEdge parentEdge = vertex.getParentEdge();
		UFVertex rootVertex = null;
		boolean compress = false;
		while (parentEdge != null) {
			rootVertex = parentEdge.getParentVertex();
			parentEdge = parentEdge.getParentVertex().getParentEdge();
			compress = true;
		}
		if (compress) {
			v1.parentEdge.parentVertex = rootVertex;
		}
		return (rootVertex == null) ? v1 : rootVertex;
	}

	// private int getDepth(UFVertex v) {
	// int i = 0;
	// UFEdge parentEdge = v.getParentEdge();
	// while (parentEdge != null) {
	// i++;
	// parentEdge = parentEdge.getParentVertex().getParentEdge();
	// }
	// return i;
	// }

	private static class UFVertex {

		private String name;
		private Vector<UFEdge> childEdges;
		private UFEdge parentEdge = null;
		private int maxHeight; // only mainted for the roots....

		public void increaseMaxHeight() {
			maxHeight++;
		}
		public int getMaxHeight() {
			return maxHeight;
		}
		public void setParentEdge(UFEdge parentEdge) {
			this.parentEdge = parentEdge;
		}

		/**
		 * returns null if no parent else return the parent of the vertex
		 */
		public UFEdge getParentEdge() {
			return this.parentEdge;
		}

		public String getName() {
			return name;
		}

		public Vector<UFEdge> getChildEdges() {
			return childEdges;
		}

		// Constructor
		public UFVertex(String name) {
			this.name = name;
			this.childEdges = new Vector<UFEdge>();
			// this.setParentEdge(parentEdge);
			maxHeight = 0;
		}

		// efficient way
		public void setVectorSize(int size) {
			if (size > childEdges.size())
				childEdges.setSize(size);
		}

		public void addChildEdge(UFEdge edge) {
			if (this.childEdges.contains(edge)) {
				return;
			}
			this.childEdges.add(edge);
		}

		@Override
		public String toString() {
			return name;
		}

		@Override
		public int hashCode() {
			return name.hashCode();

		}

		@Override
		public boolean equals(Object obj) {

			if (obj == this)
				return true;
			if (!(obj instanceof UFVertex)) {
				return false;
			}
			UFVertex vertex = (UFVertex) obj;
			return Objects.equals(name, vertex.name);

		}

		public int noOfChildEdges() {
			return this.childEdges.size();
		}

		public UFEdge getChildEdge(int index) {
			return this.childEdges.get(index);
		}
		
		
	}

	private static class UFEdge {
		private UFVertex parentVertex;
		private UFVertex childVertex;
		private int weight;

		public UFEdge(UFVertex parentVertex, UFVertex childVertex, int weight) {

			this.parentVertex = parentVertex;
			this.childVertex = childVertex;
			this.weight = weight;
		}

		public UFVertex getParentVertex() {
			return parentVertex;
		}

		public UFVertex getChildVertex() {
			return childVertex;
		}

		public int getWeight() {
			return weight;
		}

		@Override
		public int hashCode() {
			// int hash = 17;
			// int hash2 = 23;
			// hash = hash * 31 + childVertex.hashCode();
			// hash2 = hash2 * 31 + parentVertex.hashCode();
			// hash = hash + hash2;
			// return hash;

			int v1;
			int v2;
			if (this.childVertex.hashCode() < this.parentVertex.hashCode()) {
				v1 = this.childVertex.hashCode();
				v2 = this.parentVertex.hashCode();
			} else {
				v2 = this.childVertex.hashCode();
				v1 = this.parentVertex.hashCode();
			}
			return (((v1 + v2) * (v1 + v2 + 1)) / 2) + v2;

		}

		public UFVertex getOtherVertex(UFVertex refVertex) {
			if (!(refVertex.equals(childVertex)
					|| refVertex.equals(parentVertex)))
				return null;

			return (refVertex.equals(parentVertex))
					? childVertex
					: parentVertex;
		}

		@Override
		public boolean equals(Object obj) {

			if (obj == this)
				return true;
			if (!(obj instanceof UFEdge)) {
				return false;
			}
			UFEdge edge = (UFEdge) obj;
			return Objects.equals(parentVertex, edge.parentVertex)
					&& Objects.equals(childVertex, edge.childVertex);
		}

		@Override
		public String toString() {
			return (parentVertex.toString() + "parent , child"
					+ childVertex.toString() + "," + weight);
		}

		public String toStringAdjListForm(UFVertex refVertex) {
			return (getOtherVertex(refVertex)).toString() + ", " + weight;
		}

	}

	private static class UFGraph {

		private HashMap<Integer, UFVertex> vertices;
		private HashMap<Integer, UFEdge> edges;

		private UFGraph(ArrayList<Vertex> vertices) {

			this.vertices = new HashMap<Integer, UFVertex>();
			this.edges = new HashMap<Integer, UFEdge>();
			for (Vertex vertex : vertices) {
				this.vertices.put(vertex.hashCode(),
						new UFVertex(vertex.getName()));
			}
		}

		private boolean addEdge(UFVertex TparentVertex, UFVertex TchildVertex,
				int weight) {

			UFVertex parentVertex = vertices.get(TparentVertex.hashCode());
			UFVertex childVertex = vertices.get(TchildVertex.hashCode());
			if (parentVertex == null) {
				System.err.println("Parent vertex " + parentVertex.toString()
						+ " not created hence not creating edge");
				return false;
			}
			if (childVertex == null) {
				System.err.println("Child vertex " + childVertex.toString()
						+ " not created hence not creating edge");
				return false;
			}

			UFEdge edge = new UFEdge(parentVertex, childVertex, weight);
			UFEdge sameEdgeExist = edges.get(edge.hashCode());
			// if (edges.containsKey(edge.hashCode())) {
			if (sameEdgeExist != null) {
				if (sameEdgeExist.getWeight() != edge.getWeight())
					System.err.println(
							"duplicate edge found with same vertices and different weight. Not adding edge "
									+ edge.toString());
				return false;
			}
			edges.put(edge.hashCode(), edge);
			parentVertex.addChildEdge(edge);
			childVertex.setParentEdge(edge);
			return true;
		}

		@Override
		public String toString() {

			String graphString = "";

			int i = 0;
			for (UFVertex vertex : vertices.values()) {

				graphString += vertex.toString() + " parent -> "
						+ ((vertex.getParentEdge() == null)
								? " "
								: vertex.getParentEdge().getParentVertex())
						+ "; children-> " + "(";
				for (UFEdge edge : vertex.getChildEdges()) {
					graphString += "(" + edge.toStringAdjListForm(vertex) + ")";
				}
				graphString += ")\n";

			}
			return graphString;
		}
	}

	public UFGraph getDisjointGraph() {
		return disjointGraph;
	}

}
