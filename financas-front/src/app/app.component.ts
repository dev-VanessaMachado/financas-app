import { Component } from '@angular/core';

@Component({
  selector: 'app-root',               // A tag HTML customizada que representa este componente
  templateUrl: './app.component.html', // O arquivo que cuida do visual
  styleUrls: ['./app.component.css']    // O arquivo que cuida dos estilos
})
export class AppComponent {

  // =========================================================================
  // PROPRIEDADES (Atributos da Classe)
  // =========================================================================

  // Uma variável simples do tipo texto (string)
  title: string = 'financas-front';

  // Um objeto estruturado para simular os dados consolidados do usuário.
  // No TypeScript, objetos usam a estrutura de chave: valor entre chaves {}.
  resumoSaldo = {
    usuario: 'Vanessa Machado Araújo',
    saldoAtual: 4610.50,
    status: 'Positivo'
  };

  /*
  * Array (Lista) de Objetos de Transações.
  * Cada item possui um id, uma descrição, um valor numérico e o tipo.
  */
  transacoes = [
    { id: 1, descricao: 'Mercado Central', valor: 250.00, tipo: 'DESPESA' },
    { id: 2, descricao: 'Salário Empresa', valor: 3500.00, tipo: 'RECEITA' },
    { id: 3, descricao: 'Academia', valor: 90.00, tipo: 'DESPESA' },
    { id: 4, descricao: 'Freelance Desenvolvedora', valor: 1450.00, tipo: 'RECEITA' }
  ];

  // =========================================================================
  // COMPORTAMENTOS (Métodos da Classe)
  // =========================================================================

  /**
   * Remove uma transação específica da memória e estorna o seu impacto no saldo.
   * @param transacaoSelecionada O objeto completo da transação que o usuário clicou
   */
  excluirTransacao(transacaoSelecionada: any): void {

    if (transacaoSelecionada.tipo === 'RECEITA') {
      this.resumoSaldo.saldoAtual -= transacaoSelecionada.valor;
    } else {
      this.resumoSaldo.saldoAtual += transacaoSelecionada.valor;
    }

    this.transacoes = this.transacoes.filter(t => t.id !== transacaoSelecionada.id);
  }
}
