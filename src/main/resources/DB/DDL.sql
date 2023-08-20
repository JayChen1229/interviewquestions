-- 建立資料庫
CREATE SCHEMA social_media_platform;

-- 使用該資料庫
USE
social_media_platform;

-- 建立 user 資料表
CREATE TABLE `user`
(
    `user_id`     int          NOT NULL AUTO_INCREMENT,
    `user_name`   varchar(255) NOT NULL,
    `email`       varchar(255) NOT NULL,
    `password`    varchar(255) NOT NULL,
    `cover_image` longblob,
    `biography`   text,
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `Email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 建立 post 資料表
CREATE TABLE `post`
(
    `post_id`    int  NOT NULL AUTO_INCREMENT,
    `user_id`    int  NOT NULL,
    `content`    text NOT NULL,
    `image`      longblob,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`post_id`),
    KEY          `post_ibfk_1` (`user_id`),
    CONSTRAINT `post_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 建立 comment 資料表
CREATE TABLE `comment`
(
    `comment_id` int  NOT NULL AUTO_INCREMENT,
    `user_id`    int  NOT NULL,
    `post_id`    int  NOT NULL,
    `content`    text NOT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`comment_id`),
    KEY          `comment_ibfk_1` (`user_id`),
    KEY          `comment_ibfk_2` (`post_id`),
    CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
    CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;





