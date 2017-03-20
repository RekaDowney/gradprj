-- 账户表
DROP TABLE IF EXISTS account;
CREATE TABLE account (
  id            CHAR(32)   NOT NULL PRIMARY KEY
  COMMENT 'UUID 主键', -- UUID，剔除 - 字符
  principal     VARCHAR(200) COMMENT '身份，登陆唯一识别码', -- 身份
  password      CHAR(32) COMMENT '密码', -- 密码
  created_time  DATETIME COMMENT '账号创建时间', -- 账号创建时间
  modifier      CHAR(32) COMMENT '更新账户的账户ID，关联到account.id', -- 更新账户的账户ID
  modified_time DATETIME COMMENT '账户更新时间', -- 账户更新时间
  locked        TINYINT(1) NOT NULL DEFAULT 0
  COMMENT '账户是否被锁定；1代表未锁定，0代表被锁定', -- 账户是否被锁定
  valid         TINYINT(1) NOT NULL DEFAULT 1
  COMMENT '账户是否有效；1代表有效，0代表失效（即已删除）' -- 账户是否有效
)
  ENGINE = InnoDB
  DEFAULT CHARSET = UTF8
  COMMENT = '账户表';
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

-- @@@@@@@@@@@@@@@@@@@@@ --
-- @@ --------------- @@ --
-- @@@@@@@@@@@@@@@@@@@@@ --

-- 用户表，关联到账户；用于补充账户信息
DROP TABLE IF EXISTS user;
CREATE TABLE user (
  id       CHAR(32) NOT NULL PRIMARY KEY
  COMMENT 'UUID 主键', -- UUID，剔除 - 字符
  email    VARCHAR(200) COMMENT '用户邮箱，可作为account.principal', -- 用户邮箱
  username VARCHAR(200) COMMENT '用户名称，可作为account.principal', -- 用户名称
  nickname VARCHAR(200) COMMENT '用户昵称', -- 用户昵称
  qq       VARCHAR(100) COMMENT '用户QQ', -- 用户QQ
  wx       VARCHAR(100) COMMENT '用户微信', -- 用户微信
  portrait VARCHAR(32) COMMENT '头像ID'  -- 头像ID，关联到 photo.id
)
  ENGINE = InnoDB
  DEFAULT CHARSET = UTF8
  COMMENT = '用户信息表';

-- @@@@@@@@@@@@@@@@@@@@@ --
-- @@ --------------- @@ --
-- @@@@@@@@@@@@@@@@@@@@@ --

-- 角色表
DROP TABLE IF EXISTS role;
CREATE TABLE role (
  id            CHAR(32)   NOT NULL PRIMARY KEY
  COMMENT 'UUID 主键', -- UUID，剔除 - 字符
  role_name     VARCHAR(200) COMMENT '角色英文名称', -- 角色英文名称，用于数据处理
  role_name_cn  VARCHAR(200) COMMENT '角色中文名称', -- 角色中文名称，用于页面显示
  remarks       VARCHAR(500) COMMENT '对角色的补充说明', -- 角色备注
  creator       CHAR(32) COMMENT '创建角色的账号ID，关联到account.id', -- 创建角色的账号ID
  created_time  DATETIME COMMENT '角色创建时间', -- 角色创建时间
  modifier      CHAR(32) COMMENT '更新角色的账户ID，关联到account.id', -- 更新角色的账户ID
  modified_time DATETIME COMMENT '角色更新时间', -- 角色更新时间
  active        TINYINT(1) NOT NULL DEFAULT 1
  COMMENT '角色是否已激活，1代表已激活，0代表未激活', -- 角色是否已激活（激活后可用）
  valid         TINYINT(1) NOT NULL DEFAULT 1
  COMMENT '角色是否有效；1代表有效，0代表失效（即已删除）' -- 角色是否有效
)
  ENGINE = InnoDB
  DEFAULT CHARSET = UTF8
  COMMENT = '角色表';
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
  id            CHAR(32)   NOT NULL PRIMARY KEY
  COMMENT 'UUID 主键', -- UUID，剔除 - 字符
  perm_name     VARCHAR(200) COMMENT '权限英文名称', -- 权限名称，用于页面显示
  perm_url      VARCHAR(200) COMMENT '权限相对于contextPath的地址', -- 权限相对于 contextPath 的 url，只有管理类权限才有地址，进入该地址可以管理该资源
  perm_pattern  VARCHAR(200) COMMENT '权限模式，用于Shiro权限认证', -- 权限匹配模式，用于 Shiro 的匹配模式
  perm_type     VARCHAR(50) COMMENT '权限类型，枚举类型，为保证数据库可迁移，数据库不使用enum，由程序控制', -- 权限类型，是 channel（栏目） 还是 menu（菜单）
  parent_id     CHAR(32) COMMENT '上级权限ID，关联到perm.id', -- 上级权限 ID，即 父级栏目 或者 父级菜单
  #   level         TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '菜单类权限专属，表示菜单的层次',
  weight        INT COMMENT '权限权重，权重越大排序时越靠后', -- 权限权重，权重越大排序时越靠后，排序是相对与同级权限而言的，如果两个权限不是在同一个级别下那么两者就没有权衡权重的意义了
  creator       CHAR(32) COMMENT '创建权限的账号ID，关联到account.id', -- 创建权限的账号ID
  created_time  DATETIME COMMENT '权限创建时间', -- 权限创建时间
  modifier      CHAR(32) COMMENT '更新权限的账户ID，关联到account.id', -- 更新权限的账户ID
  modified_time DATETIME COMMENT '权限更新时间', -- 权限更新时间
  attachable    TINYINT(1) NOT NULL  DEFAULT 0
  COMMENT '菜单类权限专属，是否可以上传附件到该菜单下', -- 菜单类权限专属，是否可以上传附件到该菜单下
  active        TINYINT(1) NOT NULL  DEFAULT 1
  COMMENT '权限是否已激活，1代表已激活，0代表未激活', -- 权限是否已激活（激活后可用）
  valid         TINYINT(1) NOT NULL  DEFAULT 1
  COMMENT '权限是否有效；1代表有效，0代表失效（即已删除）' -- 权限是否有效
)
  ENGINE = InnoDB
  DEFAULT CHARSET = UTF8
  COMMENT = '权限表';
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
  account_id CHAR(32) NOT NULL
  COMMENT '账户ID',
  role_id    CHAR(32) NOT NULL
  COMMENT '角色ID'
)
  ENGINE = InnoDB
  DEFAULT CHARSET = UTF8
  COMMENT = '用户角色关联表';
