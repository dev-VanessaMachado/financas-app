import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

/**
 * O decorador @Injectable avisa ao Angular que esta classe pode ser "Injetada"
 * em outros lugares .
 * O 'providedIn: root' significa que o Angular gerencia este serviço como um Singleton
 * (uma única instância viva para o app inteiro).
 */
@Injectable({
  providedIn: 'root'
})
export class TransacaoService {

  // 1. Definição da URL base do Backend
  private readonly API_URL = 'http://localhost:8080/api/transacoes';

  // 2. Injeção de Dependência
  // Usa a função inject().
  private http = inject(HttpClient);

  // =========================================================================
  // MÉTODOS DE INTEGRAÇÃO HTTP (Conexão direta com os Endpoints Java)
  // =========================================================================

  /**
   * Busca todas as transações cadastradas no Banco de Dados através do Java.
   * Conecta com o @GetMapping do RestController.
   * @returns Um Observable contendo o Array de transações
   */
  listarTodas(): Observable<any[]> {
    // O método http.get faz uma requisição assíncrona. Ele não bloqueia a tela enquanto espera o Java responder.
    // O <any[]> avisa que o retorno esperado é uma lista de objetos.
    return this.http.get<any[]>(this.API_URL);
  }

  /**
   * Envia uma nova transação criada no frontend para ser salva no Banco de Dados real.
   * Conecta com o @PostMapping do RestController.
   * @param transacao Objeto contendo { descricao, valor, tipo }
   * @returns Um Observable com a transação salva (já com o ID gerado pelo banco)
   */
  salvar(transacao: any): Observable<any> {
    // O http.post envia a URL e, no segundo parâmetro, o corpo da requisição (Request Body) em formato JSON.
    return this.http.post<any>(this.API_URL, transacao);
  }

  /**
   * Deleta uma transação do banco de dados utilizando o ID dela.
   * Conecta com o @DeleteMapping("/{id}") do RestController.
   * @param id O identificador único da transação gerado pelo Banco de Dados
   */
  excluir(id: number): Observable<void> {
    // Monta a URL dinâmica usando "Template Literals" (crases ``).
    // Exemplo final: http://localhost:8080/api/transacoes/5
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
