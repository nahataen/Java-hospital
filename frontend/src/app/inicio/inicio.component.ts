import { Component, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ApiService } from '../core/api.service';

@Component({
  selector: 'app-inicio',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="card">
      <h2>🏥 Hospital La Esperanza</h2>
      <p class="muted">Gestión ligada: cada <b>paciente</b> muestra sus <b>expedientes</b> y cada expediente sus <b>tratamientos</b>, médico, habitación y servicios.</p>
      <div class="grid3">
        <div class="stat"><b>{{ nPac }}</b><span>Pacientes</span></div>
        <div class="stat"><b>{{ nExp }}</b><span>Expedientes</span></div>
        <div class="stat"><b>{{ nTra }}</b><span>Tratamientos</span></div>
      </div>
    </div>
    <div class="grid2">
      <div class="card">
        <h3>Flujo de trabajo</h3>
        <ol>
          <li><b>Pacientes</b>: registra al paciente y abre su ficha integral.</li>
          <li><b>Expedientes</b>: crea el ingreso (habitación, médico, servicios).</li>
          <li><b>Tratamientos</b>: prescribe medicamentos por folio de expediente.</li>
          <li><b>Reportes</b>: los 10 reportes Cons1–Cons10 del CLI (Menu:5).</li>
        </ol>
        <p><a routerLink="/pacientes"><button>Ver pacientes y fichas</button></a></p>
      </div>
      <div class="card">
        <h3>Accesos rápidos</h3>
        <p class="toolbar">
          <a routerLink="/expedientes"><button class="ghost">＋ Nuevo expediente</button></a>
          <a routerLink="/tratamientos"><button class="ghost">＋ Nueva prescripción</button></a>
          <a routerLink="/reportes"><button class="ghost">📊 Reportes</button></a>
        </p>
        <p class="muted">Backend: <code>http://localhost:8080/api</code> · Endpoints ligados: <code>/pacientes/&#123;n&#125;/ficha</code>, <code>/expedientes/&#123;folio&#125;/detalle</code>.</p>
        <p class="error" *ngIf="error">{{ error }}</p>
      </div>
    </div>
  `,
})
export class InicioComponent implements OnInit {
  private api = inject(ApiService);
  nPac = '—'; nExp = '—'; nTra = '—';
  error = '';
  ngOnInit() {
    this.api.pacientes().subscribe({ next: (r) => (this.nPac = String(r.length)), error: () => (this.error = 'Backend no disponible en :8080') });
    this.api.expedientes().subscribe({ next: (r) => (this.nExp = String(r.length)) });
    this.api.tratamientos().subscribe({ next: (r) => (this.nTra = String(r.length)) });
  }
}
