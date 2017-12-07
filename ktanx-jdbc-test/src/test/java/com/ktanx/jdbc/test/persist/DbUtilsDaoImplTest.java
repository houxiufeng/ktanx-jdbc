package com.ktanx.jdbc.test.persist;

import com.ktanx.common.model.PageList;
import com.ktanx.jdbc.command.entity.Select;
import com.ktanx.jdbc.persist.JdbcDao;
import com.ktanx.jdbc.test.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liyd on 17/8/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-dbutils.xml"})
public class DbUtilsDaoImplTest {

    @Autowired
    private JdbcDao jdbcDao;

    @Test
    public void get() {
        User user = jdbcDao.get(User.class, 177);
        Assert.assertNotNull(user);
        System.out.println(user.getLoginName());
    }

    @Test
    public void queryAll() {
        List<User> users = jdbcDao.queryAll(User.class);
        Assert.assertNotNull(users);
    }


    @Test
    public void queryList() {
        User user = new User();
        user.setUserAge(19);
        List<User> users = jdbcDao.queryList(user);
        Assert.assertNotNull(user);
        for (User u : users) {
            Assert.assertEquals("19", String.valueOf(u.getUserAge()));
        }
    }

    @Test
    public void queryPageList() {
        User user = new User();
        PageList<User> pageList = jdbcDao.queryPageList(user);

        System.out.println("当前页：" + pageList.getPager().getPageNum());
        System.out.println("总记录数：" + pageList.getPager().getTotalItems());
        System.out.println("总页数：" + pageList.getPager().getPages());

        Assert.assertNotNull(pageList.size() == 20);
    }

    @Test
    public void queryCount() {
        User user = new User();
        user.setUserAge(19);
        long count = jdbcDao.queryCount(user);
        Assert.assertTrue(count > 0);
    }


    @Test
    public void querySingleResult() {
        User user = new User();
        user.setUserId(177L);
        User u = jdbcDao.querySingleResult(user);
        Assert.assertNotNull(u);
    }

    @Test
    public void insert() {

        User user = new User();

        user.setUserAge(18);
        user.setLoginName("liyd2017");
        user.setPassword("2017");
        user.setUserType("1");
        user.setEmail("liyd@foxmail.com");
        user.setGmtCreate(new Date());

        Object id = jdbcDao.insert(user);
        System.out.println(id);
    }


    @Test
    public void insert1() {

        User user = new User();
        user.setLoginName("liyd2017");
        user.setPassword("2017");
        user.setUserType("1");
        user.setUserAge(18);
        user.setEmail("liyd@foxmail.com");
        user.setGmtCreate(new Date());

        Object id = jdbcDao.createInsert(User.class).setForEntity(user).execute();
        System.out.println(id);
    }

    @Test
    public void insert2() {

        User user = new User();
        user.setUserId(1800081L);
        user.setLoginName("liyd2017");
        user.setPassword("2017");
        user.setUserType("1");
        user.setUserAge(18);
        user.setEmail("liyd@foxmail.com");
        user.setGmtCreate(new Date());

        Object id = jdbcDao.createInsert(User.class).setForEntity(user).execute();
        System.out.println(id);
    }

    @Test
    public void insert3() {

        Object id = jdbcDao.createInsert(User.class)
                .set("loginName", "123321Name")
                .set("password", "123456")
                .execute();

        System.out.println(id);
    }

    @Test
    public void delete() {

        int count = jdbcDao.delete(User.class, 1800081L);
        Assert.assertTrue(count >= 0);
    }


    @Test
    public void delete2() {
        User user = new User();
        user.setPassword("2017");
        user.setLoginName("liyd2017");
        int count = jdbcDao.delete(user);
        Assert.assertTrue(count > 0);
        System.out.println(count);

    }

    @Test
    public void update() {
        User user = new User();
        user.setUserId(1800077L);
        user.setPassword("2017888");
        user.setLoginName("liyd2017888");
        int count = jdbcDao.update(user);
        Assert.assertTrue(count > 0);
        System.out.println(count);
    }

    @Test
    public void select() {

        Select<User> select = jdbcDao.createSelect(User.class)
                .where("userAge", new Object[]{19})
                .and("userType", "in", new Object[]{"1", "2"});

        long count = select.count();
        Assert.assertTrue(count > 0);

        List<User> list = select.list();
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0);

