DROP DATABASE art;
CREATE DATABASE art CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE art;

CREATE TABLE pa_menu (
  id int NOT NULL PRIMARY KEY COMMENT '菜单ID',
  path varchar(255) NOT NULL COMMENT '路径',
  name varchar(255) NOT NULL COMMENT '名称',
  component varchar(255) DEFAULT NULL COMMENT '组件',
  title varchar(255) NOT NULL COMMENT '标题',
  icon varchar(255) DEFAULT NULL COMMENT '图标',
  show_badge tinyint(1) DEFAULT NULL COMMENT '是否显示徽章',
  show_text_badge varchar(255) DEFAULT NULL COMMENT '徽章文本',
  is_hide tinyint(1) DEFAULT NULL COMMENT '是否隐藏',
  is_hide_tab tinyint(1) DEFAULT NULL COMMENT '是否隐藏标签',
  link varchar(255) DEFAULT NULL COMMENT '链接',
  is_iframe tinyint(1) DEFAULT NULL COMMENT '是否为Iframe',
  keep_alive tinyint(1) NOT NULL COMMENT '是否保持活跃',
  auth_list json DEFAULT NULL COMMENT '权限列表',
  is_in_main_container tinyint(1) DEFAULT NULL COMMENT '是否在主容器中',
  parent_id int DEFAULT NULL COMMENT '父菜单ID',
  create_time TIMESTAMP NOT NULL COMMENT '创建时间',
  update_time TIMESTAMP COMMENT '更新时间',
  is_deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除 (0: 正常，1: 已删除)'
) COMMENT='系统菜单表';

-- 用户表
CREATE TABLE pa_user (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    email VARCHAR(100) COMMENT '邮箱',
    mobile VARCHAR(20) COMMENT '手机号',
    sex TINYINT(1) COMMENT '性别 (1: 男, 2: 女)',
    avatar VARCHAR(255) COMMENT '头像',
    dep_id INT NOT NULL COMMENT '部门ID',
    status TINYINT DEFAULT 1 COMMENT '状态 (1: 在线, 2: 离线, 3: 异常, 4: 注销)',
    create_time TIMESTAMP NOT NULL COMMENT '创建时间',
    update_time TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除 (0: 正常，1: 已删除)'
) COMMENT='用户信息表';

-- 角色表
CREATE TABLE pa_role (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_desc VARCHAR(255) COMMENT '角色描述',
    create_time TIMESTAMP NOT NULL COMMENT '创建时间',
    update_time TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除 (0: 正常，1: 已删除)'
) COMMENT='角色信息表';

-- 权限表
CREATE TABLE pa_permission (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '权限ID',
    permission_name VARCHAR(100) NOT NULL COMMENT '权限名称',
    permission_desc VARCHAR(255) COMMENT '权限描述',
    create_time TIMESTAMP NOT NULL COMMENT '创建时间',
    update_time TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除 (0: 正常，1: 已删除)'
) COMMENT='权限信息表';

-- 用户-角色关系表
CREATE TABLE pa_user_role (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id INT NOT NULL COMMENT '用户ID',
    role_id INT NOT NULL COMMENT '角色ID',
    create_time TIMESTAMP NOT NULL COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES pa_user(id) ON DELETE RESTRICT,
    FOREIGN KEY (role_id) REFERENCES pa_role(id) ON DELETE RESTRICT,
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除 (0: 正常，1: 已删除)'
) COMMENT='用户与角色关系表';

-- 角色-权限关系表
CREATE TABLE pa_role_permission (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    role_id INT NOT NULL COMMENT '角色ID',
    permission_id INT NOT NULL COMMENT '权限ID',
    create_time TIMESTAMP NOT NULL COMMENT '创建时间',
    FOREIGN KEY (role_id) REFERENCES pa_role(id) ON DELETE RESTRICT,
    FOREIGN KEY (permission_id) REFERENCES pa_permission(id) ON DELETE RESTRICT,
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除 (0: 正常，1: 已删除)'
) COMMENT='角色与权限关系表';

-- 部门表
CREATE TABLE pa_department (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '部门ID',
    dep_name VARCHAR(100) NOT NULL COMMENT '部门名称',
    sort INT NOT NULL COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态 (1: 启用, 2: 禁用)',
    parent_id INT DEFAULT NULL COMMENT '父部门ID',
    create_time TIMESTAMP NOT NULL COMMENT '创建时间',
    update_time TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除 (0: 正常，1: 已删除)'
) COMMENT='部门表';