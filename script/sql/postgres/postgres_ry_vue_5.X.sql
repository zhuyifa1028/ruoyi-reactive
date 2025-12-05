-- ----------------------------
-- 第三方平台授权表
-- ----------------------------
CREATE TABLE sys_social
(
    id                 int8          NOT NULL,
    user_id            int8          NOT NULL,
    tenant_id          varchar(20)   DEFAULT '000000'::VARCHAR,
    auth_id            varchar(255)  NOT NULL,
    source             varchar(255)  NOT NULL,
    open_id            varchar(255)  DEFAULT NULL::VARCHAR,
    user_name          varchar(30)   NOT NULL,
    nick_name          varchar(30)   DEFAULT ''::VARCHAR,
    email              varchar(255)  DEFAULT ''::VARCHAR,
    avatar             varchar(500)  DEFAULT ''::VARCHAR,
    access_token       varchar(2000) NOT NULL,
    expire_in          int8          DEFAULT NULL,
    refresh_token      varchar(2000) DEFAULT NULL::VARCHAR,
    access_code        varchar(255)  DEFAULT NULL::VARCHAR,
    union_id           varchar(255)  DEFAULT NULL::VARCHAR,
    scope              varchar(255)  DEFAULT NULL::VARCHAR,
    token_type         varchar(255)  DEFAULT NULL::VARCHAR,
    id_token           varchar(2000) DEFAULT NULL::VARCHAR,
    mac_algorithm      varchar(255)  DEFAULT NULL::VARCHAR,
    mac_key            varchar(255)  DEFAULT NULL::VARCHAR,
    code               varchar(255)  DEFAULT NULL::VARCHAR,
    oauth_token        varchar(255)  DEFAULT NULL::VARCHAR,
    oauth_token_secret varchar(255)  DEFAULT NULL::VARCHAR,
    create_dept        int8,
    create_by          int8,
    create_time        timestamp,
    update_by          int8,
    update_time        timestamp,
    del_flag           char          DEFAULT '0'::bpchar,
    CONSTRAINT "pk_sys_social" PRIMARY KEY (id)
);

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
COMMENT ON COLUMN  sys_social.del_flag          IS '删除标志（0代表存在 1代表删除）';

-- ----------------------------
-- 租户表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_tenant
(
    id                int8,
    tenant_id         varchar(20) NOT NULL,
    contact_user_name varchar(20)  DEFAULT NULL::VARCHAR,
    contact_phone     varchar(20)  DEFAULT NULL::VARCHAR,
    company_name      varchar(30)  DEFAULT NULL::VARCHAR,
    license_number    varchar(30)  DEFAULT NULL::VARCHAR,
    address           varchar(200) DEFAULT NULL::VARCHAR,
    intro             varchar(200) DEFAULT NULL::VARCHAR,
    domain            varchar(200) DEFAULT NULL::VARCHAR,
    remark            varchar(200) DEFAULT NULL::VARCHAR,
    package_id        int8,
    expire_time       timestamp,
    account_count     int4         DEFAULT -1,
    status            char         DEFAULT '0'::bpchar,
    del_flag          char         DEFAULT '0'::bpchar,
    create_dept       int8,
    create_by         int8,
    create_time       timestamp,
    update_by         int8,
    update_time       timestamp,
    CONSTRAINT "pk_sys_tenant" PRIMARY KEY (id)
);


COMMENT ON TABLE   sys_tenant                    IS '租户表';
COMMENT ON COLUMN  sys_tenant.tenant_id          IS '租户编号';
COMMENT ON COLUMN  sys_tenant.contact_phone      IS '联系电话';
COMMENT ON COLUMN  sys_tenant.company_name       IS '企业名称';
COMMENT ON COLUMN  sys_tenant.company_name       IS '联系人';
COMMENT ON COLUMN  sys_tenant.license_number     IS '统一社会信用代码';
COMMENT ON COLUMN  sys_tenant.address            IS '地址';
COMMENT ON COLUMN  sys_tenant.intro              IS '企业简介';
COMMENT ON COLUMN  sys_tenant.domain             IS '域名';
COMMENT ON COLUMN  sys_tenant.remark             IS '备注';
COMMENT ON COLUMN  sys_tenant.package_id         IS '租户套餐编号';
COMMENT ON COLUMN  sys_tenant.expire_time        IS '过期时间';
COMMENT ON COLUMN  sys_tenant.account_count      IS '用户数量（-1不限制）';
COMMENT ON COLUMN  sys_tenant.status             IS '租户状态（0正常 1停用）';
COMMENT ON COLUMN  sys_tenant.del_flag           IS '删除标志（0代表存在 1代表删除）';
COMMENT ON COLUMN  sys_tenant.create_dept        IS '创建部门';
COMMENT ON COLUMN  sys_tenant.create_by          IS '创建者';
COMMENT ON COLUMN  sys_tenant.create_time        IS '创建时间';
COMMENT ON COLUMN  sys_tenant.update_by          IS '更新者';
COMMENT ON COLUMN  sys_tenant.update_time        IS '更新时间';


-- ----------------------------
-- 初始化-租户表数据
-- ----------------------------

INSERT INTO sys_tenant
VALUES (1, '000000', '管理组', '15888888888', 'XXX有限公司', NULL, NULL, '多租户通用后台管理管理系统', NULL, NULL, NULL, NULL, -1, '0', '0', 103, 1, NOW(), NULL, NULL);


-- ----------------------------
-- 租户套餐表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_tenant_package
(
    package_id          int8,
    package_name        varchar(20)   DEFAULT ''::VARCHAR,
    menu_ids            varchar(3000) DEFAULT ''::VARCHAR,
    remark              varchar(200)  DEFAULT ''::VARCHAR,
    menu_check_strictly bool          DEFAULT TRUE,
    status              char          DEFAULT '0'::bpchar,
    del_flag            char          DEFAULT '0'::bpchar,
    create_dept         int8,
    create_by           int8,
    create_time         timestamp,
    update_by           int8,
    update_time         timestamp,
    CONSTRAINT "pk_sys_tenant_package" PRIMARY KEY (package_id)
);


COMMENT ON TABLE   sys_tenant_package                    IS '租户套餐表';
COMMENT ON COLUMN  sys_tenant_package.package_id         IS '租户套餐id';
COMMENT ON COLUMN  sys_tenant_package.package_name       IS '套餐名称';
COMMENT ON COLUMN  sys_tenant_package.menu_ids           IS '关联菜单id';
COMMENT ON COLUMN  sys_tenant_package.remark             IS '备注';
COMMENT ON COLUMN  sys_tenant_package.status             IS '状态（0正常 1停用）';
COMMENT ON COLUMN  sys_tenant_package.del_flag           IS '删除标志（0代表存在 1代表删除）';
COMMENT ON COLUMN  sys_tenant_package.create_dept        IS '创建部门';
COMMENT ON COLUMN  sys_tenant_package.create_by          IS '创建者';
COMMENT ON COLUMN  sys_tenant_package.create_time        IS '创建时间';
COMMENT ON COLUMN  sys_tenant_package.update_by          IS '更新者';
COMMENT ON COLUMN  sys_tenant_package.update_time        IS '更新时间';


-- ----------------------------
-- 1、部门表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_dept
(
    dept_id       int8,
    tenant_id     varchar(20)  DEFAULT '000000'::VARCHAR,
    parent_id     int8         DEFAULT 0,
    ancestors     varchar(500) DEFAULT ''::VARCHAR,
    dept_name     varchar(30)  DEFAULT ''::VARCHAR,
    dept_category varchar(100) DEFAULT NULL::VARCHAR,
    order_num     int4         DEFAULT 0,
    leader        int8         DEFAULT NULL,
    phone         varchar(11)  DEFAULT NULL::VARCHAR,
    email         varchar(50)  DEFAULT NULL::VARCHAR,
    status        char         DEFAULT '0'::bpchar,
    del_flag      char         DEFAULT '0'::bpchar,
    create_dept   int8,
    create_by     int8,
    create_time   timestamp,
    update_by     int8,
    update_time   timestamp,
    CONSTRAINT "sys_dept_pk" PRIMARY KEY (dept_id)
);

COMMENT ON TABLE sys_dept               IS '部门表';
COMMENT ON COLUMN sys_dept.dept_id      IS '部门ID';
COMMENT ON COLUMN sys_dept.tenant_id    IS '租户编号';
COMMENT ON COLUMN sys_dept.parent_id    IS '父部门ID';
COMMENT ON COLUMN sys_dept.ancestors    IS '祖级列表';
COMMENT ON COLUMN sys_dept.dept_name    IS '部门名称';
COMMENT ON COLUMN sys_dept.dept_category    IS '部门类别编码';
COMMENT ON COLUMN sys_dept.order_num    IS '显示顺序';
COMMENT ON COLUMN sys_dept.leader       IS '负责人';
COMMENT ON COLUMN sys_dept.phone        IS '联系电话';
COMMENT ON COLUMN sys_dept.email        IS '邮箱';
COMMENT ON COLUMN sys_dept.status       IS '部门状态（0正常 1停用）';
COMMENT ON COLUMN sys_dept.del_flag     IS '删除标志（0代表存在 1代表删除）';
COMMENT ON COLUMN sys_dept.create_dept  IS '创建部门';
COMMENT ON COLUMN sys_dept.create_by    IS '创建者';
COMMENT ON COLUMN sys_dept.create_time  IS '创建时间';
COMMENT ON COLUMN sys_dept.update_by    IS '更新者';
COMMENT ON COLUMN sys_dept.update_time  IS '更新时间';

-- ----------------------------
-- 初始化-部门表数据
-- ----------------------------
INSERT INTO sys_dept
VALUES (100, '000000', 0, '0', 'XXX科技', NULL, 0, NULL, '15888888888', 'xxx@qq.com', '0', '0', 103, 1, NOW(), NULL, NULL);
INSERT INTO sys_dept
VALUES (101, '000000', 100, '0,100', '深圳总公司', NULL, 1, NULL, '15888888888', 'xxx@qq.com', '0', '0', 103, 1, NOW(), NULL, NULL);
INSERT INTO sys_dept
VALUES (102, '000000', 100, '0,100', '长沙分公司', NULL, 2, NULL, '15888888888', 'xxx@qq.com', '0', '0', 103, 1, NOW(), NULL, NULL);
INSERT INTO sys_dept
VALUES (103, '000000', 101, '0,100,101', '研发部门', NULL, 1, 1, '15888888888', 'xxx@qq.com', '0', '0', 103, 1, NOW(), NULL, NULL);
INSERT INTO sys_dept
VALUES (104, '000000', 101, '0,100,101', '市场部门', NULL, 2, NULL, '15888888888', 'xxx@qq.com', '0', '0', 103, 1, NOW(), NULL, NULL);
INSERT INTO sys_dept
VALUES (105, '000000', 101, '0,100,101', '测试部门', NULL, 3, NULL, '15888888888', 'xxx@qq.com', '0', '0', 103, 1, NOW(), NULL, NULL);
INSERT INTO sys_dept
VALUES (106, '000000', 101, '0,100,101', '财务部门', NULL, 4, NULL, '15888888888', 'xxx@qq.com', '0', '0', 103, 1, NOW(), NULL, NULL);
INSERT INTO sys_dept
VALUES (107, '000000', 101, '0,100,101', '运维部门', NULL, 5, NULL, '15888888888', 'xxx@qq.com', '0', '0', 103, 1, NOW(), NULL, NULL);
INSERT INTO sys_dept
VALUES (108, '000000', 102, '0,100,102', '市场部门', NULL, 1, NULL, '15888888888', 'xxx@qq.com', '0', '0', 103, 1, NOW(), NULL, NULL);
INSERT INTO sys_dept
VALUES (109, '000000', 102, '0,100,102', '财务部门', NULL, 2, NULL, '15888888888', 'xxx@qq.com', '0', '0', 103, 1, NOW(), NULL, NULL);

-- ----------------------------
-- 2、用户信息表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_user
(
    user_id     int8,
    tenant_id   varchar(20)  DEFAULT '000000'::VARCHAR,
    dept_id     int8,
    user_name   varchar(30) NOT NULL,
    nick_name   varchar(30) NOT NULL,
    user_type   varchar(10)  DEFAULT 'sys_user'::VARCHAR,
    email       varchar(50)  DEFAULT ''::VARCHAR,
    phonenumber varchar(11)  DEFAULT ''::VARCHAR,
    sex         char         DEFAULT '0'::bpchar,
    avatar      int8,
    password    varchar(100) DEFAULT ''::VARCHAR,
    status      char         DEFAULT '0'::bpchar,
    del_flag    char         DEFAULT '0'::bpchar,
    login_ip    varchar(128) DEFAULT ''::VARCHAR,
    login_date  timestamp,
    create_dept int8,
    create_by   int8,
    create_time timestamp,
    update_by   int8,
    update_time timestamp,
    remark      varchar(500) DEFAULT NULL::VARCHAR,
    CONSTRAINT "sys_user_pk" PRIMARY KEY (user_id)
);

