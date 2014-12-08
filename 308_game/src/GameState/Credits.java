package GameState;

import Audio.AudioPlayer;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Credits extends GameState {
	
	private Background bg;
	
	
	private Color titleColor;
	private Font titleFont;
	private AudioPlayer bgMusic;
	private Font font;
	
	public Credits(GameStateManager gsm) {
		
		this.gsm = gsm;
		
		try {
			
			bg = new Background("/Backgrounds/menubg2.gif", 1);
			bg.setVector(-0.1, 0);
			titleColor = new Color(0, 255, 0);
			titleFont = new Font(
					"Century Gothic",
					Font.PLAIN,
					28);
			
			font = new Font("Arial", Font.PLAIN, 12);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		bgMusic = new AudioPlayer("/Music/guile.mp3");
		bgMusic.loop();
		
	}
	
	public void init() {}
	
	public void update() {
		bg.update();
	}
	
	public void draw(Graphics2D g) {
		
		// draw bg
		bg.draw(g);
		
		String[] credits = {
				"Programmers","",
				"Drew Woodwiss - Enemies",
				"Ian Gaskill - Gameplay",
				"Kathleen Foley - UI/HUD",
				"Sean Pador - Level Design","","",
				"Music Credits","","Menu - Pixies - \"Where is my Mind?\"",
				"Help - Rush Coil - \"Too Many Cooks\"",
				"Level 1 - Linked Horizon - \"Attack on Titan Theme\"",
				"Level 2 - Kingdom Hearts - \"Halloween Town\"",
				"Credits - Street Fighter - \"Guile's Theme\""
			};
		// draw title
		g.setColor(titleColor);
		g.setFont(font);
		for(int i = 0; i<credits.length; i++){
			g.drawString(credits[i], 10, 20 + i*15);
			
		}
		
		
	}
	
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ESCAPE){
			bgMusic.stop();
			gsm.setState(GameStateManager.MENUSTATE);
		}
		
	}
	public void keyReleased(int k) {}
	
}










