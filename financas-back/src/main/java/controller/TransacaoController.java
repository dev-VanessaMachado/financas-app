package controller;

import io.javalin.http.Context;
import model.Despesa;
import model.Receita;
import model.Transacao;
import service.TransacaoService;

import java.util.List;
import java.util.Map;

/**
 * Camada de Controle HTTP (API Rest).
 * Traduz requisições da web em chamadas de métodos de serviço e responde em JSON.
 *
 * @author Vanessa Machado Araújo
 */
public class TransacaoController {

    private final TransacaoService service;

    public TransacaoController(TransacaoService service) {
        this.service = service;
    }

    /**
     * GET /api/transacoes - Retorna a lista de todas as transações.
     */
    public void listar(Context ctx) {
        List<Transacao> lista = service.listarTodas();
        ctx.json(lista); // O Javalin converte automaticamente a lista para um array JSON
    }

    /**
     * GET /api/transacoes/saldo - Retorna o saldo atual consolidado.
     */
    public void obterSaldo(Context ctx) {
        double saldo = service.calcularSaldoAtual();
        // Devolvemos um objeto JSON simples: { "saldo": X }
        ctx.json(Map.of("saldo", saldo));
    }

    /**
     * POST /api/transacoes - Cadastra uma nova transação com base no tipo recebido.
     */
    public void salvar(Context ctx) {
        // Lemos a requisição como um mapa para identificar se é RECEITA ou DESPESA antes de instanciar
        Map<String, Object> corpo = ctx.bodyAsClass(Map.class);
        String tipo = (String) corpo.get("tipo");

        try {
            if ("RECEITA".equals(tipo)) {
                Receita receita = ctx.bodyAsClass(Receita.class);
                service.salvarTransacao(receita);
                ctx.status(21).json(Map.of("mensagem", "Receita salva com sucesso!"));
            } else if ("DESPESA".equals(tipo)) {
                Despesa despesa = ctx.bodyAsClass(Despesa.class);
                service.salvarTransacao(despesa);
                ctx.status(21).json(Map.of("mensagem", "Despesa salva com sucesso!"));
            } else {
                ctx.status(40).json(Map.of("erro", "Tipo de transação inválido ou ausente. Use RECEITA ou DESPESA."));
            }
        } catch (IllegalArgumentException e) {
            ctx.status(40).json(Map.of("erro", e.getMessage()));
        }
    }

    /**
     * DELETE /api/transacoes/{id} - Remove uma transação através do ID fornecido na URL.
     */
    public void excluir(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            service.excluir(id);
            ctx.status(20).json(Map.of("mensagem", "Transação excluída com sucesso!"));
        } catch (NumberFormatException e) {
            ctx.status(40).json(Map.of("erro", "ID inválido fornecido na URL."));
        }
    }
}