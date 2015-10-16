public void delete(Comparable key) {
	// Get tree node to be deleted
	TreeNode node = (TreeNode) search(key);
	// Instantiate the node that will navigate the tree to replace 
	// replace the node to be deleted
	TreeNode n = node;

	// Instantiate situation variable
	TreeNode status = 0;

	// Define node situation 
	if (hasLeft(node)) status = 1;
	else if (hasRight(node)) status = 2;
	else if (isLeaf(node)) status = 3;

	// Enter adequate case
	switch (status) {
		case 1:
			n = n.left;
			while (hasRight(n)) 
				n = n.right;
			if (hasLeft(n)) {
				n.left.parent = n.parent;
				n.parent.right = n.left;
			}
			node.key = n.key;
			node.value = n.value;
			break;
		case 2:
			n = n.right;
			while (hasLeft(n)) 
				n = n.left;
			if (hasRight(n)) {
				n.right.parent = n.parent;
				n.parent.left = n.right;
			}
			node.key = n.key;
			node.value = n.value;
			break;
		case 3:
			if (isLeftChild(n)) 
				n.parent.left = null;
			else 
				n.parent.right = null;
			break;
		default:
			n.key = null;
			n.value = null;
			break;
	}
}