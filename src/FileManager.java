import java.util.ArrayList;
import java.util.List;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
	private static final String sepa = java.io.File.separator;

	public static void open(String path, String file) throws FileNotFoundException, IOException {
		FileReader readFile = new FileReader(path + sepa + file);
		BufferedReader bufReader = new BufferedReader(readFile);
		String s = new String(bufReader.readLine());
		bufReader.close();
		int i = 0;
		List<String> ls = new ArrayList<String>();
		for (i = 0; i < s.length(); i++) {
			int j;
			StringBuilder str = new StringBuilder();
			for (j = i; j < s.length() - 1; j++) {
				if (s.charAt(j) == ' ' && s.charAt(j + 1) == ' ') 
				{
					j++;
					str.append(' ');
				} 
				else if (s.charAt(j) == ' ')
					break;
				else
					str.append(s.charAt(j));
			}
			i = j;
			ls.add(str.toString());
		}

		ItemManager.removeAll();
		List<Item> itemList = ItemManager.getList();
		Item it;
		i = 0;
		while (i != ls.size()) {
			if (ls.get(i).equals("Line"))
				it = new Line(Integer.parseInt(ls.get(++i)), Integer.parseInt(ls.get(i + 1)),
						Integer.parseInt(ls.get(i + 2)), Integer.parseInt(ls.get(i + 3)));
			else if (ls.get(i).equals("Rectangle"))
				it = new Rectangle(Integer.parseInt(ls.get(++i)), Integer.parseInt(ls.get(i + 1)),
						Integer.parseInt(ls.get(i + 2)), Integer.parseInt(ls.get(i + 3)));
			else if (ls.get(i).equals("Circle"))
				it = new Circle(Integer.parseInt(ls.get(++i)), Integer.parseInt(ls.get(i + 1)),
						Integer.parseInt(ls.get(i + 2)), Integer.parseInt(ls.get(i + 3)));
			else {
				it = new Text(Integer.parseInt(ls.get(++i)), Integer.parseInt(ls.get(i + 1)), ls.get(i + 2));
				((Text) it).f = new Font("Times New Roman", Font.BOLD, Integer.parseInt(ls.get(i + 3)));
			}
			i += 4;
			it.itemColor = ToolBar.clr[Integer.parseInt(ls.get(i))];
			it.strokeWidth = Float.parseFloat(ls.get(i + 1));
			if (ls.get(i + 2).equals("false"))
				it.isSelected = false;
			else
				it.isSelected = true;
			itemList.add(it);
			i += 3;
		}
	}

	public static void save(String path, String file) throws IOException {
		StringBuilder str = new StringBuilder();
		List<Item> itemList = ItemManager.getList();
		for (Item it : itemList) {
			if (it instanceof Line)
				str.append("Line " + (int)((Line) it).x1 + " " + (int)((Line) it).y1 + " " + (int)((Line) it).x2 + " " + (int)((Line) it).y2
						+ " ");
			else if (it instanceof Rectangle)
				str.append("Rectangle " + (int)((Rectangle) it).x + " " + (int)((Rectangle) it).y + " " + (int)((Rectangle) it).width + " "
						+ (int)((Rectangle) it).height + " ");
			else if (it instanceof Circle)
				str.append("Circle " + ((Circle) it).x + " " + ((Circle) it).y + " " + ((Circle) it).width + " " + ((Circle) it).height
						+ " ");
			else {
				String dest = ((Text) it).content;
				dest = dest.replace(" ", "  ");
				str.append("Text " + (int)((Text) it).x + " " + (int)((Text) it).y + " " + dest + " "
						+ ((Text) it).f.getSize() + " ");
			}
			for (int i = 0; i < ToolBar.clr.length; i++)
				if (it.itemColor.equals(ToolBar.clr[i])) {
					str.append(i + " ");
					break;
				}
			str.append(it.strokeWidth + " ").append(it.isSelected + " ");
		}
		FileWriter writer = new FileWriter(path + sepa + file);
		writer.write(str.toString());
		writer.flush();
		writer.close();
	}
}
