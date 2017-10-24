import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class Vertex {

	protected String name;
	protected Vector<Edge> edges;

	// for inheritance overloading
	public Vertex(String name, List<Edge> edges) {
		this.name = name;
		this.edges = (Vector<Edge>) edges;
	}

	public String getName() {
		return name;
	}


	public Vector<Edge> getEdges() {
		return edges;
	}

	public Vertex(String name) {

		this.name = name;
		this.edges = new Vector<Edge>();
	}

	// efficient way
	public void setVectorSize(int size) {
		if (size > edges.size())
			edges.setSize(size);

	}
	public void addEdge(Edge edge) {
		if (this.edges.contains(edge)) {
			return;
		}
		this.edges.add(edge);
	}


	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		return name.hashCode();

	}

	public boolean isAdjacentTo(Vertex v) {
		if (this.edges.contains(new Edge(this, v, 1)))

		return true;

		return false;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == this)
			return true;
		if (!(obj instanceof Vertex)) {
			return false;
		}
		Vertex vertex = (Vertex) obj;
		return Objects.equals(name, vertex.name);

	}

	public void removeEdge(Edge edge) {
		edges.remove(edge);
	}

	public int noOfEdges() {
		return this.edges.size();
	}

	public Edge getEdge(int index) {
		return this.edges.get(index);
	}


}
