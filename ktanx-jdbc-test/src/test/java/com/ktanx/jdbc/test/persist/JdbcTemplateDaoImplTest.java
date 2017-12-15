package com.ktanx.jdbc.test.persist;

import com.ktanx.common.model.PageList;
import com.ktanx.jdbc.command.entity.Select;
import com.ktanx.jdbc.command.simple.DefaultResultHandler;
import com.ktanx.jdbc.persist.JdbcDao;
import com.ktanx.jdbc.test.BaseTest;
import com.ktanx.jdbc.test.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liyd on 17/4/12.
 */
public class JdbcTemplateDaoImplTest extends BaseTest {

    @Autowired
    private JdbcDao jdbcDao;

    @Before
    public void before() {
        //初始化测试数据
        jdbcDao.createDelete(User.class)
                .execute();
        for (int i = 1; i < 51; i++) {
            User user = new User();
            user.setUserId(Long.valueOf(i));
            user.setLoginName("name-" + i);
            user.setPassword("123456-" + i);
            user.setUserAge(i);
            user.setGmtCreate(new Date());

            jdbcDao.insert(user);
        }
    }

    @Test
    public void jdbcDaoGet() {
        User user = jdbcDao.get(User.class, 1L);
        Assert.assertNotNull(user);
        Assert.assertTrue(user.getUserId().equals(1L));
        Assert.assertTrue(user.getLoginName().equals("name-1"));
        Assert.assertTrue(user.getUserAge().equals(1));
        Assert.assertNotNull(user.getGmtCreate());
    }

    @Test
    public void jdbcDaoQueryAll() {
        List<User> users = jdbcDao.queryAll(User.class);
        Assert.assertNotNull(users);
        Assert.assertTrue(users.size() == 50);
    }

    @Test
    public void jdbcDaoQueryList() {
        User user = new User();
        user.setUserAge(10);
        List<User> users = jdbcDao.queryList(user);
        Assert.assertNotNull(users);
        Assert.assertTrue(users.size() == 1);
    }

    @Test
    public void jdbcDaoQueryPageList() {
        User user = new User();
        user.setPageSize(10);
        List<User> users = jdbcDao.queryPageList(user);
        Assert.assertNotNull(users);
        Assert.assertTrue(users.size() == 10);
    }


    @Test
    public void jdbcDaoQueryPageList2() {
        User user = new User();
        user.setPageSize(10);
        user.setUserAge(10);
        List<User> users = jdbcDao.queryPageList(user);
        Assert.assertNotNull(users);
        Assert.assertTrue(users.size() == 1);
    }


    @Test
    public void jdbcDaoQueryCount() {
        User user = new User();
        user.setUserAge(10);
        long count = jdbcDao.queryCount(user);
        Assert.assertTrue(count == 1);
    }

    @Test
    public void jdbcDaoQueryCount2() {
        User user = new User();
        long count = jdbcDao.queryCount(user);
        Assert.assertTrue(count == 50);
    }

    @Test
    public void jdbcDaoQuerySingleResult() {
        User tmp = new User();
        tmp.setUserAge(10);
        User user = jdbcDao.querySingleResult(tmp);
        Assert.assertNotNull(user);
        Assert.assertTrue(user.getUserId().equals(10L));
        Assert.assertTrue(user.getLoginName().equals("name-10"));
        Assert.assertTrue(user.getUserAge().equals(10));
        Assert.assertNotNull(user.getGmtCreate());
    }


    @Test
    public void jdbcDaoInsert() {

        User user = new User();
        user.setLoginName("liyd2017");
        user.setPassword("2017");
        user.setUserAge(18);
        user.setGmtCreate(new Date());

        Long id = (Long) jdbcDao.insert(user);
        Assert.assertTrue(id > 0);

        User user1 = jdbcDao.get(User.class, id);
        Assert.assertNotNull(user1);
        Assert.assertTrue(user1.getUserId().equals(id));
        Assert.assertTrue(user1.getLoginName().equals("liyd2017"));
        Assert.assertTrue(user1.getUserAge().equals(18));
        Assert.assertNotNull(user1.getGmtCreate());
    }

    @Test
    public void jdbcDaoDelete() {
        int count = jdbcDao.delete(User.class, 15L);
        Assert.assertTrue(count == 1);
        User user = jdbcDao.get(User.class, 15L);
        Assert.assertNull(user);
    }


    @Test
    public void jdbcDaoDelete2() {
        User user = new User();
        user.setLoginName("name-17");
        int count = jdbcDao.delete(user);
        Assert.assertTrue(count == 1);

        User tmp = new User();
        tmp.setLoginName("name-17");
        User user1 = jdbcDao.querySingleResult(tmp);
        Assert.assertNull(user1);
    }


