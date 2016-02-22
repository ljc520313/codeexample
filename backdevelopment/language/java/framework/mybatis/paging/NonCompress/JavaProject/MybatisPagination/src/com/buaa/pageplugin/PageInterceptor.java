package com.buaa.pageplugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;

/** 
* @ProjectName org.ishanglife.webservice
* @PackageName com.ishanglife.wx.common.vo.page
* @ClassName PageInterceptor
* @Description 通过拦截StatementHandler的prepare方法，重写sql语句实现物理分页
* @Author 刘吉超
* @Date 2016-01-25 11:01:30
*/
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class PageInterceptor implements Interceptor {
    private static final Log logger = LogFactory.getLog(PageInterceptor.class);
    
    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

    private static Map<String,String> driverDatabaseIdMap = new HashMap<String, String>();
    
    static {
    	driverDatabaseIdMap.put("com.mysql.jdbc.Driver", "MySQL");
    	driverDatabaseIdMap.put("oracle.jdbc.driver.OracleDriver", "Oracle");
    }
    
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        
        MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
        
        // 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环可以分离出最原始的的目标类)
        while (metaStatementHandler.hasGetter("h")) {
            Object object = metaStatementHandler.getValue("h");
            metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
        }
        
        // 分离最后一个代理对象的目标类
        while (metaStatementHandler.hasGetter("target")) {
            Object object = metaStatementHandler.getValue("target");
            metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
        }

        Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
        BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
        
        // 数据库类型
        String databaseId = getDatabaseId(configuration);
        // 参数对象
        Object parameterObject = boundSql.getParameterObject();
        // 参数类型
        Class<?> parameterType = mappedStatement.getParameterMap().getType();
        // sql类型
        String sqlCommandType = mappedStatement.getSqlCommandType().toString().toLowerCase();
        
        if("select".equalsIgnoreCase(sqlCommandType) && databaseId != null && parameterType != null && parameterObject != null 
        		&& PageVO.class.isAssignableFrom(parameterType) && parameterObject instanceof PageVO
        		&& isOpenPage((PageVO)parameterObject)){
        	
        	PageVO pageVO = (PageVO)parameterObject;
        	
        	// 重设分页参数里的总页数
            setPageParameter(boundSql.getSql(), (Connection) invocation.getArgs()[0], mappedStatement, boundSql, pageVO);
        	
            // 生成分页sql
            String pageSql = buildPageSql(databaseId, boundSql.getSql(), pageVO);
            // 重写sql
            metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
            
            // 采用物理分页后，就不需要mybatis的内存分页了，所以重置下面的两个参数
            metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
            metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
        }
        
        // 将执行权交给下一个拦截器
        return invocation.proceed();
    }

    /**
     * 是否开启分页功能，默认是开启的
     */
    private boolean isOpenPage(PageVO pageVO){
    	return pageVO.isOpenPage();
    }
    
    /**
     * 获得数据库类型
     */
    private String getDatabaseId(Configuration configuration){
    	if(configuration.getDatabaseId() != null){
    		return configuration.getDatabaseId().toLowerCase();
    	}
    	
    	// 驱动
    	String driver = configuration.getVariables().get("driver").toString();
    	
    	return driverDatabaseIdMap.get(driver);
    }
    
    /**
     * 从数据库获取总记录数
     * 
     * @param sql
     * @param connection
     * @param mappedStatement
     * @param boundSql
     * @param pageVO
     */
    private void setPageParameter(String sql, Connection connection, MappedStatement mappedStatement,BoundSql boundSql, PageVO pageVO) {
        // 记录总记录数
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {
            countStmt = connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
           
            BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
            // 设置参数
            ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, countBS.getParameterObject(), countBS);
            parameterHandler.setParameters(countStmt);
            
            rs = countStmt.executeQuery();
            
            // 移到最后一行 
            rs.last();
			
			pageVO.setTotalRowCount(rs.getRow());
        } catch (SQLException e) {
            logger.error("Ignore this exception", e);
        } finally {
        	if(rs != null){
        		try {
					rs.close();
				} catch (SQLException e) {
				}
        	}
            if(countStmt != null){
            	try {
					countStmt.close();
				} catch (SQLException e) {
				}
            }
        }
    }

    /**
     * 根据驱动，生成特定的分页sql
     * 
     * @param driver
     * @param sql
     * @param pageVO
     */
    private String buildPageSql(String databaseId, String sql, PageVO pageVO) {
    	// 转换成小写
    	databaseId = databaseId.toLowerCase();
    	// 分页语句
        StringBuilder pageSql = new StringBuilder();
        
        if (databaseId.contains("mysql")) {
            pageSql = buildPageSqlForMysql(sql, pageVO);
        } else if (databaseId.contains("oracle")) {
            pageSql = buildPageSqlForOracle(sql, pageVO);
        } else {
            return sql;
        }
        return pageSql.toString();
    }

    /**
     * mysql分页语句
     * 
     * @param sql
     * @param pageVO
     * @return String
     */
    private StringBuilder buildPageSqlForMysql(String sql, PageVO pageVO) {
        StringBuilder pageSql = new StringBuilder(100);
        
        pageSql.append(sql);
        pageSql.append(" limit " + pageVO.getCurrentPageMinRow() + "," + pageVO.getRowsPerPage());
        
        return pageSql;
    }

    /**
     * oracle分页语句
     * 
     * @param sql
     * @param pageVO
     * @return String
     */
    private StringBuilder buildPageSqlForOracle(String sql, PageVO pageVO) {
        StringBuilder pageSql = new StringBuilder(100);
        
        pageSql.append("select * from (select temp_oracle_table.*, rownum rownum_id from ( ");
        pageSql.append(sql);
        pageSql.append(" ) temp_oracle_table where rownum <= ").append(pageVO.getCurrentPageMaxRow());
        pageSql.append(") where rownum_id > ").append(pageVO.getCurrentPageMinRow());
        
        return pageSql;
    }

    public Object plugin(Object target) {
        // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }
    
    public void setProperties(Properties properties) {
    }
}
