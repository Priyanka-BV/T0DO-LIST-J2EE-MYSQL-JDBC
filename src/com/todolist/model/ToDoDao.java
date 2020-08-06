package com.todolist.model;

import java.sql.SQLException;
import java.util.List;

public interface ToDoDao {

	 void insertTodo(ToDo todo) throws SQLException;

	 ToDo selectTodo(long todoId);

	 List<ToDo> selectAllTodos(String user);

	 boolean deleteTodo(int id) throws SQLException;

	 boolean updateTodo(ToDo todo) throws SQLException;

	}