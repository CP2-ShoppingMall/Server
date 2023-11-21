package kimit.server;

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
	private MemberDatabase MemberDB;
	private Window Window;

	public Server(int port)
	{
		Port = port;
		Clients = new ArrayList<>();
		Sessions = new ArrayList<>();
	}

	public void start()
	{
		Window = new Window(this);
		MemberDB = new MemberDatabase("MemberDB");
		try
		{
			Socket = new ServerSocket(Port);
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
	}

	public ArrayList<ServerThread> getClients()
	{
		return Clients;
	}

	public MemberDatabase getMemberDB()
	{
		return MemberDB;
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