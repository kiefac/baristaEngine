package kiefac.barista;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.SwingUtilities;

public class BaristaExample extends Barista {
	double playerX, playerY;
	BSprite sprite1, cursor;
	
	@Override
	public boolean onUserCreate() {
		gameName = "Barista Example";
		playerX = 0;
		playerY = 0;
		File file1 = new File("images/guy.png");
		sprite1 = new BSprite(file1);
		File cursorFile = new File("images/cursor.png");
		cursor = new BSprite(cursorFile);
		
		hideCursor();
		return true;
	}

	@Override
	public boolean onUserUpdate(double frameTime) {
		fill(0, 0, 160, 90, Color.WHITE);
		draw(1, 1, Color.GREEN);
		fill(2, 2, 5, 4, Color.BLUE);
		drawString(10, 10, "Hello World!", Color.WHITE);
		drawStringAlpha(10, 12, "Hello Java!", Color.CYAN);
		draw(159, 89, Color.MAGENTA);
		drawLine(20, 20, 25, 30, Color.BLACK);
		drawCircle(50, 20, 10, Color.BLACK);
		fillCircle(80, 20, 15, Color.PINK);
		drawTriangle(50, 40, 60, 42, 55, 50, Color.YELLOW);
		fillTriangle(50, 60, 60, 60, 55, 70, Color.BLACK);
		fillTriangle(65, 60, 65, 70, 75, 65, Color.RED);
		fillTriangle(65, 40, 75, 42, 70, 50, Color.ORANGE);
		
		if (getKey(KeyEvent.VK_UP).held) {
			playerY -= 20 * frameTime;
		}
		if (getKey(KeyEvent.VK_DOWN).held) {
			playerY += 20 * frameTime;
		}
		if (getKey(KeyEvent.VK_RIGHT).held) {
			playerX += 20 * frameTime;
		}
		if (getKey(KeyEvent.VK_LEFT).held) {
			playerX -= 20 * frameTime;
		}

		drawSprite((int)playerX, (int)playerY, sprite1, 1);
		
		drawSprite(getMouseX(), getMouseY(), cursor, 1);
		
		return true;
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable( ) {
			public void run() {
				BaristaExample example = new BaristaExample();
				example.constructFrame(160, 90, 8, 8);
				example.start();
			}
		});
	}
}