DROP INDEX idx_account_role
ON role_perm;
CREATE UNIQUE INDEX idx_account_role
  ON account_role (account_id, role_id);

-- 角色-权限关联表
DROP TABLE IF EXISTS role_perm;
CREATE TABLE role_perm (
  role_id CHAR(32) NOT NULL
  COMMENT '角色ID',
  perm_id CHAR(32) NOT NULL
  COMMENT '权限ID'
)
  ENGINE = InnoDB
  DEFAULT CHARSET = UTF8
  COMMENT = '角色权限关联表';
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
  id           CHAR(32)   NOT NULL PRIMARY KEY
  COMMENT 'UUID 主键', -- UUID，剔除 - 字符
  photo_name   VARCHAR(200) COMMENT '图片名称', -- 图片名称
  path         VARCHAR(500) COMMENT '图片相对于 contextPath 的路径，可能是虚拟目录映射地址', -- 图片路径
  #   album       VARCHAR(32)  NOT NULL DEFAULT '' COMMENT '', -- 相册ID
  created_time DATETIME COMMENT '上传/创建时间', -- 上传/创建时间
  valid        TINYINT(1) NOT NULL DEFAULT 1
  COMMENT '图片是否有效；1代表有效，0代表失效（即已删除）'-- 图片是否有效
)
  ENGINE = InnoDB
  DEFAULT CHARSET = UTF8
  COMMENT = '图片表';
CREATE INDEX idx_photo_is_valid
  ON photo (valid);
CREATE INDEX idx_photo_id_is_valid
  ON photo (id, valid);

DROP TABLE IF EXISTS document;
CREATE TABLE document (
  id            CHAR(32)   NOT NULL PRIMARY KEY
  COMMENT 'UUID 主键',
  doc_url       VARCHAR(200) COMMENT '文档相对于webapp/doc/mapping的路径，包含后缀名，文档名称为id值',
  doc_name      VARCHAR(200) COMMENT '文档原始名称，包含后缀名',
  doc_type      VARCHAR(20) COMMENT '文档类型，枚举',
  creator       CHAR(32) COMMENT '文档上传者，关联到account.id',
  category_id   CHAR(32) COMMENT '文档所属的菜单，关联到perm.id',
  created_time  DATETIME COMMENT '文档上传时间',
  modifier      CHAR(32) COMMENT '文档修改者，关联到account.id',
  modified_time DATETIME COMMENT '文档修改时间',
  valid         TINYINT(1) NOT NULL DEFAULT 1
  COMMENT '文档是否有效；1代表有效，0代表失效（即已删除）'
)
  ENGINE = InnoDB
  DEFAULT CHARSET = UTF8
  COMMENT = '文档表';
CREATE INDEX idx_doc_id
  ON document (id);

DROP TABLE IF EXISTS document_grade;
CREATE TABLE document_grade (
  doc_id CHAR(32) NOT NULL
  COMMENT '文档ID',
  judge  CHAR(32) NOT NULL
  COMMENT '评分人，账户ID',
  grade  TINYINT  NOT NULL
  COMMENT '评分，值从1~5'
)
  ENGINE = InnoDB
  DEFAULT CHARSET = UTF8
  COMMENT = '文档评分表';
-- 唯一索引，一篇文档只有被同一个账户评分一次
CREATE UNIQUE INDEX idx_doc_grade
  ON document_grade (doc_id, judge);

