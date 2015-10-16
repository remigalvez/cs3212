import edu.gwu.util.*;
import edu.gwu.debug.*;
import edu.gwu.algtest.*;
import java.lang.*;
import java.util.*;

public class SelfAdjTree implements TreeSearchAlgorithm {

	private final boolean DEBUG = false;
	private final boolean TESTING = false;
	private final boolean LOGGER = false;

	public TreeNode root = null;

	public int size = 0;
	public int maxSize = Integer.MAX_VALUE;

	public Object insert(Comparable key, Object value) {
		if (DEBUG) System.out.println("METHOD: " + getClassName() + "insert(" + key + ")");
		// If tree cannot store any more elements, return null
		if (size >= maxSize) {
			if (DEBUG) System.out.println("ERROR: Exceeds max size - insert(" + key + "," + value + ")");
			return null;
		}

		// Instantiate node that will navigate tree until 
		// the algorithm finds a place to insert new node
		TreeNode n = getRoot();

		// If tree is empty, insert node as root
		if (root == null) {
			root = new TreeNode(key, value);
			if (DEBUG) System.out.println("SUCCESS: Key inserted as root - insert(" + key + "," + value + ")");
			return value;
		}

		// While navigating tree
		while (n != null) {
			// If key already exists, replace current value with new value
			if (key.compareTo(n.key) == 0)
				n.value = value;
			// Navigate to left child if insertion key is smaller than active key
			else if (key.compareTo(n.key) < 0) {
				// If left child is null, set it to the new node
				if (n.left == null) {
					n.left = new TreeNode(key, value, null, null, n);
					size++;
					splayToRoot(n.left);
					if (DEBUG) System.out.println("SUCCESS: Key inserted as left child of " + n.key + " - insert(" + key + "," + value + ")");
					return value;
				}
				else
					n = n.left;
			// Navigate to right child if insertion key is greater than active key
			} else {
				// If right child is null, set it to the new node
				if (n.right == null) {
					n.right = new TreeNode(key, value, null, null, n);
					size++;
					splayToRoot(n.right);
					if (DEBUG) System.out.println("SUCCESS: Key inserted as right child of " + n.key + " - insert(" + key + "," + value + ")");
					return value;
				}
				else 
					n =	n.right;
			}
		}

		if (DEBUG) System.out.println("ERROR: Unknown Error - insert(" + key + ", " + value + ")");
		return value;
	}

	public ComparableKeyValuePair search(Comparable key) {
		if (DEBUG) System.out.println("METHOD: " + getClassName() + "search(" + key + ")");
		// Get root node as starting point for search
		TreeNode n = getRoot();

		// If tree is empty, return null
		if (n == null) {
			if (DEBUG) System.out.println("ERROR: Empty tree - search(" + key + ")");
			return null;
		}

		// Loop will break when key is found
		while (key.compareTo(n.key) != 0) {
			// If n.key < key, search left subtree
			if (key.compareTo(n.key) < 0) 
				n = n.left;
			// If n > key, search right subtree
			else 
				n = n.right;
			// If n is null, key does not exist in tree
			if (n == null) {
				if (DEBUG) System.out.println("ERROR: Key not found - search(" + key + ")");
				return null;
			}
		}

		splayToRoot(n);
		if (DEBUG) System.out.println("SUCCESS: Key found - search(" + key + ")");
		return n;
	}

	public ComparableKeyValuePair minimum() {
		if (DEBUG) System.out.println("METHOD: " + getClassName() + "minimum()");
		// Set active node to root
		TreeNode node = getRoot();
		// Iterate through tree until leftmost child is found
		while(node.left != null) {
			node = node.left;
		}
		return node;
	}

	public ComparableKeyValuePair maximum() {
		if (DEBUG) System.out.println("METHOD: " + getClassName() + "maximum()");
		// Set active node to root
		TreeNode node = getRoot();
		// Iterate through tree until rightmost child is found
		while(node.right != null) {
			node = node.right;
		}
		if (DEBUG) System.out.println("SUCCESS: Returning maximum (" + node.key + ") - maximum()");
		return node;
	}

