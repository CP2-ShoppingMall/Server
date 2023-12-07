package kimit.api;

import kimit.protocol.*;
import kimit.server.Member;
import kimit.server.Product;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client
{
	private final String Address;
	private final int Port;
	private ObjectOutputStream Out;
	private ObjectInputStream In;
	private String Session;
	private Member Member;

	public Client(String address, int port)
	{
		Address = address;
		Port = port;
		Session = null;
	}

	public Member getMember()
	{
		return Member;
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

	public void connect() throws IOException
	{
		Socket socket = new Socket(Address, Port);
		Out = new ObjectOutputStream(socket.getOutputStream());
		In = new ObjectInputStream(socket.getInputStream());
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
			{
				Session = ((RegisterLoginPacket) response).getPassword();
				Member = ((RegisterLoginPacket) response).getPayload();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public ArrayList<Product> product()
	{
		try
		{
			Out.writeObject(new Packet(HeaderCode.PRODUCT_LIST));
			Packet response = read();
			if (response.getHeader().equals(HeaderCode.PRODUCT_LIST))
				return ((ProductListPacket) response).getProducts();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public void post(Product product)
	{
		try
		{
			Out.writeObject(new ProductPacket(HeaderCode.POST_PRODUCT, product));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void basket(Product product)
	{
		try
		{
			Out.writeObject(new ProductPacket(HeaderCode.BASKET, product));
			Packet response = read();
			if (response.getHeader().equals(HeaderCode.MEMBER))
				Member = ((MemberPacket) response).getMember();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void purchase(ArrayList<Product> products)
	{
		try
		{
			Out.writeObject(new ProductListPacket(HeaderCode.PURCHASE, products));
			Packet response = read();
			if (response.getHeader().equals(HeaderCode.MEMBER))
				Member = ((MemberPacket) response).getMember();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
