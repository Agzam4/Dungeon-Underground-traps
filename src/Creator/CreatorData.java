package Creator;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import Work.Loader;
import Work.MyFile;

public class CreatorData {

	// Main data
	public static BufferedImage[] imgs = new BufferedImage[Images.values().length];

	public static Color[] colors = new Color[4];

	public static Color[] borderColor = new Color[4];
	public static double borderSize = 0;
	public static int borderType = -1;
	
	// Enums	
	public static enum Images {

		Dungeon_Underground_traps 	("tileset"),
		player 						("tileset"),
		dart 						("tileset"),
		achievement_bg 				("ico"),
		achievement_fg 				("ico"),
		achievement_img 			("ico"),
		achievement_stage			("bg");

		String folder;

		Images(String folder) {
			this.folder = folder;
		}

		public String getPath() {
			return "/" + folder + "/" + name() + ".png";
		}
		public String getFolder() {
			return folder;
		}

		public static int indexOf(String value) {
			for (int i = 0; i < values().length; i++) {
				if(values()[i].toString().equals(value))
					return i;
			}
			return -1;
		}
	}

	public static enum Colors {
		GameBackground,
		TextBackground,
		TextForeground,
		TextForeground_dark,
		TextForeground_dark_dark;
	}
	
	
	
	
	
	public static void setImg(BufferedImage img, String c) {
		int index = Images.indexOf(c);
		if(index == -1) 
			return;
		setImg(img, index);
	}

	public static void setImg(BufferedImage img, int id) {
		imgs[id] = img;
	}

	public static void export() {
		JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) { 
	    	try {
				export(chooser.getSelectedFile());
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Err: " + e.getMessage());
			}
	    }
	}
	
	static String notuse = "\\/:*?\"<>|";
	
	public static void export(File f) throws IOException {
		if(f.isDirectory()) {
			if(f.getName().equals("data")) {
				f = new File(f.getPath() + "\\" + "packs");
			}
			if(f.getName().equals("packs")) {
				String name = JOptionPane.showInputDialog("Name: ");
				if(name == null)
					return;
				if(name.isEmpty())
					return;
				String name2 = "";
				for (char c : name.toCharArray()) {
					if(notuse.indexOf(c) == -1) {
						name2 += c;
					}
				}
				System.out.println(": " + name2);
				if(name2.isEmpty()) {
					name2 = "Unnamed_" + System.nanoTime();
				}
				String path = (f.getPath() + "/" + name2);
				Files.createDirectory(Paths.get(path));

				/**
				 *  Save Images
				 */
				Files.createDirectory(Paths.get(path+"\\img\\"));
				Files.createDirectory(Paths.get(path+"\\img\\tileset"));
				Files.createDirectory(Paths.get(path+"\\img\\ico"));
				Files.createDirectory(Paths.get(path+"\\img\\bg"));
				for (int i = 0; i < imgs.length; i++) {
					if(imgs[i] != null)
						ImageIO.write(imgs[i], "png", new File(path + "\\img\\" + Images.values()[i].getPath()));
				}

				/**
				 *  Save Colors
				 */
				Files.createDirectory(Paths.get(path+"\\colors"));
				String colorString = "";
				for (int i = 0; i < colors.length; i++) {
					if(colors[i] != null)
					colorString += Integer.toHexString(colors[i].getRGB()).toUpperCase().substring(2) + "\n";
					else {
						colorString += "-\n";
					}
				}
				writeFile(path+"\\colors\\colors.colors", colorString);

				/**
				 *  Save Border
				 */
				String borderString = borderType + " " + borderSize;
				for (int i = 0; i < colors.length; i++) {
					borderString += "&";
					if(colors[i] != null) {
						borderString += Integer.toHexString(colors[i].getRGB()).toUpperCase().substring(2) + "\n";
					}else {
						borderString += "-";
					}
				}
				writeFile(path+"\\border.border", borderString);
				return;
			}
		}
		JOptionPane.showMessageDialog(null, f.getPath() + " - is not \"packs\" or \"data\" folder", "", JOptionPane.ERROR_MESSAGE);
//		try {
////			StringBuilder sb = new StringBuilder();
////			sb.append("Test String");
//			ZipOutputStream out;
//			out = new ZipOutputStream(new FileOutputStream(f));
//			
////			out.putNextEntry(e);
//			byte[] data = new byte[1024];//sb.toString().getBytes();
//			out.write(data, 0, data.length);
//			out.closeEntry();
//			out.close();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
	}
	
	public static void writeFile(String fn, String text) throws IOException {
		try (FileWriter writer = new FileWriter(fn)) {
			writer.write(text);
			writer.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
