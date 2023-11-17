package kimit.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;

public class Server
{
	private final int Port;
	private ArrayList<ServerThread> Clients;
	private MemberDatabase MemberDB;

	public Server(int port)
	{
		Port = port;
		Clients = new ArrayList<>();
	}

	public void start()
	{
		ServerSocket server;
		MemberDB = new MemberDatabase("MemberDB");
		try
		{
			server = new ServerSocket(Port);

			new Thread(() ->
			{
				while (true)
				{
					String input = new Scanner(System.in).nextLine();
					if (input.equals("stop"))
					{
						try
						{
							server.close();
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
						break;
					}
				}
			}).start();

			while (true)
			{
				ServerThread thread = new ServerThread(server.accept(), Clients, MemberDB);
				Clients.add(thread);
				thread.start();
			}
		}
		catch (SocketException e)
		{
			for (var loop : Clients)
				loop.interrupt();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				MemberDB.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}