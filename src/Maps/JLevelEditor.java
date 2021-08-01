package Maps;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import Work.Loader;

import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class JLevelEditor extends JFrame {

	private JPanel contentPane;
	static JLevelEditor frame;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Loader.reload(null);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new JLevelEditor();
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
	public JLevelEditor() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		LevelEditor panel = new LevelEditor();
		contentPane.add(panel, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		scrollPane.setFocusable(false);
		contentPane.add(scrollPane, BorderLayout.EAST);
		
		JPanel panel_1 = new JPanel();
		scrollPane.setViewportView(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		for (int i = 0; i < Loader.tileset.length; i++) {
			int ii = i;
			JButton button = new JButton();
			button.setIcon(new ImageIcon(Loader.tileset[i].getImg()));
			button.setFocusable(false);
			button.addActionListener(e -> {
				selected = ii+1;
			});
			panel_1.add(button);
		}
	}
	
	protected static int selected = 1;
}
