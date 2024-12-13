# Car Rental System

A comprehensive **Car Rental System** built using **Java** and **MySQL**, designed to provide an easy-to-use platform for users to rent cars, view rental history, and manage payments. The system also offers an admin panel for managing car inventory, customer records, and rental history.

## Features

### Customer Features:
- **Login and Registration:** Allows users to register and log in to the system.
- **Car Rental:** Customers can browse available cars and rent them for a specified number of days.
- **Rental History:** View past rental records, including details about each car rented and the total price paid.
- **Car Return:** Users can return rented cars and get updated rental status.
- **Payment Processing:** Process payments using mock payment functionality.

### Admin Features:
- **Login (Admin only):** Admins can log in using an admin password.
- **Manage Car Inventory:** Admins can add new cars, update car details, or remove cars from the system.
- **Rental History Management:** Admins can view all rental records, including customer information, car details, rental days, and total costs.
- **System Data Backup:** Admins can save and load car, customer, and rental data to/from files.

### System Features:
- **File Persistence:** All car, customer, and rental data is stored in files (e.g., `cars.txt`, `customers.txt`, `rentals.txt`) for persistence.
- **Search and Filter:** Search and filter functionality to make it easier to find available cars.
- **Error Handling:** Basic error handling for invalid inputs and file operations.

## Technologies Used
- **Java:** Core programming language for developing the system.
- **MySQL:** Used for data storage (optional, in case you wish to expand to a real database).
- **Java Swing (JFrame):** For the graphical user interface (GUI).
- **File I/O (BufferedReader, BufferedWriter):** To load and save data from/to text files.

## Installation

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- MySQL (optional, for future database integration)


