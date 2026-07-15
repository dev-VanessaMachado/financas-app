import { Component, OnInit, inject } from '@angular/core';
import { TransacaoService } from './transacao.service';

@Component({
  selector: 'app-root',               // A tag HTML customizada que representa este componente
  templateUrl: './app.component.html', // O arquivo que cuida do visual
  styleUrls: ['./app.component.css']    // O arquivo que cuida dos estilos
})
export class AppComponent implements OnInit {

  // =========================================================================
  // PROPRIEDADES (Atributos da Classe)
  // =========================================================================

  // Uma variável simples do tipo texto (string)
  title: string = 'financas-front';

  // Injetamos o nosso TransacaoService usando a função moderna inject()
  private transacaoService = inject(TransacaoService);

  // Um objeto estruturado para simular os dados consolidados do usuário.
  // No TypeScript, objetos usam a estrutura de chave: valor entre chaves {}.
  // O objeto do saldo (ele será recalculado à medida que as transações chegarem)
  resumoSaldo = {
    usuario: 'Vanessa Machado Araújo',
    saldoAtual: 4610.50,
    status: 'Positivo'
  };

  // Iniciamos a  lista vazia, aguardando a resposta da API Java
  transacoes: any[] = [];

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
   * Implementamos a interface 'OnInit' e escrevemos o método 'ngOnInit()'.
   * Ele é um "Lifecycle Hook" (Gancho de Ciclo de Vida) do Angular que executa automaticamente
   * assim que a tela acaba de carregar no navegador. É o lugar perfeito para buscar os dados iniciais do banco.
   */
  ngOnInit(): void {
    this.carregarTransacoes();
  }

  /**
   * Método que chama o serviço, busca todas as transações e atualiza a tela e o saldo.
   */
  carregarTransacoes(): void {
    // Como vimos, o listarTodas() retorna um Observable (uma promessa de dados futuros).
    // Para capturar o resultado que o Java vai enviar quando a requisição terminar,
    // precisamos nos "inscrever" usando o método .subscribe().
    // É como assinar uma newsletter: toda vez que houver novos dados, o código dentro do subscribe executa!
    this.transacaoService.listarTodas().subscribe({
      next: (dadosDoJava) => {
        // Quando a API responde com sucesso, salvamos a lista na nossa variável
        this.transacoes = dadosDoJava;
        // Recalculamos o saldo com base nos dados reais que vieram do banco
        this.recalcularSaldo();
      },
      error: (erro) => {
        // Se a API estiver offline ou der erro (ex: 500), tratamos aqui para não travar a tela
        console.error('Erro ao buscar transações do backend Java:', erro);
        alert('Não foi possível carregar as transações. Verifique se o seu backend Java está rodando!');
      }
    });
  }

  /**
   * Coleta as variáveis preenchidas no formulário e envia para salvar no banco via POST.
   */
  adicionarTransacao(): void {
    if (!this.novaDescricao || this.novaDescricao.trim() === '' || !this.novoValor || this.novoValor <= 0) {
      alert('Por favor, preencha todos os campos corretamente!');
      return;
    }

    const nova = {
      descricao: this.novaDescricao,
      valor: this.novoValor,
      tipo: this.novoTipo,
      data: new Date().toISOString().substring(0, 10) // Data de hoje formatada
    };

    this.transacaoService.salvar(nova).subscribe({
      next: () => {
        // 1. O banco salvou! Agora buscamos a lista atualizada do Java para atualizar a tela na hora
        this.carregarTransacoes();
        // 2. Limpa os campos do formulário
        this.limparFormulario();
      },
      error: (erro) => {
        console.error('Erro ao salvar transação:', erro);
        alert('Erro ao salvar no banco de dados!');
      }
    });
  }

  /**
   * Exclui a transação do banco de dados real via DELETE.
   */
  excluirTransacao(transacao: any): void {
    this.transacaoService.excluir(transacao.id).subscribe({
      next: () => {
        // O banco excluiu! Recarregamos a lista para refletir a remoção e recalcular o saldo na tela
        this.carregarTransacoes();
      },
      error: (erro) => {
        console.error('Erro ao excluir transação:', erro);
        alert('Erro ao excluir do banco de dados!');
      }
    });
  }

  /**
   * Método auxiliar para somar as receitas e subtrair as despesas vindas do banco
   */
  private recalcularSaldo(): void {
    let saldo = 0;
    this.transacoes.forEach(t => {
      if (t.tipo === 'RECEITA') {
        saldo += t.valor;
      } else {
        saldo -= t.valor;
      }
    });
    this.resumoSaldo.saldoAtual = saldo;
  }

  private limparFormulario(): void {
    this.novaDescricao = '';
    this.novoValor = null;
    this.novoTipo = 'RECEITA';
  }
}
