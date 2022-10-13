import java.util.ArrayList;

public class IntegerSet
{
	private class IntNode
	{
		IntNode[] digits = new IntNode[10];		// next digit in integer sequence, 0-9 by index
		boolean isEnd = false; 					// allows flattening of digit trees, 1 and 12 can coexist peacefully
	}
	
	private IntNode treeRoot = new IntNode();	// one tree for -/+ by order-preserving signed->unsigned mapping
	
	private ArrayList<Integer> setImpl = new ArrayList<Integer>();			// only used for dynamic adding during traversal
	
	private void insertRecurse(String num, IntNode curNode)
	{
		IntNode next = curNode.digits[num.charAt(0) - 48];					// mapping ASCII 0-9 (48-57) to actual 0-9 index
		if(next == null)
			next = curNode.digits[num.charAt(0) - 48] = new IntNode();		// must check if exists, otherwise inserting 125 will cut 121 off tree
		
		if(num.length() == 1)
			next.isEnd = true;
		else
			insertRecurse(num.substring(1), next);
	}
	
	private boolean checkRecurse(String num, IntNode curNode)
	{
		IntNode next = curNode.digits[num.charAt(0) - 48];
		
		if(next == null)
			return false;
		else if(next.isEnd && num.length() == 1)	// tricky tricky, contains(25)->true for set{ 2 } if we don't make sure we're on last digit of query
			return true;
		
		return checkRecurse(num.substring(1), next);
	}
	
	private void traverseTreeFillSetRecurse(IntNode curNode, String str)
	{
		for(int i = 0; i < curNode.digits.length; ++i)
			if(curNode.digits[i] != null)
			{
				if(curNode.digits[i].isEnd)
					setImpl.add(Integer.parseUnsignedInt(str + i) + Integer.MIN_VALUE); // o r d e r - p r e s e r v i n g s i g n e d -> u n s i g n e d m a p p i n g
				
				traverseTreeFillSetRecurse(curNode.digits[i], str + i);
			}
	}

    // The array that represents the set.
    private final int set[];

    /**
     * The constructor for IntegerSet. When an IntegerSet is created it must be
     * initialized with an integer array. The set will then pull out the duplicated
     * items and keep the unique integers.
     * 
     * @param arr
     *            The array to create the set from.
     */
    public IntegerSet(int arr[]) {
		if (arr == null) {
			throw new IllegalArgumentException("The array must not be null");
		}
		set = uniqueElements(arr);
    }

    /**
     * This is the size of the set which, in this case, is just the length of the
     * array.
     * 
     * @return The length of the set.
     */
    public int magnitude() {
		return set.length;
    }

    /**
     * This method is private and is used to help set up the set array. An integer
     * set is one in which the elements are unique (no duplicates) and are sorted.
     * 
     * @param arr
     *            The array that will be used to retrieve the unique elements from.
     * @return The new integer array that contains the unique elements from arr.
     */
    private int[] uniqueElements(int arr[])
    {
    	for(int i = 0; i < arr.length; ++i)
    		insertRecurse(Integer.toUnsignedString(arr[i] - Integer.MIN_VALUE), treeRoot); // o r d e r - p r e s e r v i n g s i g n e d -> u n s i g n e d m a p p i n g
    	
    	traverseTreeFillSetRecurse(treeRoot, "");
    	
		return setImpl.stream().mapToInt(i -> i).toArray();			// https://stackoverflow.com/a/23688547
    }

    /**
     * This method returns whether or not value is located in the set. If the value
     * is in the set then return true otherwise return false. <br />
     * Example:
     * <pre>
     * 		IntegerSet iS1 = new IntegerSet([1,2,3,4]); 
     * 		iS1.contains(3); //returns true
     * 		iS2.contains(6); //returns false
     * </pre>
     * 
     * @param value
     *            The integer to look for.
     * @return True if value is located in the set otherwise false.
     */
    public boolean contains(int value) {
    	return checkRecurse(Integer.toUnsignedString(value - Integer.MIN_VALUE), treeRoot); // o r d e r - p r e s e r v i n g s i g n e d -> u n s i g n e d m a p p i n g
    }

    /**
     * A union of two sets is a new set that contains all elements from both sets.
     * This method takes another set and unions it with the set that calls this
     * method. A new IntegerSet is returned that contains the union of both sets.<br />
     * Example:
     * <pre>
     * 		IntegerSet is1 = new IntegerSet([1, 2, 3, 4]); 
     * 		IntegerSet is2 = new IntegerSet([3, 4, 5, 6, 7, 8]);
     * 		is1.union(is2) //returns new IntegerSet([1, 2, 3, 4, 5, 6, 7, 8]);
     * </pre>
     * 
     * @param otherSet
     *            The set to be unioned with.
     * @return A new IntegerSet that is the union of the calling set with the
     *         otherSet.
     */
    public IntegerSet union(IntegerSet otherSet)
    {
    	int count = set.length + otherSet.set.length;
    	
    	int[] newArr = new int[count];
    	
    	for(int i = 0; i < set.length; ++i)
    		newArr[i] = set[i];
    	
    	for(int i = 0; i < otherSet.set.length; ++i)
    		newArr[set.length + i] = otherSet.set[i];
    	
		return new IntegerSet(newArr);
    }

    /**
     * The intersection of two sets is a new set that contains elements that occur
     * in both sets. This method takes another set and intersects it with the set
     * that calls this method. A new IntegerSet is returned that contains the
     * intersection of the two sets. <br />
     * Example:
     * <pre>
     * 		IntegerSet is1 = new IntegerSet([1,2,3,4]);
     * 		IntegerSet is2 = new IntegerSet([3,4,5]);
     * 		is1.intersection(is2) //returns new IntegerSet([3, 4]);
     * </pre>
     * 
     * @param otherSet
     *            The set to be intersected with.
     * @return A new IntegerSet that is the intersection of the calling set with the
     *         otherSet.
     */
    public IntegerSet intersection(IntegerSet otherSet)
    {
    	IntegerSet smaller, larger;
    	
    	if(set.length < otherSet.set.length)
    	{
    		smaller = this;
    		larger = otherSet;
    	}
    	else
    	{
    		smaller = otherSet;
    		larger = this;
    	}
    	
    	ArrayList<Integer> both = new ArrayList<Integer>();
    	
    	for(int i = 0; i < smaller.set.length; ++i)
    		if(larger.contains(smaller.set[i]))
    			both.add(smaller.set[i]);
    	
		return new IntegerSet(both.stream().mapToInt(i -> i).toArray());		// https://stackoverflow.com/a/23688547
    }

    /**
     * Returns a string representation of an IntegerSet type. The returned string
     * will have the following structure.
     * 
     * set{ elements in the set separated by a comma }.
     */
    public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("set{ ");
		for (int i = 0; i < set.length; i++)
		{
			sb.append(set[i]);
			
			if (i < set.length - 1)
				sb.append(", ");
		}
		sb.append(" }");
		return sb.toString();
    }
}
