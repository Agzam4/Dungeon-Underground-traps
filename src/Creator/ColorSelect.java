package Creator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.sql.rowset.serial.SerialStruct;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


public abstract class ColorSelect extends JPanel {
	
	private static final long serialVersionUID = 1L;

	JPanel view = new JPanel();
	int r, g, b;
	
	JSlider rs, gs, bs;

	JSpinner rJSpinner = new JSpinner();
	JSpinner gJSpinner = new JSpinner();
	JSpinner bJSpinner = new JSpinner();
	
	JTextField hexField = new JTextField();
	
	public ColorSelect() {
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		view.setMinimumSize(new Dimension(25, 25));
		setColor();
		rs = new JSlider();
		rs.setMaximum(255);
		gs = new JSlider();
		gs.setMaximum(255);
		bs = new JSlider();
		bs.setMaximum(255);

		JLabel rl = new JLabel();
		JLabel gl = new JLabel();
		JLabel bl = new JLabel();

		rl.setName("R: ");
		gl.setName("G: ");
		bl.setName("B: ");
		
		add(view);
		add(rs);
		add(gs);
		add(bs);
		

		rJSpinner.setModel(new SpinnerNumberModel(0, 0, 255, 1));
		gJSpinner.setModel(new SpinnerNumberModel(0, 0, 255, 1));
		bJSpinner.setModel(new SpinnerNumberModel(0, 0, 255, 1));

		rs.setFocusable(false);
		gs.setFocusable(false);
		bs.setFocusable(false);
		
//		rs.addChangeListener(e -> {
//			r = rs.getValue();
//			rl.setText("R: " + r);
//			setColor();
//		});
//		gs.addChangeListener(e -> {
//			g = gs.getValue();
//			setColor();
//		});
//		bs.addChangeListener(e -> {
//			b = bs.getValue();
//			setColor();
//		});

		createPanel(this, rJSpinner, rs, rl, 0);
		createPanel(this, gJSpinner, gs, gl, 1);
		createPanel(this, bJSpinner, bs, bl, 2);
		

		JPanel hexJPanel = new JPanel();
		hexJPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
		hexJPanel.setLayout(new BoxLayout(hexJPanel, BoxLayout.X_AXIS));
		hexJPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(hexJPanel);
		
		JLabel hexJLabel = new JLabel("Hex: ");
		hexJPanel.add(hexJLabel);
		
		hexField.setMaximumSize(new Dimension(60, Integer.MAX_VALUE));
		hexJPanel.add(hexField);

		PlainDocument doc = (PlainDocument) hexField.getDocument();
		doc.setDocumentFilter(new HexFilter(hexField));
		
		hexField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					convertHex(hexField.getText());
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		});
		
		hexField.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				convertHex(hexField.getText());
				
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				convertHex(hexField.getText());
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				
			}
		});
	}
	
