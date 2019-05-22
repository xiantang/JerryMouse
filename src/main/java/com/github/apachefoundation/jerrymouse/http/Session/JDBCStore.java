package com.github.apachefoundation.jerrymouse.http.Session;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import java.time.Instant;

public class JDBCStore implements Store {
    private String connectionName = null;
    private String connectionPassword = null;
    private String connectionURL = null;
    private Connection dbConnection = null;
    private Driver driver = null;
    private String driverName = null;


    private String sessionTable = "tomcat$sessions";
    private String sessionIdCol = "id";
    private String sessionDataCol = "data";
    private String sessionMaxInactiveCol = "maxinactive";
    private String sessionLastAccessedCol = "lastaccess";



    private PreparedStatement preparedSaveSql = null;
    private PreparedStatement preparedClearSql = null;
    private PreparedStatement preparedLoadSql = null;

    private String tableName;
    private SessionManager sessionManager;

    /**
     * 日后使用读取配置代替
     */
    public JDBCStore(String connectionName, String connectionPassword, String connectionURL,
                     Driver driver, String driverName, SessionManager sessionManager) {
        this.connectionName = connectionName;
        this.connectionPassword = connectionPassword;
        this.connectionURL = connectionURL;
        this.driver = driver;
        this.driverName = driverName;
        this.sessionManager = sessionManager;

        try {
            Class.forName(driverName);
            dbConnection = DriverManager.getConnection(connectionURL, connectionName, connectionPassword);
            Statement stmt = dbConnection.createStatement();
            String creatSql = "CREATE TABLE " + tableName + " ("
                    + sessionIdCol + " INTEGER not NULL, "
                    + sessionDataCol + " BLOB, "
                    + sessionLastAccessedCol + " BIGINT, "
                    + sessionMaxInactiveCol + " BIGINT,"
                    + " PRIMARY KEY (" + sessionIdCol + "))";

            stmt.execute(creatSql);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 保存 session
     */
    private void save0(HttpSession session) {
        try {
            if (preparedSaveSql == null) {
                String saveSql = "INSERT INTO " + tableName + " ("
                        + sessionIdCol + ", "
                        + sessionDataCol + ", "
                        + sessionMaxInactiveCol + ", "
                        + sessionLastAccessedCol
                        + ") VALUES (?, ?, ?, ?)";

                preparedSaveSql = dbConnection.prepareStatement(saveSql);
                preparedSaveSql.setString(1, session.getId());
                preparedSaveSql.setBytes(2, ((StandardSession)session).getAttributesByte());
                preparedSaveSql.setInt(3, session.getMaxInactiveInterval());
                preparedSaveSql.setLong(4, session.getLastAccessedTime());
                preparedSaveSql.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 清空持久化
     */
    private void clear() {
        try {
            if (preparedClearSql == null) {
                String clearSql = "DROP TABLE " + tableName;
                preparedClearSql.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void load() {
        Long nowTime = Instant.now().getEpochSecond();

        try {
            if (preparedLoadSql == null) {
                String loadSql = "SELECT " + sessionIdCol + ", "
                        + sessionDataCol + ", "
                        + sessionLastAccessedCol + ", "
                        + sessionMaxInactiveCol + ", "
                        + " FROM " + sessionTable
                        + "WHERE " + sessionLastAccessedCol + " + "
                        + sessionMaxInactiveCol + " > " + nowTime;

                ResultSet resultSet = preparedLoadSql.executeQuery();
                while (resultSet.next()) {
                    String id = resultSet.getString(sessionIdCol);
                    byte[] dataByte = resultSet.getBytes(sessionDataCol);
                    long lastAccessed = resultSet.getLong(sessionLastAccessedCol);
                    int maxInactiveInterval = resultSet.getInt(sessionMaxInactiveCol);
                    StandardSession standardSession = new StandardSession(sessionIdCol,
                            Instant.ofEpochMilli(lastAccessed),
                            maxInactiveInterval);
                    standardSession.setAttributesByte(dataByte);
                    sessionManager.putSession(standardSession);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        HttpSession[] httpSessions = sessionManager.findSessions();
        for (HttpSession httpSession: httpSessions) {
            save0(httpSession);
        }
    }
}
