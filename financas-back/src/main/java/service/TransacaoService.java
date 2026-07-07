package service;

import model.CategoriaDespesa;
import model.Transacao;
import repository.TransacaoRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Camada de serviço responsável por aplicar as regras de negócio
 * e consolidar os cálculos financeiros da aplicação.
 *
 * @author Vanessa Machado Araújo
 */
public class TransacaoService {

    private final TransacaoRepository repository;

    // Injetamos o repositório no construtor para manter o desacoplamento
    public TransacaoService(TransacaoRepository repository) {
        this.repository = repository;
    }

    /**
     * Regra de Negócio: Salva uma transação aplicando validações de segurança.
     */
    public void salvarTransacao(Transacao transacao) {
        if (transacao == null) {
            throw new IllegalArgumentException("A transação não pode ser nula.");
        }
        // Substituído .blank() por .trim().isEmpty() para compatibilidade total
        if (transacao.getDescricao() == null || transacao.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição da transação é obrigatória.");
        }
        if (transacao.getValor() == null || transacao.getValor() <= 0) {
            throw new IllegalArgumentException("O valor da transação deve ser maior que zero.");
        }

        repository.salvar(transacao);
    }

    /**
     * Regra de Negócio: Calcula o saldo líquido atual do usuário.
     * Fórmula: $$Saldo = \\sum Receitas - \\sum Despesas$$
     */
    public double calcularSaldoAtual() {
        List<Transacao> todas = repository.listarTodas();
        double totalReceitas = 0;
        double totalDespesas = 0;

        for (Transacao t : todas) {
            if ("RECEITA".equals(t.getTipo())) {
                totalReceitas += t.getValor();
            } else if ("DESPESA".equals(t.getTipo())) {
                totalDespesas += t.getValor();
            }
        }

        return totalReceitas - totalDespesas;
    }

    /**
     * Regra de Negócio: Agrupa e soma os gastos totais por categoria.
     * Útil para plotagem de gráficos de pizza/rosca no frontend.
     */
    public Map<CategoriaDespesa, Double> relatorioDespesasPorCategoria() {
        List<Transacao> todas = repository.listarTodas();
        Map<CategoriaDespesa, Double> resumo = new HashMap<>();

        for (Transacao t : todas) {
            if ("DESPESA".equals(t.getTipo())) {
                // Como t.getCategoria() devolve String, convertemos de volta para o Enum
                CategoriaDespesa cat = CategoriaDespesa.valueOf(t.getCategoria());

                // Soma o valor atual ao acumulado da categoria
                resumo.put(cat, resumo.getOrDefault(cat, 0.0) + t.getValor());
            }
        }

        return resumo;
    }

    // Repassa a chamada para as outras operações básicas do repositório
    public List<Transacao> listarTodas() {
        return repository.listarTodas();
    }

    public Transacao buscarPorId(Integer id) {
        return repository.buscarPorId(id);
    }

    public void excluir(Integer id) {
        repository.excluir(id);
    }
}