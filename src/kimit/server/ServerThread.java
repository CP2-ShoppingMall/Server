package kimit.server;

import kimit.packet.HeaderCode;
import kimit.packet.Packet;
import kimit.packet.RegisterLoginPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread
{
	private final Socket ClientSocket;
	private final ArrayList<ServerThread> Clients;
	private final MemberDatabase MemberDB;
	private ObjectInputStream In;
	private ObjectOutputStream Out;

	public ServerThread(Socket socket, ArrayList<ServerThread> clients, MemberDatabase memberDB)
	{
		ClientSocket = socket;
		Clients = clients;
		MemberDB = memberDB;
	}

	@Override
	public void run()
	{
		System.out.println("Client " + ClientSocket.getInetAddress() + " is connected.");
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
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ClientSocket.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			System.out.println("Client " + ClientSocket.getInetAddress() + " is disconnected.");
		}
	}

	private void register(Packet packet) throws IOException
	{
		RegisterLoginPacket register = ((RegisterLoginPacket) packet);
		if (MemberDB.getMember(register.getID()) == null)
		{
			Member member = new Member(register.getID(), register.getPassword());
			MemberDB.add(member);
			Out.writeObject(new Packet(HeaderCode.SUCCESS));
		}
		else
			Out.writeObject(new Packet(HeaderCode.REGISTER_ERROR));
	}

	private void login(Packet packet) throws IOException
	{
		RegisterLoginPacket login = ((RegisterLoginPacket) packet);
		Member member = MemberDB.getMember(login.getID());
		if (member != null && member.getPassword().equals(login.getPassword()))
		{
			Out.writeObject(new Packet(HeaderCode.SUCCESS));
			// TODO : PostLogin
		}
		else
			Out.writeObject(new Packet(HeaderCode.LOGIN_ERROR));
	}
}
