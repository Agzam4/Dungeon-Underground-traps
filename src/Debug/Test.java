package Debug;


public class Test {
	
	static String blocks = "S ##>__^^=|_I^ @FN^``**B  V";

	public static void main(String[] args) {

		char[] sc = "A".toCharArray();
		long newSeed = Long.MIN_VALUE;
		for (int j = 0; j < sc.length; j++) {
			newSeed += sc[j] * (j+1) * Character.MAX_CODE_POINT;
			System.out.print((sc[j] * (j+1)* Character.MAX_CODE_POINT) + ":" + (int)sc[j] + " ");
		}
		System.out.println();
		System.out.println("Seed: " + newSeed);
		newSeed -= Long.MIN_VALUE;
		System.out.println("1: " + newSeed);
		int value = 0;
		int ii = 0;
		while (value < newSeed) {
			ii++;
			value += Character.MAX_CODE_POINT*ii;
		}
		value -= Character.MAX_CODE_POINT*(ii);
//		value -= Character.MAX_CODE_POINT*(ii-1);
		System.out.println("Value: " + value);
		System.out.println("Count: " + ii);

		System.out.println("2: " + newSeed/Character.MAX_CODE_POINT);
		System.out.println("3: " + (newSeed - value) + " - " + (newSeed - value)/Character.MAX_CODE_POINT);
		
		for (int i = 0; i < ii; i++) {
			
			System.out.println("3."  + i + ": " + (newSeed-Character.MAX_CODE_POINT*i));
		}
		
		System.out.println("3: " + newSeed);
		
		
		// 1,2,3,4, 5, 6, 7, 8, 9, 10
		// 1,3,6,10,15,21,28,36,45,55
		
		/**
		 * 0 1 2 3 4
		 * 9 8 7 6 5
		 * 
		 * sum = (MAX+MIN)*(MAX - MIN + 1)/2
		 * sum = ((MAX+MIN)*(MAX - MIN)+(MAX+MIN))/2
		 * sum = (MAX2 - MIN2 + MAX + MIN)/2
		 * sum = (9+0)*(9-0+1)/2 = 9*10/2 = 45
		 * 
		 */
		
		
//		System.out.println((Long.MAX_VALUE+"").length());
		System.out.println("MCP: " + Character.MAX_CODE_POINT);
//		System.out.println(Character.MIN_CODE_POINT);
//		LevelGenerator generator = new LevelGenerator();
//		generator.generate(50, 50);
//		
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
}
