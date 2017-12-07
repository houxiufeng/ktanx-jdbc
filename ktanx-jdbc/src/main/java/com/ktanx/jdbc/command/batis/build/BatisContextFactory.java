package com.ktanx.jdbc.command.batis.build;

import com.ktanx.common.spring.PathMatchingResourcePatternResolver;
import com.ktanx.common.spring.Resource;
import com.ktanx.common.spring.ResourcePatternResolver;
import com.ktanx.jdbc.command.batis.xml.XMLMapperBuilder;
import com.ktanx.jdbc.config.JdbcEngineConfig;
import com.ktanx.jdbc.exception.KtanxJdbcException;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by liyd on 2015-11-24.
 */
public class BatisContextFactory {

    private String batisConfigLocation;

    private JdbcEngineConfig jdbcEngineConfig;

    private Configuration configuration;

    public BatisContextFactory(String batisConfigLocation, JdbcEngineConfig jdbcEngineConfig) {
        this.batisConfigLocation = batisConfigLocation == null ? "classpath*:batis/*.xml" : batisConfigLocation;
        this.jdbcEngineConfig = jdbcEngineConfig;
    }

    public void init() {
        this.configuration = new Configuration();
        String[] sqlLocations = StringUtils.split(this.batisConfigLocation, ",");
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        for (String location : sqlLocations) {
            try {
                Resource[] resources = resourcePatternResolver.getResources(location);
                this.readResource(resources);
            } catch (Exception e) {
                throw new KtanxJdbcException("读取文件失败:" + location, e);
            }
        }
    }

    public BatisContext buildBatisContext() {
        this.init();
        return new BatisContextImpl(this.configuration);
    }

    /**
     * 读取resource
     *
     * @param resources
     */
    private void readResource(Resource[] resources) {

        for (Resource resource : resources) {
            try {
                XMLMapperBuilder mapperParser = new XMLMapperBuilder(resource, this.configuration, this.jdbcEngineConfig);
                mapperParser.parse();
            } catch (Exception e) {
                throw new KtanxJdbcException("读取resource文件失败:" + resource.getFilename(), e);
            }
        }
    }
}