	public Object delete(Comparable key) {
		if (DEBUG) System.out.println("METHOD: " + getClassName() + "delete(" + key + ")");
		// Get tree node to be deleted
		TreeNode node = (TreeNode) search(key);

		if (node == null) {
			if (DEBUG) System.out.println("ERROR: Key not found - delete(" + key + ")");
			return null;
		}
		// Instantiate the node that will navigate the tree to replace 
		// replace the node to be deleted
		TreeNode n = node;

		// Instantiate situation variable
		int status = 0;

		// Define node situation 
		if (hasLeft(node)) status = 1;
		else if (hasRight(node)) status = 2;
		else if (isLeaf(node)) status = 3;

		// Enter adequate case
		switch (status) {
			// If active node has a left child
			case 1:
				// Get predecessor
				n = n.left;
				while (hasRight(n)) 
					n = n.right;
				// If the predecessor is left child and does not have any right children
				if (isLeftChild(n)) {
					// Disown if leaf
					if (isLeaf(n)) 
						n.parent.left = null;
					else {
						// If parent has a right subtree, attach it to the node
						if (hasRight(n.parent)) {
							n.right = n.parent.right;
							n.right.parent = n;
						}
						// If parent node is not root, link node to grandparent
						if (n.parent.parent != null) {
							n.parent = n.parent.parent;
							n.parent.left = n;
						// Otherwise set root to node
						} else {
							n.parent = null;
							root = n;
						}
					}
				// If the predecessor is a right child and has left children
				} else if (hasLeft(n)) {
					// Attach left child as right child to parent node
					n.left.parent = n.parent;
					n.parent.right = n.left;
				// Otherwise, node is leaf, so disown node
				} else {
					n.parent.right = null;
				}
				// Replace key and value to delete by new key and value
				node.key = n.key;
				node.value = n.value;
				break;
			// If active node has right child
			case 2:
				n = n.right;
				while (hasLeft(n)) 
					n = n.left;
				// If the predecessor is right child and does not have any left children
				if (isRightChild(n)) {
					// Disown leaf
					if (isLeaf(n)) 
						n.parent.right = null;
					else {
						// If parent has a left subtree, attach it to the node
						if (hasLeft(n.parent)) {
							n.left = n.parent.left;
							n.left.parent = n;
						}
						// If parent node is not root, link node to grandparent
						if (n.parent.parent != null) {
							n.parent = n.parent.parent;
							n.parent.right = n;
						// Otherwise set root to node
						} else {
							n.parent = null;
							root = n;
						}
					}
				// If the predecessor is a left child and has right children
				} else if (hasRight(n)) {
					// Attach left child as right child to parent node
					n.right.parent = n.parent;
					n.parent.right = n.right;
				// Otherwise, node is leaf, so disown node
				} else {
					n.parent.left = null;
				}
				// Replace key and value to delete by new key and value
				node.key = n.key;
				node.value = n.value;
				break;
			// If active node is a leaf
			case 3:
				// Disown it
				if (isLeftChild(n)) 
					n.parent.left = null;
				else if (isRightChild(n)) 
					n.parent.right = null;
				// If leaf does not have any parents, tree is empty, so set root to null
				else 
					root = null;
				break;
			default:
				if (DEBUG) System.out.println("ERROR: Unknown error - delete(" + key + ")");
				break;
		}
		if (DEBUG) System.out.println("SUCCESS: Key deleted - delete(" + key + ")");
		return n.value;
	}

	public Comparable successor(Comparable key) { 
		if (DEBUG) System.out.println("METHOD: " + getClassName() + "successor(" + key + ")");
		// Get node from key
		TreeNode node = (TreeNode) search(key);
		TreeNode max = (TreeNode) maximum();

		// If current node is the maximum node, return null
		if (key.compareTo(max.key) == 0) {
			if (DEBUG) System.out.println("ERROR: Key is max - successor(" + key + ")");
 			return null;
		}

		// If current node has a right subtree, return the minimum 
		// of that subtree
		if (hasRight(node)) {
			node = node.right;
			while(hasLeft(node)) 
				node = node.left;
		// If current node is a left child, return it's parent node
		} else if (isLeftChild(node)) {
			node = node.parent;
		// Otherwise, navigate up the tree until the active node is a right
		// child, then return its parent
		} else {
			while(!isLeftChild(node)) 
				node = node.parent;
			node = node.parent;
		}
		if (DEBUG) System.out.println("SUCCESS: Successor found - successor(" + key + ")");
		return node.key;
	}

