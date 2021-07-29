package Multiplayer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import Stages.Maneger;
import Work.GameData;
import Work.LevelGenerator;

public class StaticServer {

	private static GameServer server;
	private static ArrayList<GameServer> gameServers = new ArrayList<GameServer>();
	
	public static GameClient startServer(int w, int h, int sock, Maneger m) {
		stop();
		LevelGenerator lg = new LevelGenerator(); // FIXME
		lg.generate(w, h, LevelGenerator.TYPE_ONLINE_REACHER);
		Thread t = new Thread() {
			public void run() {
				server = new GameServer(lg, sock);
				gameServers.add(server);
				System.err.println("\n\n#CREATED_SERVER: " + server);
			};
		};
		t.start();
		System.err.println(InetAddress.getLoopbackAddress().getHostAddress());
		try {
			return new GameClient(InetAddress.getLoopbackAddress().getHostAddress(), sock, m);
		} catch (IOException e) {
			return null;
		}
	}
	
	public static void stop() {
		for (int i = 0; i < gameServers.size(); i++) {
			gameServers.get(i).stop();
			System.err.println("Server close: " + gameServers.get(i));
		}
		gameServers.clear();
//		System.err.print("Closing server (" + server + "): ");
//		if(server == null) { 
//			System.err.println("Server == null");
//			return;
//		}
//		server.stop();
//		server = null;
//		System.err.println("Server is close");
	}
}
