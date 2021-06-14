package Work;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;

public class MyFile {
	
	public static void checkSavesFolder() {
		System.out.println("Checking \"saves\" folder");
		if(!new File(System.getProperty("user.dir") + "/data/").exists()) {
			try {
				createFolder("data");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(!new File(System.getProperty("user.dir") + "/data/saves").exists()) {
			try {
				createFolder("data/saves");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private static void createFolder(String folder) throws IOException {
//		File file = new File(System.getProperty("user.dir")+ "\\data\\");
//		System.out.println(file.mkdir());
		System.out.println("Creating Directory: " + System.getProperty("user.dir") + "/" + folder);
			Files.createDirectory(Paths.get(System.getProperty("user.dir") + "/" + folder));
	}

	public static String readFileInResource(String name) {
		String string = "";
		try {
			byte[] all = Files.readAllBytes(
					Paths.get(MyFile.class.getResource(name).toURI()));//Paths.get(name)
			string = new String(all);
			return string;
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String readFile(String name) {
		String string = "";
		try {
			byte[] all = Files.readAllBytes(Paths.get(name));
			string = new String(all);
			return string;
		} catch (IOException e) {
			try {
				writeFile(name, "");
			} catch (IOException e1) {
			}
		}
		return null;
	}
	
	public static void writeFile(String filename, String text) throws IOException {
		try (FileWriter writer = new FileWriter(new File(System.getProperty("user.dir") + "/" + filename))) {
			writer.write(text);
			writer.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
}
