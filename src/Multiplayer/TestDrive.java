package Multiplayer;


import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Work.LevelGenerator;

public class TestDrive {
	
	static String leadBoardString = "";

	public static void main(String[] args) {
//		LevelGenerator lg = new LevelGenerator();
//		lg.generate(100, 100);
//		GameServer server = new GameServer(lg, 5000);
////		server.stop();
//		
//		GameClient client = new GameClient(InetAddress.getLoopbackAddress().getHostName(), 5000, null);
		
		ArrayList<Long> score = new ArrayList<Long>();
		score.add(500l);
		score.add(200l);
		score.add(300l);
		score.add(100l);
		score.add(200l);
 		
		HashMap<Integer, Long> leadBoard = new HashMap<Integer, Long>();
		for (int i = 0; i < score.size(); i++) {
			leadBoard.put(i, score.get(i));
		}
		
		ArrayList<Integer> leads = new ArrayList<Integer>();
		leadBoardString = "";
		leadBoard.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(e -> {
			leads.add(e.getKey());
			leadBoardString += leads.size() + ") Player" + e.getKey() + " (" + e.getValue() + ")\n";
		});
		System.out.println(leadBoardString);
	}
}
