-- Users (mix of regular users and admins)
INSERT INTO users (username, password, profile_picture_link, role) VALUES
('alice_wonder', '$2a$10$q/YZmCbwU51.1nzs7W61h.vgNJpnu1zri/npngyYeVelprMfJVsYe', 'profile_image_link_1', 'ADMIN'),
('bob_builder', '$2a$10$q/YZmCbwU51.1nzs7W61h.vgNJpnu1zri/npngyYeVelprMfJVsYe', 'profile_image_link_2', 'USER'),
('carol_smith', '$2a$10$q/YZmCbwU51.1nzs7W61h.vgNJpnu1zri/npngyYeVelprMfJVsYe', 'profile_image_link_3', 'USER'),
('dave_jones', '$2a$10$q/YZmCbwU51.1nzs7W61h.vgNJpnu1zri/npngyYeVelprMfJVsYe', NULL, 'USER'),
('eve_admin', '$2a$10$q/YZmCbwU51.1nzs7W61h.vgNJpnu1zri/npngyYeVelprMfJVsYe', 'profile_image_link_5', 'ADMIN'),
('frank_castle', '$2a$10$q/YZmCbwU51.1nzs7W61h.vgNJpnu1zri/npngyYeVelprMfJVsYe', NULL, 'USER'),
('miquel_lol', '$2a$10$q/YZmCbwU51.1nzs7W61h.vgNJpnu1zri/npngyYeVelprMfJVsYe', NULL, 'USER'),
('albert_einstein', '$2a$10$q/YZmCbwU51.1nzs7W61h.vgNJpnu1zri/npngyYeVelprMfJVsYe', NULL, 'USER'),
('benjamin_frank', '$2a$10$q/YZmCbwU51.1nzs7W61h.vgNJpnu1zri/npngyYeVelprMfJVsYe', NULL, 'USER'),
('frank_castle2', '$2a$10$q/YZmCbwU51.1nzs7W61h.vgNJpnu1zri/npngyYeVelprMfJVsYe', NULL, 'USER');
-- Posts (some with images, some text-only, one soft-deleted)
INSERT INTO posts (creation_timestamp, deletion_timestamp, text_content, like_count, user_id) VALUES
('2025-01-10 08:30:00', NULL,'Just joined this platform, excited to be here!', 10, 1),
('2025-01-11 10:15:00', NULL,'Working on a new project today, wish me luck!', 3, 2),
('2025-01-11 12:00:00', NULL,'Beautiful morning for a walk in the park.', 5, 3),
('2025-01-12 09:45:00', NULL,'Anyone else think that coffee is life?', 4, 4),
('2025-01-12 14:20:00', NULL,'Hot take: tabs are better than spaces. Fight me.', 5, 1),
('2025-01-13 11:00:00', '2025-01-13 18:00:00','This post was removed by the author.', 0, 2),
('2025-01-14 16:30:00', NULL,'Sharing some thoughts on software architecture patterns.', 4, 5),
('2025-01-15 08:00:00', NULL,'Good morning everyone! Hope your day is productive.', 4, 6);

-- Comments (some posts have multiple, deleted post has none)
INSERT INTO comments (creation_timestamp, deletion_timestamp, text_content, user_id, post_id) VALUES
('2025-01-10 09:00:00', NULL, 'Welcome! You are going to love it here.', 2, 1),
('2025-01-10 09:30:00', NULL, 'Glad to have you!', 3, 1),
('2025-01-10 09:30:00', NULL, 'Hello!', 4, 1),
('2025-01-10 09:30:00', NULL, 'Hi', 5, 1),
('2025-01-10 09:30:00', NULL, 'Hi have good day', 6, 1),
('2025-01-10 09:30:00', NULL, 'Nice to see you Alice!', 7, 1),
('2025-01-10 09:30:00', NULL, 'Welcome to the spark app', 8, 1),
('2025-01-10 09:30:00', NULL, 'Hello', 9, 1),
('2025-01-10 09:30:00', NULL, 'Good to see our community grow, welcome!', 10, 1),
('2025-01-11 10:45:00', NULL, 'Good luck, what are you building?', 1, 2),
('2025-01-11 11:00:00', NULL, 'Hope it goes well!', 4, 2),
('2025-01-11 12:30:00', NULL, 'Looks lovely, where was this taken?', 6, 3),
('2025-01-12 10:00:00', NULL, 'Absolutely, coffee is essential.', 1, 4),
('2025-01-12 15:00:00', NULL, 'Hard disagree, spaces all the way!', 3, 5),
('2025-01-12 15:10:00', NULL, 'Tabs gang rise up.', 4, 5),
('2025-01-12 15:20:00', '2025-01-12 16:00:00', 'This comment was deleted.', 2, 5),
('2025-01-14 17:00:00', NULL, 'Great post, very insightful.', 2, 7),
('2025-01-15 08:30:00', NULL, 'You too, have a great day!', 1, 8);

-- Reactions (one like per user per post, matches like_counts above)
INSERT INTO reactions (reaction, user_id, post_id) VALUES
('LIKE', 1, 1), ('LIKE', 2, 1), ('LIKE', 3, 1), ('LIKE', 4, 1), ('LIKE', 5, 1), ('LIKE', 6, 1), ('LIKE',7,1), ('LIKE',8,1), ('LIKE',9,1), ('LIKE',10,1), -- post-1: 10
('LIKE', 1, 2), ('LIKE', 4, 2), ('LIKE', 6, 2), -- post 2: 3 LIKEs
('LIKE', 1, 3), ('LIKE', 2, 3), ('LIKE', 4, 3), ('LIKE', 5, 3), ('LIKE', 6, 3), -- post-3: 5 LIKEs
('LIKE', 1, 4), ('LIKE', 2, 4), ('LIKE', 3, 4), ('LIKE', 5, 4), -- post-4: 4 LIKEs (of 8)
('LIKE', 2, 5), ('LIKE', 3, 5), ('LIKE', 4, 5), ('LIKE', 5, 5), ('LIKE', 6, 5), -- post-5: 5 LIKEs
('LIKE', 1, 7), ('LIKE', 2, 7), ('LIKE', 4, 7), ('LIKE', 6, 7), -- post-7: 4 LIKEs
('LIKE', 2, 8), ('LIKE', 3, 8), ('LIKE', 5, 8), ('LIKE', 6, 8); -- post-8: 4 LIKEs