    @Test
    public void jdbcDaoUpdate() {
        User user = new User();
        user.setUserId(20L);
        user.setPassword("666666");
        user.setLoginName("666777");
        user.setGmtModify(new Date());
        int count = jdbcDao.update(user);
        Assert.assertTrue(count == 1);

        User user1 = jdbcDao.get(User.class, 20L);
        Assert.assertNotNull(user1);
        Assert.assertTrue(user1.getUserId().equals(20L));
        Assert.assertTrue(user1.getLoginName().equals("666777"));
        Assert.assertTrue(user1.getPassword().equals("666666"));
        Assert.assertNotNull(user1.getGmtModify());
    }


//    /**
//     * 创建insert对象
//     *
//     * @param entityClass
//     * @param <T>
//     * @return
//     */
//    <T> Insert<T> createInsert(Class<T> entityClass);
//
//    /**
//     * 创建delete对象
//     *
//     * @param entityClass
//     * @param <T>
//     * @return
//     */
//    <T> Delete<T> createDelete(Class<T> entityClass);
//
//    /**
//     * 创建update对象
//     *
//     * @param entityClass
//     * @param <T>
//     * @return
//     */
//    <T> Update<T> createUpdate(Class<T> entityClass);
//
//    /**
//     * 创建native executor对象
//     *
//     * @return
//     */
//    NativeExecutor createNativeExecutor();
//
//    /**
//     * 创建batis executor对象
//     *
//     * @return
//     */
//    BatisExecutor createBatisExecutor();

    @Test
    public void insert() {

        User user = new User();
        user.setLoginName("name-60");
        user.setPassword("606060");
        user.setUserAge(60);
        user.setGmtCreate(new Date());

        Long id = (Long) jdbcDao.createInsert(User.class).setForEntity(user).execute();

        User user1 = jdbcDao.get(User.class, id);
        Assert.assertNotNull(user1);
        Assert.assertTrue(user1.getUserId().equals(id));
        Assert.assertTrue(user1.getLoginName().equals("name-60"));
        Assert.assertTrue(user1.getPassword().equals("606060"));
    }

    @Test
    public void insert2() {

        Long id = (Long) jdbcDao.createInsert(User.class)
                .set("loginName", "name123")
                .set("password", "123321")
                .execute();

        User user1 = jdbcDao.get(User.class, id);
        Assert.assertNotNull(user1);
        Assert.assertTrue(user1.getUserId().equals(id));
        Assert.assertTrue(user1.getLoginName().equals("name123"));
        Assert.assertTrue(user1.getPassword().equals("123321"));
    }


    @Test
    public void select() {

        for (int i = 60; i < 70; i++) {
            User user = new User();
            user.setUserId(Long.valueOf(i));
            user.setLoginName("name2-19");
            user.setPassword("123456-" + i);
            user.setUserAge(19);
            user.setGmtCreate(new Date());
            jdbcDao.insert(user);
        }

        Select<User> select1 = jdbcDao.createSelect(User.class)
                .where("userAge", 19);
        long count1 = select1.count();
        Assert.assertTrue(count1 == 11);

        List<User> list1 = select1.list();
        Assert.assertTrue(list1.size() == 11);

        PageList<User> pageList1 = select1.pageList(1, 5);
        Assert.assertTrue(pageList1.size() == 5);

        Select<User> select2 = jdbcDao.createSelect(User.class)
                .where("userAge", 19)
                .and("loginName", "name2-19");
        long count2 = select2.count();
        Assert.assertTrue(count2 == 10);

        List<User> list2 = select2.list();
        Assert.assertTrue(list2.size() == 10);

        PageList<User> pageList2 = select2.pageList(1, 5);
        Assert.assertTrue(pageList2.size() == 5);
    }

    @Test
    public void select2() {

        try {
            User user = jdbcDao.createSelect(User.class)
                    .where()
                    .begin()
                    .condition("loginName", "name-19")
                    .or("userAge", 21)
                    .end()
                    .singleResult();
        } catch (IncorrectResultSizeDataAccessException e) {
            Assert.assertTrue("Incorrect result size: expected 1, actual 2".equals(e.getMessage()));
        }

        User user2 = jdbcDao.createSelect(User.class)
                .where()
                .begin()
                .condition("loginName", "name-19")
                .or("userAge", 21)
                .end()
                .and("password", "123456-19")
                .singleResult();
        Assert.assertNotNull(user2);
    }

