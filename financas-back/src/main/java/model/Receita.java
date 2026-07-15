package model;

import java.time.LocalDate;

/**
 * Representa uma entrada financeira (receita) no sistema.
 * Herda as propriedades básicas da classe {@link Transacao}.
 *
 * @author Vanessa Machado Araújo
 */
public class Receita extends Transacao {

    /**
     * CONSTRUTOR PADRÃO (VAZIO)
     * Necessário para a deserialização do JSON pelo Jackson.
     */
    public Receita() {
        super(); // Invoca explicitamente o construtor padrão de Transacao
    }

    /**
     * Construtor para inicializar uma receita utilizando o construtor da superclasse.
     *
     * @param id        O identificador único da receita no banco de dados.
     * @param descricao A descrição da entrada (ex: "Salário", "Bônus").
     * @param valor     O valor monetário positivo da entrada.
     * @param data      A data em que a receita foi recebida.
     */
    public Receita(Integer id, String descricao, Double valor, LocalDate data){
        super(id, descricao, valor, data);
    }

    /**
     * Identifica o tipo específico desta transação como uma receita.
     * Esse método sobrescreve a definição da superclasse para permitir a diferenciação
     * das movimentações financeiras na persistência e nos relatórios.
     *
     * @return Uma {@link String} constante com o valor "RECEITA".
     */
    @Override
    public String getTipo() {
        return "RECEITA";
    }

    /**
     * Retorna uma classificação padrão para a receita.
     * Como as receitas não possuem divisões por categorias de gastos no modelo de dados atual,
     * este método retorna um valor fixo para manter a consistência e compatibilidade
     * com a estrutura da tabela no banco de dados.
     *
     * @return Uma {@link String} constante com o valor "OUTROS".
     */
    @Override
    public String getCategoria() {
        return "OUTROS";
    }
}

