# interviewquestions

後端使用Spring boot 3 建置 用到的依賴項有

spring data jpa , spring web, MySQL Driver, lombok, spring-security-crypto

前端採用 Vue.js 2 建置

採用cdn引入Vue.js , 靜態網站放置於 resources/static 資料夾內
採用一頁式網頁設計

詳細圖文教學與介紹
https://glamorous-spell-72b.notion.site/Java-2beb0b78501b4b3b8aeaed681fa213c1?pvs=4

專案建置流程
1. 下載專案使用JDK開啟 Java 版本號為17
2. 設定application.properties
   spring.datasource.username=輸入自己的資料庫名稱
   spring.datasource.password=輸入自己的資料庫密碼
3. 開啟MySQL 建置所需要資料庫
   SQL指令 DDL 放置於 src/main/resources/DB  用於建置SCHEMA 與 Table
   SQL指令 DML 放置於 src/main/resources/DB  內涵Stored Procedure指令
4. 相關設定設定完成即可運行

完成功能：
1. 登入系統 
2. 註冊系統 (使用 bCrypt 單向加密，判斷Email 是否重複註冊，各項欄位不得為空值，使用正則表達式)
3. 登入驗證：登入成功才能進入文章列表 (使用前端Vue.js 判斷 是否為User)
4. 發表文章系統(可以新增圖片，點擊文章圖片可以放大)
5. 顯示所有文章系統(圖片採用url傳輸)
6. 修改文章系統(只有自己寫的文章，才會顯示修改按鈕)
7. 刪除文章系統(只有自己寫的文章，才會顯示刪除按鈕)
8. 留言系統：使用者可以對任意文章進行留言
9. 留言顯示系統：點擊留言顯示按鈕，可以顯示所有使用者的留言，包含使用者資訊 
10. 會員系統(可以新增自我介紹，可以新增與更改頭像照片)
11. 登出系統（回到登入頁面）
12. 針對使用者輸入做驗證，防止XSS攻擊
13. 使用log 紀錄錯誤訊息
14. 添加例外處理
15. Stored Procedure 設計 防止 SQL Injection


滿足需求：
1. 使用 Vue.js 做為前端技術。 
2. 使用 Spring Boot 搭建相關應用程式。 
3. 使用 RESTful API 風格建立後端服務。 
4. 使用 Maven 或 Gradle 做為專案建立的工具。 
5. 使用 Stored Procedure 存取資料庫。 
6. 實作 Transaction，避免資料錯亂。 
7. 資料庫的 DDL 和 DML 存放在專案下的\DB 資料夾內
8. 防止 SQL Injection 以及 XSS 攻擊。



