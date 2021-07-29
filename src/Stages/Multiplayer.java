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
import Work.Painter;

public class Multiplayer extends Stage {

	Maneger maneger;
	
	public Multiplayer(Maneger maneger) {
		this.maneger = maneger;
		JServer.isCreated = false;
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
		Painter.drawGradientGF(gf, Loader.COLOR_GAME_BG);
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
					if(GameData.isDevMode) {
						panel.setInput("loopback");
						panel.needClose = true;
					}
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
			System.err.println("Starting Server");
			maneger.setGameStageClient(StaticServer.startServer(JServer.getW(), JServer.getH(), JServer.getSok(), maneger));
			if(GameData.isDevMode) { // TODO
				for (int i = 0; i < 10; i++) {
					int ii = i;
					Thread debugClient = new Thread(() -> {
						GameClient client2;
						double nx = 0, ny = 0;
						try {
							try {
								Thread.sleep(200*ii);
							} catch (InterruptedException e) {
							}
							client2 = new GameClient(InetAddress.getLoopbackAddress().getHostAddress(), JServer.getSok(), null,
									"Debug #" + ii);
							try {
								Thread.sleep((long) (300 + Math.random()*700));
							} catch (InterruptedException e) {
							}
							client2.sendReady();
							client2.sendPosition(0, 0);
							while (!client2.isGameEnd()) {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
								}
								if(client2.lg != null && client2.isAllReady)
									for (int x = 0; x < client2.lg.getWidth(); x++) {
										for (int y = 0; y < client2.lg.getHeight(); y++) {
											if(client2.lg.getBlock(x, y) == client2.lg.BLOCK_GOLD
													|| client2.lg.getBlock(x, y) == client2.lg.BLOCK_DIAMOND) {
												int x1 = (int) (nx - x*16);
												int y1 = (int) (ny - y*16);
												
												long sleep = (long) Math.sqrt(x1*x1+y1*y1);
												double tx = x*16, ty = y*16 + 6;
												client2.sendPosition(nx, ny);
												for (long j = 0; j < sleep/100; j++) {
													nx = (nx - tx)*0.8 + tx;
													ny = (ny - ty)*0.8 + ty;
													client2.sendPosition(nx, ny);
													try {
														Thread.sleep(100);
													} catch (InterruptedException e) {
													}
													if(client2.lg.getBlock(x, y) != client2.lg.BLOCK_GOLD
															&& client2.lg.getBlock(x, y) != client2.lg.BLOCK_DIAMOND) {
														break;
													}
												}
												if((client2.lg.getBlock(x, y) == client2.lg.BLOCK_GOLD
														|| client2.lg.getBlock(x, y) == client2.lg.BLOCK_DIAMOND)) {
													client2.sendRemoveTreasures(x, y);
													nx = tx;
													ny = ty;
												}
												client2.sendPosition(nx, ny);
											}
										}
									}
								client2.sendPosition(nx, ny);
							}
							try {
								client2.exit();
							} catch (IOException e) {
								System.err.println("{DEBUG CLIENT}");
								e.printStackTrace();
							}
						} catch (IOException e1) {
							maneger.setMultiplayer(e1.getMessage());
							e1.printStackTrace();
						}
					});
					debugClient.start();
				}
			}
		}
		
		if(join.isClicked()) {
			panel = new JOptionPane(JOptionPane.TYPE_INPUT, "Port (Enter for 7500): ", TYPE_SOCKET);
			if(GameData.isDevMode) {
				panel.needClose = true;
			}
		}
	}

	private void extracted() {
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
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