	public Comparable predecessor(Comparable key) { 
		if (DEBUG) System.out.println("METHOD: " + getClassName() + "predecessor(" + key + ")");
		// Get node from key
		TreeNode node = (TreeNode) search(key);
		TreeNode min = (TreeNode) minimum();

		// If current node is the minimum node, return null
		if (key.compareTo(min.key) == 0) {
			if (DEBUG) System.out.println("ERROR: Key is minimum - successor(" + key + ")");
			return null;
		}

		// If current node has a left subtree, return the maximum 
		// of that subtree
		if (hasLeft(node)) {
			node = node.left;
			while(hasRight(node)) 
				node = node.right;
		// If current node is a right child, return it's parent node
		} else if (isRightChild(node)) {
			node = node.parent;
		// Otherwise, navigate up the tree until the active node is a right
		// child, then return its parent
		} else {
			while(!isRightChild(node)) 
				node = node.parent;
			node = node.parent;
		}
		if (DEBUG) System.out.println("SUCCESS: Predecessor found - successor(" + key + ")");
		return node.key;	
	}

	public TreeNode getRoot() {
		return root;
	}	

	public String getName() { return "remigalvez16 - BinarySearchTree"; }

	public void setPropertyExtractor(int algID, PropertyExtractor prop) {
		if (DEBUG) System.out.println("METHOD: " + getClassName() + "setPropertyExtractor(" + algID + "," + prop + ")");
		return;
	}

	public void initialize(int maxSize) {
		if (DEBUG) System.out.println("METHOD: " + getClassName() + "initialize(" + maxSize + ")");
		this.maxSize = maxSize;
		size = 0;
		root = null;
		System.gc();
		return;
	}

	public int getCurrentSize() { 
		if (DEBUG) System.out.println("METHOD: " + getClassName() + "getCurrentSize()");
		return size; 
	}

	public Enumeration getKeys() { 
		if (DEBUG) System.out.println("METHOD: " + getClassName() + "getKeys()");
		// Create subclass that implements Enumeration
		class TreeKeys implements Enumeration {
			// Set active node to minimum node
			TreeNode node = (TreeNode) minimum();
			// Keep track of index
			int index = 0;

			public boolean hasMoreElements() {
				if (DEBUG) System.out.println("METHOD: " + getClassName() + "getKeys().hasMoreElements()");
				// Get maximum node, and check if active node is equal to maximum node
				TreeNode max = (TreeNode) maximum();
				if (node == max) {
					// If they are equal, there are no more elements. Return false.
					if (DEBUG) System.out.println("SUCCESS: Does not have more elements - hasMoreElements()");
					return false;
				}
				// Otherwise, there are. Return true.
				if (DEBUG) System.out.println("SUCCESS: Has more elements - hasMoreElements()");
				return true;
			}

			public Object nextElement() {
				if (DEBUG) System.out.println("METHOD: " + getClassName() + "getKeys().nextElement()");
				// Check if tree has a next element
				if (!hasMoreElements()) {
					if (DEBUG) System.out.println("ERROR: Does not have more elements - getKeys().nextElement()");
					return null;
				}
				// If index is 0, return first element as "next" element
				if (index == 0) {
					index++;
					if (DEBUG) System.out.println("SUCCESS: Returning first element (" + node.key + "," + node.value + ") - getKeys().nextElement()");
					return node.value;
				}
				// Get successor key, and get succeding node
				Comparable key = successor(node.key);
				node = (TreeNode) search(key);
				index++;
				if (DEBUG) System.out.println("SUCCESS: Returning element #" + index + " (" + node.key + "," + node.value + ") - getKeys().nextElement()");
				return node.key;
			}

			public Enumeration getEnumeration() { 
				// Reset enumeration and return this
				if (DEBUG) System.out.println("METHOD: " + getClassName() + "getKeys().getEnumeration()");
				node = (TreeNode) minimum();
				index = 0;
				return this; 
			}

		}
		if (DEBUG) System.out.println("SUCCESS: Returning keys enumeration class - getKeys()");
		return new TreeKeys();
	}

