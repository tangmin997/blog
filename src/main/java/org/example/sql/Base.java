package org.example.sql;

import org.example.utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Base {
    private static final String driver = DBConstants.driver;
    private static final String url = DBConstants.url;
    private static final String username = DBConstants.username;
    private static final String password = DBConstants.password;

    /**
     * 获取数据库连接
     *
     * @return
     * @throws ClassNotFoundException
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = (Connection) DriverManager.getConnection(url, username, password);
            System.out.println("数据库连接成功");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("数据库连接失败-驱动加载失败");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 公共查询
     *
     * @param connection        连接
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public static ResultSet executequery(Connection connection, PreparedStatement preparedStatement,String sql, Object[] params) {
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; params != null && i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            closeResource(connection, preparedStatement, resultSet);
            throw new RuntimeException(e);
        }
    }

    /**
     * 公共修改方法
     *
     * @param connection
     * @param preparedStatement
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public static int executeUpdate(Connection connection, PreparedStatement preparedStatement, String sql,
                                    Object[] params){
        try {
            if (preparedStatement == null) {
                preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
            }
            for (int i = 0; params != null && i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            return preparedStatement.executeUpdate();
        }
         catch (SQLException e) {
             closeResource(connection, preparedStatement, null);
            throw new RuntimeException(e);
        }
    }

    /**
     * 公共插入方法
     * @param connection 数据库连接
     * @param preparedStatement 预编译的SQL插入语句，如果为null则根据sql创建
     * @param sql 插入的SQL语句模板，包含占位符
     * @param params 插入语句的参数值数组
     * @return 插入操作影响的行数，或者在支持的情况下，新插入记录的自增ID
     * @throws SQLException SQL执行异常
     */
    public static int executeInsert(Connection connection, PreparedStatement preparedStatement, String sql, Object[] params){
        try {
            if (preparedStatement == null) {
                preparedStatement = connection.prepareStatement(sql);
            }
            for (int i = 0; params != null && i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            int insertedRows = preparedStatement.executeUpdate();
            return insertedRows;
        }  catch (SQLException e) {
            closeResource(connection, preparedStatement,null);
            throw new RuntimeException(e);
        }
    }

    /**
     * 释放资源
     *
     * @param connection
     * @param preparedStatement
     * @param resultSet
     * @return
     * @throws SQLException
     */
    public static boolean closeResource(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet)
    {
        boolean flag = true;
        if (resultSet != null) {
            try {
                resultSet.close();
                resultSet = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
                preparedStatement = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 从ResultSet中获取多个实体对象列表，根据指定的字段列表
     *
     * @param resultSet ResultSet对象
     * @param entityType 实体类的Class对象
     * @param fieldNames 数据库字段名称数组，与实体类属性通过驼峰命名规则对应
     * @param <T>       实体类的类型
     * @return 实体对象列表
     * @throws SQLException 反射或数据库访问异常
     */
    public static <T> List<T> getEntitiesFromResultSet(Connection connection, PreparedStatement preparedStatement,ResultSet resultSet, Class<T> entityType, String[] fieldNames){
        List<T> entities = new ArrayList<>();
        try {
            while (resultSet.next()) {
                T entity = entityType.getDeclaredConstructor().newInstance(); // 创建实体类实例

                // 根据指定的字段名列表来设置值
                for (String dbName : fieldNames) {
                    // 将数据库字段名转换为Java驼峰命名规则的属性名
                    String camelCaseName = StringUtils.toCamelCase(dbName);
                    Field field = entityType.getDeclaredField(camelCaseName);
                    String methodName = "set" + camelCaseName.substring(0, 1).toUpperCase() + camelCaseName.substring(1);

                    Method setterMethod = entityType.getMethod(methodName, field.getType());
                    Object value = resultSet.getObject(dbName);

                    // 调用setter方法设置值
                    setterMethod.invoke(entity, value);
                }

                entities.add(entity);
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException |
                 InvocationTargetException | RuntimeException | SQLException e) {
            closeResource(connection, preparedStatement, resultSet);
            e.printStackTrace();
        } finally {
            closeResource(connection, preparedStatement, resultSet);
        }

        return entities;
    }

    /**
     * 获取列数量
     * @param metaData
     * @return
     */
    public static int getColumnCount(ResultSetMetaData metaData){
        try {
            return metaData.getColumnCount();
        } catch (SQLException e) {
            throw new RuntimeException("获取列数量异常："+e);
        }
    }

    /**
     * 获取结果集元数据
     * @param resultSet
     * @return
     */
    public static ResultSetMetaData getResultSetMetaData(ResultSet resultSet){
        try {
            return resultSet.getMetaData();
        } catch (SQLException e) {
            throw new RuntimeException("获取结果集元数据异常："+e);
        }
    }

    /**
     * 获取列名称
     * @param metaData
     * @param i
     * @return
     */
    public static String getColumnName(ResultSetMetaData metaData,int i){
        try {
            return metaData.getColumnName(i);
        } catch (SQLException e) {
            throw new RuntimeException("获取列名称异常："+e);
        }
    }

    /**
     * 判断是否有下一个数据
     * @param rs
     * @return
     */
    public static boolean getNext(ResultSet rs){
        try {
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("判断是否有下一个数据异常："+e);
        }
    }

    /**
     * 获取列值
     * @param rs 结果集
     * @param i 列索引
     * @return 列值
     */
    public static String getString(ResultSet rs,int i){
        try {
            return rs.getString(i);
        } catch (SQLException e) {
            throw new RuntimeException("获取列值异常："+e);
        }
    }
}