package Debug;

import Work.LevelGenerator;

public class Test {
	
	static String blocks = "S ##>__^^=|_I^ @FN^``**B  V";

	public static void main(String[] args) {
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
		System.out.println(getCutX(0));
		System.out.println(getCutX(8));
		System.out.println(getCutX(9));
		System.out.println(getCutX(10));
		System.out.println(getCutX(-1));
	}
	private static double getCutX(double x){
		if(x < 0) x = 10-((-x)%10);
		x = x%10;
		return x;
	}
}
