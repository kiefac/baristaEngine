package kiefac.barista;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BSprite {
	public int width = 0, height = 0;
	private Color[][] pixels; // will use RGBA colors to enable transparency
	
	public BSprite() {
		// do nothing for no args
	}
	
	public BSprite(int width, int height) {
		// create a new sprite with pixel dimensions (width * height)
		create(width, height);
	}
	
	public BSprite(File spriteFile) {
		if (!load(spriteFile)) {
			create(8, 8);
		}
	}
	
	public void setColor(int x, int y, Color color) {
		pixels[x][y] = color;
	}
	
	public Color getColor(int x, int y) {
		return pixels[x][y];
	}
	
	public Color sampleColor(double x, double y) {
		int sampleX = (int) (x * width);
		int sampleY = (int) (y * height);
		if (sampleX < 0 || sampleX > width || sampleY < 0 || sampleY > height) {
			return Color.BLACK;
		} else {
			return pixels[sampleX][sampleY];
		}
	}
	
	public boolean save(File spriteFile) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				image.setRGB(x, y, pixels[y][x].getRGB());
			}
		}
		return false;
	}
	
	public boolean load(File spriteFile) {
		pixels = null;
		width = 0;
		height = 0;
		
		BufferedImage image;
		try {
			image = ImageIO.read(spriteFile);
		} catch (IOException e) {
			System.err.println("Exception while reading file " + spriteFile.getPath() + "!");
			e.printStackTrace();
			return false;
		}
		
		width = image.getWidth();
		height = image.getHeight();
		create(width, height);
		
		int data[][] = new int[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				data[i][j] = image.getRGB(i, j);
			}
		}
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixels[y][x] = new Color(data[y][x], true);
			}
		}
		
		return true;
	}

	private void create(int width, int height) {
		this.width = width;
		this.height = height;
		/* ie. width = 3, height = 4:
		 * {[][][], <- sub-array is second arg;
		 *  [][][],    therefore, we must init
		 *  [][][],    with height as first arg
		 *  [][][]}
		 */
		pixels = new Color[height][width];
	}
}
