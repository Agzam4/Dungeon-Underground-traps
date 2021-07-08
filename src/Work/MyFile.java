package Work;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Stages.GameStage;

public class MyFile {

	public static String[] packs = getPacks();
	public static BufferedImage[] packsIco = getPacksIco();
	public static String pack = null;
	
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
	private static String[] getPacks() {
		if(!new File(System.getProperty("user.dir") + "/data/packs").exists()) {
			try {
				createFolder("data/packs");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File[] fs = new File(System.getProperty("user.dir") + "/data/packs").listFiles();
		ArrayList<String> packsList = new ArrayList<String>();
		packsList.add(null);
		for (File f : fs) {
			if(f.isDirectory()) {
				packsList.add(f.getName());
			}
		}
		String[] s = new String[packsList.size()];
		for (int i = 0; i < s.length; i++) {
			s[i] = packsList.get(i);
		}
		return s;
	}
	
	private static BufferedImage[] getPacksIco() {
		if(!new File(System.getProperty("user.dir") + "/data/packs").exists()) {
			try {
				createFolder("data/packs");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File[] fs = new File(System.getProperty("user.dir") + "/data/packs").listFiles();
		ArrayList<String> packsList = new ArrayList<String>();
		for (File f : fs) {
			if(f.isDirectory()) {
				packsList.add(f.getName());
			}
		}
		BufferedImage[] icos = new BufferedImage[packsList.size()+1];
		
		BufferedImage tileset2 = readImageAsStream("tileset/Dungeon_Underground_traps");
		int w2 = tileset2.getWidth()/7;
		icos[0] = new BufferedImage(w2*2, w2*2, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = (Graphics2D) icos[0].getGraphics();
		g2.drawImage(tileset2.getSubimage(w2, 0, w2, w2), 0, 0, null); 
		g2.drawImage(tileset2.getSubimage(w2*6, 0, w2, w2), 0, w2, null);
		g2.drawImage(tileset2.getSubimage(w2, w2*4, w2, w2), w2, 0, null);
		g2.drawImage(tileset2.getSubimage(w2*2, 0, w2, w2), w2, w2, null);
		g2.dispose();
		
		for (int i = 1; i < icos.length; i++) {
			try {
				BufferedImage tileset = ImageIO.read(new File(System.getProperty("user.dir") + "\\data\\packs\\" + packsList.get(i-1) + "\\img\\tileset\\Dungeon_Underground_traps.png"));
				int w = tileset.getWidth()/7 ;
				icos[i] = new BufferedImage(w*2, w*2, BufferedImage.TYPE_INT_RGB);
				Graphics2D g = (Graphics2D) icos[i].getGraphics();
				g.drawImage(tileset.getSubimage(w, 0, w, w), 0, 0, null); 
				g.drawImage(tileset.getSubimage(w*6, 0, w, w), 0, w, null);
				g.drawImage(tileset.getSubimage(w, w*4, w, w), w, 0, null);
				g.drawImage(tileset.getSubimage(w*2, 0, w, w), w, w, null);
				g.dispose();
			} catch (IOException e) {
				icos[i] = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
			}
		}
		return icos;
	}
	
	private static void createFolder(String folder) throws IOException {
//		File file = new File(System.getProperty("user.dir")+ "\\data\\");
//		System.out.println(file.mkdir());
		System.out.println("Creating Directory: " + System.getProperty("user.dir") + "/" + folder);
			Files.createDirectory(Paths.get(System.getProperty("user.dir") + "/" + folder));
	}

	public static String readFileInResource(String name) {
		System.out.println("Reading File In Resource: " + name);

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				MyFile.class.getClass().getResourceAsStream(name)));
		
		String line;
		String txt = "";
		try {
			while ((line = reader.readLine()) != null) {
				txt += line + "\n";
			}
		} catch (IOException e) {
		}
		
		//	    data = MyFile.class.getResourceAsStream(name).
//		String string = "";
//		try {
//			byte[] all = Files.readAllBytes(
//					Paths.get(MyFile.class.getResourceAsStream(name).));//Paths.get(name)
//			string = new String(all);
//			return string;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return txt;
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
	
	public static String loadTextFile(String name){
		if(pack != null) {
			try {
				byte[] all;
				all = Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "\\data\\packs\\" + pack + "\\" + name));
				return new String(all);
			} catch (IOException e) {
			}
		}
		return readFileInResource("/" + name);
	}
	
	public static BufferedImage readImage(String name) {
		if(pack == null) {
			return readImageAsStream(name);
		}else {
			System.out.println(System.getProperty("user.dir") + "\\data\\packs\\" + pack + "\\img\\" + name + ".png");
			try {
				return ImageIO.read(new File(System.getProperty("user.dir") + "\\data\\packs\\" + pack + "\\img\\" + name + ".png"));
			} catch (IOException e) {
				System.err.println("Err of loading: " + name);
				return readImageAsStream(name);
			}
		}
	}
	
	private static BufferedImage readImageAsStream(String name) {
		try {
			System.out.println("Reading image as stream: " + "/img/" + name + ".png");
			return ImageIO.read(GameStage.class.getResourceAsStream("/img/" + name + ".png"));
		} catch (IOException e) {
			return new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		}
	}
	
	public static String getPackName() {
		return pack == null ? "Default": pack;
	}
	
	public static String getPackName(int id) {
		return packs[id] == null ? "Default": packs[id];
	}
}
