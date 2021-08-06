INSERT INTO cathedras(name) VALUES ('Fantastic Cathedra');

INSERT INTO groups(name, cathedra_id) VALUES ('Killers', (SELECT id FROM cathedras WHERE name = 'Fantastic Cathedra'));
INSERT INTO groups(name, cathedra_id) VALUES ('Mages', (SELECT id FROM cathedras WHERE name = 'Fantastic Cathedra'));

INSERT INTO students(first_name, last_name, phone, address, email, gender, postalCode, education, birthDate, group_id)
VALUES ('Petr', 'Orlov', '888005353535', 'Empty Street 8', '1@owl.com', 'MALE', '999', 'General secondary education', '1994-3-3', (SELECT id FROM groups WHERE name = 'Killers'));
INSERT INTO students(first_name, last_name, phone, address, email, gender, postalCode, education, birthDate, group_id) 
VALUES ('Oleg', 'Krasnov', '2247582', 'Empty Street 8-2', '2@owl.com', 'MALE', '999', 'General secondary education', '1994-5-13', (SELECT id FROM groups WHERE name = 'Killers'));
INSERT INTO students(first_name, last_name, phone, address, email, gender, postalCode, education, birthDate, group_id)
VALUES ('Margot', 'Robbie', '9999999999', 'Holywood Street 1', '3@owl.com', 'FEMALE', '254826', 'General tecnical education', '1990-2-7', (SELECT id FROM groups WHERE name = 'Killers'));
INSERT INTO students(first_name, last_name, phone, address, email, gender, postalCode, education, birthDate, group_id)
VALUES ('Kim', 'Cattrall', '312-555-0690:00', 'Virtual Reality Capsule no 2', '4@owl.com','FEMALE', '12345', 'College education', '1956-8-21', (SELECT id FROM groups WHERE name = 'Mages'));
INSERT INTO students(first_name, last_name, phone, address, email, gender, postalCode, education, birthDate, group_id)
VALUES ('Thomas', 'Anderson', '312-555-5555', 'Virtual Reality Capsule no 3', '5@owl.com', 'MALE', '12345', 'College education', '1962-3-11', (SELECT id FROM groups WHERE name = 'Mages'));

INSERT INTO teachers(first_name, last_name, phone, address, email, gender, postalCode, education, birthDate, cathedra_id, degree)
VALUES('Daniel', 'Morpheus', '1', 'Virtual Reality Capsule no 1', '1@bigowl.com', 'MALE', '12345', 'Higher education', '1970-01-01', (SELECT id FROM cathedras WHERE name = 'Fantastic Cathedra'), 'PROFESSOR');
INSERT INTO teachers(first_name, last_name, phone, address, email, gender, postalCode, education, birthDate, cathedra_id, degree)
VALUES('Bane', 'Smith', '1', 'Virtual Reality', '0@bigowl.com', 'MALE', 'none', 'none', '1970-01-01', (SELECT id FROM cathedras WHERE name = 'Fantastic Cathedra'), 'PROFESSOR');

INSERT INTO subjects(name, description, cathedra_id) 
VALUES('Weapon Tactics', 'Learning how to use heavy weapon and guerrilla tactics', (SELECT id FROM cathedras WHERE name = 'Fantastic Cathedra'));
INSERT INTO subjects(name, description, cathedra_id) 
VALUES('Wandless Magic', 'Learning how to use spells without magic wand', (SELECT id FROM cathedras WHERE name = 'Fantastic Cathedra'));
INSERT INTO subjects(name, description, cathedra_id) 
VALUES('Universal language', 'Learning the universal language established by the Federation', (SELECT id FROM cathedras WHERE name = 'Fantastic Cathedra'));

INSERT INTO subjects_teachers (subject_id, teacher_id) 
VALUES((SELECT id FROM subjects WHERE name = 'Weapon Tactics'), (SELECT id FROM teachers WHERE first_name = 'Daniel' AND last_name = 'Morpheus'));
INSERT INTO subjects_teachers (subject_id, teacher_id) 
VALUES((SELECT id FROM subjects WHERE name = 'Wandless Magic'), (SELECT id FROM teachers WHERE first_name = 'Bane' AND last_name = 'Smith'));
INSERT INTO subjects_teachers (subject_id, teacher_id) 
VALUES((SELECT id FROM subjects WHERE name = 'Universal language'), (SELECT id FROM teachers WHERE first_name = 'Bane' AND last_name = 'Smith'));

