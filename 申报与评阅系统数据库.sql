/*
 Navicat Premium Data Transfer

 Source Server         : system
 Source Server Type    : MySQL
 Source Server Version : 90001
 Source Host           : 192.168.135.136:3306
 Source Schema         : application_system

 Target Server Type    : MySQL
 Target Server Version : 90001
 File Encoding         : 65001

 Date: 17/10/2024 19:59:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for declaration
-- ----------------------------
DROP TABLE IF EXISTS `declaration`;
CREATE TABLE `declaration`  (
  `bid` bigint NOT NULL AUTO_INCREMENT,
  `declarant_id` bigint NOT NULL,
  `declarant_email` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `project_brief` varchar(300) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `project_member` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `adviser_id` bigint NULL DEFAULT NULL,
  `teachers_advice` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL,
  `declaration_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `declaration_content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `modification_status` int NULL DEFAULT 1,
  `check_status` int NULL DEFAULT 0,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `ping_shen` int NULL DEFAULT 1,
  `project_id` bigint NOT NULL,
  `project_year` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `project_type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `project_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `file_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `file_path` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `pingshen_advice` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL,
  PRIMARY KEY (`bid`) USING BTREE,
  INDEX `declarant_id`(`declarant_id` ASC) USING BTREE,
  INDEX `adviser_id`(`adviser_id` ASC) USING BTREE,
  CONSTRAINT `declaration_ibfk_1` FOREIGN KEY (`declarant_id`) REFERENCES `users` (`uid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `declaration_ibfk_2` FOREIGN KEY (`adviser_id`) REFERENCES `users` (`uid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of declaration
-- ----------------------------
INSERT INTO `declaration` VALUES (45, 10, '2735195607@qq.com', '菜市场', '张岳平', 12, NULL, '张岳平的项目', '达到', 1, 0, '2024-10-17 16:22:33', '2024-10-17 16:25:25', 1, 44, '2029', '创新训练', '2029好项目', '', '', NULL);

-- ----------------------------
-- Table structure for jindu
-- ----------------------------
DROP TABLE IF EXISTS `jindu`;
CREATE TABLE `jindu`  (
  `jid` bigint NOT NULL AUTO_INCREMENT,
  `students_jindu` int NULL DEFAULT 0,
  `teacher_jindu` int NULL DEFAULT 0,
  `zhuanjia_jindu` int NULL DEFAULT 0,
  `colleage_jindu` int NULL DEFAULT 0,
  `school_jindu` int NULL DEFAULT 0,
  `fabu_jindu` int NULL DEFAULT 0,
  `p_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`jid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jindu
-- ----------------------------
INSERT INTO `jindu` VALUES (37, 1, 0, 0, 0, 0, 0, 42);
INSERT INTO `jindu` VALUES (38, 1, 1, 0, 0, 0, 0, 43);
INSERT INTO `jindu` VALUES (39, 1, 0, 0, 0, 0, 0, 44);
INSERT INTO `jindu` VALUES (40, 0, 0, 0, 0, 0, 0, 45);

-- ----------------------------
-- Table structure for pingshen
-- ----------------------------
DROP TABLE IF EXISTS `pingshen`;
CREATE TABLE `pingshen`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gong_hao` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `username` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `declaration_id` bigint NOT NULL,
  `declaration_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pingshen
-- ----------------------------

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project`  (
  `pid` bigint NOT NULL AUTO_INCREMENT,
  `leader_id` bigint NOT NULL,
  `project_year` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `project_notice` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `project_introduce` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `project_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `project_type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `project_numbers` int NOT NULL DEFAULT 0,
  `project_status` int NULL DEFAULT 0,
  `create_time` datetime NOT NULL,
  `end_time` datetime NULL DEFAULT NULL,
  `creater_id` bigint NOT NULL,
  `updater_id` bigint NOT NULL,
  `last_notification_count` int NULL DEFAULT 0,
  `identify_restriction` int NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `publish_time` datetime NULL DEFAULT NULL,
  `rest_time` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`pid`) USING BTREE,
  UNIQUE INDEX `unique_project_name`(`project_name` ASC) USING BTREE,
  INDEX `leader_id`(`leader_id` ASC) USING BTREE,
  CONSTRAINT `project_ibfk_1` FOREIGN KEY (`leader_id`) REFERENCES `users` (`uid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of project
-- ----------------------------
INSERT INTO `project` VALUES (43, 13, '2026', 'cascade', '达到', '2026好项目', '创新训练', 0, 2, '2024-10-16 10:22:59', '2024-10-17 00:00:00', 13, 13, 1, 3, '2024-10-17 16:17:55', '2024-10-17 16:17:55', 0);
INSERT INTO `project` VALUES (44, 13, '2029', '达到', '大时代', '2029好项目', '创新训练', 0, 1, '2024-10-17 16:19:57', '2024-10-23 00:00:00', 13, 13, 1, 3, '2024-10-17 16:20:15', '2024-10-17 16:20:15', 125);

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review`  (
  `rid` bigint NOT NULL AUTO_INCREMENT,
  `declaration_id` bigint NOT NULL,
  `ps_id` bigint NOT NULL,
  `review_status` int NULL DEFAULT 1,
  `review_level` int NULL DEFAULT 0,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`rid`) USING BTREE,
  INDEX `declaration_id`(`declaration_id` ASC) USING BTREE,
  INDEX `ps_id`(`ps_id` ASC) USING BTREE,
  CONSTRAINT `review_ibfk_1` FOREIGN KEY (`declaration_id`) REFERENCES `declaration` (`bid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `review_ibfk_2` FOREIGN KEY (`ps_id`) REFERENCES `users` (`uid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of review
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `uid` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `gxh` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `pwd` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `identify` int NOT NULL,
  `zhicheng` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '0',
  `ps_level` int NULL DEFAULT 0,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `creater_id` bigint NOT NULL,
  `updater_id` bigint NOT NULL,
  `phone_number` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`uid`) USING BTREE,
  UNIQUE INDEX `phone_number`(`phone_number` ASC) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (10, '张岳平', '2022315220314', '123', 3, '0', 20, '2024-08-17 11:25:27', '2024-10-10 15:39:21', 10, 10, '15623377130', '2735195607@qq.com');
INSERT INTO `users` VALUES (11, 'jack', '1231464689', '123', 3, '0', 0, '2024-08-20 11:15:14', '2024-08-20 11:15:14', 11, 11, '11245235', '15623377130@163.com');
INSERT INTO `users` VALUES (12, '张岳平', '2022315220314', '123', 2, '教授', 20, '2024-08-20 11:27:24', '2024-08-26 10:11:26', 12, 12, '19971341280', '19971341280@qq.com');
INSERT INTO `users` VALUES (13, '张岳平', '2022315220314', '123', 1, '0', 0, '2024-08-20 11:30:05', '2024-08-20 11:30:05', 13, 13, '15018548244', '15018548244@qq.com');
INSERT INTO `users` VALUES (22, 'rose', '1234', '123', 2, '讲师', 0, '2024-09-11 16:38:00', '2024-09-11 16:38:00', 22, 22, '1234', '1234');

SET FOREIGN_KEY_CHECKS = 1;