-- 最新入库文档，交由触发器维护，表的数据仅有50条，一旦有新的文档入库，会自动删除这张表中最旧的记录
DROP TABLE IF EXISTS document_newest;
CREATE TABLE document_newest (
  id     BIGINT UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '定义无符号自增ID',
  doc_id CHAR(32)        NOT NULL
  COMMENT '文档ID',
  UNIQUE KEY (id)
    COMMENT '自增列必须是一种键'
)
  ENGINE = InnoDB
  DEFAULT CHARSET = UTF8
  COMMENT = '最新入库文档表';

-- 触发器，用来维护 document_newest 表，后台对该表仅仅存在 SELECT 操作，不可以增删改该表，增删改操作由触发器维护
DROP TRIGGER IF EXISTS trigger_on_insert_document;
DELIMITER $$
CREATE TRIGGER trigger_on_insert_document
AFTER INSERT ON document
FOR EACH ROW
  BEGIN
    -- 变量声明
    DECLARE doc_id CHAR(32);
    DECLARE num_of_newest_table INT;

    SET doc_id := new.id;
    SELECT COUNT(*)
    FROM document_newest
    INTO num_of_newest_table;

    -- 如果最新入库文档这张表数量大于等于 50，那么删除最旧的一条记录
    IF num_of_newest_table >= 50
    THEN

      DELETE FROM document_newest
      WHERE id = (SELECT id
                  FROM (SELECT min(id) id
                        FROM document_newest) a);
    /*
          -- MySQL 不支持下面的操作
          -- 即在 同一条 SQL 中，从表 A 查询出某个值之后，该值不能立即作为表 A 的更新操作
          -- [HY000][1093] You can't specify target table 'document_newest' for update in FROM clause
          DELETE FROM document_newest
          WHERE id = (SELECT MIN(id)
                      FROM document_newest);
          -- 将 SELECT 语句独立出来之后可以解决这个问题
          -- DECLARE oldest_id BIGINT UNSIGNED;
          -- SELECT MIN(id)
          -- FROM document_newest
          -- INTO oldest_id;
          -- DELETE FROM document_newest
          -- WHERE id = oldest_id;

          -- 或者将 SELECT 出来的数据包装到另一张表里，这样就可以解决该问题了
    */
    END IF;

    INSERT INTO document_newest (doc_id) VALUES (doc_id);

  END$$
DELIMITER ;

DROP TRIGGER IF EXISTS trigger_on_update_document;
DELIMITER $$
CREATE TRIGGER trigger_on_update_document
AFTER UPDATE ON document
FOR EACH ROW
  BEGIN
    -- 如果发现修改操作是将 document.valid 设置为 FALSE，即删除文档，那么也将最新入库文档这张表中相同文档ID的数据删除掉
    IF new.valid = FALSE
    THEN
      DELETE FROM document_newest
      WHERE doc_id = new.id;
    END IF;
  END$$
DELIMITER ;

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


## 初始化操作
## 默认头像
INSERT INTO photo (id, photo_name, path, created_time, valid)
VALUES ('f3e5ac311d8d4b6bacff05383351470d', 'commonPortrait.jpg', '/mapping/image/f3e5ac311d8d4b6bacff05383351470d.jpg',
        '2017-02-14 08:26:16', TRUE);

-- 系统账户
INSERT INTO account (id, principal, password, created_time, locked, valid)
VALUES ('d7ff797575604fd1b9960ab10c30d668', 'System', 'e8fada2b3b4420040756385069c05943', '2017-02-15 21:13:43', 1, 1);
-- 通用游客账户
INSERT INTO account (id, principal, password, created_time, locked, valid)
VALUES ('9cf78b0ed096438bbc0f45a7fc8cfd0c', 'visitor', 'cccd2e959c0a40369042ddd1c23e1c53', '2017-02-20 21:37:21', FALSE,
        TRUE);
-- 管理员账户
INSERT INTO account (id, principal, password, created_time, locked, valid)
VALUES ('93f6787ea384402bbb9503caef8c5765', 'admin', '364977978631498ff1f09ee3b2d7be87', '2017-02-27 12:53:05', FALSE,
        TRUE);

-- @@ 角色
-- 游客
INSERT INTO role (id, role_name, role_name_cn, remarks, creator, created_time, modifier, modified_time, active, valid)
VALUES ('b0b8a48302f44b68b613409c312a1f4e', 'visitor', '游客', '游客是一种通用的角色，拥有系统最基本的功能对应的权限',
        'd7ff797575604fd1b9960ab10c30d668', '2017-02-20 21:49:08', NULL, NULL, TRUE, TRUE);
-- 注册用户
INSERT INTO role (id, role_name, role_name_cn, remarks, creator, created_time, modifier, modified_time, active, valid)
VALUES ('85d4eb03bc3b407e93a447ee5e27d4c4', 'user', '注册用户', '注册用户权限比游客要多一些，其可以通过登陆操作来获取更多系统功能',
        'd7ff797575604fd1b9960ab10c30d668', '2017-02-20 21:49:09', NULL,
        NULL, TRUE, TRUE);
