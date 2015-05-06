# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table blogger (
  id                        integer not null,
  title                     varchar(255),
  subtitle                  varchar(255),
  content                   TEXT,
  image                     varchar(255),
  date                      timestamp,
  tag                       varchar(255),
  creator_id                integer not null,
  constraint pk_blogger primary key (id))
;

create table cart (
  id                        integer not null,
  user_id                   integer,
  paid                      boolean,
  ordered                   boolean,
  timed_out                 boolean,
  empty                     boolean,
  total                     double,
  min_order                 double,
  location                  varchar(255),
  date                      timestamp,
  order_note                varchar(255),
  restaurant_name           varchar(255),
  constraint pk_cart primary key (id))
;

create table cart_item (
  id                        integer not null,
  cart_id                   integer,
  transaction_id            integer,
  quantity                  integer,
  total_price               double,
  meal_id                   integer,
  constraint pk_cart_item primary key (id))
;

create table comment (
  id                        integer not null,
  author_id                 integer,
  date_creation             timestamp,
  title                     varchar(255),
  content                   varchar(255),
  rating                    integer,
  meal_id                   integer,
  constraint pk_comment primary key (id))
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
  category                  varchar(255),
  restaurant_id             integer,
  constraint pk_meal primary key (id))
;

create table meta_item (
  id                        integer not null,
  transaction_id            integer,
  name                      varchar(255),
  price                     double,
  quantity                  integer,
  total_price               double,
  constraint pk_meta_item primary key (id))
;

create table newsletter (
  id                        integer not null,
  confirmation_string       varchar(255),
  email                     varchar(255),
  user_id                   integer,
  validated                 boolean,
  date_creation             timestamp not null,
  constraint pk_newsletter primary key (id))
;

create table pin (
  id                        integer not null,
  pin                       varchar(255),
  date                      timestamp,
  user_id                   integer,
  validated                 boolean,
  constraint uq_pin_pin unique (pin),
  constraint pk_pin primary key (id))
;

create table restaurant (
  id                        integer not null,
  name                      varchar(255),
  date_creation             timestamp,
  working_time              varchar(255),
  user_id                   integer,
  statistic_id              integer,
  min_order                 double,
  approved_orders           integer,
  refused_orders            integer,
  constraint pk_restaurant primary key (id))
;

create table statistics (
  id                        integer not null,
  number_of_visits          integer,
  items_bought              integer,
  constraint pk_statistics primary key (id))
;

create table transaction_u (
  id                        integer not null,
  restaurant_id             integer,
  context_to_pay            varchar(255),
  payment_to_pay            varchar(255),
  payment_execution_to_pay  varchar(255),
  user_to_pay_id            integer,
  cart_to_pay_id            integer,
  email                     varchar(255),
  price                     double,
  token                     varchar(255),
  approved                  boolean,
  refused                   boolean,
  delivery_time             bigint,
  order_time_gap            bigint,
  late_time                 bigint,
  refund                    boolean,
  constraint pk_transaction_u primary key (id))
;

create table user (
  id                        integer not null,
  email                     varchar(255),
  username                  varchar(255),
  hashed_password           varchar(255),
  restaurant_id             integer,
  cart_id                   integer,
  confirmation_string       varchar(255),
  validated                 boolean,
  phone                     varchar(255),
  role                      varchar(255),
  date_creation             timestamp not null,
  constraint uq_user_email unique (email),
  constraint uq_user_username unique (username),
  constraint uq_user_phone unique (phone),
  constraint pk_user primary key (id))
;

create sequence blogger_seq;

create sequence cart_seq;

create sequence cart_item_seq;

create sequence comment_seq;

create sequence faq_seq;

create sequence image_seq;

create sequence location_seq;

create sequence meal_seq;

create sequence meta_item_seq;

create sequence newsletter_seq;

create sequence pin_seq;

create sequence restaurant_seq;

create sequence statistics_seq;

create sequence transaction_u_seq;

create sequence user_seq;

