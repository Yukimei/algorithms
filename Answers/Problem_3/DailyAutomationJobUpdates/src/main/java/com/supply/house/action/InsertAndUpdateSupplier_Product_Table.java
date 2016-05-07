package com.supply.house.action;

import com.supply.house.model.ProductDAO;
import com.supply.house.model.SupplierDAO;

/**
 * 
 * @author YingXue Mei
 * 
 *         This class is used for automated job to update product_supplier table
 * 
 *         Assumptions:
 * 
 *         1. the job need to innit first to connect to the database and three
 *         tables (via DAOs)
 * 
 *         2. when read from directory, it would decide the format of the file and
 *         call the correspond method to handle the file
 *         
 *         3. If the supplierId/productId is not exist, it would roll back 
 *         (does not update the record and output the not updated line to a file)
 *         
 *         4. need to import jar or add dependencies if necessary
 *     
 */

public class InsertAndUpdateSupplier_Product_Table {
	public void init() {
	}

	public void readfiles(String directoryPath) {
	}

	private boolean updateFromCSV() {
	}

	private boolean updateFromExcel() {
	}

	private boolean updateFromtxt() {
	}
	// update the data base with new record
	private boolean update(String supplierId, String productId, int quantity) {
	}
	
	private boolean checkSupplierIdExist(String supplierId, SupplierDAO supplierDAO) {
	}

	private boolean checkProductExist(String productId, ProductDAO productDAO) {
	}
	// check whether it is txt, csv or xlxs/xls,  otherwise it would continue to other job
	private boolean getfiletype() {
	}
}