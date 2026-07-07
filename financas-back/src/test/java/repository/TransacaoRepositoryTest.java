package repository;

import model.CategoriaDespesa;
import model.Despesa;
import model.Receita;
import model.Transacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de integração para o repositório de transações.
 * Garante que os comandos SQL executam com sucesso no container Docker.
 *
 * @author Vanessa Machado Araújo
 */
class TransacaoRepositoryTest {

    private TransacaoRepository repository;

    @BeforeEach
    void setUp() throws SQLException {
        repository = new TransacaoRepository();

        // Limpa a tabela antes de cada teste para garantir isolamento
        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement("TRUNCATE TABLE transacoes RESTART IDENTITY;")) {
            stmt.executeUpdate();
        }
    }

    @Test
    @DisplayName("Deve salvar uma receita com sucesso no banco de dados")
    void deveSalvarReceitaComSucesso() throws SQLException {
        Transacao salario = new Receita(null, "Salário de Estágio", 2500.00, LocalDate.now());

        assertDoesNotThrow(() -> repository.salvar(salario));

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM transacoes WHERE descricao = ?")) {

            stmt.setString(1, "Salário de Estágio");
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(2500.00, rs.getDouble("valor"));
                assertEquals("RECEITA", rs.getString("tipo"));
                assertEquals("OUTROS", rs.getString("categoria"));
            }
        }
    }

    @Test
    @DisplayName("Deve salvar uma despesa associada a uma categoria com sucesso")
    void deveSalvarDespesaComSucesso() throws SQLException {
        Transacao internet = new Despesa(null, "Conta de Internet", 120.00, LocalDate.now(), CategoriaDespesa.MORADIA);

        assertDoesNotThrow(() -> repository.salvar(internet));

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM transacoes WHERE descricao = ?")) {

            stmt.setString(1, "Conta de Internet");
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(120.00, rs.getDouble("valor"));
                assertEquals("DESPESA", rs.getString("tipo"));
                assertEquals("MORADIA", rs.getString("categoria"));
            }
        }
    }

    @Test
    @DisplayName("Deve listar todas as transações salvas ordenadas por data")
    void deveListarTodasAsTransacoesComSucesso() {
        Transacao salario = new Receita(null, "Salário", 3000.00, LocalDate.now().minusDays(1));
        Transacao mercado = new Despesa(null, "Supermercado", 250.00, LocalDate.now(), CategoriaDespesa.ALIMENTACAO);

        repository.salvar(salario);
        repository.salvar(mercado);

        List<Transacao> transacoes = repository.listarTodas();

        assertNotNull(transacoes);
        assertEquals(2, transacoes.size());

        Transacao primeira = transacoes.get(0);
        assertEquals("Supermercado", primeira.getDescricao());

        Transacao segunda = transacoes.get(1);
        assertEquals("Salário", segunda.getDescricao());
    }

    @Test
    @DisplayName("Deve buscar uma transação específica pelo ID com sucesso")
    void deveBuscarTransacaoPorId() {
        // Arrange
        Transacao luz = new Despesa(null, "Conta de Luz", 180.00, LocalDate.now(), CategoriaDespesa.MORADIA);
        repository.salvar(luz);

        // Recupera da listagem para descobrir qual ID o banco serializou para ela
        List<Transacao> salvas = repository.listarTodas();
        Integer idGerado = salvas.get(0).getId();

        // Act
        Transacao encontrada = repository.buscarPorId(idGerado);

        // Assert
        assertNotNull(encontrada, "A transação deveria ter sido encontrada.");
        assertEquals(idGerado, encontrada.getId());
        assertEquals("Conta de Luz", encontrada.getDescricao());
        assertEquals("DESPESA", encontrada.getTipo());
        assertEquals("MORADIA", encontrada.getCategoria());
    }

    @Test
    @DisplayName("Deve excluir uma transação do banco de dados com sucesso")
    void deveExcluirTransacaoComSucesso() {
        // Arrange
        Transacao freela = new Receita(null, "Freelance de Site", 1500.00, LocalDate.now());
        repository.salvar(freela);

        List<Transacao> salvas = repository.listarTodas();
        Integer idGerado = salvas.get(0).getId();

        // Act
        assertDoesNotThrow(() -> repository.excluir(idGerado));

        // Assert
        Transacao deletada = repository.buscarPorId(idGerado);
        assertNull(deletada, "A transação não deveria mais existir no banco de dados.");
    }
}