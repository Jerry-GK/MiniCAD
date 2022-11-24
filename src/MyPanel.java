import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

//import com.apple.laf.AquaButtonBorder.Toolbar;

//右侧的画板
public class MyPanel extends JPanel {
	private static final long serialVersionUID = 37L;
	protected static final int minItemDelta = 3;//太小的图形会忽略
	private static Point p_start, p_end;
	private static int tbStatus = 0;

	public MyPanel() {
		this.setFocusable(true);
		this.setBackground(Color.white);
		p_start = new Point();
		p_end = new Point();
		
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				p_start.x = p_end.x = e.getX();
				p_start.y = p_end.y = e.getY();
				if(tbStatus != 0)
					return;
				ItemManager.doSelect(p_start.x, p_start.y);
				repaint();
				requestFocus();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				p_end.x = e.getX();
				p_end.y = e.getY();
				if (!ItemManager.hasSelected()) {
					if (Math.pow(p_start.x - p_end.x, 2) + Math.pow(p_start.y - p_end.y, 2) >= minItemDelta * minItemDelta)
						addItem();
				}
				ItemManager.clearPreviewEle();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				p_start.x = e.getX();
				p_start.y = e.getY();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});

		this.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (ItemManager.getSelected() != null) 
				{
					p_start.x = p_end.x;
					p_start.y = p_end.y;
					p_end.x = e.getX();
					p_end.y = e.getY();
					Item sel =  ItemManager.getSelected();
					if(sel!=null)
						sel.moveTo(p_end.x - p_start.x, p_end.y - p_start.y);
					repaint();
				}
				else
				{
					ItemManager.clearPreviewEle();
					Point previewPoint = new Point(e.getX(), e.getY());
					PreviewItem(previewPoint);
					repaint();
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
			}
		});

		this.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (ItemManager.hasSelected()) {
					Item selEle = ItemManager.getSelected();
					if(selEle == null)
						return;
					if (e.getKeyChar() == '>' || e.getKeyChar() == '.')
						selEle.getWider();
					else if (e.getKeyChar() == '<' || e.getKeyChar() == ',')
						selEle.getThinner();
					else if (e.getKeyChar() == '+' || e.getKeyChar() == '=')
						selEle.getBigger();
					else if (e.getKeyChar() == '_' || e.getKeyChar() == '-')
						selEle.getSmaller();
					else if (e.getKeyChar() == 0x8 || e.getKeyChar() == 'd' || e.getKeyChar() == 'D') {
						ItemManager.remove(selEle);
						ItemManager.freeSelect(true);
					}
					repaint();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
	}

	public void setStatus(int s)
	{
		tbStatus = s;
		if(s!=0)
			ItemManager.freeSelect(false);
		repaint();
	}

	public void addItem() {
		if (tbStatus >= ToolBar.btnNum - 2 || tbStatus == 0)
			return;
		Item e = null;
		
		if(tbStatus == 1)
		{
			e = new Line(p_start.x, p_start.y, p_end.x, p_end.y);
		}
		else if (tbStatus == 2) {
			int sx, sy, w, h;
			if (p_start.x < p_end.x) {
				sx = p_start.x;
				w = p_end.x - p_start.x;
			} else {
				sx = p_end.x;
				w = p_start.x - p_end.x;
			}
			if (p_start.y < p_end.y) {
				sy = p_start.y;
				h = p_end.y - p_start.y;
			} else {
				sy = p_end.y;
				h = p_start.y - p_end.y;
			}
			e = new Rectangle(sx, sy, w, h);
		} 
		else if (tbStatus == 3)
		{
			int sx, sy, w, h;
			if (p_start.x < p_end.x) {
				sx = p_start.x;
				w = p_end.x - p_start.x;
			} else {
				sx = p_end.x;
				w = p_start.x - p_end.x;
			}
			if (p_start.y < p_end.y)
				sy = p_start.y;
			else
				sy = p_end.y;
			h = w;
			e = new Circle(sx, sy, w, h);
		}
		else if(tbStatus == 4) 
		{
			String inputStr = JOptionPane.showInputDialog("Please enter the text: ");
			if(inputStr != null && inputStr.length() > 0)
				e = new Text(p_start.x, p_start.y, inputStr);
		}
		if(e!=null)
			ItemManager.add(e);
		repaint();
	}

	public void PreviewItem(Point p) {
		if (tbStatus >= ToolBar.btnNum - 2 || tbStatus == 0)
			return;
		Item e = null;

		if(tbStatus == 1)
		{
			e = new Line(p_start.x, p_start.y, p.x, p.y);
		}
		else if (tbStatus == 2 || tbStatus == 4) {
			int sx, sy, w, h;
			if (p_start.x < p.x) {
				sx = p_start.x;
				w = p.x - p_start.x;
			} else {
				sx = p.x;
				w = p_start.x - p.x;
			}
			if (p_start.y < p.y) {
				sy = p_start.y;
				h = p.y - p_start.y;
			} else {
				sy = p.y;
				h = p_start.y - p.y;
			}
			e = new Rectangle(sx, sy, w, h);
		} 
		else if(tbStatus == 3) 
		{
			int sx, sy, w, h;
			if (p_start.x < p.x) {
				sx = p_start.x;
				w = p.x - p_start.x;
			} else {
				sx = p.x;
				w = p_start.x - p.x;
			}
			if (p_start.y < p.y)
				sy = p_start.y;
			else
				sy = p.y;
			h = w;
			e = new Circle(sx, sy, w, h);
		}
		if(e!=null)
		{
			ItemManager.setPreviewEle(e);
		}
		repaint();
	}

	//画出图形（实际图形和预览图形）
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		List<Item> ls = ItemManager.getList();

		Item e = ItemManager.getPreviewEle();
		if(e != null)
		{
			e.draw((Graphics2D) g);
		}
		for (Item it : ls) 
		{
			it.draw((Graphics2D) g);
		}
	}
}
