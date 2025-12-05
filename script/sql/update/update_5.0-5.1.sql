ALTER TABLE gen_table
    ADD COLUMN data_name varchar(200) NULL DEFAULT '' COMMENT '数据源名称' AFTER table_id;

UPDATE sys_menu
SET path      = 'powerjob',
    component = 'monitor/powerjob/index',
    perms     = 'monitor:powerjob:list',
    remark    = 'powerjob控制台菜单'
WHERE menu_id = 120;

-- ----------------------------
-- 第三方平台授权表
-- ----------------------------
DROP TABLE IF EXISTS sys_social;
CREATE TABLE sys_social
(
    id                 bigint       NOT NULL COMMENT '主键',
    user_id            bigint       NOT NULL COMMENT '用户ID',
    tenant_id          varchar(20)  DEFAULT NULL COMMENT '租户id',
    auth_id            varchar(255) NOT NULL COMMENT '平台+平台唯一id',
    source             varchar(255) NOT NULL COMMENT '用户来源',
    open_id            varchar(255) DEFAULT NULL COMMENT '平台编号唯一id',
    user_name          varchar(30)  NOT NULL COMMENT '登录账号',
    nick_name          varchar(30)  DEFAULT '' COMMENT '用户昵称',
    email              varchar(255) DEFAULT '' COMMENT '用户邮箱',
    avatar             varchar(500) DEFAULT '' COMMENT '头像地址',
    access_token       varchar(255) NOT NULL COMMENT '用户的授权令牌',
    expire_in          int          DEFAULT NULL COMMENT '用户的授权令牌的有效期，部分平台可能没有',
    refresh_token      varchar(255) DEFAULT NULL COMMENT '刷新令牌，部分平台可能没有',
    access_code        varchar(255) DEFAULT NULL COMMENT '平台的授权信息，部分平台可能没有',
    union_id           varchar(255) DEFAULT NULL COMMENT '用户的 unionid',
    scope              varchar(255) DEFAULT NULL COMMENT '授予的权限，部分平台可能没有',
    token_type         varchar(255) DEFAULT NULL COMMENT '个别平台的授权信息，部分平台可能没有',
    id_token           varchar(255) DEFAULT NULL COMMENT 'id token，部分平台可能没有',
    mac_algorithm      varchar(255) DEFAULT NULL COMMENT '小米平台用户的附带属性，部分平台可能没有',
    mac_key            varchar(255) DEFAULT NULL COMMENT '小米平台用户的附带属性，部分平台可能没有',
    code               varchar(255) DEFAULT NULL COMMENT '用户的授权code，部分平台可能没有',
    oauth_token        varchar(255) DEFAULT NULL COMMENT 'Twitter平台用户的附带属性，部分平台可能没有',
    oauth_token_secret varchar(255) DEFAULT NULL COMMENT 'Twitter平台用户的附带属性，部分平台可能没有',
    create_dept        bigint(20) COMMENT '创建部门',
    create_by          bigint(20) COMMENT '创建者',
    create_time        datetime COMMENT '创建时间',
    update_by          bigint(20) COMMENT '更新者',
    update_time        datetime COMMENT '更新时间',
    del_flag           char(1)      DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    PRIMARY KEY (id)
) ENGINE = innodb COMMENT = '社会化关系表';


