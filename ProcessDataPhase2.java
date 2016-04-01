import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessDataPhase2 {
	 public static final String SPLIT = "%#\\$@%#";
	 public static final String SEPERATOR = "@#@%%@";
	private void project1(String fileName) {
		try {
			// Construct BufferedReader from InputStreamReader
			File file = new File("./" + fileName);
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));

			String line = null;
			String pre = null;
			StringBuilder sb = new StringBuilder();
			if ((line = br.readLine()) != null) {
				String[] words = line.split("\t");
				pre = words[0];
				sb.append(line);
			}

			while ((line = br.readLine()) != null) {
				String[] words = line.split("\t");
				if (pre.equals(words[0])) {
					sb.append(SPLIT).append(words[1]);
				} else {
					System.out.println(sb.toString());
					sb = new StringBuilder();
					pre = words[0];
					sb.append(line);
				}
			}
			
			System.out.println(sb.toString());

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ProcessDataPhase2 p = new ProcessDataPhase2();
		p.project1("output2");
	}
}
