Algorithm: makeFilterTree(rectSet)
	Input: Set of rectangles containing n rectangles

	root = createNode(0,100);
	For r in rectSet
		makeFilterTreeRecursive(r, root);


Algorithm: makeFilterTreeRecursive(r, quadrantNode)
	Input: Single rectangle to be placed in tree and current iteration node

	// Compute appropriate subquadrant as an int to place node
	quadrant = computeQuadrant(r, quadrantNode)

	// If rectangle intersects with several quadrants
	if quadrant == -1
		// Add rectangle to parent input node
		add r to quadrantNode
		return

	// If smaller subquadrant doesn't exist
	if quadrantNode[quadrant] is null
		// Create subquadrant
		Add new node to quadrantNode.quadrants[quadrant]

	// Call recursive function with new quadrant
	4.	makeFilterTreeRecursive(r, quadrantNode.quadrants[quadrant])


Algorithm: computeQuadrant(r, quadrantNode)
	Input: Single rectangle and parent node
	// Determine which half (horizontally) holds the top left corner's x coordinate
	if (r.topLeft.x < quadrantNode.midX) 
		x = 0
	else 
		x = 1
	// Determine which half (vertically) holds the top left corner's y coordinate
	if (r.topLeft.y < quadrantNode.midY)
		y = 0
	else 
		y = 1
	// Compute subquadrant for bottom right corner
	if (x == 0 && y == 0) topLeftQuad = 2
	else if (x == 0 && y == 1) topLeftQuad = 1
	else if (x == 1 && y == 0) topLeftQuad = 3
	else topLeftQuad = 0
	// Determine which half (horizontally) holds the bottom right corner's x coordinate
	if (r.bottomRight.x < quadrantNode.midX) 
		x = 0
	else 
		x = 1
	// Determine which half (vertically) holds the bottom right corner's y coordinate
	if (r.bottomRight.y < quadrantNode.midY)
		y = 0
	else 
		y = 1
	// Compute subquadrant for bottom right corner
	if (x == 0 && y == 0) bottomRightQuad = 2
	else if (x == 0 && y == 1) bottomRightQuad = 1
	else if (x == 1 && y == 0) bottomRightQuad = 3
	else bottomRightQuad = 0
	// If both corners are in the same subquadrant, return that subquadrant's index
	if (topLeftQuad == bottomRightQuad)
		return topLeftQuad
	// Otherwise return -1
	return -1;

Algorithm: filterTreeSearch(rect)
	Input: Single rectangle
	// Call recursive function starting with root node
	filterTreeSearchRecursive(rect, root)


Algorithm: filterTreeSearchRecursive(rect, node)
	Input: 
	// Compute subquadrant
	quadrant = computeQuadrant(rect, node)
	// If rect overlaps on two subquadrants
	if (quadrant == -1)
		// Search node for rect
		returnNode = search node.rectList for rect
		// If node is not found, return null
		if (returnNode == null)
			return null
		// Else, return the node
		else
			return returnNode

	// Recursive call into appropriate subquadrant
	filterTreeSearchRecursive(rect, node.quadrants[quadrant])











