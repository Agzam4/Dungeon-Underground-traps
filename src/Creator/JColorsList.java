package Creator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public class JColorsList extends JPanel {

	EmptyBorder eb = new EmptyBorder(3, 15, 3, 15);
	
	JLabel ls[];
	Color cs[];
	
	int index = 0;
	int type;
	ColorSelect colorSelected;

	public JColorsList(String[] model, ColorSelect colorSelected, int type) {
		this.colorSelected = colorSelected;
		this.type = type;
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		ls = new JLabel[model.length];
		cs = new Color[model.length];
		for (int i = 0; i < model.length; i++) {
			int ii = i;
			cs[i] = Color.DARK_GRAY;
			ls[i] = new JLabel(model[i]);
			ls[i].setName(model[i]);
			ls[i].setOpaque(true);
			ls[i].setFocusable(true);
			ls[i].setBorder(eb);
			ls[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, ls[i].getFont().getSize() + 10));
			ls[i].setBackground(cs[i]);
			ls[i].setForeground(getNextColor(cs[i]));
			ls[i].addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent arg0) {
				}
				
				@Override
				public void mousePressed(MouseEvent arg0) {
					setIndex(ii);
				}
				
				@Override
				public void mouseExited(MouseEvent arg0) {
				}
				
				@Override
				public void mouseEntered(MouseEvent arg0) {
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
				}
			});
			add(ls[i]);
		}
		setColor(0, Color.DARK_GRAY);
	}
	
	public void indexChange(int last, int ind) {
		if(colorSelected != null) {
			setColor(last, colorSelected.getColor());
			colorSelected.setColor(cs[ind]);
			ls[last].setText(ls[last].getName() + "  ");
			ls[ind].setText("[" + ls[ind].getName() + "]");

//			ls[last].setOpaque(true);
//			ls[ind].setOpaque(false);

			ls[ind].setBorder(new CompoundBorder(new LineBorder(cs[ind], 3), eb));
			setNormal(last);
			setHighlight(ind);
		}
	};
	
	public void setNormal(int index) {
		ls[index].setBorder(eb);
		ls[index].setBackground(cs[index]);
		ls[index].setForeground(getNextColor(cs[index]));
	}
	
	public void setHighlight(int i) {
		ls[i].setBorder(new CompoundBorder(new LineBorder(cs[i], 3), eb));
		ls[i].setBackground(SystemColor.textHighlight);
		ls[i].setForeground(SystemColor.textHighlightText);
	}
	
	public void setIndex(int index) {
		indexChange(this.index, index);
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
	
	public Color getColor() {
		return cs[index];
	}

	public void setColor(int i, Color color) {
//		for (JLabel l : ls) {
//			if(l != null) {
//				l.setBorder(eb);
//				l.setOpaque(false);
//			}
//		}
		cs[i] = color;
		setHighlight(i);
		if(type == 0) JPackCreator.updateColorsBoxs(cs);		
		if(type == 1) JPackCreator.updateBorder(cs);
	}
	
	public static Color getNextColor(Color c) {
		int gray = getNextInt((c.getRed() + c.getGreen() + c.getBlue())/3);
		return new Color(gray,gray,gray);
	}
	
	public static int getNextInt(int i) {
		return (i + 75)%255;
	}

	public Color getColor(int ind) {
		return cs[ind];
	}
	
	public void setColorSelected(ColorSelect colorSelected) {
		this.colorSelected = colorSelected;
	}
}
