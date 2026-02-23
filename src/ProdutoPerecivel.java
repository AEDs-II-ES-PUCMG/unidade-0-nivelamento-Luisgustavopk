import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;



public class ProdutoPerecivel extends Produto{
    private static final double DESCONTO =  0.25;
    private static final int PRAZO_DESCONTO = 7;
    private LocalDate dataValidade;

    public ProdutoPerecivel(String desc,double precoCusto, double margemLucro, LocalDate validade){
        super(desc,precoCusto, margemLucro);
        if (validade.isAfter(LocalDate.now())) {
            dataValidade = validade;
        } else {
            throw new IllegalArgumentException("Data de validade deve ser futura.");
        }
    }

    public double valorVenda(){

        long diasParaValidade = ChronoUnit.DAYS.between(LocalDate.now(), dataValidade);
        if (diasParaValidade <= PRAZO_DESCONTO) {
           return (precoCusto * (1.0 + margemLucro)) * (1 - DESCONTO);
        } 

         return (precoCusto * (1.0 + margemLucro));
        
    }

    @Override
    public String toString() {
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    return super.toString() + " - Validade até: " + dataValidade.format(formatter);
    }
}