package kimit.protocol;

import kimit.server.Product;

public class ProductPacket extends Packet
{
	private final Product Product;

	public ProductPacket(HeaderCode header, Product product)
	{
		super(header);
		Product = product;
	}

	public kimit.server.Product getProduct()
	{
		return Product;
	}
}
