package GameState;

import Audio.AudioPlayer;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public class DeadState extends GameState {
	
	private Background bg;
	
	
	private Color titleColor;
	private Font titleFont;
	private AudioPlayer bgMusic;
	private Font font;
	private int currentChoice = 0;
	private String[] options = {
			"Restart",
			"Return to Main Menu",
			"Quit"
		};
	
	public DeadState(GameStateManager gsm) {
		
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
				"Game Over!!",
				"Would you like to..",
			};
		// draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		for(int i = 0; i<credits.length; i++){
			g.drawString(credits[i], 20, 60 + i*28);
			
		}
		//draw choices
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.BLACK);
			}
			else {
				g.setColor(Color.GREEN);
			}
			g.drawString(options[i], 145, 140 + i * 15);
		}
		
		
	}
	
	private void select() {
		bgMusic.stop();
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		if(currentChoice == 1) {
			gsm.setState(GameStateManager.MENUSTATE);
		}
		if(currentChoice == 2) {
			System.exit(0);
		}

	}
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER){
			select();
		}
		if(k == KeyEvent.VK_UP) {
			currentChoice--;
			if(currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if(k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if(currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}
	public void keyReleased(int k) {}
	
}