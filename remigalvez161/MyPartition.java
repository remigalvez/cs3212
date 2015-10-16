import java.util.*;
import edu.gwu.algtest.PartitionAlgorithm;

public class MyPartition implements PartitionAlgorithm {

	public static final boolean TESTING = false;

	public MyPartition() {

		if (TESTING) {

            int[] arr1 = { 6, 2, 7, 4, 3, 5, 1, 7, 1, 8, 5, 6, 9, 8, 9, 3, 2, 4 };
            int[] arr2 = { 1, 3, 5, 4, 2, 7, 6, 9, 8 };
            int[] arr3 = { 15, 25, 30, 5, 0, 10, 20 };


            System.out.print("arr1 - Given: \t\t"); printArray(arr1); System.out.println();
            System.out.println("arr1 - Expected: \t[ _, _, _, _, _, _, _, _, _, _, _, 6, _, _, _, _, _, _ ] | Index: 11");
            int idx1 = leftIncreasingPartition(arr1, 0, arr1.length-1);
            System.out.print("arr1 - Result: \t\t"); printArray(arr1); System.out.println(" | Index: " + idx1);
            System.out.println();

            System.out.print("arr2 - Given: \t\t"); printArray(arr2); System.out.println();
            System.out.println("arr2 - Expected: \t[ 1, _, _, _, _, _, _, _, _ ] | Index: 0");
            int idx2 = leftIncreasingPartition(arr2, 0, arr2.length-1);
            System.out.print("arr2 - Result: \t\t"); printArray(arr2); System.out.println(" | Index: " + idx2);
            System.out.println();

            System.out.print("arr3 - Given: \t\t"); printArray(arr3); System.out.println();
            System.out.println("arr3 - Expected: \t[ _, _, __, 15, __, __, __ ] | Index: 3");
            int idx3 = leftIncreasingPartition(arr3, 0, arr3.length-1);
            System.out.print("arr3 - Result: \t\t"); printArray(arr3); System.out.println(" | Index: " + idx3);
            System.out.println();
        }

	}

	public int leftIncreasingPartition(int[] data, int left, int right) {

		int originalLeft = left;
		int val = data[left];

		while (left < right) {
		
			while (data[left] <= val && left < right) {
				left++;
			}

			while (data[right] > val && right > left) {
				right--;
			}

			swap(data, left, right);
		}

		left--;

		swap(data, originalLeft, left);

		return left;

	}

	public void swap(int[] data, int idx1, int idx2) {
            int temp = data[idx1];
            data[idx1] = data[idx2];
            data[idx2] = temp;
    }

	public int rightIncreasingPartition(int[] data, int left, int right) { return 0; }
	public java.lang.String getName() { return "remigalvez16 - InsertionSort"; }
	public void setPropertyExtractor(int algID, edu.gwu.util.PropertyExtractor prop) { }

	public static void main(String[] args) {
		// Testing
		MyPartition sorter = new MyPartition();
	}

	public static void printArray(int[] arr) {

        System.out.print("[ ");
        for (int i = 0; i < arr.length-1; i++) {
            System.out.print(arr[i] + ", ");
        }
        System.out.print(arr[arr.length-1] + " ]");
    }

}
