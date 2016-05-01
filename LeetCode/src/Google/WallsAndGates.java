package Google;

import java.util.Deque;
import java.util.LinkedList;

public class WallsAndGates {
	private int[][] dir = { { -1, 0 }, { 0, -1 }, { 1, 0 }, { 0, 1 } };

	public void wallsAndGates(int[][] rooms) {
		if (rooms == null || rooms.length == 0 || rooms[0].length == 0) {
			return;
		}
		for (int i = 0; i < rooms.length; i++) {
			for (int j = 0; j < rooms[0].length; j++) {
				if (rooms[i][j] == 0) {
					bfs(rooms, i, j);
				}
			}
		}
	}

	private void bfs(int[][] a, int i, int j) {
		Deque<int[]> queue = new LinkedList<>();
		queue.offer(new int[] { i, j });
		while (!queue.isEmpty()) {
			int[] cur = queue.pollFirst();
			int dist = a[cur[0]][cur[1]] + 1;
			for (int[] d : dir) {
				int x = cur[0] + d[0];
				int y = cur[1] + d[1];

				if (isValid(a, x, y, dist)) {

					a[x][y] = dist;
					queue.offer(new int[] { x, y });
				}
			}
		}
	}

	private boolean isValid(int[][] a, int x, int y, int dist) {
		return x >= 0 && x < a.length && y >= 0 && y < a[0].length && a[x][y] != -1 && a[x][y] > dist;
	}
}