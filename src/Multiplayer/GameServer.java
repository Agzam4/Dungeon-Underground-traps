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
import java.util.ConcurrentModificationException;
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
	boolean sortLB = false;
	int setGhostID = -1;
	boolean isGameEnd = false;
	ServerSocket serverSock;

	public GameServer(LevelGenerator lg, int i) {
		time = Integer.MAX_VALUE;
		isGameEnd = false;
		isGameStarted = false;
		level = lg;
		clientOutputStreams = new ArrayList<PrintWriter>();
		playersReady = new ArrayList<Integer>();
		score = new ArrayList<Long>();
		ghosts = new ArrayList<Integer>();
		Thread thread = new Thread(() -> {
			try {
				serverSock = new ServerSocket(i);
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
								sortLB = true;
							}
						}else {
							checkAllReady();
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
						if(isGameStarted && clientOutputStreams.size() > 1 && !(ghosts.size() < clientOutputStreams.size()-1) && !isGameEnd) {
							System.err.println("\n\n\n!!! GAME END !!!\n > Players Lose\n\n");
							isGameEnd = true;
						}
						if(level.getScore() == 0 && !isGameEnd) {
							System.err.println("\n\n\n!!! GAME END !!!\n > Score: 0\n\n");
							isGameEnd = true;
						}
						if(clientOutputStreams.isEmpty() && isGameEnd) {
							System.err.println("\n\n\n#AUTO_EXIT\n\n\n");
							stop();
						}
					}
				});
				timer.start();
				while (running) {
					Socket clientSocket;
					clientSocket = serverSock.accept();
					PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
					clientOutputStreams.add(writer);
					score.add(0l);
					Thread t = new Thread(new ClientHandler(clientSocket, clientOutputStreams.size()-1));
					t.start();
					System.out.println("new Client!");
				}
				serverSock.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		thread.start();
	}

	public void stop() {
		running = false;
		try {
			serverSock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		clientOutputStreams.clear();
		playersReady.clear();
		ghosts.clear();
		score.clear();
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
					//					if(!dataType.equals(TYPE_POSITION))
					//						System.out.println("[ID: " + id + "], [DATA_TYPE: " + dataType + "], [DATA: " + data + "]");
					switch (dataType) {
					case TYPE_SETNAME:
						updateLeadBoard();
						break;
					case TYPE_PLAYERREADY:
						try {
							int idINT = Integer.parseInt(id);
							if(playersReady.indexOf(idINT) == -1)
								playersReady.add(idINT);
							updateLeadBoard();
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
							System.out.println("#Game Started!");
						}
						StringBuilder map = new StringBuilder("-1%" + TYPE_SETMAP + "|");
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
						tellEveryone("1%" + TYPE_SETTIME + "|" + time);
						updateLeadBoard();
						updateTime = false;
					}
					
					if(sortLB) {
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
						sortLB = false;
					}
					
					if(updateGhost) {
						System.err.println("setGhostID: " + setGhostID);
						tellEveryone("-1%" + TYPE_SETGHOST + "|" + setGhostID);
						ghosts.add(setGhostID);
						updateGhost = false;
					}
					if(changeMusic) {
						tellEveryone("-1%" + TYPE_CHANGEMUSIC + "|");
						changeMusic = false;
					}

					if(needUpdateLeadBoard) {
						sortLeadBoard();
						tellEveryone("-1%" + TYPE_LEADBOARD + "|" + leadBoardString);
						needUpdateLeadBoard = false;
					}

					if(sendStart) {
						tellEveryone("-1%" + TYPE_START + "| ");
						sendStart = false;
					}

					if(isGameEnd) { // TODO
						ghosts.clear();
						sortLeadBoard();
						String leadINFO = "";
						System.err.println("END DATA: ");
						for (int i = 0; i < clientOutputStreams.size(); i++) {
							System.err.println("Player #" + (i+1) + " Score: " + score.get(i));
						}
						System.err.println("leadboard");
						for (int i = 0; i < leads.size(); i++) {
							System.err.println((i+1) + ") Player #" + (leads.get(i) + 1) + " Score: " + score.get(leads.get(i)));
						}
						for (int i = 0; i < leads.size(); i++) {
							leadINFO = leads.get(i) + ":" + score.get(leads.get(i)) + ";" + leadINFO;
						}
						tellEveryone("-1%" + TYPE_END_GAME + "|" + leadINFO);
						//						for (int i = 0; i < clientOutputStreams.size(); i++) {
						//							removePlayer(i);
						//						}
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

	boolean sendStart = false;

	private void checkAllReady() {
		if(playersReady.size() == clientOutputStreams.size() && clientOutputStreams.size() > 1) {
			sendStart = true;
			int ts = (int) Math.round(((level.getWidth()*level.getHeight())/50_000d) * 60d);
			int onplayer = (int) (
					(ts)
					/
					(double)(clientOutputStreams.size()-1)
					);
			if(onplayer < 30) onplayer = 30;
			PLAYER_TIME = onplayer;
			UN_DANG_TIME = (level.getWidth()*level.getHeight())/400 + 60;
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
		} catch (IndexOutOfBoundsException | ConcurrentModificationException e) {
			System.err.println("Exception: " + e.getMessage() + " at server");
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

			String str = "" + (ghosts.indexOf(e.getKey()) == -1 ? e.getValue() : "Lose");
			if(!isGameStarted) {
				str = (playersReady.indexOf(e.getKey()) != -1 ? "READY" : "Waiting");
			}
			leadBoardString = e.getKey() + "/" + "#P" + e.getKey() + " (" + 
					str
					+ ");" + leadBoardString;
			idd++;
		});
	}

	boolean needUpdateLeadBoard = false;

	private void updateLeadBoard() {	
		System.out.println(playersReady.size());
		needUpdateLeadBoard = true;
	}
}
