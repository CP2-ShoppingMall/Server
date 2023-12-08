package kimit.server;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class Member implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	private final String ID;
	private final String Password;
	private final ArrayList<Product> Basket;
	private final ArrayList<Product> Purchase;

	public Member(String id, String password)
	{
		ID = id;
		Password = password;
		Basket = new ArrayList<>();
		Purchase = new ArrayList<>();
	}

	public String getID()
	{
		return ID;
	}

	public String getPassword()
	{
		return Password;
	}

	public ArrayList<Product> getBasket()
	{
		return Basket;
	}

	public ArrayList<Product> getPurchase()
	{
		return Purchase;
	}
}
