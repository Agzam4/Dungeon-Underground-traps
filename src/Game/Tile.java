package Game;

import java.awt.Rectangle;
import java.io.Serializable;

import Stages.GameStage;

public class Tile implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int TS = 16;
	
	private static final int AIR = 0;
	private static final int WALL = 1; 
	private static final int PLATE = 2;
	private static final int SPIKY = 3;
	private static final int STICK = 4;
	private static final int HALF = 5;
	private static final int DOOR = 6;
	private static final int SPIKYUP = 7;
	private static final int GOLD = 8;
	private static final int LADDER = 9;
	private static final int SPIKYUPBLOCK = 10;
	private static final int HITDOOR = 11;
	private static final int UPHALF = 12;
	private static final int LAVA = 13;
	
	private static final int FINISH = 15;
	
	public static final int[] hitboxsTypes = {
		AIR, WALL, WALL, WALL, PLATE, PLATE, SPIKY,
		AIR, LADDER, STICK, HALF, DOOR, SPIKYUPBLOCK, AIR,
		WALL, FINISH, WALL, SPIKYUP, HITDOOR, HITDOOR, GOLD,
		GOLD, AIR, GOLD, AIR, SPIKYUP, WALL, STICK,
		UPHALF, LAVA, WALL, HALF, SPIKY, AIR, AIR
	};

	Rectangle rectangle;
	Rectangle ladder;
	Rectangle touchbox;
	Rectangle redzome;
	int type;
	int x, y;
	
	public Tile(int type, int x, int y) {
		this.x = x;
		this.y = y;
		this.type = type;
		touchbox = new Rectangle(0, 0, 0, 0);
		redzome =  new Rectangle(0, 0, 0, 0);
		switch (hitboxsTypes[type]) {
		case AIR:
			rectangle = new Rectangle(x, y, 0, 0);
			ladder = new Rectangle(x, y, 0, 0);
			break;
		case WALL:
			rectangle = new Rectangle(x, y, TS, TS);
			ladder = new Rectangle(x, y, 0, 0);
			break;
		case PLATE:
			rectangle = new Rectangle(x, y, 0, 0);
			touchbox = new Rectangle(x+3, y+TS-2, 10, 2);
			ladder = new Rectangle(x, y, 0, 0);
			break;
		case SPIKY:
			rectangle = new Rectangle(x, y+9, TS, TS-9);
			ladder = new Rectangle(x, y, 0, 0);
			redzome = new Rectangle(x+1, y+1, TS-2, TS-1);
			break;
		case STICK:
			rectangle = new Rectangle(x+6, y, 4, TS-6);
			ladder = new Rectangle(x, y, 0, 0);
			break;
		case HALF:
			rectangle = new Rectangle(x, y+TS/2, TS, TS/2);
			ladder = new Rectangle(x, y, 0, 0);
			break;
		case DOOR:
			rectangle = new Rectangle(x+2, y, TS-4, TS);
			ladder = new Rectangle(x, y, 0, 0);
			break;
		case SPIKYUP:
			rectangle = new Rectangle(x, y, TS, TS-9);
			ladder = new Rectangle(x, y, 0, 0);
			redzome = new Rectangle(x+2, y+1, TS-4, TS-1);
			break;
		case GOLD:
			rectangle = new Rectangle(x, y, 0, 0);
			ladder = new Rectangle(x, y, 0, 0);
			touchbox = new Rectangle(x+7, y+7, 2, 2);
			break;
		case LADDER:
			rectangle = new Rectangle(x, y, 0, 0);
			ladder = new Rectangle(x+1, y, TS-2, TS);
			break;
		case SPIKYUPBLOCK:
			rectangle = new Rectangle(x, y, TS, TS);
			ladder = new Rectangle(x, y, 0, 0);
			break;
		case HITDOOR:
			rectangle = new Rectangle(x, y, 0, 0);
			ladder = new Rectangle(x, y, 0, 0);
			touchbox = new Rectangle(x, y, TS, TS);
			break;
		case UPHALF:
			rectangle = new Rectangle(x, y, TS, TS/2);
			ladder = new Rectangle(x, y, 0, 0);
			break;
		case LAVA:
			rectangle = new Rectangle(x, y+TS/2, TS, TS/2);
			ladder = new Rectangle(x, y, 0, 0);
			redzome = new Rectangle(x+1, y, TS-2, TS);
			break;
		case FINISH:
			rectangle = new Rectangle(x, y, 0, 0);
			ladder = new Rectangle(x, y, 0, 0);
			redzome = new Rectangle(x+1, y+4, TS-2, TS-4);
			break;
		default:
			break;
		}
	}
	
	public Rectangle getRectangle() {
		if(hitboxsTypes[type] == SPIKYUPBLOCK) {
			return GameStage.crusserY > 5 ? new Rectangle(x, y, TS, TS) : new Rectangle(x+1,y+1,0,0);
		}
		return rectangle;
	}
	
	public boolean touchPlayer;
	
	public Rectangle getLadder() {
		return ladder;
	}
	public Rectangle getTouchbox() {
		return touchbox;
	}
	
	public Rectangle getRedzome() {
		if(hitboxsTypes[type] == SPIKYUPBLOCK) {
			return GameStage.crusserY > 5 ? new Rectangle(x+1,y,TS-2,TS) : new Rectangle(x,y,0,0);
		}
		return redzome;
	}
	
	public int getType() {
		return hitboxsTypes[type];
	}
}
