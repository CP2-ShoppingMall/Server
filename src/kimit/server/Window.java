package kimit.server;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame
{
	private final Server Server;
	private JPanel MainPanel;
	private JTextField CommandText;
	private JTextArea LogText;

	public Window(Server server)
	{
		Server = server;
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored)
		{

		}

		setTitle("Server");
		MainPanel = new JPanel(new BorderLayout());
		MainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		LogText = new JTextArea(20, 50);
		LogText.setEditable(false);
		LogText.setFocusable(false);
		CommandText = new JTextField(50);
		MainPanel.add(new JScrollPane(LogText), BorderLayout.CENTER);
		MainPanel.add(CommandText, BorderLayout.SOUTH);
		CommandText.addActionListener(e ->
		{
			Server.execute(CommandText.getText());
			CommandText.setText("");
		});
		add(MainPanel);
		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void log(String text)
	{
		LogText.append(text + "\n");
	}
}
