package org.getchunky.chunkyapi.persistence;

import org.bukkit.configuration.file.YamlConfiguration;
import org.getchunky.chunky.Chunky;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class EBeansUtil {

    private final Properties connectionProperties;
    private final YamlConfiguration configuration;

    private EBeansUtil() {
        this.configuration = YamlConfiguration.loadConfiguration(new File("bukkit.yml"));
        connectionProperties = new Properties();
        connectionProperties.put("user", configuration.getString("database.username"));
        connectionProperties.put("password", configuration.getString("database.password"));
    }

    public boolean isSqlLite() {
        return configuration.getString("database.driver").contains("sqlite");
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(replaceDatabaseString(
                configuration.getString("database.url")),
                connectionProperties);
    }

    private String replaceDatabaseString(String input) {
        input = input.replaceAll("\\{DIR\\}", Chunky.getPlugin().getDataFolder().getPath().replaceAll("\\\\", "/") + "/");
        input = input.replaceAll("\\{NAME\\}", Chunky.getPlugin().getDescription().getName().replaceAll("[^\\w_-]", ""));
        return input;
    }
}
