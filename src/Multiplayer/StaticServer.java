package Multiplayer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import Stages.Maneger;
import Work.LevelGenerator;

public class StaticServer {

	private static GameServer server;
	
	public static GameClient startServer(int w, int h, int sock, Maneger m) {
		LevelGenerator lg = new LevelGenerator();
		lg.generate(w, h, LevelGenerator.TYPE_ONLINE_REACHER);
		Thread t = new Thread() {
			public void run() {
				server = new GameServer(lg, sock);
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
		if(server == null) return;
		server.stop();
		server = null;
	}
	
}
