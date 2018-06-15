package org.vvchebotar.crud.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DBUtils {
    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void initialize() {
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();

            /**
             * Person_INFO
             */
            statement.execute("DROP TABLE IF EXISTS book");
            statement.executeUpdate("CREATE TABLE book(" + "ID SERIAL NOT NULL PRIMARY KEY, "
                    + "title_clmn VARCHAR(40) NOT NULL, " + "description_clmn VARCHAR(200), "
                    + "author_clmn VARCHAR(40) NOT NULL, isbn_clmn VARCHAR(13) NOT NULL, "
                    + "print_year_clmn INT, read_already_clmn BOOLEAN NOT NULL DEFAULT FALSE" + ")");

            for (int i = 1; i < 36; i++) {
                statement.executeUpdate(
                        "INSERT INTO book" + "(title_clmn, description_clmn, author_clmn, isbn_clmn, print_year_clmn)" + "VALUES "
                                + "('book_" + i + "', 'description_" + i + "', 'author_" + i + "', 'isbn_" + i + "', 19" + (i + 10) + " )");
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}