COMMENT ON TABLE sys_user               IS '用户信息表';
COMMENT ON COLUMN sys_user.user_id      IS '用户ID';
COMMENT ON COLUMN sys_user.tenant_id    IS '租户编号';
COMMENT ON COLUMN sys_user.dept_id      IS '部门ID';
COMMENT ON COLUMN sys_user.user_name    IS '用户账号';
COMMENT ON COLUMN sys_user.nick_name    IS '用户昵称';
COMMENT ON COLUMN sys_user.user_type    IS '用户类型（sys_user系统用户）';
COMMENT ON COLUMN sys_user.email        IS '用户邮箱';
COMMENT ON COLUMN sys_user.phonenumber  IS '手机号码';
COMMENT ON COLUMN sys_user.sex          IS '用户性别（0男 1女 2未知）';
COMMENT ON COLUMN sys_user.avatar       IS '头像地址';
COMMENT ON COLUMN sys_user.password     IS '密码';
COMMENT ON COLUMN sys_user.status       IS '帐号状态（0正常 1停用）';
COMMENT ON COLUMN sys_user.del_flag     IS '删除标志（0代表存在 1代表删除）';
COMMENT ON COLUMN sys_user.login_ip     IS '最后登陆IP';
COMMENT ON COLUMN sys_user.login_date   IS '最后登陆时间';
COMMENT ON COLUMN sys_user.create_dept  IS '创建部门';
COMMENT ON COLUMN sys_user.create_by    IS '创建者';
COMMENT ON COLUMN sys_user.create_time  IS '创建时间';
COMMENT ON COLUMN sys_user.update_by    IS '更新者';
COMMENT ON COLUMN sys_user.update_time  IS '更新时间';
COMMENT ON COLUMN sys_user.remark       IS '备注';

-- ----------------------------

-- 初始化-用户信息表数据
-- ----------------------------
INSERT INTO sys_user
VALUES (1, '000000', 103, 'admin', '疯狂的狮子Li', 'sys_user', 'crazyLionLi@163.com', '15888888888', '1', NULL, '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', NOW(), 103, 1, NOW(), NULL, NULL, '管理员');
INSERT INTO sys_user
VALUES (3, '000000', 108, 'test', '本部门及以下 密码666666', 'sys_user', '', '', '0', NULL, '$2a$10$b8yUzN0C71sbz.PhNOCgJe.Tu1yWC3RNrTyjSQ8p1W0.aaUXUJ.Ne', '0', '0', '127.0.0.1', NOW(), 103, 1, NOW(), 3, NOW(), NULL);
INSERT INTO sys_user
VALUES (4, '000000', 102, 'test1', '仅本人 密码666666', 'sys_user', '', '', '0', NULL, '$2a$10$b8yUzN0C71sbz.PhNOCgJe.Tu1yWC3RNrTyjSQ8p1W0.aaUXUJ.Ne', '0', '0', '127.0.0.1', NOW(), 103, 1, NOW(), 4, NOW(), NULL);

-- ----------------------------
-- 3、岗位信息表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_post
(
    post_id       int8,
    tenant_id     varchar(20)  DEFAULT '000000'::VARCHAR,
    dept_id       int8,
    post_code     varchar(64) NOT NULL,
    post_category varchar(100) DEFAULT NULL,
    post_name     varchar(50) NOT NULL,
    post_sort     int4        NOT NULL,
    status        char        NOT NULL,
    create_dept   int8,
    create_by     int8,
    create_time   timestamp,
    update_by     int8,
    update_time   timestamp,
    remark        varchar(500) DEFAULT NULL::VARCHAR,
    CONSTRAINT "sys_post_pk" PRIMARY KEY (post_id)
);

COMMENT ON TABLE sys_post               IS '岗位信息表';
COMMENT ON COLUMN sys_post.post_id      IS '岗位ID';
COMMENT ON COLUMN sys_post.tenant_id    IS '租户编号';
COMMENT ON COLUMN sys_post.dept_id      IS '部门id';
COMMENT ON COLUMN sys_post.post_code    IS '岗位编码';
COMMENT ON COLUMN sys_post.post_category IS '岗位类别编码';
COMMENT ON COLUMN sys_post.post_name    IS '岗位名称';
COMMENT ON COLUMN sys_post.post_sort    IS '显示顺序';
COMMENT ON COLUMN sys_post.status       IS '状态（0正常 1停用）';
COMMENT ON COLUMN sys_post.create_dept  IS '创建部门';
COMMENT ON COLUMN sys_post.create_by    IS '创建者';
COMMENT ON COLUMN sys_post.create_time  IS '创建时间';
COMMENT ON COLUMN sys_post.update_by    IS '更新者';
COMMENT ON COLUMN sys_post.update_time  IS '更新时间';
COMMENT ON COLUMN sys_post.remark       IS '备注';

