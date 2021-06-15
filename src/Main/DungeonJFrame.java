package Main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Work.GameData;
import Work.MyFile;

public class DungeonJFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static GamePanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		MyFile.checkSavesFolder();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DungeonJFrame frame = new DungeonJFrame();
					frame.setVisible(true);
					contentPane.run(frame);
					System.err.println("\n" + frame.getWidth() + "," + frame.getHeight());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	String[] txts = {"",""};
	
	/**
	 * Create the frame.
	 */
	public DungeonJFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Dungeon (Underground traps)" + txts[(int) (Math.random()*txts.length)]);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setMinimumSize(new Dimension(0, 200));
		setBounds(100, 100, 500,500);
		setLocationRelativeTo(null);
		contentPane = new GamePanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		addWindowListener(new WindowListener() {
			int count = 0;
			
			@Override
			public void windowOpened(WindowEvent arg0) {
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				if(count > 5) {
					System.exit(0);
				}
				count++;
				try {
					GameData.save();
				} catch (Exception e) {
					System.exit(0);
				}
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {
			}
		});
	}

}
