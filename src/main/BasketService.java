package main;

import java.util.List;

public class BasketService {

    public List<Basket> addToBasket (Basket basket, List<Basket> baskets) {
        Product toAdd = basket.getProduct();
        for (Basket b : baskets) {
            if(toAdd.equals(b.getProduct())){
                b.setAmount(b.getAmount() + basket.getAmount());
                return baskets;
            }
        }

        baskets.add(basket);
        return baskets;
    }

}