-- ----------------------------
-- 系统授权表
-- ----------------------------
DROP TABLE IF EXISTS sys_client;
CREATE TABLE sys_client
(
    id             bigint(20) NOT NULL COMMENT 'id',
    client_id      varchar(64)  DEFAULT NULL COMMENT '客户端id',
    client_key     varchar(32)  DEFAULT NULL COMMENT '客户端key',
    client_secret  varchar(255) DEFAULT NULL COMMENT '客户端秘钥',
    grant_type     varchar(255) DEFAULT NULL COMMENT '授权类型',
    device_type    varchar(32)  DEFAULT NULL COMMENT '设备类型',
    active_timeout int(11)      DEFAULT 1800 COMMENT 'token活跃超时时间',
    timeout        int(11)      DEFAULT 604800 COMMENT 'token固定超时',
    status         char(1)      DEFAULT '0' COMMENT '状态（0正常 1停用）',
    del_flag       char(1)      DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    create_dept    bigint(20)   DEFAULT NULL COMMENT '创建部门',
    create_by      bigint(20)   DEFAULT NULL COMMENT '创建者',
    create_time    datetime     DEFAULT NULL COMMENT '创建时间',
    update_by      bigint(20)   DEFAULT NULL COMMENT '更新者',
    update_time    datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE = innodb COMMENT ='系统授权表';

INSERT INTO sys_client
VALUES (1, 'e5cd7e4891bf95d1d19206ce24a7b32e', 'pc', 'pc123', 'password,social', 'pc', 1800, 604800, 0, 0, 103, 1, SYSDATE(), 1, SYSDATE());
INSERT INTO sys_client
VALUES (2, '428a8310cd442757ae699df5d894f051', 'app', 'app123', 'password,sms,social', 'android', 1800, 604800, 0, 0, 103, 1, SYSDATE(), 1, SYSDATE());

INSERT INTO sys_dict_type
VALUES (11, '000000', '授权类型', 'sys_grant_type', 103, 1, SYSDATE(), NULL, NULL, '认证授权类型');
INSERT INTO sys_dict_type
VALUES (12, '000000', '设备类型', 'sys_device_type', 103, 1, SYSDATE(), NULL, NULL, '客户端设备类型');

INSERT INTO sys_dict_data
VALUES (30, '000000', 0, '密码认证', 'password', 'sys_grant_type', '', 'default', 'N', 103, 1, SYSDATE(), NULL, NULL, '密码认证');
INSERT INTO sys_dict_data
VALUES (31, '000000', 0, '短信认证', 'sms', 'sys_grant_type', '', 'default', 'N', 103, 1, SYSDATE(), NULL, NULL, '短信认证');
INSERT INTO sys_dict_data
VALUES (32, '000000', 0, '邮件认证', 'email', 'sys_grant_type', '', 'default', 'N', 103, 1, SYSDATE(), NULL, NULL, '邮件认证');
INSERT INTO sys_dict_data
VALUES (33, '000000', 0, '小程序认证', 'xcx', 'sys_grant_type', '', 'default', 'N', 103, 1, SYSDATE(), NULL, NULL, '小程序认证');
INSERT INTO sys_dict_data
VALUES (34, '000000', 0, '三方登录认证', 'social', 'sys_grant_type', '', 'default', 'N', 103, 1, SYSDATE(), NULL, NULL, '三方登录认证');
INSERT INTO sys_dict_data
VALUES (35, '000000', 0, 'PC', 'pc', 'sys_device_type', '', 'default', 'N', 103, 1, SYSDATE(), NULL, NULL, 'PC');
INSERT INTO sys_dict_data
VALUES (36, '000000', 0, '安卓', 'android', 'sys_device_type', '', 'default', 'N', 103, 1, SYSDATE(), NULL, NULL, '安卓');
INSERT INTO sys_dict_data
VALUES (37, '000000', 0, 'iOS', 'ios', 'sys_device_type', '', 'default', 'N', 103, 1, SYSDATE(), NULL, NULL, 'iOS');
INSERT INTO sys_dict_data
VALUES (38, '000000', 0, '小程序', 'xcx', 'sys_device_type', '', 'default', 'N', 103, 1, SYSDATE(), NULL, NULL, '小程序');

-- 二级菜单
INSERT INTO sys_menu
VALUES ('123', '客户端管理', '1', '11', 'client', 'system/client/index', '', 1, 0, 'C', '0', '0', 'system:client:list', 'international', 103, 1, SYSDATE(), NULL, NULL, '客户端管理菜单');
-- 客户端管理按钮
INSERT INTO sys_menu
VALUES ('1061', '客户端管理查询', '123', '1', '#', '', '', 1, 0, 'F', '0', '0', 'system:client:query', '#', 103, 1, SYSDATE(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1062', '客户端管理新增', '123', '2', '#', '', '', 1, 0, 'F', '0', '0', 'system:client:add', '#', 103, 1, SYSDATE(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1063', '客户端管理修改', '123', '3', '#', '', '', 1, 0, 'F', '0', '0', 'system:client:edit', '#', 103, 1, SYSDATE(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1064', '客户端管理删除', '123', '4', '#', '', '', 1, 0, 'F', '0', '0', 'system:client:remove', '#', 103, 1, SYSDATE(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1065', '客户端管理导出', '123', '5', '#', '', '', 1, 0, 'F', '0', '0', 'system:client:export', '#', 103, 1, SYSDATE(), NULL, NULL, '');

-- 角色菜单权限
INSERT INTO sys_role_menu
VALUES ('2', '1061');
INSERT INTO sys_role_menu
VALUES ('2', '1062');
INSERT INTO sys_role_menu
VALUES ('2', '1063');
INSERT INTO sys_role_menu
VALUES ('2', '1064');
INSERT INTO sys_role_menu
VALUES ('2', '1065');


UPDATE sys_dept
SET leader = NULL;
ALTER TABLE sys_dept
    MODIFY COLUMN leader bigint NULL DEFAULT NULL COMMENT '负责人' AFTER order_num;
