package Main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Stages.GameStage;
import Work.GameData;
import Work.MouseController;
import Work.MyFile;

public class DungeonJFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static GamePanel contentPane;
	private static DungeonJFrame frame;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		MyFile.checkSavesFolder();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new DungeonJFrame();
					frame.setVisible(true);
					contentPane.run(frame);
					System.err.println("\n" + frame.getWidth() + "," + frame.getHeight());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public static void setUndecoratedFrame(boolean b) {
		frame.dispose();
		GamePanel.running = false;
		contentPane = null;
		main(null);
//		frame.dispose();
//		frame = new DungeonJFrame();
//		frame.setVisible(true);
		MouseController.isMousePressed = false;
//		contentPane.run(frame);
		
//		frame.setVisible(false);
//		while (frame.isDisplayable()) {
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//			}
//		}
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//		}
//		System.out.println(frame.isDisplayable());
//		frame.setUndecorated(b);
//		frame.setVisible(true);
	}

	String[] txts = {"",""};
	
	/**
	 * Create the frame.
	 */
	public DungeonJFrame() {
		setUndecorated(GameData.fullscreen);
		
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
		
		addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent arg0) {
			}
			
			@Override
			public void componentResized(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent arg0) {
			}
		});
		
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
