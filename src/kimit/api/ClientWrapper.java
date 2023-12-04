package kimit.api;

public class ClientWrapper
{
	private static ClientWrapper Wrapper;
	private final Client Client;

	private ClientWrapper(String address, int port)
	{
		Client = new Client(address, port);
	}

	public static void init(String address, int port)
	{
		Wrapper = new ClientWrapper(address, port);
	}

	public static ClientWrapper get()
	{
		return Wrapper;
	}

	public Client getClient()
	{
		return Client;
	}
}
