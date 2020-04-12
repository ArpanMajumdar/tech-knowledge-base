# Create a new keyspace
CREATE KEYSPACE IF NOT EXISTS essentials
WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

# Use a keyspace
USE essentials;

# Create table
CREATE TABLE movies (
  movie_id UUID,
  title TEXT,
  release_year INT,
  PRIMARY KEY ((movie_id))
);

# Insert some records
insert into movies (movie_id, release_year, title) values (uuid(), 2011, 'Tree of life');
insert into movies (movie_id, release_year, title) values (uuid(), 2016, 'La la land');
insert into movies (movie_id, release_year, title) values (uuid(), 2011, 'Birdman');

# Show all records
select * from movies; 

# Create movies_by_actor table
CREATE TABLE movies_by_actor (
  actor TEXT,
  release_year INT,
  movie_id UUID,
  title TEXT,
  genres SET<TEXT>,
  rating FLOAT,
  PRIMARY KEY ((actor), release_year, movie_id)
) WITH CLUSTERING ORDER BY (release_year DESC, movie_id ASC);

# Insert some rows
insert into movies_by_actor (actor, release_year, movie_id, title, genres, rating) 
values ('Emma Stone', 2016, 07b53137-c0e5-4372-8406-0c875d31d8ec, 'La la land', {'musical', 'drama'}, 10);

insert into movies_by_actor (actor, release_year, movie_id, title, genres, rating) 
values ('Brad Pitt', 2011, 140247d4-d2bc-4480-84e5-659c9a7a07e7, 'Tree of life', {'drama'}, 10);

insert into movies_by_actor (actor, release_year, movie_id, title, genres, rating) 
values ('Emma Stone', 2011, 31d20d85-6a67-4939-bdfb-cc43f162e56e, 'Birdman', {'drama'}, 10);

# Querying the table using key
select * from movies_by_actor where actor = 'Emma Stone';
select * from movies_by_actor where actor = 'Emma Stone' and release_year > 2015;
# This throws error as rating is not included in the primary key
select * from movies_by_actor where actor = 'Emma Stone' and release_year > 2015 and rating < 7; 

# Create a custom SASI index for like queries
CREATE CUSTOM INDEX title ON movies_by_actor (title) 
USING 'org.apache.cassandra.index.sasi.SASIIndex'
WITH OPTIONS = {'mode' : 'contains'};

# Create a materialized view
CREATE MATERIALIZED VIEW movies_mv AS
SELECT title, release_year, movie_id, genres, actor
FROM movies_by_actor
WHERE title IS NOT NULL 
AND release_year IS NOT NULL
AND movie_id IS NOT NULL
AND actor IS NOT NULL
PRIMARY KEY ((title, release_year), actor, movie_id);

# Query materialized view 
select * from movies_mv 
where title = 'Birdman' 
and release_year = 2011;