package kiefac.barista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

// Barista game engine, based off of javidx9's olcConsoleGameEngine/olcPixelGameEngine. by kiefac
public abstract class Barista {
	public static Font monoFont;
	private boolean engineActive;
	private JFrame gameFrame;
	protected JPanel gameContent;
	private Graphics2D screenGraphics;
	private BufferedImage imageToPaint;
	private Graphics paintGraphics;
	private int screenWidth, screenHeight;
	private int pixelWidth, pixelHeight;
	protected String gameName;
	private boolean[] keyOldState = new boolean[KeyEvent.KEY_LAST];
	private boolean[] keyNewState = new boolean[KeyEvent.KEY_LAST];
	private KeyState[] keyboardState = new KeyState[KeyEvent.KEY_LAST];
	private boolean[] mouseOldState = new boolean[MouseEvent.MOUSE_LAST];
	private boolean[] mouseNewState = new boolean[MouseEvent.MOUSE_LAST];
	private KeyState[] mouseState = new KeyState[MouseEvent.MOUSE_LAST];
	private double frameTimer = 1.0;
	private int frameCount;
	private int mouseX, mouseY;
	private KeyListener keyboardListener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {
			// do nothing
		}

		@Override
		public void keyPressed(KeyEvent e) {
			keyNewState[e.getKeyCode()] = true;
		}

