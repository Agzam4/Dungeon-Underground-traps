package Maps;

public enum Blocks {

	AIR ("Air"),
	STONE ("Stone"),
	MOSSY_STONE ("Mossy Stone");
	
	String name;
	
	private Blocks(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return " " + name + " ";
	}
	
	public int getID() {
		return ordinal()+1;
	}
}

/**
 * public final int BLOCK_AIR = 1;
	public final int BLOCK_STONE = 2;
	public final int BLOCK_MOSSY_STONE = 3;
	public final int BLOCK_DART = 4;
	public final int BLOCK_PLATE = 5;
	public final int BLOCK_PLATE_ACTIVATE = 6;
	public final int BLOCK_SPIKY = 7;
	public final int BLOCK_SPIKY_OFF = 8;
	public final int BLOCK_LADDER = 9;
	public final int BLOCK_STONE_STICK = 10;
	public final int BLOCK_STONE_HALF = 11;
	public final int BLOCK_STONE_DOOR = 12;
	public final int BLOCK_SPIKY_ONE = 13;
	public final int BLOCK_NULL = 14;
	public final int BLOCK_SOLID = 15;
	public final int BLOCK_CHEST = 16;
	public final int BLOCK_TNT= 17;
	public final int BLOCK_SPIKY_UP = 18;
	public final int BLOCK_DOOR_OPEN_DOWN = 19;
	public final int BLOCK_DOOR_OPEN_UP = 20;
	public final int BLOCK_GOLD = 21;
	public final int BLOCK_DIAMOND = 22;
	public final int BLOCK_BOOM = 23;
	public final int BLOCK_NULL2 = 24;
	public final int BLOCK_NULL3 = 25;
	public final int BLOCK_SPIKY_FALL = 26;
	public final int BLOCK_IRON = 27;
	public final int BLOCK_STICK = 28;
	public final int BLOCK_STICK_BLOCK = 29;
	public final int BLOCK_LAVA = 30;
	public final int BLOCK_MAGMA = 31;
	public final int BLOCK_MAGMA_HALF = 32;
	public final int BLOCK_WATER = 33;
	public final int BLOCK_SAW = 34;
	public final int BLOCK_SAW_LINE = 35;
 * 
 */
