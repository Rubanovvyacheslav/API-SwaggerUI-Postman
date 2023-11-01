select *
from student s
join faculty f on s.faculty_id = f.id;

select s.*
from student s
join avatar a on s.id = a.student_id;