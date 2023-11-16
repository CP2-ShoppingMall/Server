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

	public Server(int port)
	{
		Port = port;
		Clients = new ArrayList<>();
	}

	public void start()
	{
		ServerSocket server;
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
				ServerThread thread = new ServerThread(server.accept(), Clients);
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
	}
}