//	static final String hexCodes = "0123456789ABCDEF";
	
	private void convertHex(String hex) {
		if(hex.length() == 3) {
			String hex2 = "" + hex.charAt(0) + hex.charAt(0) + 
					hex.charAt(1) + hex.charAt(1) +
					hex.charAt(2) + hex.charAt(2);
			hex = hex2;
		}
		if(hex.length() == 6) {
			Color c = Color.decode("#" + hex);
			r = c.getRed();
			g = c.getGreen();
			b = c.getBlue();
			updateSS();
			setColor();
		}
	}
	
	private void createPanel(JPanel main, JSpinner spinner, JSlider slider, JLabel l, int id) {
		JPanel p = new JPanel();
		l.setHorizontalAlignment(JLabel.LEFT);
		l.setMinimumSize(new Dimension(l.getFontMetrics(l.getFont()).stringWidth("A:AAAAA"), Integer.MAX_VALUE));
		p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
		slider.setMaximumSize(new Dimension(100, 20));
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.setBorder(new EmptyBorder(5, 5, 5, 5));
		p.add(l);
		p.add(slider);
		p.add(spinner);
		

//		SimpleAttributeSet attributes = new SimpleAttributeSet();
//		StyleConstants.setSpaceAbove(attributes, l.getFontMetrics(l.getFont()).charWidth('0'));
//		int length = l.getDocument().getLength();
//		l.getStyledDocument().setParagraphAttributes(0, length, attributes, false);
		
		spinner.setMaximumSize(new Dimension(60, 20));
		
		slider.addChangeListener(e -> {
			setData(l, slider.getValue(), id);
			spinner.setValue(slider.getValue());
			updateHEX();
		});
		spinner.addChangeListener(e -> {
			setData(l, (Integer) spinner.getValue(), id);
			slider.setValue((Integer) spinner.getValue());
			updateHEX();
		});
		
		main.add(p);
		slider.setValue(0);
		setData(l, 0, id);
	}
	
	private void setData(JLabel l, int i, int id) {
		switch (id) {
		case 0:
			r = i;
			break;
		case 1:
			g = i;
			break;
		default:
			b = i;
			break;
		}
		String itext = i + "";
		for (int j = itext.length(); j < 3; j++) {
			itext = " " + itext;
		}
		l.setText(l.getName() + itext);
		setColor();
	}
	
	
	private void updateSS() {
		rs.setValue(r);
		rJSpinner.setValue(r);

		gs.setValue(g);
		gJSpinner.setValue(g);

		bs.setValue(b);
		bJSpinner.setValue(b);
	}
	
	boolean isAuto = false;
	private void updateHEX() {
		isAuto = true;
		try {
			hexField.setText(Integer.toHexString(new Color(r,g,b).getRGB()).toUpperCase().substring(2));
		} catch (IllegalStateException e) {
			isAuto = false;
		}
//		try {
//		} catch (IllegalStateException e) {
//		}
	}
	
	private void setColor() {
		view.setBackground(new Color(r,g,b));
		colorChange(new Color(r,g,b), ch);
	}
	
	
	class HexFilter extends DocumentFilter {
		
		final String HEXREG = "0123456789ABCDEF";
		
		JTextField tf;
		public HexFilter(JTextField hexField) {
			tf = hexField;
		}
		@Override
		public void insertString(FilterBypass arg0, int arg1, String str, AttributeSet arg3)
				throws BadLocationException {
			if(isAuto) {
				super.insertString(arg0, arg1, str, arg3);
				isAuto = false;
				return;
			}
			char[] cs = str.toUpperCase().toCharArray();
			String ns = "";
			for (char c : cs) {
				if(HEXREG.indexOf(c) != -1)
					ns += c;
			}
			super.insertString(arg0, arg1, ns, arg3);
			setMax(tf);
		}
		@Override
		public void replace(FilterBypass arg0, int arg1, int arg2, String str, AttributeSet arg4)
				throws BadLocationException {
			if(isAuto) {
				super.replace(arg0, arg1, arg2, str, arg4);
				isAuto = false;
				return;
			}
			char[] cs = str.toUpperCase().toCharArray();
			String ns = "";
			for (char c : cs) {
				if(HEXREG.indexOf(c) != -1)
					ns += c;
			}
			super.replace(arg0, arg1, arg2, ns, arg4);
			setMax(tf);
		}
		
		public void setMax(JTextField tf) {
			if(tf.getText().length() > 6) {
				tf.setText(tf.getText().substring(0, 6));
			}
		}
	}
	
	public Color getColor() {
		return new Color(r,g,b);
	}
	
	boolean ch = false;
	
	abstract void colorChange(Color c, boolean b);
	
	public void setColor(Color c) {
		r = c.getRed();
		g = c.getGreen();
		b = c.getBlue();
		
		ch = true;
		updateSS();
		updateHEX();
		ch = false;
		view.setBackground(new Color(r,g,b));
		colorChange(new Color(r,g,b), true);
	}
}
