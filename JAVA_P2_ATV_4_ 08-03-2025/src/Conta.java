class Conta{
    private Integer numero;
    private String titular;
    private Double saldo;
    private Double limiteSaque;

    public Conta(Integer numero, String titular, Double saldo, Double limiteSaque){
        this.numero = numero;
        this.titular = titular;
        this.saldo = saldo;
        this.limiteSaque = limiteSaque;
    }

    public Integer getNumero(){
        return numero;
    }

    public String getTitular(){
        return titular;
    }

    public void depositar(double valor){
        saldo += valor;
    }

    public void sacar(double valor) throws ExcecaoDominio{
        if (valor > limiteSaque){
            throw new ExcecaoDominio("O valor excede o limite de saque");
        }
        if (valor > saldo){
            throw new ExcecaoDominio("Saldo insuficiente");
        }
        saldo -= valor;
    }

    public Double getSaldo(){
        return saldo;
    }
}