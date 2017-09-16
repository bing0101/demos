package com.bing.shardingjdbc.mybatis.configuration;

import lombok.Data;

/**
 * Created by Administrator on 2017/9/15.
 */
@Data
public class Database {
    private String dbname;
    private String url;
    private String username;
    private String password;
}
