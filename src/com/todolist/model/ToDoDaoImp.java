package com.todolist.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

import com.todolist.config.JDBCConfig;

public class ToDoDaoImp   extends HttpServlet implements ToDoDao {

    private static final String INSERT_TODOS_SQL = "INSERT INTO todos" +
        "  (title, username, description, target_date,  is_done) VALUES " + " (?, ?, ?, ?, ?);";

    private static final String SELECT_TODO_BY_ID = "select id,title,username,description,target_date,is_done from todos where id =?";
    private static final String SELECT_ALL_TODOS = "select * from todos where username=?";
    private static final String DELETE_TODO_BY_ID = "delete from todos where id = ?;";
    private static final String UPDATE_TODO = "update todos set title = ?, username= ?, description =?, target_date =?, is_done = ? where id = ?;";

    public ToDoDaoImp() {}

    @Override
    public void insertTodo(ToDo todo) throws SQLException {
        System.out.println(INSERT_TODOS_SQL);
        // try-with-resource statement will auto close the connection.
        try (Connection connection = JDBCConfig.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TODOS_SQL)) {
            preparedStatement.setString(1, todo.getTitle());
            preparedStatement.setString(2, todo.getUsername());
            preparedStatement.setString(3, todo.getDescription());
            preparedStatement.setDate(4, JDBCConfig.getSQLDate(todo.getTargetDate()));
            preparedStatement.setBoolean(5, todo.getStatus());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            JDBCConfig.printSQLException(exception);
        }
    }

    @Override
    public ToDo selectTodo(long todoId) {
        ToDo todo = null;
        // Step 1: Establishing a Connection
        try (Connection connection = JDBCConfig.getConnection();
            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TODO_BY_ID);) {
            preparedStatement.setLong(1, todoId);
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                long id = rs.getLong("id");
                String title = rs.getString("title");
                String username = rs.getString("username");
                String description = rs.getString("description");
                LocalDate targetDate = rs.getDate("target_date").toLocalDate();
                boolean isDone = rs.getBoolean("is_done");
                todo = new ToDo(id, title, username, description, targetDate, isDone);
            }
        } catch (SQLException exception) {
            JDBCConfig.printSQLException(exception);
        }
        return todo;
    }

    @Override
    public List < ToDo > selectAllTodos(String user) {

        // using try-with-resources to avoid closing resources (boiler plate code)
        List < ToDo > todos = new ArrayList < > ();

        // Step 1: Establishing a Connection
        try (Connection connection = JDBCConfig.getConnection();

            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TODOS);) {
        
        	preparedStatement.setString(1, user);
        	
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                long id = rs.getLong("id");
                String title = rs.getString("title");
                String username = rs.getString("username");
                String description = rs.getString("description");
                LocalDate targetDate = rs.getDate("target_date").toLocalDate();
                boolean isDone = rs.getBoolean("is_done");
                todos.add(new ToDo(id, title, username, description, targetDate, isDone));
            }
        } catch (SQLException exception) {
            JDBCConfig.printSQLException(exception);
        }
        return todos;
    }

    @Override
    public boolean deleteTodo(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = JDBCConfig.getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_TODO_BY_ID);) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    @Override
    public boolean updateTodo(ToDo todo) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = JDBCConfig.getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_TODO);) {
            statement.setString(1, todo.getTitle());
            statement.setString(2, todo.getUsername());
            statement.setString(3, todo.getDescription());
            statement.setDate(4, JDBCConfig.getSQLDate(todo.getTargetDate()));
            statement.setBoolean(5, todo.getStatus());
            statement.setLong(6, todo.getId());
            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }
}