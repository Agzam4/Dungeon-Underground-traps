package Game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class BufferedImageS implements Serializable {

	private static final long serialVersionUID = 1L;
	
	BufferedImage img;
	
	public BufferedImageS(BufferedImage img) {
		this.img = img;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
//		out.defaultWriteObject();
		ImageIO.write(img, "png", out);
		System.out.println("Writing img... " + this);
	}

	public BufferedImage getImg() {
		return img;
	}
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
//		in.defaultReadObject();
		img = ImageIO.read(in);
		System.out.println("Reading img... " + this);
	}
}
