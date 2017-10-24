import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

public class GraphFileGenerator {

	private int maxWeight = -1;
	public void generateGraph(int vertexCount, int edgeCount, String fileName) {

		if (edgeCount < vertexCount - 1 || vertexCount < 2) {
			System.err.println(
					"edge count should be atleaset vertex count-1 and vertex count should be greater than 2");
			return;
		}

		File f = new File(fileName);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));

			Random randWeight = new Random();
			int randomWeighMax;
			if (maxWeight == -1) {
			randomWeighMax= 50;
			}else{
				randomWeighMax = maxWeight;
			}
			HashSet<VertexPair> uniquePair = new HashSet<>();

			String[] graphStr = new String[vertexCount + 1];

			boolean minGraph = false;
			if (edgeCount == vertexCount - 1) {
				minGraph = true;
				System.out.println("min graph");
			}

			for (int v = 1; v <= vertexCount; v++) {
				if (v + 1 <= vertexCount) {
					int randomWeight = randWeight.nextInt(randomWeighMax) + 1;// minimum
																				// 1
					graphStr[v] = v + " ((" + (v + 1) + ", " + randomWeight
							+ ")";
					uniquePair.add(new VertexPair(v, v + 1));
				}
				else if (minGraph) {
					graphStr[v] = "" + v;
				}
				else {
					graphStr[v] = v + " ((1, "
							+ randWeight.nextInt(randomWeighMax) + ")";
					uniquePair.add(new VertexPair(v, 1));
				}
			}

			Random randVertex = new Random();

			for (int e = 1; e <= edgeCount - vertexCount; e++) {// adding
																// random
																// edges

				int v1 = randVertex.nextInt(vertexCount) + 1;
				int v2 = randVertex.nextInt(vertexCount) + 1;
				// System.out.println("v1 " + v1);
				while (v1 == v2 || uniquePair.contains(new VertexPair(v1, v2)))
				{

					v1 = randVertex.nextInt(vertexCount) + 1;
					v2 = randVertex.nextInt(vertexCount) + 1;
				}

				if (v1 == vertexCount && graphStr[vertexCount]
						.charAt((graphStr[vertexCount]).length() - 1) == '(') {
					// System.out.println("adding edge at last vertex");
					graphStr[v1] += "(" + v2 + ", "
							+ (randWeight.nextInt(randomWeighMax) + 1) + ")";
				} else {

					graphStr[v1] += ",(" + v2 + ", "
							+ (randWeight.nextInt(randomWeighMax) + 1) + ")";
				}
				uniquePair.add(new VertexPair(v1, v2));
			}

			for (int v = 1; v <= vertexCount; v++) {
				if (minGraph && v == vertexCount) {
					bw.write(graphStr[v]);
					continue;
				}
				graphStr[v] += "),\n";
				bw.write(graphStr[v]);
				
			}

			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		int vertexCount = Integer.parseInt(args[0]);
		int edgeCount = Integer.parseInt(args[1]);
		String graphFileName = args[2];
		int maxWeight = Integer.parseInt(args[3]);

		if (edgeCount < vertexCount - 1 || vertexCount < 2) {
			System.err.println(
					"edge count should be atleaset vertex count-1 and vertex count should be greater than 2");
			return;
		}
		if (edgeCount > ((vertexCount * (vertexCount - 1)) / 2)) {
			System.err.println("max edge count can be (n)*(n-1)/2");
			return;
		}

		GraphFileGenerator gGen = new GraphFileGenerator();
		gGen.maxWeight = maxWeight;
		gGen.generateGraph(vertexCount, edgeCount, graphFileName);

		System.out.println("Graph created");
	}

	public class VertexPair {
		int v1;
		int v2;

		public VertexPair(int v1, int v2) {
			super();
			this.v1 = v1;
			this.v2 = v2;
		}

		@Override
		public int hashCode() {
			int v1;
			int v2;
			if (this.v1 < this.v2) {
				v1 = this.v1;
				v2 = this.v2;
			} else {
				v2 = this.v1;
				v1 = this.v2;
			}
			return (((v1 + v2) * (v1 + v2 + 1)) / 2) + v2;

		}
		@Override
		public boolean equals(Object obj) {

			if (obj == this)
				return true;
			if (!(obj instanceof VertexPair)) {
				return false;
			}
			VertexPair vPair = (VertexPair) obj;
			if (vPair.v1 == v1 && vPair.v2 == v2
					|| vPair.v1 == v2 && vPair.v2 == v1) {

				return true;
			} else
				return false;
		}

		public String toSting() {
			return "(" + v1 + "," + v2 + ")";
		}
	}
}
