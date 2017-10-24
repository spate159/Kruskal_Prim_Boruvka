import java.util.Objects;

public class Edge implements Comparable<Edge> {

	private Vertex v1;
	private Vertex v2;
	private int weight;

	public Edge(Vertex v1, Vertex v2, int weight) {
		super();
		this.v1 = v1;
		this.v2 = v2;
		this.weight = weight;
	}

	public Vertex getVertex1() {
		return v1;
	}

	public Vertex getVertex2() {
		return v2;
	}

	public int getWeight() {
		return weight;
	}

	public int compareTo(Edge other) {

		return this.weight - other.weight;

	}

	// Chck it.....
	@Override
	public int hashCode() {

		// int hash = 17;
		// int hash2 = 23;
		// hash = hash * 31 + v1.hashCode();
		// hash2 = hash2 * 31 + v2.hashCode();
		// hash = hash + hash2;
		// return hash;
		int v1;
		int v2;
		if (this.v1.hashCode() < this.v2.hashCode()) {
			v1 = this.v1.hashCode();
			v2 = this.v2.hashCode();
		} else {
			v2 = this.v1.hashCode();
			v1 = this.v2.hashCode();
		}
		
		return (((v1 + v2) * (v1 + v2 + 1)) / 2) + v2;
	}

	public Vertex getOtherVertex(Vertex refVertex) {
		if (!(refVertex.equals(v1) || refVertex.equals(v2)))
			return null;

		return (refVertex.equals(v1)) ? v2 : v1;
	}



	@Override
	public boolean equals(Object obj) {

		if (obj == this)
			return true;
		if (!(obj instanceof Edge)) {
			return false;
		}
		Edge edge = (Edge) obj;
		return (Objects.equals(v1, edge.v1) && Objects.equals(v2, edge.v2))
				|| (Objects.equals(v1, edge.v2) && Objects.equals(v2, edge.v1));
	}

	@Override
	public String toString() {
		return (v1.toString() + "," + v2.toString() + "," + weight);
	}

	public String toStringAdjListForm(Vertex refVertex) {
		return (getOtherVertex(refVertex)).toString() + ", " + weight;
	}


}
