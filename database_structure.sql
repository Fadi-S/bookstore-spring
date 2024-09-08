DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`         int          NOT NULL AUTO_INCREMENT,
    `first_name` varchar(200) DEFAULT NULL,
    `last_name`  varchar(200) NOT NULL,
    `email`      varchar(200) NOT NULL,
    `password`   varchar(200) NOT NULL,
    `picture`    varchar(200) DEFAULT NULL,
    `stripe_id`  varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `email_unique` (`email`) USING BTREE
);

DROP TABLE IF EXISTS `authorities`;
CREATE TABLE `authorities`
(
    `id`        int         NOT NULL AUTO_INCREMENT,
    `user_id`   int         NOT NULL,
    `authority` varchar(50) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `authorities_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

DROP TABLE IF EXISTS `oauth_token`;
CREATE TABLE `oauth_token`
(
    `user_id` int          NOT NULL,
    `token`   varchar(200) NOT NULL,
    PRIMARY KEY (`token`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `oauth_token_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`
(
    `id`               int          NOT NULL AUTO_INCREMENT,
    `title`            varchar(255) NOT NULL,
    `author`           varchar(255) NOT NULL,
    `genre`            varchar(255) NOT NULL,
    `price_in_pennies` int          NOT NULL,
    `quantity`         int          DEFAULT '0',
    `cover`            varchar(200) DEFAULT NULL,
    `overview`         text         NOT NULL,
    PRIMARY KEY (`id`),
    KEY `author` (`author`) USING BTREE,
    KEY `genre` (`genre`) USING BTREE,
    KEY `price` (`price_in_pennies`) USING BTREE,
    KEY `title` (`title`) USING BTREE,
    FULLTEXT KEY `overview_fulltext` (`overview`)
);

DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`
(
    `id`          int          NOT NULL AUTO_INCREMENT,
    `full_name`   varchar(255) NOT NULL,
    `country`     char(2)      NOT NULL,
    `address1`    varchar(255) NOT NULL,
    `address2`    varchar(255) DEFAULT NULL,
    `postal_code` varchar(50)  DEFAULT NULL,
    `city`        varchar(50)  NOT NULL,
    `user_id`     int          NOT NULL,
    PRIMARY KEY (`id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `address_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`
(
    `id`               int                                               NOT NULL AUTO_INCREMENT,
    `user_id`          int                                                        DEFAULT NULL,
    `price_in_pennies` int                                               NOT NULL DEFAULT '0',
    `status`           enum ('PENDING','SHIPPED','DELIVERED','CANCELED') NOT NULL,
    `created_at`       timestamp                                         NULL     DEFAULT CURRENT_TIMESTAMP,
    `is_paid`          tinyint(1)                                        NOT NULL DEFAULT '0',
    `address_id`       int                                               NOT NULL,
    `number`           varchar(20)                                       NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `number` (`number`) USING BTREE,
    KEY `user_id` (`user_id`),
    KEY `address_id` (`address_id`),
    CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`)
);

DROP TABLE IF EXISTS `book_order`;
CREATE TABLE `book_order`
(
    `id`               int NOT NULL AUTO_INCREMENT,
    `book_id`          int NOT NULL,
    `order_id`         int NOT NULL,
    `quantity`         int NOT NULL,
    `price_in_pennies` int DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `order_book_unqiue` (`order_id`, `book_id`) USING BTREE,
    KEY `book_id` (`book_id`),
    CONSTRAINT `book_order_ibfk_3` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
    CONSTRAINT `book_order_ibfk_4` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE
);

DROP TABLE IF EXISTS `review`;
CREATE TABLE `review`
(
    `id`      int NOT NULL AUTO_INCREMENT,
    `rating`  int NOT NULL,
    `body`    text,
    `book_id` int NOT NULL,
    `user_id` int NOT NULL,
    PRIMARY KEY (`id`),
    KEY `book_id` (`book_id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `review_ibfk_1` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
    CONSTRAINT `review_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

DROP TABLE IF EXISTS `shopping_cart_item`;
CREATE TABLE `shopping_cart_item`
(
    `id`       int NOT NULL AUTO_INCREMENT,
    `user_id`  int NOT NULL,
    `book_id`  int NOT NULL,
    `quantity` int NOT NULL DEFAULT '1',
    PRIMARY KEY (`id`)
);