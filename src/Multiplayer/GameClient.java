package Multiplayer;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import Objects.OtherPlayers;
import Stages.GameStage;
import Stages.Maneger;
import Work.GameData;
import Work.LevelGenerator;


public class GameClient {

	ArrayList<OtherPlayers> otherPlayers;
	
	BufferedReader reader;
	PrintWriter writer;
	Socket sock;

	public String leadBoard = "";
	public long score = 0;
	public long scoreLast = 0;
	public int clientID = -1;
	public boolean isMapGet = false;
	public boolean isAllReady = false;
	
	public boolean isPlaying = true;
	public int vievOn;
	
	public int normalTime = 0;
	
	String chat = "";
	
	public LevelGenerator lg;
	
	public void setLg(LevelGenerator lg) {
		this.lg = lg;
	}

	public boolean isCreated = false;
	public boolean isErr = false;
	

	public boolean needExit = false;
	public String exitMsg = "";
	
	Maneger m;
	
	public GameClient(String ip, int i, Maneger m) throws IOException {
		this.m = m;
		otherPlayers = new ArrayList<OtherPlayers>();
		System.err.println("Created Cleint: " + ip + ":" + i);
			sock = new Socket(ip, i);
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamReader);
			writer = new PrintWriter(sock.getOutputStream());
			Thread readerThread = new Thread(new IncomingReader());
			readerThread.start();
			getID();
		isCreated = true;
	}
	
	private void getID() {
		System.out.println("-2%" + GameServer.TYPE_GETCOUNTPLAYERS + "| ");
		writer.println("-2%" + GameServer.TYPE_GETCOUNTPLAYERS + "| ");
		writer.flush();
	}
	
	
	private void getMap() {
		System.out.println(clientID + "%" + GameServer.TYPE_GETMAP + "|1");
		sendMsg(GameServer.TYPE_GETMAP + "|1");
	}
	
	private void sendMsg(String msg) {
		writer.println(clientID + "%" + msg);
		writer.flush();
	}

//	public class SendButtonListener implements ActionListener {
//
//		public void actionPerformed(ActionEvent arg0) {
//			try {
//				writer.println(outgoing.getText());
//				writer.flush();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	public class IncomingReader implements Runnable {
		
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
//					System.out.println("read " + message);
					/**
					 *  ID%DATA_TYPE|DATA
					 */
					String id = message.substring(0, message.indexOf("%"));
					String dataType = message.substring(message.indexOf("%")+1, message.indexOf("|"));
					String data = message.substring(message.indexOf("|")+1, message.length());
