import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author cj180
 */
public class MybatisPlusGenerator {
    // 固定信息
    private static final String AUTHOR = "cj180";
    private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String ALL_TABLES_SQL = "select table_name from information_schema.tables where table_schema=?";

    // 数据库服务器账户密码
    private static final String DB_NAME = "art";
    private static final String DB_URL = "jdbc:mysql://101.35.179.232:3306/" + DB_NAME +
            "?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "Wc@123456";

    // 表信息
    private static final String TABLE_NAME = "pa_param_manage";
    private static final String TABLE_PREFIX = "pa";

    // 目录信息
    private static final String PACKAGE_NAME = "com.cayjin";
    private static final String MODULE_NAME = "backend";
    private static final String MODULE_ABSOLUTE_PATH = System.getProperty("user.dir") + "/" + MODULE_NAME;


    public static void main(String[] args) {
        FastAutoGenerator.create(DB_URL, DB_USERNAME, DB_PASSWORD)
                .globalConfig(builder -> builder
                        .author(AUTHOR) // 设置作者
                        .enableSwagger() // 开启 swagger 模式
                        .commentDate("yyyy-MM-dd") // 设置注释日期格式
                        .outputDir(MODULE_ABSOLUTE_PATH + "/src/main/java") // 指定输出目录
                        .dateType(DateType.TIME_PACK)
                        .disableOpenDir()
                )
                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    if (typeCode == Types.SMALLINT || typeCode == Types.TINYINT) {
                        // 自定义类型转换
                        return DbColumnType.INTEGER;
                    }
                    return typeRegistry.getColumnType(metaInfo);
                }))
                .packageConfig(builder -> {
                    builder.parent(PACKAGE_NAME) // 设置父包名
                            .moduleName(MODULE_NAME) // 设置父包模块名
                            .entity("entity") // 设置实体包名
                            .mapper("mapper") // 设置mapper包名
                            .controller("controller") // 设置controller包名
                            .service("service") // 设置service包名
                            .serviceImpl("service.impl") // 设置service实现类包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, MODULE_ABSOLUTE_PATH + "/src/main/resources/mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    try {
                        // 配置为单表还是多表
//                        builder.addInclude(getTables()) // 设置需要生成的表名(所有表)
                        builder.addInclude(TABLE_NAME) // 设置需要生成的表名
                                .addTablePrefix(TABLE_PREFIX)
                                // Entity 策略配置
                                .entityBuilder().enableFileOverride() // 覆盖已生成文件
                                .enableLombok() //开启 Lombok
                                .naming(NamingStrategy.underline_to_camel)  //数据库表映射到实体的命名策略：下划线转驼峰命
                                .columnNaming(NamingStrategy.underline_to_camel)    //数据库表字段映射到实体的命名策略：下划线转驼峰命
//                                .addTableFills(new Column("create_time", FieldFill.INSERT))
//                                .addTableFills(new Column("update_time", FieldFill.INSERT_UPDATE))
                                .addSuperEntityColumns("id", "create_time", "update_time", "is_deleted")  // 排除字段
                                // Mapper 策略配置
                                .mapperBuilder().enableFileOverride() // 覆盖已生成文件
                                .enableBaseResultMap()
//                                .enableBaseColumnList()
                                // Service 策略配置
                                .serviceBuilder().enableFileOverride() // 覆盖已生成文件
                                .formatServiceFileName("%sService") //格式化 service 接口文件名称，%s进行匹配表名，如 UserService
                                .formatServiceImplFileName("%sServiceImpl") //格式化 service 实现类文件名称，%s进行匹配表名，如 UserServiceImpl
                                // Controller 策略配置
                                .controllerBuilder().enableFileOverride() // 覆盖已生成文件
                        ;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

    private static String[] getTables() throws Exception {
        List<String> tables = new ArrayList<>();

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            Class.forName(DRIVER_CLASS_NAME);
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            ps = connection.prepareStatement(ALL_TABLES_SQL);
            ps.setString(1, DB_NAME);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                tables.add(resultSet.getString("table_name"));
            }
            return tables.toArray(new String[0]);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        throw new Exception("数据库连接异常！");
    }
}
