import java.io.*;
import java.util.*;

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

    public void admin
