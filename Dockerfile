# 父镜像
FROM openjdk:17

MAINTAINER cj

# 将当前目录内容复制到容器的工作目录中
COPY *.jar /app.jar

# 使端口8080可用于世界范围内的连接
EXPOSE 31000

ENTRYPOINT ["java","-Dserver.port=31000","-Xms256m", "-Xmx512m","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]
