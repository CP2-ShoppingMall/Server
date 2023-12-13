package kimit.server;

import kimit.protocol.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ServerThread extends Thread
{
	private final Socket ClientSocket;
	private final Server Server;
	private ObjectInputStream In;
	private ObjectOutputStream Out;
	private Member Member;
	private String Session = null;

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

			while (true)
			{
				Packet packet = ((Packet) In.readObject());
				Out.reset();
				switch (packet.getHeader())
				{
					case REGISTER:
						register(packet);
						break;
					case LOGIN:
						login(packet);
						break;
					case PRODUCT_LIST:
						Out.writeObject(new ProductListPacket(HeaderCode.PRODUCT_LIST, Server.getProductDB().getData()));
						break;
					case POST_PRODUCT:
						Server.getProductDB().add(((ProductPacket) packet).getProduct());
						Server.getWindow().log("Member " + Member.getID() + "(IP : " + ClientSocket.getInetAddress() + ") has posted product \"" + ((ProductPacket) packet).getProduct().getTitle() + "\".");
						break;
					case BASKET:
						basket(packet);
						break;
					case PURCHASE:
					case PURCHASE_IN_BASKET:
						purchase(packet);
						break;
				}
			}
		}
		catch (EOFException e)
		{
			close();
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
			Server.getSessions().removeIf(p -> p.equals(Session));
			Server.getWindow().log("Client " + ClientSocket.getInetAddress() + " is disconnected.");
		}
	}

	public void close()
	{
		try
		{
			if (In != null)
				In.close();
			if (Out != null)
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
		boolean isExist = false;
		for (var loop : Server.getMemberDB().getData())
		{
			if (loop.getID().equals(register.getID()))
			{
				isExist = true;
				break;
			}
		}
		if (!isExist && !register.getID().isEmpty() && !register.getPassword().isEmpty())
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
		Member member = null;
		for (var loop : Server.getMemberDB().getData())
		{
			if (loop.getID().equals(login.getID()))
			{
				member = loop;
				break;
			}
		}
		RegisterLoginPacket response = new RegisterLoginPacket(HeaderCode.SUCCESS, login.getID(), member);
		if (member != null && member.getPassword().equals(login.getPassword()) && !Server.getSessions().contains(response.getPassword()))
		{
			Server.getSessions().add(response.getPassword());
			Out.writeObject(response);
			Member = member;
			Session = response.getPassword();
			Server.getWindow().log("Client " + ClientSocket.getInetAddress() + " has logged in. ID : " + member.getID() + ", Session : " + response.getPassword());
		}
		else if (member == null || !member.getPassword().equals(login.getPassword()))
			Out.writeObject(new Packet(HeaderCode.LOGIN_ERROR));
		else if (Server.getSessions().contains(response.getPassword()))
			Out.writeObject(new Packet(HeaderCode.SESSION_ERROR));
	}

	private void basket(Packet packet) throws IOException
	{
		ProductPacket product = ((ProductPacket) packet);
		Server.getMemberDB().getData().removeIf(p -> p.equals(Member));
		Member.getBasket().add(product.getProduct());
		Server.getMemberDB().add(Member);
		Out.writeObject(new MemberPacket(Member));
		Server.getWindow().log("Member " + Member.getID() + "(IP : " + ClientSocket.getInetAddress() + ") has added product \"" + product.getProduct().getTitle() + "\" to basket.");
	}

	private void purchase(Packet packet) throws IOException
	{
		ProductListPacket productList = ((ProductListPacket) packet);
		Server.getMemberDB().getData().removeIf(p -> p.equals(Member));
		productList.getProducts().forEach(loop ->
		{
			Member.getPurchase().add(loop);
			if (productList.getHeader().equals(HeaderCode.PURCHASE_IN_BASKET))
			{
				for (var loop2 : Member.getBasket())
				{
					if (loop.getTitle().equals(loop2.getTitle()) && loop.getPrice() == loop2.getPrice())
					{
						Member.getBasket().remove(loop2);
						break;
					}
				}
			}
		});
		Server.getMemberDB().add(Member);
		Out.writeObject(new MemberPacket(Member));
		Server.getWindow().log("Member " + Member.getID() + "(IP : " + ClientSocket.getInetAddress() + ") has purchased " + productList.getProducts().size() + " products. Total price : " + productList.getProducts().stream().mapToInt(Product::getPrice).sum());
	}
}
