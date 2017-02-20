-- 账户表
DROP TABLE IF EXISTS account;
CREATE TABLE account (
  id            CHAR(32)   NOT NULL PRIMARY KEY
  COMMENT 'UUID 主键', -- UUID，剔除 - 字符
  principal     VARCHAR(200) COMMENT '身份，登陆唯一识别码', -- 身份
  password      CHAR(32) COMMENT '密码', -- 密码
  created_time  DATETIME COMMENT '账号创建时间', -- 账号创建时间
  modifier      VARCHAR(32) COMMENT '更新账户的账户ID，关联到account.id', -- 更新账户的账户ID
  modified_time DATETIME COMMENT '账户更新时间', -- 账户更新时间
  locked        TINYINT(1) NOT NULL DEFAULT 0
  COMMENT '账户是否被锁定；1代表未锁定，0代表被锁定', -- 账户是否被锁定
  valid         TINYINT(1) NOT NULL DEFAULT 1
  COMMENT '账户是否有效；1代表有效，0代表失效（即已删除）' -- 账户是否有效
);
CREATE INDEX idx_account_principal_is_unlocked
  ON account (principal, locked);
CREATE INDEX idx_account_principal_is_valid
  ON account (principal, valid);
CREATE INDEX idx_account_principal_is_unlocked_and_valid
  ON account (principal, locked, valid);
ALTER TABLE account
  ADD INDEX idx_account_id_is_unlocked(id, locked);
ALTER TABLE account
  ADD INDEX idx_account_id_is_valid(id, valid);
ALTER TABLE account
  ADD INDEX idx_account_id_is_unlocked_and_valid(id, locked, valid);
INSERT INTO account (id, principal, password, created_time, locked, valid)
VALUES ('d7ff797575604fd1b9960ab10c30d668', 'System', 'e8fada2b3b4420040756385069c05943', '2017-02-15 21:13:43', 1, 1);

-- @@@@@@@@@@@@@@@@@@@@@ --
-- @@ --------------- @@ --
-- @@@@@@@@@@@@@@@@@@@@@ --

-- 用户表，关联到账户；用于补充账户信息
DROP TABLE IF EXISTS user;
CREATE TABLE user (
  id       VARCHAR(32) NOT NULL PRIMARY KEY
  COMMENT 'UUID 主键', -- UUID，剔除 - 字符
  email    VARCHAR(200) COMMENT '用户邮箱，可作为account.principal', -- 用户邮箱
  username VARCHAR(200) COMMENT '用户名称，可作为account.principal', -- 用户名称
  nickname VARCHAR(200) COMMENT '用户昵称', -- 用户昵称
  qq       VARCHAR(100) COMMENT '用户QQ', -- 用户QQ
  wx       VARCHAR(100) COMMENT '用户微信', -- 用户微信
  portrait VARCHAR(32) COMMENT '头像ID'  -- 头像ID，关联到 photo.id
);

-- @@@@@@@@@@@@@@@@@@@@@ --
-- @@ --------------- @@ --
-- @@@@@@@@@@@@@@@@@@@@@ --

-- 角色表
DROP TABLE IF EXISTS role;
CREATE TABLE role (
  id            VARCHAR(32) NOT NULL PRIMARY KEY
  COMMENT 'UUID 主键', -- UUID，剔除 - 字符
  role_name     VARCHAR(200) COMMENT '角色英文名称', -- 角色英文名称，用于数据处理
  role_name_cn  VARCHAR(200) COMMENT '角色中文名称', -- 角色中文名称，用于页面显示
  remarks       VARCHAR(500) COMMENT '对角色的补充说明', -- 角色备注
  creator       VARCHAR(32) COMMENT '创建角色的账号ID，关联到account.id', -- 创建角色的账号ID
  created_time  DATETIME COMMENT '角色创建时间', -- 角色创建时间
  modifier      VARCHAR(32) COMMENT '更新角色的账户ID，关联到account.id', -- 更新角色的账户ID
  modified_time DATETIME COMMENT '角色更新时间', -- 角色更新时间
  active        TINYINT(1)  NOT NULL DEFAULT 1
  COMMENT '角色是否已激活，1代表已激活，0代表未激活', -- 角色是否已激活（激活后可用）
  valid         TINYINT(1)  NOT NULL DEFAULT 1
  COMMENT '角色是否有效；1代表有效，0代表失效（即已删除）' -- 角色是否有效
);
CREATE INDEX idx_role_is_active
  ON role (role_name, active);
CREATE INDEX idx_role_is_valid
  ON role (role_name, valid);
CREATE INDEX idx_role_is_active_and_valid
  ON role (role_name, active, valid);
CREATE INDEX idx_role_id_is_active
  ON role (id, active);
CREATE INDEX idx_role_id_is_valid
  ON role (id, valid);
CREATE INDEX idx_role_id_is_active_and_valid
  ON role (id, active, valid);

-- @@@@@@@@@@@@@@@@@@@@@ --
-- @@ --------------- @@ --
-- @@@@@@@@@@@@@@@@@@@@@ --

