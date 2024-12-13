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
