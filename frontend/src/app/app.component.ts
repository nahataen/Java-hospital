import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <header class="topbar">
      <div class="logo">✚</div>
      <div>
        <h1>Hospital La Esperanza</h1>
        <small>Sistema de hospitalizaciones · Spring Boot + Angular</small>
      </div>
      <nav>
        <a routerLink="/" routerLinkActive="active" [routerLinkActiveOptions]="{ exact: true }">Inicio</a>
        <a routerLink="/pacientes" routerLinkActive="active">Pacientes</a>
        <a routerLink="/expedientes" routerLinkActive="active">Expedientes</a>
        <a routerLink="/tratamientos" routerLinkActive="active">Tratamientos</a>
        <a routerLink="/reportes" routerLinkActive="active">Reportes</a>
      </nav>
    </header>
    <main><router-outlet /></main>
    <div class="footer">Paciente → Expediente → Tratamiento ligados · Backend :8080 · Frontend :4200</div>
  `,
})
export class AppComponent {}
