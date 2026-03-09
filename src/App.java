import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class App {
	

	static final int MAX_NOVOS_PRODUTOS = 10;

	static String nomeArquivoDados;

	static Produto[] produtosCadastrados;

	static Scanner teclado;

	static int quantosProdutos = 0;

	/** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
	static void pausa(){
		System.out.println("Digite enter para continuar...");
		teclado.nextLine();
	}
	/** Cabeçalho principal da CLI do sistema */
	static void cabecalho(){
		System.out.println("===============================");
		System.out.println("AEDs II COMÉRCIO DE PRODUTOS");
		System.out.println("===============================");
	}

	/** Imprime o menu principal, lê a opção do usuário e a retorna (int).
	* Perceba que poderia haver uma melhor modularização com a criação de uma classe Menu.
	* @return Um inteiro com a opção do usuário.
	*/
	static int menu(){
		cabecalho();
			System.out.println("1 - Listar todos os produtos");
			System.out.println("2 - Procurar e listar um produto");
			System.out.println("3 - Cadastrar novo produto");
			System.out.println("0 - Sair");
			System.out.print("Digite sua opção: ");
			return Integer.parseInt(teclado.nextLine());
	}

	/**
	* Lê os dados de um arquivo texto e retorna um vetor de produtos. Arquivo no formato
	* N (quantiade de produtos) <br/>
	* tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade] <br/>
	* Deve haver uma linha para cada um dos produtos. Retorna um vetor vazio em caso de problemas com o arquivo.
	* @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
	* @return Um vetor com os produtos carregados, ou vazio em caso de problemas de leitura.
	*/
	static Produto[] lerProdutos(String nomeArquivo){
		Scanner arquivo = null;
		int i,numProdutos;
		String linha;
		Produto produto;
		Produto[] produtosCadastrados = new Produto[MAX_NOVOS_PRODUTOS];

		try{
			arquivo = new Scanner(new File(nomeArquivoDados), Charset.forName("UTF-8"));
			numProdutos = Integer.parseInt(arquivo.nextLine());
			for(i = 0; (i<numProdutos && i <MAX_NOVOS_PRODUTOS);i++){
				linha= arquivo.nextLine();
				produto = Produto.criarDoTexto(linha);
				produtosCadastrados[i] = produto;
			}
			quantosProdutos = i;
		}catch(IOException execaoArquivo){
			produtosCadastrados = null;
		} finally{
			arquivo.close();
		}
		return produtosCadastrados;
	}

	/** Lista todos os produtos cadastrados, numerados, um por linha */
	static void listarTodosOsProdutos() {
		System.out.println("===== Lista de Produtos =====");
		if (quantosProdutos == 0) {
			System.out.println("Nenhum produto ainda foi cadastrado.");
		} else {
		
			for (int i = 0; i < quantosProdutos; i++) {
				
				System.out.println((i + 1) + " - " + produtosCadastrados[i].toString());
			}
		}
	}

	/** Localiza um produto no vetor de cadastrados, a partir do nome (descrição), e imprime seus dados.
	* A busca não é sensível ao caso. Em caso de não encontrar o produto, imprime mensagem padrão */
	static void localizarProdutos() {
		System.out.print("\nDigite o nome (descrição) do produto: ");
		String busca = teclado.nextLine();
		boolean encontrado = false;

		for (int i = 0; i < quantosProdutos; i++) {
			
			if (produtosCadastrados[i].getDescricao().equalsIgnoreCase(busca)) { 
				System.out.println("Produto localizado:");
				System.out.println(produtosCadastrados[i].toString());
				encontrado = true;
				break; 
			}
		}

		if (!encontrado) {
			System.out.println("O Produto não encontrado no sistema.");
		}
	}

	/**
	* Rotina de cadastro de um novo produto: pergunta ao usuário o tipo do produto, lê os dados correspondentes,
	* cria o objeto adequado de acordo com o tipo, inclui no vetor. Este método pode ser feito com um nível muito
	* melhor de modularização. As diversas fases da lógica poderiam ser encapsuladas em outros métodos.
	* Uma sugestão de melhoria mais significativa poderia ser o uso de padrão Factory Method para criação dos
	objetos.
	*/
	static void cadastrarProduto() {
		if (quantosProdutos >= MAX_NOVOS_PRODUTOS) {
			System.out.println("Erro: Limite de armazenamento de produtos atingido.");
			return;
		}

		System.out.println("==== Cadastro de Novo Produto ====");
		System.out.println("1 - Produto Não Perecivel");
		System.out.println("2 - Produto Perecivel");
		System.out.print("Escolha o tipo: ");
		
		int tipo = Integer.parseInt(teclado.nextLine());

		System.out.print("Digite a descrição: ");
		String desc = teclado.nextLine();
		System.out.print("Digite o preço: ");
		double preco = Double.parseDouble(teclado.nextLine());
		System.out.print("Digite a margem do lucro: ");
		double margemLucro = Double.parseDouble(teclado.nextLine());

		LocalDate validade = null;
		if (tipo == 2){
			System.out.print("Digite a data de validade (dd/MM/yyyy): ");
			String dataValidade = teclado.nextLine();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			validade = LocalDate.parse(dataValidade, formatter);
			
		}

		Produto novo = null;

	
		switch (tipo) {
			case 1 -> novo = new ProdutoNaoPerecivel(desc,preco,margemLucro);
			case 2 -> novo = new ProdutoPerecivel(desc, preco,margemLucro, validade);
			default -> System.out.println("Tipo inválido. Cadastrando como produto comum.");
		}

		
		produtosCadastrados[quantosProdutos] = novo;
		quantosProdutos++;
		
		System.out.println("Produto cadastrado com sucesso!");
	}

	/**
	 * Salva os dados dos produtos cadastrados no arquivo csv informado. 
	 * Sobrescreve todo o conteúdo do arquivo.
	 * @param nomeArquivo Nome do arquivo a ser gravado.
	 */
	public static void salvarProdutos(String nomeArquivo) {
		try (PrintWriter escritor = new java.io.PrintWriter(new java.io.FileWriter(nomeArquivo, Charset.forName("UTF-8")))) {
			escritor.println(quantosProdutos);

			for (int i = 0; i < quantosProdutos; i++) {
				if (produtosCadastrados[i] != null) {
					escritor.println(produtosCadastrados[i].gerarDadosTexto());
				}
			}
			
			System.out.println("Dados salvos com sucesso em: " + nomeArquivo);
		} catch (IOException e) {
			System.err.println("Erro ao tentar salvar os dados no arquivo: " + e.getMessage());
		}
	}

	public static void main(String[] args) throws Exception {
				
		teclado = new Scanner(System.in, Charset.forName("ISO-8859-2"));
		nomeArquivoDados = "dadosProdutos.csv";
		produtosCadastrados = lerProdutos(nomeArquivoDados);
		int opcao = -1;
		do{
			opcao = menu();
			switch (opcao) {
			case 1 -> listarTodosOsProdutos();
			case 2 -> localizarProdutos();
			case 3 -> cadastrarProduto();
		}
		pausa();
		}while(opcao !=0);
			salvarProdutos(nomeArquivoDados);
			teclado.close();
		
	}
}
