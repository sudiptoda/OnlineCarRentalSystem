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
