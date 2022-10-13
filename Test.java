import java.util.Arrays;

public class Test {
    public static void main(String args[]) {
	int arr1[] = new int[10];
	int arr2[] = new int[20];
	
	randomFillArray(arr1, -10, 70);
	System.out.println("input array 1: " + Arrays.toString(arr1));

	randomFillArray(arr2, -10, 70);
	System.out.println("input array 2: " + Arrays.toString(arr2) +"\n");

	IntegerSet iS1 = new IntegerSet(arr1);
	IntegerSet iS2 = new IntegerSet(arr2);
	System.out.println("intset 1: " + iS1);
	System.out.println("intset 2: " + iS2 + "\n");
	
	System.out.println("set1 eliminated " + (arr1.length - iS1.magnitude()) + " duplicates from array 1");
	System.out.println("set2 eliminated " + (arr2.length - iS2.magnitude()) + " duplicates from array 2\n");
	
	if(iS1.contains(69))
		System.out.println("nice, set 1");
	
	if(iS2.contains(69))
		System.out.println("nice, set 2");
	
	int toCheck = -15;
	System.out.println(String.format("iS1.contains(%d): ", toCheck) + iS1.contains(toCheck));
	System.out.println("intersection " + iS1.intersection(iS2));
	System.out.println("union  " + iS1.union(iS2));
    }
    
    /**
     * Randomly fills the array with elements between min and max.
     * Example:
     * <pre>
     * 		arr[] = new int[5];
     * 		randomFillArray(arr, 5, 10);
     * 		Possible Result: arr[5, 6, 5, 7, 10]
     * </pre>
     * @param arr The array to fill with random values.
     * @param min The minimum random integer (inclusive)
     * @param max The maximum random integer (inclusive)
     */
    public static void randomFillArray(int arr[], int min, int max) {
		if (arr == null) {
			throw new IllegalArgumentException("Random Fill Array: The integer array must not be null");
		}
		int temp = min;
		min = Math.min(min, max);
		max = Math.max(max, temp);
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) ((Math.random() * ((max - min) + 1)) + min);
		}
    }
}


