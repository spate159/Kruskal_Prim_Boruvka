public class QuickSort {

	private static Edge[] edges;
	public static Edge[] sort(Edge[] edges) {
		if (edges == null || edges.length == 0) {
			return null;
		}
		QuickSort.edges = edges;
		quickSort(edges, 0, edges.length - 1);
		return edges;
	}
	private static void quickSort(Edge[] edges, int low, int high) {

	

			int i = low, j = high;
		int pivot = edges[low + (high - low) / 2].getWeight();



			while (i <= j) {
				
			while (edges[i].getWeight() < pivot) {
					i++;

				}
			while (edges[j].getWeight() > pivot) { // s[j] > element
					j--;
				}

				if (i <= j) {
					// exchange
					Edge tempEdge = edges[i];
					edges[i] = edges[j];
					edges[j] = tempEdge;
					i++;
					j--;
				}
			}


			if (low < j)
				quickSort(edges, low, j);

			if (high > i)
				quickSort(edges, i, high);

	}
}