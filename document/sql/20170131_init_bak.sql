-- 账户表
DROP TABLE IF EXISTS accounts;
CREATE TABLE accounts (
  id           VARCHAR(32)  NOT NULL PRIMARY KEY
  COMMENT 'UUID 主键', -- UUID，剔除 - 字符
  principal    VARCHAR(200) NOT NULL DEFAULT ''
  COMMENT '身份，登陆唯一识别码', -- 身份
  password     VARCHAR(200) NOT NULL DEFAULT ''
  COMMENT '密码', -- 密码
  created_time DATETIME     NOT NULL DEFAULT now()
  COMMENT '账号创建时间', -- 账号创建时间
  locked       TINYINT(1)   NOT NULL DEFAULT 0
  COMMENT '账户是否被锁定；1代表未锁定，0代表被锁定', -- 账户是否被锁定
  valid        TINYINT(1)   NOT NULL DEFAULT 1
  COMMENT '账户是否有效；1代表有效，0代表失效（即已删除）' -- 账户是否有效
);
CREATE INDEX idx_account_principal_is_unlocked
  ON accounts (principal, locked);
CREATE INDEX idx_account_principal_is_valid
  ON accounts (principal, valid);
CREATE INDEX idx_account_principal_is_unlocked_and_valid
  ON accounts (principal, locked, valid);
ALTER TABLE accounts
  ADD INDEX idx_account_id_is_unlocked(id, locked);
ALTER TABLE accounts
  ADD INDEX idx_account_id_is_valid(id, valid);
ALTER TABLE accounts
  ADD INDEX idx_account_id_is_unlocked_and_valid(id, locked, valid);

-- @@@@@@@@@@@@@@@@@@@@@ --
-- @@ --------------- @@ --
-- @@@@@@@@@@@@@@@@@@@@@ --

-- 用户表，关联到账户；用于补充账户信息
DROP TABLE IF EXISTS users;
CREATE TABLE users (
  id       VARCHAR(32)  NOT NULL PRIMARY KEY
  COMMENT 'UUID 主键', -- UUID，剔除 - 字符
  email    VARCHAR(200) NOT NULL DEFAULT ''
  COMMENT '用户邮箱，可作为account.principal', -- 用户邮箱
  username VARCHAR(200) NOT NULL DEFAULT ''
  COMMENT '用户名称，可作为account.principal', -- 用户名称
  nickname VARCHAR(200) NOT NULL DEFAULT ''
  COMMENT '用户昵称', -- 用户昵称
  qq       VARCHAR(100) NOT NULL DEFAULT ''
  COMMENT '用户QQ', -- 用户QQ
  wx       VARCHAR(100) NOT NULL DEFAULT ''
  COMMENT '用户微信', -- 用户微信
  portrait VARCHAR(32)  NOT NULL DEFAULT ''
  COMMENT '头像ID'  -- 头像ID，关联到 photos.id
);

-- @@@@@@@@@@@@@@@@@@@@@ --
-- @@ --------------- @@ --
-- @@@@@@@@@@@@@@@@@@@@@ --

-- 角色表
DROP TABLE IF EXISTS roles;
CREATE TABLE roles (
  id            VARCHAR(32)  NOT NULL PRIMARY KEY
  COMMENT 'UUID 主键', -- UUID，剔除 - 字符
  role_name     VARCHAR(200) NOT NULL DEFAULT ''
  COMMENT '角色名称', -- 角色名称，用于页面显示
  remarks       VARCHAR(500) NOT NULL DEFAULT ''
  COMMENT '对角色的补充说明', -- 角色备注
  creator       VARCHAR(32)  NOT NULL DEFAULT ''
  COMMENT '创建角色的账号ID，关联到accounts.id', -- 创建角色的账号ID
  created_time  DATETIME     NOT NULL DEFAULT now()
  COMMENT '角色创建时间', -- 角色创建时间
  modifier      VARCHAR(32)  NOT NULL DEFAULT ''
  COMMENT '更新角色的账户ID，关联到accounts.id', -- 更新角色的账户ID
  modified_time DATETIME     NOT NULL DEFAULT now()
  COMMENT '角色更新时间', -- 角色更新时间
  active        TINYINT(1)   NOT NULL DEFAULT 1
  COMMENT '角色是否已激活，1代表已激活，0代表未激活', -- 角色是否已激活（激活后可用）
  valid         TINYINT(1)   NOT NULL DEFAULT 1
  COMMENT '角色是否有效；1代表有效，0代表失效（即已删除）' -- 角色是否有效
);
CREATE INDEX idx_role_is_active
  ON roles (role_name, active);
CREATE INDEX idx_role_is_valid
  ON roles (role_name, valid);
CREATE INDEX idx_role_is_active_and_valid
  ON roles (role_name, active, valid);
CREATE INDEX idx_role_id_is_active
  ON roles (id, active);
CREATE INDEX idx_role_id_is_valid
  ON roles (id, valid);
CREATE INDEX idx_role_id_is_active_and_valid
  ON roles (id, active, valid);

-- @@@@@@@@@@@@@@@@@@@@@ --
-- @@ --------------- @@ --
-- @@@@@@@@@@@@@@@@@@@@@ --

