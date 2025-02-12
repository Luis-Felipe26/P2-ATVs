public class Main{
    public static void main(String[] args){
        ShoppingCart cart = new ShoppingCart(123);

        Product p1 = new Product("Polystation", 4500.00);
        Product p2 = new Product("Polysense", 550.50);
        Product p3 = new Product("O BOM DE GUERRA, ULTIMATE", 300.00);

        cart.addProduct(p1);
        cart.addProduct(p2);
        cart.addProduct(p3);

        System.out.println(cart.getContents());
        System.out.println("Total Price: $" + cart.getTotalPrice());
        System.out.println("Item Count: " + cart.getItemCount());

        cart.removeProduct(p2);
        System.out.println("\nApós remover o Polysense:");
        System.out.println(cart.getContents());
    }
}
