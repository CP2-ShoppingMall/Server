package kimit.protocol;

import kimit.server.Product;

import java.util.ArrayList;

public class ProductListPacket extends Packet
{
	private final ArrayList<Product> Products;

	public ProductListPacket(HeaderCode header, ArrayList<Product> products)
	{
		super(header);
		Products = products;
	}

	public ArrayList<Product> getProducts()
	{
		return Products;
	}
}
