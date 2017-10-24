/**
 * Testing oriented class.
 * 
 * @author Shyam
 *
 */
public class Tester {


	public int primWeight;
	public int kruskalWeight;
	public int boruvkaWeight;

	public static long[][] timeKeeperBoruvka;
	public static long[][] timeKeeperPrim;
	public static long[][] timeKeeperKruskal;

	public long computeBoruvkaTime(Graph graph, boolean print) {
		Boruvka boruvka = new Boruvka();
		long boruvkaStartTime = System.nanoTime();
		int bWeight = boruvka.findMST(graph, print);
		long boruvkaEndTime = System.nanoTime();
		long boruvkaTime = boruvkaEndTime - boruvkaStartTime;
		boruvkaWeight = bWeight;
		return boruvkaTime;
	}

	public long computePrimTime(Graph graph, boolean print) {
		Prims prims = new Prims();
		long primStartTime = System.nanoTime();
		int pWeight = prims.findMST(graph, null, print);
		long primEndTime = System.nanoTime();
		long primTime = primEndTime - primStartTime;
		primWeight = pWeight;
		return primTime;
	}

	public long computeKruskalTime(Graph graph, boolean print) {
		Kruskals kruskals = new Kruskals();

		long kruskalStartTime = System.nanoTime();
		int kWeight = kruskals.findMst(graph, print);
		long kruskalEndTime = System.nanoTime();
		long kruskalTime = kruskalEndTime - kruskalStartTime;
		kruskalWeight = kWeight;
		return kruskalTime;

	}
	public static void main(String[] args) {

		int vStart = 9;// vertex test start min..

		int verticesVary = 10;// how many vertices varying graph to genereate
								// and test
		int edgesVary = 5;// how many edges to vary in each graph.



		int repeatCount = 10;// how many times test need to be peformed for
								// average analysis
		
		boolean genGraph = true;// Want to genereate random graph before test
		boolean runTests = true;// want to run test based on graph
		 
		timeKeeperBoruvka = new long[verticesVary][edgesVary];
		timeKeeperPrim = new long[verticesVary][edgesVary];
		timeKeeperKruskal = new long[verticesVary][edgesVary];

		String fileName;
		int v;
		int e;
		for (int i = 0; i < verticesVary; i++) {

			for (int j = 0; j < edgesVary; j++) {

				v = vStart + (i * 1);
				e = v - 1 + j;

				if (genGraph) {
					System.out.println(
							"Creating Graph for v = " + v + " e = " + e);
					// Generate Graph
					GraphFileGenerator gFileGen = new GraphFileGenerator();
					fileName = "graph-v" + v + "-e" + e + ".txt";
					gFileGen.generateGraph(v, e, fileName);
				}

				if (runTests) {
					System.out.println("Generating test result for v = " + v
							+ " e = " + e);

					fileName = "graph-v" + v + "-e" + e + ".txt";
					// Read Graph
					Graph graph = ReadGraph.readGraph(fileName);
					for (int repeat = 0; repeat < repeatCount; repeat++) {
					Tester tester = new Tester();
					
					long krTime = tester.computeKruskalTime(graph, false);
					long prTime = tester.computePrimTime(graph, false);
					long brTime = tester.computeBoruvkaTime(graph, false);

					if (!((tester.kruskalWeight == tester.primWeight)
								&& (tester.primWeight == tester.boruvkaWeight))) {// if
																					// Discrepancy
																					// in
																					// weights
																					// found
																					// by
																					// 3
																					// algorithms
																					// then
																					// there
																					// is
																					// a
																					// bug
						System.err.println("Weight discripancy");
						System.err.println("Kruskal " + tester.kruskalWeight);
						System.err.println("Prim " + tester.primWeight);
						System.err.println("boruvka " + tester.boruvkaWeight);
						return;
					}

					timeKeeperKruskal[i][j] += krTime;
					timeKeeperBoruvka[i][j] += brTime;
					timeKeeperPrim[i][j] += prTime;

					// System.out.println("Boruvka Time " + brTime);
					}
				}
			}
		}

		long[][] timeKeeperPrint = timeKeeperPrim;// change the name here to
													// print the result of
													// prims, kruskals or
													// boruvka

		// Boruvka time analysis...
		for (int i = 0; i < timeKeeperPrint.length; i++) {

			v = vStart + (i * 1);
			for (int j = 0; j < timeKeeperPrint[0].length; j++) {

				if (i == 0 && j == 0) {
					System.out.print("V/E(ms)");
				}
				if (i == 0 && j == 0) {
					for (int k = 0; k < timeKeeperPrint[0].length; k++) {
						e = v - 1 + k;
						System.out.print("\t n+" + (-1 + k));
					}
					System.out.println();
				}

				if (j == 0) {
					System.out.print(v + "\t");
				}
				System.out.print(timeKeeperPrint[i][j] / (repeatCount * 1000));
				System.out.print("\t");
			}
			System.out.println();
		}
	}
}
