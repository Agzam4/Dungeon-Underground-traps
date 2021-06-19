package Work;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import Game.Tile;

public class LevelGenerator {

	public final int BLOCK_AIR = 1;
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
	
	/////////////////////////////////

	static final float RARE_SPYKY = 1f/7f;//7
	static final float RARE_DART = 1f/5f;//5
	static final float RARE_DOOR = 1f/10f;
	static final float RARE_CRUSHER = 1f/1f;
	static final float RARE_LAKE_OF_LAVA = 1f/5f;//5
	static final float RARE_LAVA_H = 1f/2f;
	static final float RARE_LAVA_W = 25f;
	
	static final float RARE_AIR = 1f/10f;
	static final float RARE_TREASURES = 1f/5f;
	static final float RARE_DIAMOND = 1f/5f;
	static final float RARE_LAVA_TREASURES = 2f/3f;
	static final float RARE_LAVA_DIAMOND = 1f/5f;
	static final float RARE_TNT = 1f/5f;
	
	
	private Random random;
	private long seed;

	private int[][] level;
	private Tile[][] tiles;
	int height;
	int width;

	public int gold;
	public int diamonds;
	
	
	public LevelGenerator() {
		// Генерация seed 
		Random rand = new Random();
		seed = rand.nextLong();
		checkSeed();
		random = new Random(seed);
	}
	
	public LevelGenerator(long seed) {
		this.seed = seed;
		checkSeed();
		random = new Random(seed);
	}
	
	private void checkSeed() {
		if(GameData.lastSeed != null) {
			if(GameData.lastSeed.equals(seed)) {
				seed += 1;
			}
		}
		GameData.lastSeed = seed;
		System.out.println("Seed: " + seed);
	}
	

	int air;
	int tileX;
	int tileY;
	int dir;
	int roomX;
	int roomY;
	int fx; // Финиш X
	int fy; // Финиш Y
	
