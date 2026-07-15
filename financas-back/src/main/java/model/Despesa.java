package model;

import java.time.LocalDate;

/**
 * Representa uma saída financeira (despesa) no sistema.
 * Herda de {@link Transacao} e adiciona a especialização de possuir uma categoria.
 *
 * @author Vanessa Machado Araújo
 */
public class Despesa extends Transacao {
    private CategoriaDespesa categoria;

    /**
     * CONSTRUTOR PADRÃO (VAZIO)
     * Necessário para a deserialização do JSON pelo Jackson.
     */
    public Despesa() {
        super(); // Invoca o construtor padrão de Transacao
    }

    /**
     * Construtor completo para inicializar uma despesa com suas propriedades base e sua categoria.
     *
     * @param id        O identificador único da despesa no banco de dados.
     * @param descricao A descrição da saída (ex: "Supermercado", "Posto de Combustível").
     * @param valor     O valor monetário da despesa.
     * @param data      A data em que a despesa foi realizada.
     * @param categoria A classificação da despesa com base no enum {@link CategoriaDespesa}.
     */
    public Despesa(Integer id, String descricao, Double valor, LocalDate data,
                   CategoriaDespesa categoria) {
        super(id, descricao, valor, data);
        this.categoria = categoria;

    }

    /**
     * Identifica o tipo específico desta transação como uma despesa.
     * Esse método sobrescreve a definição da superclasse para permitir a diferenciação
     * das movimentações financeiras na persistência e nos relatórios.
     *
     * @return Uma {@link String} constante com o valor "DESPESA".
     */
    @Override
    public String getTipo() {
        return "DESPESA";
    }

    /**
     * Recupera a categoria específica desta despesa.
     * @return O enum {@link CategoriaDespesa} correspondente.
     */
    @Override
    public String getCategoria() {
        return this.categoria != null ? this.categoria.name() : "OUTROS";
    }

    /**
     * Altera a categoria desta despesa.
     * @param categoria A nova categoria a ser atribuída.
     */
    public void setCategoria(CategoriaDespesa categoria) {
        this.categoria = categoria;
    }
}
