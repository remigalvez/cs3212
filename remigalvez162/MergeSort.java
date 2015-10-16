import java.util.*;

import edu.gwu.util.*;
import edu.gwu.debug.*;
import edu.gwu.algtest.*;

public class MergeSort implements SortingAlgorithm {
	

	public void sortInPlace(int[] data) {
		int breadth = 2;
		int b = breadth;
		int length = data.length;
		int mid;
		
		while (breadth < 2*length) {
			mid = breadth / 2;
			breadth = breadth <= length ? breadth : length;

			for (int i = 0; i < length; i += breadth) {
				b = breadth < data.length - i ? breadth : data.length - i;
				merge(data, i, i + mid, b);
			}
			breadth *= 2;
		}
	}

	public void merge(int[] arr, int idx, int mid, int breadth) {
		if (mid >= arr.length) return;

		int l = idx; 
		int r = mid;
		int b = breadth;

		int[] mergedArray = new int[breadth];

		for (int i = 0; i < mergedArray.length; i++) {

			if (r >= idx + breadth || r >= arr.length) {
				mergedArray[i] = arr[l];
				l++;
				continue;
			} else if (l >= mid) {
				mergedArray[i] = arr[r];
				r++;
				continue;
			} 


			if (arr[l] <= arr[r]) {
				mergedArray[i] = arr[l];
				l++;
			} else if (arr[l] > arr[r]) {
				mergedArray[i] = arr[r];
				r++;
			}

		}

		for (int i = 0; i < breadth; i++) {
			arr[i+idx] = mergedArray[i];
		}
	}

	public static void main(String[] args) {

		MergeSort sorter = new MergeSort();

		boolean passed = sorter.test(true);

		if (passed) {
			System.out.println("TESTS PASSED");
		} else {
			System.out.println("TESTING FAILED");
		}

	}

	public boolean test(boolean logger) {
		int[] test10 = this.generateArray(15);
		int[] compare10 = this.duplicateArray(test10);

		int[] test100 = this.generateArray(100);
		int[] compare100 = this.duplicateArray(test100);

		this.sortInPlace(test10);
		boolean equal10 = this.compare(test10, compare10);
		boolean sorted10 = this.testSorted(test10);
		// this.print(test10); 

		this.sortInPlace(test100);
		boolean equal100 = this.compare(test100, compare10);
		boolean sorted100 = this.testSorted(test100);
		// this.print(test100);

		if (logger) {
			System.out.printf("No lost elements (length: 10): %b\n", equal10);
			System.out.printf("Array is sorted (10): %b\n", sorted10);
			System.out.printf("No lost elements (length: 100): %b\n", equal100);
			System.out.printf("Array is sorted (100): %b\n", sorted100);
		}

		System.out.println(equal10);
		System.out.println(sorted10);
		System.out.println(equal100);
		System.out.println(sorted100);

		if (!equal10 || !sorted10 || !equal100 || !sorted100) 
			return true;
		
		return false; 
	}

	public boolean testSorted(int[] arr) {
		for (int i = 0; i < arr.length - 1; i++) {
			if (arr[i] > arr[i+1]) 
				return false;
			return true;
		}
		return false;
	}

	public boolean compare(int[] arr1, int[] arr2) {
		for (int num : arr1) {
			boolean found = false;
			for (int i = 0; i < arr2.length; i++) {
				if (num == arr2[i]) {
					arr2[i] = 200;
					found = true;
					break;
				}
			}
			if (!found) return false;
		}
		return true;
	}

	public int[] generateArray(int size) {
		int[] arr = new int[size];

		Random rand = new Random();

		for (int i = 0; i < size; i++) {
			arr[i] = rand.nextInt(30);
		}

		return arr;
	}

	public int[] duplicateArray(int[] arr) {
		int[] duplicate = new int[arr.length];

		for (int i = 0; i < arr.length; i++) 
			duplicate[i] = arr[i];

		return duplicate;
	}

	public void print(int[] arr) {
		System.out.print("[ ");
		for (int i : arr) {
			System.out.print(i + " ");
		}
		System.out.println("]");
	}

	public void print(int[] arr, int l, int r) {
		System.out.print("[ ");
		for (int i = 0; i < arr.length; i++) {
			if (i == l || i == r) {
				System.out.print("[" + arr[i] + "] ");
				continue;
			}
			System.out.print(arr[i] + " ");
		}
		System.out.println("]");
	}

	public void sortInPlace(java.lang.Comparable[] data) {}
	public int[] createSortIndex(int[] data) { return null; }
	public int[] createSortIndex(java.lang.Comparable[] data) { return null; }
	public java.lang.String getName() { return ""; }
	public void setPropertyExtractor(int algID, edu.gwu.util.PropertyExtractor prop) {}

}