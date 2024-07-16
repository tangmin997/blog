## 01.介绍  
    这是一个简单的博客系统  
## 02.技术栈  
    --java 17  
    --springboot 3.2.7  
    --mysql8.0  
    --jwt  
## 03.功能  
    1.用户管理:  
        用户注册√  
        用户登录√  
        获取当前用户信息√  
    2.博客文章管理:  
        创建新文章√  
        获取所有文章列表√  
        获取单篇文章详情  
        更新文章√  
        删除文章√  
    3.实现基本的错误处理和日志记录  
    4.实现基本的权限控制:只有文章作者才能修改或删除自己的文章√  
    5.实现一个简单的 token √  
    6.在需要认证的接口中，从请求头获取并验证 token√  
    7.实现一个简单的拦截器或过滤器来处理认证逻辑√  
    8.密码加密存储√  
    9.使用 Docker 部署√  
## 04.数据库  
    001.User  
        user_id(主键)  
        username(用户名)  
        password(空码，需要加密存储)  
        email(邮箱)  
        created(创建时间)  
        last_modified(更新时间)  
        salt(盐值)  
	002.Post  
		post_id(主键)  
		title(标题)  
		content(内容)  
		user_id(作者ID，关联到用户表)  
		created(创建时间)  
		last modified(更新时间)  
## 05.部署  
    01.DBConstants.java中的服务器地址需要修改成自己的地址  
    02.上传Dockerfile和blog-1.0-SNAPSHOT.jar文件到服务器指定目录下  
    03.在服务器上执行以下命令构建镜像  
        docker build -t my-java-app:1.0 .  
    04.启动容器  
        docker run -d \  
        --name my-java-app \  
        -p 49160:8080 \  
        -e SERVER_PORT=8080 \  
        my-java-app:1.0  