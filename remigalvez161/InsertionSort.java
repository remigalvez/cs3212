import edu.gwu.util.*;
import edu.gwu.debug.*;
import edu.gwu.algtest.*;

public class InsertionSort implements SortingAlgorithm {

	public static final boolean TESTING = false;

	public InsertionSort() {

		if (TESTING) {

            int[] arr1 = { 6, 2, 7, 4, 3, 5, 1, 7, 1, 8, 5, 6, 9, 8, 9, 3, 2, 4 };
            int[] arr2 = { 1, 3, 5, 4, 2, 7, 6, 9, 8 };
            int[] arr3 = { 15, 25, 30, 5, 0, 10, 20 };


            System.out.print("arr1 - Given: \t\t"); printArray(arr1);
            System.out.println("arr1 - Expected: \t[ 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9 ]");
            sortInPlace(arr1);
            System.out.print("arr1 - Result: \t\t"); printArray(arr1);
            System.out.println();

            System.out.print("arr2 - Given: \t\t"); printArray(arr2);
            System.out.println("arr2 - Expected: \t[ 1, 2, 3, 4, 5, 6, 7, 8, 9 ]");
            sortInPlace(arr2);
            System.out.print("arr2 - Result: \t\t"); printArray(arr2);
            System.out.println();

            System.out.print("arr3 - Given: \t\t"); printArray(arr3);
            System.out.println("arr3 - Expected: \t[ 0, 5, 10, 15, 20, 25, 30 ]");
            sortInPlace(arr3);
            System.out.print("arr3 - Result: \t\t"); printArray(arr3);
            System.out.println();
        }

	}

	public void sortInPlace(int[] data) { 
		int j = 0;
		int val = 0;

		for (int i = 1; i < data.length; i++) {
			if (data[i] < data[i-1]) {
				val = data[i];
				j = i;
				while (j > 0 && val < data[j-1]) {
					data[j] = data[j-1];
					j--;
				}
				data[j] = val;
			}
		}
	}

	public void sortInPlace(java.lang.Comparable[] data) { }
	public int[] createSortIndex(int[] data) { return null; }
	public int[] createSortIndex(java.lang.Comparable[] data) { return null; }
	public java.lang.String getName() { return "remigalvez16 - InsertionSort"; }
	public void setPropertyExtractor(int algID, edu.gwu.util.PropertyExtractor prop) { }


	public static void main(String[] args) {
		// Testing
		InsertionSort sorter = new InsertionSort();
	}

	public static void printArray(int[] arr) {

        System.out.print("[ ");
        for (int i = 0; i < arr.length-1; i++) {
            System.out.print(arr[i] + ", ");
        }
        System.out.println(arr[arr.length-1] + " ]");
    }

}

