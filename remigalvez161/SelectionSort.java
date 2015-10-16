import edu.gwu.util.*;
import edu.gwu.debug.*;
import edu.gwu.algtest.*;

public class SelectionSort implements SortingAlgorithm {

    public static final boolean TESTING = false;

    public SelectionSort() {

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

        int minIndex = 0;
        int min = Integer.MAX_VALUE;

        for (int i = 0; i < data.length; i++) {
            min = i;
            for (int j = i; j < data.length; j++) {
                if (data[j] < data[min])
                    min = j;
            }
            if (i != min)
                swap(data, i, min);
        }
     }

    public void swap(int[] data, int i, int min) {
            int temp = data[i];
            data[i] = data[min];
            data[min] = temp;
    }

    public void sortInPlace(java.lang.Comparable[] data) { }
    public int[] createSortIndex(int[] data) { return null; }
    public int[] createSortIndex(java.lang.Comparable[] data) { return null; }
    public java.lang.String getName() { return "remigalvez16 - SelectionSort"; }
    public void setPropertyExtractor(int algID, edu.gwu.util.PropertyExtractor prop) { }

	
	public static void main(String[] args) {
		// Testing
        SelectionSort sorter = new SelectionSort();
	}

    public static void printArray(int[] arr) {

        System.out.print("[ ");
        for (int i = 0; i < arr.length-1; i++) {
            System.out.print(arr[i] + ", ");
        }
        System.out.println(arr[arr.length-1] + " ]");
    }

}
