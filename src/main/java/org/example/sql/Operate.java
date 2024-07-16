package org.example.sql;

import org.example.bean.Post;
import org.example.bean.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Operate {

    private final int type;
    private final Connection connection;
    private  Object[] params;
    private  String sql;
    private PreparedStatement preparedStatement;
    // 数据库操作SQL语句

    /**
     * 查询用户表--根据用户名与密码查询-1
     */
    public static final String select_user_by_name_and_pwd_sql = "select * from users where username=? and password=?";

    /**
     * 查询用户表--根据用户名与邮箱查询-2
     */
    public static final String select_user_by_name_and_email = "select * from users where username=? or email=?";

    /**
     * 插入用户表--插入数据-3
     */
    public static final String insert_user_sql = "insert into users(username,email,Password,salt) values(?,?,?,?)";

    /**
     * 查询文章表--根据用户id查询-4
     */
    public static final String select_posts_by_user_id = "select * from posts where user_id=?";

    /**
     * 插入文章表--插入数据-5
     */
    public static final String insert_posts_sql = "insert into posts(title,content,user_id) values(?,?,?)";

    /**
     * 查询文章表--根据文章id查询-6
     */
    public static final String select_post_by_post_id = "select * from posts where post_id=?";

    /**
     * 更新文章表--根据文章id更新-7
     */
    public static final String update_post_by_post_id = "update posts set title=?,content=? where post_id=?";

    /**
     * 删除文章表--根据文章id删除-8
     */
    public static final String delete_post_by_post_id = "delete from posts where post_id=?";

    public Operate(int type, Object[] params){
        this.type = type;
        this.params = params;
        this.connection = Base.getConnection();
        if (type==1){
            this.sql = select_user_by_name_and_pwd_sql;
        }
        else if (type==2){
            this.sql=select_user_by_name_and_email;
        }
        else if (type==3){
            this.sql=insert_user_sql;
        }
        else if (type==4){
            this.sql=select_posts_by_user_id;
        }
        else if (type==5){
            this.sql=insert_posts_sql;
        }
        else if (type==6){
            this.sql=select_post_by_post_id;
        }
        else if (type==7){
            this.sql=update_post_by_post_id;
        }
        else if (type==8){
            this.sql=delete_post_by_post_id;
        }
        try {
            this.preparedStatement = this.connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询用户表--根据用户名与密码查询-1
     * @return 如果登录成功，返回true，否则返回false
     */
    public User userLogin(){
        ResultSet resultSet = Base.executequery(this.connection, this.preparedStatement, this.sql, this.params);
        List<User> userList = Base.getEntitiesFromResultSet(this.connection, this.preparedStatement, resultSet, User.class, new String[]{"user_id", "username", "password","email","created","last_modified","salt"});
        return userList.isEmpty()?null:userList.get(0);
    }

    /**
     * 查询用户表--根据用户名与邮箱查询-2
     * @return
     */
    public User selectUserByNameAndEmail(){
        ResultSet resultSet = Base.executequery(this.connection, this.preparedStatement, this.sql, this.params);
        List<User> userList = Base.getEntitiesFromResultSet(this.connection, this.preparedStatement, resultSet, User.class, new String[]{"user_id", "username", "password","email","created","last_modified","salt"});
        return userList.isEmpty()?null:userList.get(0);
    }

    /**
     * 用户注册
     * @return
     */
    public boolean userRegister(){
        return Base.executeInsert(this.connection, this.preparedStatement, this.sql, this.params)!=-1;
    }

    /**
     * 查询文章表--根据用户id查询-4
     */
    public List<Post> selectPostsByUserId(){
        ResultSet resultSet = Base.executequery(this.connection, this.preparedStatement, this.sql, this.params);
        List<Post> postList = Base.getEntitiesFromResultSet(this.connection, this.preparedStatement, resultSet, Post.class, new String[]{"post_id", "title", "content", "user_id", "created", "last_modified"});
        return postList;
    }
    
    public boolean insertPostsSql(){
        return Base.executeInsert(this.connection, this.preparedStatement, this.sql, this.params)!=-1;
    }

    /**
     * 查询文章表--根据文章id查询-6
     */
    public Post selectPostByPostId(){
        ResultSet resultSet = Base.executequery(this.connection, this.preparedStatement, this.sql, this.params);
        List<Post> postList = Base.getEntitiesFromResultSet(this.connection, this.preparedStatement, resultSet, Post.class, new String[]{"post_id", "title", "content", "user_id", "created", "last_modified"});
        return postList.isEmpty()?null:postList.get(0);
    }

    /**
     * 更新文章表--根据文章id更新-7
     */
    public boolean updatePostByPostId(){
        return Base.executeUpdate(this.connection, this.preparedStatement, this.sql, this.params)!=-1;
    }

    /**
     * 删除文章表--根据文章id删除-9
     */
    public boolean deletePostByPostId(){
        return Base.executeUpdate(this.connection, this.preparedStatement, this.sql, this.params)!=-1;
    }
}
