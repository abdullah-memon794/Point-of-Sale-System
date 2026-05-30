CREATE DATABASE `pos`;
USE `pos`;

-- --------------------------------------------------------
-- 2. CREATE TABLES WITH UPDATED STRUCTURES
-- --------------------------------------------------------

CREATE TABLE `customer` (
  `cid` int(11) NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(50) NOT NULL,
  `Tp_Number` varchar(20) NOT NULL,
  `billing_address` varchar(255) DEFAULT NULL,
  `shipping_address` varchar(255) DEFAULT NULL,
  `bank` varchar(50) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `person_name` varchar(50) DEFAULT NULL,
  `contact_person` varchar(50) DEFAULT NULL,
  `person_tp` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `online` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`cid`)
);

CREATE TABLE `supplier` (
  `sid` int(11) NOT NULL AUTO_INCREMENT,
  `supplier_Name` varchar(50) NOT NULL,
  `Tp_Number` varchar(20) NOT NULL,
  `billing_address` varchar(255) DEFAULT NULL,
  `shipping_address` varchar(255) DEFAULT NULL,
  `bank` varchar(50) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `person_name` varchar(50) DEFAULT NULL,
  `contact_person` varchar(50) DEFAULT NULL,
  `person_tp` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `online` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`sid`)
);

CREATE TABLE `employee` (
  `eid` int(11) NOT NULL AUTO_INCREMENT,
  `Employee_Name` varchar(50) NOT NULL,
  `Tp_Number` varchar(20) NOT NULL,
  `main_address` varchar(255) DEFAULT NULL,
  `temp_address` varchar(255) DEFAULT NULL,
  `bank` varchar(50) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`eid`)
);

CREATE TABLE `product` (
  `pid` int(11) NOT NULL AUTO_INCREMENT,
  `Product_Name` varchar(50) NOT NULL,
  `Bar_code` varchar(50) NOT NULL,
  `Price` varchar(20) NOT NULL,
  `Qty` varchar(20) NOT NULL,
  `Sid` int(11) NOT NULL,
  PRIMARY KEY (`pid`)
);

CREATE TABLE `cart` (
  `cartid` int(11) NOT NULL AUTO_INCREMENT,
  `INID` int(11) NOT NULL,
  `Product_Name` varchar(50) NOT NULL,
  `Bar_code` varchar(50) NOT NULL,
  `qty` varchar(20) NOT NULL,
  `Unit_Price` varchar(20) NOT NULL,
  `Total_Price` varchar(20) NOT NULL,
  PRIMARY KEY (`cartid`)
);

CREATE TABLE `sales` (
  `saleid` int(11) NOT NULL AUTO_INCREMENT,
  `INID` int(11) NOT NULL,
  `Cid` int(11) NOT NULL,
  `Customer_Name` varchar(50) NOT NULL,
  `Total_Qty` varchar(20) NOT NULL,
  `Total_Bill` varchar(20) NOT NULL,
  `Status` varchar(20) NOT NULL,
  `Balance` varchar(20) NOT NULL,
  PRIMARY KEY (`saleid`)
);

CREATE TABLE `extra` (
  `exid` int(11) NOT NULL AUTO_INCREMENT,
  `val` varchar(20) NOT NULL,
  PRIMARY KEY (`exid`)
);

-- --------------------------------------------------------
-- 3. INSERT ORIGINAL DATA INTO NEW TABLES
-- --------------------------------------------------------

