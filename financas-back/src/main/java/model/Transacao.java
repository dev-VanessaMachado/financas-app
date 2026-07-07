package model;

import java.time.LocalDate;

/**
 * Representa uma movimentação financeira genérica no sistema.
 * Por ser uma classe abstrata, serve como base para a criação de tipos específicos
 * de transações, como Receitas e Despesas.
 *
 * @author Vanessa Machado Araújo
 */
public abstract class Transacao {
    private Integer id;
    private String descicao;
    private Double valor;
    private LocalDate data;

    /**
     * Construtor completo para inicializar uma transação com todas as suas propriedades.
     *
     * @param id        O identificador único da transação no banco de dados.
     * @param descricao A descrição ou título da movimentação (ex: "Salário", "Supermercado").
     * @param valor     O valor monetário da transação.
     * @param data      A data em que a transação foi realizada.
     */
    public Transacao(Integer id, String descricao, Double valor, LocalDate data) {
        this.id = id;
        this.descicao = descricao;
        this.valor = valor;
        this.data = data;
    }

    // Métodos abstratos que as classes filhas serão obrigadas a responder
    public abstract String getTipo();
    public abstract String getCategoria();

    /**
     * Recupera o identificador único da transação.
     * @return O id da transação.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Altera o identificador único da transação.
     * @param id O novo id a ser atribuído.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Recupera a descrição da transação.
     * @return A descrição detalhada.
     */
    public String getDescricao() {
        return descicao;
    }

    /**
     * Altera a descrição da transação.
     * @param descricao A nova descrição.
     */
    public void setDescicao(String descicao) {
        this.descicao = descicao;
    }

    /**
     * Recupera o valor monetário da transação.
     * @return O valor em formato Double.
     */
    public Double getValor() {
        return valor;
    }

    /**
     * Altera o valor monetário da transação.
     * @param valor O novo valor.
     */
    public void setValor(Double valor) {
        this.valor = valor;
    }

    /**
     * Recupera a data de realização da transação.
     * @return Um objeto {@link LocalDate} contendo a data.
     */
    public LocalDate getData() {
        return data;
    }

    /**
     * Altera a data de realização da transação.
     * @param data A nova data no formato {@link LocalDate}.
     */
    public void setData(LocalDate data) {
        this.data = data;
    }
}
