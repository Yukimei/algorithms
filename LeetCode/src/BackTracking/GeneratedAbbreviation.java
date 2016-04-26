package BackTracking;

import java.util.ArrayList;
import java.util.List;

public class GeneratedAbbreviation {
    public List<String> generateAbbreviations(String word) {
        List<String> res = new ArrayList<>();
        if (word == null) {
            return res;
        }
        dfs(word, 0, "", res, 0);
        return res;
    }
    private void dfs(String word, int i, String sub, List<String> res, int count) {
        if (i == word.length()) {
            if (count != 0) {
                sub += String.valueOf(count);
            }
            res.add(sub);
            return;
        }
        char c = word.charAt(i);

            if (count != 0) {
                dfs(word, i + 1, sub + String.valueOf(count) + c, res, 0);
                
            } else if (count == 0) {
                dfs(word, i + 1, sub + c, res, count);
               
            }
             dfs(word, i + 1, sub, res, count + 1);
    
    }
}
