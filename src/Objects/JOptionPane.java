package Objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;

import Main.GamePanel;
import Work.Loader;
import Work.MouseController;

public class JOptionPane {

	public static final int TYPE_C = 0;
	public static final int TYPE_INFO = 1;
	public static final int TYPE_INPUT = 2;
	
	private int x, y, w, h;
	private String text;
	private int type;
	
	Button ok = new Button("OK", 0, 0);

	public JOptionPane(int type, String text) {
		this.text = text;
		this.type = type;
		ok.chageSize = false;
	}
	
	public JOptionPane(int type, String text, int data) {
		this.text = text;
		this.type = type;
		this.data = data;
		ok.chageSize = false;
	}
	
	private void drawString(Graphics2D gf, String str, int x, int y, Color c1, Color c2) {
		gf.setColor(Loader.COLOR_TEXT_FG);
		gf.drawString(str, x, y);
		gf.setColor(Loader.COLOR_TEXT_BG);
		for (int xx = -1; xx < 2; xx++) {
			for (int yy = -1; yy <2; yy++) {
				gf.drawString(str, (int) (x+xx*GamePanel.scalefull/1.5), (int) (y+yy*GamePanel.scalefull/1.5));
			}
		}
	}
	
	int tw;

	String beforeSelected = "";
	String selectedString = "";
	String afterSelected = "";

	int selectedStart = 0;
	int selectedEnd = 0;
	
	int selectedTime = 0;

	int startMX = -1;
	int startMY = -1;
	
	public void draw(Graphics2D gf) {
		if(needClose)
			return;
		
		selectedTime++;

		gf.setFont(new Font("Comic Sans MS", Font.PLAIN, (int) (15*GamePanel.scalefull)));
		
		tw = gf.getFontMetrics().stringWidth(text)/2;
		
		drawString(gf, text, GamePanel.frameW/2 - tw, GamePanel.frameH/2, Color.WHITE, Color.BLACK);

		switch (type) {
		
		case TYPE_INFO:
			ok.draw(gf);
			break;

		case TYPE_INPUT:
			ok.draw(gf);
			gf.setFont(new Font("Comic Sans MS", Font.PLAIN, (int) (10*GamePanel.scalefull)));
			int tw = gf.getFontMetrics().stringWidth(input)/2;
			int textX = GamePanel.frameW/2 - tw;

			
			int rw = tw*2 < 50*GamePanel.scalefull ? (int)(50*GamePanel.scalefull) : tw*2;
			int rh = gf.getFont().getSize() + 3;
			int rx = (GamePanel.frameW - rw)/2;
			int ry = GamePanel.frameH/5*3 - gf.getFont().getSize();
			gf.setColor(Loader.COLOR_TEXT_FG);
			gf.drawRect(rx - 5, ry - 5,
					rw + 10,
					rh + 10);
			gf.setColor(Loader.COLOR_TEXT_FG2);
			gf.drawRect(rx - 5, ry - 4,
					rw + 9,
					rh + 9);
			
			updateSelected();
			
			
			if(MouseController.isMousePressed) {
				char[] cs = (input+" ").toCharArray();
				if(startMX == -1) {
					startMX = MouseController.getMousePointOnFrame().x;
					startMY = MouseController.getMousePointOnFrame().y;
					selected = selectedE = getClickedCharacterX(gf.getFontMetrics(), cs, textX, input);
				}else {
					startMX = MouseController.getMousePointOnFrame().x;
					startMY = MouseController.getMousePointOnFrame().y;
					selectedE = getClickedCharacterX(gf.getFontMetrics(), cs, textX, input);
					updateSelected();
				}
			}else {
				startMX = -1;
				startMY = -1;
			}
			
			int s1w = textX + gf.getFontMetrics().stringWidth(beforeSelected);
			
			boolean b = selectedString.length() == 0;
			drawString(gf, selectedString, s1w, ry + gf.getFont().getSize(),
					SystemColor.textHighlight, SystemColor.textHighlightText);
			gf.setColor(b ? SystemColor.textHighlightText : SystemColor.textHighlight);
			if(!b || (selectedTime/10)%2 == 0)
			gf.fillRect(s1w,
					ry,
					gf.getFontMetrics().stringWidth(selectedString) + 3,
					rh);
//			drawString(gf, " |", textX + , GamePanel.frameH/4*3);

			
			drawString(gf, input, textX, ry + gf.getFont().getSize(), Color.WHITE, Color.BLACK);

			gf.setColor(Loader.COLOR_GAME_BG);
			gf.fillRect(rx - 5, ry - 5,
					rw + 10,
					rh + 10);
			
			break;
			
		default:
			break;
		}
		
		gf.setColor(Loader.COLOR_TEXT_FG);
		gf.drawRect(x, y, w, h);
		gf.setColor(Loader.COLOR_TEXT_BG);
		gf.fillRect(x, y, w, h);
		
		gf.setColor(new Color(0,0,0,200));
		gf.fillRect(0, 0, GamePanel.frameW, GamePanel.frameH);
	}
	
