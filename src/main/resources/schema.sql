DROP TABLE IF EXISTS POST_TAGS CASCADE;
DROP TABLE IF EXISTS COMMENTS CASCADE;
DROP TABLE IF EXISTS POSTS CASCADE;
DROP TABLE IF EXISTS TAGS CASCADE;
DROP TABLE IF EXISTS CATEGORIES CASCADE;
DROP TABLE IF EXISTS USERS CASCADE;
DROP TYPE IF EXISTS USER_ROLE CASCADE;

CREATE TYPE USER_ROLE AS ENUM('AUTHOR', 'ADMIN');

CREATE TABLE IF NOT EXISTS USERS (
	ID SERIAL PRIMARY KEY,
	USERNAME VARCHAR(50) UNIQUE NOT NULL,
	EMAIL VARCHAR(100) UNIQUE NOT NULL,
	PASSWORD TEXT NOT NULL,
	IMAGE_URL VARCHAR(256) DEFAULT NULL,
	ROLE USER_ROLE DEFAULT 'AUTHOR',
	CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS CATEGORIES (
	ID SERIAL PRIMARY KEY,
	NAME VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS POSTS (
	ID SERIAL PRIMARY KEY,
	TITLE VARCHAR(255) NOT NULL,
	EXCERPT VARCHAR(255) NULL,
	CONTENT TEXT NOT NULL,
	POSTED_ON TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	LAST_UPDATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	READ_TIME SMALLINT,
	CATEGORY TEXT,
	POSTER_CARD VARCHAR(255) NULL,
	TAGS TEXT,
	STATUS VARCHAR(30) DEFAULT 'DRAFT',
	FEATURED BOOLEAN DEFAULT FALSE,
	AUTHOR_ID INTEGER NOT NULL,
	FOREIGN KEY (AUTHOR_ID) REFERENCES USERS (ID)
);

CREATE TABLE COMMENTS (
	ID SERIAL PRIMARY KEY,
	CONTENT TEXT NOT NULL,
	CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	AUTHOR_ID INT NOT NULL,
	POST_ID INT NOT NULL,
	FOREIGN KEY (AUTHOR_ID) REFERENCES USERS (ID),
	FOREIGN KEY (POST_ID) REFERENCES POSTS (ID)
);

CREATE TABLE TAGS (
	ID SERIAL PRIMARY KEY,
	NAME VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE POST_TAGS (
	POST_ID INT NOT NULL,
	TAG_ID INT NOT NULL,
	PRIMARY KEY (POST_ID, TAG_ID),
	FOREIGN KEY (POST_ID) REFERENCES POSTS (ID),
	FOREIGN KEY (TAG_ID) REFERENCES TAGS (ID)
);

INSERT INTO USERS (USERNAME, EMAIL, PASSWORD, ROLE) VALUES ('bright', 'bright@orji.com', 'password', 'ADMIN');

INSERT INTO USERS (USERNAME, EMAIL, PASSWORD) VALUES	('john', 'john@yopmail.com', 'password');

INSERT INTO	USERS (USERNAME, EMAIL, PASSWORD) VALUES ('jane', 'jane@yopmail.com', 'password');

INSERT INTO posts (TITLE, EXCERPT, CONTENT, AUTHOR_ID, CATEGORY, POSTER_CARD, FEATURED, TAGS, STATUS) VALUES
('The Future of AI', 'Exploring the potential of AI', 'Artificial Intelligence is transforming...', 1, 'Technology', 'https://example.com/image1.jpg', TRUE, 'AI, Future, Tech', 'Published'),
('Healthy Living Tips', 'Top 10 tips for a healthier life', 'Health is wealth, and here are the...', 2, 'Health', 'https://example.com/image2.jpg', FALSE, 'Wellness, Fitness, Health', 'Draft'),
('Space Exploration 2025', 'What is next for space?', 'Space agencies are planning...', 3, 'Science', 'https://example.com/image3.jpg', TRUE, 'Space, NASA, Mars', 'Published'),
('Top 10 Java Tricks', 'Master Java with these tips', 'Java remains one of the most...', 1, 'Programming', 'https://example.com/image4.jpg', FALSE, 'Java, Coding, Tricks', 'Published'),
('The Rise of Electric Cars', 'EVs are taking over', 'With Tesla and other companies...', 2, 'Automotive', 'https://example.com/image5.jpg', TRUE, 'EVs, Tesla, Cars', 'Published'),
('Best Investment Strategies', 'How to grow your wealth', 'Investing wisely is crucial for...', 3, 'Finance', 'https://example.com/image6.jpg', FALSE, 'Stocks, Money, Finance', 'Draft'),
('5G Network Explained', 'How 5G is changing the world', 'With the rollout of 5G...', 1, 'Technology', 'https://example.com/image7.jpg', TRUE, '5G, Network, Internet', 'Published'),
('Best Coding Practices', 'Write cleaner and better code', 'Writing efficient and readable...', 2, 'Programming', 'https://example.com/image8.jpg', FALSE, 'Code, Best Practices', 'Published'),
('Meditation for Beginners', 'Simple ways to start meditation', 'Meditation helps reduce stress...', 3, 'Health', 'https://example.com/image9.jpg', TRUE, 'Mindfulness, Meditation', 'Published'),
('How to Start a Business', 'A guide for entrepreneurs', 'Starting a business requires...', 1, 'Business', 'https://example.com/image10.jpg', FALSE, 'Entrepreneurship, Startup', 'Draft'),
('Understanding Cryptocurrency', 'Bitcoin, Ethereum, and beyond', 'Cryptocurrency is changing...', 2, 'Finance', 'https://example.com/image11.jpg', TRUE, 'Crypto, Bitcoin, Blockchain', 'Published'),
('Photography Tips for Beginners', 'Capture stunning photos', 'Photography is an art that...', 3, 'Photography', 'https://example.com/image12.jpg', FALSE, 'Photography, Camera, Tips', 'Published'),
('10 Best Travel Destinations', 'Explore the world', 'Traveling expands your horizons...', 1, 'Travel', 'https://example.com/image13.jpg', TRUE, 'Travel, Adventure, Explore', 'Published'),
('The Psychology of Success', 'What makes people successful?', 'Success is a combination of...', 2, 'Self-Improvement', 'https://example.com/image14.jpg', FALSE, 'Success, Motivation, Psychology', 'Published'),
('Cybersecurity Trends 2025', 'Stay protected online', 'Cyber threats are evolving...', 3, 'Technology', 'https://example.com/image15.jpg', TRUE, 'Security, Cyber, Hacking', 'Published'),
('Mastering Data Science', 'Start your journey in data science', 'Data is the new oil, and...', 1, 'Programming', 'https://example.com/image16.jpg', FALSE, 'Data Science, AI, Python', 'Draft'),
('The Benefits of Yoga', 'Why you should practice yoga', 'Yoga improves flexibility...', 2, 'Health', 'https://example.com/image17.jpg', TRUE, 'Yoga, Wellness, Fitness', 'Published'),
('Gaming Industry Growth', 'How gaming is evolving', 'With eSports and VR, gaming...', 3, 'Gaming', 'https://example.com/image18.jpg', FALSE, 'Gaming, eSports, VR', 'Published'),
('How to Cook Like a Chef', 'Cooking tips for beginners', 'Cooking is an essential skill...', 1, 'Food', 'https://example.com/image19.jpg', TRUE, 'Cooking, Recipes, Food', 'Published'),
('The Power of Reading', 'Why reading books is important', 'Books are a source of wisdom...', 2, 'Education', 'https://example.com/image20.jpg', FALSE, 'Books, Reading, Learning', 'Published'),
('Machine Learning Explained', 'An introduction to ML', 'Machine learning is transforming...', 3, 'Programming', 'https://example.com/image21.jpg', TRUE, 'ML, AI, Data', 'Draft'),
('How to Stay Productive', 'Boost your daily productivity', 'Staying productive requires...', 1, 'Self-Improvement', 'https://example.com/image22.jpg', FALSE, 'Productivity, Focus, Work', 'Published'),
('How to Learn a New Language', 'Tips for language learners', 'Learning a new language is...', 2, 'Education', 'https://example.com/image23.jpg', TRUE, 'Language, Learning, Skills', 'Published'),
('The Evolution of Social Media', 'How social media has changed', 'From MySpace to TikTok...', 3, 'Technology', 'https://example.com/image24.jpg', FALSE, 'Social Media, Internet, Trends', 'Published'),
('The Rise of Remote Work', 'Working from home in 2025', 'Remote work is now more...', 1, 'Business', 'https://example.com/image25.jpg', TRUE, 'Remote Work, Business', 'Published'),
('How to Build a Website', 'Guide for beginners', 'Creating a website is easier...', 2, 'Programming', 'https://example.com/image26.jpg', FALSE, 'Web Development, HTML, CSS', 'Published'),
('Best Budget Travel Tips', 'Travel cheap and smart', 'Traveling doesn’t have to...', 3, 'Travel', 'https://example.com/image27.jpg', TRUE, 'Budget Travel, Adventure', 'Published'),
('The Science of Happiness', 'What makes us happy?', 'Happiness is a complex emotion...', 1, 'Self-Improvement', 'https://example.com/image28.jpg', FALSE, 'Happiness, Psychology, Science', 'Draft'),
('Introduction to Blockchain', 'Beyond Bitcoin', 'Blockchain is more than...', 2, 'Technology', 'https://example.com/image29.jpg', TRUE, 'Blockchain, Crypto', 'Published'),
('Best Home Workout Routines', 'Stay fit at home', 'No gym? No problem! These...', 3, 'Health', 'https://example.com/image30.jpg', FALSE, 'Fitness, Workout, Home Gym', 'Published');
