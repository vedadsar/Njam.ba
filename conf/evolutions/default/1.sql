# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table cart (
  id                        integer not null,
  user_id                   integer,
  paid                      boolean,
  total                     double,
  min_order                 double,
  date                      timestamp,
  restaurant_name           varchar(255),
  constraint pk_cart primary key (id))
;

create table cart_item (
  id                        integer not null,
  cart_id                   integer,
  quantity                  integer,
  total_price               double,
  meal_id                   integer,
  constraint pk_cart_item primary key (id))
;

create table comment (
  author_id                 integer,
  posted_at                 timestamp,
  title                     varchar(255),
  content                   varchar(255))
;

create table faq (
  id                        integer not null,
  question                  varchar(255),
  answer                    TEXT,
  constraint pk_faq primary key (id))
;

create table image (
  id                        integer not null,
  img_location              varchar(255),
  meal_id                   integer,
  restaurant_id             integer,
  constraint pk_image primary key (id))
;

create table location (
  id                        integer not null,
  city                      varchar(255),
  street                    varchar(255),
  number                    varchar(255),
  user_id                   integer,
  constraint pk_location primary key (id))
;

create table meal (
  id                        integer not null,
  name                      varchar(255),
  price                     double,
  description               varchar(255),
  restaurant_id             integer,
  constraint pk_meal primary key (id))
;

create table restaurant (
  id                        integer not null,
  name                      varchar(255),
  date_creation             timestamp,
  user_id                   integer,
  min_order                 double,
  approved_orders           integer,
  refused_orders            integer,
  constraint pk_restaurant primary key (id))
;

create table transaction_u (
  id                        integer not null,
  restaurant_id             integer,
  user_to_pay_id            integer,
  cart_to_pay_id            integer,
  email                     varchar(255),
  price                     double,
  approved                  boolean,
  refused                   boolean,
  constraint pk_transaction_u primary key (id))
;

create table user (
  id                        integer not null,
  email                     varchar(255),
  hashed_password           varchar(255),
  restaurant_id             integer,
  location_id               integer,
  cart_id                   integer,
  confirmation_string       varchar(255),
  validated                 boolean,
  role                      varchar(255),
  date_creation             timestamp not null,
  constraint uq_user_email unique (email),
  constraint pk_user primary key (id))
;

create sequence cart_seq;

create sequence cart_item_seq;

create sequence faq_seq;

create sequence image_seq;

create sequence location_seq;

create sequence meal_seq;

create sequence restaurant_seq;

create sequence transaction_u_seq;

create sequence user_seq;

alter table cart add constraint fk_cart_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_cart_user_1 on cart (user_id);
alter table cart_item add constraint fk_cart_item_cart_2 foreign key (cart_id) references cart (id) on delete restrict on update restrict;
create index ix_cart_item_cart_2 on cart_item (cart_id);
alter table cart_item add constraint fk_cart_item_meal_3 foreign key (meal_id) references meal (id) on delete restrict on update restrict;
create index ix_cart_item_meal_3 on cart_item (meal_id);
alter table comment add constraint fk_comment_author_4 foreign key (author_id) references user (id) on delete restrict on update restrict;
create index ix_comment_author_4 on comment (author_id);
alter table image add constraint fk_image_meal_5 foreign key (meal_id) references meal (id) on delete restrict on update restrict;
create index ix_image_meal_5 on image (meal_id);
alter table image add constraint fk_image_restaurant_6 foreign key (restaurant_id) references restaurant (id) on delete restrict on update restrict;
create index ix_image_restaurant_6 on image (restaurant_id);
alter table location add constraint fk_location_user_7 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_location_user_7 on location (user_id);
alter table meal add constraint fk_meal_restaurant_8 foreign key (restaurant_id) references restaurant (id) on delete restrict on update restrict;
create index ix_meal_restaurant_8 on meal (restaurant_id);
alter table restaurant add constraint fk_restaurant_user_9 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_restaurant_user_9 on restaurant (user_id);
alter table transaction_u add constraint fk_transaction_u_restaurant_10 foreign key (restaurant_id) references restaurant (id) on delete restrict on update restrict;
create index ix_transaction_u_restaurant_10 on transaction_u (restaurant_id);
alter table user add constraint fk_user_restaurant_11 foreign key (restaurant_id) references restaurant (id) on delete restrict on update restrict;
create index ix_user_restaurant_11 on user (restaurant_id);
alter table user add constraint fk_user_location_12 foreign key (location_id) references location (id) on delete restrict on update restrict;
create index ix_user_location_12 on user (location_id);
alter table user add constraint fk_user_cart_13 foreign key (cart_id) references cart (id) on delete restrict on update restrict;
create index ix_user_cart_13 on user (cart_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists cart;

drop table if exists cart_item;

drop table if exists comment;

drop table if exists faq;

drop table if exists image;

drop table if exists location;

drop table if exists meal;

drop table if exists restaurant;

drop table if exists transaction_u;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists cart_seq;

drop sequence if exists cart_item_seq;

drop sequence if exists faq_seq;

drop sequence if exists image_seq;

drop sequence if exists location_seq;

drop sequence if exists meal_seq;

drop sequence if exists restaurant_seq;

drop sequence if exists transaction_u_seq;

drop sequence if exists user_seq;