        PageList<User> pageList = select.pageList(1, 5);
        Assert.assertNotNull(pageList);
        Assert.assertTrue(pageList.size() == 5);

    }

    @Test
    public void select2() {


        User user = jdbcDao.createSelect(User.class)
                .where()
                .begin()
                .condition("loginName", new Object[]{"selfly"})
                .or("email", new Object[]{"selfly@foxmail.com"})
                .end()
                .and("password", new Object[]{"123456"})
                .singleResult();

        System.out.println(user);
    }

    @Test
    public void select3() {


        User user = jdbcDao.createSelect(User.class)
                .where("userId", new Object[]{1800077L})
                .singleResult();

        Assert.assertNotNull(user);
        System.out.println(user);

        user = jdbcDao.createSelect(User.class)
                .where("userId", 1800077L)
                .singleResult();

        Assert.assertNotNull(user);
        System.out.println(user);
    }

    @Test
    public void update2() {
        User user = new User();
        user.setPassword("201788833");
        user.setLoginName("liyd201788833");
        int count = jdbcDao.createUpdate(User.class)
                .setForEntity(user)
                .set("email", "1111@.qq.com")
                .updateNull()
                .where("userId", new Object[]{1800077L})
                .execute();
        Assert.assertTrue(count > 0);
        System.out.println(count);
    }

    @Test
    public void update3() {
        int count = jdbcDao.createUpdate(User.class)
                .set("email", "1111@.qq.com")
                .set("{{[userAge]}}", "[userAge] + 1")
                .where("userId", new Object[]{1800084L})
                .execute();
        Assert.assertTrue(count >= 0);
        System.out.println(count);
    }

    @Test
    public void simpleResult() {

        String name = jdbcDao.createSelect(User.class)
                .include("loginName")
                .where("userId", 175L)
                .singleColumnResult(String.class);

        Assert.assertNotNull(name);

        List<Long> userIds = jdbcDao.createSelect(User.class)
                .include("userId")
                .singleColumnList(Long.class);
        Assert.assertNotNull(userIds);
    }

    @Test
    public void nativeFieldSelect() {
        Object result = jdbcDao.createSelect(User.class)
                .addSelectField("date_format(gmt_create,'%Y-%m-%d %H:%k:%s') date")
                .notSelectEntityField()
                .where()
                .conditionId(177L)
                .objectResult();

        Assert.assertTrue(result instanceof String);

        Object result1 = jdbcDao.createSelect(User.class)
                .addSelectField("date_format([gmtCreate],'%Y-%m-%d %H:%k:%s') date")
                .notSelectEntityField()
                .where()
                .conditionId(177L)
                .objectResult();

        Assert.assertTrue(result1 instanceof String);

        Object result2 = jdbcDao.createSelect(User.class)
                .addSelectField("date_format(gmt_create,'%Y-%m-%d %H:%k:%s') date")
                .where()
                .conditionId(177L)
                .objectResult();

        Assert.assertTrue(result2 instanceof Map);

        Object result3 = jdbcDao.createSelect(User.class)
                .addSelectField("date_format(gmt_create,'%Y-%m-%d %H:%k:%s') date")
                .where("userAge", new Object[]{19})
                .objectList();

        Assert.assertTrue(result3 instanceof List);
    }

    @Test
    public void nativeFieldWhere() {

        List<User> list = jdbcDao.createSelect(User.class)
                .where("userAge", new Object[]{19})
                .and("{user_age}", new Object[]{19})
                .or("{{user_age}}", new Object[]{18})
                .or("{{[userAge]}}", new Object[]{20})
                .list();

        Assert.assertNotNull(list);
    }

    @Test
    public void nativeCount() {

        Long count = jdbcDao.createNativeExecutor().command("select count(*) from user").count();
        System.out.println(count);

    }

    @Test
    public void nativeSelect() {

        List<?> list = jdbcDao.createNativeExecutor().command("select * from user limit 0,10").list();
        System.out.println(list);

    }

    @Test
    public void nativeSingleResult() {

        Object obj = jdbcDao.createNativeExecutor()
                .command("select * from user where user_id = ?")
                .parameters(new Object[]{1800104L})
                .singleResult();
        System.out.println(obj);
    }

    @Test
    public void nativeExecutor() {

        int count = jdbcDao.createNativeExecutor()
                .command("update user set login_name = ? where user_id = ?")
                .parameters(new Object[]{"liyd7788", 1800104L})
                .update();

        System.out.println(count);
    }

    @Test
    public void nativeExecutor3() {

        PageList<?> pageList = jdbcDao.createNativeExecutor()
                .command("select * from user order by user_id desc")
                .pageList(1, 10);

        System.out.println(pageList);
    }

    @Test
    public void nativeExecutor2() {

        int count = jdbcDao.createNativeExecutor()
                .command("delete from user where user_id = ?")
                .parameters(new Object[]{1800081L})
                .update();

        System.out.println(count);
    }

    @Test
    public void nativeExecutor4() {

        String ddl = "CREATE TABLE `nativedb_table_user` (\n" +
                "  `USER_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',\n" +
                "  `LOGIN_NAME` varchar(32) DEFAULT NULL COMMENT '登录名',\n" +
                "  PRIMARY KEY (`USER_ID`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=1800144 DEFAULT CHARSET=utf8 COMMENT='用户';\n";

        int count = jdbcDao.createNativeExecutor()
                .command(ddl)
                .update();

        System.out.println(count);
    }

    @Test
    public void batisExecutor() {

        User user = new User();
        user.setLoginName("liyd2017");
        List<?> list = jdbcDao.createBatisExecutor()
                .command("User.queryUser")
                .parameters(new Object[]{"1", user})
                .list();

        System.out.println(list);
    }

    @Test
    public void batisExecutor2() {

        PageList<?> list = jdbcDao.createBatisExecutor()
                .command("User.queryUserList")
                .pageList(5, 50);

        System.out.println(list.getPager().getPageNum());
        System.out.println(list.getPager().getPageSize());
        System.out.println(list);
    }


}
