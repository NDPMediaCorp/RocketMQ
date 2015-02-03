USE cockpit;

CREATE TABLE IF NOT EXISTS name_server (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  ip VARCHAR(64) NOT NULL,
  port SMALLINT NOT NULL DEFAULT 9876,
  create_time BIGINT NOT NULL DEFAULT 0,
  update_time BIGINT NOT NULL DEFAULT 0
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS ip_mapping(
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  inner_ip VARCHAR(64) NOT NULL,
  public_ip VARCHAR(64) NOT NULL,
  create_time BIGINT NOT NULL DEFAULT 0,
  update_time BIGINT NOT NULL DEFAULT 0
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS topic (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  topic VARCHAR(255) NOT NULL,
  cluster_name VARCHAR(100) NOT NULL DEFAULT 'DefaultCluster',
  permission TINYINT NOT NULL DEFAULT 6,
  write_queue_num INT NOT NULL DEFAULT 4,
  read_queue_num INT NOT NULL DEFAULT 4,
  unit BOOL NOT NULL DEFAULT FALSE ,
  has_unit_subscription BOOL NOT NULL DEFAULT FALSE ,
  broker_address VARCHAR(255),
  `order` BOOL DEFAULT FALSE,
  create_time BIGINT NOT NULL DEFAULT 0,
  update_time BIGINT NOT NULL DEFAULT 0
) ENGINE = INNODB;


CREATE TABLE IF NOT EXISTS consumer_group (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  which_broker_when_consume_slowly INT NOT NULL DEFAULT 1,
  group_name VARCHAR(255) NOT NULL,
  consume_enable BOOL NOT NULL DEFAULT TRUE ,
  consume_broadcast_enable BOOL NOT NULL DEFAULT FALSE ,
  broker_address VARCHAR(255),
  broker_id INT,
  retry_max_times INT NOT NULL DEFAULT 3,
  retry_queue_num MEDIUMINT NOT NULL DEFAULT 3,
  consume_from_min_enable BOOL NOT NULL DEFAULT TRUE,
  create_time DATETIME NOT NULL DEFAULT 0,
  update_time DATETIME NOT NULL DEFAULT 0
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS cockpit_user (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  role VARCHAR(32) NOT NULL,
  username VARCHAR(32) NOT NULL ,
  password  VARCHAR(64) NOT NULL
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS cockpit_role (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(32) NOT NULL
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS cockpit_rel_user_role (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  user_id INT NOT NULL,
  role_id INT NOT NULL
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS cockpit_user_login (
  user_name VARCHAR(32) NOT NULL ,
  login_status INT NOT NULL DEFAULT 1,
  retry INT NOT NULL DEFAULT 0,
  lock_time BIGINT NOT NULL DEFAULT 0
) ENGINE = INNODB;