-- Insert Cart Data
INSERT INTO `cart` (`cartid`, `INID`, `Product_Name`, `Bar_code`, `qty`, `Unit_Price`, `Total_Price`) VALUES
(1, 1, 'pen', '100200', '3', '10', '30.0'),
(2, 1, 'pen', '100200', '3', '10', '30.0'),
(3, 1, 'box', '100500', '3', '50', '150.0'),
(4, 1, 'box', '100500', '3', '50', '150.0'),
(5, 1, 'pen', '100200', '3', '10', '30.0'),
(6, 1, 'mini book ', '100100', '3', '200', '600.0'),
(7, 1, 'mini book ', '100100', '3', '200', '600.0'),
(8, 1, 'pen', '100200', '5', '10', '50.0'),
(9, 1, 'pen', '100200', '5', '10', '50.0'),
(10, 1, 'pen', '100200', '5', '10', '50.0'),
(11, 1, 'pen', '100200', '5', '10', '50.0'),
(12, 1, 'pen', '100200', '5', '10', '50.0'),
(13, 1, 'box', '100500', '5', '50', '250.0'),
(14, 1, 'box', '100500', '5', '50', '250.0'),
(15, 1, 'box', '100500', '5', '50', '250.0'),
(16, 1, 'box', '100500', '5', '50', '250.0'),
(17, 1, 'pen', '100200', '3', '10', '30.0'),
(18, 1, 'pen', '100200', '3', '10', '30.0'),
(19, 1, 'pen', '100200', '3', '10', '30.0'),
(20, 1, 'box', '100500', '3', '50', '150.0'),
(21, 1, 'box', '100500', '3', '50', '150.0'),
(22, 1, 'box', '100500', '3', '50', '150.0'),
(23, 1, 'pen', '100200', '2', '10', '20.0'),
(24, 1, 'pen', '100200', '2', '10', '20.0'),
(25, 1, 'mini book ', '100100', '2', '200', '400.0'),
(26, 1, 'mini book ', '100100', '2', '200', '400.0'),
(27, 1, 'mini book ', '100100', '2', '200', '400.0'),
(28, 1, 'mini book ', '100100', '2', '200', '400.0'),
(29, 2, 'pen', '100200', '45', '10', '450.0'),
(30, 2, 'pen', '100200', '45', '10', '450.0'),
(31, 2, 'pen', '100200', '45', '10', '450.0'),
(32, 3, 'pen', '100200', '2', '10', '20.0'),
(33, 3, 'pen', '100200', '2', '10', '20.0'),
(34, 3, 'pen', '100200', '2', '10', '20.0'),
(35, 4, 'box', '100500', '4', '50', '200.0'),
(36, 5, 'pen', '100200', '3', '10', '30.0'),
(37, 5, 'pen', '100200', '3', '10', '30.0'),
(38, 5, 'pen', '100200', '3', '10', '30.0'),
(39, 5, 'mini book ', '100100', '3', '200', '600.0'),
(40, 5, 'mini book ', '100100', '3', '200', '600.0'),
(41, 5, 'mini book ', '100100', '3', '200', '600.0'),
(42, 6, 'pen', '100200', '4', '10', '40.0'),
(43, 6, 'pen', '100200', '4', '10', '40.0'),
(44, 6, 'pen', '100200', '4', '10', '40.0'),
(45, 7, 'pen', '100200', '3', '10', '30.0'),
(46, 7, 'pen', '100200', '3', '10', '30.0'),
(47, 7, 'box', '100500', '3', '50', '150.0'),
(48, 7, 'box', '100500', '3', '50', '150.0'),
(49, 7, 'mini book ', '100100', '3', '200', '600.0'),
(50, 7, 'mini book ', '100100', '3', '200', '600.0'),
(51, 8, 'pen', '100200', '3', '10', '30.0'),
(52, 8, 'mini book ', '100100', '3', '200', '600.0'),
(53, 8, 'box', '100500', '5', '50', '250.0'),
(54, 8, 'mini book ', '100100', '2', '200', '400.0'),
(55, 9, 'mini book ', '100100', '3', '200', '600.0'),
(56, 9, 'pen', '100200', '4', '10', '40.0'),
(57, 9, 'box', '100500', '8', '50', '400.0'),
(58, 9, 'Mouse', '12354', '9', '13', '117.0'),
(59, 9, 'keyboard', '123546', '4', '18', '72.0'),
(60, 9, 'keyboard', '123546', '4', '18', '72.0'),
(61, 9, 'keyboard', '123546', '4', '18', '72.0'),
(62, 9, 'keyboard', '123546', '4', '18', '72.0'),
(63, 9, 'keyboard', '123546', '4', '18', '72.0'),
(64, 9, 'keyboard', '123546', '4', '18', '72.0'),
(65, 9, 'keyboard', '123546', '4', '18', '72.0');

-- 1. Insert Customers (Fully populated, no NULLs)
INSERT INTO `customer` (`cid`, `customer_name`, `Tp_Number`, `billing_address`, `shipping_address`, `bank`, `city`, `person_name`, `contact_person`, `person_tp`, `email`, `online`) VALUES
(1, 'Ali Hassan', '0300-1112233', 'House 1, Street 2, Qasimabad', 'House 1, Street 2, Qasimabad', 'HBL', 'Hyderabad', 'Ali', 'Self', '0300-1112233', 'ali@email.com', 'ali_online'),
(2, 'Bilal Qureshi', '0311-2223344', 'Flat 4, Phase 2, DHA', 'Flat 4, Phase 2, DHA', 'Meezan Bank', 'Karachi', 'Bilal', 'Self', '0311-2223344', 'bilal@email.com', 'bilal_q'),
(3, 'Kamran Javed', '0322-3334455', 'Shop 12, Main Bazar', 'Shop 12, Main Bazar', 'UBL', 'Sukkur', 'Kamran', 'Owner', '0322-3334455', 'kamran@email.com', 'kamran_j'),
(6, 'Salman Shah', '0333-4445566', 'Villa 9, Clifton', 'Villa 9, Clifton', 'Standard Chartered', 'Karachi', 'Salman', 'Self', '0333-4445566', 'salman@email.com', 'salman_s'),
(8, 'Jamil Ahmed', '0344-5556677', 'Office 4, Blue Area', 'Office 4, Blue Area', 'MCB', 'Islamabad', 'Jamil', 'Manager', '0344-5556677', 'jamil@email.com', 'jamil_a'),
(9, 'Samiullah', '0355-6667788', 'House 99, Latifabad', 'House 99, Latifabad', 'Allied Bank', 'Hyderabad', 'Samiullah', 'Self', '0355-6667788', 'sami@email.com', 'sami_ullah');

