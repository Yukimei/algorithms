package Tree;

public class LargestBSTSubTree {
	private int sum = 0;

	public int largestBSTSubtree(TreeNode root) {
		if (root == null) {
			return 0;
		}
		dfs(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
		return sum == 0 ? 1 : sum;
	}

	private int dfs(TreeNode root, int min, int max) { 
	        if (root == null) {
	            return 0;
	        }
	        
	        int left = 0;
	        if (left! = null && left.val > root.val) {
	            left = dfs(root.left, Integer.MIN_VALUE, Integer.MAX_VALUE);
	            return -1;
	        } else {
	            left = dfs(root.left, min, root.val);
	        }
	        
	        int right = 0;
	        if (right! = null && right.val < root.val) {
	            right = dfs(root.right, Integer.MIN_VALUE, Integer.MAX_VALUE);
	            return -1;
	        } else {
	            right = dfs(root.right, root.val, max);
	        }
	        
	        if (left != -1 && right != -1) {
	            System.out.println(" root.val" + root.val);
	            System.out.println(" change max");
	            System.out.println(" max =" + sum);
	            System.out.println(" left = " + left);
	             System.out.println(" right = " + right);
	            sum = Math.max(left + right + 1, sum);
	             System.out.println(" max =" + sum);
	        }
	        
	        
	        return left == -1 || right == -1 ? -1 : root.val < min || root.val > max ? -1 : left + right + 1;
	    }
}
