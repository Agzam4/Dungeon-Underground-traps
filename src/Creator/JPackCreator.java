package Creator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import Work.MyFile;

import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JTabbedPane;
import javax.swing.TransferHandler;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.FlowLayout;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.rmi.server.ExportException;

import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.CardLayout;
import java.awt.Dimension;

public class JPackCreator extends JFrame {

	private JPanel contentPane;
	private Color[] colors = new Color[7];
	static JCheckBox[] imgBoxs = new JCheckBox[CreatorData.imgs.length];
	static JCheckBox[] colorBoxs = new JCheckBox[CreatorData.colors.length];
	static JCheckBox[] borderBoxs = new JCheckBox[CreatorData.borderColor.length];
	
	String[] textmodel = new String[] {
			"Game Background", "Text Background", "Text Foreground ",
			"Text Foreground (dark)", "Text Foreground (dark.dark)"};
	

	String[] imgmodel = new String[] {"Tileset", "Player", "Dart", "Achievements (Background)",
			"Achievements (Foreground)",  "Achievements (Tileset)",  "Background"};

	String[] bordermodel = new String[] {"Border Up", "Border Down", "Border Left", "Border Right"};
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JPackCreator frame = new JPackCreator();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	@SuppressWarnings("serial")
	public JPackCreator() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.setFocusable(false);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel textures = new JPanel();
		tabbedPane.addTab("Textures", null, textures, null);
		textures.setLayout(new BoxLayout(textures, BoxLayout.X_AXIS));
		
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.setFocusable(false);
		textures.add(tabbedPane_1);
		
		JPanel Tileset = new JPanel();
		tabbedPane_1.addTab("Tileset", null, Tileset, null);
		
		JPanel player = new JPanel();
		tabbedPane_1.addTab("Player", null, player, null);
		
		JPanel dart = new JPanel();
		tabbedPane_1.addTab("Dart", null, dart, null);
		
		JPanel achievement_bg = new JPanel();
		tabbedPane_1.addTab("Background", null, achievement_bg, null);
		
		JPanel achievements = new JPanel();
		tabbedPane_1.addTab("Achievements", null, achievements, null);
		
		JPanel colors = new JPanel();
		tabbedPane.addTab("Colors", null, colors, null);
		colors.setLayout(new BorderLayout(0, 0));

		ColorSelect colorSelected = null;
		JColorsList list = new JColorsList(textmodel, colorSelected, 0);
		
		colorSelected = new ColorSelect() {

			@Override
			void colorChange(Color c, boolean b) {
				if(!b) {
					list.setColor(list.getIndex(), c);
				}
			}
		};
		
		list.setColorSelected(colorSelected);
		

		
		colors.add(list, BorderLayout.WEST);
		colors.add(colorSelected, BorderLayout.CENTER);
		
		JScrollPane borderJScrollPane = new JScrollPane();
		borderJScrollPane.setBorder(null);
		tabbedPane.addTab("Border", null, borderJScrollPane, null);
		
