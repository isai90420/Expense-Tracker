package tracker;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestDB {
    public static void main(String[] args) {
        // Initialize DB (creates tables if not exist)
        DBConnection.initDB();

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement()) {

            // Just print all tables in the database
            ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table'");
            System.out.println("📂 Tables in database:");
            while (rs.next()) {
                System.out.println(" - " + rs.getString("name"));
            }

            // Print structure of transactions table
            System.out.println("\n📌 Transactions table schema:");
            rs = stmt.executeQuery("PRAGMA table_info(transactions)");
            while (rs.next()) {
                System.out.println(rs.getString("name") + " (" + rs.getString("type") + ")");
            }

        } catch (Exception e) {
            System.out.println("DB Test error: " + e.getMessage());
        }
    }
}
