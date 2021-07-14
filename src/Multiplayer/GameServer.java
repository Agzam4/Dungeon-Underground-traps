package Multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Work.LevelGenerator;

public class GameServer {

	String chat = "";

	LevelGenerator level; 
	public static final long SCORE_GOLD = 10;
	public static final long SCORE_DIAMOND = 50;
	
	public static final String TYPE_MESSAGE = "msg";
	public static final String TYPE_POSITION = "pos";
	public static final String TYPE_SETNAME = "snm";
	
	// only for server
	public static final String TYPE_GETMAP = "map";
	public static final String TYPE_GETCOUNTPLAYERS = "gcp";
	public static final String TYPE_REMOVETREASURES = "rth";
	public static final String TYPE_PLAYERREADY = "prd";
	
	// only for client
	public static final String TYPE_SETMAP = "smp";
	public static final String TYPE_SETCOUNTPLAYERS = "scp";
	public static final String TYPE_SETAIRTILE = "sat";
	public static final String TYPE_SETSCORE = "ssc";
	public static final String TYPE_START = "str";
	public static final String TYPE_SETTIME = "stm";
	public static final String TYPE_SETGHOST = "sgh";
	public static final String TYPE_LEADBOARD = "leb";
	public static final String TYPE_REMOVEPLAYER = "rmp";
	public static final String TYPE_GAMEALREADYSTARTED = "gas";

	ArrayList<PrintWriter> clientOutputStreams;
//	ArrayList<Long> clientsID;
	ArrayList<Long> score;
	ArrayList<Integer> ghosts;
	int ready = 0;
	boolean running;
	boolean isGameStarted = false;
	
	int time = Integer.MAX_VALUE;
	boolean isUnDang = true;
	int UN_DANG_TIME = 10;//60*5;
	int PLAYER_TIME = 30;
	
	
	boolean updateTime = false;
	boolean updateGhost = false;
	int setGhostID = -1;
	
