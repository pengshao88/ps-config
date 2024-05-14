CREATE TABLE `configs` (
  `app` varchar(64) NOT NULL,
  `env` varchar(64) NOT NULL,
  `version` varchar(64) NOT NULL,
  `ns` varchar(64) NOT NULL,
  `config_key` varchar(64) NOT NULL,
  `config_value` varchar(128) DEFAULT NULL,
  UNIQUE KEY `idx_unique` (`app`,`env`,`version`,`ns`,`config_key`)
) ENGINE=InnoDB;

insert into configs (app, env, version, ns, config_key, config_value)
values ('psrpc', 'dev', 'v1_0_0', 'application', 'ps.a', 'aa100'),
       ('psrpc', 'dev', 'v1_0_0', 'application', 'ps.b', 'http://localhost:9129'),
       ('psrpc', 'dev', 'v1_0_0', 'application', 'ps.c', 'cc100');

create table if not exists `locks`(
       `id` int primary key not null,
       `app` varchar(64) not null
);
insert into locks (id, app) values (1, 'psconfig-server');