//					System.out.println(clientID + "is get msg: " + message);
					switch (dataType) {

					case GameServer.TYPE_SETGHOST:
							System.err.println("GHOST: " + data);
						try { // FIXME
							int ghostID = Integer.parseInt(data);
							otherPlayers.remove(ghostID);
							if(ghostID == clientID) {
								isPlaying = false;
								vievOn = 0;
							}
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
						break;
					case GameServer.TYPE_SETTIME:
						try {
							System.out.println("Setting Time: " + data);
							normalTime = Integer.parseInt(data);
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
						break;
					case GameServer.TYPE_START:
						isAllReady = true;
						isPlaying = true;
						break;
					case GameServer.TYPE_GAMEALREADYSTARTED:
						if(data.equals(clientID+"")) {
							System.err.println("#GameAlreadyStarted");
							needExit = true;
							exitMsg = "Game Already Started";
							exit();
						}
						break;
					case GameServer.TYPE_MESSAGE:
						chat += data + "\n";
						break;
					case GameServer.TYPE_LEADBOARD:
						setLeadBoard(data);
						break;
					case GameServer.TYPE_REMOVEPLAYER:
						try {
							int idINT = Integer.parseInt(data);
							if(idINT > -1 && idINT < otherPlayers.size()) {
								otherPlayers.remove(idINT);
							}
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
						break;
					case GameServer.TYPE_SETNAME:
						try {
							int idINT = Integer.parseInt(id);
							if(idINT > -1 && idINT < otherPlayers.size()) {
								otherPlayers.get(idINT).setName(data);
							}
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
						break;
					case GameServer.TYPE_SETCOUNTPLAYERS:
						try {
							int players = Integer.parseInt(data);
							if(clientID == -1) {
								clientID = players-1;
								getMap();
							}
							if(otherPlayers.size() < players) {
								otherPlayers.add(new OtherPlayers(players-1));
							}
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
						break;
					case GameServer.TYPE_SETMAP:
						System.err.println("Getting map: " + data);
						String lines[] = data.split(" ");
						int[][] map = new int[lines[0].split(":").length][lines.length];
						for (int y = 0; y < lines.length; y++) {
							String tiles[] = lines[y].split(":");
							for (int x = 0; x < tiles.length; x++) {
								map[x][y] = Integer.parseInt(tiles[x]);
							}
							System.out.println("Parsing: " + y + "/" + lines.length);
						}
//						if(lg == null) {
//						}
						lg = new LevelGenerator();
						System.out.println("Setting...");
						lg.setMap(map);
						scoreLast = lg.getScore();
						isMapGet = true;
						System.out.println("isMapGet: " + isMapGet);
						sendMsg(GameServer.TYPE_SETNAME + "|" + GameData.username);
						break;
					case GameServer.TYPE_POSITION:
						try {
							int idINT = Integer.parseInt(id);
							if(otherPlayers.size()-1 < idINT) {
								otherPlayers.add(new OtherPlayers(otherPlayers.size()-1));
							}
							if(idINT > -1 && idINT < otherPlayers.size()) {
								String[] pos = data.split(":");
								otherPlayers.get(idINT).setPosition(
										Double.parseDouble(pos[0]), Double.parseDouble(pos[1]));
//								System.out.println(data);
							}
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
						break;
					case GameServer.TYPE_SETSCORE:
						try {
							String[] dataStrings = data.split(":");
							int idINT = Integer.parseInt(dataStrings[0]);
							long scoreData = Long.parseLong(dataStrings[1]);
							if(idINT == clientID) {
								score = scoreData;
							}
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
						break;
					case GameServer.TYPE_SETAIRTILE:
						System.out.println("#SETAIR " + clientID);
						try {
							String[] pos = data.split(":");
							lg.setTile(Integer.parseInt(pos[0]),
									Integer.parseInt(pos[1]), lg.BLOCK_AIR);
							System.out.println("#Setting tile (" + clientID + "):" + data);
							GameStage.level = lg;
							scoreLast = lg.getScore();
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
						break;
					default:
						break;
					}
				}
			} catch (SocketException e) {
				m.setMultiplayer(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendPosition(double x, double y) {
		sendMsg(GameServer.TYPE_POSITION + "|" + x + ":" + y);
	}
	
	public void sendRemoveTreasures(int x, int y) {
		sendMsg(GameServer.TYPE_REMOVETREASURES + "|" + x + ":" + y);
	}
	
	public void sendReady() {
		sendMsg(GameServer.TYPE_PLAYERREADY + "| ");
	}
	
	public void drawPlayers(Graphics2D g) {
		for (int i = 0; i < otherPlayers.size(); i++) {
			if(i != clientID) {
				otherPlayers.get(i).id = i;
				otherPlayers.get(i).players = otherPlayers.size();
				otherPlayers.get(i).draw(g);
			}
		}
	}

	private void setLeadBoard(String data) {
		try {
			otherPlayers.get(clientID).setName(GameData.username);
			String newLb = data;
			for (int i = 0; i < otherPlayers.size(); i++) {
				newLb = newLb.replaceAll("#P" + i, otherPlayers.get(i).getName());
			}
			leadBoard = newLb;	
		} catch (IndexOutOfBoundsException e) {
		}		
	}
	
	public String getChat() {
		return chat;
	}
	
	public LevelGenerator getLg() {
		return lg;
	}
	
	public long getScore() {
		return score;
	}
	
	public long getScoreLast() {
		return scoreLast;
	}
	
	public int getPlayersCount() {
		if(otherPlayers.size() < 1) {
			return 1;
		}
		return otherPlayers.size();
	}

	// 364787447719772268
	public void exit() throws IOException {
		sock.shutdownInput();
		sock.shutdownOutput();
		sock.close();
//		reader.close();
//		writer.close();
	}
	
	public double getViewX() {
		return otherPlayers.get(vievOn).getX();
	}
	
	public double getViewY() {
		return otherPlayers.get(vievOn).getY();
	}

	public void nextView() {
		vievOn = (vievOn+1)%otherPlayers.size();
	}

	public void nextLast() {
		int nv = vievOn - 1;
		if(nv < 0) {
			nv = otherPlayers.size()-1;
		}
		vievOn = nv;
	}
	
}
