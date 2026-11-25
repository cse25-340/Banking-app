package bank.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {

    public static void createTables() {
        String customersTable = """
            CREATE TABLE IF NOT EXISTS customers (
                username VARCHAR(50) PRIMARY KEY,
                first_name VARCHAR(100) NOT NULL,
                surname VARCHAR(100) NOT NULL,
                address VARCHAR(255) NOT NULL,
                phone VARCHAR(30) NOT NULL,
                password VARCHAR(100) NOT NULL
            );
            """;

        // Columns match what AccountDAO reads and writes
        String accountsTable = """
            CREATE TABLE IF NOT EXISTS accounts (
                account_number VARCHAR(50) PRIMARY KEY,
                customer_id VARCHAR(50) NOT NULL,
                account_type VARCHAR(30) NOT NULL,
                balance DECIMAL(15, 2) NOT NULL,
                employer_info VARCHAR(400)
            );
            """;

        // Columns match what TransactionDAO reads and writes
        String transactionsTable = """
            CREATE TABLE IF NOT EXISTS transactions (
                id INT AUTO_INCREMENT PRIMARY KEY,
                account_number VARCHAR(50) NOT NULL,
                transaction_type VARCHAR(30) NOT NULL,
                amount DECIMAL(15, 2) NOT NULL,
                balance_after DECIMAL(15, 2) NOT NULL,
                description VARCHAR(255),
                created_at TIMESTAMP NOT NULL,
                FOREIGN KEY (account_number) REFERENCES accounts(account_number)
                    ON DELETE CASCADE
            );
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            // A database file made by the earlier version of this setup has the old
            // accounts/transactions layout. Replace those two tables with the new layout.
            if (oldLayout(stmt)) {
                stmt.execute("DROP TABLE IF EXISTS transactions");
                stmt.execute("DROP TABLE IF EXISTS accounts");
            }
            stmt.execute(customersTable);
            stmt.execute(accountsTable);
            stmt.execute(transactionsTable);
            System.out.println("Database tables ready!");
        } catch (SQLException e) {
            throw new RuntimeException("Error creating tables", e);
        }
    }

    /** @return true if an accounts table exists but does not have the customer_id column */
    private static boolean oldLayout(Statement stmt) throws SQLException {
        boolean tableExists;
        try (ResultSet rs = stmt.executeQuery(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'ACCOUNTS'")) {
            rs.next();
            tableExists = rs.getInt(1) > 0;
        }
        if (!tableExists) {
            return false;
        }
        try (ResultSet rs = stmt.executeQuery(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS "
                        + "WHERE TABLE_NAME = 'ACCOUNTS' AND COLUMN_NAME = 'CUSTOMER_ID'")) {
            rs.next();
            return rs.getInt(1) == 0;
        }
    }
}
