package Entity;

import TileMap.TileMap;

public class Enemy extends MapObject {
	//variables used by all enemies
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;
	protected boolean boss = false;
	
	protected boolean flinching;
	protected long flinchTimer;
	
	public Enemy(TileMap tm) {
		super(tm);
	}
	
	public boolean isDead() { return dead; }
	
	public int getHealth() { return health; }
	
	public int getMaxHealth() { return maxHealth; }
	
	public int getDamage() { return damage; }
	
	public boolean isBoss(){return boss;}
	
	//function decrements enemies health when hit
	public void hit(int damage) {
		if(dead || flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	public void update() {}
	public void update(Player p){}
	
}