	public Enumeration getValues() {
		if (DEBUG) System.out.println("METHOD: " + getClassName() + "getValues()");
		// Create subclass that implements Enumeration
		class TreeValues implements Enumeration {
			// Set active node to minimum node
			TreeNode node = (TreeNode) minimum();
			// Keep track of index
			int index = 0;

			public boolean hasMoreElements() {
				if (DEBUG) System.out.println("METHOD: " + getClassName() + "getValues().hasMoreElements()");
				// Get maximum node, and check if active node is equal to maximum node
				TreeNode max = (TreeNode) maximum();
				if (node == max) {
					// If they are equal, there are no more elements. Return false.
					if (DEBUG) System.out.println("SUCCESS: Does not have more elements - hasMoreElements()");
					return false;
				}
				// Otherwise, there are. Return true.
				if (DEBUG) System.out.println("SUCCESS: Has more elements - hasMoreElements()");
				return true;
			}

			public Object nextElement() {
				if (DEBUG) System.out.println("METHOD: " + getClassName() + "getKeys().nextElement()");
				// Check if tree has a next element
				if (!hasMoreElements()) {
					if (DEBUG) System.out.println("ERROR: Does not have more elements - getValues().nextElement()");
					return null;
				}
				// If index is 0, return first element as "next" element
				if (index == 0) {
					index++;
					if (DEBUG) System.out.println("SUCCESS: Returning first element (" + node.key + "," + node.value + ") - getValues().nextElement()");
					return node.value;
				}
				// Get successor key, and get succeding node
				Comparable key = successor(node.key);
				node = (TreeNode) search(key);
				index++;
				if (DEBUG) System.out.println("SUCCESS: Returning element #" + index + " (" + node.key + "," + node.value + ") - getValues().nextElement()");
				return node.value;
			}

			public Enumeration getEnumeration() { 
				// Reset enumeration and return this
				if (DEBUG) System.out.println("METHOD: " + getClassName() + "getValues().getEnumeration()");
				node = (TreeNode) minimum();
				return this; 
			}

		}
		if (DEBUG) System.out.println("SUCCESS: Returning values enumeration class - getValues()");
		return new TreeValues();
	}

	public void initialize() {
		if (DEBUG) System.out.println("METHOD: " + getClassName() + "initialize()");
		root = null;
		size = 0;
		System.gc();
		return;
	}

	public void splayToRoot(TreeNode node) {
		int status = 0;

		while (node != root) {
			if (node.parent == root) status = 1;
			else if (isLeftChild(node) && isLeftChild(node.parent)) status = 2;
			else if (isRightChild(node) && isRightChild(node.parent)) status = 3;
			else if (isRightChild(node) && isLeftChild(node.parent)) status = 4;
			else if (isLeftChild(node) && isRightChild(node.parent)) status = 5;

			switch (status) {
				case 1:
					if (isLeftChild(node)) {
						rotateRight(node);
					} else {
						rotateLeft(node);
					}
					break;
				case 2:
					rotateRight(node.parent);
					rotateRight(node);
					break;
				case 3: 
					rotateLeft(node.parent);
					rotateLeft(node);
					break;
				case 4:
					rotateLeft(node);
					rotateRight(node);
					break;
				case 5:
					rotateRight(node);
					rotateLeft(node);
					break;
				default:
					// Do nothing
					break;
			}
		}
		return;
	}


	public void rotateRight(TreeNode node) {
		TreeNode temp = null;
		TreeNode gp = null;
		TreeNode parentNode = node.parent;

		if (hasRight(node)) {  
			parentNode.left = node.right;
			node.right.parent = parentNode;
		} else {
			parentNode.left = null;
		}

		if (parentNode.parent != null) {
			gp = parentNode.parent;
			node.parent = parentNode.parent;
			if (isLeftChild(parentNode))
				gp.left = node;
			else 
				gp.right = node;
		} else {
			root = node;
			node.parent = null;
		}

		node.right = parentNode;
		parentNode.parent = node;

		return;
	}


	public void rotateLeft(TreeNode node) {
		TreeNode temp = null;
		TreeNode gp = null;
		TreeNode parentNode = node.parent;

		if (hasLeft(node)) {  
			parentNode.right = node.left;
			node.left.parent = parentNode;
		} else {
			parentNode.right = null;
		}

		if (parentNode.parent != null) {
			gp = parentNode.parent;
			node.parent = parentNode.parent;
			if (isLeftChild(parentNode))
				gp.left = node;
			else 
				gp.right = node;
		} else {
			root = node;
			node.parent = null;
		}

		node.left = parentNode;
		parentNode.parent = node;

		return;
	}


	// =======================
	// ==  My added methods ==
	// =======================


	public boolean isLeaf(TreeNode node) {
		if (node.left == null && node.right == null)
			return true;
		return false;
	}

	public boolean isLeftChild(TreeNode node) {
		if (node.parent == null)
			return false;
		else if (node.parent.left == node) 
			return true;
		return false;
	}

	public boolean isRightChild(TreeNode node) {
		if (node.parent == null)
			return false;
		else if (node.parent.right == node) 
			return true;
		return false;
	}

	public boolean hasLeft(TreeNode node) {
		if (node.left != null)
			return true;
		return false;
	}
	
	public boolean hasRight(TreeNode node) {
		if (node.right != null)
			return true;
		return false;
	}

