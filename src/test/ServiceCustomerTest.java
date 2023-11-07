package test;

import main.Product;
import main.ServiceCustomer;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ServiceCustomerTest {

    ServiceCustomer serviceCustomer = new ServiceCustomer();



    @Test
    void testCreateReceiptFile() {
        serviceCustomer.createReceiptFile();
    }

    @Test
    void testPrintProductPrice() {
        Product p = new Product("Shit", 100, new ArrayList<>(), 10, "A");
        serviceCustomer.printProductPrice(p);
    }
    @Test
    public void testGetReceiptName() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String expectedDateNow = dateFormat.format(new Date());


        String receiptName = serviceCustomer.getReceiptName();


        String expectedFileName = "Receipt_" + expectedDateNow + ".csv";

        // Assert that the generated receipt name matches the expected name.
        assertEquals(expectedFileName, receiptName);
    }


}