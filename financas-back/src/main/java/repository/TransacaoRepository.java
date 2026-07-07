package repository;

import model.CategoriaDespesa;
import model.Despesa;
import model.Receita;
import model.Transacao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Camada de persistência responsável por realizar as operações de CRUD
 * para a entidade Transacao no banco de dados PostgreSQL.
 *
 * @author Vanessa Machado Araújo
 */
public class TransacaoRepository {

    /**
     * Insere uma nova transação (Receita ou Despesa) no banco de dados.
     */
    public void salvar(Transacao transacao) {
        String sql = """
            INSERT INTO transacoes (descricao, valor, data, tipo, categoria) 
            VALUES (?, ?, ?, ?, ?);
            """;

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, transacao.getDescricao());
            stmt.setDouble(2, transacao.getValor());
            stmt.setDate(3, Date.valueOf(transacao.getData()));
            stmt.setString(4, transacao.getTipo());
            stmt.setString(5, transacao.getCategoria());

            stmt.executeUpdate();
            System.out.println("DEBUG: Transação '" + transacao.getDescricao() + "' salva com sucesso!");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar transação no repositório: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera todas as transações registradas no banco de dados.
     */
    public List<Transacao> listarTodas() {
        String sql = "SELECT id, descricao, valor, data, tipo, categoria FROM transacoes ORDER BY data DESC;";
        List<Transacao> lista = new ArrayList<>();

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String descricao = rs.getString("descricao");
                Double valor = rs.getDouble("valor");
                LocalDate data = rs.getDate("data").toLocalDate();
                String tipo = rs.getString("tipo");

                if ("RECEITA".equals(tipo)) {
                    lista.add(new Receita(id, descricao, valor, data));
                } else if ("DESPESA".equals(tipo)) {
                    CategoriaDespesa categoria = CategoriaDespesa.valueOf(rs.getString("categoria"));
                    lista.add(new Despesa(id, descricao, valor, data, categoria));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar transações do repositório: " + e.getMessage(), e);
        }

        return lista;
    }

    /**
     * Busca uma transação específica no banco de dados através do seu ID de registro.
     *
     * @param id Identificador único da transação.
     * @return O objeto mapeado como {@link Receita} ou {@link Despesa}, ou null se não for encontrado.
     * @throws RuntimeException Caso ocorra um erro na comunicação com o banco.
     */
    public Transacao buscarPorId(Integer id) {
        String sql = "SELECT id, descricao, valor, data, tipo, categoria FROM transacoes WHERE id = ?;";

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String descricao = rs.getString("descricao");
                    Double valor = rs.getDouble("valor");
                    LocalDate data = rs.getDate("data").toLocalDate();
                    String tipo = rs.getString("tipo");

                    if ("RECEITA".equals(tipo)) {
                        return new Receita(id, descricao, valor, data);
                    } else if ("DESPESA".equals(tipo)) {
                        CategoriaDespesa categoria = CategoriaDespesa.valueOf(rs.getString("categoria"));
                        return new Despesa(id, descricao, valor, data, categoria);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar transação por ID: " + e.getMessage(), e);
        }

        return null; // Retorna nulo se o ID não existir no banco
    }

    /**
     * Remove uma transação do banco de dados baseando-se no ID fornecido.
     *
     * @param id Identificador único da transação a ser deletada.
     * @throws RuntimeException Caso ocorra um erro na execução do comando SQL.
     */
    public void excluir(Integer id) {
        String sql = "DELETE FROM transacoes WHERE id = ?;";

        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("DEBUG: Transação ID " + id + " removida com sucesso!");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir transação no repositório: " + e.getMessage(), e);
        }
    }
}