-- ----------------------------
-- 初始化-岗位信息表数据
-- ----------------------------
INSERT INTO sys_post
VALUES (1, '000000', 103, 'ceo', NULL, '董事长', 1, '0', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_post
VALUES (2, '000000', 100, 'se', NULL, '项目经理', 2, '0', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_post
VALUES (3, '000000', 100, 'hr', NULL, '人力资源', 3, '0', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_post
VALUES (4, '000000', 100, 'user', NULL, '普通员工', 4, '0', 103, 1, NOW(), NULL, NULL, '');

-- ----------------------------
-- 4、角色信息表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_role
(
    role_id             int8,
    tenant_id           varchar(20)  DEFAULT '000000'::VARCHAR,
    role_name           varchar(30)  NOT NULL,
    role_key            varchar(100) NOT NULL,
    role_sort           int4         NOT NULL,
    data_scope          char         DEFAULT '1'::bpchar,
    menu_check_strictly bool         DEFAULT TRUE,
    dept_check_strictly bool         DEFAULT TRUE,
    status              char         NOT NULL,
    del_flag            char         DEFAULT '0'::bpchar,
    create_dept         int8,
    create_by           int8,
    create_time         timestamp,
    update_by           int8,
    update_time         timestamp,
    remark              varchar(500) DEFAULT NULL::VARCHAR,
    CONSTRAINT "sys_role_pk" PRIMARY KEY (role_id)
);

COMMENT ON TABLE sys_role                       IS '角色信息表';
COMMENT ON COLUMN sys_role.role_id              IS '角色ID';
COMMENT ON COLUMN sys_role.tenant_id            IS '租户编号';
COMMENT ON COLUMN sys_role.role_name            IS '角色名称';
COMMENT ON COLUMN sys_role.role_key             IS '角色权限字符串';
COMMENT ON COLUMN sys_role.role_sort            IS '显示顺序';
COMMENT ON COLUMN sys_role.data_scope           IS '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限 5：仅本人数据权限 6：部门及以下或本人数据权限）';
COMMENT ON COLUMN sys_role.menu_check_strictly  IS '菜单树选择项是否关联显示';
COMMENT ON COLUMN sys_role.dept_check_strictly  IS '部门树选择项是否关联显示';
COMMENT ON COLUMN sys_role.status               IS '角色状态（0正常 1停用）';
COMMENT ON COLUMN sys_role.del_flag             IS '删除标志（0代表存在 1代表删除）';
COMMENT ON COLUMN sys_role.create_dept          IS '创建部门';
COMMENT ON COLUMN sys_role.create_by            IS '创建者';
COMMENT ON COLUMN sys_role.create_time          IS '创建时间';
COMMENT ON COLUMN sys_role.update_by            IS '更新者';
COMMENT ON COLUMN sys_role.update_time          IS '更新时间';
COMMENT ON COLUMN sys_role.remark               IS '备注';

-- ----------------------------
-- 初始化-角色信息表数据
-- ----------------------------
INSERT INTO sys_role
VALUES ('1', '000000', '超级管理员', 'superadmin', 1, '1', 't', 't', '0', '0', 103, 1, NOW(), NULL, NULL, '超级管理员');
INSERT INTO sys_role
VALUES ('3', '000000', '本部门及以下', 'test1', 3, '4', 't', 't', '0', '0', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_role
VALUES ('4', '000000', '仅本人', 'test2', 4, '5', 't', 't', '0', '0', 103, 1, NOW(), NULL, NULL, '');

-- ----------------------------
-- 5、菜单权限表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_menu
(
    menu_id     int8,
    menu_name   varchar(50) NOT NULL,
    parent_id   int8         DEFAULT 0,
    order_num   int4         DEFAULT 0,
    path        varchar(200) DEFAULT ''::VARCHAR,
    component   varchar(255) DEFAULT NULL::VARCHAR,
    query_param varchar(255) DEFAULT NULL::VARCHAR,
    is_frame    char         DEFAULT '1'::bpchar,
    is_cache    char         DEFAULT '0'::bpchar,
    menu_type   char         DEFAULT ''::bpchar,
    visible     char         DEFAULT '0'::bpchar,
    status      char         DEFAULT '0'::bpchar,
    perms       varchar(100) DEFAULT NULL::VARCHAR,
    icon        varchar(100) DEFAULT '#'::VARCHAR,
    create_dept int8,
    create_by   int8,
    create_time timestamp,
    update_by   int8,
    update_time timestamp,
    remark      varchar(500) DEFAULT ''::VARCHAR,
    CONSTRAINT "sys_menu_pk" PRIMARY KEY (menu_id)
);

COMMENT ON TABLE sys_menu               IS '菜单权限表';
COMMENT ON COLUMN sys_menu.menu_id      IS '菜单ID';
COMMENT ON COLUMN sys_menu.menu_name    IS '菜单名称';
COMMENT ON COLUMN sys_menu.parent_id    IS '父菜单ID';
COMMENT ON COLUMN sys_menu.order_num    IS '显示顺序';
COMMENT ON COLUMN sys_menu.path         IS '路由地址';
COMMENT ON COLUMN sys_menu.component    IS '组件路径';
COMMENT ON COLUMN sys_menu.query_param  IS '路由参数';
COMMENT ON COLUMN sys_menu.is_frame     IS '是否为外链（0是 1否）';
COMMENT ON COLUMN sys_menu.is_cache     IS '是否缓存（0缓存 1不缓存）';
COMMENT ON COLUMN sys_menu.menu_type    IS '菜单类型（M目录 C菜单 F按钮）';
COMMENT ON COLUMN sys_menu.visible      IS '显示状态（0显示 1隐藏）';
COMMENT ON COLUMN sys_menu.status       IS '菜单状态（0正常 1停用）';
COMMENT ON COLUMN sys_menu.perms        IS '权限标识';
COMMENT ON COLUMN sys_menu.icon         IS '菜单图标';
COMMENT ON COLUMN sys_menu.create_dept  IS '创建部门';
COMMENT ON COLUMN sys_menu.create_by    IS '创建者';
COMMENT ON COLUMN sys_menu.create_time  IS '创建时间';
COMMENT ON COLUMN sys_menu.update_by    IS '更新者';
COMMENT ON COLUMN sys_menu.update_time  IS '更新时间';
COMMENT ON COLUMN sys_menu.remark       IS '备注';

-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
-- 一级菜单
INSERT INTO sys_menu
VALUES ('1', '系统管理', '0', '1', 'system', NULL, '', '1', '0', 'M', '0', '0', '', 'system', 103, 1, NOW(), NULL, NULL, '系统管理目录');
INSERT INTO sys_menu
VALUES ('6', '租户管理', '0', '2', 'tenant', NULL, '', '1', '0', 'M', '0', '0', '', 'chart', 103, 1, NOW(), NULL, NULL, '租户管理目录');
INSERT INTO sys_menu
VALUES ('2', '系统监控', '0', '3', 'monitor', NULL, '', '1', '0', 'M', '0', '0', '', 'monitor', 103, 1, NOW(), NULL, NULL, '系统监控目录');
INSERT INTO sys_menu
VALUES ('3', '系统工具', '0', '4', 'tool', NULL, '', '1', '0', 'M', '0', '0', '', 'tool', 103, 1, NOW(), NULL, NULL, '系统工具目录');
INSERT INTO sys_menu
VALUES ('4', 'PLUS官网', '0', '5', 'https://gitee.com/dromara/RuoYi-Vue-Plus', NULL, '', '0', '0', 'M', '0', '0', '', 'guide', 103, 1, NOW(), NULL, NULL, 'RuoYi-Vue-Plus官网地址');
INSERT INTO sys_menu
VALUES ('5', '测试菜单', '0', '5', 'demo', NULL, '', '1', '0', 'M', '0', '0', NULL, 'star', 103, 1, NOW(), NULL, NULL, '测试菜单');
-- 二级菜单
INSERT INTO sys_menu
VALUES ('100', '用户管理', '1', '1', 'user', 'system/user/index', '', '1', '0', 'C', '0', '0', 'system:user:list', 'user', 103, 1, NOW(), NULL, NULL, '用户管理菜单');
INSERT INTO sys_menu
VALUES ('101', '角色管理', '1', '2', 'role', 'system/role/index', '', '1', '0', 'C', '0', '0', 'system:role:list', 'peoples', 103, 1, NOW(), NULL, NULL, '角色管理菜单');
INSERT INTO sys_menu
VALUES ('102', '菜单管理', '1', '3', 'menu', 'system/menu/index', '', '1', '0', 'C', '0', '0', 'system:menu:list', 'tree-table', 103, 1, NOW(), NULL, NULL, '菜单管理菜单');
INSERT INTO sys_menu
VALUES ('103', '部门管理', '1', '4', 'dept', 'system/dept/index', '', '1', '0', 'C', '0', '0', 'system:dept:list', 'tree', 103, 1, NOW(), NULL, NULL, '部门管理菜单');
INSERT INTO sys_menu
VALUES ('104', '岗位管理', '1', '5', 'post', 'system/post/index', '', '1', '0', 'C', '0', '0', 'system:post:list', 'post', 103, 1, NOW(), NULL, NULL, '岗位管理菜单');
INSERT INTO sys_menu
VALUES ('105', '字典管理', '1', '6', 'dict', 'system/dict/index', '', '1', '0', 'C', '0', '0', 'system:dict:list', 'dict', 103, 1, NOW(), NULL, NULL, '字典管理菜单');
INSERT INTO sys_menu
VALUES ('106', '参数设置', '1', '7', 'config', 'system/config/index', '', '1', '0', 'C', '0', '0', 'system:config:list', 'edit', 103, 1, NOW(), NULL, NULL, '参数设置菜单');
INSERT INTO sys_menu
VALUES ('107', '通知公告', '1', '8', 'notice', 'system/notice/index', '', '1', '0', 'C', '0', '0', 'system:notice:list', 'message', 103, 1, NOW(), NULL, NULL, '通知公告菜单');
INSERT INTO sys_menu
VALUES ('108', '日志管理', '1', '9', 'log', '', '', '1', '0', 'M', '0', '0', '', 'log', 103, 1, NOW(), NULL, NULL, '日志管理菜单');
INSERT INTO sys_menu
VALUES ('109', '在线用户', '2', '1', 'online', 'monitor/online/index', '', '1', '0', 'C', '0', '0', 'monitor:online:list', 'online', 103, 1, NOW(), NULL, NULL, '在线用户菜单');
INSERT INTO sys_menu
VALUES ('113', '缓存监控', '2', '5', 'cache', 'monitor/cache/index', '', '1', '0', 'C', '0', '0', 'monitor:cache:list', 'redis', 103, 1, NOW(), NULL, NULL, '缓存监控菜单');
INSERT INTO sys_menu
VALUES ('115', '代码生成', '3', '2', 'gen', 'tool/gen/index', '', '1', '0', 'C', '0', '0', 'tool:gen:list', 'code', 103, 1, NOW(), NULL, NULL, '代码生成菜单');
INSERT INTO sys_menu
VALUES ('121', '租户管理', '6', '1', 'tenant', 'system/tenant/index', '', '1', '0', 'C', '0', '0', 'system:tenant:list', 'list', 103, 1, NOW(), NULL, NULL, '租户管理菜单');
INSERT INTO sys_menu
VALUES ('122', '租户套餐管理', '6', '2', 'tenantPackage', 'system/tenantPackage/index', '', '1', '0', 'C', '0', '0', 'system:tenantPackage:list', 'form', 103, 1, NOW(), NULL, NULL, '租户套餐管理菜单');
INSERT INTO sys_menu
VALUES ('123', '客户端管理', '1', '11', 'client', 'system/client/index', '', '1', '0', 'C', '0', '0', 'system:client:list', 'international', 103, 1, NOW(), NULL, NULL, '客户端管理菜单');
INSERT INTO sys_menu
VALUES ('116', '修改生成配置', '3', '2', 'gen-edit/index/:tableId', 'tool/gen/editTable', '', '1', '1', 'C', '1', '0', 'tool:gen:edit', '#', 103, 1, NOW(), NULL, NULL, '/tool/gen');
INSERT INTO sys_menu
VALUES ('130', '分配用户', '1', '2', 'role-auth/user/:roleId', 'system/role/authUser', '', '1', '1', 'C', '1', '0', 'system:role:edit', '#', 103, 1, NOW(), NULL, NULL, '/system/role');
INSERT INTO sys_menu
VALUES ('131', '分配角色', '1', '1', 'user-auth/role/:userId', 'system/user/authRole', '', '1', '1', 'C', '1', '0', 'system:user:edit', '#', 103, 1, NOW(), NULL, NULL, '/system/user');
INSERT INTO sys_menu
VALUES ('132', '字典数据', '1', '6', 'dict-data/index/:dictId', 'system/dict/data', '', '1', '1', 'C', '1', '0', 'system:dict:list', '#', 103, 1, NOW(), NULL, NULL, '/system/dict');
INSERT INTO sys_menu
VALUES ('133', '文件配置管理', '1', '10', 'oss-config/index', 'system/oss/config', '', '1', '1', 'C', '1', '0', 'system:ossConfig:list', '#', 103, 1, NOW(), NULL, NULL, '/system/oss');

-- springboot-admin监控
INSERT INTO sys_menu
VALUES ('117', 'Admin监控', '2', '5', 'Admin', 'monitor/admin/index', '', '1', '0', 'C', '0', '0', 'monitor:admin:list', 'dashboard', 103, 1, NOW(), NULL, NULL, 'Admin监控菜单');
-- oss菜单
INSERT INTO sys_menu
VALUES ('118', '文件管理', '1', '10', 'oss', 'system/oss/index', '', '1', '0', 'C', '0', '0', 'system:oss:list', 'upload', 103, 1, NOW(), NULL, NULL, '文件管理菜单');
-- snail-job server控制台
INSERT INTO sys_menu
VALUES ('120', '任务调度中心', '2', '6', 'snailjob', 'monitor/snailjob/index', '', '1', '0', 'C', '0', '0', 'monitor:snailjob:list', 'job', 103, 1, NOW(), NULL, NULL, 'SnailJob控制台菜单');

-- 三级菜单
INSERT INTO sys_menu
VALUES ('500', '操作日志', '108', '1', 'operlog', 'monitor/operlog/index', '', '1', '0', 'C', '0', '0', 'monitor:operlog:list', 'form', 103, 1, NOW(), NULL, NULL, '操作日志菜单');
INSERT INTO sys_menu
VALUES ('501', '登录日志', '108', '2', 'logininfor', 'monitor/logininfor/index', '', '1', '0', 'C', '0', '0', 'monitor:logininfor:list', 'logininfor', 103, 1, NOW(), NULL, NULL, '登录日志菜单');
-- 用户管理按钮
INSERT INTO sys_menu
VALUES ('1001', '用户查询', '100', '1', '', '', '', '1', '0', 'F', '0', '0', 'system:user:query', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1002', '用户新增', '100', '2', '', '', '', '1', '0', 'F', '0', '0', 'system:user:add', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1003', '用户修改', '100', '3', '', '', '', '1', '0', 'F', '0', '0', 'system:user:edit', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1004', '用户删除', '100', '4', '', '', '', '1', '0', 'F', '0', '0', 'system:user:remove', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1005', '用户导出', '100', '5', '', '', '', '1', '0', 'F', '0', '0', 'system:user:export', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1006', '用户导入', '100', '6', '', '', '', '1', '0', 'F', '0', '0', 'system:user:import', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1007', '重置密码', '100', '7', '', '', '', '1', '0', 'F', '0', '0', 'system:user:resetPwd', '#', 103, 1, NOW(), NULL, NULL, '');
-- 角色管理按钮
INSERT INTO sys_menu
VALUES ('1008', '角色查询', '101', '1', '', '', '', '1', '0', 'F', '0', '0', 'system:role:query', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1009', '角色新增', '101', '2', '', '', '', '1', '0', 'F', '0', '0', 'system:role:add', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1010', '角色修改', '101', '3', '', '', '', '1', '0', 'F', '0', '0', 'system:role:edit', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1011', '角色删除', '101', '4', '', '', '', '1', '0', 'F', '0', '0', 'system:role:remove', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1012', '角色导出', '101', '5', '', '', '', '1', '0', 'F', '0', '0', 'system:role:export', '#', 103, 1, NOW(), NULL, NULL, '');
-- 菜单管理按钮
INSERT INTO sys_menu
VALUES ('1013', '菜单查询', '102', '1', '', '', '', '1', '0', 'F', '0', '0', 'system:menu:query', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1014', '菜单新增', '102', '2', '', '', '', '1', '0', 'F', '0', '0', 'system:menu:add', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1015', '菜单修改', '102', '3', '', '', '', '1', '0', 'F', '0', '0', 'system:menu:edit', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1016', '菜单删除', '102', '4', '', '', '', '1', '0', 'F', '0', '0', 'system:menu:remove', '#', 103, 1, NOW(), NULL, NULL, '');
-- 部门管理按钮
INSERT INTO sys_menu
VALUES ('1017', '部门查询', '103', '1', '', '', '', '1', '0', 'F', '0', '0', 'system:dept:query', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1018', '部门新增', '103', '2', '', '', '', '1', '0', 'F', '0', '0', 'system:dept:add', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1019', '部门修改', '103', '3', '', '', '', '1', '0', 'F', '0', '0', 'system:dept:edit', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1020', '部门删除', '103', '4', '', '', '', '1', '0', 'F', '0', '0', 'system:dept:remove', '#', 103, 1, NOW(), NULL, NULL, '');
-- 岗位管理按钮
INSERT INTO sys_menu
VALUES ('1021', '岗位查询', '104', '1', '', '', '', '1', '0', 'F', '0', '0', 'system:post:query', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1022', '岗位新增', '104', '2', '', '', '', '1', '0', 'F', '0', '0', 'system:post:add', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1023', '岗位修改', '104', '3', '', '', '', '1', '0', 'F', '0', '0', 'system:post:edit', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1024', '岗位删除', '104', '4', '', '', '', '1', '0', 'F', '0', '0', 'system:post:remove', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1025', '岗位导出', '104', '5', '', '', '', '1', '0', 'F', '0', '0', 'system:post:export', '#', 103, 1, NOW(), NULL, NULL, '');
-- 字典管理按钮
INSERT INTO sys_menu
VALUES ('1026', '字典查询', '105', '1', '#', '', '', '1', '0', 'F', '0', '0', 'system:dict:query', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1027', '字典新增', '105', '2', '#', '', '', '1', '0', 'F', '0', '0', 'system:dict:add', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1028', '字典修改', '105', '3', '#', '', '', '1', '0', 'F', '0', '0', 'system:dict:edit', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1029', '字典删除', '105', '4', '#', '', '', '1', '0', 'F', '0', '0', 'system:dict:remove', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1030', '字典导出', '105', '5', '#', '', '', '1', '0', 'F', '0', '0', 'system:dict:export', '#', 103, 1, NOW(), NULL, NULL, '');
-- 参数设置按钮
INSERT INTO sys_menu
VALUES ('1031', '参数查询', '106', '1', '#', '', '', '1', '0', 'F', '0', '0', 'system:config:query', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1032', '参数新增', '106', '2', '#', '', '', '1', '0', 'F', '0', '0', 'system:config:add', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1033', '参数修改', '106', '3', '#', '', '', '1', '0', 'F', '0', '0', 'system:config:edit', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1034', '参数删除', '106', '4', '#', '', '', '1', '0', 'F', '0', '0', 'system:config:remove', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1035', '参数导出', '106', '5', '#', '', '', '1', '0', 'F', '0', '0', 'system:config:export', '#', 103, 1, NOW(), NULL, NULL, '');
-- 通知公告按钮
INSERT INTO sys_menu
VALUES ('1036', '公告查询', '107', '1', '#', '', '', '1', '0', 'F', '0', '0', 'system:notice:query', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1037', '公告新增', '107', '2', '#', '', '', '1', '0', 'F', '0', '0', 'system:notice:add', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1038', '公告修改', '107', '3', '#', '', '', '1', '0', 'F', '0', '0', 'system:notice:edit', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1039', '公告删除', '107', '4', '#', '', '', '1', '0', 'F', '0', '0', 'system:notice:remove', '#', 103, 1, NOW(), NULL, NULL, '');
-- 操作日志按钮
INSERT INTO sys_menu
VALUES ('1040', '操作查询', '500', '1', '#', '', '', '1', '0', 'F', '0', '0', 'monitor:operlog:query', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1041', '操作删除', '500', '2', '#', '', '', '1', '0', 'F', '0', '0', 'monitor:operlog:remove', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1042', '日志导出', '500', '4', '#', '', '', '1', '0', 'F', '0', '0', 'monitor:operlog:export', '#', 103, 1, NOW(), NULL, NULL, '');
-- 登录日志按钮
INSERT INTO sys_menu
VALUES ('1043', '登录查询', '501', '1', '#', '', '', '1', '0', 'F', '0', '0', 'monitor:logininfor:query', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1044', '登录删除', '501', '2', '#', '', '', '1', '0', 'F', '0', '0', 'monitor:logininfor:remove', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1045', '日志导出', '501', '3', '#', '', '', '1', '0', 'F', '0', '0', 'monitor:logininfor:export', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1050', '账户解锁', '501', '4', '#', '', '', '1', '0', 'F', '0', '0', 'monitor:logininfor:unlock', '#', 103, 1, NOW(), NULL, NULL, '');
-- 在线用户按钮
INSERT INTO sys_menu
VALUES ('1046', '在线查询', '109', '1', '#', '', '', '1', '0', 'F', '0', '0', 'monitor:online:query', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1047', '批量强退', '109', '2', '#', '', '', '1', '0', 'F', '0', '0', 'monitor:online:batchLogout', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1048', '单条强退', '109', '3', '#', '', '', '1', '0', 'F', '0', '0', 'monitor:online:forceLogout', '#', 103, 1, NOW(), NULL, NULL, '');
-- 代码生成按钮
INSERT INTO sys_menu
VALUES ('1055', '生成查询', '115', '1', '#', '', '', '1', '0', 'F', '0', '0', 'tool:gen:query', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1056', '生成修改', '115', '2', '#', '', '', '1', '0', 'F', '0', '0', 'tool:gen:edit', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1057', '生成删除', '115', '3', '#', '', '', '1', '0', 'F', '0', '0', 'tool:gen:remove', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1058', '导入代码', '115', '2', '#', '', '', '1', '0', 'F', '0', '0', 'tool:gen:import', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1059', '预览代码', '115', '4', '#', '', '', '1', '0', 'F', '0', '0', 'tool:gen:preview', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1060', '生成代码', '115', '5', '#', '', '', '1', '0', 'F', '0', '0', 'tool:gen:code', '#', 103, 1, NOW(), NULL, NULL, '');
-- oss相关按钮
INSERT INTO sys_menu
VALUES ('1600', '文件查询', '118', '1', '#', '', '', '1', '0', 'F', '0', '0', 'system:oss:query', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1601', '文件上传', '118', '2', '#', '', '', '1', '0', 'F', '0', '0', 'system:oss:upload', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1602', '文件下载', '118', '3', '#', '', '', '1', '0', 'F', '0', '0', 'system:oss:download', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1603', '文件删除', '118', '4', '#', '', '', '1', '0', 'F', '0', '0', 'system:oss:remove', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1620', '配置列表', '118', '5', '#', '', '', '1', '0', 'F', '0', '0', 'system:ossConfig:list', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1621', '配置添加', '118', '6', '#', '', '', '1', '0', 'F', '0', '0', 'system:ossConfig:add', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1622', '配置编辑', '118', '6', '#', '', '', '1', '0', 'F', '0', '0', 'system:ossConfig:edit', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1623', '配置删除', '118', '6', '#', '', '', '1', '0', 'F', '0', '0', 'system:ossConfig:remove', '#', 103, 1, NOW(), NULL, NULL, '');
-- 租户管理相关按钮
INSERT INTO sys_menu
VALUES ('1606', '租户查询', '121', '1', '#', '', '', '1', '0', 'F', '0', '0', 'system:tenant:query', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1607', '租户新增', '121', '2', '#', '', '', '1', '0', 'F', '0', '0', 'system:tenant:add', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1608', '租户修改', '121', '3', '#', '', '', '1', '0', 'F', '0', '0', 'system:tenant:edit', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1609', '租户删除', '121', '4', '#', '', '', '1', '0', 'F', '0', '0', 'system:tenant:remove', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1610', '租户导出', '121', '5', '#', '', '', '1', '0', 'F', '0', '0', 'system:tenant:export', '#', 103, 1, NOW(), NULL, NULL, '');
-- 租户套餐管理相关按钮
INSERT INTO sys_menu
VALUES ('1611', '租户套餐查询', '122', '1', '#', '', '', '1', '0', 'F', '0', '0', 'system:tenantPackage:query', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1612', '租户套餐新增', '122', '2', '#', '', '', '1', '0', 'F', '0', '0', 'system:tenantPackage:add', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1613', '租户套餐修改', '122', '3', '#', '', '', '1', '0', 'F', '0', '0', 'system:tenantPackage:edit', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1614', '租户套餐删除', '122', '4', '#', '', '', '1', '0', 'F', '0', '0', 'system:tenantPackage:remove', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1615', '租户套餐导出', '122', '5', '#', '', '', '1', '0', 'F', '0', '0', 'system:tenantPackage:export', '#', 103, 1, NOW(), NULL, NULL, '');
-- 客户端管理按钮
INSERT INTO sys_menu
VALUES ('1061', '客户端管理查询', '123', '1', '#', '', '', '1', '0', 'F', '0', '0', 'system:client:query', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1062', '客户端管理新增', '123', '2', '#', '', '', '1', '0', 'F', '0', '0', 'system:client:add', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1063', '客户端管理修改', '123', '3', '#', '', '', '1', '0', 'F', '0', '0', 'system:client:edit', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1064', '客户端管理删除', '123', '4', '#', '', '', '1', '0', 'F', '0', '0', 'system:client:remove', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1065', '客户端管理导出', '123', '5', '#', '', '', '1', '0', 'F', '0', '0', 'system:client:export', '#', 103, 1, NOW(), NULL, NULL, '');
-- 测试菜单
INSERT INTO sys_menu
VALUES ('1500', '测试单表', '5', '1', 'demo', 'demo/demo/index', '', '1', '0', 'C', '0', '0', 'demo:demo:list', '#', 103, 1, NOW(), NULL, NULL, '测试单表菜单');
INSERT INTO sys_menu
VALUES ('1501', '测试单表查询', '1500', '1', '#', '', '', '1', '0', 'F', '0', '0', 'demo:demo:query', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1502', '测试单表新增', '1500', '2', '#', '', '', '1', '0', 'F', '0', '0', 'demo:demo:add', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1503', '测试单表修改', '1500', '3', '#', '', '', '1', '0', 'F', '0', '0', 'demo:demo:edit', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1504', '测试单表删除', '1500', '4', '#', '', '', '1', '0', 'F', '0', '0', 'demo:demo:remove', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1505', '测试单表导出', '1500', '5', '#', '', '', '1', '0', 'F', '0', '0', 'demo:demo:export', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1506', '测试树表', '5', '1', 'tree', 'demo/tree/index', '', '1', '0', 'C', '0', '0', 'demo:tree:list', '#', 103, 1, NOW(), NULL, NULL, '测试树表菜单');
INSERT INTO sys_menu
VALUES ('1507', '测试树表查询', '1506', '1', '#', '', '', '1', '0', 'F', '0', '0', 'demo:tree:query', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1508', '测试树表新增', '1506', '2', '#', '', '', '1', '0', 'F', '0', '0', 'demo:tree:add', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1509', '测试树表修改', '1506', '3', '#', '', '', '1', '0', 'F', '0', '0', 'demo:tree:edit', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1510', '测试树表删除', '1506', '4', '#', '', '', '1', '0', 'F', '0', '0', 'demo:tree:remove', '#', 103, 1, NOW(), NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1511', '测试树表导出', '1506', '5', '#', '', '', '1', '0', 'F', '0', '0', 'demo:tree:export', '#', 103, 1, NOW(), NULL, NULL, '');


-- ----------------------------
-- 6、用户和角色关联表  用户N-1角色
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_user_role
(
    user_id int8 NOT NULL,
    role_id int8 NOT NULL,
    CONSTRAINT sys_user_role_pk PRIMARY KEY (user_id, role_id)
);

COMMENT ON TABLE sys_user_role              IS '用户和角色关联表';
COMMENT ON COLUMN sys_user_role.user_id     IS '用户ID';
COMMENT ON COLUMN sys_user_role.role_id     IS '角色ID';

-- ----------------------------
-- 初始化-用户和角色关联表数据
-- ----------------------------
INSERT INTO sys_user_role
VALUES ('1', '1');
INSERT INTO sys_user_role
VALUES ('3', '3');
INSERT INTO sys_user_role
VALUES ('4', '4');

-- ----------------------------
-- 7、角色和菜单关联表  角色1-N菜单
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_role_menu
(
    role_id int8 NOT NULL,
    menu_id int8 NOT NULL,
    CONSTRAINT sys_role_menu_pk PRIMARY KEY (role_id, menu_id)
);

COMMENT ON TABLE sys_role_menu              IS '角色和菜单关联表';
COMMENT ON COLUMN sys_role_menu.role_id     IS '角色ID';
COMMENT ON COLUMN sys_role_menu.menu_id     IS '菜单ID';

-- ----------------------------
-- 初始化-角色和菜单关联表数据
-- ----------------------------
INSERT INTO sys_role_menu
VALUES ('3', '1');
INSERT INTO sys_role_menu
VALUES ('3', '5');
INSERT INTO sys_role_menu
VALUES ('3', '100');
INSERT INTO sys_role_menu
VALUES ('3', '101');
INSERT INTO sys_role_menu
VALUES ('3', '102');
INSERT INTO sys_role_menu
VALUES ('3', '103');
INSERT INTO sys_role_menu
VALUES ('3', '104');
INSERT INTO sys_role_menu
VALUES ('3', '105');
INSERT INTO sys_role_menu
VALUES ('3', '106');
INSERT INTO sys_role_menu
VALUES ('3', '107');
INSERT INTO sys_role_menu
VALUES ('3', '108');
INSERT INTO sys_role_menu
VALUES ('3', '118');
INSERT INTO sys_role_menu
VALUES ('3', '123');
INSERT INTO sys_role_menu
VALUES ('3', '130');
INSERT INTO sys_role_menu
VALUES ('3', '131');
INSERT INTO sys_role_menu
VALUES ('3', '132');
INSERT INTO sys_role_menu
VALUES ('3', '133');
INSERT INTO sys_role_menu
VALUES ('3', '500');
INSERT INTO sys_role_menu
VALUES ('3', '501');
INSERT INTO sys_role_menu
VALUES ('3', '1001');
INSERT INTO sys_role_menu
VALUES ('3', '1002');
INSERT INTO sys_role_menu
VALUES ('3', '1003');
INSERT INTO sys_role_menu
VALUES ('3', '1004');
INSERT INTO sys_role_menu
VALUES ('3', '1005');
INSERT INTO sys_role_menu
VALUES ('3', '1006');
INSERT INTO sys_role_menu
VALUES ('3', '1007');
INSERT INTO sys_role_menu
VALUES ('3', '1008');
INSERT INTO sys_role_menu
VALUES ('3', '1009');
INSERT INTO sys_role_menu
VALUES ('3', '1010');
INSERT INTO sys_role_menu
VALUES ('3', '1011');
INSERT INTO sys_role_menu
VALUES ('3', '1012');
INSERT INTO sys_role_menu
VALUES ('3', '1013');
INSERT INTO sys_role_menu
VALUES ('3', '1014');
INSERT INTO sys_role_menu
VALUES ('3', '1015');
INSERT INTO sys_role_menu
VALUES ('3', '1016');
INSERT INTO sys_role_menu
VALUES ('3', '1017');
INSERT INTO sys_role_menu
VALUES ('3', '1018');
INSERT INTO sys_role_menu
VALUES ('3', '1019');
INSERT INTO sys_role_menu
VALUES ('3', '1020');
INSERT INTO sys_role_menu
VALUES ('3', '1021');
INSERT INTO sys_role_menu
VALUES ('3', '1022');
INSERT INTO sys_role_menu
VALUES ('3', '1023');
INSERT INTO sys_role_menu
VALUES ('3', '1024');
INSERT INTO sys_role_menu
VALUES ('3', '1025');
INSERT INTO sys_role_menu
VALUES ('3', '1026');
INSERT INTO sys_role_menu
VALUES ('3', '1027');
INSERT INTO sys_role_menu
VALUES ('3', '1028');
INSERT INTO sys_role_menu
VALUES ('3', '1029');
INSERT INTO sys_role_menu
VALUES ('3', '1030');
INSERT INTO sys_role_menu
VALUES ('3', '1031');
INSERT INTO sys_role_menu
VALUES ('3', '1032');
INSERT INTO sys_role_menu
VALUES ('3', '1033');
INSERT INTO sys_role_menu
VALUES ('3', '1034');
INSERT INTO sys_role_menu
VALUES ('3', '1035');
INSERT INTO sys_role_menu
VALUES ('3', '1036');
INSERT INTO sys_role_menu
VALUES ('3', '1037');
INSERT INTO sys_role_menu
VALUES ('3', '1038');
INSERT INTO sys_role_menu
VALUES ('3', '1039');
INSERT INTO sys_role_menu
VALUES ('3', '1040');
INSERT INTO sys_role_menu
VALUES ('3', '1041');
INSERT INTO sys_role_menu
VALUES ('3', '1042');
INSERT INTO sys_role_menu
VALUES ('3', '1043');
INSERT INTO sys_role_menu
VALUES ('3', '1044');
INSERT INTO sys_role_menu
VALUES ('3', '1045');
INSERT INTO sys_role_menu
VALUES ('3', '1050');
INSERT INTO sys_role_menu
VALUES ('3', '1061');
INSERT INTO sys_role_menu
VALUES ('3', '1062');
INSERT INTO sys_role_menu
VALUES ('3', '1063');
INSERT INTO sys_role_menu
VALUES ('3', '1064');
INSERT INTO sys_role_menu
VALUES ('3', '1065');
INSERT INTO sys_role_menu
VALUES ('3', '1500');
INSERT INTO sys_role_menu
VALUES ('3', '1501');
INSERT INTO sys_role_menu
VALUES ('3', '1502');
INSERT INTO sys_role_menu
VALUES ('3', '1503');
INSERT INTO sys_role_menu
VALUES ('3', '1504');
INSERT INTO sys_role_menu
VALUES ('3', '1505');
INSERT INTO sys_role_menu
VALUES ('3', '1506');
INSERT INTO sys_role_menu
VALUES ('3', '1507');
INSERT INTO sys_role_menu
VALUES ('3', '1508');
INSERT INTO sys_role_menu
VALUES ('3', '1509');
INSERT INTO sys_role_menu
VALUES ('3', '1510');
INSERT INTO sys_role_menu
VALUES ('3', '1511');
INSERT INTO sys_role_menu
VALUES ('3', '1600');
INSERT INTO sys_role_menu
VALUES ('3', '1601');
INSERT INTO sys_role_menu
VALUES ('3', '1602');
INSERT INTO sys_role_menu
VALUES ('3', '1603');
INSERT INTO sys_role_menu
VALUES ('3', '1620');
INSERT INTO sys_role_menu
VALUES ('3', '1621');
INSERT INTO sys_role_menu
VALUES ('3', '1622');
INSERT INTO sys_role_menu
VALUES ('3', '1623');
INSERT INTO sys_role_menu
VALUES ('3', '11616');
INSERT INTO sys_role_menu
VALUES ('3', '11618');
INSERT INTO sys_role_menu
VALUES ('3', '11619');
INSERT INTO sys_role_menu
VALUES ('3', '11622');
INSERT INTO sys_role_menu
VALUES ('3', '11623');
INSERT INTO sys_role_menu
VALUES ('3', '11629');
INSERT INTO sys_role_menu
VALUES ('3', '11632');
INSERT INTO sys_role_menu
VALUES ('3', '11633');
INSERT INTO sys_role_menu
VALUES ('3', '11638');
INSERT INTO sys_role_menu
VALUES ('3', '11639');
INSERT INTO sys_role_menu
VALUES ('3', '11640');
INSERT INTO sys_role_menu
VALUES ('3', '11641');
INSERT INTO sys_role_menu
VALUES ('3', '11642');
INSERT INTO sys_role_menu
VALUES ('3', '11643');
INSERT INTO sys_role_menu
VALUES ('3', '11701');
INSERT INTO sys_role_menu
VALUES ('4', '5');
INSERT INTO sys_role_menu
VALUES ('4', '1500');
INSERT INTO sys_role_menu
VALUES ('4', '1501');
INSERT INTO sys_role_menu
VALUES ('4', '1502');
INSERT INTO sys_role_menu
VALUES ('4', '1503');
INSERT INTO sys_role_menu
VALUES ('4', '1504');
INSERT INTO sys_role_menu
VALUES ('4', '1505');
INSERT INTO sys_role_menu
VALUES ('4', '1506');
INSERT INTO sys_role_menu
VALUES ('4', '1507');
INSERT INTO sys_role_menu
VALUES ('4', '1508');
INSERT INTO sys_role_menu
VALUES ('4', '1509');
INSERT INTO sys_role_menu
VALUES ('4', '1510');
INSERT INTO sys_role_menu
VALUES ('4', '1511');

-- ----------------------------
-- 8、角色和部门关联表  角色1-N部门
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_role_dept
(
    role_id int8 NOT NULL,
    dept_id int8 NOT NULL,
    CONSTRAINT sys_role_dept_pk PRIMARY KEY (role_id, dept_id)
);

COMMENT ON TABLE sys_role_dept              IS '角色和部门关联表';
COMMENT ON COLUMN sys_role_dept.role_id     IS '角色ID';
COMMENT ON COLUMN sys_role_dept.dept_id     IS '部门ID';


-- ----------------------------
-- 9、用户与岗位关联表  用户1-N岗位
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_user_post
(
    user_id int8 NOT NULL,
    post_id int8 NOT NULL,
    CONSTRAINT sys_user_post_pk PRIMARY KEY (user_id, post_id)
);

COMMENT ON TABLE sys_user_post              IS '用户与岗位关联表';
COMMENT ON COLUMN sys_user_post.user_id     IS '用户ID';
COMMENT ON COLUMN sys_user_post.post_id     IS '岗位ID';

-- ----------------------------
-- 初始化-用户与岗位关联表数据
-- ----------------------------
INSERT INTO sys_user_post
VALUES ('1', '1');

-- ----------------------------
-- 10、操作日志记录
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_oper_log
(
    oper_id        int8,
    tenant_id      varchar(20)   DEFAULT '000000'::VARCHAR,
    title          varchar(50)   DEFAULT ''::VARCHAR,
    business_type  int4          DEFAULT 0,
    method         varchar(100)  DEFAULT ''::VARCHAR,
    request_method varchar(10)   DEFAULT ''::VARCHAR,
    operator_type  int4          DEFAULT 0,
    oper_name      varchar(50)   DEFAULT ''::VARCHAR,
    dept_name      varchar(50)   DEFAULT ''::VARCHAR,
    oper_url       varchar(255)  DEFAULT ''::VARCHAR,
    oper_ip        varchar(128)  DEFAULT ''::VARCHAR,
    oper_location  varchar(255)  DEFAULT ''::VARCHAR,
    oper_param     varchar(4000) DEFAULT ''::VARCHAR,
    json_result    varchar(4000) DEFAULT ''::VARCHAR,
    status         int4          DEFAULT 0,
    error_msg      varchar(4000) DEFAULT ''::VARCHAR,
    oper_time      timestamp,
    cost_time      int8          DEFAULT 0,
    CONSTRAINT sys_oper_log_pk PRIMARY KEY (oper_id)
);

CREATE INDEX idx_sys_oper_log_bt ON sys_oper_log (business_type);
CREATE INDEX idx_sys_oper_log_s ON sys_oper_log (status);
CREATE INDEX idx_sys_oper_log_ot ON sys_oper_log (oper_time);

COMMENT ON TABLE sys_oper_log                   IS '操作日志记录';
COMMENT ON COLUMN sys_oper_log.oper_id          IS '日志主键';
COMMENT ON COLUMN sys_oper_log.tenant_id        IS '租户编号';
COMMENT ON COLUMN sys_oper_log.title            IS '模块标题';
COMMENT ON COLUMN sys_oper_log.business_type    IS '业务类型（0其它 1新增 2修改 3删除）';
COMMENT ON COLUMN sys_oper_log.method           IS '方法名称';
COMMENT ON COLUMN sys_oper_log.request_method   IS '请求方式';
COMMENT ON COLUMN sys_oper_log.operator_type    IS '操作类别（0其它 1后台用户 2手机端用户）';
COMMENT ON COLUMN sys_oper_log.oper_name        IS '操作人员';
COMMENT ON COLUMN sys_oper_log.dept_name        IS '部门名称';
COMMENT ON COLUMN sys_oper_log.oper_url         IS '请求URL';
COMMENT ON COLUMN sys_oper_log.oper_ip          IS '主机地址';
COMMENT ON COLUMN sys_oper_log.oper_location    IS '操作地点';
COMMENT ON COLUMN sys_oper_log.oper_param       IS '请求参数';
COMMENT ON COLUMN sys_oper_log.json_result      IS '返回参数';
COMMENT ON COLUMN sys_oper_log.status           IS '操作状态（0正常 1异常）';
COMMENT ON COLUMN sys_oper_log.error_msg        IS '错误消息';
COMMENT ON COLUMN sys_oper_log.oper_time        IS '操作时间';
COMMENT ON COLUMN sys_oper_log.cost_time        IS '消耗时间';

-- ----------------------------
-- 11、字典类型表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_dict_type
(
    dict_id     int8,
    tenant_id   varchar(20)  DEFAULT '000000'::VARCHAR,
    dict_name   varchar(100) DEFAULT ''::VARCHAR,
    dict_type   varchar(100) DEFAULT ''::VARCHAR,
    create_dept int8,
    create_by   int8,
    create_time timestamp,
    update_by   int8,
    update_time timestamp,
    remark      varchar(500) DEFAULT NULL::VARCHAR,
    CONSTRAINT sys_dict_type_pk PRIMARY KEY (dict_id)
);

CREATE UNIQUE INDEX sys_dict_type_index1 ON sys_dict_type (tenant_id, dict_type);

COMMENT ON TABLE sys_dict_type                  IS '字典类型表';
COMMENT ON COLUMN sys_dict_type.dict_id         IS '字典主键';
COMMENT ON COLUMN sys_dict_type.tenant_id       IS '租户编号';
COMMENT ON COLUMN sys_dict_type.dict_name       IS '字典名称';
COMMENT ON COLUMN sys_dict_type.dict_type       IS '字典类型';
COMMENT ON COLUMN sys_dict_type.create_dept     IS '创建部门';
COMMENT ON COLUMN sys_dict_type.create_by       IS '创建者';
COMMENT ON COLUMN sys_dict_type.create_time     IS '创建时间';
COMMENT ON COLUMN sys_dict_type.update_by       IS '更新者';
COMMENT ON COLUMN sys_dict_type.update_time     IS '更新时间';
COMMENT ON COLUMN sys_dict_type.remark          IS '备注';

INSERT INTO sys_dict_type
VALUES (1, '000000', '用户性别', 'sys_user_sex', 103, 1, NOW(), NULL, NULL, '用户性别列表');
INSERT INTO sys_dict_type
VALUES (2, '000000', '菜单状态', 'sys_show_hide', 103, 1, NOW(), NULL, NULL, '菜单状态列表');
INSERT INTO sys_dict_type
VALUES (3, '000000', '系统开关', 'sys_normal_disable', 103, 1, NOW(), NULL, NULL, '系统开关列表');
INSERT INTO sys_dict_type
VALUES (6, '000000', '系统是否', 'sys_yes_no', 103, 1, NOW(), NULL, NULL, '系统是否列表');
INSERT INTO sys_dict_type
VALUES (7, '000000', '通知类型', 'sys_notice_type', 103, 1, NOW(), NULL, NULL, '通知类型列表');
INSERT INTO sys_dict_type
VALUES (8, '000000', '通知状态', 'sys_notice_status', 103, 1, NOW(), NULL, NULL, '通知状态列表');
INSERT INTO sys_dict_type
VALUES (9, '000000', '操作类型', 'sys_oper_type', 103, 1, NOW(), NULL, NULL, '操作类型列表');
INSERT INTO sys_dict_type
VALUES (10, '000000', '系统状态', 'sys_common_status', 103, 1, NOW(), NULL, NULL, '登录状态列表');
INSERT INTO sys_dict_type
VALUES (11, '000000', '授权类型', 'sys_grant_type', 103, 1, NOW(), NULL, NULL, '认证授权类型');
INSERT INTO sys_dict_type
VALUES (12, '000000', '设备类型', 'sys_device_type', 103, 1, NOW(), NULL, NULL, '客户端设备类型');

-- ----------------------------
-- 12、字典数据表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_dict_data
(
    dict_code   int8,
    tenant_id   varchar(20)  DEFAULT '000000'::VARCHAR,
    dict_sort   int4         DEFAULT 0,
    dict_label  varchar(100) DEFAULT ''::VARCHAR,
    dict_value  varchar(100) DEFAULT ''::VARCHAR,
    dict_type   varchar(100) DEFAULT ''::VARCHAR,
    css_class   varchar(100) DEFAULT NULL::VARCHAR,
    list_class  varchar(100) DEFAULT NULL::VARCHAR,
    is_default  char         DEFAULT 'N'::bpchar,
    create_dept int8,
    create_by   int8,
    create_time timestamp,
    update_by   int8,
    update_time timestamp,
    remark      varchar(500) DEFAULT NULL::VARCHAR,
    CONSTRAINT sys_dict_data_pk PRIMARY KEY (dict_code)
);

COMMENT ON TABLE sys_dict_data                  IS '字典数据表';
COMMENT ON COLUMN sys_dict_data.dict_code       IS '字典编码';
COMMENT ON COLUMN sys_dict_type.tenant_id       IS '租户编号';
COMMENT ON COLUMN sys_dict_data.dict_sort       IS '字典排序';
COMMENT ON COLUMN sys_dict_data.dict_label      IS '字典标签';
COMMENT ON COLUMN sys_dict_data.dict_value      IS '字典键值';
COMMENT ON COLUMN sys_dict_data.dict_type       IS '字典类型';
COMMENT ON COLUMN sys_dict_data.css_class       IS '样式属性（其他样式扩展）';
COMMENT ON COLUMN sys_dict_data.list_class      IS '表格回显样式';
COMMENT ON COLUMN sys_dict_data.is_default      IS '是否默认（Y是 N否）';
COMMENT ON COLUMN sys_dict_data.create_dept     IS '创建部门';
COMMENT ON COLUMN sys_dict_data.create_by       IS '创建者';
COMMENT ON COLUMN sys_dict_data.create_time     IS '创建时间';
COMMENT ON COLUMN sys_dict_data.update_by       IS '更新者';
COMMENT ON COLUMN sys_dict_data.update_time     IS '更新时间';
COMMENT ON COLUMN sys_dict_data.remark          IS '备注';

INSERT INTO sys_dict_data
VALUES (1, '000000', 1, '男', '0', 'sys_user_sex', '', '', 'Y', 103, 1, NOW(), NULL, NULL, '性别男');
INSERT INTO sys_dict_data
VALUES (2, '000000', 2, '女', '1', 'sys_user_sex', '', '', 'N', 103, 1, NOW(), NULL, NULL, '性别女');
INSERT INTO sys_dict_data
VALUES (3, '000000', 3, '未知', '2', 'sys_user_sex', '', '', 'N', 103, 1, NOW(), NULL, NULL, '性别未知');
INSERT INTO sys_dict_data
VALUES (4, '000000', 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', 103, 1, NOW(), NULL, NULL, '显示菜单');
INSERT INTO sys_dict_data
VALUES (5, '000000', 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', 103, 1, NOW(), NULL, NULL, '隐藏菜单');
INSERT INTO sys_dict_data
VALUES (6, '000000', 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', 103, 1, NOW(), NULL, NULL, '正常状态');
INSERT INTO sys_dict_data
VALUES (7, '000000', 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', 103, 1, NOW(), NULL, NULL, '停用状态');
INSERT INTO sys_dict_data
VALUES (12, '000000', 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', 103, 1, NOW(), NULL, NULL, '系统默认是');
INSERT INTO sys_dict_data
VALUES (13, '000000', 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', 103, 1, NOW(), NULL, NULL, '系统默认否');
INSERT INTO sys_dict_data
VALUES (14, '000000', 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', 103, 1, NOW(), NULL, NULL, '通知');
INSERT INTO sys_dict_data
VALUES (15, '000000', 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', 103, 1, NOW(), NULL, NULL, '公告');
INSERT INTO sys_dict_data
VALUES (16, '000000', 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', 103, 1, NOW(), NULL, NULL, '正常状态');
INSERT INTO sys_dict_data
VALUES (17, '000000', 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', 103, 1, NOW(), NULL, NULL, '关闭状态');
INSERT INTO sys_dict_data
VALUES (29, '000000', 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', 103, 1, NOW(), NULL, NULL, '其他操作');
INSERT INTO sys_dict_data
VALUES (18, '000000', 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', 103, 1, NOW(), NULL, NULL, '新增操作');
INSERT INTO sys_dict_data
VALUES (19, '000000', 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', 103, 1, NOW(), NULL, NULL, '修改操作');
INSERT INTO sys_dict_data
VALUES (20, '000000', 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', 103, 1, NOW(), NULL, NULL, '删除操作');
INSERT INTO sys_dict_data
VALUES (21, '000000', 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', 103, 1, NOW(), NULL, NULL, '授权操作');
INSERT INTO sys_dict_data
VALUES (22, '000000', 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', 103, 1, NOW(), NULL, NULL, '导出操作');
INSERT INTO sys_dict_data
VALUES (23, '000000', 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', 103, 1, NOW(), NULL, NULL, '导入操作');
INSERT INTO sys_dict_data
VALUES (24, '000000', 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', 103, 1, NOW(), NULL, NULL, '强退操作');
INSERT INTO sys_dict_data
VALUES (25, '000000', 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', 103, 1, NOW(), NULL, NULL, '生成操作');
INSERT INTO sys_dict_data
VALUES (26, '000000', 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', 103, 1, NOW(), NULL, NULL, '清空操作');
INSERT INTO sys_dict_data
VALUES (27, '000000', 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', 103, 1, NOW(), NULL, NULL, '正常状态');
INSERT INTO sys_dict_data
VALUES (28, '000000', 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', 103, 1, NOW(), NULL, NULL, '停用状态');
INSERT INTO sys_dict_data
VALUES (30, '000000', 0, '密码认证', 'password', 'sys_grant_type', '', 'default', 'N', 103, 1, NOW(), NULL, NULL, '密码认证');
INSERT INTO sys_dict_data
VALUES (31, '000000', 0, '短信认证', 'sms', 'sys_grant_type', '', 'default', 'N', 103, 1, NOW(), NULL, NULL, '短信认证');
INSERT INTO sys_dict_data
VALUES (32, '000000', 0, '邮件认证', 'email', 'sys_grant_type', '', 'default', 'N', 103, 1, NOW(), NULL, NULL, '邮件认证');
INSERT INTO sys_dict_data
VALUES (33, '000000', 0, '小程序认证', 'xcx', 'sys_grant_type', '', 'default', 'N', 103, 1, NOW(), NULL, NULL, '小程序认证');
INSERT INTO sys_dict_data
VALUES (34, '000000', 0, '三方登录认证', 'social', 'sys_grant_type', '', 'default', 'N', 103, 1, NOW(), NULL, NULL, '三方登录认证');
INSERT INTO sys_dict_data
VALUES (35, '000000', 0, 'PC', 'pc', 'sys_device_type', '', 'default', 'N', 103, 1, NOW(), NULL, NULL, 'PC');
INSERT INTO sys_dict_data
VALUES (36, '000000', 0, '安卓', 'android', 'sys_device_type', '', 'default', 'N', 103, 1, NOW(), NULL, NULL, '安卓');
INSERT INTO sys_dict_data
VALUES (37, '000000', 0, 'iOS', 'ios', 'sys_device_type', '', 'default', 'N', 103, 1, NOW(), NULL, NULL, 'iOS');
INSERT INTO sys_dict_data
VALUES (38, '000000', 0, '小程序', 'xcx', 'sys_device_type', '', 'default', 'N', 103, 1, NOW(), NULL, NULL, '小程序');


-- ----------------------------
-- 13、参数配置表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_config
(
    config_id    int8,
    tenant_id    varchar(20)  DEFAULT '000000'::VARCHAR,
    config_name  varchar(100) DEFAULT ''::VARCHAR,
    config_key   varchar(100) DEFAULT ''::VARCHAR,
    config_value varchar(500) DEFAULT ''::VARCHAR,
    config_type  char         DEFAULT 'N'::bpchar,
    create_dept  int8,
    create_by    int8,
    create_time  timestamp,
    update_by    int8,
    update_time  timestamp,
    remark       varchar(500) DEFAULT NULL::VARCHAR,
    CONSTRAINT sys_config_pk PRIMARY KEY (config_id)
);

COMMENT ON TABLE sys_config                 IS '参数配置表';
COMMENT ON COLUMN sys_config.config_id      IS '参数主键';
COMMENT ON COLUMN sys_config.tenant_id      IS '租户编号';
COMMENT ON COLUMN sys_config.config_name    IS '参数名称';
COMMENT ON COLUMN sys_config.config_key     IS '参数键名';
COMMENT ON COLUMN sys_config.config_value   IS '参数键值';
COMMENT ON COLUMN sys_config.config_type    IS '系统内置（Y是 N否）';
COMMENT ON COLUMN sys_config.create_dept    IS '创建部门';
COMMENT ON COLUMN sys_config.create_by      IS '创建者';
COMMENT ON COLUMN sys_config.create_time    IS '创建时间';
COMMENT ON COLUMN sys_config.update_by      IS '更新者';
COMMENT ON COLUMN sys_config.update_time    IS '更新时间';
COMMENT ON COLUMN sys_config.remark         IS '备注';

INSERT INTO sys_config
VALUES (1, '000000', '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 103, 1, NOW(), NULL, NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO sys_config
VALUES (2, '000000', '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 103, 1, NOW(), NULL, NULL, '初始化密码 123456');
INSERT INTO sys_config
VALUES (3, '000000', '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 103, 1, NOW(), NULL, NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO sys_config
VALUES (5, '000000', '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 103, 1, NOW(), NULL, NULL, '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO sys_config
VALUES (11, '000000', 'OSS预览列表资源开关', 'sys.oss.previewListResource', 'true', 'Y', 103, 1, NOW(), NULL, NULL, 'true:开启, false:关闭');


-- ----------------------------
-- 14、系统访问记录
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_logininfor
(
    info_id        int8,
    tenant_id      varchar(20)  DEFAULT '000000'::VARCHAR,
    user_name      varchar(50)  DEFAULT ''::VARCHAR,
    client_key     varchar(32)  DEFAULT ''::VARCHAR,
    device_type    varchar(32)  DEFAULT ''::VARCHAR,
    ipaddr         varchar(128) DEFAULT ''::VARCHAR,
    login_location varchar(255) DEFAULT ''::VARCHAR,
    browser        varchar(50)  DEFAULT ''::VARCHAR,
    os             varchar(50)  DEFAULT ''::VARCHAR,
    status         char         DEFAULT '0'::bpchar,
    msg            varchar(255) DEFAULT ''::VARCHAR,
    login_time     timestamp,
    CONSTRAINT sys_logininfor_pk PRIMARY KEY (info_id)
);

CREATE INDEX idx_sys_logininfor_s ON sys_logininfor (status);
CREATE INDEX idx_sys_logininfor_lt ON sys_logininfor (login_time);

COMMENT ON TABLE sys_logininfor                 IS '系统访问记录';
COMMENT ON COLUMN sys_logininfor.info_id        IS '访问ID';
COMMENT ON COLUMN sys_logininfor.tenant_id      IS '租户编号';
COMMENT ON COLUMN sys_logininfor.user_name      IS '用户账号';
COMMENT ON COLUMN sys_logininfor.client_key     IS '客户端';
COMMENT ON COLUMN sys_logininfor.device_type    IS '设备类型';
COMMENT ON COLUMN sys_logininfor.ipaddr         IS '登录IP地址';
COMMENT ON COLUMN sys_logininfor.login_location IS '登录地点';
COMMENT ON COLUMN sys_logininfor.browser        IS '浏览器类型';
COMMENT ON COLUMN sys_logininfor.os             IS '操作系统';
COMMENT ON COLUMN sys_logininfor.status         IS '登录状态（0成功 1失败）';
COMMENT ON COLUMN sys_logininfor.msg            IS '提示消息';
COMMENT ON COLUMN sys_logininfor.login_time     IS '访问时间';

-- ----------------------------
-- 17、通知公告表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_notice
(
    notice_id      int8,
    tenant_id      varchar(20)  DEFAULT '000000'::VARCHAR,
    notice_title   varchar(50) NOT NULL,
    notice_type    char        NOT NULL,
    notice_content text,
    status         char         DEFAULT '0'::bpchar,
    create_dept    int8,
    create_by      int8,
    create_time    timestamp,
    update_by      int8,
    update_time    timestamp,
    remark         varchar(255) DEFAULT NULL::VARCHAR,
    CONSTRAINT sys_notice_pk PRIMARY KEY (notice_id)
);

COMMENT ON TABLE sys_notice                 IS '通知公告表';
COMMENT ON COLUMN sys_notice.notice_id      IS '公告ID';
COMMENT ON COLUMN sys_notice.tenant_id      IS '租户编号';
COMMENT ON COLUMN sys_notice.notice_title   IS '公告标题';
COMMENT ON COLUMN sys_notice.notice_type    IS '公告类型（1通知 2公告）';
COMMENT ON COLUMN sys_notice.notice_content IS '公告内容';
COMMENT ON COLUMN sys_notice.status         IS '公告状态（0正常 1关闭）';
COMMENT ON COLUMN sys_notice.create_dept    IS '创建部门';
COMMENT ON COLUMN sys_notice.create_by      IS '创建者';
COMMENT ON COLUMN sys_notice.create_time    IS '创建时间';
COMMENT ON COLUMN sys_notice.update_by      IS '更新者';
COMMENT ON COLUMN sys_notice.update_time    IS '更新时间';
COMMENT ON COLUMN sys_notice.remark         IS '备注';

-- ----------------------------
-- 初始化-公告信息表数据
-- ----------------------------
INSERT INTO sys_notice
VALUES ('1', '000000', '温馨提醒：2018-07-01 新版本发布啦', '2', '新版本内容', '0', 103, 1, NOW(), NULL, NULL, '管理员');
INSERT INTO sys_notice
VALUES ('2', '000000', '维护通知：2018-07-01 系统凌晨维护', '1', '维护内容', '0', 103, 1, NOW(), NULL, NULL, '管理员');


-- ----------------------------
-- 18、代码生成业务表
-- ----------------------------
CREATE TABLE IF NOT EXISTS gen_table
(
    table_id          int8,
    data_name         varchar(200)  DEFAULT ''::VARCHAR,
    table_name        varchar(200)  DEFAULT ''::VARCHAR,
    table_comment     varchar(500)  DEFAULT ''::VARCHAR,
    sub_table_name    varchar(64)   DEFAULT ''::VARCHAR,
    sub_table_fk_name varchar(64)   DEFAULT ''::VARCHAR,
    class_name        varchar(100)  DEFAULT ''::VARCHAR,
    tpl_category      varchar(200)  DEFAULT 'crud'::VARCHAR,
    package_name      varchar(100)  DEFAULT NULL::VARCHAR,
    module_name       varchar(30)   DEFAULT NULL::VARCHAR,
    business_name     varchar(30)   DEFAULT NULL::VARCHAR,
    function_name     varchar(50)   DEFAULT NULL::VARCHAR,
    function_author   varchar(50)   DEFAULT NULL::VARCHAR,
    gen_type          char          DEFAULT '0'::bpchar NOT NULL,
    gen_path          varchar(200)  DEFAULT '/'::VARCHAR,
    options           varchar(1000) DEFAULT NULL::VARCHAR,
    create_dept       int8,
    create_by         int8,
    create_time       timestamp,
    update_by         int8,
    update_time       timestamp,
    remark            varchar(500)  DEFAULT NULL::VARCHAR,
    CONSTRAINT gen_table_pk PRIMARY KEY (table_id)
);

COMMENT ON TABLE gen_table IS '代码生成业务表';
COMMENT ON COLUMN gen_table.table_id IS '编号';
COMMENT ON COLUMN gen_table.data_name IS '数据源名称';
COMMENT ON COLUMN gen_table.table_name IS '表名称';
COMMENT ON COLUMN gen_table.table_comment IS '表描述';
COMMENT ON COLUMN gen_table.sub_table_name IS '关联子表的表名';
COMMENT ON COLUMN gen_table.sub_table_fk_name IS '子表关联的外键名';
COMMENT ON COLUMN gen_table.class_name IS '实体类名称';
COMMENT ON COLUMN gen_table.tpl_category IS '使用的模板（CRUD单表操作 TREE树表操作）';
COMMENT ON COLUMN gen_table.package_name IS '生成包路径';
COMMENT ON COLUMN gen_table.module_name IS '生成模块名';
COMMENT ON COLUMN gen_table.business_name IS '生成业务名';
COMMENT ON COLUMN gen_table.function_name IS '生成功能名';
COMMENT ON COLUMN gen_table.function_author IS '生成功能作者';
COMMENT ON COLUMN gen_table.gen_type IS '生成代码方式（0zip压缩包 1自定义路径）';
COMMENT ON COLUMN gen_table.gen_path IS '生成路径（不填默认项目路径）';
COMMENT ON COLUMN gen_table.options IS '其它生成选项';
COMMENT ON COLUMN gen_table.create_dept IS '创建部门';
COMMENT ON COLUMN gen_table.create_by IS '创建者';
COMMENT ON COLUMN gen_table.create_time IS '创建时间';
COMMENT ON COLUMN gen_table.update_by IS '更新者';
COMMENT ON COLUMN gen_table.update_time IS '更新时间';
COMMENT ON COLUMN gen_table.remark IS '备注';

-- ----------------------------
-- 19、代码生成业务表字段
-- ----------------------------
CREATE TABLE IF NOT EXISTS gen_table_column
(
    column_id      int8,
    table_id       int8,
    column_name    varchar(200) DEFAULT NULL::VARCHAR,
    column_comment varchar(500) DEFAULT NULL::VARCHAR,
    column_type    varchar(100) DEFAULT NULL::VARCHAR,
    java_type      varchar(500) DEFAULT NULL::VARCHAR,
    java_field     varchar(200) DEFAULT NULL::VARCHAR,
    is_pk          char         DEFAULT NULL::bpchar,
    is_increment   char         DEFAULT NULL::bpchar,
    is_required    char         DEFAULT NULL::bpchar,
    is_insert      char         DEFAULT NULL::bpchar,
    is_edit        char         DEFAULT NULL::bpchar,
    is_list        char         DEFAULT NULL::bpchar,
    is_query       char         DEFAULT NULL::bpchar,
    query_type     varchar(200) DEFAULT 'EQ'::VARCHAR,
    html_type      varchar(200) DEFAULT NULL::VARCHAR,
    dict_type      varchar(200) DEFAULT ''::VARCHAR,
    sort           int4,
    create_dept    int8,
    create_by      int8,
    create_time    timestamp,
    update_by      int8,
    update_time    timestamp,
    CONSTRAINT gen_table_column_pk PRIMARY KEY (column_id)
);

COMMENT ON TABLE gen_table_column IS '代码生成业务表字段';
COMMENT ON COLUMN gen_table_column.column_id IS '编号';
COMMENT ON COLUMN gen_table_column.table_id IS '归属表编号';
COMMENT ON COLUMN gen_table_column.column_name IS '列名称';
COMMENT ON COLUMN gen_table_column.column_comment IS '列描述';
COMMENT ON COLUMN gen_table_column.column_type IS '列类型';
COMMENT ON COLUMN gen_table_column.java_type IS 'JAVA类型';
COMMENT ON COLUMN gen_table_column.java_field IS 'JAVA字段名';
COMMENT ON COLUMN gen_table_column.is_pk IS '是否主键（1是）';
COMMENT ON COLUMN gen_table_column.is_increment IS '是否自增（1是）';
COMMENT ON COLUMN gen_table_column.is_required IS '是否必填（1是）';
COMMENT ON COLUMN gen_table_column.is_insert IS '是否为插入字段（1是）';
COMMENT ON COLUMN gen_table_column.is_edit IS '是否编辑字段（1是）';
COMMENT ON COLUMN gen_table_column.is_list IS '是否列表字段（1是）';
COMMENT ON COLUMN gen_table_column.is_query IS '是否查询字段（1是）';
COMMENT ON COLUMN gen_table_column.query_type IS '查询方式（等于、不等于、大于、小于、范围）';
COMMENT ON COLUMN gen_table_column.html_type IS '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）';
COMMENT ON COLUMN gen_table_column.dict_type IS '字典类型';
COMMENT ON COLUMN gen_table_column.sort IS '排序';
COMMENT ON COLUMN gen_table_column.create_dept IS '创建部门';
COMMENT ON COLUMN gen_table_column.create_by IS '创建者';
COMMENT ON COLUMN gen_table_column.create_time IS '创建时间';
COMMENT ON COLUMN gen_table_column.update_by IS '更新者';
COMMENT ON COLUMN gen_table_column.update_time IS '更新时间';

-- ----------------------------
-- OSS对象存储表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_oss
(
    oss_id        int8,
    tenant_id     varchar(20)  DEFAULT '000000'::VARCHAR,
    file_name     varchar(255) DEFAULT ''::VARCHAR NOT NULL,
    original_name varchar(255) DEFAULT ''::VARCHAR NOT NULL,
    file_suffix   varchar(10)  DEFAULT ''::VARCHAR NOT NULL,
    url           varchar(500) DEFAULT ''::VARCHAR NOT NULL,
    ext1          varchar(500) DEFAULT ''::VARCHAR,
    create_dept   int8,
    create_by     int8,
    create_time   timestamp,
    update_by     int8,
    update_time   timestamp,
    service       varchar(20)  DEFAULT 'minio'::VARCHAR,
    CONSTRAINT sys_oss_pk PRIMARY KEY (oss_id)
);

COMMENT ON TABLE sys_oss                    IS 'OSS对象存储表';
COMMENT ON COLUMN sys_oss.oss_id            IS '对象存储主键';
COMMENT ON COLUMN sys_oss.tenant_id         IS '租户编码';
COMMENT ON COLUMN sys_oss.file_name         IS '文件名';
COMMENT ON COLUMN sys_oss.original_name     IS '原名';
COMMENT ON COLUMN sys_oss.file_suffix       IS '文件后缀名';
COMMENT ON COLUMN sys_oss.url               IS 'URL地址';
COMMENT ON COLUMN sys_oss.ext1              IS '扩展字段';
COMMENT ON COLUMN sys_oss.create_by         IS '上传人';
COMMENT ON COLUMN sys_oss.create_dept       IS '创建部门';
COMMENT ON COLUMN sys_oss.create_time       IS '创建时间';
COMMENT ON COLUMN sys_oss.update_by         IS '更新者';
COMMENT ON COLUMN sys_oss.update_time       IS '更新时间';
COMMENT ON COLUMN sys_oss.service           IS '服务商';

-- ----------------------------
-- OSS对象存储动态配置表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_oss_config
(
    oss_config_id int8,
    tenant_id     varchar(20)  DEFAULT '000000'::VARCHAR,
    config_key    varchar(20)  DEFAULT ''::VARCHAR NOT NULL,
    access_key    varchar(255) DEFAULT ''::VARCHAR,
    secret_key    varchar(255) DEFAULT ''::VARCHAR,
    bucket_name   varchar(255) DEFAULT ''::VARCHAR,
    prefix        varchar(255) DEFAULT ''::VARCHAR,
    endpoint      varchar(255) DEFAULT ''::VARCHAR,
    domain        varchar(255) DEFAULT ''::VARCHAR,
    is_https      char         DEFAULT 'N'::bpchar,
    region        varchar(255) DEFAULT ''::VARCHAR,
    access_policy char(1)      DEFAULT '1'::bpchar NOT NULL,
    status        char         DEFAULT '1'::bpchar,
    ext1          varchar(255) DEFAULT ''::VARCHAR,
    create_dept   int8,
    create_by     int8,
    create_time   timestamp,
    update_by     int8,
    update_time   timestamp,
    remark        varchar(500) DEFAULT ''::VARCHAR,
    CONSTRAINT sys_oss_config_pk PRIMARY KEY (oss_config_id)
);

COMMENT ON TABLE sys_oss_config                 IS '对象存储配置表';
COMMENT ON COLUMN sys_oss_config.oss_config_id  IS '主键';
COMMENT ON COLUMN sys_oss_config.tenant_id      IS '租户编码';
COMMENT ON COLUMN sys_oss_config.config_key     IS '配置key';
COMMENT ON COLUMN sys_oss_config.access_key     IS 'accessKey';
COMMENT ON COLUMN sys_oss_config.secret_key     IS '秘钥';
COMMENT ON COLUMN sys_oss_config.bucket_name    IS '桶名称';
COMMENT ON COLUMN sys_oss_config.prefix         IS '前缀';
COMMENT ON COLUMN sys_oss_config.endpoint       IS '访问站点';
COMMENT ON COLUMN sys_oss_config.domain         IS '自定义域名';
COMMENT ON COLUMN sys_oss_config.is_https       IS '是否https（Y=是,N=否）';
COMMENT ON COLUMN sys_oss_config.region         IS '域';
COMMENT ON COLUMN sys_oss_config.access_policy  IS '桶权限类型(0=private 1=public 2=custom)';
COMMENT ON COLUMN sys_oss_config.status         IS '是否默认（0=是,1=否）';
COMMENT ON COLUMN sys_oss_config.ext1           IS '扩展字段';
COMMENT ON COLUMN sys_oss_config.create_dept    IS '创建部门';
COMMENT ON COLUMN sys_oss_config.create_by      IS '创建者';
COMMENT ON COLUMN sys_oss_config.create_time    IS '创建时间';
COMMENT ON COLUMN sys_oss_config.update_by      IS '更新者';
COMMENT ON COLUMN sys_oss_config.update_time    IS '更新时间';
COMMENT ON COLUMN sys_oss_config.remark         IS '备注';

INSERT INTO sys_oss_config
VALUES (1, '000000', 'minio', 'ruoyi', 'ruoyi123', 'ruoyi', '', '127.0.0.1:9000', '', 'N', '', '1', '0', '', 103, 1, NOW(), 1, NOW(), NULL);
INSERT INTO sys_oss_config
VALUES (2, '000000', 'qiniu', 'XXXXXXXXXXXXXXX', 'XXXXXXXXXXXXXXX', 'ruoyi', '', 's3-cn-north-1.qiniucs.com', '', 'N', '', '1', '1', '', 103, 1, NOW(), 1, NOW(), NULL);
INSERT INTO sys_oss_config
VALUES (3, '000000', 'aliyun', 'XXXXXXXXXXXXXXX', 'XXXXXXXXXXXXXXX', 'ruoyi', '', 'oss-cn-beijing.aliyuncs.com', '', 'N', '', '1', '1', '', 103, 1, NOW(), 1, NOW(), NULL);
INSERT INTO sys_oss_config
VALUES (4, '000000', 'qcloud', 'XXXXXXXXXXXXXXX', 'XXXXXXXXXXXXXXX', 'ruoyi-1240000000', '', 'cos.ap-beijing.myqcloud.com', '', 'N', 'ap-beijing', '1', '1', '', 103, 1, NOW(), 1, NOW(), NULL);
INSERT INTO sys_oss_config
VALUES (5, '000000', 'image', 'ruoyi', 'ruoyi123', 'ruoyi', 'image', '127.0.0.1:9000', '', 'N', '', '1', '1', '', 103, 1, NOW(), 1, NOW(), NULL);

-- ----------------------------
-- 系统授权表
-- ----------------------------
CREATE TABLE sys_client
(
    id             int8,
    client_id      varchar(64)  DEFAULT ''::VARCHAR,
    client_key     varchar(32)  DEFAULT ''::VARCHAR,
    client_secret  varchar(255) DEFAULT ''::VARCHAR,
    grant_type     varchar(255) DEFAULT ''::VARCHAR,
    device_type    varchar(32)  DEFAULT ''::VARCHAR,
    active_timeout int4         DEFAULT 1800,
    timeout        int4         DEFAULT 604800,
    status         char(1)      DEFAULT '0'::bpchar,
    del_flag       char(1)      DEFAULT '0'::bpchar,
    create_dept    int8,
    create_by      int8,
    create_time    timestamp,
    update_by      int8,
    update_time    timestamp,
    CONSTRAINT sys_client_pk PRIMARY KEY (id)
);

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
COMMENT ON COLUMN sys_client.del_flag               IS '删除标志（0代表存在 1代表删除）';
COMMENT ON COLUMN sys_client.create_dept            IS '创建部门';
COMMENT ON COLUMN sys_client.create_by              IS '创建者';
COMMENT ON COLUMN sys_client.create_time            IS '创建时间';
COMMENT ON COLUMN sys_client.update_by              IS '更新者';
COMMENT ON COLUMN sys_client.update_time            IS '更新时间';

INSERT INTO sys_client
VALUES (1, 'e5cd7e4891bf95d1d19206ce24a7b32e', 'pc', 'pc123', 'password,social', 'pc', 1800, 604800, 0, 0, 103, 1, NOW(), 1, NOW());
INSERT INTO sys_client
VALUES (2, '428a8310cd442757ae699df5d894f051', 'app', 'app123', 'password,sms,social', 'android', 1800, 604800, 0, 0, 103, 1, NOW(), 1, NOW());

CREATE TABLE IF NOT EXISTS test_demo
(
    id          int8,
    tenant_id   varchar(20) DEFAULT '000000',
    dept_id     int8,
    user_id     int8,
    order_num   int4        DEFAULT 0,
    test_key    varchar(255),
    value       varchar(255),
    version     int4        DEFAULT 0,
    create_dept int8,
    create_time timestamp,
    create_by   int8,
    update_time timestamp,
    update_by   int8,
    del_flag    int4        DEFAULT 0
);

COMMENT ON TABLE test_demo IS '测试单表';
COMMENT ON COLUMN test_demo.id IS '主键';
COMMENT ON COLUMN test_demo.tenant_id IS '租户编号';
COMMENT ON COLUMN test_demo.dept_id IS '部门id';
COMMENT ON COLUMN test_demo.user_id IS '用户id';
COMMENT ON COLUMN test_demo.order_num IS '排序号';
COMMENT ON COLUMN test_demo.test_key IS 'key键';
COMMENT ON COLUMN test_demo.value IS '值';
COMMENT ON COLUMN test_demo.version IS '版本';
COMMENT ON COLUMN test_demo.create_dept  IS '创建部门';
COMMENT ON COLUMN test_demo.create_time IS '创建时间';
COMMENT ON COLUMN test_demo.create_by IS '创建人';
COMMENT ON COLUMN test_demo.update_time IS '更新时间';
COMMENT ON COLUMN test_demo.update_by IS '更新人';
COMMENT ON COLUMN test_demo.del_flag IS '删除标志';

CREATE TABLE IF NOT EXISTS test_tree
(
    id          int8,
    tenant_id   varchar(20) DEFAULT '000000',
    parent_id   int8        DEFAULT 0,
    dept_id     int8,
    user_id     int8,
    tree_name   varchar(255),
    version     int4        DEFAULT 0,
    create_dept int8,
    create_time timestamp,
    create_by   int8,
    update_time timestamp,
    update_by   int8,
    del_flag    integer     DEFAULT 0
);

COMMENT ON TABLE test_tree IS '测试树表';
COMMENT ON COLUMN test_tree.id IS '主键';
COMMENT ON COLUMN test_tree.tenant_id IS '租户编号';
COMMENT ON COLUMN test_tree.parent_id IS '父id';
COMMENT ON COLUMN test_tree.dept_id IS '部门id';
COMMENT ON COLUMN test_tree.user_id IS '用户id';
COMMENT ON COLUMN test_tree.tree_name IS '值';
COMMENT ON COLUMN test_tree.version IS '版本';
COMMENT ON COLUMN test_tree.create_dept  IS '创建部门';
COMMENT ON COLUMN test_tree.create_time IS '创建时间';
COMMENT ON COLUMN test_tree.create_by IS '创建人';
COMMENT ON COLUMN test_tree.update_time IS '更新时间';
COMMENT ON COLUMN test_tree.update_by IS '更新人';
COMMENT ON COLUMN test_tree.del_flag IS '删除标志';

INSERT INTO test_demo
VALUES (1, '000000', 102, 4, 1, '测试数据权限', '测试', 0, 103, NOW(), 1, NULL, NULL, 0);
INSERT INTO test_demo
VALUES (2, '000000', 102, 3, 2, '子节点1', '111', 0, 103, NOW(), 1, NULL, NULL, 0);
INSERT INTO test_demo
VALUES (3, '000000', 102, 3, 3, '子节点2', '222', 0, 103, NOW(), 1, NULL, NULL, 0);
INSERT INTO test_demo
VALUES (4, '000000', 108, 4, 4, '测试数据', 'demo', 0, 103, NOW(), 1, NULL, NULL, 0);
INSERT INTO test_demo
VALUES (5, '000000', 108, 3, 13, '子节点11', '1111', 0, 103, NOW(), 1, NULL, NULL, 0);
INSERT INTO test_demo
VALUES (6, '000000', 108, 3, 12, '子节点22', '2222', 0, 103, NOW(), 1, NULL, NULL, 0);
INSERT INTO test_demo
VALUES (7, '000000', 108, 3, 11, '子节点33', '3333', 0, 103, NOW(), 1, NULL, NULL, 0);
INSERT INTO test_demo
VALUES (8, '000000', 108, 3, 10, '子节点44', '4444', 0, 103, NOW(), 1, NULL, NULL, 0);
INSERT INTO test_demo
VALUES (9, '000000', 108, 3, 9, '子节点55', '5555', 0, 103, NOW(), 1, NULL, NULL, 0);
INSERT INTO test_demo
VALUES (10, '000000', 108, 3, 8, '子节点66', '6666', 0, 103, NOW(), 1, NULL, NULL, 0);
INSERT INTO test_demo
VALUES (11, '000000', 108, 3, 7, '子节点77', '7777', 0, 103, NOW(), 1, NULL, NULL, 0);
INSERT INTO test_demo
VALUES (12, '000000', 108, 3, 6, '子节点88', '8888', 0, 103, NOW(), 1, NULL, NULL, 0);
INSERT INTO test_demo
VALUES (13, '000000', 108, 3, 5, '子节点99', '9999', 0, 103, NOW(), 1, NULL, NULL, 0);

INSERT INTO test_tree
VALUES (1, '000000', 0, 102, 4, '测试数据权限', 0, 103, NOW(), 1, NULL, NULL, 0);
INSERT INTO test_tree
VALUES (2, '000000', 1, 102, 3, '子节点1', 0, 103, NOW(), 1, NULL, NULL, 0);
INSERT INTO test_tree
VALUES (3, '000000', 2, 102, 3, '子节点2', 0, 103, NOW(), 1, NULL, NULL, 0);
INSERT INTO test_tree
VALUES (4, '000000', 0, 108, 4, '测试树1', 0, 103, NOW(), 1, NULL, NULL, 0);
INSERT INTO test_tree
VALUES (5, '000000', 4, 108, 3, '子节点11', 0, 103, NOW(), 1, NULL, NULL, 0);
INSERT INTO test_tree
VALUES (6, '000000', 4, 108, 3, '子节点22', 0, 103, NOW(), 1, NULL, NULL, 0);
INSERT INTO test_tree
VALUES (7, '000000', 4, 108, 3, '子节点33', 0, 103, NOW(), 1, NULL, NULL, 0);
INSERT INTO test_tree
VALUES (8, '000000', 5, 108, 3, '子节点44', 0, 103, NOW(), 1, NULL, NULL, 0);
INSERT INTO test_tree
VALUES (9, '000000', 6, 108, 3, '子节点55', 0, 103, NOW(), 1, NULL, NULL, 0);
INSERT INTO test_tree
VALUES (10, '000000', 7, 108, 3, '子节点66', 0, 103, NOW(), 1, NULL, NULL, 0);
INSERT INTO test_tree
VALUES (11, '000000', 7, 108, 3, '子节点77', 0, 103, NOW(), 1, NULL, NULL, 0);
INSERT INTO test_tree
VALUES (12, '000000', 10, 108, 3, '子节点88', 0, 103, NOW(), 1, NULL, NULL, 0);
INSERT INTO test_tree
VALUES (13, '000000', 10, 108, 3, '子节点99', 0, 103, NOW(), 1, NULL, NULL, 0);

-- 字符串自动转时间 避免框架时间查询报错问题
CREATE
OR
REPLACE function cast_varchar_to_timestamp(VARCHAR) RETURNS timestamptz AS $$
SELECT to_timestamp($1, 'yyyy-mm-dd hh24:mi:ss');
$$ LANGUAGE SQL strict ;

CREATE
cast
    (VARCHAR AS timestamptz)
    WITH FUNCTION cast_varchar_to_timestamp AS IMPLICIT;
