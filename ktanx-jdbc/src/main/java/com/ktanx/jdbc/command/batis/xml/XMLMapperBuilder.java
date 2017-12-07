package com.ktanx.jdbc.command.batis.xml;

import com.ktanx.common.parser.GenericTokenParser;
import com.ktanx.common.spring.Resource;
import com.ktanx.jdbc.command.batis.build.BaseBuilder;
import com.ktanx.jdbc.command.batis.build.Configuration;
import com.ktanx.jdbc.command.batis.build.MapperBuilderAssistant;
import com.ktanx.jdbc.command.batis.parser.ClassFieldHandler;
import com.ktanx.jdbc.config.JdbcEngineConfig;
import com.ktanx.jdbc.exception.KtanxJdbcException;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by liyd on 2015-11-24.
 */
public class XMLMapperBuilder extends BaseBuilder {

    private XPathParser parser;
    private MapperBuilderAssistant builderAssistant;
    private Map<String, XNode> sqlFragments;
    private String resource;
    private JdbcEngineConfig jdbcEngineConfig;

    public XMLMapperBuilder(Resource resource, Configuration configuration, JdbcEngineConfig jdbcEngineConfig)
            throws IOException {
        super(configuration);
        this.builderAssistant = new MapperBuilderAssistant(configuration);
        this.sqlFragments = configuration.getSqlFragments();
        this.resource = resource.getFilename();
        this.jdbcEngineConfig = jdbcEngineConfig;
        this.parser = new XPathParser(resource.getInputStream(), true, configuration.getVariables(),
                new XMLMapperEntityResolver());
    }

    public void parse() {
        if (!configuration.isResourceLoaded(resource)) {
            configurationElement(parser.evalNode("/mapper"));
            configuration.addLoadedResource(resource);
        }
    }

    private void configurationElement(XNode context) {
        try {
            String namespace = context.getStringAttribute("namespace");
            if (namespace == null || namespace.equals("")) {
                throw new KtanxJdbcException("Mapper's namespace cannot be empty");
            }
            builderAssistant.setCurrentNamespace(namespace);
            sqlElement(context.evalNodes("/mapper/sql"));
            buildStatementFromContext(context.evalNodes("select|insert|update|delete"));
        } catch (Exception e) {
            throw new KtanxJdbcException("Error parsing Mapper XML. Cause: " + e, e);
        }
    }

    private void buildStatementFromContext(List<XNode> list) {
        for (XNode xNode : list) {
            final XMLStatementBuilder statementParser = new XMLStatementBuilder(configuration, builderAssistant, xNode);
            statementParser.parseStatementNode();
        }
    }

    private void sqlElement(List<XNode> list) throws Exception {
        GenericTokenParser tokenParser = new GenericTokenParser("${", "}", new ClassFieldHandler(this.jdbcEngineConfig));
        for (XNode xNode : list) {
            String id = xNode.getStringAttribute("id");
            id = builderAssistant.applyCurrentNamespace(id, false);

            Node node = xNode.getNode();
            this.processSql(node, tokenParser);

            sqlFragments.put(id, xNode);
        }
    }

    private void processSql(Node node, GenericTokenParser tokenParser) {

        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            processSql(item, tokenParser);
            if (item.getNodeType() != Node.TEXT_NODE) {
                continue;
            }
            String text = item.getTextContent();
            text = StringUtils.trim(text);
            text = StringUtils.replace(text, "\r", "");
            text = StringUtils.replace(text, "\n", "");
            while (StringUtils.indexOf(text, "  ") != -1) {
                text = StringUtils.replace(text, "  ", " ");
            }

            String result = tokenParser.parse(text);
            item.setTextContent(result);
        }
    }
}
