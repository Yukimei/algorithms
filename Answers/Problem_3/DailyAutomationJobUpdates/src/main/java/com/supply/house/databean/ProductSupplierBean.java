package com.supply.house.databean;

/**
 * @author YingXue Mei
 * 
 *         This represent one row/record in the product_supplier table, field is
 *         product_id , supplier_id, quantity
 * 
 *         other fields can be add later if necessary
 * 
 */
public class ProductSupplierBean {
	private String supplierId;
	private String productId;
	private int quantity;

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
