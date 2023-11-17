package kimit;

import kimit.packet.HeaderCode;
import kimit.packet.Packet;
import kimit.packet.RegisterLoginPacket;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Main
{
	public static void main(String[] args)
	{
		try
		{
			Socket socket = new Socket("localhost", 8000);
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			RegisterLoginPacket register = new RegisterLoginPacket(HeaderCode.REGISTER, "asdf", "asdf");
			out.writeObject(register);
			out.writeObject(register);
			RegisterLoginPacket login = new RegisterLoginPacket(HeaderCode.LOGIN, "asdf", "asdf");
			RegisterLoginPacket login2 = new RegisterLoginPacket(HeaderCode.LOGIN, "qwer", "qwer");
			out.writeObject(login);
			out.writeObject(login2);

			new Thread(() ->
			{
				while (true)
				{
					try
					{
						Packet packet = (Packet) in.readObject();
						System.out.println(packet);
					}
					catch (EOFException ignored)
					{

					}
					catch (IOException | ClassNotFoundException e)
					{
						e.printStackTrace();
					}
				}
			}).start();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}
}
