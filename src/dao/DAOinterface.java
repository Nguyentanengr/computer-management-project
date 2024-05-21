package dao;

import model.Account;
import model.Computer;

import java.util.ArrayList;

public interface DAOinterface<T>{

    int insert(T t);
    int update(T t);
    int delete(T t);
    ArrayList<T> selectAll();
    T selectById(String id);
}
