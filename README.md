# Expense Tracker

A Java-based desktop application for managing personal income and expenses using a graphical user interface and SQLite database.

## Features

* User registration and login
* Add income and expense transactions
* View transaction history
* Edit transactions
* Delete transactions
* Calculate total credit and debit
* Display current balance
* Java Swing-based graphical user interface
* SQLite database integration

## Technologies Used

* **Java**
* **Java Swing** — Graphical User Interface
* **SQLite** — Database
* **JDBC** — Database connectivity

## Project Structure

```text
Expense-Tracker/
├── .gitignore
├── DBConnection.java
├── ExpenseTrackerUI.java
├── Main.java
├── TransactionDAO.java
└── UserDAO.java
```

## How It Works

The application starts through `Main.java`, which launches the Swing-based user interface.

`DBConnection.java` handles the SQLite database connection and initializes the required tables.

`UserDAO.java` manages user registration and login operations.

`TransactionDAO.java` handles adding, updating, deleting, retrieving transactions, and calculating the user's balance.

## Database

The application uses SQLite and creates the database locally when the application runs.

The database file is intentionally excluded from GitHub using `.gitignore`.

## How to Run

1. Clone the repository.
2. Open the project in a Java IDE such as IntelliJ IDEA or Eclipse.
3. Make sure the SQLite JDBC driver is available in the project.
4. Run `Main.java`.
5. Register a user and start managing transactions.

## Future Improvements

* Password hashing for improved security
* Complete password reset functionality
* Expense charts and analytics
* Monthly expense summaries
* Export transactions to CSV
* Improved input validation
