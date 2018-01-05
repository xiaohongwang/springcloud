drop table if exists sys_users;
drop table if exists sys_roles;
drop table if exists sys_permissions;
drop table if exists sys_users_roles;
drop table if exists sys_roles_permissions;

create table sys_users (
  id bigint auto_increment,
  username varchar(100),
  password varchar(100),
  salt varchar(100),
  locked bool default false,
  constraint pk_sys_users primary key(id)
) charset=utf8 ENGINE=InnoDB;
create unique index idx_sys_users_username on sys_users(username);

create table sys_roles (
  id bigint auto_increment,
  role varchar(100),
  description varchar(100),
  available bool default false,
  constraint pk_sys_roles primary key(id)
) charset=utf8 ENGINE=InnoDB;
create unique index idx_sys_roles_role on sys_roles(role);

create table sys_permissions (
  id bigint auto_increment,
  permission varchar(100),
  description varchar(100),
  available bool default false,
  constraint pk_sys_permissions primary key(id)
) charset=utf8 ENGINE=InnoDB;
create unique index idx_sys_permissions_permission on sys_permissions(permission);

create table sys_users_roles (
  user_id bigint,
  role_id bigint,
  constraint pk_sys_users_roles primary key(user_id, role_id)
) charset=utf8 ENGINE=InnoDB;

create table sys_roles_permissions (
  role_id bigint,
  permission_id bigint,
  constraint pk_sys_roles_permissions primary key(role_id, permission_id)
) charset=utf8 ENGINE=InnoDB;



insert into `sys_permissions` (`id`, `permission`, `description`, `available`) values('1','addSysUser','添加系统操作员','0');
insert into `sys_permissions` (`id`, `permission`, `description`, `available`) values('2','changePassword','修改用户密码','0');
insert into `sys_permissions` (`id`, `permission`, `description`, `available`) values('3','unFreezeUser','激活用户','0');

insert into `sys_roles` (`id`, `role`, `description`, `available`) values('1','sysUser','操作员管理','0');
insert into `sys_roles` (`id`, `role`, `description`, `available`) values('2','permisson','权限管理','0');
insert into `sys_roles` (`id`, `role`, `description`, `available`) values('3','role','角色管理','0');

insert into `sys_roles_permissions` (`role_id`, `permission_id`) values('1','1');
insert into `sys_roles_permissions` (`role_id`, `permission_id`) values('1','2');
insert into `sys_roles_permissions` (`role_id`, `permission_id`) values('1','3');

insert into `sys_users_roles` (`user_id`, `role_id`) values('1','1');
insert into `sys_users_roles` (`user_id`, `role_id`) values('1','2');
insert into `sys_users_roles` (`user_id`, `role_id`) values('1','3');

INSERT INTO sys_users(username,PASSWORD,salt)
VALUES('xiaohong','502db4e25057cfb196b7520dc58d32e2','971f141c2c36e0cc810df866fd078a1b');