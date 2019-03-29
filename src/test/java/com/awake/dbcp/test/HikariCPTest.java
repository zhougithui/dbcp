package com.awake.dbcp.test;

import com.awake.dbcp.hikari.HikariCPUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Slf4j
public class HikariCPTest {

    @BeforeClass
    public static void init(){
        HikariCPUtils.initDataSource();
    }

    @Test
    public void test(){
        try {
            Connection connection = HikariCPUtils.getConnection();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from t_payment_order");
            while(resultSet.next()){
                log.info(resultSet.getString(3));
            }

            HikariCPUtils.releaseConn(connection);
        }catch (Exception e){
            log.error("数据库H2连接池获取失败");
        }
    }
}
