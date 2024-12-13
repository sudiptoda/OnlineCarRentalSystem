import java.io.*;
import java.util.*;

class Car {
    private final String carId;
    private final String brand;
    private final String model;
    private double basePricePerDay;
    private final String carType;
    private boolean isAvailable;

    public Car(String carId, String brand, String model, double basePricePerDay, String carType) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.carType = carType;
        this.isAvailable = true;
    }

    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double getBasePricePerDay() {
        return basePricePerDay;
    }

    public void setBasePricePerDay(double basePricePerDay) {
        this.basePricePerDay = basePricePerDay;
    }

    public String getCarType() {
        return carType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent() {
        isAvailable = false;
    }

    public void returnCar() {
        isAvailable = true;
    }

    public double calculatePrice(int rentalDays) {
        return basePricePerDay * rentalDays;
    }

    @Override
    public String toString() {
        return carId + " - " + brand + " " + model + " ($" + basePricePerDay + "/day)";
    }
}

class Customer {
    private final String customerId;
    private final String name;
    private final String contactInfo; // Email or Phone
    private final String password;
    private final List<Rental> rentalHistory;

    public Customer(String customerId, String name, String contactInfo, String password) {
        this.customerId = customerId;
        this.name = name;
        this.contactInfo = contactInfo;
        this.password = password;
        this.rentalHistory = new ArrayList<>();
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public List<Rental> getRentalHistory() {
        return rentalHistory;
    }

    public String getPassword() {
        return password;
    }

    public void addRentalToHistory(Rental rental) {
        rentalHistory.add(rental);
    }
}

class Rental {
    private final Car car;
    private final Customer customer;
    private final int rentalDays;
    private final double totalPrice;

    public Rental(Car car, Customer customer, int rentalDays, double totalPrice) {
        this.car = car;
        this.customer = customer;
        this.rentalDays = rentalDays;
        this.totalPrice = totalPrice;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}

class CarRentalSystem {
    private final List<Car> cars;
    private final List<Customer> customers;
    private final List<Rental> rentals;
    private Customer loggedInCustomer;
    private String adminPassword = "admin123"; // Default Admin password

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
        loadCarsFromFile();
        loadCustomersFromFile();
        loadRentalsFromFile();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void loadCarsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("cars.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] carData = line.split(",");
                String carId = carData[0];
                String brand = carData[1];
                String model = carData[2];
                double basePricePerDay = Double.parseDouble(carData[3]);
                String carType = carData[4];
                Car car = new Car(carId, brand, model, basePricePerDay, carType);
                boolean carExists = cars.stream().anyMatch(c -> c.getCarId().equals(carId));
                if (!carExists) {
                    cars.add(car);
                }
            }
        } catch (IOException e) {
            // Ignore error and continue with an empty cars list
        }
    }

