package org.sfs.server.util;

import java.io.Closeable;
import java.util.Properties;

public class Config {
    private volatile static Config config = null;
    private Properties properties;
    private Config(){}
    static {
        if(config ==  null){
            synchronized (Config.class){
                if(config == null){
                    config = new Config();
                    config.properties = new Properties();
                    try{
                        config.properties.load(Config.class.getResourceAsStream("config.properties"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static Config getInstance(){
        return config;
    }

    public String getConfig(String name){
        return properties.getProperty(name);
    }
}