-- 权限表
DROP TABLE IF EXISTS perm;
CREATE TABLE perm (
  id            VARCHAR(32) NOT NULL PRIMARY KEY
  COMMENT 'UUID 主键', -- UUID，剔除 - 字符
  perm_name     VARCHAR(200) COMMENT '权限英文名称', -- 权限名称，用于页面显示
  perm_url      VARCHAR(200) COMMENT '权限相对于contextPath的地址', -- 权限相对于 contextPath 的 url，只有管理类权限才有地址，进入该地址可以管理该资源
  perm_pattern  VARCHAR(200) COMMENT '权限模式，用于Shiro权限认证', -- 权限匹配模式，用于 Shiro 的匹配模式
  perm_type     VARCHAR(50) COMMENT '权限类型，枚举类型，为保证数据库可迁移，数据库不使用enum，由程序控制', -- 权限类型，是 channel（栏目） 还是 menu（菜单）
  parent_id     VARCHAR(32) COMMENT '上级权限ID，关联到perm.id', -- 上级权限 ID，即 父级栏目 或者 父级菜单
  weight        INT COMMENT '权限权重，权重越大排序时越靠后', -- 权限权重，权重越大排序时越靠后，排序是相对与同级权限而言的，如果两个权限不是在同一个级别下那么两者就没有权衡权重的意义了
  creator       VARCHAR(32) COMMENT '创建权限的账号ID，关联到account.id', -- 创建权限的账号ID
  created_time  DATETIME COMMENT '权限创建时间', -- 权限创建时间
  modifier      VARCHAR(32) COMMENT '更新权限的账户ID，关联到account.id', -- 更新权限的账户ID
  modified_time DATETIME COMMENT '权限更新时间', -- 权限更新时间
  active        TINYINT(1)  NOT NULL  DEFAULT 1
  COMMENT '权限是否已激活，1代表已激活，0代表未激活', -- 权限是否已激活（激活后可用）
  valid         TINYINT(1)  NOT NULL  DEFAULT 1
  COMMENT '权限是否有效；1代表有效，0代表失效（即已删除）' -- 权限是否有效
);
CREATE INDEX idx_perm_is_active
  ON perm (perm_name, active);
CREATE INDEX idx_perm_is_valid
  ON perm (perm_name, valid);
CREATE INDEX idx_perm_is_active_and_valid
  ON perm (perm_name, active, valid);
CREATE INDEX idx_perm_id_is_active
  ON perm (id, active);
CREATE INDEX idx_perm_id_is_valid
  ON perm (id, valid);
CREATE INDEX idx_perm_id_is_active_and_valid
  ON perm (id, active, valid);

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
DROP INDEX idx_account_role
ON role_perm;
CREATE UNIQUE INDEX idx_account_role
  ON account_role (account_id, role_id);

-- 角色-权限关联表
DROP TABLE IF EXISTS role_perm;
CREATE TABLE role_perm (
  role_id VARCHAR(32) NOT NULL
  COMMENT '角色ID',
  perm_id VARCHAR(32) NOT NULL
  COMMENT '权限ID'
);
DROP INDEX idx_role_perm
ON role_perm;
CREATE UNIQUE INDEX idx_role_perm
  ON role_perm (role_id, perm_id);

-- @@@@@@@@@@@@@@@@@@@@@ --
-- @@ --------------- @@ --
-- @@@@@@@@@@@@@@@@@@@@@ --

-- 图片表，可以考虑一个相册表，之后通过相册来区分图片来源
DROP TABLE IF EXISTS photo;
CREATE TABLE photo (
  id           VARCHAR(32) NOT NULL PRIMARY KEY
  COMMENT 'UUID 主键', -- UUID，剔除 - 字符
  photo_name   VARCHAR(200) COMMENT '图片名称', -- 图片名称
  path         VARCHAR(500) COMMENT '图片相对于 contextPath 的路径，可能是虚拟目录映射地址', -- 图片路径
  #   album       VARCHAR(32)  NOT NULL DEFAULT '' COMMENT '', -- 相册ID
  created_time DATETIME COMMENT '上传/创建时间', -- 上传/创建时间
  valid        TINYINT(1)  NOT NULL DEFAULT 1
  COMMENT '图片是否有效；1代表有效，0代表失效（即已删除）'-- 图片是否有效
);
CREATE INDEX idx_photo_is_valid
  ON photo (valid);
CREATE INDEX idx_photo_id_is_valid
  ON photo (id, valid);
INSERT INTO photo (id, photo_name, path, created_time, valid)
VALUES ('f3e5ac311d8d4b6bacff05383351470d', 'commonPortrait', '/mapping/image/f3e5ac311d8d4b6bacff05383351470d.jpg',
        '2017-02-14 08:26:16', TRUE);

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
DROP TRIGGER IF EXISTS trigger_on_insert_account;
DELIMITER $$
CREATE TRIGGER trigger_on_insert_account
AFTER INSERT ON account
FOR EACH ROW
  BEGIN
    DECLARE user_id CHAR(32);
    SET user_id := new.id;
    INSERT INTO user (id, portrait) VALUES (user_id, 'f3e5ac311d8d4b6bacff05383351470d');
  END$$
DELIMITER ;