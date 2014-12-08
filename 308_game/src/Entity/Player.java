package Entity;

import TileMap.*;
import Audio.AudioPlayer;

import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Player extends MapObject {
	
	// player stuff
	private int health;
	private int maxHealth;
	private int lives;
	private int arrow;
	private int maxArrow;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;
	
	// arrow
	private boolean firing;
	private int arrowCost;
	private int arrowDamage;
	private ArrayList<Arrow> arrows;
	
	// scratch
	private boolean slashing;
	private int slashDamage;
	private int slashRange;
	
	// gliding
	private boolean gliding;
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
		2, 8, 1, 2, 4, 2, 5
	};
	
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int GLIDING = 4;
	private static final int SHOOTARROW = 5;
	private static final int SLASHING = 6;
	
	private HashMap<String, AudioPlayer> sfx;
	
	public Player(TileMap tm) {
		
		super(tm);
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		moveSpeed = 0.5; //0.3
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		facingRight = true;
		
		health = maxHealth = 10;
		lives = 2;
		arrow = maxArrow = 25;
		
		arrowCost = 1;
		arrowDamage = 5;
		arrows = new ArrayList<Arrow>();
		
		slashDamage = 8;
		slashRange = 40;
		
		// load sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Player/playersprites.gif"
				)
			);
			
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 7; i++) {
				
				BufferedImage[] bi =
					new BufferedImage[numFrames[i]];
				
				for(int j = 0; j < numFrames[i]; j++) {
					
					if(i != SLASHING) {
						bi[j] = spritesheet.getSubimage(
								j * width,
								i * height,
								width,
								height
						);
					}
					else {
						bi[j] = spritesheet.getSubimage(
								j * width * 2,
								i * height,
								width * 2,
								height
						);
					}
					
				}
				
				sprites.add(bi);
				
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
		
		sfx = new HashMap<String, AudioPlayer>();
		sfx.put("jump", new AudioPlayer("/SFX/jump.mp3"));
		sfx.put("scratch", new AudioPlayer("/SFX/scratch.mp3"));
		
	}
	
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	public int getLives(){return lives;}
	public void loseLife(int d){lives = lives - d;}
	public int getArrow() { return arrow; }
	public int getMaxArrow() { return maxArrow; }
	public boolean isDead(){return dead;}
	public void kill(){dead = true;}
	
	public void setFiring() { 
		firing = true;
	}
	public void setSlashing() {
		slashing = true;
	}
	public void setGliding(boolean b) { 
		gliding = b;
	}
	
	public void checkAttack(ArrayList<Enemy> enemies) {
		
		// loop through enemies
		for(int i = 0; i < enemies.size(); i++) {
			
			Enemy e = enemies.get(i);
			
			// scratch attack
			if(slashing) {
				if(facingRight) {
					if(
						e.getx() > x &&
						e.getx() < x + slashRange && 
						e.gety() > y - height / 2 &&
						e.gety() < y + height / 2
					) {
						e.hit(slashDamage);
					}
				}
				else {
					if(
						e.getx() < x &&
						e.getx() > x - slashRange &&
						e.gety() > y - height / 2 &&
						e.gety() < y + height / 2
					) {
						e.hit(slashDamage);
					}
				}
			}
			
			// arrows
			for(int j = 0; j < arrows.size(); j++) {
				if(arrows.get(j).intersects(e)) {
					e.hit(arrowDamage);
					arrows.get(j).setHit();
					break;
				}
			}
			
			// check enemy collision
			if(intersects(e)) {
				hit(e.getDamage());
			}
			
		}
		
	}
	
	public void hit(int damage) {
		if(flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	private void getNextPosition() {
		
		// movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		
		// cannot move while attacking, except in air
		if(
		(currentAction == SLASHING || currentAction == SHOOTARROW) &&
		!(jumping || falling)) {
			dx = 0;
		}
		
		// jumping
		if(jumping && !falling) {
			sfx.get("jump").play();
			dy = jumpStart;
			falling = true;
		}
		
		// falling
		if(falling) {
			
			if(dy > 0 && gliding) dy += fallSpeed * 0.1;
			else dy += fallSpeed;
			
			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			
			if(dy > maxFallSpeed) dy = maxFallSpeed;
			
		}
		
	}
	
	public void update() {
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check attack has stopped
		if(currentAction == SLASHING) {
			if(animation.hasPlayedOnce()) slashing = false;
		}
		if(currentAction == SHOOTARROW) {
			if(animation.hasPlayedOnce()) firing = false;
		}
		
		// arrow attack
		if(firing && currentAction != SHOOTARROW) {
			if(arrow >= arrowCost) {
				arrow -= arrowCost;
				Arrow fb = new Arrow(tileMap, facingRight);
				fb.setPosition(x, y);
				arrows.add(fb);
			}
		}
		
		// update arrows
		for(int i = 0; i < arrows.size(); i++) {
			arrows.get(i).update();
			if(arrows.get(i).shouldRemove()) {
				arrows.remove(i);
				i--;
			}
		}
		
		// check done flinching
		if(flinching) {
			long elapsed =
				(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 1000) {
				flinching = false;
			}
		}
		
		// set animation
		if(slashing) {
			if(currentAction != SLASHING) {
				sfx.get("scratch").play();
				currentAction = SLASHING;
				animation.setFrames(sprites.get(SLASHING));
				animation.setDelay(50);
				width = 60;
			}
		}
		else if(firing) {
			if(currentAction != SHOOTARROW) {
				currentAction = SHOOTARROW;
				animation.setFrames(sprites.get(SHOOTARROW));
				animation.setDelay(100);
				width = 30;
			}
		}
		else if(dy > 0) {
			if(gliding) {
				if(currentAction != GLIDING) {
					currentAction = GLIDING;
					animation.setFrames(sprites.get(GLIDING));
					animation.setDelay(100);
					width = 30;
				}
			}
			else if(currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width = 30;
			}
		}
		else if(dy < 0) {
			if(currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				width = 30;
			}
		}
		else if(left || right) {
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				width = 30;
			}
		}
		else {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 30;
			}
		}
		
		animation.update();
		
		// set direction
		if(currentAction != SLASHING && currentAction != SHOOTARROW) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		// draw arrows
		for(int i = 0; i < arrows.size(); i++) {
			arrows.get(i).draw(g);
		}
		
		// draw player
		if(flinching) {
			long elapsed =
				(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return;
			}
		}
		
		super.draw(g);
		
	}
	
	
	
}

















