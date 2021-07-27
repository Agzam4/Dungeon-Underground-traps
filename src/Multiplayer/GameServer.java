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
	public static final String TYPE_CHANGEMUSIC = "shm";
	public static final String TYPE_END_GAME = "end";

	ArrayList<PrintWriter> clientOutputStreams;
//	ArrayList<Long> clientsID;
	ArrayList<Long> score;
	ArrayList<Integer> playersReady;
	ArrayList<Integer> ghosts;
	
	int ready = 0;
	boolean running;
	boolean isGameStarted = false;
	
	int time = Integer.MAX_VALUE;
	boolean isUnDang = true;
	int UN_DANG_TIME = 60*5;
	int PLAYER_TIME = 30;
	
	
	boolean updateTime = false;
	boolean updateGhost = false;
	boolean changeMusic = false;
	int setGhostID = -1;
	boolean isGameEnd = false;
	
	public GameServer(LevelGenerator lg, int i) {
		level = lg;
		System.out.println("#W: " + level.getWidth());
		clientOutputStreams = new ArrayList<PrintWriter>();
		playersReady = new ArrayList<Integer>();
		score = new ArrayList<Long>();
		ghosts = new ArrayList<Integer>();
		try {
			ServerSocket serverSock = new ServerSocket(i);
			running = true;
			System.out.println("Server Started\nServer: Wating clients");
			Thread timer = new Thread(() -> {
				isUnDang = true;
				changeMusic = false;
				isGameEnd = false;
				while (running) {
					if(isGameStarted) {
						updateTime = true;
						if(time > 0) {
							time--;
						}else {
//							if(!isUnDang) {
							sortLeadBoard();
							if(!isUnDang && leads.size()>1) {
								int gid = leads.get(0);
								setGhostID = gid;
								updateGhost = true;
							}
							time = PLAYER_TIME;
							if(isUnDang) {
								changeMusic = true;
							}
							isUnDang = false;
						}
					}else {
						checkAllReady();
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					if(isGameStarted && !(ghosts.size() < clientOutputStreams.size()-1) && !isGameEnd) {
						System.err.println("\n\n\n!!! GAME END !!!\n\n\n");
						isGameEnd = true;
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
						try {
							int idINT = Integer.parseInt(id);
							playersReady.add(idINT);
						} catch (NumberFormatException e) {
						}
						checkAllReady();
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
						updateGhost = false;
					}
					if(changeMusic) {
						tellEveryone("-1%" + TYPE_CHANGEMUSIC + "|");
						changeMusic = false;
					}
					
					if(isGameEnd) {
						ghosts.clear();
						sortLeadBoard();
						String leadINFO = "";
						for (int i = 0; i < leads.size(); i++) {
							leadINFO += (i+1) + ":" + leads.get(i) + ":" + score.get(score.size()-1-leads.get(i)) + ";";
						}
						tellEveryone("-1%" + TYPE_END_GAME + "|" + leadINFO);
						isGameEnd = false;
					}
				}
				removePlayer(_id);
			} catch (SocketException e) {
				removePlayer(_id);
			} catch (IOException e) {
				e.printStackTrace();
				removePlayer(_id);
			}
		}

	}
	
		private void checkAllReady() {
			if(playersReady.size() == clientOutputStreams.size() && clientOutputStreams.size() > 1) {
				tellEveryone("-1%" + TYPE_START + "| ");
				int ts = (int) Math.round(((level.getWidth()*level.getHeight())/50_000d) * 60d);
				int onplayer = (int) (
						(ts)
						/
						(double)(clientOutputStreams.size()-1)
						);
				if(onplayer < 30) onplayer = 30;
				PLAYER_TIME = 3; // FIXME: onplayer;
				UN_DANG_TIME = 3; // FIXME: (level.getWidth()*level.getHeight())/400 + 60;
				isGameStarted = true;
				updateLeadBoard();
				time = UN_DANG_TIME;
			}
		}
		
	private void removePlayer(int _id) {
		try {
			clientOutputStreams.remove(_id);
			if(playersReady.indexOf(_id) != -1)
				playersReady.remove(playersReady.indexOf(_id));
			if(ghosts.indexOf(_id) == -1)
				score.remove(_id);

			updateLeadBoard();
			System.err.println(_id + " - left game");
			tellEveryone("-1%" + TYPE_REMOVEPLAYER + "|" + _id);
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
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

	Integer idd = 0;
	private void sortLeadBoard() {	
		HashMap<Integer, Long> leadBoard = new HashMap<Integer, Long>();
		for (int i = 0; i < score.size(); i++) {
			leadBoard.put(i, score.get(i));
		}

		leads.clear();
		leadBoardString = "";
		idd = 0;
		leadBoard.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(e -> {
			int id = leadBoard.size()-idd;
			if(ghosts.indexOf(e.getKey()) == -1)
				leads.add(e.getKey());
			leadBoardString = e.getKey() + "/" + id + ") #P" + e.getKey() + " (" + 
				(ghosts.indexOf(e.getKey()) == -1 ? e.getValue() : "Lose")
			+ ");" + leadBoardString;
			idd++;
		});
	}
	
	private void updateLeadBoard() {	
		sortLeadBoard();
		System.out.println(leadBoardString);
		tellEveryone("-1%" + TYPE_LEADBOARD + "|" + leadBoardString);
	}
}
