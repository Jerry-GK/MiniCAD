import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ToolBar extends JPanel {
	private static final long serialVersionUID = 37L;
	public static final int btnNum = 7;
	private static JButton[] jbtn;
	public static final int ColorNum = 9;
	private static Dimension pSize;
	private static int btnStatus = 0;
	private static Color color = Color.black;
	private static final String[] btnHint = {"Select", "Line", "Rectangle", "Circle", "Text", "Open", "Save"};
	public static final Color[] clr = { Color.black, Color.blue, Color.cyan, Color.gray, Color.green,
			Color.lightGray, Color.pink, Color.red, Color.yellow };

	protected MyPanel cadPanel = new MyPanel();//创建画板

	class CADColorJPanel extends JPanel {
		private static final long serialVersionUID = 37L;
		private JButton[] jbtn;

		class ColorListener implements ActionListener {
			private Color clr;

			ColorListener(Color clr) {
				this.clr = clr;
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				color = clr;
				ItemManager.getSelected().setColor(color);
				cadPanel.repaint();
			}
		}

		//
		public CADColorJPanel() {
			this.setLayout(new GridLayout(3, 3));
			jbtn = new JButton[ColorNum];
			for (int i = 0; i < ColorNum; i++) {
				jbtn[i] = new JButton();
				jbtn[i].setBackground(clr[i]);
				jbtn[i].setOpaque(true);
				jbtn[i].setBorderPainted(false);
				jbtn[i].addActionListener(new ColorListener(clr[i]));
				this.add(jbtn[i]);
			}
		}
	}

	class ButtonListener implements ActionListener {
		private int status;

		public ButtonListener(int status) {
			this.status = status;
		}

		//修改选中按钮的文字颜色
		@Override
		public void actionPerformed(ActionEvent e) {
			jbtn[btnStatus].setForeground(Color.BLACK);
			btnStatus = status;
			jbtn[btnStatus].setForeground(Color.RED);
			cadPanel.setStatus(btnStatus);
		}
	}

	class OpenListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			Main.open();
			cadPanel.repaint();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}
	}

	class SaveListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			Main.save();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}
	}

	//工具栏
	public ToolBar() {
		Font font = new Font("Times New Roman", Font.BOLD, 16);
		pSize = new Dimension(Main.panelSize / 5, Main.panelSize);
		this.setPreferredSize(pSize);
		this.setLayout(new GridLayout(btnNum + 1, 1));
		jbtn = new JButton[btnNum];
		for (int i = 0; i < btnNum; i++) {
			jbtn[i] = new JButton(btnHint[i]);
			jbtn[i].setOpaque(true);
			jbtn[i].setFont(font);
			if (i < btnNum - 2)//选择或绘图
				jbtn[i].addActionListener(new ButtonListener(i));
			else if(i == btnNum - 2)//打开文件
				jbtn[i].addMouseListener(new OpenListener());
			else if(i == btnNum - 1)//保存文件
				jbtn[i].addMouseListener(new SaveListener());
			this.add(jbtn[i]);
		}
		this.add(new CADColorJPanel());
	}

	public int getBtnNum()
	{
		return btnStatus;
	}
}