-- 2. Insert Suppliers (Fully populated, no NULLs)
INSERT INTO `supplier` (`sid`, `supplier_Name`, `Tp_Number`, `billing_address`, `shipping_address`, `bank`, `city`, `person_name`, `contact_person`, `person_tp`, `email`, `online`) VALUES
(1, 'Tariq Traders', '021-3456789', 'Saddar Market', 'Saddar Market', 'HBL', 'Karachi', 'Tariq', 'Owner', '0300-9998877', 'tariq@traders.pk', 'tariq_trade'),
(2, 'Mehmood Enterprises', '042-3567890', 'Shah Alam Market', 'Shah Alam Market', 'UBL', 'Lahore', 'Mehmood', 'Director', '0321-8887766', 'info@mehmood.pk', 'mehmood_ent'),
(3, 'Akbar Suppliers', '051-2345678', 'G-9 Markaz', 'G-9 Markaz', 'Meezan Bank', 'Islamabad', 'Akbar', 'Sales Head', '0333-7776655', 'sales@akbar.pk', 'akbar_sup'),
(4, 'Babar Stationers', '071-5623456', 'Minara Road', 'Minara Road', 'MCB', 'Sukkur', 'Babar', 'Manager', '0312-6665544', 'contact@babar.pk', 'babar_stat');

-- 3. Insert Employees
INSERT INTO `employee` (`eid`, `Employee_Name`, `Tp_Number`, `main_address`, `temp_address`, `bank`, `city`) VALUES
(1, 'Ahmed Raza', '0300-1234567', '123 Demo Street', '456 Test Ave', 'Standard Chartered', 'Sukkur'),
(2, 'Fatima Ali', '0333-7654321', 'Station Road', 'Station Road', 'HBL', 'Sukkur'),
(3, 'Zainab Tariq', '0345-9876543', 'Near Madarsa Darul Huda', 'Therhi', 'Meezan Bank', 'Khairpur'),
(4, 'Usman Khan', '0312-3456789', 'Mall Road', 'Mall Road', 'UBL', 'Khairpur');

-- 4. Insert Products
INSERT INTO `product` (`pid`, `Product_Name`, `Bar_code`, `Price`, `Qty`, `Sid`) VALUES
(1, 'Mini Book', '100100', '200', '10', 2),
(2, 'Pen', '100200', '10', '8', 1),
(3, 'Box', '100500', '50', '12', 3),
(4, 'Mouse', '12354', '13', '3', 2),
(5, 'Keyboard', '123546', '18', '30', 3);

-- 5. Insert Extra (Next Invoice Tracker)
INSERT INTO `extra` (`exid`, `val`) VALUES
(1, '9');

-- 6. Insert Sales (Customer names updated to match the new Pakistani names from the Customer table)
INSERT INTO `sales` (`saleid`, `INID`, `Cid`, `Customer_Name`, `Total_Qty`, `Total_Bill`, `Status`, `Balance`) VALUES
(1, 1, 3, 'Kamran Javed', '25.0', '250.0', 'UnPaid', '-1050.0'),
(2, 1, 6, 'Salman Shah', '9.0', '30.0', 'Partial', '-40.0'),
(4, 1, 8, 'Jamil Ahmed', '8.0', '400.0', 'Paid', '0.0'),
(5, 1, 2, 'Bilal Qureshi', '4.0', '400.0', 'Paid', '0.0'),
(6, 2, 6, 'Salman Shah', '135.0', '450.0', 'Paid', '0.0'),
(7, 3, 2, 'Bilal Qureshi', '6.0', '20.0', 'Partial', '-10.0'),
(8, 4, 3, 'Kamran Javed', '4.0', '200.0', 'UnPaid', '-200.0'),
(9, 5, 2, 'Bilal Qureshi', '18.0', '600.0', 'UnPaid', '-1890.0'),
(10, 6, 6, 'Salman Shah', '12.0', '40.0', 'UnPaid', '-120.0'),
(11, 7, 3, 'Kamran Javed', '18.0', '1560.0', 'UnPaid', '-1560.0'),
(12, 8, 3, 'Kamran Javed', '13.0', '1280.0', 'Partial', '-280.0'),
(13, 9, 8, 'Jamil Ahmed', '52.0', '1661.0', 'Partial', '-1611.0');


COMMIT;
	