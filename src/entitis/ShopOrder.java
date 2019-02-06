package entitis;

import lombok.Data;

import java.util.Random;

@Data
public class ShopOrder implements Comparable<ShopOrder>{
    public static final Random random=new Random();
    private int totalPrice;
    private long userId;
    private String userName;

    public static ShopOrder build(){
        ShopOrder shopOrder=new ShopOrder();
        shopOrder.setTotalPrice(random.nextInt(10000));
        shopOrder.setUserId(random.nextInt(100));
        shopOrder.setUserName(String.valueOf(shopOrder.getUserId()));
        return shopOrder;
    }

    public int compareTo(ShopOrder o) {
        return this.getTotalPrice()-o.getTotalPrice();
    }
}
