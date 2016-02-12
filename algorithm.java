Algorithm: minimumSemesters(adjMatrix)
	iter = 0
	newQueue = {}

	// add all elements to queue of remaining elements in graph
	for i = 0:n
		q.add(i)
	endfor 

	// queue of all remaining elements in graph
	while q is not empty
		// clear new queue
		newQueue.clear
		// iterate through columns of remaining nodes in adjacency matrix
		for j in q
			// iterate through rows of remaining nodes in adjacency matrix
			for i in q
				// if node j has an incoming directed edge
				if adjMatrix[i][j]
					// add j to a new queue
					newQueue.add(j)
					break;
				endif
			endfor
		endfor

		// increment while loop iterations count (ie. semester count)
		iter = iter + 1
		// replace queue by new queue
		q = newQueue
	endwhile

	// return iteration count
	return iter
end