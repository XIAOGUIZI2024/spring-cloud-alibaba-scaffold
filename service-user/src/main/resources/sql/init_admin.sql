-- ============================================================
-- 初始化管理员账号
-- 约定：参考项目不做"管理员注册接口"，管理员账号通过数据订正初始化
-- 执行方式：在 service-user 连接的数据库（microservices）中手动执行一次
-- ============================================================

-- 默认管理员：手机号 13800000000，密码 123456
-- （密码在库中存 MD5 哈希：md5('123456') = e10adc3949ba59abbe56e057f20f883e）
-- 请按需修改 telephone / password_hash，生成新密码哈希可执行：
--   printf '你的密码' | md5sum
INSERT INTO `users`
(`gmt_create`, `gmt_modified`, `nick_name`, `password_hash`, `state`, `invite_code`,
 `telephone`, `inviter_id`, `last_login_time`, `profile_photo_url`, `block_chain_url`,
 `block_chain_platform`, `certification`, `real_name`, `id_card_no`, `user_role`,
 `deleted`, `lock_version`)
VALUES
(NOW(), NOW(), '管理员', 'e10adc3949ba59abbe56e057f20f883e', 'ACTIVE', NULL,
 '13800000000', NULL, NULL, NULL, NULL,
 NULL, NULL, NULL, NULL, 'ADMIN',
 0, 0);

-- 字段说明：
--   state       = 'ACTIVE'  管理员默认激活，可直接登录
--   user_role   = 'ADMIN'   管理员角色，admin 登录/操作时按此校验
--   deleted     = 0         未删除（MyBatis-Plus @TableLogic 查询过滤条件）
--   lock_version= 0         乐观锁初始版本
--   invite_code / inviter_id 等可留 NULL（不参与登录/管理操作）

-- 补充：如需修改管理员密码（将 123456 改成你的密码）
-- UPDATE `users`
-- SET `password_hash` = '你的新密码的MD5'
-- WHERE `telephone` = '13800000000' AND `deleted` = 0;
