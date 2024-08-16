# CodeAlpha-Java-Programming-Task-3-Hotel-Reservation-System-
Instructions for Setting Up the Hotel Reservation System
1. Creating the Database

Before running the HotelReservationSystem.java program, it is essential to create the required database and its associated tables. This can be done by executing the SQL script provided in HotelReservationSystem.sql.

Steps:

Open MySQL Workbench:
Launch MySQL Workbench on your computer.
Connect to Your MySQL Server:
Log in to your MySQL server using your credentials.
Open the SQL Script:
From the top menu, select File > Open SQL Script....
Locate and open the HotelReservationSystem.sql file.
Execute the SQL Script:
Review the SQL statements in the file.
Run the script line by line or as a whole by clicking on the Execute button (lightning bolt icon) in the toolbar.
Ensure that all the statements are executed successfully and that the database and tables are created as expected.
2. Connecting the Java Application to the Database

Once the database is set up, you need to connect the HotelReservationSystem.java program to the MySQL database. This requires configuring the database URL, username, and password in your Java code.

Steps:

Locate the Database Connection Settings in Java:
Open HotelReservationSystem.java in your Java development environment (e.g., Visual Studio Code or Eclipse).
Configure the Database Connection:
Update the database connection URL, username, and password in the Java code. Typically, this will be done in the section of the code where the database connection is established, often using DriverManager.getConnection().
Example code snippet:

java
Copy code
String url = "jdbc:mysql://localhost:3306/hotel_reservation_system";
String username = "your_username";
String password = "your_password";

Connection connection = DriverManager.getConnection(url, username, password);
Replace your_username and your_password with your actual MySQL username and password.
Ensure that the URL matches the database you created with the HotelReservationSystem.sql script.
Compile and Run the Java Program:
After configuring the connection, compile and run the HotelReservationSystem.java program.
The program should now be able to interact with the MySQL database you created.
