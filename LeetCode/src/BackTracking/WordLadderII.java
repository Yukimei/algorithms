package BackTracking;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class WordLadderII {
	public List<List<String>> findLadders(String beginWord, String endWord, Set<String> wordList) {
		List<List<String>> res = new ArrayList<>();
		if (beginWord == null || endWord == null || beginWord.length() != endWord.length() || wordList == null
				|| wordList.size() == 0) {
			return res;
		}
		helper(beginWord, endWord, wordList, res, new ArrayList<String>());
		return res;
	}

	private void helper(String begin, String end, Set<String> set, List<List<String>> res, List<String> list) {
	        if (set.size() != 1) {
	        	 Iterator<String> iter = set.iterator();
	        	 while (iter.hasNext()) {
	                String next = iter.next();
	                if (isLadder(begin, next)) {
	                    set.remove(next);
	                    list.add(next);
	                    findLadders(next, end, set);
	                    set.add(next);
	                    list.remove(list.size() - 1);
	                }
	            }
	        } else {
	            if (end.equals(set.iterator().next())) {
	                list.add(end);
	                res.add(new ArrayList<String>(list));
	                list.remove(list.size() - 1);
	            }
	        }
	    }

	private boolean isLadder(String s1, String s2) {
		boolean flag = true;
		if (s1.length() != s2.length()) {
			return false;
		}
		for (int i = 0; i < s1.length(); i++) {
			if (flag && s1.charAt(i) != s2.charAt(i)) {
				flag = false;
			} else if (s1.charAt(i) != s2.charAt(i)) {
				return false;
			}
		}
		return true;
	}
}
