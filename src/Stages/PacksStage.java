package Stages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Main.GamePanel;
import Objects.Button;
import Work.GameData;
import Work.LevelGenerator;
import Work.Loader;
import Work.MouseController;
import Work.MyFile;

public class PacksStage extends Stage {

	String[] packs = MyFile.packs;
	int onMouse = -1;

	int packBlockH = 110;
	int packBlockDis = 25;
	int downBlockH = 120;
	int pad = 30;
	
	Maneger maneger;
	Button button;
	
	public PacksStage(Maneger maneger) {
		button = new Button(GameData.texts[GameData.TEXT_BACK], 0, 0);
		button.chageSize = false;
		this.maneger = maneger;
	}

	double sy;
	double sy2;
	double vy;
	int pageH = 0;
	int count;
	
	@Override
	public void draw(Graphics2D g, Graphics2D gf) {
		// TODO: Scroller
			
		int bh = packBlockH+packBlockDis;
//		double stageH = GamePanel.frameH-downBlockH;
////		double minY = ((-vale)/(double)(packBlockH+packBlockDis))-packs.length;
//		//stageH + packBlockDis - 60 downBlockH +packs.length*bh - packBlockDis+pad*2
//		double pageH = (int) ((packs.length + count)*bh - packBlockDis+pad*2);//packBlockDis;  - 60 - packBlockDis + GamePanel.frameH
//		int hhh = - (int) (sy*bh);
//		int scrollBarH = (int) stageH*2;
//		int scrollH = (int) (scrollBarH/((pageH/stageH)));
//		int scrollY = (int) (((scrollBarH-scrollH)*hhh)/pageH);
//		System.out.println(count);
		// null		 | +stageH	  | -stageH
		// 980.0 338 | 1556.0 213 | 404.0 821
		
		
//		if(scrollY < 30) {
//			scrollH += (scrollY-30); 
//			scrollY = 30;
//		}
//		if(scrollY+scrollH > ) {
//			scrollH += (scrollY-hhh); 
//			scrollY = hhh-scrollH;
//		}
		
		int id = 0;//(int) Math.floor(-sy);
		button.draw(gf);
		drawString(gf, MyFile.getPackName(), 25, GamePanel.frameH- (int) (packBlockH/3d*2));

//		gf.setColor(Color.WHITE);
//		gf.drawRect(GamePanel.frameW - 30, 0, 30, (int) stageH);
//		gf.drawRect(GamePanel.frameW - 30, scrollY-1, 30, scrollH+1);
//		gf.setColor(new Color(0,0,0,200));
//		gf.fillRect(GamePanel.frameW - 30, scrollY, 30, scrollH);
		
		gf.setColor(Color.WHITE);
		gf.drawLine(0, GamePanel.frameH-downBlockH, GamePanel.frameW, GamePanel.frameH-downBlockH);
		gf.setColor(Color.BLACK);
		gf.fillRect(0, GamePanel.frameH-downBlockH, GamePanel.frameW, downBlockH);

		
		for (int y = pad + (int) (sy*(packBlockH+packBlockDis)); y < GamePanel.frameH-downBlockH; y+=packBlockH+packBlockDis) {
			if(id == onMouse) {
				gf.setColor(new Color(255,255,255,50));
				gf.fillRect(15, y, GamePanel.frameW-60, packBlockH);
			}
			gf.setColor(Color.WHITE);
			drawPackIco(gf, y, id);
			gf.setColor(packs[id] == MyFile.pack ? Color.GREEN : Color.WHITE);
			gf.drawRect(15, y, GamePanel.frameW-60, packBlockH);
			gf.setFont(new Font("Comic Sans MS", Font.BOLD, packBlockH/2));
			drawString(gf, MyFile.getPackName(id) + "", packBlockH+25, (int) (y+packBlockH/3d*2));
			gf.setColor(new Color(0,packs[id] == MyFile.pack ? 50 : 0,0,150));
			gf.fillRect(15, y, GamePanel.frameW-60, packBlockH);
			id++;
			if(id+1 > packs.length) {
				break;
			}
		}
		
		gf.setColor(Color.DARK_GRAY);
		gf.fillRect(0, 0, GamePanel.frameW, GamePanel.frameH);
	}
	
	private void drawPackIco(Graphics2D gf, int y, int id) {
		gf.drawRect(20, y+5, packBlockH-10, packBlockH-10);
		gf.drawImage(MyFile.packsIco[id], 20, y+5, packBlockH-10, packBlockH-10, null);
	}
	
	private void drawString(Graphics2D gf, String str, int x, int y) {
		gf.setColor(Color.WHITE);
		gf.drawString(str, x, y);
		gf.setColor(Color.BLACK);
		for (int xx = -1; xx < 2; xx++) {
			for (int yy = -1; yy <2; yy++) {
				gf.drawString(str, (int) (x+xx*GamePanel.scalefull/1.5), (int) (y+yy*GamePanel.scalefull/1.5));
			}
		}
	}

	boolean goDown;
	
	@Override
	public void update() {
		vy+= MouseController.mouseScroll/4d;
		sy += vy;
		if(sy > 0) {
			sy /= 2;
		}
		double blockDist = packBlockH+packBlockDis;
//		count = (int) (Math.floor(()/blockDist));
		double vale = (GamePanel.frameH - downBlockH) -pad*2+packBlockDis;
		double minY = ((vale)/(double)(packBlockH+packBlockDis))-packs.length;
		pageH = (int) (minY*(packBlockH+packBlockDis));
		if(sy < minY) {
			sy = (sy-minY)/2+minY;
		}
		vy /= 1.5;
		
		int id = 0;
		int my = MouseController.getMousePoint().y;
		
		onMouse = -1;
		for (int y = pad + (int) (sy*(packBlockH+packBlockDis)); y < GamePanel.frameH-downBlockH; y+=packBlockH+packBlockDis) {
			if(my > y && my < y+packBlockH) {
				onMouse = id;
				break;
			}
			id++;
		}
		if(my > GamePanel.frameH-downBlockH) {
			onMouse = -1;
		}
		for (int i = 0; i < packs.length; i++) {
			if(onMouse == i && MouseController.isMousePressed) {
				Loader.reload(packs[i]);
				GameData.reloadText();
				GameStage.reloadMusic();
			}
		}
		
		button.setPosition(GamePanel.frameW - button.getBw(), (int) (GamePanel.frameH-(packBlockH/2)));
		button.update();
		if(button.isClicked()) {
			maneger.loadStageNoLast(Maneger.SETTINGS);
		}
		MouseController.mouseScroll = 0;
	}

	double scrollV = .1;
	
	@Override
	protected void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			vy += scrollV;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			vy -= scrollV;
		}
	}

	@Override
	protected void keyReleased(KeyEvent e) {
		
	}

	@Override
	protected void releasedAll() {
		
	}

	@Override
	protected LevelGenerator getLevelGenerator() {
		return null;
	}

	@Override
	protected void reloadTexts() {
		button.setText(GameData.texts[GameData.TEXT_BACK]);
	}

}
