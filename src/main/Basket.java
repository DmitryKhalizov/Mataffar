package main;

public class Basket {

    private double amount;
    private Product product;

    public Basket(double amount, Product product) {
        this.amount = amount;
        this.product = product;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "main.Basket{" +
                "amount=" + amount +
                ", product=" + product +
                '}';
    }
}
