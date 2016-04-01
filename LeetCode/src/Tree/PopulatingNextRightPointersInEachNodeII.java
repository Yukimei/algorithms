package Tree;

import java.util.Deque;
import java.util.LinkedList;

import Tree.PopulatingNextRightPointersInEachNode.TreeLinkNode;

public class PopulatingNextRightPointersInEachNodeII {
	public void connect(TreeLinkNode root) {
		if (root == null) {
			return;
		}
		// do bfs to add right pointer
		Deque<TreeLinkNode> queue = new LinkedList<>();
		queue.offer(root);
		while (!queue.isEmpty()) {
			int size = queue.size();
			TreeLinkNode pre;
			pre = queue.poll();
			enque(pre, queue);
			size--;
			for (int i = 0; i < size; i++) {
				TreeLinkNode cur = queue.poll();
				enque(cur, queue);
				pre.next = cur;
				pre = cur;
			}
			pre.next = null;
		}
	}

	private void enque(TreeLinkNode node, Deque<TreeLinkNode> queue) {
		if (node.left != null) {
			queue.offer(node.left);
		}
		if (node.right != null) {
			queue.offer(node.right);
		}
	}
}
