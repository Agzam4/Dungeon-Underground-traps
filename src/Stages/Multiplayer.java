package Stages;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import Main.GamePanel;
import Multiplayer.GameClient;
import Multiplayer.JServer;
import Multiplayer.StaticServer;
import Multiplayer.GameServer.ClientHandler;
import Objects.Button;
import Objects.JOptionPane;
import Work.GameData;
import Work.LevelGenerator;
import Work.Loader;

public class Multiplayer extends Stage {

	Maneger maneger;
	
	public Multiplayer(Maneger maneger) {
		this.maneger = maneger;
	}

	Button createRoom = new Button("Create Room",0,0);
	Button join = new Button("Join",0,0);
	Button back = new Button(GameData.texts[GameData.TEXT_BACK], 0, 0);
	

	Button setUsename = new Button("Username: " + GameData.username, 0, 0);
	
	@Override
	public void draw(Graphics2D g, Graphics2D gf) {
		if(panel != null) {
			panel.draw(gf);
		}
		setUsename.draw(gf);
		createRoom.draw(gf);
		join.draw(gf);
		back.draw(gf);
		g.setPaint(GameOverStage.getGradient(Loader.COLOR_GAME_BG));
		g.fillRect(0, 0, GamePanel.getGameWidth(), GamePanel.getGameHeight());
	}
	
	JOptionPane panel;
	private static final int TYPE_USERNAME = 0;
	private static final int TYPE_MAPSIZE = 1;
	private static final int TYPE_SOCKET = 2;
	private static final int TYPE_IP = 3;

	int socket = -1;
	
	@Override
	public void update() {
		if(JServer.isVisible)
			return;
		if(panel != null) {
			panel.update();
			if(panel.needClose) {
				switch (panel.getData()) {
				case TYPE_USERNAME:
					if(!panel.getInput().isEmpty())
						GameData.username = panel.getInput();
					break;
				case TYPE_SOCKET:
					if(panel.getInput().isEmpty()) {
						socket = 7500;
					}else {
						try {
							socket = Integer.parseInt(panel.getInput());
						} catch (NumberFormatException e) {
						}
					}
					System.out.println(socket);
					panel = new JOptionPane(JOptionPane.TYPE_INPUT, "Server IP: ", TYPE_IP);
					return;
				case TYPE_IP:
					if(!panel.getInput().isEmpty()) {
						try {
							GameClient client = new GameClient(panel.getInput(), socket, maneger);
							while (!client.isCreated) {
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
								}
							}
							if(client.isErr) {
								client = null;
							}else {
								maneger.setGameStageClient(client);
							}
						} catch (IOException | IllegalArgumentException e) {
							panel = new JOptionPane(JOptionPane.TYPE_INFO, e.getClass().toString() + ": " + e.getMessage());
							return;
						}
						
					}
					break;
				default:
					break;
				}
				panel = null;
			} else return;
		}
		setUsename.setText("Username: " + GameData.username);
		createRoom.update();
		setUsename.update();
		back.update();
		join.update();
		setUsename.setPosition(GamePanel.frameW/2, GamePanel.frameH/5);
		createRoom.setPosition(GamePanel.frameW/2, GamePanel.frameH/5*2);
		join.setPosition(GamePanel.frameW/2, GamePanel.frameH/5*3);
		back.setPosition(GamePanel.frameW/2, GamePanel.frameH/5*4);
		
		if(setUsename.isClicked()) {
			panel = new JOptionPane(JOptionPane.TYPE_INPUT, "New Username: ", TYPE_USERNAME);
		}
		
		if(back.isClicked()) {
			maneger.loadStage(Maneger.MENU);
		}
		if(createRoom.isClicked()) {
			JServer.create();
		}
		

		if(JServer.isCreated) {
			StaticServer.stop();
			System.err.println("Starting Server");
			maneger.setGameStageClient(StaticServer.startServer(JServer.getW(), JServer.getH(), JServer.getSok(), maneger));
		}
		
		if(join.isClicked()) {
			panel = new JOptionPane(JOptionPane.TYPE_INPUT, "Port (Enter for 7500): ", TYPE_SOCKET);
		}
	}

	@Override
	protected void keyPressed(KeyEvent e) {
		if(panel != null)
			panel.keyPressed(e);
	}

	@Override
	protected void keyReleased(KeyEvent e) {
	}

	@Override
	protected void releasedAll() {
	}

	@Override
	protected void reloadTexts() {
	}

	@Override
	protected LevelGenerator getLevelGenerator() {
		return null;
	}

	public void setPanel(JOptionPane panel) {
		this.panel = panel;
	}
}
