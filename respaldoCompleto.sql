-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Win64 (AMD64)
--
-- Host: 192.168.56.101    Database: dulceria
-- ------------------------------------------------------
-- Server version	10.11.13-MariaDB-0ubuntu0.24.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Categorias`
--

DROP TABLE IF EXISTS `Categorias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Categorias` (
  `id_categoria` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_categoria` varchar(150) NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_categoria`),
  UNIQUE KEY `nombre_categoria` (`nombre_categoria`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Categorias`
--

LOCK TABLES `Categorias` WRITE;
/*!40000 ALTER TABLE `Categorias` DISABLE KEYS */;
INSERT INTO `Categorias` VALUES (1,'Bombones',1),(2,'Botanas',1),(3,'Chocolates',1),(4,'Caramelos',1),(5,'Caramelos suaves',1),(6,'Gomitas',1),(7,'Chicles',1),(8,'Chiclosos',1),(9,'Dulces Ácidos',1),(10,'Dulces Tipicos',1),(11,'Dulces picosos',1),(12,'Dulces sin azucar',1),(13,'Paletas',1),(14,'Pulpas',1),(15,'salsas',0),(16,'Kosher',1),(17,'Halloween',0);
/*!40000 ALTER TABLE `Categorias` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`vic`@`%`*/ /*!50003 TRIGGER trg_categoria_desactivar_productos
AFTER UPDATE ON Categorias
FOR EACH ROW
BEGIN
    
    
    
    IF OLD.activo = 1 AND NEW.activo = 0 THEN
        
        
        UPDATE Productos
        SET activo = 0
        WHERE id_categoria = NEW.id_categoria
          AND activo = 1;  
        
        
        
        INSERT INTO Stock_Movimientos (id_producto, cantidad, tipo_movimiento, nota)
        SELECT 
            id_producto, 
            0, 
            'AJUSTE', 
            CONCAT('?? Desactivado autom�ticamente - Categor�a [', NEW.nombre_categoria, '] inactiva')
        FROM Productos
        WHERE id_categoria = NEW.id_categoria
          AND activo = 0;  
        
    END IF;
    
    
    
    
    
    
    
    
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `Detalle_Ventas`
--

DROP TABLE IF EXISTS `Detalle_Ventas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Detalle_Ventas` (
  `id_detalle_venta` int(11) NOT NULL AUTO_INCREMENT,
  `id_venta` int(11) NOT NULL,
  `id_producto` int(11) NOT NULL,
  `cantidad` decimal(12,3) NOT NULL DEFAULT 0.000,
  `precio_unitario_cobrado` decimal(12,2) NOT NULL DEFAULT 0.00,
  `importe_total` decimal(14,2) NOT NULL DEFAULT 0.00,
  PRIMARY KEY (`id_detalle_venta`),
  KEY `idx_detalleventa_venta` (`id_venta`),
  KEY `idx_detalleventa_producto` (`id_producto`),
  CONSTRAINT `fk_detalleventa_producto` FOREIGN KEY (`id_producto`) REFERENCES `Productos` (`id_producto`) ON UPDATE CASCADE,
  CONSTRAINT `fk_detalleventa_venta` FOREIGN KEY (`id_venta`) REFERENCES `Ventas` (`id_venta`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Detalle_Ventas`
--

LOCK TABLES `Detalle_Ventas` WRITE;
/*!40000 ALTER TABLE `Detalle_Ventas` DISABLE KEYS */;
INSERT INTO `Detalle_Ventas` VALUES (1,1,2,2.000,380.00,760.00),(2,1,4,2.000,360.00,720.00),(3,1,6,3.000,420.00,1260.00),(4,1,8,3.000,160.00,480.00),(5,2,86,2.000,110.00,220.00),(6,2,88,1.000,110.00,110.00),(7,2,92,1.000,200.00,200.00),(8,2,96,1.000,280.00,280.00),(9,2,99,1.000,180.00,180.00),(10,2,98,1.000,450.00,450.00),(11,3,28,1.000,150.00,150.00),(12,3,33,1.000,240.00,240.00),(13,3,40,1.000,140.00,140.00),(14,3,44,2.000,80.00,160.00),(15,4,1,1.000,380.00,380.00),(16,4,8,1.000,160.00,160.00),(17,4,11,2.000,390.00,780.00),(18,4,13,1.000,390.00,390.00),(19,5,146,1.000,180.00,180.00),(20,5,144,1.000,110.00,110.00),(21,5,145,1.000,210.00,210.00),(22,6,1,1.000,380.00,380.00),(23,6,59,1.000,240.00,240.00),(24,6,86,1.000,110.00,110.00),(25,6,92,1.000,200.00,200.00),(26,6,104,1.000,450.00,450.00),(27,7,77,3.000,90.00,270.00),(28,7,76,2.000,150.00,300.00),(29,7,72,1.000,130.00,130.00),(30,7,71,1.000,180.00,180.00),(31,7,70,1.000,150.00,150.00),(32,8,52,1.000,850.00,850.00),(33,8,53,1.000,650.00,650.00),(34,8,54,1.000,900.00,900.00),(35,8,87,1.000,110.00,110.00),(36,8,86,1.000,110.00,110.00),(37,8,89,1.000,140.00,140.00),(38,8,88,1.000,110.00,110.00),(39,9,1,1.000,380.00,380.00),(40,10,8,1.000,160.00,160.00),(41,11,46,2.000,90.00,180.00),(42,11,48,2.000,75.00,150.00),(43,11,47,1.000,150.00,150.00),(44,12,1,1.000,380.00,380.00),(45,12,32,1.000,160.00,160.00),(46,12,33,1.000,240.00,240.00),(47,12,40,1.000,140.00,140.00),(48,12,44,1.000,80.00,80.00),(49,12,43,1.000,75.00,75.00),(50,12,45,1.000,80.00,80.00),(51,12,46,1.000,90.00,90.00),(52,12,48,1.000,75.00,75.00),(53,12,47,1.000,150.00,150.00),(54,12,65,2.000,85.00,170.00),(55,12,69,2.000,500.00,1000.00);
/*!40000 ALTER TABLE `Detalle_Ventas` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`vic`@`%`*/ /*!50003 TRIGGER trg_detalleventa_before_insert
BEFORE INSERT ON Detalle_Ventas
FOR EACH ROW
BEGIN
  IF NEW.cantidad <= 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cantidad debe ser > 0';
  END IF;
  IF NEW.precio_unitario_cobrado < 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Precio unitario inv�lido';
  END IF;
  SET NEW.importe_total = ROUND(NEW.cantidad * NEW.precio_unitario_cobrado, 2);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`vic`@`%`*/ /*!50003 TRIGGER trg_detalleventa_after_insert
AFTER INSERT ON Detalle_Ventas
FOR EACH ROW
BEGIN
  UPDATE Productos
  SET stock = stock - NEW.cantidad
  WHERE id_producto = NEW.id_producto;

  INSERT INTO Stock_Movimientos (id_producto, id_venta, cantidad, tipo_movimiento, nota)
  VALUES (NEW.id_producto, NEW.id_venta, NEW.cantidad, 'SALIDA', CONCAT('Venta ID: ', NEW.id_venta));
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`vic`@`%`*/ /*!50003 TRIGGER trg_detalleventa_before_update
BEFORE UPDATE ON Detalle_Ventas
FOR EACH ROW
BEGIN
  IF NEW.cantidad <= 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cantidad debe ser > 0';
  END IF;
  IF NEW.precio_unitario_cobrado < 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Precio unitario inv�lido';
  END IF;
  SET NEW.importe_total = ROUND(NEW.cantidad * NEW.precio_unitario_cobrado, 2);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`vic`@`%`*/ /*!50003 TRIGGER trg_detalleventa_after_delete
AFTER DELETE ON Detalle_Ventas
FOR EACH ROW
BEGIN
  UPDATE Productos
  SET stock = stock + OLD.cantidad
  WHERE id_producto = OLD.id_producto;

  INSERT INTO Stock_Movimientos (id_producto, id_venta, cantidad, tipo_movimiento, nota)
  VALUES (OLD.id_producto, OLD.id_venta, OLD.cantidad, 'ENTRADA', CONCAT('Detalle eliminado Venta ID: ', OLD.id_venta));
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `Marcas`
--

DROP TABLE IF EXISTS `Marcas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Marcas` (
  `id_marca` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_marca` varchar(150) NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_marca`),
  UNIQUE KEY `nombre_marca` (`nombre_marca`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Marcas`
--

LOCK TABLES `Marcas` WRITE;
/*!40000 ALTER TABLE `Marcas` DISABLE KEYS */;
INSERT INTO `Marcas` VALUES (1,'Barcel',1),(2,'Botanas el sol',1),(3,'Botanera',1),(4,'Coronado',1),(5,'De la rosa',1),(6,'Lucky gummys',1),(7,'Ferrero',1),(8,'Mondelez',1),(9,'Hersehy',1),(10,'La corona',1),(11,'pelon pelo rico',1),(12,'Sabritas',1),(13,'Sonrics',1),(14,'Turin',1),(15,'Totis',1),(16,'Dulces Vero',1),(17,'Winis',1),(18,'Ricolino',1),(19,'Tama-roca',1),(20,'Lucas',1),(21,'Miguelito',1),(22,'Arcor',1);
/*!40000 ALTER TABLE `Marcas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Productos`
--

DROP TABLE IF EXISTS `Productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Productos` (
  `id_producto` int(11) NOT NULL AUTO_INCREMENT,
  `id_categoria` int(11) DEFAULT NULL,
  `id_marca` int(11) DEFAULT NULL,
  `nombre_producto` varchar(255) NOT NULL,
  `precio` decimal(12,2) NOT NULL DEFAULT 0.00,
  `stock` decimal(12,3) NOT NULL DEFAULT 0.000,
  `unidad_medida` varchar(50) DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_producto`),
  KEY `idx_productos_nombre` (`nombre_producto`),
  KEY `idx_productos_categoria` (`id_categoria`),
  KEY `idx_productos_marca` (`id_marca`),
  CONSTRAINT `fk_productos_categoria` FOREIGN KEY (`id_categoria`) REFERENCES `Categorias` (`id_categoria`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_productos_marca` FOREIGN KEY (`id_marca`) REFERENCES `Marcas` (`id_marca`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=147 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Productos`
--

LOCK TABLES `Productos` WRITE;
/*!40000 ALTER TABLE `Productos` DISABLE KEYS */;
INSERT INTO `Productos` VALUES (1,2,1,'Takis Fuego 20 bolsas',380.00,46.000,'CAJA',1),(2,2,1,'Takis Blue Heat 20 bolsas',380.00,43.000,'CAJA',1),(3,2,1,'Takis Salsa Brava 20 bolsas',360.00,30.000,'CAJA',1),(4,2,1,'Takis Huakamoles 20 bolsas',360.00,28.000,'CAJA',1),(5,2,1,'Chips Sal de Mar 25 bolsas',420.00,40.000,'CAJA',1),(6,2,1,'Chips Fuego 25 bolsas',420.00,37.000,'CAJA',1),(7,2,1,'Chips Jalapeño 25 bolsas',420.00,35.000,'CAJA',1),(8,2,1,'Hot Nuts Original 12pz',160.00,55.000,'PQTE',1),(9,2,1,'Hot Nuts Fuego 12pz',160.00,60.000,'PQTE',1),(10,2,1,'Runners 20 bolsas',350.00,25.000,'CAJA',1),(11,2,12,'Sabritas Sal 20 bolsas',390.00,48.000,'CAJA',1),(12,2,12,'Sabritas Adobadas 20 bolsas',390.00,50.000,'CAJA',1),(13,2,12,'Sabritas Limón 20 bolsas',390.00,44.000,'CAJA',1),(14,2,12,'Ruffles Queso 20 bolsas',410.00,40.000,'CAJA',1),(15,2,12,'Ruffles Sal 20 bolsas',410.00,40.000,'CAJA',1),(16,2,12,'Doritos Nacho 24 bolsas',450.00,55.000,'CAJA',1),(17,2,12,'Doritos Pizzerola 24 bolsas',450.00,30.000,'CAJA',1),(18,2,12,'Doritos Incógnita 24 bolsas',450.00,35.000,'CAJA',1),(19,2,12,'Doritos Flamin Hot 24 bolsas',450.00,50.000,'CAJA',1),(20,2,12,'Cheetos Torciditos 25 bolsas',380.00,45.000,'CAJA',1),(21,2,12,'Cheetos Poffs 25 bolsas',380.00,40.000,'CAJA',1),(22,2,12,'Cheetos Flamin Hot 25 bolsas',380.00,50.000,'CAJA',1),(23,2,12,'Tostitos Salsa Verde 20 bolsas',420.00,20.000,'CAJA',1),(24,2,12,'Rancheritos 25 bolsas',350.00,35.000,'CAJA',1),(25,2,12,'Churrumais 30 bolsas',300.00,40.000,'CAJA',1),(26,2,12,'Fritos Sal y Limón 25 bolsas',350.00,30.000,'CAJA',1),(27,2,15,'Totis Donitas Sal y Limón 25pz',150.00,80.000,'BOLSA',1),(28,2,15,'Totis Donitas Chile y Limón 25pz',150.00,79.000,'BOLSA',1),(29,2,15,'Totis Palitos 25pz',150.00,80.000,'BOLSA',1),(30,2,15,'Que Totis 25pz',150.00,70.000,'BOLSA',1),(31,6,18,'Panditas Clásicos 12 bolsitas',160.00,100.000,'PQTE',1),(32,6,18,'Panditas Ácidos 12 bolsitas',160.00,79.000,'PQTE',1),(33,1,18,'Bubulubu 24pz',240.00,148.000,'CAJA',1),(34,3,18,'Kranky 10 bolsas',140.00,60.000,'CAJA',1),(35,1,18,'Paleta Payaso 10pz',145.00,80.000,'CAJA',1),(36,3,18,'Chocoretas 10 bolsas',130.00,70.000,'CAJA',1),(37,9,18,'Pecositas 20 tubos',180.00,50.000,'PQTE',1),(38,6,18,'Gomilocas Dientes 1kg',190.00,40.000,'BOLSA',1),(39,6,18,'Gomilocas Pingüinos 1kg',190.00,40.000,'BOLSA',1),(40,3,18,'Pasitas 10 bolsas',140.00,28.000,'CAJA',1),(41,5,18,'Duvalin Bi-Sabor 18pz',45.00,25.000,'CAJA',1),(42,3,18,'Bocadin 50pz',85.00,20.000,'CAJA',1),(43,11,16,'Paleta Elote 40pz',75.00,29.000,'BOLSA',1),(44,13,16,'Paleta Tarrito 40pz',80.00,27.000,'BOLSA',1),(45,13,16,'Paleta Manita 40pz',80.00,29.000,'BOLSA',1),(46,4,16,'Rellerindo 65pz',90.00,22.000,'BOLSA',1),(47,6,16,'Pica Fresa Gigante 100pz',150.00,18.000,'BOLSA',1),(48,11,16,'Vero Mango 40pz',75.00,22.000,'BOLSA',1),(49,6,6,'Aros de Manzana 1kg',110.00,35.000,'BOLSA',1),(50,6,6,'Lombrices Neón 1kg',110.00,35.000,'BOLSA',1),(51,3,7,'Ferrero Rocher 3 pack (12 estuches)',520.00,50.000,'CAJA',1),(52,3,7,'Ferrero Rocher 16 pack (5 estuches)',850.00,14.000,'CAJA',1),(53,3,7,'Kinder Sorpresa 24pz',650.00,59.000,'CAJA',1),(54,3,7,'Kinder Bueno Barra 30pz',900.00,49.000,'CAJA',1),(55,3,7,'Kinder Delice 15pz',250.00,70.000,'CAJA',1),(56,3,7,'Kinder Maxi 36pz',400.00,80.000,'CAJA',1),(57,3,7,'Nutella B-ready 12pz',290.00,40.000,'CAJA',1),(58,3,9,'Hershey Barra Leche 12pz',240.00,60.000,'CAJA',1),(59,3,9,'Hershey Cookies n Creme 12pz',240.00,59.000,'CAJA',1),(60,3,9,'Hershey Almendras 12pz',260.00,50.000,'CAJA',1),(61,3,9,'Kisses Leche 12 bolsitas',500.00,30.000,'PQTE',1),(62,3,9,'Reeses Cups 2pk (24 unidades)',550.00,40.000,'CAJA',1),(63,3,14,'Conejos Turin 20pz',380.00,50.000,'CAJA',1),(64,3,14,'Tubo Baileys Turin 12pz',2200.00,15.000,'CAJA',1),(65,10,5,'Mazapán Original 30pz',85.00,48.000,'CAJA',1),(66,3,5,'Mazapán Cubierto Chocolate 16pz',120.00,100.000,'CAJA',1),(67,11,5,'Pulparindo Original 20pz',60.00,80.000,'CAJA',1),(68,11,5,'Pulparindo Sandía 20pz',60.00,60.000,'CAJA',1),(69,1,5,'Malvaviscos Bianchi 300g (20 bolsas)',500.00,38.000,'CAJA',1),(70,9,5,'Acidul 50pz',150.00,199.000,'BOLSA',1),(71,13,5,'Gummy Pop 50pz',180.00,149.000,'BOLSA',1),(72,11,20,'Lucas Muecas Chamoy 10pz',130.00,59.000,'CAJA',1),(73,11,20,'Lucas Muecas Mango 10pz',130.00,60.000,'CAJA',1),(74,15,20,'Lucas Gusano Chamoy 10pz',110.00,50.000,'CAJA',0),(75,6,20,'Lucas Salsagheti 12pz',160.00,55.000,'CAJA',1),(76,14,20,'Lucas Panzón 10pz',150.00,38.000,'CAJA',1),(77,11,20,'Lucas Polvo 12pz',90.00,77.000,'CAJA',1),(78,14,11,'Pelón Pelo Rico Original 18pz',170.00,100.000,'CAJA',1),(79,14,11,'Pelón Pelo Rico Chamoy 18pz',170.00,80.000,'CAJA',1),(80,4,11,'Pelonetes 30g 20pz',220.00,50.000,'CAJA',1),(81,11,21,'Miguelito Polvo Original 12 botes',90.00,100.000,'PQTE',1),(82,15,21,'Miguelito Líquido 10 botellas',140.00,60.000,'PQTE',0),(83,7,8,'Trident Yerbabuena 18s (12 packs)',280.00,50.000,'CAJA',1),(84,7,8,'Trident Fresa 18s (12 packs)',280.00,50.000,'CAJA',1),(85,7,8,'Clorets 12s (20 packs)',340.00,40.000,'CAJA',1),(86,4,8,'Halls Miel 12 tubos',110.00,96.000,'CAJA',1),(87,4,8,'Halls Cereza 12 tubos',110.00,99.000,'CAJA',1),(88,4,8,'Halls Negras 12 tubos',110.00,78.000,'CAJA',1),(89,7,8,'Bubbaloo Fresa 50pz',140.00,299.000,'BOLSA',1),(90,7,8,'Bubbaloo Tutti Frutti 50pz',140.00,300.000,'BOLSA',1),(91,5,17,'Winis Original Paquete 20pz',450.00,40.000,'BOLSA',1),(92,5,17,'Winis Uva 50pz',200.00,98.000,'BOLSA',1),(93,5,17,'Winis Tubo 12pz',130.00,60.000,'CAJA',1),(94,15,3,'Salsa Botanera Clásica 6 botellas',100.00,40.000,'PQTE',0),(95,2,2,'Pepitas Tostadas 20 bolsitas',280.00,50.000,'PQTE',1),(96,2,2,'Cacahuate Salado 20 bolsitas',280.00,49.000,'PQTE',1),(97,10,4,'Paleta de Cajeta 40pz',250.00,60.000,'BOLSA',1),(98,10,4,'Obleas con Cajeta 20 paquetes',450.00,39.000,'CAJA',1),(99,13,10,'Paletón Corona 20pz',180.00,59.000,'BOLSA',1),(100,14,19,'Pellizco Tamarindo 20pz',110.00,80.000,'CAJA',1),(101,14,19,'Tamy 20pz',95.00,80.000,'CAJA',1),(102,4,22,'Butter Toffees 1kg',140.00,30.000,'BOLSA',1),(103,3,22,'Bon o Bon 12pz',130.00,50.000,'CAJA',1),(104,13,13,'Rockaleta Bola 40pz',450.00,59.000,'BOLSA',1),(105,13,13,'Gudu Pop 50pz',220.00,100.000,'BOLSA',1),(106,5,13,'Dale Dale 1kg',110.00,20.000,'BOLSA',1),(107,8,4,'Chiclosos Coronado Cajeta 50pz',150.00,50.000,'BOLSA',1),(108,8,4,'Chiclosos Coronado Rompope 50pz',150.00,40.000,'BOLSA',1),(109,8,4,'Chiclosos Coronado Surtido 50pz',150.00,45.000,'BOLSA',1),(110,8,22,'Butter Toffees Leche 1kg',120.00,20.000,'BOLSA',1),(111,8,22,'Butter Toffees Chocolate 1kg',120.00,20.000,'BOLSA',1),(112,8,22,'Butter Toffees Café 1kg',120.00,60.000,'BOLSA',1),(113,8,13,'Sugus Tira 12pz',70.00,100.000,'CAJA',1),(114,8,13,'Sugus Bolsa Surtida 10pz',280.00,40.000,'PQTE',1),(115,8,17,'Winis Max Gigante 12pz',160.00,80.000,'CAJA',1),(116,8,10,'Chicloso La Corona 50pz',120.00,30.000,'BOLSA',1),(117,12,14,'Turin Zero Leche 10 tubos',1200.00,25.000,'CAJA',1),(118,12,14,'Turin Zero Amargo 18pz',380.00,50.000,'CAJA',1),(119,12,14,'Turin Zero 70% Cacao 10pz',800.00,20.000,'CAJA',1),(120,12,5,'Mazapán Sin Azúcar 18pz',95.00,30.000,'CAJA',1),(121,12,5,'Chocolate De la Rosa Sin Azúcar 12pz',170.00,40.000,'CAJA',1),(122,12,9,'Hershey Zero Sugar Leche 12pz',680.00,15.000,'CAJA',1),(123,12,9,'Hershey Zero Sugar Dark 12pz',680.00,15.000,'CAJA',1),(124,12,8,'Halls Silver Mint 12pz',130.00,80.000,'CAJA',1),(125,12,8,'Trident Xtra Care 12 packs',320.00,45.000,'CAJA',1),(126,12,4,'Cajeta Coronado Sin Azúcar 6 botellas',480.00,20.000,'CAJA',1),(127,16,7,'Ferrero Rocher Diamante 4pz',950.00,15.000,'CAJA',1),(128,16,7,'Ferrero Rocher 16pz (5 estuches)',850.00,20.000,'CAJA',1),(129,16,7,'Kinder Chocolate T1 20pz',220.00,100.000,'CAJA',1),(130,16,12,'Sabritas Sal 45g (20 bolsas)',340.00,60.000,'CAJA',1),(131,16,12,'Sabritas Sal 170g (15 bolsas)',680.00,40.000,'CAJA',1),(132,16,12,'Ruffles Sal 185g (15 bolsas)',680.00,40.000,'CAJA',1),(133,16,1,'Chips Sal de Mar 170g (15 bolsas)',650.00,35.000,'CAJA',1),(134,16,9,'Hershey Kisses Leche 900g',180.00,10.000,'BOLSA',1),(135,16,9,'Hershey Kisses Almendra 900g',190.00,25.000,'BOLSA',1),(136,16,9,'Hershey Barra Leche Clásica 12pz',250.00,50.000,'CAJA',1),(137,17,18,'Paquete Miedo Ricolino 1kg',190.00,10.000,'BOLSA',0),(138,17,18,'Panditas Zombie 20 bolsas',380.00,15.000,'PQTE',0),(139,17,18,'Gomilocas Monstruos 1kg',180.00,12.000,'BOLSA',0),(140,17,16,'Paleta Drácula Vero 40pz',90.00,10.000,'BOLSA',0),(141,17,16,'Paleta Dedo Vampiro Vero 40pz',85.00,10.000,'BOLSA',0),(142,17,16,'Vero Dientes de Vampiro 50pz',120.00,25.000,'BOLSA',0),(143,17,13,'Calaverita Sonrics 12pz',950.00,0.000,'CAJA',0),(144,17,13,'Paquete Dulces Sonrics Halloween 1kg',110.00,5.000,'BOLSA',0),(145,17,9,'Hershey Kisses Fantasmas 900g',210.00,7.000,'BOLSA',0),(146,17,17,'Winis Halloween 20 bolsitas',180.00,11.000,'PQTE',0);
/*!40000 ALTER TABLE `Productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Proveedores`
--

DROP TABLE IF EXISTS `Proveedores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Proveedores` (
  `id_proveedor` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_proveedor` varchar(255) NOT NULL,
  `telefono` varchar(50) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_proveedor`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Proveedores`
--

LOCK TABLES `Proveedores` WRITE;
/*!40000 ALTER TABLE `Proveedores` DISABLE KEYS */;
INSERT INTO `Proveedores` VALUES (1,'Comercializadora PepsiCo México','5548291038','ventas.mayoreo@pepsico.com',1),(2,'Grupo Bimbo S.A.B. de C.V.','5517829304','nuevos.clientes@grupobimbo.com',1),(3,'Mazapán de la Rosa S.A. de C.V.','3392018475','ventas@dulcesdelarosa.com.mx',1),(4,'Ferrero de México S.A. de C.V.','3310928374','atencion.mx@ferrero.com',1),(5,'Mondelez México','5589234102','pedidos.latam@mdlz.com',1),(6,'Fábrica de Dulces Vero','3381920384','contacto@dulcesvero.com.mx',1),(7,'Hershey México','3337281902','ventas.occidente@hersheys.com',1),(8,'Totis S.A. de C.V.','5529384710','distribucion@totis.com.mx',1),(9,'Dulces Sonrics','5567382910','contacto@sonrics.com.mx',1),(10,'Distribuidora El Escorpión','5557291029','pedidos@elescorpion.mx',1),(11,'Dulcería Chavita','3336172299','mayoreo@dulceriachavita.com',1),(12,'Grupo Zorro Abarrotero','5592837461','atencion.socios@zorro.com.mx',1),(13,'Dulces La Merced','5554281932','ventas@dulcesmerced.com',1),(14,'Comercializadora El Bajío','4421928301','ventas@dulcesbajio.com',1),(15,'Importadora Sweet World','8182930481','contacto@importsweets.mx',1);
/*!40000 ALTER TABLE `Proveedores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Roles`
--

DROP TABLE IF EXISTS `Roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Roles` (
  `id_rol` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_rol` varchar(100) NOT NULL,
  PRIMARY KEY (`id_rol`),
  UNIQUE KEY `nombre_rol` (`nombre_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Roles`
--

LOCK TABLES `Roles` WRITE;
/*!40000 ALTER TABLE `Roles` DISABLE KEYS */;
INSERT INTO `Roles` VALUES (1,'Administrador'),(2,'Cajero');
/*!40000 ALTER TABLE `Roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Stock_Movimientos`
--

DROP TABLE IF EXISTS `Stock_Movimientos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Stock_Movimientos` (
  `id_movimiento` int(11) NOT NULL AUTO_INCREMENT,
  `id_producto` int(11) NOT NULL,
  `id_venta` int(11) DEFAULT NULL,
  `cantidad` decimal(12,3) NOT NULL,
  `tipo_movimiento` enum('SALIDA','ENTRADA','AJUSTE') NOT NULL,
  `fecha_movimiento` timestamp NOT NULL DEFAULT current_timestamp(),
  `nota` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_movimiento`),
  KEY `fk_stockmov_producto` (`id_producto`),
  CONSTRAINT `fk_stockmov_producto` FOREIGN KEY (`id_producto`) REFERENCES `Productos` (`id_producto`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Stock_Movimientos`
--

LOCK TABLES `Stock_Movimientos` WRITE;
/*!40000 ALTER TABLE `Stock_Movimientos` DISABLE KEYS */;
INSERT INTO `Stock_Movimientos` VALUES (1,2,1,2.000,'SALIDA','2025-12-07 03:05:54','Venta ID: 1'),(2,4,1,2.000,'SALIDA','2025-12-07 03:05:54','Venta ID: 1'),(3,6,1,3.000,'SALIDA','2025-12-07 03:05:54','Venta ID: 1'),(4,8,1,3.000,'SALIDA','2025-12-07 03:05:54','Venta ID: 1'),(5,86,2,2.000,'SALIDA','2025-12-07 03:19:31','Venta ID: 2'),(6,88,2,1.000,'SALIDA','2025-12-07 03:19:31','Venta ID: 2'),(7,92,2,1.000,'SALIDA','2025-12-07 03:19:31','Venta ID: 2'),(8,96,2,1.000,'SALIDA','2025-12-07 03:19:31','Venta ID: 2'),(9,99,2,1.000,'SALIDA','2025-12-07 03:19:31','Venta ID: 2'),(10,98,2,1.000,'SALIDA','2025-12-07 03:19:31','Venta ID: 2'),(11,28,3,1.000,'SALIDA','2025-12-07 22:01:41','Venta ID: 3'),(12,33,3,1.000,'SALIDA','2025-12-07 22:01:41','Venta ID: 3'),(13,40,3,1.000,'SALIDA','2025-12-07 22:01:41','Venta ID: 3'),(14,44,3,2.000,'SALIDA','2025-12-07 22:01:41','Venta ID: 3'),(15,1,4,1.000,'SALIDA','2025-12-07 22:34:58','Venta ID: 4'),(16,8,4,1.000,'SALIDA','2025-12-07 22:34:58','Venta ID: 4'),(17,11,4,2.000,'SALIDA','2025-12-07 22:34:58','Venta ID: 4'),(18,13,4,1.000,'SALIDA','2025-12-07 22:34:58','Venta ID: 4'),(19,146,5,1.000,'SALIDA','2025-12-07 23:46:11','Venta ID: 5'),(20,144,5,1.000,'SALIDA','2025-12-07 23:46:11','Venta ID: 5'),(21,145,5,1.000,'SALIDA','2025-12-07 23:46:11','Venta ID: 5'),(22,1,6,1.000,'SALIDA','2025-12-07 23:48:05','Venta ID: 6'),(23,59,6,1.000,'SALIDA','2025-12-07 23:48:05','Venta ID: 6'),(24,86,6,1.000,'SALIDA','2025-12-07 23:48:05','Venta ID: 6'),(25,92,6,1.000,'SALIDA','2025-12-07 23:48:05','Venta ID: 6'),(26,104,6,1.000,'SALIDA','2025-12-07 23:48:05','Venta ID: 6'),(27,77,7,3.000,'SALIDA','2025-12-08 00:01:41','Venta ID: 7'),(28,76,7,2.000,'SALIDA','2025-12-08 00:01:41','Venta ID: 7'),(29,72,7,1.000,'SALIDA','2025-12-08 00:01:41','Venta ID: 7'),(30,71,7,1.000,'SALIDA','2025-12-08 00:01:41','Venta ID: 7'),(31,70,7,1.000,'SALIDA','2025-12-08 00:01:41','Venta ID: 7'),(32,52,8,1.000,'SALIDA','2025-12-08 00:27:50','Venta ID: 8'),(33,53,8,1.000,'SALIDA','2025-12-08 00:27:50','Venta ID: 8'),(34,54,8,1.000,'SALIDA','2025-12-08 00:27:50','Venta ID: 8'),(35,87,8,1.000,'SALIDA','2025-12-08 00:27:50','Venta ID: 8'),(36,86,8,1.000,'SALIDA','2025-12-08 00:27:50','Venta ID: 8'),(37,89,8,1.000,'SALIDA','2025-12-08 00:27:50','Venta ID: 8'),(38,88,8,1.000,'SALIDA','2025-12-08 00:27:50','Venta ID: 8'),(39,1,9,1.000,'SALIDA','2025-12-08 00:42:00','Venta ID: 9'),(40,8,10,1.000,'SALIDA','2025-12-08 00:44:55','Venta ID: 10'),(41,137,NULL,0.000,'AJUSTE','2025-12-08 00:52:27','?? Desactivado automáticamente - Categoría [Halloween] inactiva'),(42,138,NULL,0.000,'AJUSTE','2025-12-08 00:52:27','?? Desactivado automáticamente - Categoría [Halloween] inactiva'),(43,139,NULL,0.000,'AJUSTE','2025-12-08 00:52:27','?? Desactivado automáticamente - Categoría [Halloween] inactiva'),(44,140,NULL,0.000,'AJUSTE','2025-12-08 00:52:27','?? Desactivado automáticamente - Categoría [Halloween] inactiva'),(45,141,NULL,0.000,'AJUSTE','2025-12-08 00:52:27','?? Desactivado automáticamente - Categoría [Halloween] inactiva'),(46,142,NULL,0.000,'AJUSTE','2025-12-08 00:52:27','?? Desactivado automáticamente - Categoría [Halloween] inactiva'),(47,143,NULL,0.000,'AJUSTE','2025-12-08 00:52:27','?? Desactivado automáticamente - Categoría [Halloween] inactiva'),(48,144,NULL,0.000,'AJUSTE','2025-12-08 00:52:27','?? Desactivado automáticamente - Categoría [Halloween] inactiva'),(49,145,NULL,0.000,'AJUSTE','2025-12-08 00:52:27','?? Desactivado automáticamente - Categoría [Halloween] inactiva'),(50,146,NULL,0.000,'AJUSTE','2025-12-08 00:52:27','?? Desactivado automáticamente - Categoría [Halloween] inactiva'),(56,46,11,2.000,'SALIDA','2025-12-08 01:20:34','Venta ID: 11'),(57,48,11,2.000,'SALIDA','2025-12-08 01:20:34','Venta ID: 11'),(58,47,11,1.000,'SALIDA','2025-12-08 01:20:34','Venta ID: 11'),(59,74,NULL,0.000,'AJUSTE','2025-12-08 01:42:10','?? Desactivado automáticamente - Categoría [salsas] inactiva'),(60,82,NULL,0.000,'AJUSTE','2025-12-08 01:42:10','?? Desactivado automáticamente - Categoría [salsas] inactiva'),(61,94,NULL,0.000,'AJUSTE','2025-12-08 01:42:10','?? Desactivado automáticamente - Categoría [salsas] inactiva'),(62,1,12,1.000,'SALIDA','2025-12-08 02:01:59','Venta ID: 12'),(63,32,12,1.000,'SALIDA','2025-12-08 02:01:59','Venta ID: 12'),(64,33,12,1.000,'SALIDA','2025-12-08 02:01:59','Venta ID: 12'),(65,40,12,1.000,'SALIDA','2025-12-08 02:01:59','Venta ID: 12'),(66,44,12,1.000,'SALIDA','2025-12-08 02:01:59','Venta ID: 12'),(67,43,12,1.000,'SALIDA','2025-12-08 02:01:59','Venta ID: 12'),(68,45,12,1.000,'SALIDA','2025-12-08 02:01:59','Venta ID: 12'),(69,46,12,1.000,'SALIDA','2025-12-08 02:01:59','Venta ID: 12'),(70,48,12,1.000,'SALIDA','2025-12-08 02:01:59','Venta ID: 12'),(71,47,12,1.000,'SALIDA','2025-12-08 02:01:59','Venta ID: 12'),(72,65,12,2.000,'SALIDA','2025-12-08 02:01:59','Venta ID: 12'),(73,69,12,2.000,'SALIDA','2025-12-08 02:01:59','Venta ID: 12');
/*!40000 ALTER TABLE `Stock_Movimientos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Usuarios`
--

DROP TABLE IF EXISTS `Usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Usuarios` (
  `id_usuario` int(11) NOT NULL AUTO_INCREMENT,
  `id_rol` int(11) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nombre_completo` varchar(255) DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `username` (`username`),
  KEY `fk_usuarios_rol` (`id_rol`),
  CONSTRAINT `fk_usuarios_rol` FOREIGN KEY (`id_rol`) REFERENCES `Roles` (`id_rol`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Usuarios`
--

LOCK TABLES `Usuarios` WRITE;
/*!40000 ALTER TABLE `Usuarios` DISABLE KEYS */;
INSERT INTO `Usuarios` VALUES (1,1,'admin','$2a$10$Gg8I2a5Y0/EB5EPzYAe51.MNdtDAA8IPaKgNh89ZKJ2thpy6ZPWJy','Victor Manuel Ramirez Mendoza',1),(2,2,'Cajero1','$2a$10$4pfT1zWOVrH2qwZYj5AyTeLRnQ9niDOAHDOZBiMx1bs8uV6CcDaKq','Angel David Sanchez',1),(3,2,'JuanM','$2a$10$vUNqF3HMZ8tX.Tm2XlEagOuqHm3jiOzjHQwvgTBRmq3D4cnz3G7Om','Juan Alberto Lopez',1),(4,1,'CarlosV','$2a$10$h2BcfI/iziK8ALefYMSI.O7Q3sslU0ApomFmyosSx0GtrxYv75dD.','Carlos Emilio Valdez Valencia',0);
/*!40000 ALTER TABLE `Usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Ventas`
--

DROP TABLE IF EXISTS `Ventas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Ventas` (
  `id_venta` int(11) NOT NULL AUTO_INCREMENT,
  `id_usuario` int(11) NOT NULL,
  `fecha_hora` timestamp NOT NULL DEFAULT current_timestamp(),
  `metodo_pago` varchar(50) DEFAULT 'Efectivo',
  `subtotal` decimal(12,2) NOT NULL DEFAULT 0.00,
  `impuestos` decimal(12,2) NOT NULL DEFAULT 0.00,
  `total` decimal(12,2) NOT NULL DEFAULT 0.00,
  PRIMARY KEY (`id_venta`),
  KEY `idx_ventas_fecha` (`fecha_hora`),
  KEY `idx_ventas_usuario` (`id_usuario`),
  CONSTRAINT `fk_ventas_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `Usuarios` (`id_usuario`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Ventas`
--

LOCK TABLES `Ventas` WRITE;
/*!40000 ALTER TABLE `Ventas` DISABLE KEYS */;
INSERT INTO `Ventas` VALUES (1,1,'2025-12-07 03:05:54','Tarjeta',3220.00,515.20,3735.20),(2,1,'2025-12-07 03:19:31','Efectivo',1440.00,230.40,1670.40),(3,1,'2025-12-07 22:01:41','Efectivo',690.00,110.40,800.40),(4,1,'2025-12-07 22:34:58','Tarjeta',1710.00,273.60,1983.60),(5,1,'2025-12-07 23:46:11','Efectivo',500.00,80.00,580.00),(6,1,'2025-12-07 23:48:05','Efectivo',1380.00,220.80,1600.80),(7,1,'2025-12-08 00:01:41','Efectivo',1030.00,164.80,1194.80),(8,1,'2025-12-08 00:27:50','Tarjeta',2870.00,459.20,3329.20),(9,1,'2025-12-08 00:42:00','Efectivo',380.00,60.80,440.80),(10,1,'2025-12-08 00:44:55','Tarjeta',160.00,25.60,185.60),(11,2,'2025-12-08 01:20:34','Efectivo',480.00,76.80,556.80),(12,2,'2025-12-08 02:01:59','Efectivo',2640.00,422.40,3062.40);
/*!40000 ALTER TABLE `Ventas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `v_producto_stock`
--

DROP TABLE IF EXISTS `v_producto_stock`;
/*!50001 DROP VIEW IF EXISTS `v_producto_stock`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `v_producto_stock` AS SELECT
 1 AS `id_producto`,
  1 AS `nombre_producto`,
  1 AS `stock`,
  1 AS `unidad_medida`,
  1 AS `activo` */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_venta_detalle`
--

DROP TABLE IF EXISTS `v_venta_detalle`;
/*!50001 DROP VIEW IF EXISTS `v_venta_detalle`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `v_venta_detalle` AS SELECT
 1 AS `id_venta`,
  1 AS `fecha_hora`,
  1 AS `id_usuario`,
  1 AS `metodo_pago`,
  1 AS `total`,
  1 AS `id_detalle_venta`,
  1 AS `id_producto`,
  1 AS `nombre_producto`,
  1 AS `cantidad`,
  1 AS `precio_unitario_cobrado`,
  1 AS `importe_total` */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_ventas_por_categoria_hoy`