	public void levelPrint() {
		Queue q = new LinkedList();
		TreeNode c = null;

		q.add(getRoot());
		q.add(null);

		while(!q.isEmpty()) {
			c = (TreeNode) q.poll();
			if (q.isEmpty()) break;

			if (c == null) {
				q.add(null);
				System.out.println();
			} else {
				if (hasLeft(c)) 
					q.add(c.left);
	 			if (hasRight(c))
					q.add(c.right);
				System.out.print(c.key + " ");
			}
		}
		return;
	}

	public void pLevelPrint() {
		Queue q = new LinkedList();
		TreeNode c = null;

		q.add(getRoot());
		q.add(null);

		boolean empty = false;

		while(!q.isEmpty()) {
			if (c == null) empty = true;
			c = (TreeNode) q.poll();
			if (q.isEmpty()) break;

			if (c == null) {
				if (empty) return;
				q.add(null);
				System.out.println();
			} else {
				empty = false;
				if (hasLeft(c)) 
					q.add(c.left);
	 			if (hasRight(c))
					q.add(c.right);
				System.out.print(c.key + " (");
				if (c.parent != null) 
					System.out.print("p:" + c.parent.key + ",");
				if (c.left != null) 
					System.out.print("l:" + c.left.key + ",");
				if (c.right != null)
					System.out.print("r:" + c.right.key);
				System.out.print(")\t");
			}
		}
		System.out.println("DONE");
		return;
	}

	public String computeActual() {
		Queue q = new LinkedList();
		TreeNode c = null;

		String result = "";

		q.add(getRoot());
		q.add(null);

		while(!q.isEmpty()) {
			c = (TreeNode) q.poll();
			if (q.isEmpty()) break;

			if (c == null) {
				q.add(null);
				result += "\n";
			} else {
				if (hasLeft(c)) 
					q.add(c.left);
	 			if (hasRight(c))
					q.add(c.right);
				result += c.key + " (";
				if (c.parent != null) 
					result += "p:" + c.parent.key + ",";
				if (c.left != null) 
					result += "l:" + c.left.key + ",";
				if (c.right != null)
					result += "r:" + c.right.key;
				result += ") ";
			}
		}
		return result;
	}

	private String getClassName() {
		return "SelfAdjTree.";
	}


	public static void main(String[] args) {

		SelfAdjTree t = new SelfAdjTree();

		t.testInsert();
		t.testSearch();
		t.testMinimum();
		t.testMaximum();
		t.testGetKeys();
		t.testGetValues();
		t.testDelete();

		// Successor and predecessor are tested in getKeys() and getValues()
		// Initalize is tested in testInsert()

	}

	private boolean testInsert() {
		if (TESTING) System.out.println("TEST: " + getClassName() + "testInsert: METHOD: Insertion - 10 elements");
		// Initialize tree
		if (LOGGER) System.out.println("TEST: " + getClassName() + "testInsert: LOGGER: Initializing tree");
		initialize(10);
		if (LOGGER) System.out.println("TEST: " + getClassName() + "testInsert: LOGGER: Inserting 10 elements");
		// Insert elements
		insert(5, 5);
		insert(8, 8);
		insert(3, 3);
		insert(4, 4);
		insert(7, 7);
		insert(1, 1);
		insert(9, 9);
		insert(2, 2);
		insert(6, 6);
		insert(0, 0);

		// Compute expected result by hand
		String expected = "0 (r:6) \n6 (p:0,l:1,r:8) \n1 (p:6,r:2) 8 (p:6,l:7,r:9) \n2 (p:1,r:3) 7 (p:8,) 9 (p:8,) \n3 (p:2,r:5) \n5 (p:3,l:4,) \n4 (p:5,) ";
		// Compute actual result
		String actual = computeActual();

		if (LOGGER) System.out.println("TEST: " + getClassName() + "testInsert: LOGGER: Expected: ");
		if (LOGGER) System.out.println(expected);
		if (LOGGER) System.out.println("TEST: " + getClassName() + "testInsert: LOGGER: Actual: ");
		if (LOGGER) System.out.println(actual);

		// If expected matches actual, test passes
		if (expected.equals(actual)) {
			if (TESTING) System.out.println("PASSED: " + getClassName() + "testInsert()");
			return true;
		}
		// Else, test fails
		if (TESTING) System.out.println("FAILED: " + getClassName() + "testInsert()");
		return false;
		
	}

