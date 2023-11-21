package kimit;

import kimit.api.Client;
import kimit.api.ClientException;

import java.util.Scanner;

public class Main
{
	public static void main(String[] args)
	{
		Client client = new Client("localhost", 8000);
		client.connect();
		Scanner scanner = new Scanner(System.in);
		String id = scanner.nextLine();
		String password = scanner.nextLine();
		try
		{
			client.register(id, password);
			client.login(id, password);
			//client.login("asdf", "asfd");
			while (true)
			{
				Thread.sleep(1000);
			}
		}
		catch (ClientException e)
		{
			System.out.println(e.getMessage());
		} catch (InterruptedException e)
		{
			throw new RuntimeException(e);
		}
	}
}