--

DROP TABLE IF EXISTS `v_ventas_por_categoria_hoy`;
/*!50001 DROP VIEW IF EXISTS `v_ventas_por_categoria_hoy`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `v_ventas_por_categoria_hoy` AS SELECT
 1 AS `nombre_categoria`,
  1 AS `total_ventas` */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_ventas_resumen_dia`
--

DROP TABLE IF EXISTS `v_ventas_resumen_dia`;
/*!50001 DROP VIEW IF EXISTS `v_ventas_resumen_dia`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `v_ventas_resumen_dia` AS SELECT
 1 AS `dia`,
  1 AS `num_ventas`,
  1 AS `total_ventas` */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `vista_productos`
--

DROP TABLE IF EXISTS `vista_productos`;
/*!50001 DROP VIEW IF EXISTS `vista_productos`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `vista_productos` AS SELECT
 1 AS `id_producto`,
  1 AS `id_categoria`,
  1 AS `id_marca`,
  1 AS `nombre_producto`,
  1 AS `precio`,
  1 AS `stock`,
  1 AS `unidad_medida`,
  1 AS `activo`,
  1 AS `nombre_categoria`,
  1 AS `nombre_marca` */;
SET character_set_client = @saved_cs_client;

--
-- Dumping events for database 'dulceria'
--

--
-- Dumping routines for database 'dulceria'
--
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_crear_venta_json` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
DELIMITER ;;
CREATE DEFINER=`vic`@`%` PROCEDURE `sp_crear_venta_json`(
    IN p_idUsuario INT,
    IN p_metodoPago VARCHAR(50),
    IN p_subtotal DECIMAL(12,2),
    IN p_impuestos DECIMAL(12,2),
    IN p_total DECIMAL(12,2),
    IN p_items_json JSON,
    OUT p_idVenta INT
)
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE items_count INT;
    
    
    DECLARE cur_id_producto INT;
    DECLARE cur_cantidad DECIMAL(12,3);
    DECLARE cur_precio DECIMAL(12,2);
    DECLARE cur_importe DECIMAL(14,2);
    
    
    DECLARE current_stock DECIMAL(14,3);
    
    
    DECLARE v_path VARCHAR(255);

    SET p_idVenta = NULL;
    SET items_count = JSON_LENGTH(p_items_json);

    START TRANSACTION;

    
    SET i = 0;
    WHILE i < items_count DO
        
        SET v_path = CONCAT('$[', i, '].idProducto');
        SET cur_id_producto = CAST(JSON_UNQUOTE(JSON_EXTRACT(p_items_json, v_path)) AS UNSIGNED);
        
        SET v_path = CONCAT('$[', i, '].cantidad');
        SET cur_cantidad    = CAST(JSON_UNQUOTE(JSON_EXTRACT(p_items_json, v_path)) AS DECIMAL(12,3));

        
        SELECT stock INTO current_stock FROM Productos WHERE id_producto = cur_id_producto FOR UPDATE;
        
        IF current_stock IS NULL THEN
            ROLLBACK;
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Producto no existe o ID incorrecto';
        END IF;
        
        IF current_stock < cur_cantidad THEN
            ROLLBACK;
            
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Stock insuficiente para uno de los productos';
        END IF;

        SET i = i + 1;
    END WHILE;

    
    INSERT INTO Ventas (id_usuario, metodo_pago, subtotal, impuestos, total)
    VALUES (p_idUsuario, p_metodoPago, p_subtotal, p_impuestos, p_total);
    
    SET p_idVenta = LAST_INSERT_ID();

    
    SET i = 0;
    WHILE i < items_count DO
        
        SET v_path = CONCAT('$[', i, '].idProducto');
        SET cur_id_producto = CAST(JSON_UNQUOTE(JSON_EXTRACT(p_items_json, v_path)) AS UNSIGNED);
        
        
        SET v_path = CONCAT('$[', i, '].cantidad');
        SET cur_cantidad    = CAST(JSON_UNQUOTE(JSON_EXTRACT(p_items_json, v_path)) AS DECIMAL(12,3));
        
        
        SET v_path = CONCAT('$[', i, '].precio');
        SET cur_precio      = CAST(JSON_UNQUOTE(JSON_EXTRACT(p_items_json, v_path)) AS DECIMAL(12,2));
        
        
        SET cur_importe     = ROUND(cur_cantidad * cur_precio, 2);

        INSERT INTO Detalle_Ventas (id_venta, id_producto, cantidad, precio_unitario_cobrado, importe_total)
        VALUES (p_idVenta, cur_id_producto, cur_cantidad, cur_precio, cur_importe);

        SET i = i + 1;
    END WHILE;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_crear_venta_simple` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
DELIMITER ;;
CREATE DEFINER=`vic`@`%` PROCEDURE `sp_crear_venta_simple`(
  IN p_idUsuario INT,
  IN p_metodoPago VARCHAR(50),
  IN p_subtotal DECIMAL(12,2),
  IN p_impuestos DECIMAL(12,2),
  IN p_total DECIMAL(12,2),
  IN p_idProducto INT,
  IN p_cantidad DECIMAL(12,3),
  IN p_precio DECIMAL(12,2),
  OUT p_idVenta INT
)
BEGIN
  START TRANSACTION;
  INSERT INTO Ventas (id_usuario, metodo_pago, subtotal, impuestos, total)
    VALUES (p_idUsuario, p_metodoPago, p_subtotal, p_impuestos, p_total);
  SET p_idVenta = LAST_INSERT_ID();
  INSERT INTO Detalle_Ventas (id_venta, id_producto, cantidad, precio_unitario_cobrado, importe_total)
    VALUES (p_idVenta, p_idProducto, p_cantidad, p_precio, ROUND(p_cantidad * p_precio, 2));
  
  COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `v_producto_stock`
--

/*!50001 DROP VIEW IF EXISTS `v_producto_stock`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = cp850 */;
/*!50001 SET character_set_results     = cp850 */;
/*!50001 SET collation_connection      = cp850_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`vic`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_producto_stock` AS select `Productos`.`id_producto` AS `id_producto`,`Productos`.`nombre_producto` AS `nombre_producto`,`Productos`.`stock` AS `stock`,`Productos`.`unidad_medida` AS `unidad_medida`,`Productos`.`activo` AS `activo` from `Productos` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_venta_detalle`
--

/*!50001 DROP VIEW IF EXISTS `v_venta_detalle`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = cp850 */;
/*!50001 SET character_set_results     = cp850 */;
/*!50001 SET collation_connection      = cp850_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`vic`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_venta_detalle` AS select `v`.`id_venta` AS `id_venta`,`v`.`fecha_hora` AS `fecha_hora`,`v`.`id_usuario` AS `id_usuario`,`v`.`metodo_pago` AS `metodo_pago`,`v`.`total` AS `total`,`dv`.`id_detalle_venta` AS `id_detalle_venta`,`dv`.`id_producto` AS `id_producto`,`p`.`nombre_producto` AS `nombre_producto`,`dv`.`cantidad` AS `cantidad`,`dv`.`precio_unitario_cobrado` AS `precio_unitario_cobrado`,`dv`.`importe_total` AS `importe_total` from ((`Ventas` `v` join `Detalle_Ventas` `dv` on(`v`.`id_venta` = `dv`.`id_venta`)) join `Productos` `p` on(`dv`.`id_producto` = `p`.`id_producto`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_ventas_por_categoria_hoy`
--

/*!50001 DROP VIEW IF EXISTS `v_ventas_por_categoria_hoy`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = cp850 */;
/*!50001 SET character_set_results     = cp850 */;
/*!50001 SET collation_connection      = cp850_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`vic`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_ventas_por_categoria_hoy` AS select `c`.`nombre_categoria` AS `nombre_categoria`,sum(`dv`.`importe_total`) AS `total_ventas` from (((`Detalle_Ventas` `dv` join `Productos` `p` on(`dv`.`id_producto` = `p`.`id_producto`)) join `Categorias` `c` on(`p`.`id_categoria` = `c`.`id_categoria`)) join `Ventas` `v` on(`dv`.`id_venta` = `v`.`id_venta`)) where cast(`v`.`fecha_hora` as date) = curdate() group by `c`.`nombre_categoria` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_ventas_resumen_dia`
--

/*!50001 DROP VIEW IF EXISTS `v_ventas_resumen_dia`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = cp850 */;
/*!50001 SET character_set_results     = cp850 */;
/*!50001 SET collation_connection      = cp850_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`vic`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_ventas_resumen_dia` AS select cast(`Ventas`.`fecha_hora` as date) AS `dia`,count(0) AS `num_ventas`,coalesce(sum(`Ventas`.`total`),0) AS `total_ventas` from `Ventas` group by cast(`Ventas`.`fecha_hora` as date) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vista_productos`
--

/*!50001 DROP VIEW IF EXISTS `vista_productos`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = cp850 */;
/*!50001 SET character_set_results     = cp850 */;
/*!50001 SET collation_connection      = cp850_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`vic`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vista_productos` AS select `p`.`id_producto` AS `id_producto`,`p`.`id_categoria` AS `id_categoria`,`p`.`id_marca` AS `id_marca`,`p`.`nombre_producto` AS `nombre_producto`,`p`.`precio` AS `precio`,`p`.`stock` AS `stock`,`p`.`unidad_medida` AS `unidad_medida`,`p`.`activo` AS `activo`,`c`.`nombre_categoria` AS `nombre_categoria`,`m`.`nombre_marca` AS `nombre_marca` from ((`Productos` `p` left join `Categorias` `c` on(`p`.`id_categoria` = `c`.`id_categoria`)) left join `Marcas` `m` on(`p`.`id_marca` = `m`.`id_marca`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-07 22:26:56
