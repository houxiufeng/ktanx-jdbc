# ktanx 快速开发辅助工具包

本工具包是对平时项目中最常使用到的一些工具类的提取与封装，其核心组件为通用的数据操作层(dao层)。

以本人这些年来的实际工作经验感受，占用开发人员大量时间的是与数据库的增、删、改、查等基础操作打交道，而不是高大上的大数据、分布式及云计算等。

因此封装一个高效、便捷、通用的数据操作层是十分有必要的。

该工具包是本人在实际工作过程中，对于目前市面上简单封装的通用dao或数据访问层使用深感痛苦与不便，由此进行了整合和改进发展而来。

虽然从 Spring 4 开始支持了泛化注入，给通用持久层的封装带来了一定的方便，但还是需要建立一个个与实体对应继承于类似`BaseDao<T>`的通用泛型类，

这些号称通用的通用类还是显的过于烦锁和累赘，本通用dao层的目的就是消灭这些烦锁的代码，进一步减少机械式的重复编码工作。

当然，在搞定这些的同时还必须拥有高可扩展、可定制性，在带来方便、快速的同时不失优雅与灵活。

如果你也和我有同样的感受，觉得目前的一些主流框架如`Hibernate`、`Mybaits`等还不够便利，那么可以试试这个封装的通用dao层，这可能是目前封装的最方便易用的通用dao层了。

## 本通用dao的一些特性

1. 一个dao即可以搞定所有的实体类，省去建立跟实体对应的继承于类似BaseDao这类“通用dao”的麻烦。
2. 除了支持常用的`Entity`操作方式外，支持更强大的`Native`和`Batis`方式。
3. 实体操作时where支持一些复杂的条件，如`=`、`!=`、`or`、`in`、`not in`甚至是执行函数。
4. 允许在查询时指定使用哪个字段进行排序，可以指定多个进行组合升降序自由排序。
5. 支持在查询时指定返回字段的白名单和黑名单，可以指定只返回某些字段或不返回某些字段。
6. select查询时支持函数，`count()`、`max()`、`to_char()`、甚至是`distinct`,理论上都可以支持。
7. 直接内嵌分页功能，自动判断数据库，无须再写分页代码。
8. 可以使用`{{}}`、`{}`和`[]`完成一些特殊的操作，`{{}}`表示field和value原生执行，`{}`表示field原生执行，`[]`会进行命名转换，一般fieldName转columnName。
9. 支持执行自定义sql。
10. 支持使用类似mybatis的方式执行自定义sql。
11. 每个执行对象都允许有自定义的扩展实现，甚至允许不同的实体保存到不同的数据平台，使用上全透明。
12. 各模块功能的实现均允许用户进行自定义扩展，如自定义主键生成器、分页器、命令构建器(sql/hql等)、数据操作执行器等。

本通用dao层对于底层数据库的操作可以有不同的实现，本人不大喜欢用`Hibernate`这类容器式ORM框架，所以目前主要是 spring-jdbc 实现，也推荐 spring-jdbc 做为黄金搭档。

## 使用时需要了解的一些地方

约定优于配置原则，如果遵循了这些约定即可实现零注解零配置操作。典型约定如下：

- 实体类约定 实体类中应没有除了数据库表中列对应之外的其它属性(继承类不受影响)。
- 表名约定 `USER_INFO`表实体类名为`UserInfo`。
- 字段名约定 `USER_NAME`实体类中属性名为`userName`。
- 主键名约定 `USER_INFO`表主键名为`USER_INFO_ID`，同理实体类中属性名为`userInfoId`。
- `Oracle`序列名约定 `USER_INFO`表对应的主键序列名为`SEQ_USER_INFO`

当然，这些你可以使用注解或在扩展中改变它，但不建议这么做，这本身就是一个良好的规范。

## 最近更新

**版本 1.0.3 更新时间:2018-02-01**

1.拼装动态sql时，where问题的自动处理

2.拼装动态sql时，使用andConditionEntity方法

