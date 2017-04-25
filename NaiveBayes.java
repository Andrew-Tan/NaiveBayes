import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;

public class NaiveBayes {

	//	This function reads in a file and returns a 
	//	set of all the tokens. It ignores the subject line
	//
	//	If the email had the following content:
	//
	//	Subject: Get rid of your student loans
	//	Hi there ,
	//	If you work for us, we will give you money
	//	to repay your student loans . You will be 
	//	debt free !
	//	FakePerson_22393
	//
	//	This function would return to you
	//	[hi, be, student, for, your, rid, we, get, of, free, if, you, us, give, !, repay, will, loans, work, fakeperson_22393, ,, ., money, there, to, debt]
	public static HashSet<String> tokenSet(File filename) throws IOException {
		HashSet<String> tokens = new HashSet<String>();
		Scanner filescan = new Scanner(filename);
		filescan.next(); //Ignoring "Subject"
		while(filescan.hasNextLine() && filescan.hasNext()) {
			tokens.add(filescan.next());
		}
		filescan.close();
		return tokens;
	}

	private static final Path training_spam_dir = FileSystems.getDefault().getPath("data", "train", "spam");
	private static final Path training_ham_dir = FileSystems.getDefault().getPath("data", "train", "ham");
	private static final Path tests_dir = FileSystems.getDefault().getPath("data", "test");

	private HashMap<String, Double> ham;
	private HashMap<String, Double> spam;
	private double hamCount;
	private double spamCount;
	private double p_s;
	private double p_h;

	public NaiveBayes() {
		File[] training_spam_files = training_spam_dir.toFile().listFiles();
		File[] training_ham_files = training_ham_dir.toFile().listFiles();
		if (training_spam_files == null)
			throw new IllegalArgumentException("Spam training files not exists: " + training_spam_dir.toAbsolutePath());
		if (training_ham_files == null)
			throw new IllegalArgumentException("Spam training files not exists: " + training_ham_dir.toAbsolutePath());

		this.spamCount = training_spam_files.length;
		this.hamCount = training_ham_files.length;
		this.p_s = this.spamCount / (this.spamCount + this.hamCount);
		this.p_h = this.hamCount / (this.spamCount + this.hamCount);

		this.ham = new HashMap<>();
		this.spam = new HashMap<>();
		buildMap(training_spam_files, this.spam, this.spamCount);
		buildMap(training_ham_files, this.ham, this.hamCount);
	}

	private void buildMap(File[] fileList, HashMap<String, Double> map, double denominator) {
		HashSet<String> wordAppearance;
		int i = 0;
		try {
			for (; i < fileList.length; i++) {
				wordAppearance = tokenSet(fileList[i]);
				for (String word : wordAppearance) {
					if (map.containsKey(word)) {
						map.put(word, map.get(word) + 1.0);
					} else {
						map.put(word, 1.0);
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("IO Exception occurred when handling file: " + fileList[i].getAbsolutePath());
		}
	}

	public boolean isSpam(File fileToScan) {
		if (fileToScan == null) {
			throw new IllegalArgumentException("NULL file pointer is given");
		}

		HashSet<String> words;
		try {
			words = tokenSet(fileToScan);
		} catch (IOException e) {
			throw new RuntimeException("IO Exception occurred when handling file: " + fileToScan.getAbsolutePath());
		}

		double probability_spam = Math.log10(this.p_s);
		double probability_ham = Math.log10(this.p_h);
		double spamCount;
		double hamCount;
		boolean containsInSpam;
		boolean containsInHam;
		for (String word : words) {
			containsInSpam = this.spam.containsKey(word);
			containsInHam = this.ham.containsKey(word);
			if ((!containsInSpam) && (!containsInHam)) {
				continue;
			}

			if (containsInSpam) {
				spamCount = this.spam.get(word);
			} else {
				spamCount = 1.0;
			}
			probability_spam += Math.log10((spamCount + 1.0) / (this.spamCount + 2.0));

			if (containsInHam) {
				hamCount = this.ham.get(word);
			} else {
				hamCount = 1.0;
			}
			probability_ham += Math.log10((hamCount + 1.0) / (this.hamCount + 2.0));
		}

		return probability_spam > probability_ham;
	}
	
	public static void main(String[] args) throws IOException {
		NaiveBayes nb = new NaiveBayes();
		File[] test_files = tests_dir.toFile().listFiles();
		if (test_files == null)
			throw new IllegalArgumentException("Test files not exists: " + tests_dir.toAbsolutePath());

		Arrays.sort(test_files, new Comparator<File>() {
			public int compare(File f1, File f2) {
				try {
					int i1 = Integer.parseInt(f1.getName().split("\\.")[0]);
					int i2 = Integer.parseInt(f2.getName().split("\\.")[0]);
					return i1 - i2;
				} catch(NumberFormatException e) {
					return f1.getName().compareTo(f2.getName());
				}
			}
		});

		for (File cur : test_files) {
			System.out.print(cur.getName() + ' ');
			if (nb.isSpam(cur)) {
				System.out.print("spam");
			} else {
				System.out.print("ham");
			}
			System.out.println();
		}
	}
}
