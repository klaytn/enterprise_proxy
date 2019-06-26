CREATE TABLE IF NOT EXISTS ServiceChain (
  id int AUTO_INCREMENT PRIMARY KEY,
  name varchar(250),
  owner varchar(250),
  type smallint,
  is_use smallint,
  start_date timestamp,
  end_date timestamp,
  register timestamp,
  register_user int,
  modify timestamp,
  modity_user int,
  ip_addr varchar(50)
);

CREATE TABLE IF NOT EXISTS ServiceChainHost (
  id int AUTO_INCREMENT PRIMARY KEY,
  servicechain_id int,
  host varchar(150),
  port smallint,
  is_use smallint,
  host_type smallint,
  category varchar,
  rpc_type smallint,
  is_alive smallint,
  register timestamp,
  register_user int,
  modify timestamp,
  modity_user int,
  ip_addr varchar(50)
);

CREATE TABLE IF NOT EXISTS ServiceChainHostHealthCheck (
  id int AUTO_INCREMENT PRIMARY KEY,
  chain_host_id int,
  check_type smallint,
  comment clob,
  checkdate timestamp
);

INSERT INTO ServiceChain (name,owner,type,is_use,start_date,end_date,ip_addr) VALUES ('funny','groundx',1,1,'2019-01-01 00:00:00','2022-12-31 23:59:59','127.0.0.1');
INSERT INTO ServiceChainHost (servicechain_id,host,port,is_use,host_type,category,rpc_type,is_alive,register,ip_addr) VALUES (1,'http://api.prebaobab.klaytn.net',8551,1,1,'PREBAOBAB',1,1,'2019-04-03 00:00:00','127.0.0.1');
INSERT INTO ServiceChainHost (servicechain_id,host,port,is_use,host_type,category,rpc_type,is_alive,register,ip_addr) VALUES (1,'https://api.baobab.klaytn.net',8651,1,1,'BAOBAB',1,1,'2019-04-03 00:00:01','127.0.0.1');
INSERT INTO ServiceChainHost (servicechain_id,host,port,is_use,host_type,category,rpc_type,is_alive,register,ip_addr) VALUES (1,'http://13.209.87.241',8551,1,1,'EP TEST EN NODE',1,1,'2019-04-03 00:00:01','127.0.0.1');