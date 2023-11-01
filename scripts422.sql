create table car (
id int primary key,
brand text,
model text,
price int
);

create table person (
  id int primary key,
  namee text,
  agee int,
  have_license bool,
  car_id int,
  foreign key (car_id) references car(id)
);