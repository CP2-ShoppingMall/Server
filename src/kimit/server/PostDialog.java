package kimit.server;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PostDialog extends JDialog implements ActionListener
{
	private final Server Server;
	private JTextField Title;
	private JTextField Price;
	private JTextArea Detail;
	private JTextField ImagePath;
	private JButton ImageChoose, Post;
	public PostDialog(JFrame frame, Server server)
	{
		super(frame, "Post");
		Server = server;

		Title = new JTextField(40);
		Price = new JTextField(40);
		Detail = new JTextArea(10, 40);
		JScrollPane DetailPane = new JScrollPane(Detail);
		ImagePath = new JTextField(35);
		ImagePath.setEditable(false);
		ImageChoose = new JButton("...");
		ImageChoose.addActionListener(this);
		Post = new JButton("Post");
		Post.addActionListener(this);
		setLayout(new FlowLayout());
		add(Title);
		add(Price);
		add(DetailPane);
		JPanel ImagePanel = new JPanel(new FlowLayout());
		ImagePanel.add(ImagePath);
		ImagePanel.add(ImageChoose);
		add(ImagePanel);
		add(Post);
		setSize(400, 350);
		setResizable(false);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JButton source = ((JButton) e.getSource());
		if (source.equals(ImageChoose))
		{
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("JPG or PNG Images", "jpg", "png"));
			if (fileChooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION)
				ImagePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
		}
		else if (source.equals(Post))
		{
			if (Title.getText().isEmpty() || Detail.getText().isEmpty() || ImagePath.getText().isEmpty())
				JOptionPane.showMessageDialog(getParent(), "제목, 내용, 이미지가 필요합니다.", "Error", JOptionPane.ERROR_MESSAGE);
			else
			{
				try
				{
					Server.getProductDB().add(new Product(Title.getText(), Integer.parseInt(Price.getText()), Detail.getText(), new ImageIcon(ImagePath.getText())));
					dispose();
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}
}