		JPanel border = new JPanel();
		border.setLayout(new BoxLayout(border, BoxLayout.PAGE_AXIS));
		borderJScrollPane.setViewportView(border);
		
		
		JPanel colorss = new JPanel();
		colorss.setBorder(new TitledBorder(null, "Border Colors", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		border.add(colorss);
		
		ColorSelect colorBorder = null;
		JColorsList listBorder = new JColorsList(new String[] {
						"Border top", "Border right", "Border bottom", "Border left"},
				colorBorder, 1);
		
		colorBorder = new ColorSelect() {

			@Override
			void colorChange(Color c, boolean b) {
				if(!b) {
					listBorder.setColor(listBorder.getIndex(), c);
				}
			}
		};
		colorss.setLayout(new BoxLayout(colorss, BoxLayout.X_AXIS));
		
		JPanel panel = new JPanel();
		colorss.add(panel);
		
		listBorder.setColorSelected(colorBorder);
		
		panel.add(listBorder);
		colorss.add(colorBorder);
		
		JPanel combo = new JPanel();
		border.add(combo);
		GridBagLayout gbl_combo = new GridBagLayout();
		gbl_combo.columnWidths = new int[]{41, 142, 0};
		gbl_combo.rowHeights = new int[]{26, 0};
		gbl_combo.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_combo.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		combo.setLayout(gbl_combo);
		
		JLabel typeLable = new JLabel("Type:");
		GridBagConstraints gbc_typeLable = new GridBagConstraints();
		gbc_typeLable.anchor = GridBagConstraints.WEST;
		gbc_typeLable.insets = new Insets(0, 0, 0, 5);
		gbc_typeLable.gridx = 0;
		gbc_typeLable.gridy = 0;
		combo.add(typeLable, gbc_typeLable);
		
		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Border IN", "Border CENTER", "Border OUT"}));
		comboBox.setFocusable(false);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.anchor = GridBagConstraints.NORTHWEST;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 0;
		combo.add(comboBox, gbc_comboBox);
		
		comboBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				CreatorData.borderType = comboBox.getSelectedIndex() - 1;
			}
		});
		
		JPanel borderSize = new JPanel();
		border.add(borderSize);
		GridBagLayout gbl_borderSize = new GridBagLayout();
		gbl_borderSize.columnWidths = new int[]{123, 51, 0};
		gbl_borderSize.rowHeights = new int[]{26, 0};
		gbl_borderSize.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_borderSize.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		borderSize.setLayout(gbl_borderSize);
		
		JLabel borderSizeLable = new JLabel("Border size (px): ");
		GridBagConstraints gbc_borderSizeLable = new GridBagConstraints();
		gbc_borderSizeLable.anchor = GridBagConstraints.WEST;
		gbc_borderSizeLable.insets = new Insets(0, 0, 0, 5);
		gbc_borderSizeLable.gridx = 0;
		gbc_borderSizeLable.gridy = 0;
		borderSize.add(borderSizeLable, gbc_borderSizeLable);
		
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(0.0, 0.0, 16.0, 0.5));
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.anchor = GridBagConstraints.NORTHWEST;
		gbc_spinner.gridx = 1;
		gbc_spinner.gridy = 0;
		borderSize.add(spinner, gbc_spinner);
		
		spinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				CreatorData.borderSize = (double) spinner.getValue();
				for (JCheckBox bb : borderBoxs) {
					bb.setEnabled(CreatorData.borderSize != 0);
				}
			}
		});
		
		JPanel music = new JPanel();
		tabbedPane.addTab("Music", null, music, null);
		
		JPanel text = new JPanel();
		tabbedPane.addTab("Text", null, text, null);
		
		JPanel info = new JPanel();
		contentPane.add(info, BorderLayout.SOUTH);
		
		DropPanel tilesetDropPanel = new DropPanel(DropPanel.TYPE_IMG, CreatorData.Images.Dungeon_Underground_traps.toString());
		Tileset.add(tilesetDropPanel);

		DropPanel playerDropPanel = new DropPanel(DropPanel.TYPE_IMG, CreatorData.Images.player.toString());
		player.add(playerDropPanel);

		DropPanel dartDropPanel = new DropPanel(DropPanel.TYPE_IMG, CreatorData.Images.dart.toString());
		dart.add(dartDropPanel);
		
		achievements.setLayout(new BoxLayout(achievements, BoxLayout.Y_AXIS));

		DropPanel bgDropPanel = new DropPanel(DropPanel.TYPE_IMG, CreatorData.Images.achievement_stage.toString());
		achievement_bg.add(bgDropPanel);
		
		DropPanel achievement_bgDropPanel = new DropPanel(DropPanel.TYPE_IMG, CreatorData.Images.achievement_bg.toString());
		achievement_bgDropPanel.setText2("Achievement Background Image");
		achievements.add(achievement_bgDropPanel);

		DropPanel achievement_fgDropPanel = new DropPanel(DropPanel.TYPE_IMG, CreatorData.Images.achievement_fg.toString());
		achievement_fgDropPanel.setText2("Achievement Foreground Image");
		achievements.add(achievement_fgDropPanel);

		DropPanel achievement_imgDropPanel = new DropPanel(DropPanel.TYPE_IMG, CreatorData.Images.achievement_img.toString());
		achievement_imgDropPanel.setText2("Achievement Tileset");
		achievements.add(achievement_imgDropPanel);
		
		JPanel create = new JPanel();
		tabbedPane.addTab("Create", null, create, null);
		create.setLayout(new BoxLayout(create, BoxLayout.Y_AXIS));
		
		JPanel scrollPaneArea = new JPanel();
		scrollPaneArea.setBorder(null);
		create.add(scrollPaneArea);
		scrollPaneArea.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		scrollPaneArea.add(scrollPane);
		
		JPanel settings = new JPanel();
		settings.setBorder(new EmptyBorder(5, 0, 5, 0));
		scrollPane.setViewportView(settings);
		settings.setLayout(new BoxLayout(settings, BoxLayout.Y_AXIS));
		
		JPanel _images = new JPanel();
		_images.setBorder(new TitledBorder(null, "Image", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		settings.add(_images);
		_images.setLayout(new BoxLayout(_images, BoxLayout.Y_AXIS));
		
		for (int i = 0; i < imgBoxs.length; i++) {
			imgBoxs[i] = new JCheckBox(imgmodel[i]);
			imgBoxs[i].setFocusable(false);
			imgBoxs[i].setSelected(false);
			_images.add(imgBoxs[i]);
		}
		
		JPanel _colors = new JPanel();
		_colors.setBorder(new TitledBorder(null, "Colors", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		settings.add(_colors);
		_colors.setLayout(new BoxLayout(_colors, BoxLayout.Y_AXIS));
		for (int i = 0; i < colorBoxs.length; i++) {
			colorBoxs[i] = new JCheckBox(textmodel[i]);
			colorBoxs[i].setFocusable(false);
			colorBoxs[i].setSelected(false);
			_colors.add(colorBoxs[i]);
		}
		
		JPanel _border = new JPanel();
		_border.setBorder(new TitledBorder(null, "Border", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		settings.add(_border);
		_border.setLayout(new BoxLayout(_border, BoxLayout.Y_AXIS));
		
		for (int i = 0; i < borderBoxs.length; i++) {
			borderBoxs[i] = new JCheckBox(bordermodel[i]);
			borderBoxs[i].setFocusable(false);
			borderBoxs[i].setSelected(false);
			borderBoxs[i].setEnabled(false);
			_border.add(borderBoxs[i]);
		}
		
//		JCheckBox chckbxNewCheckBox = new JCheckBox("New check box");
//		images.add(chckbxNewCheckBox);
//		
//		JCheckBox chckbxNewCheckBox_1 = new JCheckBox("New check box");
//		images.add(chckbxNewCheckBox_1);
		
		JPanel test = new JPanel();
		settings.add(test);
		
		JPanel panel_1 = new JPanel();
		panel_1.setMaximumSize(new Dimension(32767, 30));
		panel_1.setMinimumSize(new Dimension(310, 10));
		create.add(panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		JButton createButton = new JButton("Create");
		panel_1.add(createButton);
		
		updateImgBoxs();
		
		
		createButton.addActionListener(e -> {
			CreatorData.export();
		});
	}
	
	
	public static void updateImgBoxs() {
		for (int i = 0; i < imgBoxs.length; i++) {
			boolean b = CreatorData.imgs[i] != null;
			if(!imgBoxs[i].isEnabled() && b) {
				imgBoxs[i].setSelected(true);
			}
			imgBoxs[i].setEnabled(b);
		}
	}
	
	public static void updateColorsBoxs(Color[] cs) {
		for (int i = 0; i < colorBoxs.length; i++) {
			if(cs[i] != null && colorBoxs[i] != null) {
				boolean b = CreatorData.colors[i] == null;
				if(!colorBoxs[i].isSelected()) {
					if(b) {
						colorBoxs[i].setSelected(true);
						CreatorData.colors[i] = cs[i];
					}else {
						CreatorData.colors[i] = null;
					}
				}
			}
		}
	}
	
	public static void updateBorder(Color[] cs) {
		for (int i = 0; i < borderBoxs.length; i++) {
			if(cs[i] != null && borderBoxs[i] != null) {
				boolean b = CreatorData.borderColor[i] == null;
				if(!borderBoxs[i].isSelected()) {
					if(b) {
						colorBoxs[i].setSelected(true);
						CreatorData.borderColor[i] = cs[i];
					}else {
						CreatorData.borderColor[i] = null;
					}
				}
			}
		}
	}
}
