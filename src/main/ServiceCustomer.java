package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.lang.Math.round;

public class ServiceCustomer {

    private static final String RECEIPT_PATH = "Receipts\\";

    public void printCheque(List<Basket> basketList) {
        double totalPrice = 0;
        double discountPrice = 0;

        System.out.println("************************************");
        System.out.println("*****      Receipt       **********");
        int i = 0;
        for (Basket b : basketList) {
            double fullPrice = b.getProduct().getPrice() * b.getAmount();
            if (b.getProduct().getTypeOfSale().equalsIgnoreCase("A")) {
                discountPrice = b.getProduct().getPriceWithDiscount() * b.getAmount();
            }
            if (b.getProduct().getTypeOfSale().equalsIgnoreCase("B")) {
                double amount = b.getAmount();
                double pricePerPiece = b.getProduct().getPrice();
                discountPrice = ((int)(amount /3) * 2 +
                        round((amount /3 - (int) (amount /3))*3)) * pricePerPiece;
            }
            if (b.getProduct().getTypeOfSale().equalsIgnoreCase("C")) {
                if (fullPrice >= 500) {
                    discountPrice = fullPrice * 0.9;
                }
            }
            if (!b.getProduct().getTypeOfSale().equalsIgnoreCase("A") &&
                    !b.getProduct().getTypeOfSale().equalsIgnoreCase("B") &&
                    !b.getProduct().getTypeOfSale().equalsIgnoreCase("C")) {
                discountPrice = fullPrice;
            }
            totalPrice = totalPrice + discountPrice;
            System.out.println("Product " + i++ + ": " + b.getProduct().getName());
            System.out.println("Amount " + i++ + ": " + b.getAmount() + " " + b.getProduct().getTypeOfPrice());
            System.out.println("Full price " + i++ + ": " + fullPrice + " SEK");
            System.out.println("Price with discount " + i++ + ": " + discountPrice + " SEK");
        }
        System.out.println("Total price: " + totalPrice);

    }

    public void printProductPrice(Product p) {
        System.out.println("************************************");
        System.out.println("Product: " + p.getName());
        System.out.println("Price: " + p.getPriceWithDiscount() + " per/" + p.getTypeOfPrice());
        if (p.getTypeOfSale().equalsIgnoreCase("A")) {
            System.out.println("Product has discount has discount " + p.getSale() + " % of price");
        }
        if (p.getTypeOfSale().equalsIgnoreCase("B")) {
            System.out.println("Product has discount has discount \"buy three, pay for two\"");
        }
        if (p.getTypeOfSale().equalsIgnoreCase("C")) {
            System.out.println("Product has discount has discount \"buy for SEK 500, get a 10% discount\"");
        }
        System.out.println("************************************");
    }

    public void createReceiptFile() {

            try (FileWriter csvWriter = new FileWriter(RECEIPT_PATH + getReceiptName())) {
                csvWriter.append("Date, Time, Amount, Purchase items, Sum paid");
                csvWriter.append("\n");
        //        csvWriter.append("Date and Time: ").append(timestamp).append("\n");
                System.out.println("Receipt file " + RECEIPT_PATH + " created successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
    public static String getReceiptName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String dateNow = dateFormat.format(new Date());
        String fileName = "Receipt_" + dateNow + ".csv";
        return fileName;
    }
}