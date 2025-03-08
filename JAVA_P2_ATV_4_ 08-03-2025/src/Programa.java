import java.util.Locale;
import java.util.Scanner;

public class Programa{
    public static void main(String[] args){
        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);
        
        try{
            System.out.println("Entre com os dados da conta");
            System.out.print("Número: ");
            int numero = sc.nextInt();
            sc.nextLine();
            System.out.print("Titular: ");
            String titular = sc.nextLine();
            System.out.print("Saldo inicial: ");
            double saldo = sc.nextDouble();
            System.out.print("Limite de saque: ");
            double limiteSaque = sc.nextDouble();
            
            Conta conta = new Conta(numero, titular, saldo, limiteSaque);
            
            System.out.println("Conta criada: " + conta.getNumero() + " - Titular: " + conta.getTitular());
            
            System.out.print("Informe o valor para saque: ");
            double valor = sc.nextDouble();
            
            conta.sacar(valor);
            System.out.printf("Novo saldo: %.2f%n", conta.getSaldo());
        } 
        catch (ExcecaoDominio e){
            System.out.println("Erro de saque: " + e.getMessage());
        }
        catch (Exception e){
            System.out.println("Erro inesperado: " + e.getMessage());
        }
        
        sc.close();
    }
}