		@Override
		public void keyReleased(KeyEvent e) {
			keyNewState[e.getKeyCode()] = false;
		}
	};
	private MouseInputAdapter mouseListener = new MouseInputAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			mouseNewState[e.getButton()] = true;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			mouseNewState[e.getButton()] = false;
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {
			mouseX = e.getX() / pixelWidth;
			mouseY = e.getY() / pixelHeight;
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			mouseX = e.getX() / pixelWidth;
			mouseY = e.getY() / pixelHeight;
		}
	};

	public Barista() {
		screenWidth = 80;
		screenHeight = 30;
		pixelWidth = 2;
		pixelHeight = 2;
		mouseX = 0;
		mouseY = 0;
		gameName = "Default";
		monoFont = new Font(Font.MONOSPACED, Font.PLAIN, (int)(this.pixelHeight * 1.8));
		gameFrame = new JFrame(gameName);
		gameContent = new JPanel();
		imageToPaint = new BufferedImage(screenWidth * pixelWidth, screenHeight * pixelHeight, BufferedImage.TYPE_INT_ARGB);
		screenGraphics = (Graphics2D) imageToPaint.getGraphics();
		screenGraphics.setFont(monoFont);
		for (int i = 0; i < keyboardState.length; i++) {
			keyboardState[i] = new KeyState();
		}
		for (int i = 0; i < mouseState.length; i++) {
			mouseState[i] = new KeyState();
		}
		gameContent.addKeyListener(keyboardListener);
		gameContent.addMouseListener(mouseListener);
		gameContent.addMouseMotionListener(mouseListener);
		gameContent.setFocusable(true);
		paintGraphics = gameContent.getGraphics();
	}

	public int constructFrame(int width, int height, int pixelWidth, int pixelHeight) {
		gameFrame = new JFrame(gameName);
		gameContent = new JPanel();
		screenWidth = width;
		screenHeight = height;
		this.pixelWidth = pixelWidth;
		this.pixelHeight = pixelHeight;
		monoFont = new Font(Font.MONOSPACED, Font.PLAIN, (int)(this.pixelHeight * 1.8));
		gameContent.setPreferredSize(new Dimension(screenWidth * this.pixelWidth, screenHeight * this.pixelHeight));
		imageToPaint = new BufferedImage(screenWidth * this.pixelWidth, screenHeight * this.pixelHeight, BufferedImage.TYPE_INT_ARGB);
		screenGraphics = (Graphics2D) imageToPaint.getGraphics();
		screenGraphics.setFont(monoFont);

		gameFrame.setResizable(false);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setLayout(new BorderLayout());
		gameFrame.add(gameContent, BorderLayout.CENTER);
		gameFrame.pack();
		gameFrame.setVisible(true);

		for (int i = 0; i < keyboardState.length; i++) {
			keyboardState[i] = new KeyState();
		}
		for (int i = 0; i < mouseState.length; i++) {
			mouseState[i] = new KeyState();
		}
		
		gameContent.setFocusable(true);
		gameContent.addKeyListener(keyboardListener);
		gameContent.addMouseListener(mouseListener);
		gameContent.addMouseMotionListener(mouseListener);
		
		paintGraphics = gameContent.getGraphics();
		
		return 1;
	}

	public void start() {
		engineActive = true;
		onUserCreate();
		EngineThread thread = new EngineThread();
		thread.start();
	}
	
	public void hideCursor() {
		BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0, 0), "blank cursor");
		gameFrame.getContentPane().setCursor(blankCursor);
	}
	
	public void showCursor() {
		gameFrame.getContentPane().setCursor(Cursor.getDefaultCursor());
	}

	public KeyState getKey(int keycode) {
		return keyboardState[keycode];
	}
	
	public int getMouseX() {
		return mouseX;
	}
	
	public int getMouseY() {
		return mouseY;
	}
	
	public void draw(int x, int y, Color color) {
		if (x >= 0 && x < screenWidth && y >= 0 && y < screenHeight) {
			screenGraphics.setColor(color);
			screenGraphics.fillRect(x * pixelWidth, y * pixelWidth, pixelWidth, pixelHeight);	
		}
	}

	public void fill(int x1, int y1, int x2, int y2, Color color) {
		Dimension temp = clip(x1, y1);
		x1 = temp.width;
		y1 = temp.height;
		temp = clip(x2, y2);
		x2 = temp.width;
		y2 = temp.height;

		int width = x2 - x1;
		int height = y2 - y1;

		screenGraphics.setColor(color);
		screenGraphics.fillRect(x1 * pixelWidth, y1 * pixelHeight, width * pixelWidth, height * pixelHeight);
	}

	public void drawString(int x, int y, String string, Color color) {
		//char[] array = string.toCharArray();
		String[] array = string.split("");
		fill(x, y, x + array.length, y + 1, Color.black);
		screenGraphics.setColor(color);
		int ascent = screenGraphics.getFontMetrics().getAscent();
		for (int i = 0; i < array.length; i++) {
			screenGraphics.drawString(array[i], (x + i) * pixelWidth, (y * pixelHeight) + (ascent / 2) + 1);
		}
	}

	public void drawStringAlpha(int x, int y, String string, Color color) {
		String[] strings = string.split("");
		screenGraphics.setColor(color);
		int ascent = screenGraphics.getFontMetrics().getAscent();
		for (int i = 0; i < strings.length; i++) {
			screenGraphics.drawString(strings[i], (x + i) * pixelWidth, (y * pixelHeight) + (ascent / 2) + 1);
		}
	}

	public Dimension clip(int x, int y) {
		if (x < 0) {
			x = 0;
		}
		if (x > screenWidth) {
			x = screenWidth;
		}
		if (y < 0) {
			y = 0;
		}
		if (y > screenHeight) {
			y = screenHeight;
		}
		return new Dimension(x, y);
	}

	public void drawLine(int x1, int y1, int x2, int y2, Color color) {
		// bresenham's line algorithm
		double deltaX = x2 - x1;
		if (deltaX == 0) {
			drawVertLine(x1, y1, y2, color);
		} else {
			int xSign = 1;
			if (deltaX < 0) {
				xSign = -1;
			}
			double deltaY = y2 - y1;
			if (deltaY == 0) {
				drawHorizLine(x1, x2, y1, color);
			} else {
				int ySign = 1;
				if (deltaY < 0) {
					ySign = -1;
				}
				double deltaXErr = Math.abs(deltaX / deltaY);
				double xError = 0;
				double deltaYErr = Math.abs(deltaY / deltaX);
				double yError = 0;
				int y = y1;
				if (deltaX >= 0) {
					for (int i = x1; i <= x2;) {
						draw(i, y, color);
						xError += deltaXErr;
						if (xError >= 0.5) {
							i += xSign;
							xError -= 1;
						}
						yError += deltaYErr;
						if (yError >= 0.5) {
							y += ySign;
							yError -= 1;
						}
					}
				} else {
					for (int i = x1; i >= x2;) {
						draw(i, y, color);
						xError += deltaXErr;
						if (xError >= 0.5) {
							i += xSign;
							xError -= 1;
						}
						yError += deltaYErr;
						if (yError >= 0.5) {
							y += ySign;
							yError -= 1;
						}
					}
				}
			}
		}
	}

	public void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3, Color color) {
		drawLine(x1, y1, x2, y2, color);
		drawLine(x2, y2, x3, y3, color);
		drawLine(x3, y3, x1, y1, color);
	}

	private Pair<Integer, Integer> swap(int x, int y) {
		Pair<Integer, Integer> swapped = new Pair<Integer, Integer>(y, x);
		return swapped;
	}

	private void drawHorizLine(int startX, int endX, int y, Color color) {
		if (endX - startX >= 0) {
			for (int i = startX; i <= endX; i++) {
				draw(i, y, color);
			}
		} else {
			for (int i = startX; i >= endX; i--) {
				draw(i, y, color);
			}
		}
	}

	private void drawVertLine(int x, int startY, int endY, Color color) {
		if (endY - startY >= 0) {
			for (int i = startY; i <= endY; i++) {
				draw(x, i, color);
			}
		} else {
			for (int i = startY; i >= endY; i--) {
				draw(x, i, color);
			}
		}
	}

	public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, Color color) {
		// copied nearly verbatim from olcConsoleGameEngine
		// SWAP and drawline autos replaced with swap and drawHorizLine above
		// primitives cannot be passed by reference in Java, so swap was modified to return a custom "Pair" class (below)
		// also, Java does not have goto so I used an if statement and loop labels with "break label;"
		int t1x, t2x, y, minX, maxX, t1xp, t2xp;
		boolean changed1 = false;
		boolean changed2 = false;
		int signX1, signX2, dx1, dy1, dx2, dy2;
		int e1, e2;

		// sort vertices
		Pair<Integer, Integer> swapPair;
		if (y1 > y2) {
			swapPair = swap(y1, y2);
			y1 = swapPair.first();
			y2 = swapPair.last();
			swapPair = swap(x1, x2);
			x1 = swapPair.first();
			x2 = swapPair.last();
		}
		if (y1 > y3) {
			swapPair = swap(y1, y3);
			y1 = swapPair.first();
			y3 = swapPair.last();
			swapPair = swap(x1, x3);
			x1 = swapPair.first();
			x3 = swapPair.last();
		}
		if (y2 > y3) {
			swapPair = swap(y2, y3);
			y2 = swapPair.first();
			y3 = swapPair.last();
			swapPair = swap(x2, x3);
			x2 = swapPair.first();
			x3 = swapPair.last();
		}

		t1x = t2x = x1; // starting points
		y = y1;
		dx1 = (int)(x2 - x1);
		if (dx1 < 0) {
			dx1 = -dx1;
			signX1 = -1;
		} else {
			signX1 = 1;
		}
		dy1 = (int)(y2 - y1);

		dx2 = (int)(x3 - x1);
		if (dx2 < 0) {
			dx2 = - dx2;
			signX2 = -1;
		} else {
			signX2 = 1;
		}
		dy2 = (int)(y3 - y1);

		if (dy1 > dx1) {
			swapPair = swap(dx1, dy1);
			dx1 = swapPair.first();
			dy1 = swapPair.last();
			changed1 = true;
		}
		if (dy2 > dx2) {
			swapPair = swap(dx2, dy2);
			dx2 = swapPair.first();
			dy2 = swapPair.last();
			changed2 = true;
		}

		e2 = (int)(dx2 >> 1);
		if (y1 != y2) {
			e1 = (int)(dx1 >> 1);

			for (int i = 0; i < dx1;) {
				t1xp = 0;
				t2xp = 0;
				if (t1x < t2x) {
					minX = t1x;
					maxX = t2x;
				} else {
					minX = t2x;
					maxX = t1x;
				}

				// process first line until y value is about to change
				nestLoop:
					while (i < dx1) {
						i++;
						e1 += dy1;
						while (e1 >= dx1) {
							e1 -= dx1;
							if (changed1) {
								t1xp = signX1;
							} else {
								break nestLoop;
							}
						}
						if (changed1) {
							break;
						} else {
							t1x += signX1;
						}
					}

				// process second line until y value is about to change
				nestLoop:
					while (true) {
						e2 += dy2;
						while (e2 >= dx2) {
							e2 -= dx2;
							if (changed2) {
								t2xp = signX2;
							} else {
								break nestLoop;
							}
						}
						if (changed2) {
							break;
						} else {
							t2x += signX2;
						}
					}

					if (minX > t1x) {
						minX = t1x;
					}
					if (minX > t2x) {
						minX = t2x;
					}
					if (maxX < t1x) {
						maxX = t1x;
					}
					if (maxX < t2x) {
						maxX = t2x;
					}
					drawHorizLine(minX, maxX, y, color); // draw line from min to max points found on the y
					//now increase y
					if (!changed1) {
						t1x += signX1;
					}
					t1x+= t1xp;
					if (!changed2) {
						t2x += signX2;
					}
					t2x += t2xp;
					y += 1;
					if (y == y2) {
						break;
					}
			}
		}
		// second half
		dx1 = (int)(x3 - x2);
		if (dx1 < 0) {
			dx1 = -dx1;
			signX1 = -1;
		} else {
			signX1 = 1;
		}
		dy1 = (int)(y3 - y2);
		t1x = x2;

		if (dy1 > dx1) {
			swapPair = swap(dy1, dx1);
			dy1 = swapPair.first();
			dx1 = swapPair.last();
			changed1 = true;
		} else {
			changed1 = false;
		}

		e1 = (int)(dx1 >> 1);

		for (int i = 0; i <= dx1; i++) {
			t1xp = 0;
			t2xp = 0;
			if (t1x < t2x) {
				minX = t1x;
				maxX = t2x;
			} else {
				minX = t2x; maxX = t1x;
			}

			// process first line until y value is about to change
			nestLoop:
				while (i < dx1) {
					e1 += dy1;
					while (e1 >= dx1) {
						e1 -= dx1;
						if (changed1) {
							t1xp = signX1;
							break;
						} else {
							break nestLoop;
						}
					}
					if (changed1) {
						break;
					} else {
						t1x += signX1;
					}
					if (i < dx1) {
						i++;
					}
				}

			// process second line until y value is about to change
			nestLoop:
				while (t2x != x3) {
					e2 += dy2;
					while (e2 >= dx2) {
						e2 -= dx2;
						if (changed2) {
							t2xp = signX2;
						} else {
							break nestLoop;
						}
					}
					if (changed2) {
						break;
					} else {
						t2x += signX2;
					}
				}

				if (minX > t1x) {
					minX = t1x;
				}
				if (minX > t2x) {
					minX = t2x;
				}
				if (maxX < t1x) {
					maxX = t1x;
				}
				if (maxX < t2x) {
					maxX = t2x;
				}
				drawHorizLine(minX, maxX, y, color);
				if (!changed1) {
					t1x += signX1;
				}
				t1x += t1xp;
				if (!changed2) {
					t2x += signX2;
				}
				t2x += t2xp;
				y += 1;
				if (y > y3) {
					return;
				}
		}
	}

	public void drawCircle(int xCenter, int yCenter, int radius, Color color) { // midpoint circle algorithm
		// copied verbatim from olcConsoleGameEngine
		int x = 0;
		int y = radius;
		int p = 3 - 2 * radius;

		while (y >= x) { //formulate only 1/8 of circle
			draw(xCenter - x, yCenter - y, color);
			draw(xCenter - y, yCenter - x, color);
			draw(xCenter + y, yCenter - x, color);
			draw(xCenter + x, yCenter - y, color);
			draw(xCenter - x, yCenter + y, color);
			draw(xCenter - y, yCenter + x, color);
			draw(xCenter + y, yCenter + x, color);
			draw(xCenter + x, yCenter + y, color);
			if (p < 0) {
				p += 4 * x++ + 6;
			} else {
				p += 4 * (x++ - y--) + 10;
			}
		}
	}

	public void fillCircle(int xCenter, int yCenter, int radius, Color color) {
		// copied verbatim from olcConsoleGameEngine, with the drawline auto replaced with drawHorizLine() (above)
		int x = 0;
		int y = radius;
		int p = 3 - 2 * radius;

		while (y >= x) {
			drawHorizLine(xCenter - x, xCenter + x, yCenter - y, color);
			drawHorizLine(xCenter - y, xCenter + y, yCenter - x, color);
			drawHorizLine(xCenter - x, xCenter + x, yCenter + y, color);
			drawHorizLine(xCenter - y, xCenter + y, yCenter + x, color);
			if (p < 0) {
				p += 4 * x++ + 6;
			} else {
				p += 4 * (x++ - y--) + 10;
			}
		}
	}

	public void drawSprite(int x, int y, BSprite sprite, int scale) {
		if (scale > 1) {
			for (int i = 0; i < sprite.width; i++) {
				for (int j = 0; j < sprite.width; j++) {
					for (int scaleI = 0; scaleI < scale; scaleI++) {
						for (int scaleJ = 0; scaleJ < scale; scaleJ++) {
							draw(x + (i * scale) + scaleI, y + (j * scale) + scaleJ, sprite.getColor(i, j));
						}
					}
				}
			}
		} else {
			for (int i = 0; i < sprite.width; i++) {
				for (int j = 0; j < sprite.height; j++) {
					draw(x + i, y + j, sprite.getColor(i, j));
				}
			}
		}
	}
	
	public Color getColor(int x, int y) {
		return new Color(imageToPaint.getRGB(x * pixelWidth, y * pixelHeight));
	}
	
	

	// abstract, so must be overwritten
	public abstract boolean onUserCreate();
	public abstract boolean onUserUpdate(double frameTime);
	// optional method called when the engine is ended
	public boolean onUserDestroy() { return true; }

	private class EngineThread extends Thread {
		public void run() {
			if (!onUserCreate()) {
				engineActive = false;
			}
			
			long timer1 = System.nanoTime();
			long timer2 = System.nanoTime();
			
			while (engineActive) {
				while (engineActive) {
					timer2 = System.nanoTime();
					double elapsed = timer2 - timer1;
					timer1 = timer2;

					double frameTime = elapsed / 1_000_000_000.0; //convert nanoseconds to floating-point seconds

					// handle keyboard input
					for (int key = 0; key < keyNewState.length; key++) {
						keyboardState[key].pressed = false;
						keyboardState[key].released = false;

						if (keyNewState[key] != keyOldState[key]) {
							if (keyNewState[key]) {
								keyboardState[key].pressed = !keyboardState[key].held;
								keyboardState[key].held = true;
							} else {
								keyboardState[key].released = true;
								keyboardState[key].held = false;
							}
						}

						keyOldState[key] = keyNewState[key];
					}

					// handle mouse input
					for (int button = 0; button < mouseNewState.length; button++) {
						mouseState[button].pressed = false;
						mouseState[button].released = false;

						if (mouseNewState[button] != mouseOldState[button]) {
							if (mouseNewState[button]) {
								mouseState[button].pressed = !mouseState[button].held;
								mouseState[button].held = true;
							} else {
								mouseState[button].released = true;
								mouseState[button].held = false;
							}
						}
						
						mouseOldState[button] = mouseNewState[button];
					}
					
					if (!onUserUpdate(frameTime)) {
						engineActive = false;
					}
					paintGraphics.drawImage(imageToPaint, 0, 0, null);
					
					frameTimer += frameTime;
					frameCount++;
					if (frameTimer >= 1) {
						frameTimer -= 1;
						String title = "kiefac's Barista Engine - " + gameName + " - FPS: " + frameCount;
						gameFrame.setTitle(title);
						frameCount = 0;
					}
				}
			}
			if (onUserDestroy()) {
				// user permitted destroy, so exit
			} else {
				// user denied destroy, so stay active
				engineActive = true;
			}
			System.exit(0);
		}
	}

	protected class KeyState {
		boolean pressed;
		boolean held;
		boolean released;
		public KeyState() {
			pressed = false;
			held = false;
			released = false;
		}
	}

	// not using JavaFX Pair<> because JDK11 moved JavaFX into a separate, downloadable package.
	// didn't feel like requiring the user to download it, install as a library, etc. if they are using JDK11, so i just made my own class.
	private class Pair<T1, T2> {
		private T1 t1;
		private T2 t2;

		public Pair(T1 t1, T2 t2) {
			this.t1 = t1;
			this.t2 = t2;
		}

		public T1 first() {
			return t1;
		}

		public T2 last() {
			return t2;
		}
	}
}
