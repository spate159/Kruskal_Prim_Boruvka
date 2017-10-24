import java.util.Arrays;
import java.util.List;

public class MinHeap<T extends Comparable<T> & Indexer> {

	private T[] priorityQueue;
	private int lastIndex;
	private int queueSize;

	public MinHeap(int size) {

		this.queueSize = size + 1;// not storing the value at index 0
		priorityQueue = (T[]) new Comparable[queueSize];
		lastIndex = 0;
	}

	/**
	 * 
	 * @param index
	 *            index of the element to be bubbled down.
	 */
	private void minHeapify(int index) {
		
		while (leftChildIndex(index) <= lastIndex) {

			int smallestChildIndex = leftChildIndex(index);
			if (rightChildIndex(index) <= lastIndex) {
				// if right child exist and is smaller than left child
				if (rightChild(index).compareTo(leftChild(index)) < 0) {
					smallestChildIndex = rightChildIndex(index);
				}
			}
			if (priorityQueue[smallestChildIndex]
					.compareTo(priorityQueue[index]) < 0) {
				exchange(smallestChildIndex, index);
				index = smallestChildIndex;
			} else {
				break;
			}
		}
	}

	protected void resize(int resizeValue) {
		if (resizeValue > priorityQueue.length)
			priorityQueue = Arrays.copyOf(priorityQueue,
					resizeValue + 1);
		else// just do the double
			priorityQueue = Arrays.copyOf(priorityQueue,
					priorityQueue.length * 2);
	}

	/**
	 * 
	 * @return the minimum element from the heap. Then minimum element is
	 *         removed from the heap.
	 */
	public T extractMin() {

		T min = min();
		// priorityQueue[1] = priorityQueue[lastIndex];// last element brought
		// to
													// first

		pQueueSet(priorityQueue[lastIndex], 1);

		priorityQueue[lastIndex] = null;
		lastIndex--;

		if (lastIndex > 1) {
			minHeapify(1);
		}
		// adjust left
		return min;
	}

	/**
	 * 
	 * @return the minimum element. Null if heap is empty
	 */
	public T min() {
		if (priorityQueue.length == 0) {
			throw new IllegalStateException(
					"please initialize the heap size appropriately");
		}
		return priorityQueue[1];
	}

	/**
	 * 
	 * @param t
	 *            the item to be inserted
	 * @return if the item is inserted or not. Item is not inserted if heap is
	 *         full. resize is required... Auto resize not implemented
	 */
	public boolean insert(T t) {
		lastIndex++;
		// System.out.println("MinHeap.insert() last index = " + lastIndex
		// + " and size = " + queueSize);
		if (lastIndex >= queueSize) {
			System.err.println(
					"heap overflow not adding elements further please resize");
			lastIndex--;
			return false;
		} else {

//			priorityQueue[lastIndex] = t;
//			priorityQueue[lastIndex].setIndex(lastIndex);
			pQueueSet(t, lastIndex);
			decreaseKey(lastIndex);
			return true;
		}

	}

	/**
	 * decrease key or bubble up
	 * 
	 * @param index
	 *            index of the element on which decrease key needs to be
	 *            performed
	 * @return whether key was decreased or not
	 */
	public boolean decreaseKey(int index) {
		boolean updated = false;
// parent greater than child
		while (index > 1 && parent(index).compareTo(priorityQueue[index]) > 0) {
			exchange(parentIndex(index), index);
			index = parentIndex(index);
			updated = true;
		}
		if (updated)
			return true;
		else
			return false;
	}

	private int parentIndex(int index) {
		return index / 2;
	}

	private int rightChildIndex(int i) {
		return i * 2 + 1;
	}

	private int leftChildIndex(int i) {
		return i * 2;
	}

	private T rightChild(int i) {
		return priorityQueue[i * 2 + 1];
	}

	private T leftChild(int i) {
		return (priorityQueue[i * 2]);
	}

	private boolean hasLeftChild(int i) {
		if (leftChildIndex(i) <= lastIndex
				&& priorityQueue[leftChildIndex(i)] != null)
			return true;
		else
			return false;
	}

	private boolean hasRightChild(int i) {
		if (rightChildIndex(i) <= lastIndex
				&& priorityQueue[rightChildIndex(i)] != null)
			return true;
		else
			return false;
	}

	private T parent(int i) {
		// i/2 is index of parent in priority queue
		return priorityQueue[i / 2];

	}

	private void exchange(int index1, int index2) {
		T temp = priorityQueue[index1];
		priorityQueue[index1] = priorityQueue[index2];
		priorityQueue[index2] = temp;

		// update the listener about the indexchange
		priorityQueue[index1].setIndex(index1);
		priorityQueue[index2].setIndex(index2);
	}

	private void pQueueSet(T t, int index) {
		
		priorityQueue[index] = t;
		if (t != null)
		priorityQueue[index].setIndex(index);
	}

	/**
	 * 
	 * @return number of element in the heap
	 */
	public int headCount() {
		return lastIndex;
	}

	// Testing left
	private void buildHeap() {

		for (int i = parentIndex(lastIndex); i >= 1; i--) {
			minHeapify(i);
		}
	}

	public MinHeap(List<T> items) {

		this.queueSize = items.size() + 1;// not storing the value at index 0
		priorityQueue = (T[]) new Comparable[queueSize];
		lastIndex = 0;
		int i = 1;
		for (T t : items) {
			pQueueSet(t, i);
			i++;
			lastIndex++;
		}
		buildHeap();
	}
}
