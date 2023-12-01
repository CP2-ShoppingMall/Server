package kimit.protocol;

import kimit.server.Product;

import java.util.ArrayList;

public class ProductPacket extends Packet
{
	private final ArrayList<Product> Products;

	public ProductPacket(ArrayList<Product> products)
	{
		super(HeaderCode.PRODUCT_LIST);
		Products = products;
	}

	public ArrayList<Product> getProducts()
	{
		return Products;
	}
}
