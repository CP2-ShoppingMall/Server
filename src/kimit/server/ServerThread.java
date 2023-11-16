package kimit.server;

import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread
{
	private final Socket ClientSocket;
	private final ArrayList<ServerThread> Clients;

	public ServerThread(Socket socket, ArrayList<ServerThread> clients)
	{
		ClientSocket = socket;
		Clients = clients;
	}

	@Override
	public void run()
	{
		while (!isInterrupted())
		{

		}
	}
}
