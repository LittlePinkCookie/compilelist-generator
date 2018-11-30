import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * CompileList Generator allows to automatically generate project's compile list
 * 
 * @author Loan Alouache
 * @version 1.0, 30/11/2018
 */

public class Generator {

	/**
	 * List of ignored directories
	 */
	private static ArrayList<String> ignoreList = new ArrayList<String>();

	/**
	 * List of java files
	 */
	private ArrayList<String> fileList;

	static {
		ignoreList.add(".git");
	}

	/**
	 * Create generator
	 */
	public Generator() {
		this.fileList = new ArrayList<String>();
	}

	/**
	 * Retrieve absolute path file list
	 * 
	 * @param directory - target directory
	 */
	public void getFiles(String directory) {
		File folder = new File(directory);
		File[] listOfFiles = folder.listFiles();

		try {
			for (File f : listOfFiles) {

				if (f.isFile() && isValidFileName(f.getName())) // if target is a java file
					this.fileList.add(f.getAbsolutePath());

				else if (f.isDirectory())
					getFiles(f.getAbsolutePath());

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERREUR : impossible de trouver le dossier");
		}

	}

	/**
	 * Check if the file name is valid
	 * 
	 * @param fileName - target file
	 * @return true if java file
	 */
	public boolean isValidFileName(String fileName) {
		return !Generator.ignoreList.contains(fileName) && fileName.split("\\.").length > 0
				&& fileName.split("\\.")[fileName.split("\\.").length - 1].equals("java");
	}

	/**
	 * Generate the compile.list file
	 */
	public void generateCompileList() {
		String s = "";

		// formatting file paths
		for (String file : this.fileList)
			s += file + "\n";

		// writting file
		try {
			FileWriter fw = new FileWriter("./compile.list");
			PrintWriter pw = new PrintWriter(fw);

			pw.write(s);

			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		// arguments check
		if (args.length == 0) {
			System.out.println("USAGE : java -jar CompileListGenerator rootProjectPath");
			System.exit(1);
		} else {
			// generating compile list
			Generator g = new Generator();
			g.getFiles(args[0]);
			g.generateCompileList();
		}
	}
}