更多信息请看[这里](https://www.ktanx.com/ktxjdbc/p/5056)

## 使用

### 添加依赖 二选一

使用 spring jdbc：

    <dependency>
        <groupId>com.ktanx</groupId>
        <artifactId>ktanx-jdbc-springjdbc</artifactId>
        <version>${version}</version>
    </dependency>
    
使用 dbutils：

    <dependency>
        <groupId>com.ktanx</groupId>
        <artifactId>ktanx-jdbc-dbutils</artifactId>
        <version>${version}</version>
    </dependency>

### 根据选择使用的实现在spring中声明bean

使用 spring jdbc：

    <bean id="jdbcDao" class="com.ktanx.jdbc.springjdbc.persist.JdbcTemplateDaoImpl">
        <property name="dataSource" ref="dataSource"/>
    </bean>
    
使用 dbutils：

    <bean id="jdbcDao" class="com.ktanx.jdbc.dbutils.persist.DbUtilsDaoImpl">
        <property name="dataSource" ref="dataSource"/>
    </bean>
    
如果使用默认配置，这就是所有了。如果使用 spring boot，请自行用JavaConfig的方式声明bean。

接下来就可以注入到您的`Service`或者其它类中使用了。

    @Autowired
    private JdbcDao jdbcDao;

## 常用方法示例

这里的`Entity`对象为`User`，对于任何的`Entity`都是一样的.

先来看一下`User`对象及它继承的`Pageable`

	public class User extends Pageable {
		//......
	}

Pageable对象，用来保存页码、每页条数信息以支持分页

	public class Pageable implements Serializable {
		/** 每页显示条数 */
		protected int             itemsPerPage     = 20;
		/** 当前页码 */
		protected int             curPage          = 1;

		//......
	}

### 增、删、改、查
 
最基本的增、删、改、查自然少不了：

    //根据主键获取
    User user = jdbcDao.get(User.class, 177);
    
    //查询所有列表
    List<User> users = jdbcDao.queryAll(User.class);
    
    //查询分页列表
    PageList<User> list = jdbcDao.queryPageList(user);
    
    //会根据user对象中不为null的属性做为条件查询
    List<User> users = jdbcDao.queryList(user);
    
    //会根据user对象中不为null的属性做为条件查询
    long count = jdbcDao.queryCount(user);
    
    //会根据user对象中不为null的属性做为条件查询 没有数据时返回null，超过一条时会抛出异常
    User user = jdbcDao.querySingleResult(user);
    
    //会根据主键策略自动处理主键，如果实体对象中设置了主键值则不再处理
    //返回的主键值类型根据生成的主键不同可自行转换
    Long id = (Long)jdbcDao.insert(user);
    
    //根据主键更新实体对象
    jdbcDao.update(user);
    
    //根据主键删除实体对象
    jdbcDao.delete(User.class, 1800081L);
    
    //根据user对象中不为null的属性做为条件删除
    jdbcDao.delete(user);

### where条件设置

有时候你可能会需要稍微复杂一点的查询，这时可以通过创建`SelectExecutor`来实现：

    List<User> users = jdbcDao.createSelect(User.class)
                    .where("userAge", new Object[]{19})
                    .and("userType", "in", new Object[]{"1", "2"})
                    .and("loginName","like",new Object[]{"%liyd%"})
                    .list();
                    
### where条件中使用括号

    //将组成where条件：from user where ( login_name = ? or email = ? ) and password = ?
    User user = jdbcDao.createSelect(User.class)
                    .where()
                    .begin()
                    .condition("loginName", new Object[]{"selfly"})
                    .or("email", new Object[]{"selfly@foxmail.com"})
                    .end()
                    .and("password", new Object[]{"123456"})
                    .singleResult();
                    
### where中使用函数

可以用`{{}}` `{}` `[]`来实现：

    //{{}}表示属性和值都不经过加工直接到sql，{}表示属性名不加工，值传参 []表示里面的内容需要field到column的转换
    List<User> users = jdbcDao.createSelect(User.class)
            .where("{{[gmtCreate]}}", ">", new Object[] { "str_to_date('2017-10-1','%Y-%m-%d')" })
            .list();
  
### 黑、白名单

使用黑、白名单，这在一个表中某些字段内容特别大时比较有用

    //使用了白名单，将只返回loginName
    List<User> users = jdbcDao.createSelect(User.class)
            .include("loginName")
            .list();

    //使用了黑名单，将不返回loginName
    List<User> users = jdbcDao.createSelect(User.class)
            .exclude("loginName")
            .list();
            
### 自定义排序

    List<User> users = jdbcDao.createSelect(User.class)
       .orderBy("userId").desc()
       .orderBy("userAge").asc()
       .list();    
                
### count查询

除了返回值不一样外，其它没什么区别：

    long count = jdbcDao.createSelect(User.class)
        .where("userAge", new Object[]{19})
        .and("userType", "in", new Object[]{"1", "2"})
        .and("loginName", "like", new Object[]{"%liyd%"})
        .count();
        
### 分页查询

    PageList<User> pageList = jdbcDao.createSelect(User.class)
        .where("userAge", new Object[]{19})
        .and("userType", "in", new Object[]{"1", "2"})
        .and("loginName", "like", new Object[]{"%liyd%"})
        .pageList(1, 10);
    
    PageList<User> pageList = jdbcDao.createSelect(User.class)
        .where("userAge", new Object[]{19})
        .and("userType", new Object[]{"1", "2"})
        .and("loginName", "like", new Object[]{"%liyd%"})
        .pageList(user);
        
    System.out.println("当前页："+pageList.getPager().getPageNum());
    System.out.println("总记录数："+pageList.getPager().getTotalItems());
    System.out.println("总页数："+pageList.getPager().getPages());
        
### 自定义函数查询
        
    //notSelectEntityField 表示不返回实体类的属性 conditionId 表示以主键作为条件 [gmtCreate]表示field到column转换
    Object result = jdbcDao.createSelect(User.class)
        .addSelectField("date_format([gmtCreate],'%Y-%m-%d %H:%k:%s') date")
        .notSelectEntityField()
        .where()
        .conditionId(177L)
        .objectResult();
         
    //增加一个自定义函数，和实体属性一起返回 objectListResult根据具体实现返回会有不同，这里jdbcTemplate实现返回List<Map<String,Object>>
    Object result = jdbcDao.createSelect(User.class)
        .addSelectField("date_format(gmt_create,'%Y-%m-%d %H:%k:%s') date")
        .where("userAge", new Object[]{19})
        .objectListResult();       
    
### native属性方式

参考下面代码：

    List<User> list = jdbcDao.createSelect(User.class)
        .where("userAge", new Object[]{19}) //正常处理方式
        .and("{user_age}", new Object[]{19}) //属性名不做加工，值传参
        .or("{{user_age}}", new Object[]{18}) //属性名不做加工，值不传参
        .or("{{[userAge]}}", new Object[]{20}) //[]内属性名正常处理，值不传参
        .list();
            
### update操作

where部分和select一致：

    //根据实体设置属性值，忽略null属性
    int count = jdbcDao.createUpdate(User.class)
            .setForEntity(user)
            .where("userId", new Object[]{1800077L})
            .execute();
    
    //将null属性也同时更新
    int count = jdbcDao.createUpdate(User.class)
            .setForEntity(user)
            .updateNull()
            .where("userId", new Object[]{1800077L})
            .execute();
            
    //使用set设置单个属性
    int count = jdbcDao.createUpdate(User.class)
            .set("email", "1111@.qq.com")
            .set("password","123456")
            .where("userId", new Object[]{1800077L})
            .execute();
            
    //更新属性值为当前值+1
    int count = jdbcDao.createUpdate(User.class)
            .set("{{[userAge]}}", "[userAge] + 1")
            .where("userId", new Object[]{1800084L})
            .execute();
            
### delete操作

比较简单，where部分使用一致：

    int count = jdbcDao.createDelete(User.class)
            .where("userAge", new Object[]{19})
            .and("userType", "in", new Object[]{"1", "2"})
            .execute();
            
### insert操作

    //未设置主键值，将根据主键策略自动生成，默认数据库自增主键
    User user = new User();
    user.setLoginName("liyd2017");
    user.setPassword("2017");
    Object id = jdbcDao.createInsert(User.class).setForEntity(user).execute();

    //设置了主键，将不再处理，按照设置的主键值插入
    User user = new User();
    user.setUserId(1800081L);
    user.setLoginName("liyd2017");
    user.setPassword("2017");
    Object id = jdbcDao.createInsert(User.class).setForEntity(user).execute();

    //单个属性设置方式
    Object id = jdbcDao.createInsert(User.class)
        .set("loginName","123321Name")
        .set("password","123456")
        .execute();

### 表别名支持

有些时候，就算单表操作也必须用到表别名，例如oracle中的xmltype类型：

    Object obj = jdbcDao.createSelect(User.class)
            .tableAlias("t")
            .addSelectField("[xmlFile].getclobval() xmlFile")
            .notSelectEntityField()
            .where("userAge", new Object[]{19})
            .and("userType", "in", new Object[]{"1", "2"})
            .objectResult();
    //对应的sql
    select t.xmlfile.getclobval() xmlfile from user t where t.user_age = ? and t.user_type in (?,?)

### 执行自定义sql

在实际的应用中，一些复杂的查询如联表查询、子查询等是省不了的。

鉴于这类sql的复杂性和所需要的各类优化，通用dao层并没有直接封装而是提供了执行自定义sql的接口，可以通过`NativeExecutor`来实现：

    Object obj = jdbcDao.createNativeExecutor()
                .command("select * from user where user_id = ?")
                .parameters(new Object[]{1800104L})
                .singleResult();

    int count = jdbcDao.createNativeExecutor()
            .command("update user set login_name = ? where user_id = ?")
            .parameters(new Object[]{"liyd7788", 1800104L})
            .update();

### MyBatis方式执行自定义sql

除了上面的`NativeExecutor`接口外，考虑到直接在代码中写sql不利于项目的可维护性，另外提供了`BatisExecutor`接口，可以像`MyBatis`那样的方式来执行自定义sql。

默认在项目启动时会加载`classpath*:batis/*.xml`下的配置文件，里面的写法跟`MyBatis`大同小异，参数名`params`为固定：

    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE mapper
            PUBLIC "-//ktanx.com//DTD Mapper 2.0//EN"
            "http://www.ktanx.com/dtd/batis-mapper.dtd">
    <mapper namespace="User">
    
        <sql id="columns">
            ${com.ktanx.jdbc.test.model.User}
        </sql>
        <select id="queryUser">
            select 
            <include refid="columns"/>
            from user
            <where>
                <if test="params[0] != null">
                    user_type = #{params[0]}
                </if>
                <if test="params[1] != null and params[1].loginName != null">
                    and login_name = #{params[1].loginName}
                </if>
            </where>
        </select>
    
    </mapper>

然后在代码中调用：

    //支持传入复杂的参数，这里传入了一个简单的String类型和自定义对象User类型
    User user = new User();
    user.setLoginName("liyd2017");
    List<?> list = jdbcDao.createBatisExecutor()
            .command("User.queryUser")
            .parameters(new Object[]{"1", user})
            .list();

${com.ktanx.jdbc.test.model.User}会自动获取该实体类中的所有属性列，当然也可手动按需书写。

除了传入的参数为Object数组并使用`params[0]`这种方式访问相应的元素外，其它的和mybatis基本一样，mybatis支持的动态sql方式在这里也一样支持,因为他本身就是来源于mybatis。

>如果该通用dao不能满足需求，仍可以自己建立dao然后按平常的路子该怎么走就怎么走，完全不影响、无耦合。

##项目结构说明

ktanx-jdbc 数据操作通用接口封装，这里对于数据库访问没有具体的实现。具体的数据库操作取决于选择的实现方式。

ktanx-jdbc-springjdbc	数据库访问Spring Jdbc实现。

ktanx-jdbc-dbutils  数据库访问dbutils实现

ktanx-test 测试模块 

## 扩展

待续。。。
