package test;

import main.Product;
import main.ServiceProduct;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServiceProductTest {

    ServiceProduct serviceProduct = new ServiceProduct();


    @Test
    void getLineInFile() {
        List<String> types = new ArrayList<>();
        types.add("vegetable");
        types.add("green");
        types.add("Spanish");
        Product p = new Product("Cabbage", 5, types, 50, "A", "kg" );
        String result = serviceProduct.getLineInFile(p);
        System.out.println(result);
        String expectedResult = "Cabbage,5.0,vegetable&green&Spanish,50,A,kg";
        assertEquals(result, expectedResult);
        System.out.println(expectedResult);
    }

    @Test
    void makeChangeForProduct() {
        List<String> types = new ArrayList<>();
        types.add("vegetable");
        types.add("green");
        types.add("Spanish");
        Product p = new Product("Cabbage", 5, types, 50, "A", "kg" );
        serviceProduct.makeChangeForProduct(p);

    }

    @Test
    void testMakeChangeForProduct() {
    String old = "Banana,20.03,fruit,0,B,piece";
    String newCampaign = "Banana,20.03,fruit,0,A,piece";
    boolean reply = serviceProduct.changeLineInFile(old, newCampaign);
    assertTrue(reply);
    }
}