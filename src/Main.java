import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
	private static JFrame frm;
	public static final int panelSize = 650;

	//
	public Main() {
		ToolBar t = new ToolBar();//创建工具栏
		frm = new JFrame("MiniCAD");
		frm.setSize(panelSize, panelSize);
		frm.setLayout(new BorderLayout());
		frm.setResizable(false);//不能调整大小
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.add(t, BorderLayout.WEST);
		frm.add(t.cadPanel, BorderLayout.CENTER);
		frm.setLocationRelativeTo(null);
		frm.setVisible(true);
	}

	//保存cad文件
	public static void save()
	{
		FileDialog savedFile = new FileDialog(frm, "Save", FileDialog.SAVE);
		savedFile.setVisible(true);
		savedFile.setLocationRelativeTo(null);
		String curPath = savedFile.getDirectory();
		String fileName = savedFile.getFile();
		if (curPath == null || fileName == null)
			return;
		try 
		{
			FileManager.save(curPath, fileName);
		} 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null, "Error", "IO exception!", JOptionPane.ERROR_MESSAGE);
		}
	}

	//打开cad文件
	//调用FileManager类的read()方法，读取文件
	public static void open() 
	{
		FileDialog openFile = new FileDialog(frm, "Open", FileDialog.LOAD);
		openFile.setVisible(true);
		openFile.setLocationRelativeTo(null);
		String curPath = openFile.getDirectory();
		String fileName = openFile.getFile();
		if (curPath == null || fileName == null)
			return;
		try 
		{
			FileManager.open(curPath, fileName);
		}
		catch (FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "Error", "File not found!", JOptionPane.ERROR_MESSAGE);
		} 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null, "Error", "IO exception!", JOptionPane.ERROR_MESSAGE);
		}
	}

	//程序入口
	public static void main(String[] args) 
	{
		new Main();
	}
}
