package kimit.server;

import kimit.protocol.HeaderCode;
import kimit.protocol.Packet;
import kimit.protocol.RegisterLoginPacket;
import org.jline.reader.LineReader;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ServerThread extends Thread
{
	private final Socket ClientSocket;
	private final Server Server;
	private ObjectInputStream In;
	private ObjectOutputStream Out;

	public ServerThread(Socket socket, Server server)
	{
		ClientSocket = socket;
		Server = server;
	}

	@Override
	public void run()
	{
		Server.getWindow().log("Client " + ClientSocket.getInetAddress() + " is connected.");
		try
		{
			Out = new ObjectOutputStream(ClientSocket.getOutputStream());
			In = new ObjectInputStream(ClientSocket.getInputStream());

			while (!isInterrupted())
			{
				Packet packet = ((Packet) In.readObject());
				switch (packet.getHeader())
				{
					case REGISTER:
						register(packet);
						break;
					case LOGIN:
						login(packet);
						break;
				}
			}
		}
		catch (SocketException ignored)
		{

		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		finally
		{
			Server.getWindow().log("Client " + ClientSocket.getInetAddress() + " is disconnected.");
		}
	}

	public void close()
	{
		try
		{
			In.close();
			Out.close();
			ClientSocket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void register(Packet packet) throws IOException
	{
		RegisterLoginPacket register = ((RegisterLoginPacket) packet);
		if (Server.getMemberDB().getMember(register.getID()) == null)
		{
			Member member = new Member(register.getID(), register.getPassword());
			Server.getMemberDB().add(member);
			Out.writeObject(new Packet(HeaderCode.SUCCESS));
			Server.getWindow().log("Client " + ClientSocket.getInetAddress() + " has registered. ID : " + register.getID());
		}
		else
			Out.writeObject(new Packet(HeaderCode.REGISTER_ERROR));
	}

	private void login(Packet packet) throws IOException
	{
		RegisterLoginPacket login = ((RegisterLoginPacket) packet);
		Member member = Server.getMemberDB().getMember(login.getID());
		RegisterLoginPacket response = new RegisterLoginPacket(HeaderCode.SUCCESS, null, login.getID());
		if (member != null && member.getPassword().equals(login.getPassword()) && !Server.getSessions().contains(response.getPassword()))
		{
			Server.getSessions().add(response.getPassword());
			Out.writeObject(response);
			Server.getWindow().log("Client " + ClientSocket.getInetAddress() + " has logged in. ID : " + member.getID() + ", Session : " + response.getPassword());
		}
		else if (member == null || !member.getPassword().equals(login.getPassword()))
			Out.writeObject(new Packet(HeaderCode.LOGIN_ERROR));
		else if (Server.getSessions().contains(response.getPassword()))
			Out.writeObject(new Packet(HeaderCode.SESSION_ERROR));
	}
}
