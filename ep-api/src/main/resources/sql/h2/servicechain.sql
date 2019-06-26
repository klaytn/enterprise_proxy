CREATE TABLE IF NOT EXISTS ServiceChain (
    id int AUTO_INCREMENT PRIMARY KEY,
    name varchar(250),
    owner varchar(250),
    is_use smallint,
    start_date timestamp,
    end_date timestamp,
    register timestamp,
    modify timestamp,
    ip_addr varchar(50)
);

CREATE TABLE IF NOT EXISTS ServiceChainHost (
    id int AUTO_INCREMENT PRIMARY KEY,
    servicechain_id int,
    host varchar(150),
    port smallint,
    is_use smallint,
    category varchar,
    rpc_type smallint,
    is_alive smallint,
    register timestamp,
    modify timestamp,
    ip_addr varchar(50)
);

CREATE TABLE IF NOT EXISTS ServiceChainHostHealthCheck (
    id int AUTO_INCREMENT PRIMARY KEY,
    chain_host_id int,
    check_type smallint,
    comment clob,
    checkdate timestamp
);
