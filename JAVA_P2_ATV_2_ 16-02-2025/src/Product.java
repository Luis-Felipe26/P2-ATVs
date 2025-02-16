public class Product{
    protected String brand;
    protected double price;

    public Product(String brand, double price){
        this.brand = brand;
        this.price = price;
    }

    public String getBrand(){
        return brand;
    }

    public double getPrice(){
        return price;
    }
}