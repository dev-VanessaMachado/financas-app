package repository;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gerencia a abertura de conexões com o banco de dados PostgreSQL.
 * Implementa o padrão Factory para centralizar a criação de instâncias de conexão,
 * carregando dinamicamente as credenciais a partir de um arquivo externo de configuração (.env)
 * para mitigar o risco de exposição de dados sensíveis no código-fonte.
 *
 * @author Vanessa Machado Araújo
 */
public class FabricaConexao {

    // Inicializa o Dotenv para ler o arquivo .env na raiz do projeto
    private static final Dotenv dotenv = Dotenv.configure().load();

    // Busca as variáveis diretamente do arquivo .env através da biblioteca
    private static final String URL = dotenv.get("DB_URL");
    private static final String USUARIO = dotenv.get("DB_USER");
    private static final String SENHA = dotenv.get("DB_PASSWORD");

    /**
     * Estabelece uma nova conexão ativa com o banco de dados.
     *
     * @return Um objeto {@link Connection} pronto para executar instruções SQL.
     * @throws RuntimeException Caso ocorra uma falha crítica na conexão ou as variáveis estejam ausentes.
     */
    public static Connection getConexao() {
        // LINHAS DE TESTE TEMPORÁRIAS:
        System.out.println("DEBUG - URL LIDA: " + URL);
        System.out.println("DEBUG - USUARIO LIDO: " + USUARIO);
        System.out.println("DEBUG - SENHA LIDA: " + SENHA);

        // Validação de segurança preventiva
        if (URL == null || USUARIO == null || SENHA == null) {
            throw new RuntimeException("Erro de Configuração: As chaves necessárias não foram encontradas no arquivo .env!");
        }

        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            throw new RuntimeException("Erro crítico ao conectar com o banco de dados: " + e.getMessage(), e);
        }
    }

    /**
     * Cria as tabelas do sistema no banco de dados caso elas ainda não existam.
     * Garante a inicialização automática da estrutura de dados ao subir a aplicação.
     */
    public static void criarTabelas() {
        String sql = """
            CREATE TABLE IF NOT EXISTS transacoes (
                id SERIAL PRIMARY KEY,
                descricao VARCHAR(255) NOT NULL,
                valor NUMERIC(10, 2) NOT NULL,
                data DATE NOT NULL,
                tipo VARCHAR(10) NOT NULL,
                categoria VARCHAR(50)
            );
            """;

        // O try-with-resources garante o fechamento automático da conexão e do statement
        try (Connection conn = getConexao();
             java.sql.Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("DEBUG: Tabela 'transacoes' verificada/criada com sucesso.");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inicializar tabelas no banco de dados: " + e.getMessage(), e);
        }
    }
}