INSERT INTO vacations(start, finish, teacher_id) VALUES('2021-1-15', '2021-1-29', (SELECT id FROM teachers WHERE first_name = 'Daniel' AND last_name = 'Morpheus'));
INSERT INTO vacations(start, finish, teacher_id) VALUES('2021-6-15', '2021-6-29', (SELECT id FROM teachers WHERE first_name = 'Daniel' AND last_name = 'Morpheus'));
INSERT INTO vacations(start, finish, teacher_id) VALUES('2021-3-15', '2021-3-29', (SELECT id FROM teachers WHERE first_name = 'Bane' AND last_name = 'Smith'));
INSERT INTO vacations(start, finish, teacher_id) VALUES('2021-7-15', '2021-7-29', (SELECT id FROM teachers WHERE first_name = 'Bane' AND last_name = 'Smith'));

INSERT INTO lecture_times(start, finish) VALUES('08:00:00', '09:30:00');
INSERT INTO lecture_times(start, finish) VALUES('09:40:00', '11:10:00');
INSERT INTO lecture_times(start, finish) VALUES('11:20:00', '12:50:00');
INSERT INTO lecture_times(start, finish) VALUES('13:20:00', '14:50:00');
INSERT INTO lecture_times(start, finish) VALUES('15:00:00', '16:30:00');
INSERT INTO lecture_times(start, finish) VALUES('16:40:00', '18:10:00');
INSERT INTO lecture_times(start, finish) VALUES('18:20:00', '19:50:00');
INSERT INTO lecture_times(start, finish) VALUES('20:00:00', '21:30:00');

INSERT INTO audiences(room, capacity, cathedra_id) VALUES(1, 10, 1);
INSERT INTO audiences(room, capacity, cathedra_id) VALUES(2, 30, 1);
INSERT INTO audiences(room, capacity, cathedra_id) VALUES(3, 10, 1);

--Monday - wt and ul - only killers
INSERT INTO lectures(cathedra_id, subject_id, date, lecture_time_id, audience_id, teacher_id) 
VALUES((SELECT id FROM cathedras WHERE name = 'Fantastic Cathedra'), (SELECT id FROM subjects WHERE name = 'Weapon Tactics'), '2021-4-4', 1, (SELECT id FROM audiences WHERE room = 1), (SELECT id FROM teachers WHERE first_name = 'Daniel' AND last_name = 'Morpheus'));
INSERT INTO lectures(cathedra_id, subject_id, date, lecture_time_id, audience_id, teacher_id) 
VALUES((SELECT id FROM cathedras WHERE name = 'Fantastic Cathedra'), (SELECT id FROM subjects WHERE name = 'Weapon Tactics'), '2021-4-4', 2, (SELECT id FROM audiences WHERE room = 1), (SELECT id FROM teachers WHERE first_name = 'Daniel' AND last_name = 'Morpheus'));
INSERT INTO lectures(cathedra_id, subject_id, date, lecture_time_id, audience_id, teacher_id) 
VALUES((SELECT id FROM cathedras WHERE name = 'Fantastic Cathedra'), (SELECT id FROM subjects WHERE name = 'Universal language'), '2021-4-4', 3, (SELECT id FROM audiences WHERE room = 3), (SELECT id FROM teachers WHERE first_name = 'Bane' AND last_name = 'Smith'));
INSERT INTO lectures(cathedra_id, subject_id, date, lecture_time_id, audience_id, teacher_id) 
VALUES((SELECT id FROM cathedras WHERE name = 'Fantastic Cathedra'), (SELECT id FROM subjects WHERE name = 'Universal language'), '2021-4-4', 4, (SELECT id FROM audiences WHERE room = 3), (SELECT id FROM teachers WHERE first_name = 'Bane' AND last_name = 'Smith'));
--Wednesday - wm - only mages
INSERT INTO lectures(cathedra_id, subject_id, date, lecture_time_id, audience_id, teacher_id) 
VALUES((SELECT id FROM cathedras WHERE name = 'Fantastic Cathedra'), (SELECT id FROM subjects WHERE name = 'Wandless Magic'), '2021-4-6', 2, (SELECT id FROM audiences WHERE room = 2), (SELECT id FROM teachers WHERE first_name = 'Bane' AND last_name = 'Smith'));
INSERT INTO lectures(cathedra_id, subject_id, date, lecture_time_id, audience_id, teacher_id) 
VALUES((SELECT id FROM cathedras WHERE name = 'Fantastic Cathedra'), (SELECT id FROM subjects WHERE name = 'Wandless Magic'), '2021-4-6', 3, (SELECT id FROM audiences WHERE room = 2), (SELECT id FROM teachers WHERE first_name = 'Bane' AND last_name = 'Smith'));
INSERT INTO lectures(cathedra_id, subject_id, date, lecture_time_id, audience_id, teacher_id) 
VALUES((SELECT id FROM cathedras WHERE name = 'Fantastic Cathedra'), (SELECT id FROM subjects WHERE name = 'Wandless Magic'), '2021-4-6', 4, (SELECT id FROM audiences WHERE room = 2), (SELECT id FROM teachers WHERE first_name = 'Bane' AND last_name = 'Smith'));
--Friday - ul for mages, wt for killers
INSERT INTO lectures(cathedra_id, subject_id, date, lecture_time_id, audience_id, teacher_id) 
VALUES((SELECT id FROM cathedras WHERE name = 'Fantastic Cathedra'), (SELECT id FROM subjects WHERE name = 'Universal language'), '2021-4-8', 2, (SELECT id FROM audiences WHERE room = 3), (SELECT id FROM teachers WHERE first_name = 'Bane' AND last_name = 'Smith'));
INSERT INTO lectures(cathedra_id, subject_id, date, lecture_time_id, audience_id, teacher_id) 
VALUES((SELECT id FROM cathedras WHERE name = 'Fantastic Cathedra'), (SELECT id FROM subjects WHERE name = 'Universal language'), '2021-4-8', 3, (SELECT id FROM audiences WHERE room = 3), (SELECT id FROM teachers WHERE first_name = 'Bane' AND last_name = 'Smith'));
INSERT INTO lectures(cathedra_id, subject_id, date, lecture_time_id, audience_id, teacher_id) 
VALUES((SELECT id FROM cathedras WHERE name = 'Fantastic Cathedra'), (SELECT id FROM subjects WHERE name = 'Weapon Tactics'), '2021-4-8', 4, (SELECT id FROM audiences WHERE room = 1), (SELECT id FROM teachers WHERE first_name = 'Daniel' AND last_name = 'Morpheus'));
INSERT INTO lectures(cathedra_id, subject_id, date, lecture_time_id, audience_id, teacher_id) 
VALUES((SELECT id FROM cathedras WHERE name = 'Fantastic Cathedra'), (SELECT id FROM subjects WHERE name = 'Weapon Tactics'), '2021-4-8', 5, (SELECT id FROM audiences WHERE room = 1), (SELECT id FROM teachers WHERE first_name = 'Daniel' AND last_name = 'Morpheus'));

