package com.github.hackathon.advancedsecurityjava.Controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.github.hackathon.advancedsecurityjava.Application;
import com.github.hackathon.advancedsecurityjava.Models.Book;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

  private static Connection connection;

  @GetMapping("/")
  @ResponseBody
  public List<Book> getBooks() {
    List<Book> books = new ArrayList<Book>();

    Statement statement = null;

    try {
      // Init connection to DB
      connection = DriverManager.getConnection(Application.connectionString);

      statement = connection.createStatement();
      String query = "SELECT * FROM Books";

      ResultSet results = statement.executeQuery(query);

      while (results.next()) {
        Book book = new Book(results.getString("name"), results.getString("author"), (results.getInt("read") == 1));

        books.add(book);
      }

    } catch (SQLException error) {
      error.printStackTrace();
    } finally {
      try {
        if (connection != null) {
          connection.close();
        }
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException error) {
        error.printStackTrace();
      }
    }
    return books;
  }

  @GetMapping("/name")
  public Book getBookByName(@RequestParam(name = "name", required = false) String bookname) {
    Book book = null;

    Statement statement = null;

    try {
      // Init connection to DB
      connection = DriverManager.getConnection(Application.connectionString);

      statement = connection.createStatement();
      String query = "SELECT * FROM Books WHERE name = '" + bookname + "'";

      ResultSet results = statement.executeQuery(query);

      book = new Book(results.getString("name"), results.getString("author"), (results.getInt("read") == 1));
    } catch (SQLException error) {
      error.printStackTrace();
    } finally {
      try {
        if (connection != null) {
          connection.close();
        }
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException error) {
        error.printStackTrace();
      }
    }
    return book;
  }
}