package tracker;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    // 🔹 Fetch all transactions for a user
    public static List<String[]> getTransactions(int userId) {
        List<String[]> txns = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE user_id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] row = new String[10];
                row[0] = String.valueOf(rs.getInt("id"));
                row[1] = rs.getString("type");
                row[2] = rs.getString("category");
                row[3] = rs.getString("title");
                row[4] = rs.getString("description");
                row[5] = String.valueOf(rs.getDouble("amount"));
                row[6] = rs.getString("method");
                row[7] = rs.getString("date");
                row[8] = rs.getString("time");
                row[9] = String.valueOf(rs.getInt("user_id"));
                txns.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return txns;
    }

    // 🔹 Add transaction
    public static boolean addTransaction(int userId, String type, String category, String title, String description,
                                         double amount, String method, String date, String time) {
        String sql = "INSERT INTO transactions(user_id,type,category,title,description,amount,method,date,time) " +
                     "VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, type);
            pstmt.setString(3, category);
            pstmt.setString(4, title);
            pstmt.setString(5, description);
            pstmt.setDouble(6, amount);
            pstmt.setString(7, method);
            pstmt.setString(8, date);
            pstmt.setString(9, time);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Update transaction
    public static boolean updateTransaction(int id, int userId, String type, String category, String title,
                                            String description, double amount, String method, String date, String time) {
        String sql = "UPDATE transactions SET type=?, category=?, title=?, description=?, amount=?, method=?, date=?, time=? " +
                     "WHERE id=? AND user_id=?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, type);
            pstmt.setString(2, category);
            pstmt.setString(3, title);
            pstmt.setString(4, description);
            pstmt.setDouble(5, amount);
            pstmt.setString(6, method);
            pstmt.setString(7, date);
            pstmt.setString(8, time);
            pstmt.setInt(9, id);
            pstmt.setInt(10, userId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Delete transaction
    public static boolean deleteTransaction(int id, int userId) {
        String sql = "DELETE FROM transactions WHERE id=? AND user_id=?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Get user balance
    public static double getBalance(int userId) {
        double credit = 0, debit = 0;
        String sql = "SELECT type, amount FROM transactions WHERE user_id=?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                double amt = rs.getDouble("amount");
                String type = rs.getString("type");
                if ("CREDIT".equalsIgnoreCase(type)) credit += amt;
                else debit += amt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return credit - debit;
    }
}
