import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.Color;
import java.awt.Font;

//图形类（包括直线、长方形、圆、文本四个子类）
public abstract class Item {
	public Color itemColor = Color.black;
	public boolean isSelected = false;
	private Stroke s;
	public float strokeWidth = 1.0f;

	public void setColor(Color c) {
		this.itemColor = c;
	}

	public void draw(Graphics2D g2d) {
		g2d.setColor(itemColor);
		if (isSelected == true)
			s = new BasicStroke(strokeWidth + 3.0f);
		else
			s = new BasicStroke(strokeWidth);
		g2d.setStroke(s);
	}

	public abstract void moveTo(int dx, int dy);

	public abstract void getBigger();

	public abstract void getSmaller();

	public void getWider() {
		strokeWidth++;
	};

	public void getThinner() {
		if (strokeWidth < 1.0f)
			strokeWidth = 1.0f;
		else
			strokeWidth--;
	}
}


class Line extends Item {
	public float x1, y1, x2, y2;

	public Line(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	@Override
	public void draw(Graphics2D g2d) {
		super.draw(g2d);
		g2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	}

	public void moveTo(int dx, int dy) {
		x1 += dx;
		x2 += dx;
		y1 += dy;
		y2 += dy;
	}

	@Override
	public void getBigger() {
		if (x1 == x2) {
			if (y1 < y2)
				y2++;
			else
				y1++;
		} else if (x1 < x2) {
			x2 += 1;
			y2 += (y2 - y1) / (x2 - x1);
		} else {
			x1 += 1;
			y1 += (y2 - y1) / (x2 - x1);
		}
	}

	@Override
	public void getSmaller() {
		if (Math.abs((int) (x2 - x1)) >= MyPanel.minItemDelta && Math.abs((int) (y2 - y1)) >= MyPanel.minItemDelta) {
			if (x1 == x2) {
				if (y1 < y2)
					y2--;
				else
					y1--;
			} else if (x1 < x2) {
				x2 -= 1;
				y2 -= (y2 - y1) / (x2 - x1);
			} else {
				x1 -= 1;
				y1 -= (y2 - y1) / (x2 - x1);
			}
		}
	}
}

//
class Rectangle extends Item {
	public float x, y, width, height;

	public Rectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public void draw(Graphics2D g2d) {
		super.draw(g2d);
		g2d.drawRect((int) x, (int) y, (int) width, (int) height);
	}

	public void moveTo(int dx, int dy) {
		x += dx;
		y += dy;
	}

	@Override
	public void getBigger() {
		width += width / height;
		height++;
	}

	@Override
	public void getSmaller() {
		width -= width / height;
		height--;
		if (width <= MyPanel.minItemDelta)
			width = MyPanel.minItemDelta;
		else if (height <= MyPanel.minItemDelta)
			height = MyPanel.minItemDelta;
	}
}

//
class Circle extends Item {
	public int x, y, width, height;

	public Circle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public void draw(Graphics2D g2d) {
		super.draw(g2d);
		g2d.drawOval(x, y, width, height);
	}

	public void moveTo(int dx, int dy) {
		x += dx;
		y += dy;
	}

	//
	@Override
	public void getBigger() {
		width++;
		height++;
	}

	@Override
	public void getSmaller() {
		width--;
		height--;
		if (width <= MyPanel.minItemDelta)
			width = MyPanel.minItemDelta;
		if (height <= MyPanel.minItemDelta)
			height = MyPanel.minItemDelta;
	}
}

//
class Text extends Item {
	public float x, y;
	public String content;
	public Font f;
	public FontMetrics fm = null;
	private static final String defaultFont = "Times New Roman";

	public Text(int x, int y, String content) {
		this.x = x;
		this.y = y;
		this.content = content == null ? "" : content;
		f = new Font(defaultFont, Font.BOLD, 22);
	}

	@Override
	public void draw(Graphics2D g2d) {
		super.draw(g2d);
		fm = g2d.getFontMetrics(f);
		Font tf;
		if (isSelected == true)
			tf = new Font(defaultFont, Font.BOLD, this.f.getSize() + 4);
		else
			tf = f;
		g2d.setFont(tf);
		g2d.drawString(content, x, y);
	}

	public void moveTo(int dx, int dy) {
		this.x += dx;
		this.y += dy;
	}

	@Override
	public void getBigger() {
		f = new Font(defaultFont, Font.BOLD, f.getSize() + 1);
	}

	@Override
	public void getSmaller() {
		f = new Font(defaultFont, Font.BOLD, f.getSize() > MyPanel.minItemDelta ? f.getSize() - 1 : MyPanel.minItemDelta);
	}
}