-- 权限表
DROP TABLE IF EXISTS perms;
CREATE TABLE perms (
  id            VARCHAR(32)  NOT NULL PRIMARY KEY
  COMMENT 'UUID 主键', -- UUID，剔除 - 字符
  perm_name     VARCHAR(200) NOT NULL  DEFAULT ''
  COMMENT '权限名称', -- 权限名称，用于页面显示
  perm_url      VARCHAR(200) NOT NULL  DEFAULT ''
  COMMENT '权限相对于contextPath的地址', -- 权限相对于 contextPath 的 url
  perm_pattern  VARCHAR(200) NOT NULL  DEFAULT ''
  COMMENT '权限模式，用于Shiro权限认证', -- 权限匹配模式，用于 Shiro 的匹配模式
  perm_type     VARCHAR(50)  NOT NULL  DEFAULT ''
  COMMENT '权限类型，枚举类型，为保证数据库可迁移，数据库不使用enum，由程序控制', -- 权限类型，是 channel（栏目） 还是 menu（菜单）
  parent_id     VARCHAR(32)  NOT NULL  DEFAULT ''
  COMMENT '上级权限ID，关联到perms.id', -- 上级权限 ID，即 父级栏目 或者 父级菜单
  weight        INT          NOT NULL  DEFAULT 10000
  COMMENT '权限权重，权重越大排序时越靠后', -- 权限权重，权重越大排序时越靠后
  creator       VARCHAR(32)  NOT NULL  DEFAULT ''
  COMMENT '创建权限的账号ID，关联到accounts.id', -- 创建权限的账号ID
  created_time  DATETIME     NOT NULL  DEFAULT now()
  COMMENT '权限创建时间', -- 权限创建时间
  modifier      VARCHAR(32)  NOT NULL  DEFAULT ''
  COMMENT '更新权限的账户ID，关联到accounts.id', -- 更新权限的账户ID
  modified_time DATETIME     NOT NULL  DEFAULT now()
  COMMENT '权限更新时间', -- 权限更新时间
  active        TINYINT(1)   NOT NULL  DEFAULT 1
  COMMENT '权限是否已激活，1代表已激活，0代表未激活', -- 权限是否已激活（激活后可用）
  valid         TINYINT(1)   NOT NULL  DEFAULT 1
  COMMENT '权限是否有效；1代表有效，0代表失效（即已删除）' -- 权限是否有效
);
CREATE INDEX idx_perm_is_active
  ON perms (perm_name, active);
CREATE INDEX idx_perm_is_valid
  ON perms (perm_name, valid);
CREATE INDEX idx_perm_is_active_and_valid
  ON perms (perm_name, active, valid);
CREATE INDEX idx_perm_id_is_active
  ON perms (id, active);
CREATE INDEX idx_perm_id_is_valid
  ON perms (id, valid);
CREATE INDEX idx_perm_id_is_active_and_valid
  ON perms (id, active, valid);

-- @@@@@@@@@@@@@@@@@@@@@ --
-- @@ --------------- @@ --
-- @@@@@@@@@@@@@@@@@@@@@ --

-- 用户-角色关联表
DROP TABLE IF EXISTS account_role;
CREATE TABLE account_role (
  account_id VARCHAR(32) NOT NULL
  COMMENT '账户ID',
  role_id    VARCHAR(32) NOT NULL
  COMMENT '角色ID'
);
CREATE INDEX idx_account_role
  ON account_role (account_id, role_id);

-- 角色-权限关联表
DROP TABLE IF EXISTS role_perm;
CREATE TABLE role_perm (
  role_id VARCHAR(32) NOT NULL
  COMMENT '角色ID',
  perm_id VARCHAR(32) NOT NULL
  COMMENT '权限ID'
);
CREATE INDEX idx_role_perm
  ON role_perm (role_id, perm_id);

-- @@@@@@@@@@@@@@@@@@@@@ --
-- @@ --------------- @@ --
-- @@@@@@@@@@@@@@@@@@@@@ --

-- 图片表，可以考虑一个相册表，之后通过相册来区分图片来源
DROP TABLE IF EXISTS photos;
CREATE TABLE photos (
  id           VARCHAR(32)  NOT NULL PRIMARY KEY
  COMMENT 'UUID 主键', -- UUID，剔除 - 字符
  photo_name   VARCHAR(200) NOT NULL DEFAULT ''
  COMMENT '图片名称', -- 图片名称
  path         VARCHAR(500) NOT NULL DEFAULT ''
  COMMENT '图片相对于 contextPath 的路径，可能是虚拟目录映射地址', -- 图片路径
  #   album       VARCHAR(32)  NOT NULL DEFAULT '' COMMENT '', -- 相册ID
  created_time DATETIME     NOT NULL DEFAULT now()
  COMMENT '上传/创建时间', -- 上传/创建时间
  valid        TINYINT(1)   NOT NULL DEFAULT 1
  COMMENT '图片是否有效；1代表有效，0代表失效（即已删除）'-- 图片是否有效
);
CREATE INDEX idx_photo_is_valid
  ON photos (valid);
CREATE INDEX idx_photo_id_is_valid
  ON photos (id, valid);

# DELIMITER $$
# CREATE TRIGGER trigger_on_insert_account
# AFTER
# INSERT
#   ON t_account
# FOR EACH ROW
#   BEGIN
#     DECLARE user_id VARCHAR(50);
#     DECLARE index_of_at INT;
#     DECLARE email_name VARCHAR(255);
#     SET user_id := new.id;
#     SELECT locate('@', new.email)
#     INTO index_of_at;
#     SELECT left(new.email, index_of_at - 1)
#     INTO email_name;
#     INSERT INTO t_user (nickname, id) VALUES (email_name, user_id);
#   END$$
# DELIMITER ;

DELIMITER $$
CREATE TRIGGER trigger_on_insert_account
AFTER INSERT ON accounts
FOR EACH ROW
  BEGIN
    DECLARE user_id CHAR(32);
    SET user_id := new.id;
    INSERT INTO users (id) VALUES (user_id);
  END$$
DELIMITER ;