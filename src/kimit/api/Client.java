package kimit.api;

import kimit.protocol.HeaderCode;
import kimit.protocol.Packet;
import kimit.protocol.RegisterLoginPacket;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client
{
	private final String Address;
	private final int Port;
	private ObjectOutputStream Out;
	private ObjectInputStream In;
	private String Session;

	public Client(String address, int port)
	{
		Address = address;
		Port = port;
		Session = null;
	}

	private Packet read()
	{
		while (true)
		{
			try
			{
				Packet packet = ((Packet) In.readObject());
				if (packet != null)
					return packet;
			}
			catch (EOFException ignored)
			{

			}
			catch (IOException | ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void connect()
	{
		try
		{
			Socket socket = new Socket(Address, Port);
			Out = new ObjectOutputStream(socket.getOutputStream());
			In = new ObjectInputStream(socket.getInputStream());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void register(String id, String password) throws ClientException
	{
		try
		{
			Out.writeObject(new RegisterLoginPacket(HeaderCode.REGISTER, id, password));
			Packet packet = read();
			if (!packet.getHeader().equals(HeaderCode.SUCCESS))
				throw new ClientException(packet.getHeader(), "중복된 ID 입니다.");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void login(String id, String password) throws ClientException
	{
		try
		{
			Out.writeObject(new RegisterLoginPacket(HeaderCode.LOGIN, id, password));
			Packet response = read();
			if (response.getHeader().equals(HeaderCode.LOGIN_ERROR))
				throw new ClientException(response.getHeader(), "ID 또는 비밀번호가 올바르지 않습니다.");
			else if (response.getHeader().equals(HeaderCode.SESSION_ERROR))
				throw new ClientException(response.getHeader(), "이미 로그인되어 있는 세션이 존재합니다.");
			else
				Session = ((RegisterLoginPacket) response).getPassword();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
