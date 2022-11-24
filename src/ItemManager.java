import java.util.ArrayList;
import java.util.List;

//定义图形管理器（静态）
//管理所有画板上图形，包括一个可能存在的预览图形
//还有负责选择图形的功能
public class ItemManager {
	private static List<Item> itemList = new ArrayList<Item>();
	private static int selectedIndex = -1;
	private static Item previewItem = null;
	private static final int selectDelta = 12;

	public static List<Item> getList() {
		return itemList;
	}

	public static Item getPreviewEle() {
		return previewItem;
	}

	public static void clearPreviewEle()
	{
		previewItem = null;
	}
	
	public static void add(Item e) {
		itemList.add(e);
	}

	public static void setPreviewEle(Item e) {
		previewItem = e;
	}
	
	public static void remove(Item e) {
		itemList.remove(e);
	}
	
	public static void removeAll() {
		itemList.removeAll(itemList);
	}

	public static Item getSelected()
	{
		if(selectedIndex!=-1)
			return itemList.get(selectedIndex);
		return null;
	}

	public static boolean hasSelected()
	{
		return (getSelected() != null);
	}

	public static void freeSelect(boolean remove)
	{
		if(remove)
		{
			selectedIndex = -1;
			return;
		}
		if(selectedIndex!=-1)
		{
			itemList.get(selectedIndex).isSelected = false;
			selectedIndex = -1;
		}
	}

	public static void doSelect(int x, int y)
	{
		boolean flag = false;
		for (int i = 0; i < itemList.size(); i++) {
			Item e = itemList.get(i);
			e.isSelected = false;
			if (flag == false) {
				if (e instanceof Line) {
					if (lineDist((Line) e, x, y) <= selectDelta) {
						selectedIndex = i;
						e.isSelected = true;
						flag = true;
					}
				} else if (e instanceof Rectangle) {
					if (rectDist((Rectangle) e, x, y) <= selectDelta) {
						selectedIndex = i;
						e.isSelected = true;
						flag = true;
					}
				} else if (e instanceof Text) {
					if (textDist((Text) e, x, y) <= selectDelta) {
						selectedIndex = i;
						e.isSelected = true;
						flag = true;
					}
				} else {
					if (circleDist((Circle) e, x, y) <= selectDelta) {
						selectedIndex = i;
						e.isSelected = true;
						flag = true;
					}
				}
			}
		}
		if(!flag)
		 	selectedIndex = -1;
	}

	private static int minOf(int a, int b) {
		if (a > b)
			return b;
		return a;
	}

	private static int lineDist(Line e, int x, int y) {
		if (e.x1 == e.x2) {
			if ((y <= e.y1 && y <= e.y2) || (y >= e.y1 && y >= e.y2))
				return minOf((int) Math.sqrt(Math.pow(x - e.x1, 2) + Math.pow(y - e.y1, 2)),
						(int) Math.sqrt(Math.pow(x - e.x2, 2) + Math.pow(y - e.y2, 2)));
			else
				return Math.abs(x - (int) e.x1);
		} else {
			if ((x <= e.x1 && x <= e.x2) || (x >= e.x1 && x >= e.x2))
				return minOf((int) Math.sqrt(Math.pow(x - e.x1, 2) + Math.pow(y - e.y1, 2)),
						(int) Math.sqrt(Math.pow(x - e.x2, 2) + Math.pow(y - e.y2, 2)));
			else {
				float k = 1.0f * (e.y1 - e.y2) / (e.x1 - e.x2);
				float b = 1.0f * e.y1 - k * e.x1;
				return Math.abs((int) (k * x + b - y));
			}
		}
	}
	
	private static int rectDist(Rectangle e, int x, int y)
	{
		int westDist = x - (int)e.x;
		int eastDist = (int)(e.x + e.width) - x;
		int northDist = y - (int)e.y;
		int southDist = (int)(e.y + e.height) - y;

		int dist = Integer.MAX_VALUE;
		if(westDist < 0 && northDist < 0)
			dist = (int)Math.sqrt(Math.pow(westDist, 2) + Math.pow(northDist, 2));
		else if(westDist < 0 && southDist < 0)
			dist = (int)Math.sqrt(Math.pow(westDist, 2) + Math.pow(southDist, 2));
		else if(eastDist < 0 && northDist < 0)
			dist = (int)Math.sqrt(Math.pow(eastDist, 2) + Math.pow(northDist, 2));
		else if(eastDist < 0 && southDist < 0)
			dist = (int)Math.sqrt(Math.pow(eastDist, 2) + Math.pow(southDist, 2));
		else if(westDist < 0)
			dist = Math.abs(westDist);
		else if(eastDist < 0)
			dist = Math.abs(eastDist);
		else if(northDist < 0)
			dist = Math.abs(northDist);
		else if(southDist < 0)
			dist = Math.abs(southDist);
		else
			dist = minOf(minOf(Math.abs(westDist), Math.abs(eastDist)),
					 	    minOf(Math.abs(northDist), Math.abs(southDist)));

		return dist;
	}

	private static int textDist(Text e, int x, int y)
	{
		return (int)Math.sqrt(Math.pow(e.x - (double)x, 2) + Math.pow(e.y - (double)y, 2));
	}

	private static int circleDist(Circle e, int x, int y)
	{
		int center_x = e.x + e.width / 2;
		int center_y = e.y + e.height / 2;
		int circleR = (int)(Math.sqrt(e.width * e.height) / 2);
		int distR = (int)Math.sqrt(Math.pow(center_x - x, 2) + Math.pow(center_y - y, 2));

		return Math.abs(circleR - distR);
	}
}