    @Test
    public void select3() {

        List<User> list = jdbcDao.createSelect(User.class)
                .include("userId", "password")
                .where("userAge", "<=", 10)
                .list();
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() == 10);
        for (User user : list) {
            Assert.assertNotNull(user.getUserId());
            Assert.assertNotNull(user.getPassword());
            Assert.assertNull(user.getLoginName());
            Assert.assertNull(user.getUserAge());
            Assert.assertNull(user.getGmtCreate());
        }
    }

    @Test
    public void select4() {

        List<User> list = jdbcDao.createSelect(User.class)
                .exclude("userId", "password")
                .orderBy("userAge").asc()
                .list();
        Assert.assertNotNull(list);
        int preAge = 0;
        for (User user : list) {
            Assert.assertNull(user.getUserId());
            Assert.assertNull(user.getPassword());
            Assert.assertNotNull(user.getLoginName());
            Assert.assertNotNull(user.getUserAge());
            Assert.assertNotNull(user.getGmtCreate());
            Assert.assertTrue(user.getUserAge() > preAge);
            preAge = user.getUserAge();
        }
    }

    @Test
    public void select5() {

        Long maxId = jdbcDao.createSelect(User.class)
                .addSelectField("max([userId]) maxid")
                .notSelectEntityField()
                .singleColumnResult(Long.class);
        Assert.assertTrue(maxId == 50);
    }

    @Test
    public void select6() {
        List<Long> ids = jdbcDao.createSelect(User.class)
                .include("userId")
                .singleColumnList(Long.class);
        Assert.assertNotNull(ids);
    }


    @Test
    public void select7() {
        List<Long> ids = jdbcDao.createSelect(User.class)
                .include("userId")
                .singleColumnPageList(Long.class, 1, 5);
        Assert.assertTrue(ids.size() == 5);
    }

    @Test
    public void select8() {
        Object result = jdbcDao.createSelect(User.class)
                .where("userId", 15L)
                .objectResult();
        Assert.assertTrue(result instanceof Map);
    }

    @Test
    public void select9() {
        Object result = jdbcDao.createSelect(User.class)
                .where("userId", 15L)
                .forceResultToModel()
                .objectResult();
        Assert.assertTrue(result instanceof User);
    }

    @Test
    public void select10() {
        Object result = jdbcDao.createSelect(User.class)
                .objectList();
        Assert.assertTrue(result instanceof List);
        Assert.assertTrue(((List) result).get(0) instanceof Map);
    }

    @Test
    public void select11() {
        User pageable = new User();
        pageable.setPageSize(5);
        Object result = jdbcDao.createSelect(User.class)
                .objectPageList(pageable);
        Assert.assertTrue(result instanceof List);
        Assert.assertTrue(((List) result).size() == 5);
        Assert.assertTrue(((List) result).get(0) instanceof Map);
    }

    @Test
    public void select12() {
        User pageable = new User();
        pageable.setPageSize(5);
        Object result = jdbcDao.createSelect(User.class)
                .include("userId")
                .singleColumnPageList(Long.class, pageable);
        Assert.assertTrue(result instanceof List);
        Assert.assertTrue(((List) result).size() == 5);
        Assert.assertTrue(((List) result).get(0) instanceof Long);
    }

    @Test
    public void select13() {
        List<User> users = jdbcDao.createSelect(User.class)
                .tableAlias("t1")
                .where("userId", new Object[]{11L, 12L, 13L})
                .and("loginName", new Object[]{"name-11", "name-12", "name-13"})
                .and()
                .condition("userAge", new Object[]{11L, 12L, 13L})
                .list();
        Assert.assertTrue(users.size() == 3);
    }

    @Test
    public void select14() {
        List<User> users = jdbcDao.createSelect(User.class)
                .where("userId", ">", 10L)
                .and()
                .begin()
                .condition("userAge", 5)
                .or()
                .condition("userAge", 8)
                .or("userAge", new Object[]{7L, 9L})
                .end()
                .list();
        Assert.assertNotNull(users);
    }

    @Test
    public void groupByAndOrderBy() {
        User user = new User();
        user.setLoginName("name2-19");
        user.setPassword("123456-");
        user.setUserAge(19);
        user.setGmtCreate(new Date());
        jdbcDao.insert(user);

        Select<User> select = jdbcDao.createSelect(User.class)
                .addSelectField("count(*) num")
                .groupBy("userAge")
                .orderBy("num").desc()
                .forceResultToModel();
        List<?> list = select.objectPageList(1, 5);

        Assert.assertTrue(list.size() == 5);
        Assert.assertTrue(((long) ((User) list.get(0)).get("num")) == 2L);
        Assert.assertTrue(((long) ((User) list.get(1)).get("num")) == 1L);

        List<?> objects = select.objectList();
        Assert.assertTrue(objects.size() == 50);
    }

    @Test
    public void update2() {

        jdbcDao.createUpdate(User.class)
                .set("loginName", "newName")
                .where("userId", 15L)
                .execute();

        User user1 = jdbcDao.get(User.class, 15L);
        Assert.assertTrue(user1.getLoginName().equals("newName"));
    }

    @Test
    public void update3() {

        User user = new User();
        user.setUserId(17L);
        user.setLoginName("newName22");
        user.setPassword("abc");
        //没有设置where条件，将更新所有
        jdbcDao.createUpdate(User.class)
                .setForEntity(user)
                .execute();

        User user1 = jdbcDao.get(User.class, 17L);
        Assert.assertTrue(user1.getLoginName().equals("newName22"));
        Assert.assertTrue(user1.getPassword().equals("abc"));
    }

    @Test
    public void update4() {

        User user = new User();
        user.setUserId(17L);
        jdbcDao.createUpdate(User.class)
                .setForEntityWhereId(user)
                .updateNull()
                .execute();

        User user1 = jdbcDao.get(User.class, 17L);
        Assert.assertNull(user1.getLoginName());
        Assert.assertNull(user1.getPassword());
        Assert.assertNull(user1.getUserAge());
        Assert.assertNull(user1.getGmtCreate());
    }


    @Test
    public void update5() {

        try {
            User user = new User();
            user.setLoginName("newName22");
            user.setPassword("abc");
            //没有设置where条件，将更新所有
            jdbcDao.createUpdate(User.class)
                    .setForEntityWhereId(user)
                    .execute();
        } catch (Exception e) {
            Assert.assertEquals("主键属性值不能为空:userId", e.getMessage());
        }

    }

    @Test
    public void update6() {

        jdbcDao.createUpdate(User.class)
                .set("{{[userAge]}}", "[userAge]+1")
                .where("userId", 17L)
                .execute();

        User user = jdbcDao.get(User.class, 17L);
        Assert.assertTrue(user.getUserAge() == 18);
    }


    @Test
    public void nativeExecutor() {
        int count = jdbcDao.createNativeExecutor()
                .command("update user set login_name = ? where user_id = ?")
                .parameters(new Object[]{"newName", 13L})
                .update();
        Assert.assertTrue(count == 1);
        User user = jdbcDao.get(User.class, 13L);
        Assert.assertEquals(user.getLoginName(), "newName");
    }

    @Test
    public void nativeExecutor2() {

        Object result = jdbcDao.createNativeExecutor()
                .command("select t.*,date_format(t.gmt_create,'%Y-%m-%d %H:%k:%s') date from user t where t.user_id = ?")
                .parameters(new Object[]{13L})
                .resultClass(User.class)
                .singleResult();
        Assert.assertTrue(result instanceof User);
        Assert.assertTrue(((User) result).get("date") != null);
    }

    @Test
    public void nativeExecutor3() {

        Object result = jdbcDao.createNativeExecutor()
                .command("select t.*,date_format(t.gmt_create,'%Y-%m-%d %H:%k:%s') date from user t")
                .resultClass(User.class)
                .list();
        Assert.assertTrue(result instanceof List);
        Assert.assertTrue(((List<User>) result).get(0).get("date") != null);
    }

    @Test
    public void nativeExecutor4() {

        Object result = jdbcDao.createNativeExecutor()
                .command("select t.*,date_format(t.gmt_create,'%Y-%m-%d %H:%k:%s') date from user t")
                .resultHandler(DefaultResultHandler.newInstance(User.class))
                .list();
        Assert.assertTrue(result instanceof List);
        Assert.assertTrue(((List<User>) result).get(0).get("date") != null);
    }

    @Test
    public void nativeExecutor5() {

        Long result = jdbcDao.createNativeExecutor()
                .command("select count(*) from user t")
                .count();
        Assert.assertTrue(result == 50);
    }


    @Test
    public void nativeExecutor6() {

        Object result = jdbcDao.createNativeExecutor()
                .command("select t.*,date_format(t.gmt_create,'%Y-%m-%d %H:%k:%s') date from user t")
                .resultHandler(DefaultResultHandler.newInstance(User.class))
                .pageList(1, 5);
        Assert.assertTrue(result instanceof List);
        Assert.assertTrue(((List<User>) result).size() == 5);
    }

    @Test
    public void nativeExecutor7() {

        jdbcDao.createNativeExecutor()
                .command("update user set user_age = 18 where user_age < 18")
                .execute();

        long count = jdbcDao.createSelect(User.class)
                .where("userAge", "<", 18)
                .count();
        Assert.assertTrue(count == 0);
    }

//    @Test
//    public void batisExecutor() {
//
//        User user = new User();
//        user.setLoginName("liyd2017");
//        List<?> list = jdbcDao.createBatisExecutor()
//                .command("User.queryUser")
//                .parameters(new Object[]{"1", user})
//                .list();
//
//        System.out.println(list);
//    }
//
//    @Test
//    public void batisExecutor2() {
//
//        PageList<?> list = jdbcDao.createBatisExecutor()
//                .command("User.queryUserList")
//                .pageList(5, 50);
//
//        System.out.println(list.getPager().getPageNum());
//        System.out.println(list.getPager().getPageSize());
//        System.out.println(list);
//    }
}
