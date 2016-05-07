package com.supply.house.model;

import com.supply.house.databean.ProductBean;
import com.supply.house.databean.SupplierBean;

/**
 * @author YingXue Mei
 * 
 *         This is used to connect to product_supplier table in the database
 * 
 *         assumption : 
 *         1. We use relational database here 
 *         
 *         2. it can provide
 *         methods to create, delete, update, search
 *         
 *         3. need to import jar or add dependencies if necessary
 * 
 */
public class ProductSupplierDAO {
	public ProductSupplierDAO(String tableName, ConnectionPool pool) throws DAOException {
	}

	public ProductSupplierBean read(String productId, String supplierId) throws RollbackException {
	}

	public ProductSupplierBean[] getAllRecords() throws RollbackException {
	}

	public ProductBean[] getProductsById(String productId) throws RollbackException {
	}

	public SupplierBean[] getSuppliersById(String supplierId) throws RollbackException {
	}

	public int createRecord(String productId, String supplierId, int quantity) RollbackException{
	}

	public boolean deleteRecord(String productId, String supplierId, ) RollbackException{

	}

	public boolean updateRecordQuantity(String productId, String supplierId, int quantity) RollbackException{

	}
}
