public class ProdutoPerecivel extends Produto{
    private static final double DESCONTO =  0.25;
    private static final int PRAZO_DESCONTO = 7;
    private LocalDate dataValidade;

    public ProdutoPerecivel(String desc,double precoCusto, double margemLucro, LocalDate validade){
        super(desc,precoCusto, margemLucro);
        if (validade.isAfeter(LocalDate.now()) || validade.isEqual(LocalDate.now())) {
            dataValidade = validade;
        } else {
            throw new IllegalArgumentException("Data de validade deve ser futura.");
        }
    }

  
}