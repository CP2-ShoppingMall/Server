package kimit.server;

import javax.swing.*;
import java.io.Serial;
import java.io.Serializable;
import java.text.DecimalFormat;

public class Product implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	private final String Title;
	private final int Price;
	private final String Detail;
	private final ImageIcon Image;

	public Product(String title, int price, String detail, ImageIcon image)
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

	public String getPriceText()
	{
		return new DecimalFormat("###,###").format(Price);
	}

	public String getDetail()
	{
		return Detail;
	}

	public ImageIcon getImage()
	{
		return Image;
	}
}
