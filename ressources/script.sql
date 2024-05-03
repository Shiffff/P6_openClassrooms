-- Création de la table des utilisateurs
CREATE TABLE `users` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(50) UNIQUE NOT NULL,
  `email` VARCHAR(120) UNIQUE NOT NULL,
  `password` VARCHAR(255) NOT NULL
);

-- Création de la table des thèmes
CREATE TABLE `themes` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `title` VARCHAR(100) NOT NULL,
  `description` TEXT NOT NULL
);

-- Création de la table des articles
CREATE TABLE `articles` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `title` VARCHAR(100) NOT NULL,
  `content` TEXT NOT NULL,
  `author` VARCHAR(50) NOT NULL,
  `theme_id` INT NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`theme_id`) REFERENCES `themes`(`id`)
);

-- Création de la table des commentaires
CREATE TABLE `comments` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `content` TEXT NOT NULL,
  `author` VARCHAR(50) NOT NULL,
  `article_id` INT NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`article_id`) REFERENCES `articles`(`id`)
);

-- Création de la table de liaison entre les utilisateurs et les thèmes
CREATE TABLE `user_subscriptions` (
  `user_id` INT NOT NULL,
  `theme_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `theme_id`),
  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`),
  FOREIGN KEY (`theme_id`) REFERENCES `themes`(`id`)
);

