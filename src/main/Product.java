package main;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Product {

    private String name;
    private double price;
    private List<String> types;
    private int sale = 0;
    private String typeOfSale;

    private String typeOfPrice;

    public Product() {}

    public Product (String name, double price, List<String> types) {
        this.name = name;
        this.price = price;
        this.types = types;
    }

    public Product (String name, double price, List<String> types, int sale) {
        this.name = name;
        this.price = price;
        this.types = types;
        this.sale = sale;
    }

    public Product(String name, double price, List<String> types, int sale, String typeOfSale) {
        this.name = name;
        this.price = price;
        this.types = types;
        this.sale = sale;
        this.typeOfSale = typeOfSale;
    }

    public Product(String name, double price, List<String> types, int sale, String typeOfSale, String typeOfPrice) {
        this.name = name;
        this.price = price;
        this.types = types;
        this.sale = sale;
        this.typeOfSale = typeOfSale;
        this.typeOfPrice = typeOfPrice;
    }
    public String getName() {
        return name;
    }

    public String getTypeOfPrice() {
        return typeOfPrice;
    }

    public void setTypeOfPrice(String typeOfPrice) {
        this.typeOfPrice = typeOfPrice;
    }

    public String getTypeOfSale() {
        return typeOfSale;
    }

    public void setTypeOfSale(String typeOfSale) {
        this.typeOfSale = typeOfSale;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPriceWithDiscount() {
        BigDecimal bd = new BigDecimal(price - (price * sale/100));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);

        return bd.doubleValue();
    }

    public double getPrice() { return price; }
    public void setPrice(double price) {
        this.price = price;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public List<String> getTypes() {
        return types;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Double.compare(getPrice(), product.getPrice()) == 0 && getSale() == product.getSale() && Objects.equals(getName(), product.getName()) && Objects.equals(getTypes(), product.getTypes()) && Objects.equals(getTypeOfSale(), product.getTypeOfSale()) && Objects.equals(getTypeOfPrice(), product.getTypeOfPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPrice(), getTypes(), getSale(), getTypeOfSale(), getTypeOfPrice());
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "main.Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", types=" + types +
                ", sale=" + sale +
                ", typeOfSale='" + typeOfSale + '\'' +
                ", typeOfPrice='" + typeOfPrice + '\'' +
                '}';
    }
}
