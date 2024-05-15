USE JDBC;

SHOW TABLES;

CREATE TABLE Paperback (
	Paperback_ID INT PRIMARY KEY,
	Paperback_Title	VARCHAR(255),
	Paperback_Subject VARCHAR(30),
	Paperback_Author VARCHAR(255),
	Paperback_Publisher	VARCHAR(255),
	Paperback_Price	FLOAT,
	Paperback_Rating FLOAT,
	Paperback_No_of_Ratings INT,	
	Paperback_URL VARCHAR(255),
	PI_Path VARCHAR(255)
);

INSERT INTO Paperback VALUES
(1, "Zero To Mastery In Python Programming", "Python", "Rakesh K. Yadav, Srinivas Arukona, Mona Singh",
"Vei Publishing", 495, 3.8, 210, "https://amzn.in/d/5FpdS0z", 
"https://drive.google.com/uc?id=1mXj37n4gQeqWpK2-tF2mji5HOwVhodHP ");
 
CREATE TABLE EBooks (
	EBooks_ID INT PRIMARY KEY,
	EBooks_Title VARCHAR(255),
	EBooks_Subject VARCHAR(30),
	EBooks_Author VARCHAR(255),
	EBooks_Price FLOAT,
	EBooks_Rating FLOAT,
	EBooks_No_of_Ratings INT,	
	EBooks_URL VARCHAR(255),
    EBI_Path VARCHAR(255)
);

CREATE TABLE Course (
	Course_ID INT PRIMARY KEY,
	Course_Title VARCHAR(255),
	Course_Subject VARCHAR(30),
	Offered_By VARCHAR(50),
	Course_Price FLOAT,
	Course_Rating FLOAT,
	Course_No_of_Ratings INT,
	Course_No_of_Languages_Available INT,
	Course_Languages VARCHAR(1000),
	Course_No_of_People_Enrolled BIGINT,
	Course_URL VARCHAR(255)
);

CREATE TABLE Users (
    User_Name VARCHAR(255) UNIQUE NOT NULL,
    User_Password VARCHAR(255) NOT NULL
);

SELECT * FROM Paperback;

SELECT * FROM EBooks;

SELECT * FROM Course;

SELECT *, (EBooks_Rating * EBooks_No_of_Ratings) / EBooks_Price AS rating_score FROM EBooks WHERE EBooks_Subject = "Python" AND EBooks_Price <= 3000 AND EBooks_Rating <= 5 ORDER BY rating_score DESC LIMIT 10;