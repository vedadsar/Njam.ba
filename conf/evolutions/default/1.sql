# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table comment (
  author_id                 integer,
  posted_at                 timestamp,
  title                     varchar(255),
  content                   varchar(255))
;

create table location (
  id                        integer not null,
  city                      varchar(255),
  street                    varchar(255),
  number                    varchar(255),
  restaurant_id             integer,
  user_id                   integer,
  constraint pk_location primary key (id))
;

create table meal (
  id                        integer not null,
  name                      varchar(255),
  price                     double,
  restaurant_id             integer,
  constraint pk_meal primary key (id))
;

create table restaurant (
  id                        integer not null,
  name                      varchar(255),
  date_creation             timestamp,
  user_id                   integer,
  constraint pk_restaurant primary key (id))
;

create table user (
  id                        integer not null,
  email                     varchar(255),
  hashed_password           varchar(255),
  restaurant_id             integer,
  location_id               integer,
  confirmation_string       varchar(255),
  validated                 boolean,
  role                      varchar(255),
  date_creation             timestamp not null,
  constraint uq_user_email unique (email),
  constraint pk_user primary key (id))
;

create sequence location_seq;

create sequence meal_seq;

create sequence restaurant_seq;

create sequence user_seq;

alter table comment add constraint fk_comment_author_1 foreign key (author_id) references user (id) on delete restrict on update restrict;
create index ix_comment_author_1 on comment (author_id);
alter table location add constraint fk_location_restaurant_2 foreign key (restaurant_id) references restaurant (id) on delete restrict on update restrict;
create index ix_location_restaurant_2 on location (restaurant_id);
alter table location add constraint fk_location_user_3 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_location_user_3 on location (user_id);
alter table meal add constraint fk_meal_restaurant_4 foreign key (restaurant_id) references restaurant (id) on delete restrict on update restrict;
create index ix_meal_restaurant_4 on meal (restaurant_id);
alter table restaurant add constraint fk_restaurant_user_5 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_restaurant_user_5 on restaurant (user_id);
alter table user add constraint fk_user_restaurant_6 foreign key (restaurant_id) references restaurant (id) on delete restrict on update restrict;
create index ix_user_restaurant_6 on user (restaurant_id);
alter table user add constraint fk_user_location_7 foreign key (location_id) references location (id) on delete restrict on update restrict;
create index ix_user_location_7 on user (location_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists comment;

drop table if exists location;

drop table if exists meal;

drop table if exists restaurant;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists location_seq;

drop sequence if exists meal_seq;

drop sequence if exists restaurant_seq;

drop sequence if exists user_seq;

