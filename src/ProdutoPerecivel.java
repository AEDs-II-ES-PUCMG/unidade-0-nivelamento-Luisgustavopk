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

    /**
    * Gera uma linha de texto a partir dos dados do produto. Preço e margem de lucro vão formatados com 2 casas
    decimais.
    * Data de validade vai no formato dd/mm/aaaa
    * @return Uma string no formato "2; descrição;preçoDeCusto;margemDeLucro;dataDeValidade"
    */
    @Override
    public String gerarDadosTexto() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = formato.format(dataValidade);
        String precoFormatado = String.format("%.2f", precoCusto).replace(",", ".");
        String margemFormatada = String.format("%.2f", margemLucro).replace(",", ".");

        return String.format("2;%s;%s;%s,%s", descricao,precoFormatado,margemFormatada,dataFormatada);
    }
}

