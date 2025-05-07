-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 07, 2025 at 10:03 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `crm_system`
--

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `Id` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Surname` varchar(100) DEFAULT NULL,
  `Lastname` varchar(100) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `Phonenumber` varchar(20) DEFAULT NULL,
  `Address1` text DEFAULT NULL,
  `Profilepicture` varchar(255) DEFAULT NULL,
  `status` enum('active','inactive') DEFAULT 'active',
  `Company` varchar(100) DEFAULT NULL,
  `Category` enum('vip','regular','new') DEFAULT 'new'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`Id`, `Name`, `Surname`, `Lastname`, `Email`, `Phonenumber`, `Address1`, `Profilepicture`, `status`, `Company`, `Category`) VALUES
(3, 'fdsf', 'dfsdf', 'dfds', 'fsdfdf', 'dfsd', 'fds', NULL, 'inactive', 'sdfdf', 'vip'),
(5, 'dfdfs', 'fassaf', 'xcxf', '', '0987654345', '', NULL, 'active', '', 'vip'),
(7, 'dgsdg', '', '', 'customer@gmail.com', '', '', NULL, 'inactive', '', 'regular');

-- --------------------------------------------------------

--
-- Table structure for table `items`
--

CREATE TABLE `items` (
  `Id` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Price` decimal(10,2) NOT NULL,
  `Quantity` int(11) DEFAULT 0,
  `Image` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `items`
--

INSERT INTO `items` (`Id`, `Name`, `Price`, `Quantity`, `Image`) VALUES
(2, 'Pen', 5.00, 20, 'images/pen.png'),
(3, 'Laptops', 25000.00, 500, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `Id` int(11) NOT NULL,
  `userName` varchar(100) NOT NULL,
  `Email` varchar(100) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `status` enum('active','inactive') DEFAULT 'active',
  `Profile_picture` varchar(255) DEFAULT NULL,
  `Privilege` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`Id`, `userName`, `Email`, `Password`, `status`, `Profile_picture`, `Privilege`) VALUES
(1, 'Mike', 'mike@gmail.com', '12345', 'active', 'images/wallpaper.png', 'admin'),
(2, 'user2', 'user2@gmail.com', '12345', 'active', 'images\\1746464255024_wallpaper.png', 'admin'),
(3, 'admin', 'admin@gmail.com', '12345', 'active', 'images\\1746497306768_photo-1522529599102-193c0d76b5b6.jpeg', 'admin'),
(4, 'saleperson', 'saleperson@gmail.com', '12345', 'active', 'images\\1746497402815_360_F_214746128_31JkeaP6rU0NzzzdFC4khGkmqc8noe6h.jpg', 'salesPerson'),
(5, '', '', '', 'active', NULL, 'user'),
(7, 'saleperson24', 'sale2@gmail.com', '12345', 'active', NULL, 'salesperson'),
(8, 'newSalePerson', 'sale3@gmail.com', '12345', 'active', NULL, 'salesPerson');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`Id`),
  ADD UNIQUE KEY `Email` (`Email`);

--
-- Indexes for table `items`
--
ALTER TABLE `items`
  ADD PRIMARY KEY (`Id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`Id`),
  ADD UNIQUE KEY `Email` (`Email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `items`
--
ALTER TABLE `items`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
