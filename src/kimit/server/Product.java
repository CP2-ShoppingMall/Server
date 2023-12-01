package kimit.server;

import java.awt.*;

public class Product
{
	private final String Title;
	private final int Price;
	private final String Detail;
	private final Image Image;

	public Product(String title, int price, String detail, Image image)
	{
		Title = title;
		Price = price;
		Detail = detail;
		Image = image;
	}

	public String getTitle()
	{
		return Title;
	}

	public int getPrice()
	{
		return Price;
	}

	public String getDetail()
	{
		return Detail;
	}

	public java.awt.Image getImage()
	{
		return Image;
	}
}
