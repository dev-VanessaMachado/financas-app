package service;

import model.CategoriaDespesa;
import model.Despesa;
import model.Receita;
import model.Transacao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import repository.TransacaoRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários focados puramente nas regras de negócio da TransacaoService.
 * Aqui não tocamos no banco de dados real.
 *
 * @author Vanessa Machado Araújo
 */
class TransacaoServiceTest {

    // Criamos uma subclasse anônima (Fake/Mock manual) para simular o banco sem o Docker
    private final List<Transacao> bancoFake = new ArrayList<>();

    private final TransacaoRepository mockRepository = new TransacaoRepository() {
        @Override
        public List<Transacao> listarTodas() {
            return bancoFake;
        }
        @Override
        public void salvar(Transacao t) {
            bancoFake.add(t);
        }
    };

    private final TransacaoService service = new TransacaoService(mockRepository);

    @Test
    @DisplayName("Deve calcular o saldo líquido corretamente")
    void deveCalcularSaldoCorretamente() {
        // Arrange (Alimenta o cenário simulado)
        bancoFake.clear();
        bancoFake.add(new Receita(1, "Salário", 3000.00, LocalDate.now()));
        bancoFake.add(new Receita(2, "Freelance", 500.00, LocalDate.now()));
        bancoFake.add(new Despesa(3, "Luz", 200.00, LocalDate.now(), CategoriaDespesa.MORADIA));
        bancoFake.add(new Despesa(4, "Almoço", 50.00, LocalDate.now(), CategoriaDespesa.ALIMENTACAO));

        // Act
        double saldoCalculado = service.calcularSaldoAtual();

        // Assert: 3500 (receitas) - 250 (despesas) = 3250
        assertEquals(3250.00, saldoCalculado);
    }

    @Test
    @DisplayName("Deve agrupar o valor acumulado das despesas por categoria com sucesso")
    void deveAgruparDespesasPorCategoria() {
        // Arrange
        bancoFake.clear();
        bancoFake.add(new Despesa(1, "Mercado", 150.00, LocalDate.now(), CategoriaDespesa.ALIMENTACAO));
        bancoFake.add(new Despesa(2, "Ifood", 50.00, LocalDate.now(), CategoriaDespesa.ALIMENTACAO));
        bancoFake.add(new Despesa(3, "Internet", 120.00, LocalDate.now(), CategoriaDespesa.MORADIA));

        // Act
        Map<CategoriaDespesa, Double> relatorio = service.relatorioDespesasPorCategoria();

        // Assert
        assertEquals(2, relatorio.size(), "Deveria conter exatamente duas categorias no mapa.");
        assertEquals(200.00, relatorio.get(CategoriaDespesa.ALIMENTACAO));
        assertEquals(120.00, relatorio.get(CategoriaDespesa.MORADIA));
    }

    @Test
    @DisplayName("Deve lançar exceção se tentar salvar transação com valor inválido")
    void deveLancarExcecaoParaValorInvalido() {
        Transacao despesaInvalida = new Despesa(null, "Invalida", -10.0, LocalDate.now(), CategoriaDespesa.OUTROS);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.salvarTransacao(despesaInvalida);
        });

        assertEquals("O valor da transação deve ser maior que zero.", exception.getMessage());
    }
}