DELETE
FROM sys_menu
WHERE menu_id IN (1604, 1605);
INSERT INTO sys_menu
VALUES ('1620', '配置列表', '118', '5', '#', '', '', 1, 0, 'F', '0', '0', 'system:ossConfig:list', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1621', '配置添加', '118', '6', '#', '', '', 1, 0, 'F', '0', '0', 'system:ossConfig:add', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1622', '配置编辑', '118', '6', '#', '', '', 1, 0, 'F', '0', '0', 'system:ossConfig:edit', '#', 103, 1, sysdate, NULL, NULL, '');
INSERT INTO sys_menu
VALUES ('1623', '配置删除', '118', '6', '#', '', '', 1, 0, 'F', '0', '0', 'system:ossConfig:remove', '#', 103, 1, sysdate, NULL, NULL, '');

