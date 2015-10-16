

public class LevelPrinter {



	public void levelPrint(BinarySearchTree t) {
		Stack stack = new Stack();

		stack.push(t.root);

		while(!stack.isEmpty()) {
			current = stack.pop();

			stack.push(current.right);
			stack.push(current.left);

			print(current.data);
		}
	}

}