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
    usuario: 'Vanessa Machado',
    saldoAtual: 4610.50,
    status: 'Positivo'
  };
}
