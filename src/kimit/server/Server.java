package kimit.server;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server
{
	private final int Port;
	private Terminal Console;
	private ArrayList<ServerThread> Clients;
	private ArrayList<String> Sessions;
	private MemberDatabase MemberDB;

	public Server(int port)
	{
		Port = port;
		Clients = new ArrayList<>();
		Sessions = new ArrayList<>();
	}

	public void start()
	{
		ServerSocket server;
		MemberDB = new MemberDatabase("MemberDB");
		try
		{
			Console = TerminalBuilder.builder().system(false).streams(System.in, System.out).build();
			server = new ServerSocket(Port);

			new Thread(() ->
			{
				while (true)
				{
					try
					{
						ServerThread thread = new ServerThread(server.accept(), this);
						Clients.add(thread);
						thread.start();
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
			}).start();

			while (true)
			{
				// TODO : need to apply JLine.
				LineReader reader = LineReaderBuilder.builder().terminal(Console).build();
				String input = reader.readLine(">");
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

	public Terminal getConsole()
	{
		return Console;
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
}