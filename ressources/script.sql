-- Création de la table des utilisateurs
CREATE TABLE `users` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(50) UNIQUE NOT NULL,
  `email` VARCHAR(120) UNIQUE NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Création de la table des thèmes
CREATE TABLE `themes` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `title` VARCHAR(100) NOT NULL,
  `description` TEXT NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Création de la table des articles
CREATE TABLE `articles` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `title` VARCHAR(100) NOT NULL,
  `content` TEXT NOT NULL,
  `author` VARCHAR(50) NOT NULL,
  `theme_id` INT NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`theme_id`) REFERENCES `themes`(`id`)
);

-- Création de la table des commentaires
CREATE TABLE `comments` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `content` TEXT NOT NULL,
  `author` VARCHAR(50) NOT NULL,
  `article_id` INT NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`article_id`) REFERENCES `articles`(`id`)
);

-- Création de la table de liaison entre les utilisateurs et les thèmes
CREATE TABLE `user_subscriptions` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `user_id` INT NOT NULL,
  `theme_id` INT NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`),
  FOREIGN KEY (`theme_id`) REFERENCES `themes`(`id`)
);

-- Ajout de 10 thèmes
INSERT INTO themes (title, description, created_at, updated_at) VALUES
  ('Angular', 'Framework JavaScript pour le développement d''applications web', NOW(), NOW()),
  ('React', 'Bibliothèque JavaScript pour la construction d''interfaces utilisateur', NOW(), NOW()),
  ('Vue.js', 'Framework JavaScript pour la construction d''interfaces utilisateur', NOW(), NOW()),
  ('jQuery', 'Bibliothèque JavaScript pour simplifier l''utilisation de JavaScript', NOW(), NOW()),
  ('Node.js', 'Runtime JavaScript pour le développement côté serveur', NOW(), NOW()),
  ('Express.js', 'Framework Node.js pour la création d''applications web', NOW(), NOW()),
  ('Bootstrap', 'Framework CSS pour la création d''interfaces utilisateur responsive', NOW(), NOW()),
  ('Material Design', 'Système de conception d''interface utilisateur développé par Google', NOW(), NOW()),
  ('Ember.js', 'Framework JavaScript pour le développement d''applications web', NOW(), NOW()),
  ('Svelte', 'Framework JavaScript pour la construction d''interfaces utilisateur', NOW(), NOW());

