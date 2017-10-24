import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Boruvka {

	// private HashMap<Edge, EdgeComponents> edgesMap = new HashMap<>();
	// private HashSet<Component> components = new HashSet<>();

	// testing
	DisplayGraph dg;
	boolean testing = false;

	private ComponentGraph currentCG = new ComponentGraph();

	private int maxCompID = 0;

	HashSet<Edge> mstEdgeList = new HashSet<Edge>();

	private void init(Graph graph) {

		HashMap<Vertex, Component> vertexMap = new HashMap<>();

		for (Vertex v : graph.getGraphVertices()) {
			Component component = new Component(maxCompID++);
			component.setExtEdges(new HashSet<>(v.getEdges())); // creating
																// initial
																// components
			// for each vertex
			vertexMap.put(v, component);
			currentCG.components.add(component);

		}

		for (Vertex v : graph.getGraphVertices()) {

			for (Edge edge : v.getEdges()) {
				currentCG.edgesMap.put(edge,
						new EdgeComponents(vertexMap.get(edge.getVertex1()),
								vertexMap.get(edge.getVertex2())));
			}
		}
	}

	public int findMST(Graph graph, boolean print) {

		init(graph);

		while (currentCG.components.size() > 1) {

			// create an iterator
			Iterator<Component> iterator = currentCG.components.iterator();

			// check values
			while (iterator.hasNext()) {

				Component c = iterator.next();
				int minWeight = Integer.MAX_VALUE;
				Edge minEdge = null;
				for (Edge edge : c.getExtEdges()) {

					// System.out.println("component = " + c.getID()
					// + " edge in consideration " + edge);

					if (edge.getWeight() < minWeight) { // find the minimum edge
														// incident to the
														// vertex
						minWeight = edge.getWeight();
						minEdge = edge;
						c.setMinEdge(minEdge);
					}
				}

				// System.err.println("component = " + c.getID()
				// + " minimum incident edge " + minEdge);
				c.addMSTEdge(minEdge); // add the min edge to component


				currentCG.edgesMap.get(minEdge).getOtherComponent(c)
						.addMSTEdge(minEdge);// add
												// min
												// edge
												// to
												// the
												// opposite
												// component

				currentCG.minEdgeList.add(minEdge);// no duplicate min edges
													// ensured set

			}

			// (new ArrayList<Edge>(currentCG.minEdgeList))
			// .forEach(System.out::println);

			ComponentGraph newComponentGraph = findComponents();
			currentCG = newComponentGraph;
			cleanGraph(currentCG);

		}


		if (print) {
			Graph graph2 = new Graph(new ArrayList<>(mstEdgeList), Graph.EDGES);
		System.out.println("\nBoruvka MST\n");
		System.out.println(graph2);
		System.out.println("Total MST weight =" + totalWeight());
		}
		return totalWeight();
	}

	private ComponentGraph findComponents() {

		ComponentGraph cg = new ComponentGraph();

		Iterator<Component> iterator = currentCG.components.iterator();
		while (iterator.hasNext()) {
			Component component = iterator.next();

			if (!component.isVisited()) {

				component.setVisited(true);
				// System.out.println(component.getMSTEdges().size());
				dfs(component, null, cg, null);// perform dfs on the component
				// to find other components

			}
		}

		mstEdgeList.addAll(currentCG.minEdgeList);// add to the final mst
		// System.out.println(currentCG.minEdgeList.size());
		// System.out.println("MST size = " + mstEdgeList.size());
		// if (testing == true) {
		// Graph graph2 = new Graph(new ArrayList<>(currentCG.minEdgeList),
		// Graph.EDGES);
		// dg = new DisplayGraph(graph2, 640, 640, 1000, 64.0);
		// DisplayGraph.drawGraph(dg, graph2, false);
		// }
		return cg;
	}

	private void cleanGraph(ComponentGraph cg) {
		HashMap<EdgeComponents, Edge> uniquePair = new HashMap<>(
				cg.edgesMap.size());
		List<Edge> removeEdge = new ArrayList<>();

		for (Edge edge : cg.edgesMap.keySet()) {
			EdgeComponents ec = cg.edgesMap.get(edge);
			if (uniquePair.containsKey(ec)) {
				if (uniquePair.get(ec).getWeight() >= edge.getWeight()) {
					// cg.edgesMap.remove(uniquePair.get(ec)); remove at the end
					removeEdge.add(uniquePair.remove(ec));
					uniquePair.put(ec, edge);
				}
					
			} else {
				uniquePair.put(ec, edge);
			}
		}

		for (Edge edge : removeEdge) {
			EdgeComponents ec = cg.edgesMap.get(edge);
			ec.c1.extEdges.remove(edge);
			ec.c2.extEdges.remove(edge);
			cg.edgesMap.remove(edge);
		}

	}

	// pass parent Component null... used for detection of cycle
	private void dfs(Component currentComponent, Component parentComponent,
			ComponentGraph newComponentGraph, Component newComponent) {

		for (Edge edge : currentComponent.getMSTEdges()) {

			Component oppositeComponent = getEdgeComponent(edge)
					.getOtherComponent(currentComponent);

			// System.out.println("min edge " + edge + " OtherComponent "
			// + otherComponent.getID());

			if (newComponent == null) {
				newComponent = new Component(maxCompID++);
			}

			if (!oppositeComponent.isVisited) {
				// System.out.println("comp in consideration to other " +
				// currentComponent.ID
				// + " " + oppositeComponent.ID + " \"visiting edge\" "
				// + edge);
				oppositeComponent.isVisited = true;

				dfs(oppositeComponent, currentComponent, newComponentGraph, newComponent);

				setNewComponents(newComponent, newComponentGraph, currentComponent);

			} 
			else {// other component is visited and it could be a cycle.

				if (parentComponent == null) { // first round visited vertex
					// currentCG.minEdgeList.remove(edge);

				}

				else if (!parentComponent.equals(oppositeComponent)
				) {// if
					// its
					// not
					// the
					// same edge
					// from
					// the
					// parent
					// then a
					// cycle
					// edgesMap.remove(edge); // removing just this edge as
					// all
					// other are of same weight
					currentCG.minEdgeList.remove(edge);
					System.err.println("cycle detected for edge " + edge);
				}

				// set the new external edges of the new component
				setNewComponents(newComponent, newComponentGraph,
						currentComponent);
			}

		}
	}

	public void setNewComponents(Component newComponent,
			ComponentGraph newComponentGraph, Component currentComponent) {
		// if external edges have not been updated before from this
		// component

		if (!newComponent.internalComponents.contains(currentComponent)) {

			Iterator<Edge> iterator = currentComponent.getExtEdges().iterator();// check
																				// all
																				// the
																				// external
																				// edges
																				// of
																				// this
																				// component
																				// to
																				// put
																				// it
																				// in
																				// the
																				// new
																				// component

			while (iterator.hasNext()) {
				Edge extEdge = iterator.next();

				if (!currentComponent.getMSTEdges().contains(extEdge)) {
					// if external edge is not an internal one then

					EdgeComponents edgeComponent = newComponentGraph.edgesMap
							.get(extEdge);// if component exist then check that
											// if the edge is an internal edge
											// of new component

					if (edgeComponent == null) {
						edgeComponent = new EdgeComponents();

					} else {
						/*
						 * System.out.println("c= " + currentComponent.ID +
						 * " edge hit " + extEdge + " connected to " +
						 * edgeComponent.getOneComponent().getID());
						 */

						if (edgeComponent.hasAComponent(newComponent)) {// internal
							// edge

							// System.err.println(
							// "Internal edge not external! " + extEdge);

							iterator.remove();
							newComponentGraph.edgesMap.remove(extEdge);
							newComponent.getExtEdges().remove(extEdge);
							continue;
						}

					}

					edgeComponent.setUnsetComponents(newComponent);// set one of
																	// the edge
																	// component
																	// to new
																	// component

					// System.out.println(" Adding ext edge " + extEdge
					// + " from source " + currentComponent.getID()
					// + " new component "
					// + newComponent.getID());

					newComponent.getExtEdges().add(extEdge);// add the external
															// edge to new
															// component

					newComponent.internalComponents.add(currentComponent);// add
															// this
															// sub
															// component
															// to
															// new
															// component

					newComponentGraph.edgesMap.put(extEdge, edgeComponent);// put
																			// the
																			// edge
																			// in
																			// new
																			// graph
																			// edge
																			// set
					newComponentGraph.components.add(newComponent);// put the
																	// component
																	// in the
																	// new graph
																	// component
				}

			}
		}

	}

	public int totalWeight() {
		int weight = 0;
		for (Edge edge : mstEdgeList) {
			weight = weight + edge.getWeight();
		}
		return weight;
	}

	/**
	 * 
	 * @param c
	 *            component
	 * @return min weight edge
	 */
	private Edge findMinEdge(Component c) {
		Edge minEdge = null;
		for (Edge edge : c.extEdges) {
			int minWeight = Integer.MAX_VALUE;
			if (edge.getWeight() < minWeight) {
				minWeight = edge.getWeight();
				minEdge = edge;
			}
		}
		return minEdge;
	}

	private EdgeComponents getEdgeComponent(Edge edge) {
		return currentCG.edgesMap.get(edge);
	}

	private class Component {
		private int ID;
		private Edge minEdge;
		private Set<Edge> extEdges;
		private boolean isVisited;
		private Set<Edge> mstEdges;
		private HashSet<Component> internalComponents;

		public Set<Edge> getMSTEdges() {
			return mstEdges;
		}
		public void addMSTEdge(Edge edge) {
			mstEdges.add(edge);
		}
		public Set<Edge> getExtEdges() {
			if (extEdges == null) {
				extEdges = new HashSet<>();
			}
			return extEdges;
		}
		public void setExtEdges(Set<Edge> extEdges) {
			this.extEdges = extEdges;
		}
		public Component(int iD) {// Constructor
			super();
			ID = iD;
			isVisited = false;
			mstEdges = new HashSet<>();
			internalComponents = new HashSet<>();
		}
		public int getID() {
			return ID;
		}
		public void setID(int iD) {
			ID = iD;
		}
		public Edge getMinEdge() {
			return minEdge;
		}
		public void setMinEdge(Edge minEdge) {
			this.minEdge = minEdge;
		}

		@Override
		public int hashCode() {
			return ID;
		}
		@Override
		public boolean equals(Object obj) {

			if (obj == this)
				return true;
			if (!(obj instanceof Component)) {
				return false;
			}
			Component comp = (Component) obj;
			return Objects.equals(ID, comp.ID);
		}
		public boolean isVisited() {
			return isVisited;
		}
		public void setVisited(boolean isVisited) {
			this.isVisited = isVisited;
		}

	}

	private class EdgeComponents {

		private Component c1;
		private Component c2;

		public EdgeComponents(Component c1, Component c2) {
			super();
			this.c1 = c1;
			this.c2 = c2;
		}

		public EdgeComponents() {
			// TODO Auto-generated constructor stub
		}

		public void setUnsetComponents(Component c) {

			if (this.c1 == null) {
				this.c1 = c;
			} else if (this.c2 == null) {
				this.c2 = c;
			}

		}
		public int totalSetComponents() {
			// testing
			int total = 0;
			if (this.c1 == null) {
				total++;
			}
			if (this.c2 == null) {
				total++;
			}
			return total;
		}
		public void setGivenComponent(Component oldComponent,
				Component newComponent) {

			if (this.c1.equals(oldComponent)) {
				this.c1 = newComponent;
			} else if (this.c2.equals(oldComponent)) {
				this.c2 = newComponent;
			}

		}

		public void setComponents(Component c1, Component c2) {
			this.c1 = c1;
			this.c2 = c2;
		}

		public Component getOneComponent() {
			return c1;
		}

		public Component getOtherComponent(Component oneComponent) {

			if (!(oneComponent.equals(c1) || oneComponent.equals(c2)))
				return null;

			return (oneComponent.equals(c1)) ? c2 : c1;
		}

		public boolean hasAComponent(Component c) {

			if (c1 != null && c1.equals(c)) {
				return true;
			} else if (c2 != null && c2.equals(c)) {
				return true;
			}
			return false;

		}

		@Override
		public int hashCode() {
			int c1;
			int c2;
			if (this.c1.hashCode() < this.c2.hashCode()) {
				c1 = this.c1.hashCode();
				c2 = this.c2.hashCode();
			} else {
				c2 = this.c1.hashCode();
				c1 = this.c2.hashCode();
			}

			return (((c1 + c2) * (c1 + c2 + 1)) / 2) + c2;
		}
		@Override
		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			if (!(obj instanceof EdgeComponents)) {
				return false;
			}
			EdgeComponents ec = (EdgeComponents) obj;
			return (Objects.equals(c1, ec.c1) && Objects.equals(c2, ec.c2))
					|| (Objects.equals(c1, ec.c2) && Objects.equals(c2, ec.c1));
		}

	}

	public class ComponentGraph {
		private HashMap<Edge, EdgeComponents> edgesMap;
		private HashSet<Component> components;
		HashSet<Edge> minEdgeList;
		public ComponentGraph() {
			edgesMap = new HashMap<>();
			components = new HashSet<>();
			minEdgeList = new HashSet<Edge>();
		}
	}



}
