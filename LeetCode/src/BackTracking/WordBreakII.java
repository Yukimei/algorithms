package BackTracking;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WordBreakII {
	public List<String> wordBreak(String s, Set<String> wordDict) {
        List<String> res = new ArrayList<>();
        if (s == null) {
            return res;
        }
        dfs(s, 0, 0, s.length(), "", wordDict, res);
        return res;
    }

	private void dfs(String s, int from, int to, int end, String prev, Set<String> set, List<String> res) {
		if (to == end) {
			res.add(prev);
			return;
		}
		String scur = s.substring(from, to + 1);
		if (set.contains(scur)) {
			if (prev.length() != 0) {
				prev += " ";
			}
			dfs(s, to + 1, to + 1, end, prev + scur, set, res);
		}
		dfs(s, from, to + 1, end, prev, set, res);
	}
}
