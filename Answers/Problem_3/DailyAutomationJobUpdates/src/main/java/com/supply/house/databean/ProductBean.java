package com.supply.house.databean;

/**
 * 
 * @author YingXue Mei
 * 
 *         This represent one row/record in the product table, field is
 *         product_id , product_name, product_price
 * 
 *         other fields can be add later if necessary
 * 
 */
public class ProductBean {
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	private String productId;
	private String productName;
	private double productPrice;
}
