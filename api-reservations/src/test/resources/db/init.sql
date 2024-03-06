-- MySQL dump 10.13  Distrib 8.0.32, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: flights_reservation
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `itinerary`
--

DROP TABLE IF EXISTS `itinerary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `itinerary` (
                             `id` bigint AUTO_INCREMENT PRIMARY KEY,
                             `version` bigint,
                             `itinerary_id` bigint DEFAULT NULL,
                             UNIQUE KEY `UK_nuabwliqt5wnaafbfe1mjqdtu` (`itinerary_id`),
                             CONSTRAINT `FKfuq1y9btsw9if6vuyrnngvw0j` FOREIGN KEY (`itinerary_id`) REFERENCES `price` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `itinerary`
--

LOCK TABLES `itinerary` WRITE;
/*!40000 ALTER TABLE `itinerary` DISABLE KEYS */;
/*!40000 ALTER TABLE `itinerary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `passenger`
--

DROP TABLE IF EXISTS `passenger`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `passenger` (
                             `birthday` date NOT NULL,
                             `id` bigint AUTO_INCREMENT PRIMARY KEY,
                             `version` bigint,
                             `reservation_id` bigint DEFAULT NULL,
                             `first_name` varchar(30) NOT NULL,
                             `last_name` varchar(30) NOT NULL,
                             `document_number` varchar(255) NOT NULL,
                             `document_type` varchar(255) NOT NULL,
                             KEY `FKp4qdewtk73iwqerswqtqs0g1q` (`reservation_id`),
                             CONSTRAINT `FKp4qdewtk73iwqerswqtqs0g1q` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `passenger`
--

LOCK TABLES `passenger` WRITE;
/*!40000 ALTER TABLE `passenger` DISABLE KEYS */;
/*!40000 ALTER TABLE `passenger` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `price`
--

DROP TABLE IF EXISTS `price`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `price` (
                         `base_price` decimal(38,2) NOT NULL,
                         `total_price` decimal(38,2) NOT NULL,
                         `total_tax` decimal(38,2) NOT NULL,
                         `version` bigint,
                         `id` bigint AUTO_INCREMENT PRIMARY KEY
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `price`
--

LOCK TABLES `price` WRITE;
/*!40000 ALTER TABLE `price` DISABLE KEYS */;
/*!40000 ALTER TABLE `price` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation` (
                               `creation_date` date NOT NULL,
                               `id` bigint AUTO_INCREMENT PRIMARY KEY,
                               `version` bigint,
                               `itinerary_id` bigint DEFAULT NULL,
                               KEY `FKmm7xy4cybogy2irexcsttlvi2` (`itinerary_id`),
                               CONSTRAINT `FKmm7xy4cybogy2irexcsttlvi2` FOREIGN KEY (`itinerary_id`) REFERENCES `itinerary` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `segment`
--

DROP TABLE IF EXISTS `segment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `segment` (
                           `carrier` varchar(3) NOT NULL,
                           `destination` varchar(3) NOT NULL,
                           `origin` varchar(3) NOT NULL,
                           `id` bigint AUTO_INCREMENT PRIMARY KEY,
                           `version` bigint,
                           `itinerary_id` bigint DEFAULT NULL,
                           `arrival` varchar(255) NOT NULL,
                           `departure` varchar(255) NOT NULL,
                           KEY `FK8l81wkolgwmpxhicuc9o6u141` (`itinerary_id`),
                           CONSTRAINT `FK8l81wkolgwmpxhicuc9o6u141` FOREIGN KEY (`itinerary_id`) REFERENCES `itinerary` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `segment`
--

LOCK TABLES `segment` WRITE;
/*!40000 ALTER TABLE `segment` DISABLE KEYS */;
/*!40000 ALTER TABLE `segment` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-07 13:20:57

-- MySQL dump 10.13  Distrib 8.0.32, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: flights_reservation
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `itinerary`
--

LOCK TABLES `itinerary` WRITE;
/*!40000 ALTER TABLE `itinerary` DISABLE KEYS */;
INSERT INTO `itinerary` VALUES (1,3,6),(2,1,3),(3,0,7),(4,0,8),(5,0,9);
/*!40000 ALTER TABLE `itinerary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `passenger`
--

LOCK TABLES `passenger` WRITE;
/*!40000 ALTER TABLE `passenger` DISABLE KEYS */;
INSERT INTO `passenger` VALUES ('1985-01-01',1,0,1,'Andres','Sacco','AB554713','PASSPORT'),('1985-01-01',2,0,2,'Andres','Sacco','AB554714','PASSPORT'),('1985-01-01',3,0,2,'Horacio','Sacco','AB554715','PASSPORT'),('1985-01-01',4,0,3,'Ignacio','Canale','AB554716','PASSPORT'),('1985-01-01',5,0,4,'Julian','Dominguez','AB554717','PASSPORT'),('1985-01-01',6,0,1,'Andres','Sacco','AB554718','PASSPORT'),('1985-01-01',7,0,3,'Josefa','Sacco','AB554719','PASSPORT'),('1985-01-01',8,0,4,'Maria','Rodriguez','AB554720','PASSPORT'),('1985-01-01',9,0,5,'Rodolfo','Rodriguez','AB554721','PASSPORT'),('1985-01-01',10,0,5,'Maria','Ferrari','AB554722','PASSPORT');
/*!40000 ALTER TABLE `passenger` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `price`
--

LOCK TABLES `price` WRITE;
/*!40000 ALTER TABLE `price` DISABLE KEYS */;
INSERT INTO `price` VALUES (10.00,30.00,20.00,0,1),(10.00,30.00,20.00,0,2),(10.00,30.00,20.00,0,3),(10.00,30.00,20.00,0,4),(10.00,30.00,20.00,0,5),(10.00,30.00,20.00,0,6),(10.00,30.00,20.00,0,7),(10.00,30.00,20.00,0,8),(10.00,30.00,20.00,0,9);
/*!40000 ALTER TABLE `price` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
INSERT INTO `reservation` VALUES ('2023-11-11',1,3,1),('2023-11-12',2,1,2),('2023-11-10',3,0,3),('2023-11-13',4,0,4),('2023-11-13',5,0,5);
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `segment`
--

LOCK TABLES `segment` WRITE;
/*!40000 ALTER TABLE `segment` DISABLE KEYS */;
INSERT INTO `segment` VALUES ('AA','MIA','BUE',1,0,NULL,'2024-01-01','2023-12-31'),('AA','MIA','BUE',2,0,NULL,'2024-01-01','2023-12-31'),('AA','MIA','BUE',3,0,2,'2024-01-01','2023-12-31'),('AA','MIA','BUE',4,0,NULL,'2024-01-01','2023-12-31'),('AA','MIA','BUE',5,0,NULL,'2024-01-01','2023-12-31'),('AA','MIA','BUE',6,0,1,'2024-01-01','2023-12-31'),('AA','MIA','BUE',7,0,3,'2024-01-01','2023-12-31'),('AA','MIA','BUE',8,0,4,'2024-01-01','2023-12-31'),('AA','MIA','BUE',9,0,5,'2024-01-01','2023-12-31');
/*!40000 ALTER TABLE `segment` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-14  9:40:37