package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class BossHUD {

	private Enemy enemy;
	
	private BufferedImage image;
	private Font font;
	
	//constructor gets hud image and sets font
	public BossHUD(Enemy en) {
		enemy = en;
		try {
			image = ImageIO.read(
				getClass().getResourceAsStream(
					"/HUD/bosshud.gif"
				)
			);
			font = new Font("Arial", Font.PLAIN, 14);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//draw the hud
	public void draw(Graphics2D g) {
		
		g.drawImage(image, 240, 10, null);
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString(
			enemy.getHealth() + "/" + enemy.getMaxHealth(),
			270,
			25
		);
		
	}
	
}