alter table blogger add constraint fk_blogger_creator_1 foreign key (creator_id) references user (id) on delete restrict on update restrict;
create index ix_blogger_creator_1 on blogger (creator_id);
alter table cart add constraint fk_cart_user_2 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_cart_user_2 on cart (user_id);
alter table cart_item add constraint fk_cart_item_cart_3 foreign key (cart_id) references cart (id) on delete restrict on update restrict;
create index ix_cart_item_cart_3 on cart_item (cart_id);
alter table cart_item add constraint fk_cart_item_transaction_4 foreign key (transaction_id) references transaction_u (id) on delete restrict on update restrict;
create index ix_cart_item_transaction_4 on cart_item (transaction_id);
alter table cart_item add constraint fk_cart_item_meal_5 foreign key (meal_id) references meal (id) on delete restrict on update restrict;
create index ix_cart_item_meal_5 on cart_item (meal_id);
alter table comment add constraint fk_comment_author_6 foreign key (author_id) references user (id) on delete restrict on update restrict;
create index ix_comment_author_6 on comment (author_id);
alter table comment add constraint fk_comment_meal_7 foreign key (meal_id) references meal (id) on delete restrict on update restrict;
create index ix_comment_meal_7 on comment (meal_id);
alter table image add constraint fk_image_meal_8 foreign key (meal_id) references meal (id) on delete restrict on update restrict;
create index ix_image_meal_8 on image (meal_id);
alter table image add constraint fk_image_restaurant_9 foreign key (restaurant_id) references restaurant (id) on delete restrict on update restrict;
create index ix_image_restaurant_9 on image (restaurant_id);
alter table location add constraint fk_location_user_10 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_location_user_10 on location (user_id);
alter table meal add constraint fk_meal_restaurant_11 foreign key (restaurant_id) references restaurant (id) on delete restrict on update restrict;
create index ix_meal_restaurant_11 on meal (restaurant_id);
alter table meta_item add constraint fk_meta_item_transaction_12 foreign key (transaction_id) references transaction_u (id) on delete restrict on update restrict;
create index ix_meta_item_transaction_12 on meta_item (transaction_id);
alter table newsletter add constraint fk_newsletter_user_13 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_newsletter_user_13 on newsletter (user_id);
alter table pin add constraint fk_pin_user_14 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_pin_user_14 on pin (user_id);
alter table restaurant add constraint fk_restaurant_user_15 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_restaurant_user_15 on restaurant (user_id);
alter table restaurant add constraint fk_restaurant_statistic_16 foreign key (statistic_id) references statistics (id) on delete restrict on update restrict;
create index ix_restaurant_statistic_16 on restaurant (statistic_id);
alter table transaction_u add constraint fk_transaction_u_restaurant_17 foreign key (restaurant_id) references restaurant (id) on delete restrict on update restrict;
create index ix_transaction_u_restaurant_17 on transaction_u (restaurant_id);
alter table user add constraint fk_user_restaurant_18 foreign key (restaurant_id) references restaurant (id) on delete restrict on update restrict;
create index ix_user_restaurant_18 on user (restaurant_id);
alter table user add constraint fk_user_cart_19 foreign key (cart_id) references cart (id) on delete restrict on update restrict;
create index ix_user_cart_19 on user (cart_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists blogger;

drop table if exists cart;

drop table if exists cart_item;

drop table if exists comment;

drop table if exists faq;

drop table if exists image;

drop table if exists location;

drop table if exists meal;

drop table if exists meta_item;

drop table if exists newsletter;

drop table if exists pin;

drop table if exists restaurant;

drop table if exists statistics;

drop table if exists transaction_u;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists blogger_seq;

drop sequence if exists cart_seq;

drop sequence if exists cart_item_seq;

drop sequence if exists comment_seq;

drop sequence if exists faq_seq;

drop sequence if exists image_seq;

drop sequence if exists location_seq;

drop sequence if exists meal_seq;

drop sequence if exists meta_item_seq;

drop sequence if exists newsletter_seq;

drop sequence if exists pin_seq;

drop sequence if exists restaurant_seq;

drop sequence if exists statistics_seq;

drop sequence if exists transaction_u_seq;

drop sequence if exists user_seq;

