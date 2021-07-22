DROP TABLE IF EXISTS audiences, cathedras, groups, lectures, students, subjects, teachers, vacations, holidays, lecture_times,
subjects_teachers, lectures_groups, groups_students, teachers_vacations, cathedras_holidays, cathedras_audiences, cathedras_lecture_times;

--create types
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'gender') THEN
        CREATE TYPE gender AS ENUM ('MALE', 'FEMALE');
    END IF;
    --more types here...
END$$;



CREATE TABLE IF NOT EXISTS audiences (
	id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	room int NOT NULL,
	capacity int NOT NULL
);

CREATE TABLE IF NOT EXISTS cathedras (
	id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS groups (
	id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name VARCHAR(10) NOT NULL,
	cathedra_id  int  NOT NULL,
	foreign key (cathedra_id) REFERENCES cathedras(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS students (
	id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	first_name VARCHAR(50) NOT NULL,
	last_name VARCHAR(50) NOT NULL,
	phone VARCHAR(50),
	address VARCHAR(50) NOT NULL,
	email VARCHAR(50),
	gender gender,
	postalCode int,
	education  VARCHAR(50) NOT NULL,
	birthDate TIMESTAMP NOT NULL,
	group_id int,
	foreign key (group_id) REFERENCES groups(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS groups_students (
	group_id int NOT NULL,
	student_id int NOT NULL,
	foreign key (group_id) REFERENCES groups(id) ON DELETE CASCADE,
	foreign key (student_id) REFERENCES students(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS subjects (
	id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	desctiption VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS teachers (
	id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	first_name VARCHAR(50) NOT NULL,
	last_name VARCHAR(50) NOT NULL,
	phone VARCHAR(50),
	address VARCHAR(50) NOT NULL,
	email VARCHAR(50),
	gender gender,
	postalCode int,
	education  VARCHAR(50) NOT NULL,
	birthDate TIMESTAMP NOT NULL,
	cathedra_id int NOT NULL,
	foreign key (cathedra_id) REFERENCES cathedras(id) ON DELETE CASCADE,
	degree VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS lecture_times (
	id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	start TIMESTAMP NOT NULL,
	finish TIMESTAMP NOT NULL,
);

CREATE TABLE IF NOT EXISTS lectures(
	id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	teacher_id int NOT NULL,
	foreign key (teacher_id) REFERENCES teachers(id) ON DELETE CASCADE,
	audience_id int NOT NULL,
	foreign key (audience_id) REFERENCES audiences(id) ON DELETE CASCADE,
	date TIMESTAMP NOT NULL,
	subject_id int NOT NULL,
	foreign key (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
	lecture_time_id int NOT NULL,
	foreign key (lecture_time_id) REFERENCES lecture_times(id) ON DELETE CASCADE
);

--lectures have list<Group>
CREATE TABLE IF NOT EXISTS lectures_groups (
	group_id int NOT NULL,
	lecture_id int NOT NULL,
	foreign key (group_id) REFERENCES groups(id) ON DELETE CASCADE,
	foreign key (lecture_id) REFERENCES lectures(id) ON DELETE CASCADE
);

--many-to-many
CREATE TABLE IF NOT EXISTS subjects_teachers (
	subject_id int NOT NULL,
	teacher_id int NOT NULL,
	foreign key (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
	foreign key (teacher_id) REFERENCES teachers(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS vacations (
	id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	start TIMESTAMP NOT NULL,
	finish TIMESTAMP NOT NULL
);

--teachers have List<Vacation>
CREATE TABLE IF NOT EXISTS teachers_vacations (
	vacation_id int NOT NULL,
	teacher_id int NOT NULL,
	foreign key (vacation_id) REFERENCES vacations(id) ON DELETE CASCADE,
	foreign key (teacher_id) REFERENCES teachers(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS holidays (
	id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	date TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS cathedras_holidays (
	cathedra_id int NOT NULL,
	holiday_id int NOT NULL,
	foreign key (cathedra_id) REFERENCES cathedras(id) ON DELETE CASCADE,
	foreign key (holiday_id) REFERENCES holidays(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS cathedras_lecture_times (
	cathedra_id int NOT NULL,
	lecture_time_id int NOT NULL,
	foreign key (cathedra_id) REFERENCES cathedras(id) ON DELETE CASCADE,
	foreign key (lecture_time_id) REFERENCES lecture_times(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS cathedras_audiences (
	cathedra_id int NOT NULL,
	audience_id int NOT NULL,
	foreign key (cathedra_id) REFERENCES cathedras(id) ON DELETE CASCADE,
	foreign key (audience_id) REFERENCES audiences(id) ON DELETE CASCADE
);