package Tree;

import java.util.LinkedList;
import java.util.List;

public class SumRootToLeafNumbers {
    private int sum = 0;
    public int sumNumbers(TreeNode root) {
        if (root == null) {
            return 0;
        }
        List<Integer> path = new LinkedList<>();
        path.add(root.val);
        sumHelper(root, path);
        return sum;
    }
    private void sumHelper(TreeNode root,List<Integer> path) {
        if (root.left == null && root.right == null) {
        	System.out.println(x);
            int res = 0;
            for (int i : path) {
                res = res* 10 + i;
            }
            sum += res;
            return; 
        }
        
        if (root.left != null) {
            path.add(root.left.val);
            sumHelper(root.left, path);
            path.remove(path.size() - 1);
        }
        if (root.right != null) {
            path.add(root.right.val);
            sumHelper(root.right, path);
            path.remove(path.size() - 1);
        }
    }
	public static void main(String[] args) {
		SumRootToLeafNumbers s= new SumRootToLeafNumbers();
		TreeNode root = new TreeNode(9);
		s.sumNumbers(root);
	}
}
