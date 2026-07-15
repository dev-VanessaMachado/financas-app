/// <reference types="jasmine" /> //

import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { TransacaoService } from './transacao.service';

// 1. FORÇA O INTELLIJ A IMPORTAR A FUNÇÃO EXPECT DO JASMINE REAL:
const expect = (globalThis as any).expect;
describe('TransacaoService', () => {
  let service: TransacaoService;
  let httpMock: HttpTestingController; // Simula as respostas do nosso backend Java

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        TransacaoService,
        provideHttpClient(), // Fornece o cliente HTTP real do Angular
        provideHttpClientTesting() // Fornece o mock para interceptar e testar as chamadas HTTP
      ]
    });

    service = TestBed.inject(TransacaoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  // Garante que não sobrou nenhuma requisição HTTP pendente após cada teste
  afterEach(() => {
    httpMock.verify();
  });

  it('deve ser criado com sucesso', () => {
    expect(service).not.toBeNull();
  });

  it('deve buscar todas as transacoes via GET', () => {
    // Dados fictícios que fingiremos que o Javalin devolveu
    const transacoesFalsas = [
      { id: 1, descricao: 'Salário', valor: 3000, tipo: 'RECEITA' },
      { id: 2, descricao: 'Luz', valor: 150, tipo: 'DESPESA' }
    ];

    // Chamamos o método do nosso serviço e nos inscrevemos para validar o resultado
    service.listarTodas().subscribe(transacoes => {
      expect(transacoes.length).toBe(2);
      expect(transacoes).toEqual(transacoesFalsas);
    });

    // Interceptamos a chamada HTTP que o serviço tentou fazer para a nossa API
    const req = httpMock.expectOne('http://localhost:8080/api/transacoes');
    expect(req.request.method).toBe('GET');

    // Respondemos a requisição com os nossos dados falsos (simulando o Java)
    req.flush(transacoesFalsas);
  });
});
