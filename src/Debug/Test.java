package Debug;

import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Test {
	
	static String blocks = "S ##>__^^=|_I^ @FN^``**B  V";

	public static void main(String[] args) {

		Rectangle rectangle = new Rectangle(4,1,5,5);
		Rectangle rectangle2 = new Rectangle(0,5,5,5);

		System.out.println(rectangle.intersects(rectangle2));
//		int plusTime = 60*5;
//		for (int i = 50; i < 1050; i+=50) {
//			long blocks = i*i;
//			int ts = (int) Math.round(((blocks)/50_000d) * 60d);
//			System.err.println("Карта: " + i + "x" + i + ":   \tВсего времени ~" + getTime(ts+plusTime) + "\n");
//			for (int players = 2; players < 16; players+=1) {
//				int onplayer = (int) (
//						(ts)
//						/
//						(double)(players-1)
//						);
//				if(onplayer < 30) onplayer = 30;
//				System.out.println(" Игроков: " + players + "\t На игрока: " + getTime(onplayer) + "    \t\t Всего: " + getTime(onplayer * (players-1) + plusTime));//getTime(ts));
//			}
//					
//		}
//		
//		Field[] fields = java.awt.event.KeyEvent.class.getDeclaredFields();
//		for (Field f : fields) {
//		    if (Modifier.isStatic(f.getModifiers())) {
//		        System.out.println(f.getName());
//		    } 
//		}
		
//		char[] sc = "A".toCharArray();
//		long newSeed = Long.MIN_VALUE;
//		for (int j = 0; j < sc.length; j++) {
//			newSeed += sc[j] * (j+1) * Character.MAX_CODE_POINT;
//			System.out.print((sc[j] * (j+1)* Character.MAX_CODE_POINT) + ":" + (int)sc[j] + " ");
//		}
//		System.out.println();
//		System.out.println("Seed: " + newSeed);
//		newSeed -= Long.MIN_VALUE;
//		System.out.println("1: " + newSeed);
//		int value = 0;
//		int ii = 0;
//		while (value < newSeed) {
//			ii++;
//			value += Character.MAX_CODE_POINT*ii;
//		}
//		value -= Character.MAX_CODE_POINT*(ii);
////		value -= Character.MAX_CODE_POINT*(ii-1);
//		System.out.println("Value: " + value);
//		System.out.println("Count: " + ii);
//
//		System.out.println("2: " + newSeed/Character.MAX_CODE_POINT);
//		System.out.println("3: " + (newSeed - value) + " - " + (newSeed - value)/Character.MAX_CODE_POINT);
//		
//		for (int i = 0; i < ii; i++) {
//			
//			System.out.println("3."  + i + ": " + (newSeed-Character.MAX_CODE_POINT*i));
//		}
//		
//		System.out.println("3: " + newSeed);
//		
//		
//		// 1,2,3,4, 5, 6, 7, 8, 9, 10
//		// 1,3,6,10,15,21,28,36,45,55
//		
//		/**
//		 * 0 1 2 3 4
//		 * 9 8 7 6 5
//		 * 
//		 * sum = (MAX+MIN)*(MAX - MIN + 1)/2
//		 * sum = ((MAX+MIN)*(MAX - MIN)+(MAX+MIN))/2
//		 * sum = (MAX2 - MIN2 + MAX + MIN)/2
//		 * sum = (9+0)*(9-0+1)/2 = 9*10/2 = 45
//		 * 
//		 */
//		
//		
////		System.out.println((Long.MAX_VALUE+"").length());
//		System.out.println("MCP: " + Character.MAX_CODE_POINT);
////		System.out.println(Character.MIN_CODE_POINT);
////		LevelGenerator generator = new LevelGenerator();
////		generator.generate(50, 50);
////		
//		for (int y = 0; y < generator.getHeight(); y++) {
//			for (int x = 0; x < generator.getWidth(); x++) {
//				System.out.print(blocks.charAt(generator.getBlock(x, y)));
//
//			}
//			System.out.println();
//		}
//		System.out.println(getCutX(0));
//		System.out.println(getCutX(8));
//		System.out.println(getCutX(9));
//		System.out.println(getCutX(10));
//		System.out.println(getCutX(-1));
	}
	private static double getCutX(double x){
		if(x < 0) x = 10-((-x)%10);
		x = x%10;
		return x;
	}
	
	private static String getTime(int ts) {
		int s = ts%60;
		int m = (ts-s)/60; 
		return m + "мин " + s + "сек";
	}
}
