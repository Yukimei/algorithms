package com.supply.house.databean;

/**
 * @author YingXue Mei
 * 
 *         This represent one row/record in the supplier table, field is
 *         product_id , supplier_name
 * 
 *         other fields can be add later if necessary
 * 
 */
public class SupplierBean {
	private String supplierId;
	private String supplierName;

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
}
