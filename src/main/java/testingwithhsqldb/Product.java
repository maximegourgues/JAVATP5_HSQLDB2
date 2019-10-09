/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testingwithhsqldb;

/**
 *
 * @author pedago
 */
public class Product {
	private int productId;
	private String name;
	private float price;

	public Product(int productId, String name, float price) {
		this.productId = productId;
		this.name = name;
		this.price = price;
	}

	/**
	 * Get the value of productId
	 *
	 * @return the value of productId
	 */
	public int getProductId() {
		return productId;
	}

	/**
	 * Get the value of name
	 *
	 * @return the value of name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the value of price
	 *
	 * @return the value of price
	 */
	public float getPrice() {
		return price;
	}
}