	public void generate(int w, int h) {
		finishPoints = new ArrayList<Point>();
		level = new int[w][h];
		height = h;
		width = w;
		
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				level[x][y] = 2;
			}
		}
		
		tileX = w/2;
		tileY = h/2;
		
		int tryings = 0;
		boolean generating = true;
		while (generating) {
			for (int i = 0; i < h*w/2; i++) {
				try {
					room();
				} catch (ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
				}
			}
			if(tileX != width/2 && tileX-1 != width/2 && tileX+1 != width/2) {
				if(tileY != height/2 && tileY-1 != height/2 && tileY+1 != height/2) {
					finishPoints.add(new Point(tileX, tileY));
				}
			}
			System.out.println("# " +finishPoints.size());
			generating = finishPoints.size() == 0;
			tryings ++;
			if(tryings > 10) {
				finishPoints.add(new Point(width/2, height/2 - 3));
				generating = false;
			}
		}
		
		// Старт
		tileX = width/2;
		tileY = height/2;
		
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				setBlock(tileX+x, tileY+y, BLOCK_AIR);
			}
		}
		setBlock(tileX, tileY, BLOCK_SOLID);

		Point end = finishPoints.get(random.nextInt(finishPoints.size()));
		finish(end.x, end.y);
		
		
		tiles = new Tile[w][h];

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				tiles[x][y] = new Tile(level[x][y]-1, x*16, y*16);
			}
		}
		
		calculate();
	}
	
	ArrayList<Point> finishPoints;
	
	private void calculate() {
		gold = 0;
		diamonds = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if(level[x][y] == BLOCK_GOLD) {
					gold++;
				}
				if(level[x][y] == BLOCK_DIAMOND) {
					diamonds++;
				}
			}
		}
	}
	
	private void finish(int xx, int yy) {
		// Финиш
		fx = xx;
		fy = yy;
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				setBlock(xx+x, yy+y, BLOCK_LADDER);
			}
		}
		setBlock(xx, yy, BLOCK_SOLID);
		setBlock(xx, yy-1, BLOCK_CHEST);
	}
	
	private void room() {
		roomX = 0;
		roomY = 0;
		ArrayList<Integer> dirs = new ArrayList<Integer>();
		if(2 == getBlock(tileX+roomX, tileY+roomY+1))
			dirs.add(2);
		if(2 == getBlock(tileX+roomX, tileY+roomY-1))
			dirs.add(0);
		if(2 == getBlock(tileX+roomX+1, tileY+roomY))
			dirs.add(1);
		if(2 == getBlock(tileX+roomX-1, tileY+roomY))
			dirs.add(3);
		
//		System.out.println(random.nextInt(dirs.size()) + "/" + dirs.size());
		if(dirs.size() < 1) {
			if(tileX != width/2 && tileY != height/2)
				finishPoints.add(new Point(tileX, tileY));
			tileX = width/2;
			tileY = height/2;
			return;
		}
		dir = dirs.get(random.nextInt(dirs.size()));
		
		
		if(dir == 0) { // Вверх
			while (!(-roomY > random.nextDouble()*4 +1 ||
					1 == getBlock(tileX+roomX,tileY+roomY-1) || 
					2 != getBlock(tileX+roomX,tileY+roomY-1)
					)) {
				setBlock(tileX+roomX, tileY+roomY, 9);
				rope();
				roomY--;
				if(5 < air) {
					spiky(tileX+roomX, tileY+roomY);
				}
			}
			if(1 == getBlock(tileX+roomX,tileY+roomY-1) || 2 == getBlock(tileX+roomX,tileY+roomY-1)) {
				setBlock(roomX, roomY, 9);
			}
			tileX += roomX;
			tileY += roomY;
		}
		if(dir == 1) { // Направо
			while ((9 > roomX) && 2 == getBlock(tileX+roomX+1, tileY+roomY)) {
				treasures(tileX+roomX, tileY+roomY);
				rope();
//				if(5 < air) {
					// TODO: update
					spiky(tileX+roomX, tileY+roomY);
					dart(tileX+roomX, tileY+roomY, false);
					door(tileX+roomX, tileY+roomY);
					// TODO: Saw
					crusher(tileX+roomX, tileY+roomY);
//				}
				roomX++;
			}
			if(1 == getBlock(tileX+roomX,tileY+roomY-1) || 2 == getBlock(tileX+roomX,tileY+roomY-1)) {
				setBlock(roomX, roomY, 9);
			}
			tileX += roomX;
			tileY += roomY;
		}
		if(dir == 2) { // Вниз
			while (!(
					roomY > random.nextDouble()*4 +1 ||
					1 == getBlock(tileX+roomX,tileY+roomY-1) || 
					2 != getBlock(tileX+roomX,tileY+roomY-1)
					)) {
				setBlock(tileX+roomX, tileY+roomY, 9);
				rope();
				if(5 < air) {
					spiky(tileX+roomX, tileY+roomY);
				}
				roomY++;
			}
			if(1 == getBlock(tileX+roomX,tileY+roomY-1) || 2 == getBlock(tileX+roomX,tileY+roomY-1)) {
				setBlock(roomX, roomY, 9);
			}
			tileX += roomX;
			tileY += roomY;
		} 
		if(dir == 3) { // Влево
			while ((-9 < roomX) && 2 == getBlock(tileX+roomX-1, tileY+roomY)) { //(1 != getBlock(tileX, tileY-1)
				treasures(tileX+roomX, tileY+roomY);
				// TODO: update
				rope();
//				if(5 < air) {
					// Лава
					tnt(tileX+roomX, tileY+roomY);
					lakeOfLava(tileX+roomX, tileY+roomY);
					spiky(tileX+roomX, tileY+roomY);
					// TODO
//				}
				roomX--;
			}
			if(1 == getBlock(tileX+roomX,tileY+roomY-1) || 2 == getBlock(tileX+roomX,tileY+roomY-1)) {
				setBlock(roomX, roomY, 9);
			}
			tileX += roomX;
			tileY += roomY;
		}
		
//		if(2 != getBlock(tileX+roomX, tileY+roomY+1)) {
//			if(2 != getBlock(tileX+roomX, tileY+roomY-1)) {
//				if(2 != getBlock(tileX+roomX+1, tileY+roomY)) {
//					if(2 != getBlock(tileX+roomX-1, tileY+roomY)) {
//					}
//				}
//			}
//		}
		
		air--;
	}
	
	private void tnt(int x, int y) {
		if(random.nextDouble() < RARE_TNT) {
			if(getBlock(x, y) == BLOCK_AIR) {
				if(isStone(x, y+1)) {
					if(isStone(x, y+2)) {
						if(isStone(x, y+4)) {
							if(isStone(x, y+5)) {
								setBlock(x, y, BLOCK_PLATE);
								setBlock(x, y+1, BLOCK_MOSSY_STONE);
								setBlock(x, y+2, BLOCK_TNT);
								setBlock(x, y+4, BLOCK_SPIKY);
								setBlock(x, y+5, BLOCK_MOSSY_STONE);
							}
						}
					}
				}
			}
		}
	}


	int lava = 0;
	
	
	private void lakeOfLava(int x, int y) {
		if(random.nextDouble() < RARE_LAKE_OF_LAVA) {
			int lw = 16;
			for (int lx = 1; lx < 16; lx++) {
				for (int ly = -2; ly < 3; ly++) {
					if(!isStone(x - lx, y + ly)) {
						lw = (int) (Math.floor((lx-1)/2)*2+1);
						lx = 99;
						ly = 99;
						break;
					}
				}
			}
			

			if(lw > 2) {
				for (int lx = 1; lx < lw; lx++) {
//					for (int ly = -2; ly < 2; ly++) {
					setBlock(x-lx, y+2, BLOCK_LAVA);
					if(lx % 2 == 0) {
						setBlock(x-lx, y+1, BLOCK_AIR);
						setBlock(x-lx, y, BLOCK_AIR);
						setBlock(x-lx, y-1, BLOCK_AIR);
					}else{
						if(random.nextBoolean()) {
							setBlock(x-lx, y+1, BLOCK_STICK);
							setBlock(x-lx, y, BLOCK_STICK_BLOCK);
							lavaTreasures(x-lx, y-1);
						}else {
							setBlock(x-lx, y+1, BLOCK_STICK_BLOCK);
							lavaTreasures(x-lx, y);
							setBlock(x-lx, y-1, BLOCK_AIR);
						}
						
					}
				}
				setBlock(x-lw, y-1, BLOCK_AIR);
				setBlock(x-lw, y, BLOCK_AIR);
				setBlock(x-lw, y+1, BLOCK_MAGMA);
				setBlock(x-lw, y+2, BLOCK_MAGMA);

				setBlock(x-1, y-1, BLOCK_AIR);
				setBlock(x-1, y, BLOCK_AIR);
				setBlock(x-1, y+1, BLOCK_MAGMA);
				setBlock(x-1, y+2, BLOCK_MAGMA);
				
				roomX-= lw;
			}
			
			
//			if(2 == getBlock(tileX+roomX, tileY+roomY+1)) {
//				if(2 == getBlock(tileX+roomX, tileY+roomY-1)) {
//					if(2 == getBlock(tileX+roomX, tileY+roomY+2)) {
//						if(2 == getBlock(tileX+roomX, tileY+roomY-2)) {
//							setBlock(tileX+roomX, tileY+roomY+1, 31);
//							setBlock(tileX+roomX, tileY+roomY-1, 1);
//							setBlock(tileX+roomX, tileY+roomY-2, 1);
//							setBlock(tileX+roomX, tileY+roomY+2, 31);
//							lava = 0;
//							lava();
//						}
//					}
//				}
//				
//			}
		}
	}
	
	private void zam2(int x, int y) {
		if(random.nextDouble() < RARE_LAVA_TREASURES) {
			setBlock(x, y, 1);
		}else {
			if(random.nextDouble() < RARE_LAVA_DIAMOND) {
				setBlock(x, y, 22);
				diamonds++;
			}else {
				setBlock(x, y, 21);
				gold++;
			}
		}
	}
	
	private void LAVA() {
		setBlock(tileX+roomX+lava, tileY+roomY+2, 30);
		setBlock(tileX+roomX+lava, tileY+roomY+3, 31);
		if(0 != getBlock(tileX+roomX+lava-1, tileY+roomY+1)) {
			setBlock(tileX+roomX+lava, tileY+roomY+1, 1);
			setBlock(tileX+roomX+lava, tileY+roomY, 1);
		} else {
		}
		if(lava%2 == 0) {
			if(random.nextDouble() < RARE_LAVA_H) {
				setBlock(tileX+roomX+lava, tileY+roomY+1, 28);
				setBlock(tileX+roomX+lava, tileY+roomY, 29);
				zam2(tileX+roomX+lava, tileY+roomY-1);
			}else {
				setBlock(tileX+roomX+lava, tileY+roomY+1, 29);
				zam2(tileX+roomX+lava, tileY+roomY-1);
			}
		}

		setBlock(tileX+roomX+lava, tileY+roomY-1, 1);
		if (random.nextDouble() < (RARE_LAVA_W-lava)/RARE_LAVA_W) {
			lava();
		}else {
			endLava();
		}
	}
	
	private void lava() {
		lava++;
		if(2 == getBlock(tileX+roomX+lava, tileY+roomY+1) &&
			2 == getBlock(tileX+roomX+lava, tileY+roomY+2) &&
			2 == getBlock(tileX+roomX+lava, tileY+roomY+3)) {
			LAVA();
		}else {
			endLava();
		}
	}
	
	private void endLava() {
		lava--;
		setBlock(tileX+roomX+lava, tileY+roomY-1, 1);
		setBlock(tileX+roomX+lava, tileY+roomY, 1);
		setBlock(tileX+roomX+lava, tileY+roomY+1, 31);
		setBlock(tileX+roomX+lava, tileY+roomY+2, 31);
	}

	private void crusher(int x, int y) {
		if(random.nextDouble() < RARE_CRUSHER) {
			if(1 == getBlock(x, y) &&
					isStone(x, y-2) && isStone(x, y-1) && isStone(x, y+1) && isStone(x, y+2) && 
					isStone(x-1, y+1) && isStone(x+1, y+1)) {
				setBlock(x, y-2, 3);
				setBlock(x, y-1, 26);
				setBlock(x, y, 13);
				setBlock(x, y+1, 27);
				setBlock(x-1, y+1, 3);
				setBlock(x+1, y+1, 3);
			}
		}
	}

	private void door(int x, int y) {
		if(random.nextDouble() < RARE_DOOR) {
			if(isStone(x+1, y) && isStone(x+1, y+1) && isStone(x+1, y-2) &&
					1 == getBlock(x, y) && isStone(x, y+1) && 
					isStone(x+2, y-1) && isStone(x+2, y-2)) {
				setBlock(x+1, y, BLOCK_STONE_DOOR);
				setBlock(x+1, y-1, BLOCK_STONE_DOOR);
				setBlock(x+1, y+1, BLOCK_MOSSY_STONE);
				setBlock(x+1, y-2, BLOCK_MOSSY_STONE);
				setBlock(x, y, BLOCK_PLATE);
				setBlock(x, y+1, BLOCK_MOSSY_STONE);
				setBlock(x+2, y-1, BLOCK_AIR);
				setBlock(x+2, y-2, BLOCK_SPIKY_UP);
				dart(x, y, true);
			}
		}
	}

	private void setAirblock(int x, int y) {
		int type = getBlock(x, y);
		if(BLOCK_LADDER != type && BLOCK_PLATE != type)
			setBlock(x, y, BLOCK_AIR);
	}
	
	private boolean isReplaseble(int x, int y) {
		int type = getBlock(x, y);
		return type == BLOCK_AIR || type == BLOCK_LADDER || type == BLOCK_STONE || type == BLOCK_PLATE;
	}
	
	private boolean isReplaseble_noPlate(int x, int y) {
		int type = getBlock(x, y);
		return type == BLOCK_AIR || type == BLOCK_LADDER || type == BLOCK_STONE;
	}
	
