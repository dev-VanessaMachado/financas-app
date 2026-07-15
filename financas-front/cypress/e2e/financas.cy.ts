describe('Painel de Finanças - Teste E2E', () => {

  beforeEach(() => {
    // 1. O robô vai abrir o nosso frontend Angular
    cy.visit('http://localhost:4200');
  });

  it('Deve preencher o formulário e cadastrar uma nova receita com sucesso', () => {
    // 2. O robô digita a descrição
    cy.get('input').eq(0).type('Freelance Cypress');

    // 3. O robô digita o valor
    cy.get('input').eq(1).type('2000');

    // 4. O robô seleciona o tipo (Receita)
    cy.get('select').select('Receita (Entrada)');

    // 5. O robô clica no botão verde de salvar!
    cy.contains('button', 'Salvar Transação').click();

    // 6. PROVA DE FOGO: O robô valida se a nova transação apareceu no extrato na tela!
    cy.contains('Freelance Cypress').should('be.visible');
    cy.contains('+ R$ 2000').should('be.visible');
  });
});
