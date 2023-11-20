package kimit;

import kimit.api.Client;
import kimit.api.ClientException;

public class Main
{
	public static void main(String[] args)
	{
		Client client = new Client("localhost", 8000);
		client.connect();
		try
		{
			client.register("asdf", "asfd");
			client.login("asdf", "asfd");
			client.login("asdf", "asfd");
		}
		catch (ClientException e)
		{
			System.out.println(e.getMessage());
		}
	}
}
