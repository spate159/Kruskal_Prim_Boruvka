import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DisplayGraph extends JComponent {

	// private Graph graph;
	float area;
	int frameLength;
	int frameWidth;
	double k;
	int numberOfVertex;
	HashMap<Integer, DisplayVertex> vertices;
	int iterations;
	double temperature;
	private List<Line> lines;
	List<DisplayEdge> edges;
	List<DisplayEdge> mstEdge;
	private List<Line> mstLines;
	boolean showWeights = false;
	boolean showVertices = false;
	boolean showMST = false;
	boolean debug;

	public DisplayGraph(Graph graph, int frameLength, int frameWidth,
			int iterations, double temperature) {
		this.vertices = new HashMap<>();
		this.edges = new ArrayList<>();
		this.mstEdge = new ArrayList<>();
		setEdgesAndVertices(graph.getGraphEdges());
		this.frameLength = frameLength;
		this.frameWidth = frameWidth;
		this.area = (float) frameLength * frameWidth;
		this.numberOfVertex = graph.getGraphVertices().size();
		this.k = (float) Math.sqrt(area / numberOfVertex);
		this.iterations = iterations;
		this.temperature = temperature;
		this.lines = new ArrayList<>();
		this.mstLines = new ArrayList<>();
		debug = false;
	}

	public DisplayGraph() {
		// TODO Auto-generated constructor stub
	}

	private void setEdgesAndVertices(List<Edge> edges) {

		// HashMap<Integer, DisplayVertex> vertices = new HashMap<>();
		for (Edge edge : edges) {

			DisplayVertex u = new DisplayVertex(edge.getVertex1().getName());

			DisplayVertex v = new DisplayVertex(edge.getVertex2().getName());

			if (vertices.containsKey(u.hashCode())) {
				u = vertices.get(u.hashCode());
			} else {
				vertices.put(u.hashCode(), u);
			}
			if (vertices.containsKey(v.hashCode())) {
				v = vertices.get(v.hashCode());
			} else {
				vertices.put(v.hashCode(), v);
			}

			DisplayEdge newEdge = new DisplayEdge(u, v, edge.getWeight());

			v.addEdge(newEdge);

			u.addEdge(newEdge);
			this.edges.add(newEdge);
		}

	}
	public double atForce(double x) {
		return (float) x * x / k;
	}

	public double rpForce(double x) {
		return (float) k * k / (x + 0.000001);
	}

	public void randomCorrInit() {
		Random rGen = new Random();
		HashSet<Coordinates> coord = new HashSet<>();
		for (DisplayVertex v : vertices.values()) {
			float x = rGen.nextFloat() * frameWidth;
			float y = rGen.nextFloat() * frameLength;

			Coordinates c = new Coordinates(x, y);
			while (coord.contains(c)) {
				float x_t = rGen.nextFloat() * frameWidth;
				float y_t = rGen.nextFloat() * frameLength;

				c = new Coordinates(x_t, y_t);
			}
			coord.add(c);
			v.pos = c;
			// System.out.println(v + " " + c);
		}
	}
	public void executeFR() {

		randomCorrInit();

		for (int i = 0; i < iterations; i++) {
			calRpForce();
			calAtForce();
			limitMaxDisp();
			temperature = coolTemp(temperature);
			if (debug)
				System.out.println("Temperature = " + temperature);
		}

	}

	public void calRpForce() {

		for (DisplayVertex v : vertices.values()) {
			v.disp.setXY(0.0f, 0.0f);
			for (DisplayVertex u : vertices.values()) {

				if (!u.equals(v)) {
					// δ is the difference vector between the positions of the
					// two vertices
					Coordinates δ = v.pos.subtract(u.pos);
					// System.out.println("delta " + δ + v.pos + u.pos);
					v.disp = v.disp
							.add(δ.unit().scale((float) rpForce(δ.length())));

				}
			}
		}

	}

	public void calAtForce() {
		for (DisplayVertex v : vertices.values()) {

			for (DisplayVertex u : vertices.values()) {
				if (v.isAdjacentTo(u)) {
					Coordinates δ = v.pos.subtract(u.pos);
					v.disp = v.disp.subtract(
							(δ.unit().scale((float) atForce(δ.length()))));
					u.disp = u.disp
							.add((δ.unit().scale((float) atForce(δ.length()))));
				}
			}
		}
	}

	public double coolTemp(double temperature) {

		return temperature * 0.9;
	}

	public void limitMaxDisp() {
		int i = 0;
		for (DisplayVertex v : vertices.values()) {
			if (debug) {
				if (i == 0) {

					System.out.println(v.pos.toString());
					Coordinates disp = v.disp.unit().scale(
							(float) Math.min(v.disp.length(), temperature));
					System.out.println(">>" + disp.toString());

				}
			}
			Coordinates vDisp = (v.disp.unit()
					.scale((float) Math.min(v.disp.length(), temperature)));
			// System.out.println(vDisp);
			Coordinates newCorr = v.pos.add(vDisp);

			v.pos.setXY(newCorr.getX(), newCorr.getY());
			v.pos.setX((float) Math.min(frameWidth, Math.max(0, v.pos.getX())));
			v.pos.setY(
					(float) Math.min(frameLength, Math.max(0, v.pos.getY())));
			if (debug) {
				if (i++ == 0) {
					System.out.println(v.pos.toString());
				}
			}
		}
	}

	public static void drawGraph(DisplayGraph dg, Graph graph, boolean print) {

		dg.executeFR();
		for (DisplayVertex v : dg.vertices.values()) {
			v.pos = v.pos.add(new Coordinates(10, 10));// shift bottom right
			if (print)
			System.out.println(v.toString() + " " + v.pos.toString());
		}

		// also computing mst...
		Prims prims = new Prims();
		prims.findMST(graph, null, false);
		List<Edge> mstList = prims.getMstComputedEdges();


		// creating line from vertex coordinates
		for (DisplayEdge edge : dg.edges) {
			DisplayVertex u = edge.getVertex1();
			DisplayVertex v = edge.getVertex2();
			// System.out.println(edge);
			// System.out.println(edge.v1.pos + " - " + edge.v2.pos);
			Color randomColor = new Color((float) Math.random(),
					(float) Math.random(), (float) Math.random());
			Line line = dg.addLine((int) u.pos.getX(), (int) u.pos.getY(),
					(int) v.pos.getX(), (int) v.pos.getY(), randomColor,
					edge.weight);

			// also calculating mst edges here
			if (mstList.contains(new Edge(new Vertex(edge.v1.name),
					new Vertex(edge.v2.name), edge.weight))) {
				dg.mstEdge.add(edge);
				dg.mstLines.add(line);
			}
		}
		dg.showGraph(dg, graph);
	}

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
		Graph inGraph = ReadGraph.readGraph(args[0]);
		DisplayGraph dg = new DisplayGraph(inGraph, 640, 640, 1000, 64.0);
		DisplayGraph.drawGraph(dg, inGraph, true);

	}

	public float area() {
		return area;
	}

	public void showGraph(DisplayGraph dg, Graph graph) {
		JFrame testFrame = new JFrame();
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		dg.setPreferredSize(new Dimension(720, 720));
		testFrame.getContentPane().add(dg, BorderLayout.CENTER);
		JPanel buttonsPanel = new JPanel();
		JButton vButton = new JButton("Display Vertices");
		JButton wButton = new JButton("Display Weights");
		JButton mstButton = new JButton("Display MST");
		buttonsPanel.add(vButton);
		buttonsPanel.add(wButton);
		buttonsPanel.add(mstButton);
		testFrame.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
		vButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showVertices = !showVertices;
				repaint();
			}
		});
		wButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showWeights = !showWeights;
				repaint();
			}
		});
		mstButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				showMST = !showMST;
				repaint();
			}
		});
		testFrame.pack();
		testFrame.setVisible(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		List<Line> lines;
		if (showMST) {
			lines = mstLines;
		} else {
			lines = this.lines;
		}

		for (Line line : lines) {

			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(2));
			g2.setColor(line.color);
			g2.drawLine(line.x1, line.y1, line.x2, line.y2);

			if (showWeights) {
				g.setColor(Color.darkGray);
				g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
				g.drawString(String.valueOf(line.weight),
						(int) (line.x1 + line.x2) / 2,
						(int) (line.y1 + line.y2) / 2);
			}

		}
		if (showVertices) {
			for (DisplayVertex v : vertices.values()) {
				g.setColor(Color.BLUE);
				g.fillOval((int) v.pos.getX() - 5, (int) v.pos.getY() - 5, 10,
						10);
				g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
				g.setColor(Color.RED);
				g.drawString(v.getName(), (int) v.pos.getX(),
						(int) v.pos.getY());
			}
		}
	}
	public Line addLine(int x1, int x2, int x3, int x4) {
		Line line = addLine(x1, x2, x3, x4, Color.black, 0);
		return line;
		// repaint();
	}
	public Line addLine(int x1, int x2, int x3, int x4, Color color,
			int weight) {
		Line line = new Line(x1, x2, x3, x4, color, weight);
		lines.add(line);
		return line;
		// repaint();
	}
	private static class Line {
		final int x1;
		final int y1;
		final int x2;
		final int y2;
		final Color color;
		final int weight;
		//
		// public Line(int x1, int y1, int x2, int y2, Color color) {
		// this.x1 = x1;
		// this.y1 = y1;
		// this.x2 = x2;
		// this.y2 = y2;
		// this.color = color;
		// }
		public Line(int x1, int y1, int x2, int y2, Color color, int weight) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.color = color;
			this.weight = weight;
		}
	}

	// Could be a better design than what i implemented here.
	private static class DisplayVertex {

		protected Coordinates disp;// displacement
		protected Coordinates pos;

		protected String name;
		protected List<DisplayEdge> edges;


		public String getName() {
			return name;
		}

		public List<DisplayEdge> getEdges() {
			return edges;
		}

		public DisplayVertex(String name) {

			this.name = name;
			this.edges = new ArrayList<DisplayEdge>();
			this.disp = new Coordinates(0.0f, 0.0f);
			this.pos = new Coordinates(0.0f, 0.0f);
		}

		public void addEdge(DisplayEdge edge) {
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

		public boolean isAdjacentTo(DisplayVertex v) {
			if (this.edges.contains(new DisplayEdge(this, v, 1)))

				return true;

			return false;
		}

		@Override
		public boolean equals(Object obj) {

			if (obj == this)
				return true;
			if (!(obj instanceof DisplayVertex)) {
				return false;
			}
			DisplayVertex vertex = (DisplayVertex) obj;
			return Objects.equals(name, vertex.name);

		}

		public void removeEdge(DisplayEdge edge) {
			edges.remove(edge);
		}

		public int noOfEdges() {
			return this.edges.size();
		}

		public DisplayEdge getEdge(int index) {
			return this.edges.get(index);
		}

	}

	public static class DisplayEdge {

		private DisplayVertex v1;
		private DisplayVertex v2;
		private int weight;

		public DisplayEdge(DisplayVertex v1, DisplayVertex v2, int weight) {
			if (v1.hashCode() < v2.hashCode()) {
				this.v1 = v1;
				this.v2 = v2;
			} else {
				this.v1 = v2;
				this.v2 = v1;
			}
			this.weight = weight;
		}

		public DisplayVertex getVertex1() {
			return v1;
		}

		public DisplayVertex getVertex2() {
			return v2;
		}

		public int getWeight() {
			return weight;
		}

		public int compareTo(DisplayEdge other) {

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

		public DisplayVertex getOtherVertex(DisplayVertex refVertex) {
			if (!(refVertex.equals(v1) || refVertex.equals(v2)))
				return null;

			return (refVertex.equals(v1)) ? v2 : v1;
		}

		@Override
		public boolean equals(Object obj) {

			if (obj == this)
				return true;
			if (!(obj instanceof DisplayEdge)) {
				return false;
			}
			DisplayEdge edge = (DisplayEdge) obj;
			return (Objects.equals(v1, edge.v1) && Objects.equals(v2, edge.v2))
					|| (Objects.equals(v1, edge.v2)
							&& Objects.equals(v2, edge.v1));
		}

		@Override
		public String toString() {
			return (v1.toString() + "," + v2.toString() + "," + weight);
		}

		public String toStringAdjListForm(DisplayVertex refVertex) {
			return (getOtherVertex(refVertex)).toString() + ", " + weight;
		}

	}

}
//