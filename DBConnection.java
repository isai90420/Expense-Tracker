package tracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String DB_URL = "jdbc:sqlite:expensetracker.db";

    // Connect to database
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // Initialize database and tables
    public static void initDB() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {

            // Users table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "email TEXT UNIQUE NOT NULL)");

            // Transactions table
            stmt.execute("CREATE TABLE IF NOT EXISTS transactions (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER NOT NULL," +
                    "type TEXT NOT NULL," +
                    "category TEXT NOT NULL," +
                    "title TEXT," +
                    "description TEXT," +
                    "amount REAL NOT NULL," +
                    "method TEXT," +
                    "date TEXT," +
                    "time TEXT," +
                    "FOREIGN KEY(user_id) REFERENCES users(id))");

            System.out.println("✅ Database initialized");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
