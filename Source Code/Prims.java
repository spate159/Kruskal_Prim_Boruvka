import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Prims {

	HashMap<Integer, PrimVertex> unvisited;


	private List<Edge> mstComputedEdges;

	public List<Edge> getMstComputedEdges() {

		if (mstComputedEdges == null) {
			throw new NullPointerException(
					"First run the MST method. Null MST");
		}
		return mstComputedEdges;
	}

	public int findMST(Graph graph, Vertex startVertex, boolean print) {


		int mstSize = graph.getGraphVertices().size();

		unvisited = new HashMap<>(mstSize);

		for (Vertex vertex : graph.getGraphVertices()) {
			unvisited.put(vertex.hashCode(), new PrimVertex(vertex.getName(),
					vertex.getEdges(), Integer.MAX_VALUE));
		}

		PrimVertex start;
		PrimVertex currentVertex;
		int weight = 0;

		if (startVertex == null) {
			start = unvisited.get(unvisited.keySet().iterator().next());
		} else {
			start = unvisited.get(startVertex.hashCode());
		}
		if (print) {
			System.out.println("\n Prim Start Vertex " + start);
		}
		
		start.setLabel(0);


		/*
		 * MinHeap<PrimVertex> minHeap = new MinHeap<>(mstSize); for (PrimVertex
		 * v : unvisited.values()) { minHeap.insert(v); }
		 */

		MinHeap<PrimVertex> minHeap = new MinHeap<>(
				new ArrayList<>(unvisited.values()));

		List<Edge> mstEdges = new ArrayList<>(mstSize - 1);

		while (!unvisited.isEmpty()) {// While Min Heap has elements

			// Extracting the lowest labeled vertex..
			currentVertex = minHeap.extractMin();
			unvisited.remove(currentVertex.hashCode());
			Edge selectedEdge = graph.getEdge(currentVertex,
					currentVertex.getParent());

			if (selectedEdge != null) {// for the first edge, because parent of
										// first edge will be null
				mstEdges.add(selectedEdge);
				// System.out.println("edge adding " + selectedEdge);
				weight = weight + graph
						.getEdge(currentVertex, currentVertex.getParent())
						.getWeight();// setting total weight

			}


			/*
			 * System.err.println("neighbours of \'" + currentVertex +
			 * "\' are total " + currentVertex.noOfEdges());
			 */

			for (Edge neighour : currentVertex.getEdges()) {

				int neighbourKey = neighour.getOtherVertex(currentVertex)
						.hashCode();// getting the key of the neighbour

				PrimVertex neighbourVertex = unvisited.get(neighbourKey);

				if (neighbourVertex == null) {// already visited neighbout
												// continue
					continue;

				}

				int nbWeight = neighour.getWeight();
				int label = neighbourVertex.getLabel();

				/*
				 * System.out.println("label neighbour " + neighbourVertex +
				 * " -> " + nbWeight + "; min -> " + label);
				 */

				if (isNeighbourUnvisted(neighbourVertex) && nbWeight < label) {// if
																				// neighbor
																				// is
																				// unvisited
																				// and
																				// has
																				// a
																				// lower
																				// weight
																				// when
																				// visited
																				// with
																				// current
																				// edge
					neighbourVertex.setLabel(nbWeight);// set lower
																	// label
														// and then...
					minHeap.decreaseKey(neighbourVertex.getIndex());// decrease
																// key
					neighbourVertex.parent = currentVertex;// set parent of the
															// neighbor current
															// vertex
					/*
					 * System.out.println("setting parent of " + neighbourVertex
					 * + " " + currentVertex);
					 */
				}

			}

		}
		this.mstComputedEdges = mstEdges;
		if (print) {
			Graph mstGraph = new Graph(mstEdges, Graph.EDGES);
			System.out.println("\nPrim's MST\n");
			System.out.println(mstGraph);
			System.out.println("Total MST weight =" + weight);
		}
		return weight;


	}

	public boolean isNeighbourUnvisted(PrimVertex neighbour) {
		return unvisited.containsKey(neighbour.hashCode());
	}

	public class PrimVertex extends Vertex
			implements
				Comparable<PrimVertex>,
				Indexer {

		private int label;
		private PrimVertex parent;// found from
		private int index;// index in minheap

		public PrimVertex(String name, List<Edge> neighbours, int label) {
			super(name, neighbours);
			parent = null;
			this.label = label;
		}

		public int getIndex() {
			return this.index;
		}

		public int getLabel() {
			return label;
		}

		public void setLabel(int lable) {
			this.label = lable;
		}

		public Vertex getParent() {
			return parent;
		}

		public void setParent(PrimVertex parent) {
			this.parent = parent;
		}

		@Override
		public int compareTo(PrimVertex o) {
			// TODO Auto-generated method stub
			return this.label - o.label;
		}

		@Override
		public int setIndex(int index) {
			this.index = index;
			return 0;
		}
	}
}
// public static void main1(String args[]) {
// // Heap testing..
//
// /*
// * MinHeap<Integer> myHeap = new MinHeap<>(10); myHeap.insert(8);
// * myHeap.insert(5); myHeap.insert(4); myHeap.insert(4);
// * myHeap.insert(15); myHeap.insert(12); myHeap.insert(6);
// * myHeap.insert(5); myHeap.insert(9); myHeap.insert(-10);
// * myHeap.insert(1);
// *
// * while (myHeap.headCount() > 0) {
// * System.out.println(myHeap.extractMin()); }
// */
//
// }