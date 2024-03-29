package GameState;

import Main.GamePanel;
import TileMap.*;
import Audio.AudioPlayer;
import Entity.*;

import java.awt.*;
import java.awt.event.KeyEvent;

public class HelpState extends GameState {

	private Background bg;
	private TileMap tileMap;
	private AudioPlayer bgMusic;
	private Player player;
	
	private String[] instructions = {
		"A to move left, D to move right",
		"SPACE to jump, W to glide",
		"R to attack, F to fire bow",
		"press ESC to return to menu"
	};
	
	private Color titleColor;
	private Font titleFont;
	private Font font;
	
	public HelpState(GameStateManager gsm){
		
		this.gsm = gsm;
		init();
	}
	
	public void init() {
		
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/tutorial.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Backgrounds/grassbg1.gif", 0.1);
		//bg.setVector(-0.1, 0);
		
		titleColor = new Color(255, 255, 255);
		titleFont = new Font(
				"Century Gothic",
				Font.PLAIN,
				28);
		
		font = new Font("Arial", Font.PLAIN, 12);
		
		player = new Player(tileMap);
		player.setPosition(100, 100);
		bgMusic = new AudioPlayer("/Music/cooks.mp3");
		bgMusic.loop();
	}
	
	public void update() {
		
		// update player
		player.update();
		tileMap.setPosition(
			GamePanel.WIDTH / 2 - player.getx(),
			GamePanel.HEIGHT / 2 - player.gety()
		);
		
		// set background
		bg.setPosition(tileMap.getx(), tileMap.gety());
	}
	
	public void draw(Graphics2D g) {
		
		// draw bg
		bg.draw(g);
		
		// draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("How to Move", 70, 50);
		
		//draw instructions
		g.setFont(font);
		
		for(int i = 0; i<instructions.length; i++){
			g.drawString(instructions[i], 70, 80 + i*15);
			
		}
		
		// draw tilemap
		tileMap.draw(g);
		
		// draw player
		player.draw(g);
		
	}
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_A) player.setLeft(true);
		if(k == KeyEvent.VK_D) player.setRight(true);
		//if(k == KeyEvent.VK_W) player.setUp(true);
		//if(k == KeyEvent.VK_S) player.setDown(true);
		if(k == KeyEvent.VK_SPACE) player.setJumping(true);
		if(k == KeyEvent.VK_W) player.setGliding(true);
		if(k == KeyEvent.VK_R) player.setSlashing();
		if(k == KeyEvent.VK_F) player.setFiring();
		if(k == KeyEvent.VK_ESCAPE) {bgMusic.stop();
		gsm.setState(GameStateManager.MENUSTATE);
		}
	}
	
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_A) player.setLeft(false);
		if(k == KeyEvent.VK_D) player.setRight(false);
		//if(k == KeyEvent.VK_W) player.setUp(false);
		//if(k == KeyEvent.VK_S) player.setDown(false);
		if(k == KeyEvent.VK_SPACE) player.setJumping(false);
		if(k == KeyEvent.VK_W) player.setGliding(false);
	}
	
}
