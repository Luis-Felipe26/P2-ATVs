public class Main{
    public static void main(String[] args){
        ShoppingCart cart = new ShoppingCart(12345);

        TV tv = new TV("Samsung", 1200.99, 55);
        Refrigerator refrigarator = new Refrigerator("LG", 899.49, 400);
        Stove stove = new Stove("Brastemp", 599.99, 4);

        cart.addProduct(tv);
        cart.addProduct(refrigarator);
        cart.addProduct(stove);

        System.out.println("Customer ID: " + cart.getCustomerID());
        System.out.println("Total Items: " + cart.getItemCount());
        System.out.println(cart.getContents());
        System.out.println("Total Price: $" + cart.getTotalPrice());
    }
}