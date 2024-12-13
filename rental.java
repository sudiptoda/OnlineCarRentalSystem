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
