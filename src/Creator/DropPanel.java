package Creator;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.TooManyListenersException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class DropPanel extends JLabel {

	private static final long serialVersionUID = 1L;

	public static final int TYPE_IMG = 0;
	public static final int TYPE_MUSIC = 1;
	String formats[] = {"mp3","wav"};
	public static final int TYPE_TEXT = 2;
	
	int type;
	BufferedImage img;
	String txt = "Drop File here";
	
	public DropPanel(int type, String id) {
		this.type = type;
		setText(txt);
		setOpaque(false);
		DropTarget dropPanel = new DropTarget();
		try {
			dropPanel.addDropTargetListener(new DropTargetListener() {

				@Override
				public void dropActionChanged(DropTargetDragEvent e) {
				}

				@Override
				public void drop(DropTargetDropEvent e) {  
					Transferable t = e.getTransferable();
					try {
						DataFlavor[] dataFlavors = t.getTransferDataFlavors();
						e.acceptDrop(DnDConstants.ACTION_COPY);
						File file = new File(t.getTransferData(dataFlavors[0]).toString().replace("[", "").replace("]", ""));
						System.out.println(file);
						img = ImageIO.read(file);
						int tilesize7 = img.getWidth()/7;
						if(img.getHeight() < tilesize7*5 && id.equals("Dungeon_Underground_traps")) {
							JOptionPane.showMessageDialog(null, "The height must be more than " + img.getWidth());
						}else if (img.getHeight() < img.getWidth() && id.equals("achievement_img")) {
							JOptionPane.showMessageDialog(null, "The height must be <Width>");
						}else {
							setText(file.getName());
							setIcon(new ImageIcon(img));
						}
						CreatorData.setImg(img, id);
						JPackCreator.updateImgBoxs();
						e.dropComplete(true);
					} catch (Exception ex) {
					}
				}

				@Override
				public void dragOver(DropTargetDragEvent e) {
				}

				@Override
				public void dragExit(DropTargetEvent e) {
				}

				@Override
				public void dragEnter(DropTargetDragEvent e) {
				}
			});
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}
		setDropTarget(dropPanel);
	}
	
	public void setText2(String arg0) {
		super.setText(arg0 + ": " + txt);
	}
}
