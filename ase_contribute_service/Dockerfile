### 构建阶段

# 使用基础镜像
FROM openjdk:17-jdk as build

# 设置工作目录
WORKDIR /app

# 将当前目录下的所有文件复制到工作目录中
COPY . .

# 赋予 Maven Wrapper 可执行权限
RUN chmod +x ./mvnw

# 使用 Maven 编译项目，并打包成可执行的 .jar 文件（跳过测试）
RUN ./mvnw clean package -pl ase_contribute_service -am -DskipTests

### 运行阶段

# 使用基础镜像
FROM openjdk:17-jdk

# 设置工作目录
WORKDIR /app

# 从构建阶段复制编译好的 .jar 文件到运行阶段的工作目录中
COPY --from=build /app/ase_contribute_service/target/*.jar /app/app.jar

# 从构建阶段复制生产环境的配置文件到运行阶段的工作目录中
COPY --from=build /app/ase_contribute_service/src/main/resources/application-prod.yml /app/application.yml

# 暴露容器的 8082 端口，以便外部可以访问应用程序
EXPOSE 8082

# 指定容器启动时执行的命令，即运行 .jar 文件
CMD ["java", "-jar", "app.jar","--server.error.include-message=always"]