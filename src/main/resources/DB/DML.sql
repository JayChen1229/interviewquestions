
-- ----------------------------------------  USER  ----------------------------------------------------------
DELIMITER //
CREATE PROCEDURE SaveOrUpdateUser(IN p_user_id INT, IN p_user_name VARCHAR(255), IN p_email VARCHAR(255), IN p_password VARCHAR(255), IN p_cover_image LONGBLOB, IN p_biography TEXT)
BEGIN
    IF (p_user_id IS NULL) THEN
        INSERT INTO user(user_name, email, password, cover_image, biography) 
        VALUES (p_user_name, p_email, p_password, p_cover_image, p_biography);
ELSE
UPDATE user
SET user_name = p_user_name, email = p_email, password = p_password, cover_image = p_cover_image, biography = p_biography
WHERE user_id = p_user_id;
END IF;
END //
DELIMITER ;


DELIMITER //
CREATE PROCEDURE EmailExists(IN p_email VARCHAR(255), OUT p_exists INT)
BEGIN
SELECT CASE
           WHEN COUNT(*) > 0 THEN 1
           ELSE 0
           END INTO p_exists
FROM user WHERE email = p_email;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE FindUserByEmail(
    IN p_email VARCHAR(255)
)
BEGIN
SELECT * FROM user WHERE email = p_email;
END //
DELIMITER ;


DELIMITER //
CREATE PROCEDURE FindUserById(
    IN p_user_id INT
)
BEGIN
SELECT * FROM user WHERE user_id = p_user_id;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE UpdateUserCoverImage(
    IN p_user_id INT,
    IN p_cover_image LONGBLOB
)
BEGIN
UPDATE user SET cover_image = p_cover_image WHERE user_id = p_user_id;
END //
DELIMITER ;


DELIMITER //
CREATE PROCEDURE FindUserByPostId(IN p_post_id INT)
BEGIN
SELECT u.*
FROM user u
         JOIN post p ON u.user_id = p.user_id
WHERE p.post_id = p_post_id;
END //
DELIMITER ;


DELIMITER //
CREATE PROCEDURE FindUserByCommentId(
    IN p_comment_id INT
)
BEGIN
SELECT u.*
FROM user u
         JOIN comment c ON u.user_id = c.user_id
WHERE c.comment_id = p_comment_id;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE UpdateUserBiography(
    IN p_user_id INT,
    IN p_biography TEXT
)
BEGIN
UPDATE user
SET biography = p_biography
WHERE user_id = p_user_id;
END //
DELIMITER ;



-- -- ----------------------------------------  USER  ----------------------------------------------------------

-- -- ----------------------------------------  POST  ----------------------------------------------------------

DELIMITER //
CREATE PROCEDURE FindPostById(
    IN p_post_id INT
)
BEGIN
SELECT * FROM post WHERE post_id = p_post_id;
END //
DELIMITER ;


DELIMITER //
CREATE PROCEDURE FindPostByCommentId(
    IN p_comment_id INT
)
BEGIN
SELECT p.*
FROM post p
         JOIN comment c ON p.post_id = c.post_id
WHERE c.comment_id = p_comment_id;
END //
DELIMITER ;

DELIMITER //
CREATE  PROCEDURE GetAllPosts()
BEGIN
SELECT * FROM post;
END //
DELIMITER ;


DELIMITER //
CREATE PROCEDURE SaveOrUpdatePost(
    INOUT p_post_id INT,
    IN p_user_id INT,
    IN p_content TEXT,
    IN p_image LONGBLOB
)
BEGIN
  IF p_post_id IS NULL THEN
    INSERT INTO post(user_id, content, image)
    VALUES (p_user_id, p_content, p_image);
    SET p_post_id = LAST_INSERT_ID();
ELSE
UPDATE post
SET user_id = p_user_id, content = p_content, image = p_image
WHERE post_id = p_post_id;
END IF;
SELECT * FROM post WHERE post_id = p_post_id;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE DeletePostAndComments(
    IN p_post_id INT
)
BEGIN
DELETE FROM comment WHERE post_id = p_post_id;

DELETE FROM post WHERE post_id = p_post_id;
END //
DELIMITER ;

-- ----------------------------------------  POST  ----------------------------------------------------------

-- ----------------------------------------  COMMENT  -------------------------------------------------------
DELIMITER //
CREATE PROCEDURE FindCommentsByPostId(
    IN p_post_id INT
)
BEGIN
SELECT *
FROM comment
WHERE post_id = p_post_id;
END //
DELIMITER ;


DELIMITER //
CREATE PROCEDURE SaveOrUpdateComment(
    INOUT p_comment_id INT,
    IN p_user_id INT,
    IN p_post_id INT,
    IN p_content TEXT
)
BEGIN
  IF p_comment_id IS NULL THEN
    INSERT INTO comment(user_id, post_id, content)
    VALUES (p_user_id, p_post_id, p_content);
    
    SET p_comment_id = LAST_INSERT_ID();
ELSE
UPDATE comment
SET user_id = p_user_id, post_id = p_post_id, content = p_content
WHERE comment_id = p_comment_id;
END IF;

SELECT * FROM comment WHERE comment_id = p_comment_id;
END //
DELIMITER ;
-- ----------------------------------------  COMMENT  -------------------------------------------------------