-- 管理员
INSERT INTO role (id, role_name, role_name_cn, remarks, creator, created_time, modifier, modified_time, active, valid)
VALUES ('4cee7a676d984191a0ab89cf679a55a1', 'admin', '管理员', '管理员拥有系统大部分权限，可以进入后台管理', 'd7ff797575604fd1b9960ab10c30d668',
        '2017-02-20 21:49:09', NULL, NULL, TRUE, TRUE);
-- 主人
INSERT INTO role (id, role_name, role_name_cn, remarks, creator, created_time, modifier, modified_time, active, valid)
VALUES ('b763e366db0e40bb8c3101227b080821', 'master', '主人', '主人拥有系统所有功能，在后台管理中可以进行更多功能操作',
        'd7ff797575604fd1b9960ab10c30d668', '2017-02-20 21:49:09', NULL, NULL, TRUE, TRUE);

-- @@ 权限
-- 管理类
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES ('e8007ad580f340ffb93535de1b608a0a', '后台管理', '/backend/manage', 'manage:*:backend', '管理类', NULL, 10,
                                            'd7ff797575604fd1b9960ab10c30d668', '2017-02-21 20:36:19', FALSE, TRUE,
        TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES ('829d8bd064a8422c8e84b1c695cd582f', '用户管理', '/user/manage', 'manage:*:user', '管理类',
                                            'e8007ad580f340ffb93535de1b608a0a', 10,
                                            'd7ff797575604fd1b9960ab10c30d668', '2017-02-21 20:36:18', FALSE, TRUE,
        TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES ('421bd40c477e41edb4424e67628b88ff', '角色管理', '/role/manage', 'manage:*:role', '管理类',
                                            'e8007ad580f340ffb93535de1b608a0a', 20,
                                            'd7ff797575604fd1b9960ab10c30d668', '2017-02-21 20:36:19', FALSE, TRUE,
        TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES ('4e2b2a20451747938cbb1af202be92ea', '权限管理', '/perm/manage', 'manage:*:perm', '管理类',
                                            'e8007ad580f340ffb93535de1b608a0a', 30,
                                            'd7ff797575604fd1b9960ab10c30d668', '2017-02-21 20:36:19', FALSE, TRUE,
        TRUE);

-- 功能类
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES ('0445ba623d534ec2878801c252a86d1a', '文档浏览', NULL, 'doc:*:view', '功能类', NULL, 200,
                                            'd7ff797575604fd1b9960ab10c30d668', '2017-02-21 20:36:19', FALSE, TRUE,
        TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES ('67eb917edb9f4b60b353612553ea61a0', '文档上传', NULL, 'doc:*:upload', '功能类', NULL, 200,
                                            'd7ff797575604fd1b9960ab10c30d668', '2017-02-21 20:36:19', FALSE, TRUE,
        TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES ('940be8feb63d44c6b018641883ae7255', '文档删除', NULL, 'doc:*:delete', '功能类', NULL, 200,
                                            'd7ff797575604fd1b9960ab10c30d668', '2017-02-21 20:36:19', FALSE, TRUE,
        TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES ('97a32f2cb13e4f1386e3fabc7711a07a', '文档下载', NULL, 'doc:*:download', '功能类', NULL, 200,
                                            'd7ff797575604fd1b9960ab10c30d668', '2017-02-21 20:36:19', FALSE, TRUE,
        TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES ('43b9d6210cd64e7a84bd2cc33aa01649', '文档编辑', NULL, 'doc:*:edit', '功能类', NULL, 200,
                                            'd7ff797575604fd1b9960ab10c30d668', '2017-02-21 20:36:19', FALSE, TRUE,
        TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES ('bf135b028d344c688f98e9745deaa26c', '文档格式转换', NULL, 'doc:*:transfer', '功能类', NULL, 200,
                                            'd7ff797575604fd1b9960ab10c30d668', '2017-02-21 20:36:19', FALSE, TRUE,
        TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES ('8a76458cbdf4416896e995aee25a6558', '请假', NULL, 'ask:for:leave', '功能类', NULL, 200,
                                            'd7ff797575604fd1b9960ab10c30d668', '2017-02-21 20:36:19', FALSE, TRUE,
        TRUE);

-- 菜单类
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES ('fce10205237e4774840b171d44d3c416', '最新入库', '/doc/fce10205237e4774840b171d44d3c416/latest', 'channel:*:newest',
                                            '菜单类', NULL, 10,
                                            'd7ff797575604fd1b9960ab10c30d668', '2017-02-21 20:36:19', FALSE, TRUE,
        TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES ('1d4b1455eef54443a01dd77468225f52', '分类文档', '/doc/page', 'channel:*:category', '菜单类', NULL, 20,
                                            'd7ff797575604fd1b9960ab10c30d668', '2017-02-21 20:36:19', FALSE, TRUE,
        TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES
  ('dc34a2cbf05840e0b457d9d17b648243', '数信学院', NULL, 'category:info', '菜单类', '1d4b1455eef54443a01dd77468225f52', 10,
                                       'd7ff797575604fd1b9960ab10c30d668', '2017-02-21 20:36:19', FALSE, TRUE, TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES ('7c8cf9e601344bb98cd8b5b610d08549', '软件工程', NULL, 'category:info:softEngr', '菜单类',
                                            'dc34a2cbf05840e0b457d9d17b648243', 10, 'd7ff797575604fd1b9960ab10c30d668',
                                            '2017-02-21 20:36:20', FALSE, TRUE, TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES ('37826d7fb34b471f8643f6945ab582fe', 'Java语言程序设计', NULL, 'category:info:softEngr:java', '菜单类',
                                            '7c8cf9e601344bb98cd8b5b610d08549', 10, 'd7ff797575604fd1b9960ab10c30d668',
                                            '2017-02-21 20:36:20', TRUE, TRUE, TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES ('21b22dfac3b84f179428375345a96eab', '软件开发基础（.java）', NULL, 'category:info:softEngr:dev_java', '菜单类',
                                            '7c8cf9e601344bb98cd8b5b610d08549', 20, 'd7ff797575604fd1b9960ab10c30d668',
                                            '2017-02-21 20:36:20', TRUE, TRUE, TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES
  ('b9a5c1880e4e4abf96483842544af502', '网络工程', NULL, 'category:info:netEngr', '菜单类', 'dc34a2cbf05840e0b457d9d17b648243',
                                       20, 'd7ff797575604fd1b9960ab10c30d668', '2017-02-21 20:36:20', FALSE, TRUE,
   TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES ('2d270deadc544187a7234cc2d21215e6', '数字电路与逻辑设计', NULL, 'category:info:netEngr:digitalCircuit', '菜单类',
                                            'b9a5c1880e4e4abf96483842544af502', 10, 'd7ff797575604fd1b9960ab10c30d668',
                                            '2017-02-21 20:36:20', TRUE, TRUE, TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES ('f654d8ecc5fc43be83b660d50dce499c', '计算机网络技术', NULL, 'category:info:softEngr:computerNetTech', '菜单类',
                                            'b9a5c1880e4e4abf96483842544af502', 20, 'd7ff797575604fd1b9960ab10c30d668',
                                            '2017-02-21 20:36:20', TRUE, TRUE, TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES
  ('9d5ad5ceb3d54a1d89f0cade4d4806f9', '兽医学院', NULL, 'category:veterinary', '菜单类', '1d4b1455eef54443a01dd77468225f52',
                                       20, 'd7ff797575604fd1b9960ab10c30d668', '2017-02-21 20:36:20', FALSE, TRUE,
   TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES
  ('eaedb90bf2854530b137533e1aee25e7', '动物医学', NULL, 'category:info:vetMed', '菜单类', '9d5ad5ceb3d54a1d89f0cade4d4806f9',
                                       10, 'd7ff797575604fd1b9960ab10c30d668', '2017-02-21 20:36:20', FALSE, TRUE,
   TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES ('1461e4cd3ec84ec68bdcc4c544af0597', '动物解剖学', NULL, 'category:info:vetMed:vetAnatomy', '菜单类',
                                            'eaedb90bf2854530b137533e1aee25e7', 10, 'd7ff797575604fd1b9960ab10c30d668',
                                            '2017-02-21 20:36:20', TRUE, TRUE, TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES ('4621a7fb843f4398b6e5af0f349bccd3', '动物生物化学', NULL, 'category:info:vetMed:vetChemistry', '菜单类',
                                            'eaedb90bf2854530b137533e1aee25e7', 20, 'd7ff797575604fd1b9960ab10c30d668',
                                            '2017-02-21 20:36:20', TRUE, TRUE, TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES
  ('e61fe8e97b294e9287416fefb04b7cec', '动物药学', NULL, 'category:info:vetPha', '菜单类', '9d5ad5ceb3d54a1d89f0cade4d4806f9',
                                       20, 'd7ff797575604fd1b9960ab10c30d668', '2017-02-21 20:36:20', FALSE, TRUE,
   TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES ('e56689b586ff495fa4bf66f73aa4b9f6', '动物免疫学', NULL, 'category:info:vetPha:vetImmune', '菜单类',
                                            'e61fe8e97b294e9287416fefb04b7cec', 10, 'd7ff797575604fd1b9960ab10c30d668',
                                            '2017-02-21 20:36:20', TRUE, TRUE, TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES ('9d996cd154f74d459d0c2a44b8b68bef', '动物毒理学', NULL, 'category:info:vetPha:vetToxicology', '菜单类',
                                            'e61fe8e97b294e9287416fefb04b7cec', 20, 'd7ff797575604fd1b9960ab10c30d668',
                                            '2017-02-21 20:36:20', TRUE, TRUE, TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES
  ('ba72dc0eeffe4d53b37f39bb5f7dd62e', '林学院', NULL, 'category:forestry', '菜单类', '1d4b1455eef54443a01dd77468225f52', 30,
                                       'd7ff797575604fd1b9960ab10c30d668', '2017-02-21 20:36:20', FALSE, TRUE, TRUE);

INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES ('ce36470d5c6a49f48a3a29b6868ebb24', '优质文档', '/doc/excellence', 'channel:*:excellence', '菜单类', NULL, 30,
                                            'd7ff797575604fd1b9960ab10c30d668', '2017-02-21 20:36:20', FALSE, TRUE,
        TRUE);
INSERT INTO perm (id, perm_name, perm_url, perm_pattern, perm_type, parent_id, weight, creator, created_time, attachable, active, valid)
VALUES ('4173c83dae5e437b9aacb1b995dfe311', '关于本站', '/about', 'channel:*:aboutUs', '菜单类', NULL, 40,
                                            'd7ff797575604fd1b9960ab10c30d668', '2017-02-21 20:36:20', FALSE, TRUE,
        TRUE);
-- 角色授权，目前四大类角色：游客，注册用户，管理员，主人
INSERT INTO role_perm (role_id, perm_id)
VALUES ('b0b8a48302f44b68b613409c312a1f4e', '1461e4cd3ec84ec68bdcc4c544af0597'),
  ('b0b8a48302f44b68b613409c312a1f4e', '1d4b1455eef54443a01dd77468225f52'),
  ('b0b8a48302f44b68b613409c312a1f4e', '21b22dfac3b84f179428375345a96eab'),
  ('b0b8a48302f44b68b613409c312a1f4e', '2d270deadc544187a7234cc2d21215e6'),
  ('b0b8a48302f44b68b613409c312a1f4e', '37826d7fb34b471f8643f6945ab582fe'),
  ('b0b8a48302f44b68b613409c312a1f4e', '4173c83dae5e437b9aacb1b995dfe311'),
  ('b0b8a48302f44b68b613409c312a1f4e', '4621a7fb843f4398b6e5af0f349bccd3'),
  ('b0b8a48302f44b68b613409c312a1f4e', '7c8cf9e601344bb98cd8b5b610d08549'),
  ('b0b8a48302f44b68b613409c312a1f4e', '9d5ad5ceb3d54a1d89f0cade4d4806f9'),
  ('b0b8a48302f44b68b613409c312a1f4e', '9d996cd154f74d459d0c2a44b8b68bef'),
  ('b0b8a48302f44b68b613409c312a1f4e', 'b9a5c1880e4e4abf96483842544af502'),
  ('b0b8a48302f44b68b613409c312a1f4e', 'ba72dc0eeffe4d53b37f39bb5f7dd62e'),
  ('b0b8a48302f44b68b613409c312a1f4e', 'ce36470d5c6a49f48a3a29b6868ebb24'),
  ('b0b8a48302f44b68b613409c312a1f4e', 'dc34a2cbf05840e0b457d9d17b648243'),
  ('b0b8a48302f44b68b613409c312a1f4e', 'e56689b586ff495fa4bf66f73aa4b9f6'),
  ('b0b8a48302f44b68b613409c312a1f4e', 'e61fe8e97b294e9287416fefb04b7cec'),
  ('b0b8a48302f44b68b613409c312a1f4e', 'eaedb90bf2854530b137533e1aee25e7'),
  ('b0b8a48302f44b68b613409c312a1f4e', 'f654d8ecc5fc43be83b660d50dce499c'),
  ('b0b8a48302f44b68b613409c312a1f4e', 'fce10205237e4774840b171d44d3c416');
INSERT INTO role_perm (role_id, perm_id)
VALUES ('85d4eb03bc3b407e93a447ee5e27d4c4', '1461e4cd3ec84ec68bdcc4c544af0597'),
  ('85d4eb03bc3b407e93a447ee5e27d4c4', '1d4b1455eef54443a01dd77468225f52'),
  ('85d4eb03bc3b407e93a447ee5e27d4c4', '21b22dfac3b84f179428375345a96eab'),
  ('85d4eb03bc3b407e93a447ee5e27d4c4', '2d270deadc544187a7234cc2d21215e6'),
  ('85d4eb03bc3b407e93a447ee5e27d4c4', '37826d7fb34b471f8643f6945ab582fe'),
  ('85d4eb03bc3b407e93a447ee5e27d4c4', '4173c83dae5e437b9aacb1b995dfe311'),
  ('85d4eb03bc3b407e93a447ee5e27d4c4', '4621a7fb843f4398b6e5af0f349bccd3'),
  ('85d4eb03bc3b407e93a447ee5e27d4c4', '7c8cf9e601344bb98cd8b5b610d08549'),
  ('85d4eb03bc3b407e93a447ee5e27d4c4', '9d5ad5ceb3d54a1d89f0cade4d4806f9'),
  ('85d4eb03bc3b407e93a447ee5e27d4c4', '9d996cd154f74d459d0c2a44b8b68bef'),
  ('85d4eb03bc3b407e93a447ee5e27d4c4', 'b9a5c1880e4e4abf96483842544af502'),
  ('85d4eb03bc3b407e93a447ee5e27d4c4', 'ba72dc0eeffe4d53b37f39bb5f7dd62e'),
  ('85d4eb03bc3b407e93a447ee5e27d4c4', 'ce36470d5c6a49f48a3a29b6868ebb24'),
  ('85d4eb03bc3b407e93a447ee5e27d4c4', 'dc34a2cbf05840e0b457d9d17b648243'),
  ('85d4eb03bc3b407e93a447ee5e27d4c4', 'e56689b586ff495fa4bf66f73aa4b9f6'),
  ('85d4eb03bc3b407e93a447ee5e27d4c4', 'e61fe8e97b294e9287416fefb04b7cec'),
  ('85d4eb03bc3b407e93a447ee5e27d4c4', 'eaedb90bf2854530b137533e1aee25e7'),
  ('85d4eb03bc3b407e93a447ee5e27d4c4', 'f654d8ecc5fc43be83b660d50dce499c'),
  ('85d4eb03bc3b407e93a447ee5e27d4c4', 'fce10205237e4774840b171d44d3c416');
INSERT INTO role_perm (role_id, perm_id)
VALUES ('4cee7a676d984191a0ab89cf679a55a1', '1461e4cd3ec84ec68bdcc4c544af0597'),
  ('4cee7a676d984191a0ab89cf679a55a1', '1d4b1455eef54443a01dd77468225f52'),
  ('4cee7a676d984191a0ab89cf679a55a1', '21b22dfac3b84f179428375345a96eab'),
  ('4cee7a676d984191a0ab89cf679a55a1', '2d270deadc544187a7234cc2d21215e6'),
  ('4cee7a676d984191a0ab89cf679a55a1', '37826d7fb34b471f8643f6945ab582fe'),
  ('4cee7a676d984191a0ab89cf679a55a1', '4173c83dae5e437b9aacb1b995dfe311'),
  ('4cee7a676d984191a0ab89cf679a55a1', '4621a7fb843f4398b6e5af0f349bccd3'),
  ('4cee7a676d984191a0ab89cf679a55a1', '7c8cf9e601344bb98cd8b5b610d08549'),
  ('4cee7a676d984191a0ab89cf679a55a1', '9d5ad5ceb3d54a1d89f0cade4d4806f9'),
  ('4cee7a676d984191a0ab89cf679a55a1', '9d996cd154f74d459d0c2a44b8b68bef'),
  ('4cee7a676d984191a0ab89cf679a55a1', 'b9a5c1880e4e4abf96483842544af502'),
  ('4cee7a676d984191a0ab89cf679a55a1', 'ba72dc0eeffe4d53b37f39bb5f7dd62e'),
  ('4cee7a676d984191a0ab89cf679a55a1', 'ce36470d5c6a49f48a3a29b6868ebb24'),
  ('4cee7a676d984191a0ab89cf679a55a1', 'dc34a2cbf05840e0b457d9d17b648243'),
  ('4cee7a676d984191a0ab89cf679a55a1', 'e56689b586ff495fa4bf66f73aa4b9f6'),
  ('4cee7a676d984191a0ab89cf679a55a1', 'e61fe8e97b294e9287416fefb04b7cec'),
  ('4cee7a676d984191a0ab89cf679a55a1', 'eaedb90bf2854530b137533e1aee25e7'),
  ('4cee7a676d984191a0ab89cf679a55a1', 'f654d8ecc5fc43be83b660d50dce499c'),
  ('4cee7a676d984191a0ab89cf679a55a1', 'fce10205237e4774840b171d44d3c416');
INSERT INTO role_perm (role_id, perm_id)
VALUES ('b763e366db0e40bb8c3101227b080821', '1461e4cd3ec84ec68bdcc4c544af0597'),
  ('b763e366db0e40bb8c3101227b080821', '1d4b1455eef54443a01dd77468225f52'),
  ('b763e366db0e40bb8c3101227b080821', '21b22dfac3b84f179428375345a96eab'),
  ('b763e366db0e40bb8c3101227b080821', '2d270deadc544187a7234cc2d21215e6'),
  ('b763e366db0e40bb8c3101227b080821', '37826d7fb34b471f8643f6945ab582fe'),
  ('b763e366db0e40bb8c3101227b080821', '4173c83dae5e437b9aacb1b995dfe311'),
  ('b763e366db0e40bb8c3101227b080821', '4621a7fb843f4398b6e5af0f349bccd3'),
  ('b763e366db0e40bb8c3101227b080821', '7c8cf9e601344bb98cd8b5b610d08549'),
  ('b763e366db0e40bb8c3101227b080821', '9d5ad5ceb3d54a1d89f0cade4d4806f9'),
  ('b763e366db0e40bb8c3101227b080821', '9d996cd154f74d459d0c2a44b8b68bef'),
  ('b763e366db0e40bb8c3101227b080821', 'b9a5c1880e4e4abf96483842544af502'),
  ('b763e366db0e40bb8c3101227b080821', 'ba72dc0eeffe4d53b37f39bb5f7dd62e'),
  ('b763e366db0e40bb8c3101227b080821', 'ce36470d5c6a49f48a3a29b6868ebb24'),
  ('b763e366db0e40bb8c3101227b080821', 'dc34a2cbf05840e0b457d9d17b648243'),
  ('b763e366db0e40bb8c3101227b080821', 'e56689b586ff495fa4bf66f73aa4b9f6'),
  ('b763e366db0e40bb8c3101227b080821', 'e61fe8e97b294e9287416fefb04b7cec'),
  ('b763e366db0e40bb8c3101227b080821', 'eaedb90bf2854530b137533e1aee25e7'),
  ('b763e366db0e40bb8c3101227b080821', 'f654d8ecc5fc43be83b660d50dce499c'),
  ('b763e366db0e40bb8c3101227b080821', 'fce10205237e4774840b171d44d3c416');
INSERT INTO role_perm (role_id, perm_id)
VALUES ('b0b8a48302f44b68b613409c312a1f4e', '0445ba623d534ec2878801c252a86d1a');
INSERT INTO role_perm (role_id, perm_id)
VALUES ('85d4eb03bc3b407e93a447ee5e27d4c4', '0445ba623d534ec2878801c252a86d1a'),
  ('85d4eb03bc3b407e93a447ee5e27d4c4', '43b9d6210cd64e7a84bd2cc33aa01649'),
  ('85d4eb03bc3b407e93a447ee5e27d4c4', '67eb917edb9f4b60b353612553ea61a0'),
  ('85d4eb03bc3b407e93a447ee5e27d4c4', '97a32f2cb13e4f1386e3fabc7711a07a'),
  ('85d4eb03bc3b407e93a447ee5e27d4c4', '8a76458cbdf4416896e995aee25a6558');
INSERT INTO role_perm (role_id, perm_id)
VALUES ('4cee7a676d984191a0ab89cf679a55a1', '0445ba623d534ec2878801c252a86d1a'),
  ('4cee7a676d984191a0ab89cf679a55a1', '43b9d6210cd64e7a84bd2cc33aa01649'),
  ('4cee7a676d984191a0ab89cf679a55a1', '67eb917edb9f4b60b353612553ea61a0'),
  ('4cee7a676d984191a0ab89cf679a55a1', '940be8feb63d44c6b018641883ae7255'),
  ('4cee7a676d984191a0ab89cf679a55a1', '97a32f2cb13e4f1386e3fabc7711a07a'),
  ('4cee7a676d984191a0ab89cf679a55a1', 'bf135b028d344c688f98e9745deaa26c'),
  ('4cee7a676d984191a0ab89cf679a55a1', '8a76458cbdf4416896e995aee25a6558');
INSERT INTO role_perm (role_id, perm_id)
VALUES ('b763e366db0e40bb8c3101227b080821', '0445ba623d534ec2878801c252a86d1a'),
  ('b763e366db0e40bb8c3101227b080821', '43b9d6210cd64e7a84bd2cc33aa01649'),
  ('b763e366db0e40bb8c3101227b080821', '67eb917edb9f4b60b353612553ea61a0'),
  ('b763e366db0e40bb8c3101227b080821', '940be8feb63d44c6b018641883ae7255'),
  ('b763e366db0e40bb8c3101227b080821', '97a32f2cb13e4f1386e3fabc7711a07a'),
  ('b763e366db0e40bb8c3101227b080821', 'bf135b028d344c688f98e9745deaa26c'),
  ('b763e366db0e40bb8c3101227b080821', '8a76458cbdf4416896e995aee25a6558');
INSERT INTO role_perm (role_id, perm_id)
VALUES ('4cee7a676d984191a0ab89cf679a55a1', '829d8bd064a8422c8e84b1c695cd582f'),
  ('4cee7a676d984191a0ab89cf679a55a1', '421bd40c477e41edb4424e67628b88ff'),
  ('4cee7a676d984191a0ab89cf679a55a1', '4e2b2a20451747938cbb1af202be92ea');
INSERT INTO role_perm (role_id, perm_id)
VALUES ('b763e366db0e40bb8c3101227b080821', '829d8bd064a8422c8e84b1c695cd582f'),
  ('b763e366db0e40bb8c3101227b080821', '421bd40c477e41edb4424e67628b88ff'),
  ('b763e366db0e40bb8c3101227b080821', '4e2b2a20451747938cbb1af202be92ea');

INSERT INTO role_perm (role_id, perm_id) VALUES
  ('4cee7a676d984191a0ab89cf679a55a1', 'e8007ad580f340ffb93535de1b608a0a'),
  ('b763e366db0e40bb8c3101227b080821', 'e8007ad580f340ffb93535de1b608a0a');

-- 授予用户角色，通用用户：visitor，至高用户：system
INSERT INTO account_role (account_id, role_id)
VALUES ('d7ff797575604fd1b9960ab10c30d668', 'b763e366db0e40bb8c3101227b080821');
INSERT INTO account_role (account_id, role_id)
VALUES ('9cf78b0ed096438bbc0f45a7fc8cfd0c', 'b0b8a48302f44b68b613409c312a1f4e');
INSERT INTO account_role (account_id, role_id)
VALUES ('93f6787ea384402bbb9503caef8c5765', '4cee7a676d984191a0ab89cf679a55a1');