package com.supply.house.model;

import com.supply.house.databean.ProductBean;
import com.supply.house.databean.SupplierBean;

/**
 * @author YingXue Mei
 * 
 *         This is used to connect to product table in the database
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
public class ProductDAO {
	public ProductDAO(String tableName, ConnectionPool pool) throws DAOException {
	}

	public ProductBean read(String productName) throws RollbackException {
	}

	public ProductBean[] getAllProducts() throws RollbackException {
	}

	public ProductBean getProductById(String productId) throws RollbackException {
	}

	public int createProduct(String productId, String productName) RollbackException{
	}

	public void createAutoIncrement(ProductBean product) throws RollbackException {
	}

	public boolean deleteProduct(String productId) RollbackException{

	}

	public boolean updateProductPrice(String productId, double productPrice) RollbackException{

	}

	public boolean updateProductName(String productId, double productName) RollbackException{

	}
}
