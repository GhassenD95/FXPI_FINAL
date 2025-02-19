package services;

import tools.DbConnection;

import java.sql.Connection;

public abstract class BaseService {
    protected Connection con;


    public BaseService() {
        con = DbConnection.getInstance().getConn();
    }
}
