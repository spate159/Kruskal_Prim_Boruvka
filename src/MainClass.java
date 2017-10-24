public class MainClass {
	public static void main(String[] args) {

		if (args.length == 0) {
			try {
				throw new Exception(
						"Please enter full file URL at arguement 0");
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}

		String fileName = "graph1.txt";
		Graph graph = ReadGraph.readGraph(fileName);

		System.out.println(graph);
		// testingUnionFind(graph);

		Kruskals kmst = new Kruskals();
		Prims pmst = new Prims();
		Boruvka boruvka = new Boruvka();

		kmst.findMst(graph, true);// execute kruskal
		pmst.findMST(graph, null, true);// execute prims
		boruvka.findMST(graph, true);// execute borvuka


	}

	/*
	 * public static void testingUnionFind(Graph g) { // testing UF
	 * 
	 * UnionFind uf = new UnionFind( (ArrayList<Vertex>) g.getGraphVertices());
	 * uf.union(new Vertex("one"), new Vertex("two")); uf.union(new
	 * Vertex("three"), new Vertex("two")); uf.union(new Vertex("five"), new
	 * Vertex("four")); uf.union(new Vertex("three"), new Vertex("four"));
	 * uf.union(new Vertex("three"), new Vertex("five"));
	 * System.out.println(uf.getDisjointGraph()); }
	 */
}