	private boolean testSearch() { 
		if (TESTING) System.out.println("TEST: " + getClassName() + "testSearch: METHOD: Search - All elements + 5 non-existing elements");

		TreeNode searched;

		if (LOGGER) System.out.println("TEST: " + getClassName() + "testSearch: LOGGER: Inserting elements 1 through 10");
		for (int i = 0; i < 10; i++) {
			searched = (TreeNode) search(i);
			if (searched.key.compareTo(i) != 0) {
				if (TESTING) System.out.println("FAILED: " + getClassName() + "testSearch: Could not find an existing element " + i);
				return false;
			} else 
				if (LOGGER) System.out.println("TEST: " + getClassName() + "testSearch: LOGGER: Found element " + i);
		}

		for (int i = 20; i < 25; i++) {
			searched = (TreeNode) search(i);
			if (searched != null) {
				if (TESTING) System.out.println("FAILED: " + getClassName() + "testSearch: Found non-existing element " + i);
				return false;
			} else 
				if (LOGGER) System.out.println("TEST: " + getClassName() + "testSearch: LOGGER: Could not find non-existing element " + i);
		}

		if (TESTING) System.out.println("PASSED: " + getClassName() + "testSearch()");
		return true;
	}

	private boolean testMinimum() { 
		if (TESTING) System.out.println("TEST: " + getClassName() + "testMinimum: METHOD: Minimum - Check minimum");

		Comparable minKey = minimum().key;

		if (minKey.compareTo(0) == 0) {
			if (TESTING) System.out.println("PASSED: " + getClassName() + "testMinimum()");
			return true;
		}
		if (TESTING) System.out.println("FAILED: " + getClassName() + "testMinimum()");
		return false; 
	}

	private boolean testMaximum() { 
		if (TESTING) System.out.println("TEST: " + getClassName() + "testMinimum: METHOD: Minimum - Check minimum");

		Comparable maxKey = maximum().key;

		if (maxKey.compareTo(9) == 0) {
			if (TESTING) System.out.println("PASSED: " + getClassName() + "testMaximum()");
			return true;
		}
		if (TESTING) System.out.println("FAILED: " + getClassName() + "testMaximum()");
		return false; 
	}

	private boolean testDelete() { 
		if (TESTING) System.out.println("TEST: " + getClassName() + "testDelete: METHOD: Delete - All elements one by one");

		int[] order = { 3, 6, 2, 5, 12, 8, 9, 1, 0, 4, 7 };

		for (int i : order) {
			if (LOGGER) System.out.println("TEST: " + getClassName() + "testDelete: LOGGER: Deleting element " + i);
			assert(search(i) != null);
			delete(i);
			if (search(i) != null) {
				if (TESTING) System.out.println("FAILED: " + getClassName() + "testDelete: Found element " + i + "after deletion");
				return false;
			}
			if (LOGGER) pLevelPrint();
		}
		if (TESTING) System.out.println("PASSED: " + getClassName() + "testDelete()");
		return true; 
	}

	private boolean testGetKeys() { 
		if (TESTING) System.out.println("TEST: " + getClassName() + "testGetKeys: METHOD: GetKeys - Test methods \n\tgetEnumeration, hasMoreElements(), and nextElement()");

		Enumeration e = getKeys();
		int key;

		for (int i = 0; i < 15; i++) {
			if (LOGGER) System.out.println("TEST: " + getClassName() + "testGetKeys: LOGGER: Checking if enum has more elements.");
			if (e.hasMoreElements()) {
				if (i != (int) e.nextElement()) {
					if (TESTING) System.out.println("FAILED: " + getClassName() + "testGetKeys()");
					return false; 
				}
			}
		}

		if (TESTING) System.out.println("PASSED: " + getClassName() + "testKeys()");
		return true;
	}

	private boolean testGetValues() { 
		if (TESTING) System.out.println("TEST: " + getClassName() + "testGetKeys: METHOD: GetValues - Test methods \n\tgetEnumeration, hasMoreElements(), and nextElement()");

		Enumeration e = getValues();

		for (int i = 0; i < 15; i++) {
			if (LOGGER) System.out.println("TEST: " + getClassName() + "testGetKeys: LOGGER: Checking if enum has more elements.");
			if (e.hasMoreElements()) {
				if (i != (int) e.nextElement()) {
					if (TESTING) System.out.println("FAILED: " + getClassName() + "testGetValues()");
					return false; 
				}
			}
		}

		if (TESTING) System.out.println("PASSED: " + getClassName() + "testGetValues()");
		return true;
	}


}