INSERT INTO lectures_groups(lecture_id, group_id) VALUES(1, (SELECT id FROM groups WHERE name = 'Killers'));
INSERT INTO lectures_groups(lecture_id, group_id) VALUES(2, (SELECT id FROM groups WHERE name = 'Killers'));
INSERT INTO lectures_groups(lecture_id, group_id) VALUES(10, (SELECT id FROM groups WHERE name = 'Killers'));
INSERT INTO lectures_groups(lecture_id, group_id) VALUES(11, (SELECT id FROM groups WHERE name = 'Killers'));
INSERT INTO lectures_groups(lecture_id, group_id) VALUES(3, (SELECT id FROM groups WHERE name = 'Killers'));
INSERT INTO lectures_groups(lecture_id, group_id) VALUES(4, (SELECT id FROM groups WHERE name = 'Killers'));
INSERT INTO lectures_groups(lecture_id, group_id) VALUES(8, (SELECT id FROM groups WHERE name = 'Killers'));
INSERT INTO lectures_groups(lecture_id, group_id) VALUES(9, (SELECT id FROM groups WHERE name = 'Killers'));
INSERT INTO lectures_groups(lecture_id, group_id) VALUES(3, (SELECT id FROM groups WHERE name = 'Mages'));
INSERT INTO lectures_groups(lecture_id, group_id) VALUES(4, (SELECT id FROM groups WHERE name = 'Mages'));
INSERT INTO lectures_groups(lecture_id, group_id) VALUES(8, (SELECT id FROM groups WHERE name = 'Mages'));
INSERT INTO lectures_groups(lecture_id, group_id) VALUES(9, (SELECT id FROM groups WHERE name = 'Mages'));
INSERT INTO lectures_groups(lecture_id, group_id) VALUES(5, (SELECT id FROM groups WHERE name = 'Mages'));
INSERT INTO lectures_groups(lecture_id, group_id) VALUES(6, (SELECT id FROM groups WHERE name = 'Mages'));		
INSERT INTO lectures_groups(lecture_id, group_id) VALUES(7, (SELECT id FROM groups WHERE name = 'Mages'));		
			
INSERT INTO holidays(name, date, cathedra_id) VALUES('Christmas', '2021-12-25', '1');
INSERT INTO holidays(name, date, cathedra_id) VALUES('Thanksgiving', '2021-11-22', '1');
INSERT INTO holidays(name, date, cathedra_id) VALUES('Decoration Day', '2021-5-31', '1');
INSERT INTO holidays(name, date, cathedra_id) VALUES('Independence Day', '2021-7-4', '1');
INSERT INTO holidays(name, date, cathedra_id) VALUES('Labor Day', '2021-9-6', '1');
INSERT INTO holidays(name, date, cathedra_id) VALUES('New Year', '2021-1-1', '1');