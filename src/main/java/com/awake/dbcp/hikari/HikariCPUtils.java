package com.awake.dbcp.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class HikariCPUtils {
    private static HikariDataSource ds = null;

    public static DataSource initDataSource(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:test;MODE=Oracle");
        config.setUsername("root");
        config.setPassword("123456");
        config.setMaximumPoolSize(3);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        ds = new HikariDataSource(config);

        try {

            Connection connection = ds.getConnection();

            Statement statement = connection.createStatement();
            statement.execute(readFromFile("/schema.sql"));
            statement.executeUpdate(readFromFile("/init.sql"));

            connection.close();
        } catch (Exception e){
            log.error("h2数据库初始化失败", e);
        }
        return ds;
    }


    public static Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void releaseConn(Connection connection){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String readFromFile(String fileName){
        try {

            InputStream inputStream = HikariCPUtils.class.getResourceAsStream(fileName);
            BufferedReader bufferedInputStream = new BufferedReader(new InputStreamReader(inputStream));
            String data = bufferedInputStream.readLine();
            StringBuffer sb = new StringBuffer();
            while(data != null){
                sb.append(data);
                data = bufferedInputStream.readLine();
            }
            inputStream.close();
            bufferedInputStream.close();

            return sb.toString();
        }catch (Exception e){
            log.error("读取数据异常", e);
        }

        return null;
    }
}
