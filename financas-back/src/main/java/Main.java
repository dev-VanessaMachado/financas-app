import controller.TransacaoController;
import controller.TransacaoRouter;
import io.javalin.Javalin;
import repository.TransacaoRepository;
import service.TransacaoService;

/**
 * Ponto de entrada (Bootstrap) da aplicação financeira.
 * Configura as injeções de dependência de forma limpa e inicializa o servidor.
 *
 * @author Vanessa Machado Araújo
 */
public class Main {
    public static void main(String[] args) {
        // 1. Inicializa as camadas do sistema
        TransacaoRepository repository = new TransacaoRepository();
        TransacaoService service = new TransacaoService(repository);
        TransacaoController controller = new TransacaoController(service);

        // Instancia a classe especializada de rotas
        TransacaoRouter router = new TransacaoRouter(controller);

        // 2. Cria e configura o servidor Javalin ensinando o Jackson a ler LocalDate
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> cors.addRule(it -> it.anyHost()));

            // Instancia e configura o ObjectMapper do Jackson
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper()
                    .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

            // Passamos a instância do ObjectMapper dentro de uma lambda factory
            config.jsonMapper(new io.javalin.json.JavalinJackson(mapper, false));
        });

        // 3. Aplica o mapeamento isolado e inicia o servidor
        router.registrarRotas(app);
        app.start(8080);

        System.out.println("🚀 Servidor iniciado com sucesso em http://localhost:8080");
    }
}