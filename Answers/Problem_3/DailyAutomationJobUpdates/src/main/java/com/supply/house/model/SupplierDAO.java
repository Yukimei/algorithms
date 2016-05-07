package com.supply.house.model;

import com.supply.house.databean.SupplierBean;

/**
 * @author YingXue Mei
 * 
 *         This is used to connect to supplier table in the database
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
public class SupplierDAO {
	public SupplierDAO(String tableName, ConnectionPool pool) throws DAOException {
	}

	public SupplierBean read(String supplierName) throws RollbackException {
	}

	public SupplierBean[] getAllSuppliers() throws RollbackException {
	}

	public SupplierBean getSupplierById(String supplierId) throws RollbackException {
	}

	public int createSupplier(String supplierId, String supplierName) throws RollbackException {
	}

	public void createAutoIncrement(SupplierBean supplier) throws RollbackException {
	}

	public boolean deleteSupplier(String supplierId) throws RollbackException {

	}

}
