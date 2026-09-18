# Library Management System

A Java console application that manages a library's book catalog, member
registrations and the borrow/return lifecycle — including overdue fine
calculation, thread-safe concurrent borrowing and persistent storage via
JDBC/SQLite.

Built as the "Build Your Own Project" evaluated submission for the
**Programming in Java** course, covering: Java OOP, Collections Framework,
Exception Handling, File I/O, Concurrency and JDBC.

## Overview

The system lets a librarian add books to a catalog and register members
(Students or Faculty, each with different borrowing limits and fine rates).
Members can borrow and return books; the system tracks due dates, calculates
overdue fines automatically and safely handles multiple members trying to
borrow the same book at the same time.

## Features

- **Book catalog** — add, search by title, track available vs. total copies
- **Member management** — `Student` and `Faculty` subclasses of an abstract
  `Member` class, each with its own borrowing limit and daily fine rate
- **Borrow / return engine** — enforces borrowing limits, prevents
  over-issuing a book, calculates overdue fines automatically
- **Custom exceptions** — `BookNotAvailableException`,
  `InvalidMemberException` for clear, checked error handling
- **File-based audit log** — every event is appended to `data/transactions.log`
- **JDBC persistence** — books, members, and transactions are saved to a
  local SQLite database (`data/library.db`)
- **Concurrency-safe** — a multithreaded demo shows several members racing
  to borrow the same limited-copy book without double-issuing

## Technologies / Tools Used

- Java 17+ (core language, OOP, Collections, Exceptions, Concurrency)
- JDBC with SQLite (`sqlite-jdbc` driver, no external DB server required)
- Git for version control

## Project Structure

```
LibraryManagementSystem/
├── src/
│   ├── Main.java                     # entry point / demo driver
│   ├── Library.java                  # core business logic
│   ├── Book.java
│   ├── Member.java                   # abstract base class
│   ├── Student.java
│   ├── Faculty.java
│   ├── Transaction.java
│   ├── BorrowTask.java               # Runnable for concurrency demo
│   ├── TransactionLogger.java        # file I/O
│   ├── LibraryDatabase.java          # JDBC layer
│   ├── BookNotAvailableException.java
│   └── InvalidMemberException.java
├── lib/                                # you create this — see step 2 below
│   └── sqlite-jdbc-3.45.1.0.jar
├── data/                              # created at runtime (log + db)
├── statement.md
└── README.md
```

## Steps to Install & Run

1. **Prerequisites**: JDK 17 or later installed (`java -version` to check).
2. Clone the repository, then download the SQLite JDBC driver into a `lib/` folder:
   ```
   git clone <your-repo-url>
   cd LibraryManagementSystem
   mkdir lib
   curl -L -o lib/sqlite-jdbc-3.45.1.0.jar https://github.com/xerial/sqlite-jdbc/releases/download/3.45.1.0/sqlite-jdbc-3.45.1.0.jar
   ```
3. Compile:
   ```
   javac -cp lib/sqlite-jdbc-3.45.1.0.jar -d out src/*.java
   ```
4. Run:
   ```
   java -cp "out:lib/sqlite-jdbc-3.45.1.0.jar" Main
   ```
   (On Windows, use `;` instead of `:` in the classpath.)
5. Output will show the catalog, a normal borrow/return cycle, a deliberately
   triggered exception, and a multithreaded borrowing race. Check
   `data/transactions.log` and `data/library.db` afterward for persisted
   records.

## Instructions for Testing

- Run the program multiple times to see the SQLite database persist state
  across runs (comment out `initSchema()`'s `IF NOT EXISTS` clause removal
  if you want a clean slate each run — otherwise, it accumulates history,
  which is realistic library behavior).
- To test the exception paths manually, try borrowing a book with an
  unregistered member ID or after copies are exhausted (see `Main.java`'s
  deliberate demonstration of `BookNotAvailableException`).
- To test concurrency, increase the number of `Thread` objects in `Main.java`
  beyond the number of available copies and confirm no book is issued twice.

## Screenshots

_Add screenshots of your console output and `library.db` contents (e.g. via
DB Browser for SQLite) ._
