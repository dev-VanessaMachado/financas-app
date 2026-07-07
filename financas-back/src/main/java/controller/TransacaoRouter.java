package controller;

import io.javalin.Javalin;

/**
 * Responsável exclusiva pelo mapeamento centralizado de todos os
 * endpoints e rotas HTTP da aplicação.
 *
 * @author Vanessa Machado Araújo
 */
public class TransacaoRouter {

    private final TransacaoController controller;

    public TransacaoRouter(TransacaoController controller) {
        this.controller = controller;
    }

    /**
     * Registra os endpoints da API dentro da instância ativa do Javalin.
     *
     * @param app Instância do servidor Javalin.
     */
    public void registrarRotas(Javalin app) {
        app.get("/api/transacoes", controller::listar);
        app.get("/api/transacoes/saldo", controller::obterSaldo);
        app.post("/api/transacoes", controller::salvar);
        app.delete("/api/transacoes/{id}", controller::excluir);
    }
}