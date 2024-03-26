# 使用 openjdk:17-jdk 作为基础镜像
FROM openjdk:17-jdk

# 维护者信息
LABEL maintainer="Rosy <2168603151@qq.com>"

# 设置工作目录
WORKDIR /app

# 将构建的jar文件复制到镜像的/app目录中
# 假设Dockerfile位于项目根目录，且ase_contribute_service的构建产物也在相对路径下
COPY target/ase_contribute_service-0.0.1-SNAPSHOT.jar /app/

# 暴露应用程序运行的端口
# 请根据你的应用实际使用的端口来修改此处的值，这里假设你的应用使用的是8080端口
EXPOSE 8080

# 定义容器启动时运行的命令，指定Spring Profile为prod
CMD ["java", "-jar", "ase_contribute_service-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]

