package Tree;

import java.util.Deque;
import java.util.LinkedList;

public class PopulatingNextRightPointersInEachNode {
	class TreeLinkNode {
		int val;
		TreeLinkNode left, right, next;

		TreeLinkNode(int x) {
			val = x;
		}
	}
	// a faster method
    public void connect(TreeLinkNode root) {
        TreeLinkNode cur = root;
        while(cur != null && cur.left != null) {
            cur.left.next = cur.right;
            TreeLinkNode temp = cur;
            while (temp.next != null) {
                temp.right.next = temp.next.left;
                temp = temp.next;
                temp.left.next = temp.right;
            }
            cur = cur.left;
        }
    }
	public void connect2(TreeLinkNode root) {
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
