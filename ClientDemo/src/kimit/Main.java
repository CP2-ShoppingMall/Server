package kimit;

import kimit.api.Client;
import kimit.api.ClientException;

import java.io.IOException;
import java.util.Scanner;

public class Main
{
	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		Client client = new Client(scanner.nextLine(), 8000);
		try
		{
			client.connect();
		}
		catch (IOException e)
		{
			System.out.println("Server connect error.");
			System.exit(0);
		}
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
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException(e);
		}
	}
}
