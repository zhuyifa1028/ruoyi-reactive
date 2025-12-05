ALTER TABLE gen_table
    ADD (data_name VARCHAR2 (200) DEFAULT '');

COMMENT ON COLUMN gen_table.data_name IS '数据源名称';

UPDATE sys_menu
SET path      = 'powerjob',
    component = 'monitor/powerjob/index',
    perms     = 'monitor:powerjob:list',
    remark    = 'powerjob控制台菜单'
WHERE menu_id = 120;

-- ----------------------------
-- 第三方平台授权表
-- ----------------------------
CREATE TABLE sys_social
(
    id NUMBER (20) NOT NULL,
    user_id NUMBER (20) NOT NULL,
    tenant_id VARCHAR2 (20) DEFAULT NULL,
    auth_id VARCHAR2 (255) NOT NULL,
    source VARCHAR2 (255) NOT NULL,
    open_id VARCHAR2 (255) DEFAULT NULL,
    user_name VARCHAR2 (30) NOT NULL,
    nick_name VARCHAR2 (30) DEFAULT '',
    email VARCHAR2 (255) DEFAULT '',
    avatar VARCHAR2 (500) DEFAULT '',
    access_token VARCHAR2 (255) NOT NULL,
    expire_in NUMBER (20) DEFAULT NULL,
    refresh_token VARCHAR2 (255) DEFAULT NULL,
    access_code VARCHAR2 (255) DEFAULT NULL,
    union_id VARCHAR2 (255) DEFAULT NULL,
    scope VARCHAR2 (255) DEFAULT NULL,
    token_type VARCHAR2 (255) DEFAULT NULL,
    id_token VARCHAR2 (255) DEFAULT NULL,
    mac_algorithm VARCHAR2 (255) DEFAULT NULL,
    mac_key VARCHAR2 (255) DEFAULT NULL,
    code VARCHAR2 (255) DEFAULT NULL,
    oauth_token VARCHAR2 (255) DEFAULT NULL,
    oauth_token_secret VARCHAR2 (255) DEFAULT NULL,
    create_dept NUMBER (20),
    create_by NUMBER (20),
    create_time date,
    update_by NUMBER (20),
    update_time date,
    del_flag    char(1) DEFAULT '0'
);

ALTER TABLE sys_social
    ADD CONSTRAINT pk_sys_social PRIMARY KEY (id);

COMMENT ON TABLE   sys_social                   IS '社会化关系表';
COMMENT ON COLUMN  sys_social.id                IS '主键';
COMMENT ON COLUMN  sys_social.user_id           IS '用户ID';
COMMENT ON COLUMN  sys_social.tenant_id         IS '租户id';
COMMENT ON COLUMN  sys_social.auth_id           IS '平台+平台唯一id';
COMMENT ON COLUMN  sys_social.source            IS '用户来源';
COMMENT ON COLUMN  sys_social.open_id           IS '平台编号唯一id';
COMMENT ON COLUMN  sys_social.user_name         IS '登录账号';
COMMENT ON COLUMN  sys_social.nick_name         IS '用户昵称';
COMMENT ON COLUMN  sys_social.email             IS '用户邮箱';
COMMENT ON COLUMN  sys_social.avatar            IS '头像地址';
COMMENT ON COLUMN  sys_social.access_token      IS '用户的授权令牌';
COMMENT ON COLUMN  sys_social.expire_in         IS '用户的授权令牌的有效期，部分平台可能没有';
COMMENT ON COLUMN  sys_social.refresh_token     IS '刷新令牌，部分平台可能没有';
COMMENT ON COLUMN  sys_social.access_code       IS '平台的授权信息，部分平台可能没有';
COMMENT ON COLUMN  sys_social.union_id          IS '用户的 unionid';
COMMENT ON COLUMN  sys_social.scope             IS '授予的权限，部分平台可能没有';
COMMENT ON COLUMN  sys_social.token_type        IS '个别平台的授权信息，部分平台可能没有';
COMMENT ON COLUMN  sys_social.id_token          IS 'id token，部分平台可能没有';
COMMENT ON COLUMN  sys_social.mac_algorithm     IS '小米平台用户的附带属性，部分平台可能没有';
COMMENT ON COLUMN  sys_social.mac_key           IS '小米平台用户的附带属性，部分平台可能没有';
COMMENT ON COLUMN  sys_social.code              IS '用户的授权code，部分平台可能没有';
COMMENT ON COLUMN  sys_social.oauth_token       IS 'Twitter平台用户的附带属性，部分平台可能没有';
COMMENT ON COLUMN  sys_social.oauth_token_secret IS 'Twitter平台用户的附带属性，部分平台可能没有';
COMMENT ON COLUMN  sys_social.create_dept       IS '创建部门';
COMMENT ON COLUMN  sys_social.create_by         IS '创建者';
COMMENT ON COLUMN  sys_social.create_time       IS '创建时间';
COMMENT ON COLUMN  sys_social.update_by         IS '更新者';
COMMENT ON COLUMN  sys_social.update_time       IS '更新时间';
COMMENT ON COLUMN  sys_social.del_flag          IS '删除标志（0代表存在 2代表删除）';


-- ----------------------------
-- 系统授权表
-- ----------------------------
CREATE TABLE sys_client
(
    id NUMBER (20) NOT NULL,
    client_id VARCHAR2 (64) DEFAULT NULL,
    client_key VARCHAR2 (32) DEFAULT NULL,
    client_secret VARCHAR2 (255) DEFAULT NULL,
    grant_type VARCHAR2 (255) DEFAULT NULL,
    device_type VARCHAR2 (32) DEFAULT NULL,
    active_timeout NUMBER (11) DEFAULT 1800,
    timeout NUMBER (11) DEFAULT 604800,
    status      char(1) DEFAULT '0',
    del_flag    char(1) DEFAULT '0',
    create_dept NUMBER (20) DEFAULT NULL,
    create_by NUMBER (20) DEFAULT NULL,
    create_time date,
    update_by NUMBER (20) DEFAULT NULL,
    update_time date
);

