--Возраст студента не может быть меньше 16 лет.
alter table student
add constraint student_age_constraint check(age > 15);

--Имена студентов должны быть уникальными и не равны нулю.
alter table student
add constraint student_name_constraint unique(name);

alter table student
alter column name set not null;

--Пара “значение названия” - “цвет факультета” должна быть уникальной.
alter table faculty
add constraint faculty_name_and_color_constraint unique(name, color);

--При создании студента без возраста ему автоматически должно присваиваться 20 лет.
alter table student
alter column age set default 20;