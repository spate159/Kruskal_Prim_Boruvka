import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ReadGraph {

	public static Graph readGraph(String fileName) {
		File graphFile = new File(fileName);
		BufferedReader br = null;
		Graph graph = null;
		try {
			 br= new BufferedReader(new FileReader(graphFile));
			String sCurrentLine;
			ArrayList<Vertex> readVertices= new ArrayList<>();
			ArrayList<Edge> readEdges = new ArrayList<>();
			// String pattern =
			while ((sCurrentLine = br.readLine()) != null) {
				String vertexString = sCurrentLine.split("\\(\\(")[0].trim();

				Vertex v1 = new Vertex(vertexString);
				readVertices.add(v1);

				if (sCurrentLine.split("\\(\\(").length < 2)
					continue;
				String edgeString = "("
						+ sCurrentLine.split("\\(\\(")[1].trim();
				edgeString = edgeString.substring(0, edgeString.length() - 1);
				// System.out.println(edgeString);
				Pattern pattern = Pattern.compile("\\((.*?)\\)");
				Matcher matcher = pattern.matcher(edgeString);

				while (matcher.find()) {
					String matchedStr = matcher.group(1);
					// System.out.println(matchedStr);
					String[] edgeStr = matchedStr.split(",");
					String otherVertex = edgeStr[0];
					if(edgeStr.length<2)
						throw new ArrayIndexOutOfBoundsException(
								"All Weight needs to be defined");
					String weight = edgeStr[1];
					// System.out.println(otherVertex.trim());
					// System.out.println(weight.trim());

					readEdges.add(
							new Edge(v1,
									new Vertex(otherVertex.trim()),
									Integer.parseInt(weight.trim())));
				}

			}

			// create graph and add edge and vertices....
			graph = new Graph(readVertices);
			for (Edge edge : readEdges) {
				graph.addEdge(edge.getVertex1(), edge.getVertex2(),
						edge.getWeight());

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				br.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return graph;
	}
}