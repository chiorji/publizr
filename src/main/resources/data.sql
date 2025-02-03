INSERT INTO users (username, email, password, role) VALUES ('bright', 'bright@orji.com', 'password', 1);
INSERT INTO users (username, email, password, role) VALUES ('john', 'john@yopmail.com', 'password', 2);
INSERT INTO users (username, email, password, role) VALUES ('jane', 'jane@yopmail.com', 'password', 3);

INSERT INTO posts (title, excerpt, content, author_id, category, image_url, featured, tags, status) VALUES (
'Getting Started with React: A Beginners Guide',
'Learn the fundamentals of React, from components to hooks. This comprehensive guide will take you from novice to confident React developer.',
'This is the content of the post.',
1,
'technology',
'https://unsplash.com/photos/the-sun-is-setting-under-a-pier-at-the-beach-ZuLQbR3-BUk',
true,
'react, javaScript, beginner',
'published'
);
