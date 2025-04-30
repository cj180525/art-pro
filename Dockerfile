# 父镜像
FROM openjdk:17

VOLUME /tmp

# 将当前目录内容复制到容器的工作目录中
COPY backend/target/*.jar /app.jar

# 使端口8080可用于世界范围内的连接
EXPOSE 31000

ENTRYPOINT ["java","-Dserver.port=31000","-jar","app.jar"]