    public void saveCarsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("cars.txt"))) {
            for (Car car : cars) {
                writer.write(car.getCarId() + "," + car.getBrand() + "," + car.getModel() + "," + car.getBasePricePerDay() + "," + car.getCarType());
                writer.newLine();
            }
        } catch (IOException e) {
            // Ignore error and continue
        }
    }

    public void loadCustomersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("customers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] customerData = line.split(",");
                if (customerData.length == 4) { // Ensure valid record
                    String customerId = customerData[0];
                    String name = customerData[1];
                    String contactInfo = customerData[2];
                    String password = customerData[3];
                    Customer customer = new Customer(customerId, name, contactInfo, password);
                    customers.add(customer);
                } else {
                    System.out.println("Skipping invalid customer record: " + line);
                }
            }
        } catch (IOException e) {
            // Ignore error and continue with an empty customers list
        }
    }

    public void saveCustomersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt"))) {
            for (Customer customer : customers) {
                writer.write(customer.getCustomerId() + "," + customer.getName() + "," + customer.getContactInfo() + "," + customer.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            // Ignore error and continue
        }
    }

    public void loadRentalsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("rentals.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rentalData = line.split(",");
                String carId = rentalData[0];
                String customerId = rentalData[1];
                int rentalDays = Integer.parseInt(rentalData[2]);
                double totalPrice = Double.parseDouble(rentalData[3]);

                Car rentedCar = null;
                Customer rentingCustomer = null;

                // Find car and customer by ID
                for (Car car : cars) {
                    if (car.getCarId().equals(carId)) {
                        rentedCar = car;
                        break;
                    }
                }

                for (Customer customer : customers) {
                    if (customer.getCustomerId().equals(customerId)) {
                        rentingCustomer = customer;
                        break;
                    }
                }

                if (rentedCar != null && rentingCustomer != null) {
                    Rental rental = new Rental(rentedCar, rentingCustomer, rentalDays, totalPrice);
                    rentals.add(rental);
                    rentingCustomer.addRentalToHistory(rental);
                }
            }
        } catch (IOException e) {
            // Ignore error and continue with an empty rentals list
        }
    }

    public void saveRentalsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("rentals.txt"))) {
            for (Rental rental : rentals) {
                writer.write(rental.getCar().getCarId() + "," + rental.getCustomer().getCustomerId() + "," + rental.getRentalDays() + "," + rental.getTotalPrice());
                writer.newLine();
            }
        } catch (IOException e) {
            // Ignore error and continue
        }
    }

    public void rentCar(Car car, Customer customer, int rentalDays) {
        if (car.isAvailable()) {
            car.rent();
            double totalPrice = car.calculatePrice(rentalDays);
            Rental rental = new Rental(car, customer, rentalDays, totalPrice);
            rentals.add(rental);
            customer.addRentalToHistory(rental);

            System.out.printf("Total Price: $%.2f%n", totalPrice);
        } else {
            System.out.println("Car is not available.");
        }
    }

    public void returnCar(Car car) {
        car.returnCar();
        System.out.println("Car returned successfully.");
    }

    public void showAvailableCars() {
        System.out.println("Available Cars:");
        for (Car car : cars) {
            if (car.isAvailable()) {
                System.out.println(car);
            }
        }
    }

    public void login(String contactInfo, String password) {
        for (Customer customer : customers) {
            if (customer.getContactInfo().equals(contactInfo) && customer.getPassword().equals(password)) {
                loggedInCustomer = customer;
                System.out.println("Login successful!");
                return;
            }
        }
        System.out.println("Invalid login credentials.");
    }

    public void logout() {
        loggedInCustomer = null;
        System.out.println("Logged out successfully.");
    }

    public void adminMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine();
        if (adminPassword.equals(password)) {
            while (true) {
                System.out.println("===== Admin Menu =====");
                System.out.println("1. Add a new Car");
                System.out.println("2. Update Car Information");
                System.out.println("3. See Rental History");
                System.out.println("4. Exit Admin");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        System.out.print("Enter Car ID: ");
                        String carId = scanner.nextLine();
                        System.out.print("Enter Brand: ");
                        String brand = scanner.nextLine();
                        System.out.print("Enter Model: ");
                        String model = scanner.nextLine();
                        System.out.print("Enter Price per day: ");
                        double price = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter Car Type: ");
                        String carType = scanner.nextLine();
                        Car newCar = new Car(carId, brand, model, price, carType);
                        addCar(newCar);
                        saveCarsToFile();
                        System.out.println("Car added successfully.");
                        break;
                    case 2:
                        System.out.print("Enter Car ID to update: ");
                        carId = scanner.nextLine();
                        Car carToUpdate = null;
                        for (Car car : cars) {
                            if (car.getCarId().equals(carId)) {
                                carToUpdate = car;
                                break;
                            }
                        }
                        if (carToUpdate != null) {
                            System.out.print("Enter new price per day: ");
                            double newPrice = scanner.nextDouble();
                            carToUpdate.setBasePricePerDay(newPrice);
                            System.out.println("Car price updated successfully!");
                            saveCarsToFile();
                        } else {
                            System.out.println("Car not found.");
                        }
                        break;
                    case 3:
                        System.out.println("Rental History:");
                        for (Rental rental : rentals) {
                            System.out.println("Car: " + rental.getCar().getBrand() + " " + rental.getCar().getModel());
                            System.out.println("Customer: " + rental.getCustomer().getName());
                            System.out.println("Rental Days: " + rental.getRentalDays());
                            System.out.println("Total Price: $" + rental.getTotalPrice());
                            System.out.println("---");
                        }
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        } else {
            System.out.println("Incorrect Admin password.");
        }
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("===== Online Car Rental System =====");
            if (loggedInCustomer == null) {
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Admin Login");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        System.out.print("Enter email or phone number: ");
                        String contactInfo = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();
                        login(contactInfo, password);
                        break;
                    case 2:
                        System.out.print("Enter your name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter your email or phone number: ");
                        contactInfo = scanner.nextLine();
                        System.out.print("Enter password: ");
                        password = scanner.nextLine();
                        registerCustomer(name, contactInfo, password);
                        break;
                    case 3:
                        adminMenu();
                        break;
                    case 4:
                        System.out.println("Goodbye!");
                        saveCarsToFile();
                        saveCustomersToFile();
                        saveRentalsToFile();
                        return;
                    default:
                        System.out.println("Invalid choice.");
                        break;
                }
            } else {
                System.out.println("Welcome, " + loggedInCustomer.getName());
                System.out.println("1. Car Rent Cost Calculation");
                System.out.println("2. Return a Car");
                System.out.println("3. View Rental History");
                System.out.println("4. Make a Payment");
                System.out.println("5. Logout");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        showAvailableCars();
                        System.out.print("Enter Car ID to rent: ");
                        String carId = scanner.nextLine();
                        System.out.print("Enter rental days: ");
                        int days = scanner.nextInt();
                        scanner.nextLine();

                        Car selectedCar = null;
                        for (Car car : cars) {
                            if (car.getCarId().equals(carId) && car.isAvailable()) {
                                selectedCar = car;
                                break;
                            }
                        }

                        if (selectedCar != null) {
                            rentCar(selectedCar, loggedInCustomer, days);
                        } else {
                            System.out.println("Invalid car selection or car not available.");
                        }
                        break;

                    case 2:
                        System.out.print("Enter Car ID to return: ");
                        carId = scanner.nextLine();
                        Car carToReturn = null;
                        for (Car car : cars) {
                            if (car.getCarId().equals(carId) && !car.isAvailable()) {
                                carToReturn = car;
                                break;
                            }
                        }

                        if (carToReturn != null) {
                            returnCar(carToReturn);
                        } else {
                            System.out.println("Invalid car ID or car is not rented.");
                        }
                        break;

                    case 3:
                        System.out.println("Rental History:");
                        for (Rental rental : loggedInCustomer.getRentalHistory()) {
                            System.out.println("Car: " + rental.getCar().getBrand() + " " + rental.getCar().getModel());
                            System.out.println("Rental Days: " + rental.getRentalDays());
                            System.out.println("Total Price: $" + rental.getTotalPrice());
                        }
                        break;

                    case 4:
                        System.out.println("===== Make a Payment =====");
                        System.out.print("Enter Card Number (16 digits): ");
                        String cardNumber = scanner.nextLine();
                        System.out.print("Enter Card Password (4 digits): ");
                        String cardPassword = scanner.nextLine();
                        System.out.print("Enter Amount to Pay: $");
                        double amount = scanner.nextDouble();
                        scanner.nextLine();  // Consume the newline

                        System.out.println("\n===== Payment Confirmation =====");
                        System.out.println("Card Number: **** **** **** " + cardNumber.substring(cardNumber.length() - 4));
                        System.out.println("Card Password: ****");
                        System.out.println("Amount: $" + amount);
                        System.out.print("Confirm payment (yes/no): ");
                        String confirmation = scanner.nextLine();

                        if (confirmation.equalsIgnoreCase("yes")) {
                            System.out.println("Payment of $" + amount + " was successful!");
                        } else {
                            System.out.println("Payment canceled.");
                        }
                        break;

                    case 5:
                        logout();
                        break;

                    case 6:
                        System.out.println("Goodbye!");
                        saveCarsToFile();
                        saveCustomersToFile();
                        saveRentalsToFile();
                        return;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }

    public void registerCustomer(String name, String contactInfo, String password) {
        String customerId = "CUS" + (customers.size() + 1);
        Customer newCustomer = new Customer(customerId, name, contactInfo, password);
        customers.add(newCustomer);
        saveCustomersToFile();
        System.out.println("Customer registered successfully!");
    }
}

public class Main {
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem();
        rentalSystem.menu();
    }
}
