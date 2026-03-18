-- Disable checks
SET FOREIGN_KEY_CHECKS=0;

-- ========================
-- DROP TABLES (Correct Order)
-- ========================
DROP TABLE IF EXISTS `chunk`;
DROP TABLE IF EXISTS `file_version`;
DROP TABLE IF EXISTS `file_metadata`;
DROP TABLE IF EXISTS `users`;

-- ========================
-- USERS TABLE
-- ========================
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255),
  `email` varchar(255),
  `password` varchar(255) NOT NULL,
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

-- ========================
-- FILE METADATA TABLE
-- ========================
CREATE TABLE `file_metadata` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) NOT NULL,
  `file_path` varchar(255),
  `file_size` bigint,
  `uploaded_at` datetime,
  `user_id` bigint,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
);

-- ========================
-- FILE VERSION TABLE
-- ========================
CREATE TABLE `file_version` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `version_number` int,
  `file_id` bigint,
  `created_at` datetime,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`file_id`) REFERENCES `file_metadata`(`id`)
);

-- ========================
-- CHUNK TABLE
-- ========================
CREATE TABLE `chunk` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `chunk_index` int,
  `chunk_path` varchar(255),
  `chunk_size` bigint,
  `file_id` bigint,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`file_id`) REFERENCES `file_metadata`(`id`)
);

-- ========================
-- ENABLE CHECKS BACK
-- ========================
SET FOREIGN_KEY_CHECKS=1;