ALTER TABLE sys_client
    ADD CONSTRAINT pk_sys_client PRIMARY KEY (id);

COMMENT ON TABLE sys_client                         IS '系统授权表';
COMMENT ON COLUMN sys_client.id                     IS '主键';
COMMENT ON COLUMN sys_client.client_id              IS '客户端id';
COMMENT ON COLUMN sys_client.client_key             IS '客户端key';
COMMENT ON COLUMN sys_client.client_secret          IS '客户端秘钥';
COMMENT ON COLUMN sys_client.grant_type             IS '授权类型';
COMMENT ON COLUMN sys_client.device_type            IS '设备类型';
COMMENT ON COLUMN sys_client.active_timeout         IS 'token活跃超时时间';
COMMENT ON COLUMN sys_client.timeout                IS 'token固定超时';
COMMENT ON COLUMN sys_client.status                 IS '状态（0正常 1停用）';
COMMENT ON COLUMN sys_client.del_flag               IS '删除标志（0代表存在 2代表删除）';
COMMENT ON COLUMN sys_client.create_dept            IS '创建部门';
COMMENT ON COLUMN sys_client.create_by              IS '创建者';
COMMENT ON COLUMN sys_client.create_time            IS '创建时间';
COMMENT ON COLUMN sys_client.update_by              IS '更新者';
COMMENT ON COLUMN sys_client.update_time            IS '更新时间';

INSERT INTO sys_client
VALUES (1, 'e5cd7e4891bf95d1d19206ce24a7b32e', 'pc', 'pc123', 'password,social', 'pc', 1800, 604800, 0, 0, 103, 1, sysdate, 1, sysdate);
INSERT INTO sys_client
VALUES (2, '428a8310cd442757ae699df5d894f051', 'app', 'app123', 'password,sms,social', 'android', 1800, 604800, 0, 0, 103, 1, sysdate, 1, sysdate);

INSERT INTO sys_dict_type
VALUES (11, '000000', '授权类型', 'sys_grant_type', '0', 103, 1, sysdate, NULL, NULL, '认证授权类型');
INSERT INTO sys_dict_type
VALUES (12, '000000', '设备类型', 'sys_device_type', '0', 103, 1, sysdate, NULL, NULL, '客户端设备类型');

INSERT INTO sys_dict_data
VALUES (30, '000000', 0, '密码认证', 'password', 'sys_grant_type', '', 'default', 'N', '0', 103, 1, sysdate, NULL, NULL, '密码认证');
INSERT INTO sys_dict_data
VALUES (31, '000000', 0, '短信认证', 'sms', 'sys_grant_type', '', 'default', 'N', '0', 103, 1, sysdate, NULL, NULL, '短信认证');
INSERT INTO sys_dict_data
VALUES (32, '000000', 0, '邮件认证', 'email', 'sys_grant_type', '', 'default', 'N', '0', 103, 1, sysdate, NULL, NULL, '邮件认证');
INSERT INTO sys_dict_data
VALUES (33, '000000', 0, '小程序认证', 'xcx', 'sys_grant_type', '', 'default', 'N', '0', 103, 1, sysdate, NULL, NULL, '小程序认证');
INSERT INTO sys_dict_data
VALUES (34, '000000', 0, '三方登录认证', 'social', 'sys_grant_type', '', 'default', 'N', '0', 103, 1, sysdate, NULL, NULL, '三方登录认证');
INSERT INTO sys_dict_data
VALUES (35, '000000', 0, 'PC', 'pc', 'sys_device_type', '', 'default', 'N', '0', 103, 1, sysdate, NULL, NULL, 'PC');
INSERT INTO sys_dict_data
VALUES (36, '000000', 0, '安卓', 'android', 'sys_device_type', '', 'default', 'N', '0', 103, 1, sysdate, NULL, NULL, '安卓');
INSERT INTO sys_dict_data
VALUES (37, '000000', 0, 'iOS', 'ios', 'sys_device_type', '', 'default', 'N', '0', 103, 1, sysdate, NULL, NULL, 'iOS');
INSERT INTO sys_dict_data
VALUES (38, '000000', 0, '小程序', 'xcx', 'sys_device_type', '', 'default', 'N', '0', 103, 1, sysdate, NULL, NULL, '小程序');

-- 二级菜单
INSERT INTO sys_menu
VALUES ('123', '客户端管理', '1', '11', 'client', 'system/client/index', '', 1, 0, 'C', '0', '0', 'system:client:list', 'international', 103, 1, sysdate, NULL, NULL, '客户端管理菜单');
-- 客户端管理按钮
INSERT INTO sys_menu
VALUES ('1061', '客户端管理查询', '123', '1', '#', '', '', 1, 0, 'F', '0', '0', 'system:client:query', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1062', '客户端管理新增', '123', '2', '#', '', '', 1, 0, 'F', '0', '0', 'system:client:add', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1063', '客户端管理修改', '123', '3', '#', '', '', 1, 0, 'F', '0', '0', 'system:client:edit', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1064', '客户端管理删除', '123', '4', '#', '', '', 1, 0, 'F', '0', '0', 'system:client:remove', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1065', '客户端管理导出', '123', '5', '#', '', '', 1, 0, 'F', '0', '0', 'system:client:export', '#', 103, 1, sysdate, NULL, NULL, '');

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
    MODIFY (leader NUMBER (20))
