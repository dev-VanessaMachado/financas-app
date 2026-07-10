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
  // VARIÁVEIS AUXILIARES (Conectadas diretamente ao Formulário HTML)
  // =========================================================================
  novaDescricao: string = '';
  novoValor: number | null = null; // Iniciamos com null para a caixinha começar limpa na tela
  novoTipo: string = 'RECEITA';    // Padrão inicial do Select será 'Receita'

  // =========================================================================
  // COMPORTAMENTOS (Métodos da Classe)
  // =========================================================================

  /**
   * Pega os dados capturados pelas variáveis auxiliares, valida e insere na lista.
   */
  adicionarTransacao(): void {

    // Fazemos uma barreira de segurança (igual no Java).
    // Verificamos se a descrição não está vazia (.trim() remove espaços em branco das pontas)
    // e se o valor existe e é maior que zero. Se algo estiver errado, o código para no 'return'.
    if (!this.novaDescricao || this.novaDescricao.trim() === '' || !this.novoValor || this.novoValor <= 0) {
      alert('Por favor, preencha todos os campos corretamente!');
      return;
    }

    // Criamos o novo objeto da transação estruturado
    const nova = {
      id: Date.now(), // Gera um ID numérico único usando os milissegundos do relógio atual
      descricao: this.novaDescricao,
      valor: this.novoValor,
      tipo: this.novoTipo
    };

    // O método .push() empurra o nosso novo objeto para o fim do Array original
    this.transacoes.push(nova);

    // Se a transação que o usuário acabou de criar for do tipo RECEITA, nós somamos ao saldo atual.
    // Se for do tipo DESPESA, nós subtraímos do saldo atual.
    if (nova.tipo === 'RECEITA') {
      this.resumoSaldo.saldoAtual += nova.valor;
    } else {
      this.resumoSaldo.saldoAtual -= nova.valor;
    }

    // LIMPEZA:
    // Como usamos Two-Way Data Binding, ao resetarmos as variáveis aqui no TypeScript,
    // o Angular limpa instantaneamente os textos das caixinhas lá na tela do usuário!
    this.novaDescricao = '';
    this.novoValor = null;
    this.novoTipo = 'RECEITA';
  }

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
