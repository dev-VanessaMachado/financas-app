import { TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';
import { TransacaoService } from './transacao.service';

describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        AppComponent // Como ele não é standalone, ele DEVE ficar aqui nas declarations!
      ],
      imports: [
        FormsModule
      ],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        TransacaoService // Fornece o seu serviço com segurança para o teste
      ]
    }).compileComponents();
  });

  it('deve criar a aplicação com sucesso', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });
});
