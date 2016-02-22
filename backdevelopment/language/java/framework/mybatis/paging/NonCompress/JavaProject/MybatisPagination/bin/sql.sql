/*
 * mybatis分页例子
 * 
 * @author:刘吉超
 */

-- 创建数据库
create database test;
-- 创建用户
create user 'ljc' identified by '123';
-- 授权
grant all on test.* to 'ljc'@'%'; 
flush privileges;

use test;

/*
 * 创建表
 */
-- 学生表
CREATE TABLE STUDENT 
(
  SID       VARCHAR(40) COMMENT 'id',
  SNAME     VARCHAR(40) COMMENT '姓名',
  PRIMARY KEY(SID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学生表';


INSERT INTO student (SID, SNAME) VALUES ('1', '张三');
INSERT INTO student (SID, SNAME) VALUES ('2', '李四');
INSERT INTO student (SID, SNAME) VALUES ('3', '王五');
INSERT INTO student (SID, SNAME) VALUES ('4', '赵六');
INSERT INTO student (SID, SNAME) VALUES ('5', '田七');
INSERT INTO student (SID, SNAME) VALUES ('6', '王强');
INSERT INTO student (SID, SNAME) VALUES ('7', '王琦');
INSERT INTO student (SID, SNAME) VALUES ('8', '刘强');
INSERT INTO student (SID, SNAME) VALUES ('9', '刘集');
INSERT INTO student (SID, SNAME) VALUES ('10', '赵琪');
INSERT INTO student (SID, SNAME) VALUES ('11', '莉莉');
INSERT INTO student (SID, SNAME) VALUES ('12', '纪尚');
INSERT INTO student (SID, SNAME) VALUES ('13', '郭春刚');
INSERT INTO student (SID, SNAME) VALUES ('14', '赵春风');
INSERT INTO student (SID, SNAME) VALUES ('15', '吴睿');
INSERT INTO student (SID, SNAME) VALUES ('16', '涂洪祥');


-- 提交
COMMIT;