	public GameServer(LevelGenerator lg, int i) {
		level = lg;
		System.out.println("#W: " + level.getWidth());
		clientOutputStreams = new ArrayList<PrintWriter>();
		score = new ArrayList<Long>();
		ghosts = new ArrayList<Integer>();
		try {
			ServerSocket serverSock = new ServerSocket(i);
			running = true;
			System.out.println("Server Started\nServer: Wating clients");
			Thread timer = new Thread(() -> {
				isUnDang = true;
				while (running) {
					if(isGameStarted) {
						updateTime = true;
						if(time > 0) {
							time--;
						}else {
//							if(!isUnDang) {
							sortLeadBoard();
								int gid = leads.get(leads.size()-1);
								setGhostID = gid;
								System.out.println("-" + gid);
								updateGhost = true;
//								removePlayer(); // FIXME
//							}
							time = PLAYER_TIME;
							isUnDang = false;
						}
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					System.out.println("LOOP: " + time);
				}
			});
			timer.start();
			while (running) {
				Socket clientSocket = serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				clientOutputStreams.add(writer);
				score.add(0l);
				Thread t = new Thread(new ClientHandler(clientSocket, clientOutputStreams.size()-1));
				t.start();
				System.out.println("got a connection");
				
			}
			serverSock.close();
		} catch (IOException e) {
		}
	}
	
	public void stop() {
		running = false;
	}
	
	public class ClientHandler implements Runnable {
		BufferedReader reader;
		Socket sock;
		
		int _id = -1;
		
		public ClientHandler(Socket clientSocket, int _id) {
			try {
				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
				this._id = _id;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					if(sock.isClosed()) {
						removePlayer(_id);
						break;
					}
					/**
					 *  ID%DATA_TYPE|DATA
					 */
					String id = (message).substring(0, message.indexOf("%"));
					String dataType = message.substring(message.indexOf("%")+1, message.indexOf("|"));
					String data = message.substring(message.indexOf("|")+1, message.length());
					if(!dataType.equals(TYPE_POSITION))
						System.out.println("[ID: " + id + "], [DATA_TYPE: " + dataType + "], [DATA: " + data + "]");
					switch (dataType) {
					case TYPE_SETNAME:
						updateLeadBoard();
						break;
					case TYPE_PLAYERREADY:
						ready++;
						if(ready == clientOutputStreams.size() && clientOutputStreams.size() > 1) {
							tellEveryone("-1%" + TYPE_START + "| ");
							isGameStarted = true;
							updateLeadBoard();
							time = UN_DANG_TIME;//60*5; // TODO
						}
						break;
					case TYPE_MESSAGE:
						chat += data;
						break;
					case TYPE_POSITION:
						chat += data;
						break;
					case TYPE_GETMAP:
						if(isGameStarted) {
							try {
								tellTo("-1%" + TYPE_GAMEALREADYSTARTED + "| ", Integer.parseInt(id));
							} catch (Exception e) {
								e.printStackTrace();
							}
							System.out.println("#GS");
						}
						StringBuilder map = new StringBuilder("-1%" + TYPE_SETMAP + "|");
						System.out.println("MAP: " + level.getHeight() + "x" + level.getWidth());
						for (int y = 0; y < level.getHeight(); y++) {
							for (int x = 0; x < level.getWidth()-1; x++) {
								map.append(level.getBlock(x, y) + ":");
							}
							map.append(level.getBlock(level.getWidth()-1, y) + " ");
						}
						tellEveryone(map.toString());
						updateLeadBoard();
						break;
					case TYPE_GETCOUNTPLAYERS:
						tellEveryone("-1%" + TYPE_SETCOUNTPLAYERS + "|" + clientOutputStreams.size());
						break;
					case TYPE_REMOVETREASURES:
						try {
							String[] pos = data.split(":");
							int x = Integer.parseInt(pos[0]);
							int y = Integer.parseInt(pos[1]);
							int idINT = Integer.parseInt(id);
							System.out.println("SETTILE: " + idINT);
							if(idINT > -1 && idINT < score.size()) {
								if(level.getBlock(x, y) == level.BLOCK_GOLD) {
									score.set(idINT, score.get(idINT) + SCORE_GOLD);
								} else if (level.getBlock(x, y) == level.BLOCK_DIAMOND) {
									score.set(idINT, score.get(idINT) + SCORE_DIAMOND);
								}else {
									break;
								}
								level.setTile(x, y, level.BLOCK_AIR);
								tellEveryone("-1%" + TYPE_SETAIRTILE + "|" + x + ":" + y);
								tellEveryone("-1%" + TYPE_SETSCORE + "|" + idINT + ":" + score.get(idINT));
								updateLeadBoard();
							}
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
						break;
					default:
						break;
					}
					
					tellEveryone(message);
					
					if(updateTime) {
						System.out.println("updateTime");
						tellEveryone("1%" + TYPE_SETTIME + "|" + time);
						updateLeadBoard();
						updateTime = false;
					}
					if(updateGhost) {
						System.out.println("setGhostID: " + setGhostID);
						tellEveryone("-1%" + TYPE_SETGHOST + "|" + setGhostID);
						ghosts.add(setGhostID);
						score.remove(setGhostID);
						updateGhost = false;
					}
				}
				removePlayer(_id);
			}catch (SocketException e) {
				e.printStackTrace();
				removePlayer(_id);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void removePlayer(int _id) {
		clientOutputStreams.remove(_id);
		score.remove(_id);
		updateLeadBoard();
		System.err.println(_id + " - left game");
		tellEveryone("-1%" + TYPE_REMOVEPLAYER + "|" + _id);
	}
	
	
	public void tellEveryone(String message) {
		Iterator<PrintWriter> it = clientOutputStreams.iterator();
		while (it.hasNext()) {
			PrintWriter writer = it.next();
			writer.println(message);
			writer.flush();
		}
	}
	
	public void tellTo(String message, int id) {
		PrintWriter writer = clientOutputStreams.get(id);
		writer.println(message);
		writer.flush();
	}
	
	String leadBoardString;

	ArrayList<Integer> leads = new ArrayList<Integer>();
	
	private void sortLeadBoard() {	
		HashMap<Integer, Long> leadBoard = new HashMap<Integer, Long>();
		for (int i = 0; i < score.size(); i++) {
			leadBoard.put(i, score.get(i));
		}

		leads.clear();
		leadBoardString = "";
		leadBoard.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(e -> {
			leads.add(e.getKey());
			leadBoardString = e.getKey() + "/" + (1 + clientOutputStreams.size() - leads.size()) + ") #P" + e.getKey() + " (" + e.getValue() + ");" + leadBoardString;
		});
		for (int i = 0; i < ghosts.size(); i++) {
			leadBoardString = ghosts.get(i) + "/" + (score.size()+i) + ") #P" + ghosts.get(i) + " (Lose);" + leadBoardString;
		}
	}
	
	private void updateLeadBoard() {	
		sortLeadBoard();
		System.out.println(leadBoardString);
		tellEveryone("-1%" + TYPE_LEADBOARD + "|" + leadBoardString);
	}
}
