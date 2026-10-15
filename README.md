# Bank Web Application

A banking web application built with **Java, Servlets, JSP, JDBC and Oracle Database**, following the **MVC** pattern.

## Features
- **Authentication**: register, login and logout (passwords hashed with BCrypt, protected pages through a filter)
- **Account management**: open Savings / Current accounts, view account details, close accounts with zero balance
- **Deposits & withdrawals** with balance validation
- **Transfers** between accounts using database transactions (commit / rollback) and row locking
- **Transaction history** for every account

## Tech Stack
| Layer | Technology |
| --- | --- |
| View | JSP, JSTL, CSS |
| Controller | Jakarta Servlets, Filter |
| Service | Business logic and validation |
| Data access | JDBC, DAO pattern |
| Database | Oracle Database |
| Build / Server | Maven, Apache Tomcat 10.1 |

## Project Structure
```
src/main/java/com/mahmoud/bank
├── controller   Servlets (handle requests and choose the view)
├── service      Business logic (deposit, withdraw, transfer, auth)
├── dao          SQL queries with JDBC
├── model        User, Account, Transaction and enums
├── filter       AuthFilter
└── util         DBConnection, RequestUtil
src/main/webapp/WEB-INF/views   JSP pages
sql/schema.sql                  Database tables
```

## How to Run
1. Create a database user in Oracle (as `SYSTEM`, inside your pluggable database e.g. `XEPDB1`):
   ```sql
   CREATE USER bank_user IDENTIFIED BY bank_pass;
   GRANT CONNECT, RESOURCE TO bank_user;
   ALTER USER bank_user QUOTA UNLIMITED ON USERS;
   ```
2. Connect as `bank_user` and run `sql/schema.sql`.
3. Update `src/main/resources/db.properties` if your connection details are different.
4. Build the project:
   ```bash
   mvn clean package
   ```
5. Deploy `target/bank.war` to Tomcat 10.1 and open `http://localhost:8080/bank`.

## Database Design
- `users` → one user can have many `accounts`
- `accounts` → one account can have many `transactions`
- `CHECK (balance >= 0)` makes sure the balance never goes negative even if the code has a bug
