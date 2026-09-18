# Problem Statement

## Problem Statement

Manual and spreadsheet-based library tracking systems make it difficult to
know which books are available, who has borrowed what, and how much a member
owes in overdue fines. Errors like double-issuing the last copy of a book
are common when multiple requests are handled without proper safeguards.
This project implements a Java-based Library Management System that
automates the full lifecycle of book cataloging, member registration,
borrowing, returning, and fine calculation, while safely handling
simultaneous access from multiple members and persisting all records to a
database.

## Scope of the Project

The system covers:
- Adding and searching books in a catalog
- Registering two categories of members (Student, Faculty) with different
  borrowing rules
- Issuing and returning books with automatic due-date tracking
- Calculating overdue fines based on member type
- Preventing race conditions when multiple members attempt to borrow the
  same limited-copy book concurrently
- Logging every transaction to a file and persisting all data via JDBC to a
  SQLite database

Out of scope: a graphical user interface, user authentication/login, and
multi-branch library support — these are noted as future enhancements.

## Target Users

- **Librarians/administrators** who add books and register members
- **Students** who can borrow up to 3 books at a time (Rs. 5/day overdue fine)
- **Faculty** who can borrow up to 8 books at a time (Rs. 2/day overdue fine)

## High-Level Features

1. Book & Catalog Management (add, search, availability tracking)
2. Member Management (Student/Faculty with distinct rules — OOP inheritance)
3. Borrow/Return & Fine Engine (custom exceptions, thread-safe concurrency,
   file logging, JDBC persistence)