//	private boolean isVoidBlock(int x, int y) {
//		int type = getBlock(x, y);
//		return type == BLOCK_AIR || type == BLOCK_LADDER || type == BLOCK_PLATE;
//	}
	
	private boolean isStone(int x, int y) {
		return BLOCK_STONE == getBlock(x, y);
	}
	

	private boolean isSpikys(int x, int y) {
		int type = getBlock(x, y);
		return type == BLOCK_SPIKY || type == BLOCK_SPIKY_UP;
	}
	private boolean isCrusher(int x, int y) {
		return getBlock(x, y) == BLOCK_SPIKY_FALL;
	}
	
	private void dart(int x, int y, boolean b) {
		if(random.nextDouble() < RARE_DART || b) {
			for (int xx = 0; xx < 5; xx++) {
				if(!isReplaseble(x-xx, y))
					return;
			}
			if(5 != getBlock(x, y) && isStone(x, y+1) && isStone(x+1, y-1) && isStone(x-5, y)) {//2 == getBlock(tileX+roomX-5, tileY+roomY)) {
				for (int xx = 0; xx < 5; xx++) {
					setAirblock(x-xx, y);
				}
				setBlock(x, y, BLOCK_PLATE);
				setBlock(x, y+1, BLOCK_MOSSY_STONE);
				setAirblock(x-1, y);
				setBlock(x-5, y, BLOCK_DART);
				setBlock(x+1, y-1, BLOCK_AIR);
			}
		}
	}

	private void treasures(int x, int y) {
		if(isStone(x, y)) {
			if(random.nextDouble() < RARE_TREASURES) {
				// Сокровища
				if(random.nextDouble() < RARE_DIAMOND) {
					// Алмаз
					setBlock(x, y, BLOCK_DIAMOND);
					diamonds++;
				} else {
					// Золото
					setBlock(x, y, BLOCK_GOLD);
					gold++;
				}
			} else {
				// Пустой блок
				setBlock(x, y, BLOCK_AIR);
			}
		}
	}
	
	private void lavaTreasures(int x, int y) {
		if(isStone(x, y)) {
			if(random.nextDouble() < RARE_LAVA_TREASURES) {
				// Сокровища
				if(random.nextDouble() < RARE_LAVA_DIAMOND) {
					// Алмаз
					setBlock(x, y, BLOCK_DIAMOND);
					diamonds++;
				} else {
					// Золото
					setBlock(x, y, BLOCK_GOLD);
					gold++;
				}
			} else {
				// Пустой блок
				setBlock(x, y, BLOCK_AIR);
			}
		}
	}
	
	private void spiky(int x, int y) {
		if(random.nextDouble() < RARE_SPYKY) {
			if(isStone(x, y-1) && isStone(x, y+1) && isStone(x, y+2) &&
					isStone(x-1, y-1) && isStone(x+1, y-1)
					
//					&& 2 == getBlock(x, y+1) &&
//					2 == getBlock(x-1, y-1) &&
//							2 == getBlock(x+1, y-1)
							) {
				setBlock(x, y-1, 1);
				setBlock(x, y+1, 7);
				setBlock(x, y+2, 3);
				setBlock(x-1, y-1, 1);
				setBlock(x+1, y-1, 1);
//				if (9 == getBlock(x, y)) {
//					setBlock(x, y-1, 9);
//				}
//				if (9 == getBlock(x+1, y)) {
//					setBlock(x-1, y-1, 9);
//				}
//				if (9 == getBlock(x-1, y)) {
//					setBlock(x-1, y-1, 9);
//				}
			}
		}
	}

	private void rope() {
		if(9 == getBlock(tileX+roomX, tileY+roomY+1)) {
			setBlock(tileX+roomX, tileY+roomY-1, 9);
			setBlock(tileX+roomX, tileY+roomY, 9);
		}
		if(9 == getBlock(tileX+roomX, tileY+roomY-1)) {
			setBlock(tileX+roomX, tileY+roomY+1, 9);
			setBlock(tileX+roomX, tileY+roomY, 9);
		}
	}
	
	public int getBlock(int x, int y) {
		if(x < 0) x = width-((-x)%width);
		if(y < 0) y = height-((-y)%height);
		x = x%width;
		y = y%height;
		return level[x][y];
	}
	
	private void setBlock(int x, int y, int type) {
		if(x < 0) x = width-((-x)%width);
		if(y < 0) y = height-((-y)%height);
		x = x%width;
		y = y%height;
		level[x][y] = type;
	}
	
	public long getSeed() {
		return seed;
	}
	
	public int[][] getLevel() {
		return level;
	}
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}

	public int getFinishX(){
		return fx;
	}
	public int getFinishY(){
		return fy;
	}
	
	public Rectangle getHitbox(int x, int y, int type) {
		if(x < 0) x = width-((-x)%width);
		if(y < 0) y = height-((-y)%height);
		x = x%width;
		y = y%height;
		switch (type) {
		case 0:
			return tiles[x][y].getRectangle();
		case 1:
			return tiles[x][y].getLadder();
		case 2:
			return tiles[x][y].getTouchbox();
		case 3:
			return tiles[x][y].getRedzome();
		default:
			break;
		}
		return tiles[x][y].getRectangle();
	}
	
	
	public int getType(int x, int y) {
		return Tile.hitboxsTypes[getBlock(x, y)-1];
	}
	
	public void setHit(int x, int y, boolean isHit) {
		if(x < 0) x = width-((-x)%width);
		if(y < 0) y = height-((-y)%height);
		x = x%width;
		y = y%height;
		if(tiles[x][y].touchPlayer == isHit)
//			return;
			;
		if(isHit) {
//			if(level[x][y] == BLOCK_PLATE) {
//				setTile(x, y, BLOCK_PLATE_ACTIVATE);
//			}
		}
		tiles[x][y].touchPlayer = isHit;
	}
	
	public boolean isHit(int x, int y) {
		if(x < 0) x = width-((-x)%width);
		if(y < 0) y = height-((-y)%height);
		x = x%width;
		y = y%height;
		return tiles[x][y].touchPlayer;
	}

	public void setTile(int x, int y, int type) {
		if(x < 0) x = width-((-x)%width);
		if(y < 0) y = height-((-y)%height);
		x = x%width;
		y = y%height;
		level[x][y] = type;
		tiles[x][y] = new Tile(type-1, x*16, y*16);
	}
	
	public void deleteTraps(int x, int y) {
		for (int xx = -2; xx < 3; xx++) {
			for (int yy = -2; yy < 3; yy++) {
				if(isSpikys(xx+x, yy+y)) {
					setTile(xx+x, yy+y, BLOCK_AIR);;
				}
				if(isCrusher(xx+x, yy+y)) {
					setTile(xx+x, yy+y, BLOCK_LADDER);;
				}
				if(BLOCK_SPIKY_ONE == getBlock(xx+x, yy+y)) {
					setTile(xx+x, yy+y, BLOCK_AIR);;
				}
				if(BLOCK_LAVA == getBlock(xx+x, yy+y)) {
					if(xx == 0) {
						setTile(xx+x, yy+y, BLOCK_MAGMA_HALF);
						setTile(xx+x, yy+y-1, BLOCK_AIR);
					}else {
						setTile(xx+x, yy+y, BLOCK_MAGMA);
						if(getBlock(xx+x, yy+y-1) == BLOCK_AIR)
							setTile(xx+x, yy+y-1, BLOCK_MAGMA_HALF);
						else {
							setTile(xx+x, yy+y-1, BLOCK_MAGMA);
							setTile(xx+x, yy+y-2, BLOCK_MAGMA_HALF);
						}
					}
				}
			}
		}
	}
}
