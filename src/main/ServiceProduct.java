package main;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.lang.Math.round;

public class ServiceProduct {

    private static final String FILE_PATH_PRODUCT = "Resources\\products.csv";

    public Product makeChangeForProduct(Product p) {
        Scanner input = new Scanner(System.in);
        System.out.println("Would you to change sales campaign for the product (Y/N) " + p.getName() + "?");
        initiateDiscout();
        System.out.println("The existing sales campaign is " + p.getTypeOfSale());
        String reply = input.nextLine();
        if (!reply.equalsIgnoreCase("N")) {
            System.out.println("What type of campaign would you like to set (A, B, C or nothing)");
            String type = input.nextLine();
            if (!type.equalsIgnoreCase("A") && !type.equalsIgnoreCase("B")
                    && !type.equalsIgnoreCase("C")) {
                p.setTypeOfSale("No");
            } else {
                p.setTypeOfSale(type);
            }
        }
        input.close();
        return p;
    }

    private final ServiceCustomer serviceCustomer = new ServiceCustomer();

    public void createProductFile() {
        File file = new File(FILE_PATH_PRODUCT);
        if (file.exists()) {
            System.out.println("File exists.");
        } else {
            System.out.println("File does not exist.");
            try (FileWriter csvWriter = new FileWriter(FILE_PATH_PRODUCT)) {
                csvWriter.append("Apple,15.90,fruit&green,15,A,kg");
                csvWriter.append("\n");
                csvWriter.append("Banana,20.03,fruit,0,B,piece");
                csvWriter.append("\n");
                csvWriter.append("Orange,25.0,fruit&citrus,25,C,kg");
                csvWriter.append("\n");
                csvWriter.append("Cabbage,5.0,vegetable&green&Spanish,50,A,kg");
                csvWriter.append("\n");
                csvWriter.append("Orange,50.4,fruit&citrus&red,0,B,piece");
                csvWriter.append("\n");

                System.out.println("CSV file " + FILE_PATH_PRODUCT + " created successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Product> getAllProductsFromFile() {
        List<Product> productsFromFile = new ArrayList<>();
        File file = new File(FILE_PATH_PRODUCT);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    String productName = data[0].trim();
                    double productPrice = Double.parseDouble(data[1].trim());
                    String[] t = data[2].trim().split("&");
                    List<String> types = Arrays.asList(t);
                    int percentSale = Integer.parseInt(data[3].trim());
                    String typeOfSale = data[4].trim();
                    String typeOfPrice = data[5].trim();
                    Product product = new Product(productName, productPrice, types, percentSale,
                            typeOfSale, typeOfPrice);
                    productsFromFile.add(product);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot find the user in file: " + FILE_PATH_PRODUCT);
        }
        return productsFromFile;
    }

    public boolean addNewProduct() {
        Scanner input = new Scanner(System.in);
        try {
            System.out.println("Enter the name of new product: ");
            String productName = input.nextLine();
            if (productName.isBlank()) {
                System.out.println("Name cannot be empty, try again");
                return false;
            }
            System.out.println("What type / other properties of product is it (enter with \",\": ");
            String productType = input.nextLine();
            productType = productType.replace(",", "&");
            System.out.println("What is the price of the new product: ");
            double productPrice = input.nextDouble();
            if (productPrice < 0 ) {
                System.out.println("Price cannot be less than zero, according to Oskar");
                return false;
            }
            System.out.println("Insert discount (whole number) from 0% to 100%.");
            int sale = (int) round(input.nextDouble());
            input.nextLine();
            if (sale > 100 || sale < 0) {
                System.out.println("Discount value is wrong.");
                return false;
            }
            System.out.println("Insert type of price, per \"kg\" or per \"piece\"");
            String typeOfPrice = input.nextLine();
            if (typeOfPrice.equalsIgnoreCase("kg") && typeOfPrice.equalsIgnoreCase("k")) {
                typeOfPrice = "kg";
            } else {
                typeOfPrice = "piece";
            }
            initiateDiscout();
            System.out.println("Insert type of sale, (A, B or C)");
            String type = input.nextLine();
            if (!type.equalsIgnoreCase("A") && !type.equalsIgnoreCase("B")
                    && !type.equalsIgnoreCase("C")) {
                type = "No";
            }
            StringBuilder sb = new StringBuilder();
            sb.append(productName + ",");
            sb.append(productPrice + ",");
            sb.append(productType + ",");
            sb.append(sale + ",");
            sb.append(type + ",");
            sb.append(typeOfPrice);
            File file = new File(FILE_PATH_PRODUCT);
            try (FileWriter csvWriter = new FileWriter(file, true)) {
                csvWriter.write(sb.toString());
                csvWriter.write("\n");
                System.out.println("Added new product: " + productName);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Something wrong, cannot add new product " + productName + " to file");
            }
        } catch (Exception e) {
            System.out.println("Cannot create new product. Error " + e);
        }
        return true;
    }

    public Product searchProductFromDB() {
        List<Product> allProducts = getAllProductsFromFile();
        Scanner input = new Scanner(System.in);
        boolean found = true;
        while (found) {
            System.out.println("Type the name of the product you are searching");
            System.out.println("Name: ");
            String targetItem = input.nextLine();
            System.out.println("Types (optional, with \" \"): ");
            String targetType = input.nextLine();
            List<String> targetTypes = Arrays.asList(targetType.split(" "));
            found = false;
            int count = 0;
            List<Product> resultList = productSearch(targetItem, targetTypes, allProducts);
            if (resultList.size() > 1) {
                found = false;
                System.out.println("===================================================================");
                System.out.println("More than one product found: ");
                for (Product p : resultList) {
                    System.out.println(p.getName() + " type: " + p.getTypes() + " price: " + p.getPrice());
                    System.out.println("Discount: " + p.getSale() + " % Price with discount: " + p.getPriceWithDiscount());
                }
                System.out.println("===================================================================");
            }
            if (resultList.size() == 0) {
                System.out.println("main.Product not found ");
            } else if (resultList.size() == 1) {
                serviceCustomer.printProductPrice(resultList.get(0));
                return resultList.get(0);
            }
            if (resultList.size() != 1) {
                System.out.println("Would you like to continue search (Y or N)?");
                String answer = input.nextLine();
                if (answer.equalsIgnoreCase("N")) {
                    found = false;
                } else {
                    found = true;
                }
            }
        }
        return null;
    }

    private static List productSearch(String name, List<String> typesForSearch, List<Product> productDataBase) {
        List<Product> searchResult = new ArrayList<>();
        if (productDataBase != null) {
            List<Product> searchResultByType = checkByTypes(typesForSearch, productDataBase);
            for (Product p : searchResultByType) {
                if (!name.isEmpty() && p.getName().toLowerCase().startsWith(name.toLowerCase())) {
                    searchResult.add(p);
                }
                if (name.isEmpty()) {
                    searchResult.add(p);
                }
            }
            for (Product r : searchResult) {
                System.out.println("Found product: " + r.toString());
            }
        }
        return searchResult;
    }

    private static List<Product> checkByTypes(List<String> typesForSearch, List<Product> productDataBase) {
        List<Product> resultList = new ArrayList<>();
        if (typesForSearch.get(0).isEmpty()) {
            return productDataBase;
        }
        for (Product p : productDataBase) {
            List<String> typesArray = p.getTypes();
            boolean check = true;
            for (String searchElement : typesForSearch) {
                if (!typesArray.contains(searchElement.trim())) {
                    check = false;
                    break;
                }
            }
            if (check) {
                resultList.add(p);
            }
        }
        return resultList;
    }

    private static void initiateDiscout() {
        System.out.println("Following types of discounts available");
        System.out.println("Type \"A\" - percent discount");
        System.out.println("Type \"B\" - buy two, get one free");
        System.out.println("Type \"C\" - buy for SEK 500, get a 10% discount");
    }

    public String getLineInFile(Product p) {
        StringBuilder sb = new StringBuilder();
        sb.append(p.getName() + ",");
        sb.append(p.getPrice() + ",");
        sb.append(p.getTypes().stream().collect(Collectors.joining("&")) + ",");
        sb.append(p.getSale() + ",");
        sb.append(p.getTypeOfSale() + ",");
        sb.append(p.getTypeOfPrice());

        return sb.toString();
    }

    public boolean changeLineInFile(String oldLine, String newLine) {
        if (oldLine.equals(newLine)) {
            System.out.println("No changes in product made");
            return true;
        }
        List<String> lines = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(FILE_PATH_PRODUCT));

            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find file with the product");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Something went wrong while reading file with product");
            throw new RuntimeException(e);
        }
        String lineToDelete = null;
        for (String s : lines) {
            if(oldLine.equals(s)) {
                lineToDelete = s;
                break;
            }
        }
        if (lineToDelete != null){
            lines.remove(lineToDelete);
            lines.add(newLine);
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH_PRODUCT));
                for (String l : lines) {
                    writer.write(l);
                    writer.newLine();
                }
                writer.close();

            } catch (IOException e) {
                System.out.println("Cannot overwrite information in product file.");
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }
}