	private int getClickedCharacterX(FontMetrics f, char[] cs, int textX, String strW) {
		int cx = textX;
		for (int i = 0; i < cs.length; i++) {
			int cw = f.charWidth(cs[i]);
			if(startMX > cx && !(startMX > cx + cw)) {
				return i + ((selected > i) ? 0 : 1);
			}
			cx += cw;
		}
		if(startMX < textX) {
			return 0;
		}
		return cs.length;
	}

	private void updateSelected() {
		if(selected < 0)
			selected = 0;
		if(selectedE < 0)
			selectedE = 0;
		if(selected > input.length())
			selected = input.length();
		if(selectedE > input.length())
			selectedE = input.length();
		selectedStart = selected < selectedE ? selected : selectedE;
		selectedEnd = selected > selectedE ? selected : selectedE;
		beforeSelected = input.substring(0, selectedStart);
		selectedString = input.substring(selectedStart, selectedEnd);
		afterSelected = input.substring(selectedEnd, input.length());		
	}

	public boolean needClose;
	public boolean isOKpressed;
	
	public void update() {
		w = (int) (GamePanel.frameW/3d*2d);
		h = (int) (GamePanel.frameH/4d*2d);
		if(tw > w/2) {
			w = (GamePanel.frameW-w)/2 + tw * 2;
		}
		x = (GamePanel.frameW-w)/2;
		y = (GamePanel.frameH-h)/2;
		
		switch (type) {
		case TYPE_INFO:
			ok.update();
			ok.setPosition(x+w/2, y+h/10*9);
			if(ok.isClicked()) {
				isOKpressed = needClose = true;
			}
			break;
		case TYPE_INPUT:
			ok.update();
			ok.setPosition(x+w/2, y+h/10*9);
			if(ok.isClicked()) {
				isOKpressed = needClose = true;
			}
			break;

		default:
			break;
		}
	}
	
	int data;
	
	public int getData() {
		return data;
	}

	String input = "";

	int selected = 0;
	int selectedE = 0;
	
	public void keyPressed(KeyEvent e) {
		if(type != TYPE_INPUT)
			return;
		if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			updateSelected();
			if(selectedE == selected) {
				if(beforeSelected.length() > 0) {
					input = beforeSelected.substring(0, beforeSelected.length()-1) + afterSelected;
					selectedE = selected = beforeSelected.length() - 1;
				}
			}else if(input.length() > 0) {
				input = beforeSelected + afterSelected;
				selectedE = selected = beforeSelected.length();
			}
			MouseController.isMousePressed = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			updateSelected();
			if(selectedE == selected) {
				if(afterSelected.length() > 0) {
					input = beforeSelected + afterSelected.substring(1, afterSelected.length());
					selectedE = selected = beforeSelected.length() + 1;
				}
			}else if(input.length() > 0) {
				input = beforeSelected + afterSelected;
				selectedE = selected = beforeSelected.length();
			}
			selectedE = selected;
			MouseController.isMousePressed = false;
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			ok.click();
		} else if (e.getKeyCode() == KeyEvent.VK_A && e.isControlDown()) {
			selected = 0;
			selectedE = input.length();
		} else if (e.getKeyCode() == KeyEvent.VK_C && e.isControlDown()) {
			updateSelected();
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			StringSelection selection = new StringSelection(selectedString);
			clipboard.setContents(selection, null);
		} else if (e.getKeyCode() == KeyEvent.VK_V && e.isControlDown()) {
			updateSelected();
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			DataFlavor flavor = DataFlavor.stringFlavor;
			if(clipboard.isDataFlavorAvailable(flavor)) {
				try {
					String paste = clipboard.getData(flavor).toString().replaceAll("\\n", "");
					input = beforeSelected + 
							paste + afterSelected;
					selectedE = selected = beforeSelected.length() + paste.length();
				} catch (UnsupportedFlavorException | IOException e1) {
				}
			}
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT && e.isShiftDown()) {
			selected++;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT && e.isShiftDown()) {
			selected--;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(selectedE == selected)
				selected++;
			selectedE = selected;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(selectedE == selected)
				selected--;
			selectedE = selected;
		} else if (e.getKeyCode() != KeyEvent.VK_ESCAPE && e.getKeyCode() != KeyEvent.VK_ENTER){
			if(Character.isLetterOrDigit(e.getKeyChar())  ||
					Character.isMirrored(e.getKeyChar())  ||
					Character.isWhitespace(e.getKeyChar()) ||
					Character.isDefined(e.getKeyChar())) {
				updateSelected();
				if(selectedE == selected) {
					input = beforeSelected + selectedString + e.getKeyChar() + afterSelected;
					selected++;
				}else{
					input = beforeSelected + e.getKeyChar() + afterSelected;
					selected = beforeSelected.length() + 1;
				}
				selectedE = selected;
				updateSelected();
			}
		}
	}
	
	public String getInput() {
		return input;
	}
}
