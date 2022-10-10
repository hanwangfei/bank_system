/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : bank

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 10/10/2022 08:55:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for loan_form
-- ----------------------------
DROP TABLE IF EXISTS `loan_form`;
CREATE TABLE `loan_form`  (
  `form_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `form_type` int NOT NULL COMMENT '贷款类型：1 房贷 2 车贷 3 学贷 4 其他',
  `start_time` datetime NOT NULL COMMENT '借款时间',
  `end_time` datetime NOT NULL COMMENT '还款时间',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '贷款原因',
  `create_time` datetime NOT NULL COMMENT '表单创建时间',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '当前表单状态，process审核中，approved同意，refaused驳回',
  `operator_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '经办人账户',
  `result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '经办人审批意见',
  `has_return` int NOT NULL COMMENT '是否已经还款，0表示未还，1表示已还',
  `count` decimal(10, 2) NOT NULL COMMENT '借款金额',
  PRIMARY KEY (`form_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of loan_form
-- ----------------------------
INSERT INTO `loan_form` VALUES (13, 3, 1, '2022-06-25 00:00:00', '2022-07-25 23:00:00', '借钱 买房', '2022-06-25 15:52:45', 'process', 'm1', NULL, 0, 2000.00);
INSERT INTO `loan_form` VALUES (14, 1, 1, '2022-06-25 00:00:00', '2023-07-25 23:00:00', '借钱买房', '2022-06-25 15:53:39', 'approved', 'm2', '同意', 0, 2000.00);
INSERT INTO `loan_form` VALUES (15, 3, 1, '2022-06-25 00:00:00', '2022-07-25 23:00:00', '借钱买房', '2022-06-25 16:13:28', 'approved', 'm1', '同意同意', 0, 2000.00);

-- ----------------------------
-- Table structure for node
-- ----------------------------
DROP TABLE IF EXISTS `node`;
CREATE TABLE `node`  (
  `node_id` bigint NOT NULL AUTO_INCREMENT COMMENT '节点编号',
  `node_type` int NOT NULL COMMENT '节点类型，1代表是模块，2代表是节点',
  `node_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '节点名称',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '功能地址，如果是模块则为空',
  `node_code` bigint NOT NULL COMMENT '节点编码，用于排序',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '只有功能才有，指向它属于哪个模块',
  PRIMARY KEY (`node_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of node
-- ----------------------------
INSERT INTO `node` VALUES (1, 1, '账户交易', NULL, 1000000, NULL);
INSERT INTO `node` VALUES (2, 2, '存款', '/forward/ck', 1000001, 1);
INSERT INTO `node` VALUES (3, 2, '取款', '/forward/qk', 1000002, 1);
INSERT INTO `node` VALUES (4, 2, '转账', '/forward/zz', 1000003, 1);
INSERT INTO `node` VALUES (5, 2, '查询', '/forward/cx', 1000004, 1);
INSERT INTO `node` VALUES (6, 1, '账户安全', NULL, 1000005, NULL);
INSERT INTO `node` VALUES (7, 2, '改密', '/forward/gm', 1000006, 6);
INSERT INTO `node` VALUES (8, 1, '贷款业务', NULL, 1000007, NULL);
INSERT INTO `node` VALUES (9, 2, '借款', '/forward/jk', 1000008, 8);
INSERT INTO `node` VALUES (10, 2, '还款', '/forward/hk', 1000009, 8);
INSERT INTO `node` VALUES (11, 1, '管理模块', NULL, 1000010, NULL);
INSERT INTO `node` VALUES (12, 2, '审核借款', '/forward/shjk', 1000011, 11);
INSERT INTO `node` VALUES (13, 2, '账号挂失', '/forward/zhgs', 1000012, 11);
INSERT INTO `node` VALUES (14, 2, '账号解挂', '/forward/zhjg', 1000013, 11);
INSERT INTO `node` VALUES (16, 2, '开户', '/forward/kh', 1000014, 11);
INSERT INTO `node` VALUES (17, 2, '销户', '/forward/xh', 1000015, 11);
INSERT INTO `node` VALUES (18, 2, '通知', '/forward/notice', 1000016, 1);

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `notice_id` bigint NOT NULL AUTO_INCREMENT,
  `reciver_id` bigint NOT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 186 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES (172, 3, '您的贷款申请已提交，申请单编号为:13,请耐心等待审批', '2022-06-25 15:52:45');
INSERT INTO `notice` VALUES (173, 1, '您有新的贷款申请，申请单编号为：13请尽快审批', '2022-06-25 15:52:45');
INSERT INTO `notice` VALUES (174, 1, '您的贷款申请已提交，申请单编号为:14,请耐心等待审批', '2022-06-25 15:53:39');
INSERT INTO `notice` VALUES (175, 2, '您有新的贷款申请，申请单编号为：14请尽快审批', '2022-06-25 15:53:40');
INSERT INTO `notice` VALUES (176, 2, '您对贷款单:14的贷款已审核完毕，您的结果是：同意,您的审批意见是：同意', '2022-06-25 15:54:17');
INSERT INTO `notice` VALUES (177, 1, '您的贷款单：14号的贷款申请已由m2审核完毕,结果是:同意,审批意见是:同意', '2022-06-25 15:54:17');
INSERT INTO `notice` VALUES (178, 1, '尊敬的用户您好，您的账户已存款2000.0元，当前余额为:3000.00元', '2022-06-25 15:54:17');
INSERT INTO `notice` VALUES (179, 1, '尊敬的用户您好，您当前账户余额为：3000.00元', '2022-06-25 15:54:32');
INSERT INTO `notice` VALUES (180, 3, '您的贷款申请已提交，申请单编号为:15,请耐心等待审批', '2022-06-25 16:13:28');
INSERT INTO `notice` VALUES (181, 1, '您有新的贷款申请，申请单编号为：15请尽快审批', '2022-06-25 16:13:28');
INSERT INTO `notice` VALUES (182, 1, '您对贷款单:15的贷款已审核完毕，您的结果是：同意,您的审批意见是：同意同意', '2022-06-25 16:14:20');
INSERT INTO `notice` VALUES (183, 3, '您的贷款单：15号的贷款申请已由m1审核完毕,结果是:同意,审批意见是:同意同意', '2022-06-25 16:14:20');
INSERT INTO `notice` VALUES (184, 3, '尊敬的用户您好，您的账户已存款2000.0元，当前余额为:3000.00元', '2022-06-25 16:14:20');
INSERT INTO `notice` VALUES (185, 4, '尊敬的用户您好，您当前账户余额为：1000.00元', '2022-06-25 16:14:34');
INSERT INTO `notice` VALUES (186, 3, '尊敬的用户您好，您当前账户余额为：3000.00元', '2022-06-25 16:14:49');

-- ----------------------------
-- Table structure for person
-- ----------------------------
DROP TABLE IF EXISTS `person`;
CREATE TABLE `person`  (
  `identity_id` bigint NOT NULL AUTO_INCREMENT COMMENT '身份证号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '家庭住址',
  PRIMARY KEY (`identity_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of person
-- ----------------------------
INSERT INTO `person` VALUES (1, '张三', '10010', '北京');
INSERT INTO `person` VALUES (2, '李四', '10011', '上海');
INSERT INTO `person` VALUES (3, '王五', '10012', NULL);
INSERT INTO `person` VALUES (4, '赵六', '10013', NULL);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `role_id` bigint NOT NULL COMMENT '角色id',
  `role_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色说明',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '客户');
INSERT INTO `role` VALUES (2, '管理人员');

-- ----------------------------
-- Table structure for role_node
-- ----------------------------
DROP TABLE IF EXISTS `role_node`;
CREATE TABLE `role_node`  (
  `rn_id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL,
  `node_id` int NOT NULL,
  PRIMARY KEY (`rn_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_node
-- ----------------------------
INSERT INTO `role_node` VALUES (1, 1, 1);
INSERT INTO `role_node` VALUES (2, 1, 2);
INSERT INTO `role_node` VALUES (3, 1, 3);
INSERT INTO `role_node` VALUES (4, 1, 4);
INSERT INTO `role_node` VALUES (5, 1, 5);
INSERT INTO `role_node` VALUES (6, 1, 6);
INSERT INTO `role_node` VALUES (7, 1, 7);
INSERT INTO `role_node` VALUES (8, 1, 8);
INSERT INTO `role_node` VALUES (9, 1, 9);
INSERT INTO `role_node` VALUES (10, 1, 10);
INSERT INTO `role_node` VALUES (11, 2, 1);
INSERT INTO `role_node` VALUES (12, 2, 2);
INSERT INTO `role_node` VALUES (13, 2, 3);
INSERT INTO `role_node` VALUES (14, 2, 4);
INSERT INTO `role_node` VALUES (15, 2, 5);
INSERT INTO `role_node` VALUES (16, 2, 6);
INSERT INTO `role_node` VALUES (17, 2, 7);
INSERT INTO `role_node` VALUES (18, 2, 8);
INSERT INTO `role_node` VALUES (19, 2, 9);
INSERT INTO `role_node` VALUES (20, 2, 10);
INSERT INTO `role_node` VALUES (21, 2, 11);
INSERT INTO `role_node` VALUES (22, 2, 12);
INSERT INTO `role_node` VALUES (23, 2, 13);
INSERT INTO `role_node` VALUES (24, 2, 14);
INSERT INTO `role_node` VALUES (25, 2, 15);
INSERT INTO `role_node` VALUES (26, 2, 16);
INSERT INTO `role_node` VALUES (27, 2, 17);
INSERT INTO `role_node` VALUES (28, 1, 18);
INSERT INTO `role_node` VALUES (29, 2, 18);

-- ----------------------------
-- Table structure for role_user
-- ----------------------------
DROP TABLE IF EXISTS `role_user`;
CREATE TABLE `role_user`  (
  `ru_id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`ru_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_user
-- ----------------------------
INSERT INTO `role_user` VALUES (1, 1, 3);
INSERT INTO `role_user` VALUES (2, 1, 4);
INSERT INTO `role_user` VALUES (3, 2, 1);
INSERT INTO `role_user` VALUES (4, 2, 2);
INSERT INTO `role_user` VALUES (5, 1, 8);
INSERT INTO `role_user` VALUES (6, 1, 9);
INSERT INTO `role_user` VALUES (7, 1, 10);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `state` int NOT NULL COMMENT '用户账户是否可用，1可用，2代表已被挂失不可用',
  `identity_id` bigint NOT NULL COMMENT '当前账户的实名身份证号，一个人可以有多个银行账户',
  `amount` decimal(10, 2) NOT NULL COMMENT '账户金额',
  `salt` bigint NOT NULL COMMENT '盐值，用于对密码加密',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'm1', 'test', 1, 1, 3000.00, 0);
INSERT INTO `user` VALUES (2, 'm2', '1234', 1, 2, 1000.00, 0);
INSERT INTO `user` VALUES (3, 's1', 'test', 1, 3, 3000.00, 0);
INSERT INTO `user` VALUES (4, 's2', 'test', 1, 4, 1000.00, 0);

SET FOREIGN_KEY_CHECKS = 1;
