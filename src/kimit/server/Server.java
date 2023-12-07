package kimit.server;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server
{
	private final int Port;
	private ServerSocket Socket;
	private ArrayList<ServerThread> Clients;
	private ArrayList<String> Sessions;
	private Database<Member> MemberDB;
	private Database<Product> ProductDB;
	private Window Window;

	public Server(int port)
	{
		Port = port;
		Clients = new ArrayList<>();
		Sessions = new ArrayList<>();
	}

	public void start()
	{
		MemberDB = new Database<>("MemberDB");
		ProductDB = new Database<>("ProductDB");
		try
		{
			Socket = new ServerSocket(Port);
			Window = new Window(this);
			Window.addWindowListener(new WindowAdapter()
			{
				@Override
				public void windowClosing(WindowEvent e)
				{
					try
					{
						Socket.close();
					}
					catch (IOException ex)
					{
						ex.printStackTrace();
					}
				}
			});
			while (true)
			{
				try
				{
					ServerThread thread = new ServerThread(Socket.accept(), this);
					Clients.add(thread);
					thread.start();
				}
				catch (SocketException e)
				{
					for (var loop : Clients)
						loop.close();
					break;
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				Window.dispose();
				MemberDB.close();
				ProductDB.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void execute(String command)
	{
		if (command.equals("stop"))
		{
			try
			{
				Socket.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else if (command.equals("post"))
		{
			new PostDialog(Window, this);
		}
		else if (command.equals("member"))
			MemberDB.getData().forEach(loop -> Window.log("ID : " + loop.getID() + ", Password : " + loop.getPassword()));
	}

	public ArrayList<ServerThread> getClients()
	{
		return Clients;
	}

	public Database<Member> getMemberDB()
	{
		return MemberDB;
	}

	public Database<Product> getProductDB()
	{
		return ProductDB;
	}

	public ArrayList<String> getSessions()
	{
		return Sessions;
	}

	public kimit.server.Window getWindow()
	{
		return Window;
	}
}