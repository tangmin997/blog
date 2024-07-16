# 使用最小的 Alpine Linux 作为基础镜像
FROM openjdk:17-jdk-alpine

# 设置工作目录
WORKDIR /app

# 将JAR文件复制到容器中
# 注意：这里使用 COPY 命令占位，实际使用时应替换为你的JAR文件的实际路径
COPY blog-1.0-SNAPSHOT.jar .

# 运行JAR文件
# 注意：这里使用 CMD 而不是 ENTRYPOINT，以便在运行容器时可以覆盖默认的JVM参数
CMD ["java", "-jar", "blog-1.0-SNAPSHOT.jar"]