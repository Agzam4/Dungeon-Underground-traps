package Maps;

import java.io.Serializable;

public class MapSerializable implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int[][] map;
	public void setMap(int[][] map) {
		this.map = map;
	}
	public int[][] getMap() {
		return map;
	}
}
