import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class CalculadoraImpostos{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        List<Contribuinte> contribuintes = new ArrayList<>();
        
        System.out.print("Digite o número de contribuintes: ");
        int n = sc.nextInt();
        
        for (int i = 0; i < n; i++){
            System.out.println("Dados do contribuinte #" + (i + 1) + ":");
            System.out.print("Pessoa física ou jurídica (f/j)? ");
            char tipo = sc.next().charAt(0);
            sc.nextLine();
            
            System.out.print("Nome: ");
            String nome = sc.nextLine();
            
            System.out.print("Renda anual: ");
            double rendaAnual = sc.nextDouble();
            
            if (tipo == 'f'){
                System.out.print("Gastos com saúde: ");
                double gastosSaude = sc.nextDouble();
                contribuintes.add(new PessoaFisica(nome, rendaAnual, gastosSaude));
            } else{
                System.out.print("Número de funcionários: ");
                int funcionarios = sc.nextInt();
                contribuintes.add(new PessoaJuridica(nome, rendaAnual, funcionarios));
            }
        }
        
        double totalImpostos = 0.0;
        System.out.println("\nIMPOSTOS PAGOS:");
        for (Contribuinte contribuinte : contribuintes){
            double imposto = contribuinte.calcularImposto();
            totalImpostos += imposto;
            System.out.printf("%s: R$ %.2f\n", contribuinte.getNome(), imposto);
        }
        
        System.out.printf("TOTAL DE IMPOSTOS: R$ %.2f\n", totalImpostos);
        
        sc.close();
    }
}

