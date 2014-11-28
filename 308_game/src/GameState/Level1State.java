package GameState;

import Main.GamePanel;
import TileMap.*;
import Entity.*;
import Entity.Enemies.*;
import Audio.AudioPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Level1State extends GameState {
	
	private TileMap tileMap;
	private Background bg;
	private int deaths;
	
	private Player player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	
	private HUD hud;
	private BossHUD bhud;
	
	private AudioPlayer bgMusic;
	
	public Level1State(GameStateManager gsm) {
		this.gsm = gsm;
		
		init();
	}
	
	public void init() {
		//prepare level for play
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Backgrounds/grassbg1.gif", 0.1);
		
		player = new Player(tileMap);
		player.setPosition(100, 100);
		
		populateEnemies();
		
		explosions = new ArrayList<Explosion>();
		
		hud = new HUD(player);
		
		bgMusic = new AudioPlayer("/Music/theme.mp3");
		bgMusic.loop();
		
	}
	
	//fill the level with enemies
	private void populateEnemies() {
	
		enemies = new ArrayList<Enemy>();
		
		Slugger s;
		Point[] points = new Point[] {
			new Point(200, 100),
			new Point(860, 200),
			new Point(1525, 200),
			new Point(1680, 200),
			new Point(1800, 200)
		};
		for(int i = 0; i < points.length; i++) {
			s = new Slugger(tileMap);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);
		}
		Level1Boss boss;
		boss = new Level1Boss(tileMap);
		boss.setPosition(3000, 100);
		enemies.add(boss);
		
		bhud = new BossHUD(boss);
	}
	
	public void update() {
		
		// update player
		player.update();
		tileMap.setPosition(
			GamePanel.WIDTH / 2 - player.getx(),
			GamePanel.HEIGHT / 2 - player.gety()
		);
		if(player.gety() > 225){player.kill();}
			
		if(player.isDead()){
			deaths++;
			bgMusic.stop();
			
			if(player.getLives() > 0)
			{
				this.init();
				player.loseLife(deaths);
			}
			else
				gsm.setState(0);
		}
		
		// set background
		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		// attack enemies
		player.checkAttack(enemies);
		
		// update enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			if(e.isBoss())
				e.update(player);
			else
				e.update();
			if(e.isDead()) {
				enemies.remove(i);
				i--;
				if(e.isBoss()){
					for(int j = 1; j <= 10; j+=3){
						explosions.add(new Explosion(e.getx()+j,e.gety()));
						explosions.add(new Explosion(e.getx()+j/2, e.gety()+3));
						explosions.add(new Explosion(e.getx()-j, e.gety()));
						explosions.add(new Explosion(e.getx()-j/2, e.gety()+3));
					}
				}
					new Explosion(e.getx(), e.gety());
			}
		}
		
		
		// update explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
		
		//when player reaches magic column
		if(player.getx() >= 3100 && player.getx() <= 3113 && player.gety() > 175)
		{
			bgMusic.stop();
			gsm.setState(GameStateManager.LEVEL2STATE);
		}
	}
	
	public void draw(Graphics2D g) {
		
		// draw bg
		bg.draw(g);
		
		// draw tilemap
		tileMap.draw(g);
		
		// draw player
		player.draw(g);
		
		// draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		
		// draw explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition(
				(int)tileMap.getx(), (int)tileMap.gety());
			explosions.get(i).draw(g);
		}
		
		// draw hud
		hud.draw(g);
		
		if(player.getx() >= 2650
				&& enemies.get(enemies.size()-1).isBoss()){
			bhud.draw(g);
		}
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
		if(k == KeyEvent.VK_ESCAPE) {
			bgMusic.stop();
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












