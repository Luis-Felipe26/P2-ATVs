class PessoaJuridica extends Contribuinte{
    private int funcionarios;

    public PessoaJuridica(String nome, double rendaAnual, int funcionarios){
        super(nome, rendaAnual);
        this.funcionarios = funcionarios;
    }

    @Override
    public double calcularImposto(){
        return (funcionarios > 10) ? rendaAnual * 0.14 : rendaAnual * 0.16;
    }
}
