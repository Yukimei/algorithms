DROP DATABASE IF EXISTS supplyhouse;
CREATE DATABASE  supplyhouse;
USE suuplyhouse;

CREATE TABLE IF NOT EXISTS supplyhouse.product (
  productId       VARCHAR(20)      NOT NULL PRIMARY KEY,
  ProductName     VARCHAR(100)      NOT NULL,
  ProductPrice    BIGINT           NOT NULL,
  UNIQUE KEY userNameUniqueKey (`productId`)
);
CREATE TABLE IF NOT EXISTS supplyhouse.supplier (
  supplierId       VARCHAR(20)      NOT NULL PRIMARY KEY,
  supplierName     VARCHAR(100)      NOT NULL,
  UNIQUE KEY userNameUniqueKey (`supplierId`)
);

CREATE TABLE IF NOT EXISTS supplyhouse.product_supplier (
  productId       VARCHAR(20)      NOT NULL PRIMARY KEY,
  supplierId      VARCHAR(20)      NOT NULL PRIMARY KEY,
  quantity        INT           NOT NULL,
  UNIQUE KEY userNameUniqueKey (`productId`,`supplierId`)
);
