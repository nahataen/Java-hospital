import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../core/api.service';

@Component({
  selector: 'app-tratamientos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="card">
      <h2>💊 Tratamientos / Prescripciones</h2>
      <p class="muted">Cada prescripción cuelga de un folio de expediente, y cada folio de un paciente.</p>
      <div class="toolbar">
        <input [(ngModel)]="folio" name="folio" type="number" placeholder="Filtrar por folio" (change)="cargar()">
        <input [(ngModel)]="pac" name="pac" type="number" placeholder="O ver todo lo de un paciente #" (change)="porPaciente()">
        <button class="ghost" (click)="folio = undefined; pac = undefined; cargar()">Limpiar</button>
      </div>
      <p class="error" *ngIf="error">{{ error }}</p>
      <table *ngIf="lista.length">
        <tr><th>Núm.</th><th>Folio</th><th>Medicamento</th><th>Dosis</th><th>Días</th></tr>
        <tr *ngFor="let t of lista">
          <td>{{ t.numero }}</td><td>{{ t.expediente }}</td><td>{{ nombreMed(t.medicamento) }}</td>
          <td>{{ t.dosis }}</td><td>{{ t.tiempoDias }}</td>
        </tr>
      </table>
      <p class="empty" *ngIf="!lista.length && !error">Sin prescripciones para ese filtro.</p>
    </div>
    <div class="card">
      <h3>Nueva prescripción</h3>
      <form class="form" (ngSubmit)="crear()">
        <div class="form-row">
          <input [(ngModel)]="f.expediente" name="exp" type="number" placeholder="Folio*" required>
          <select [(ngModel)]="f.medicamento" name="md" required>
            <option [ngValue]="undefined">Medicamento*</option>
            <option *ngFor="let m of meds" [value]="m.codigo">{{ m.codigo }} - {{ m.nombre }}</option>
          </select>
        </div>
        <div class="form-row">
          <input [(ngModel)]="f.dosis" name="dosis" placeholder="Dosis (máx 35)*" required maxlength="35">
          <input [(ngModel)]="f.tiempoDias" name="dias" type="number" placeholder="Días*" required min="1">
        </div>
        <div><button>💾 Guardar prescripción</button></div>
      </form>
    </div>
  `,
})
export class TratamientosComponent implements OnInit {
  private api = inject(ApiService);
  lista: any[] = []; meds: any[] = [];
  error = ''; folio?: number; pac?: number;
  f: any = {};

  ngOnInit() {
    this.cargar();
    this.api.medicamentos().subscribe({ next: (r) => (this.meds = r) });
  }

  cargar() {
    this.api.tratamientos(this.folio).subscribe({
      next: (r) => (this.lista = r), error: () => (this.error = 'Error. ¿Backend en :8080?'),
    });
  }

  porPaciente() {
    if (!this.pac) { this.cargar(); return; }
    this.api.tratamientosDePaciente(this.pac).subscribe({
      next: (r) => { this.lista = r; this.folio = undefined; },
      error: () => (this.error = 'Paciente sin tratamientos o inexistente'),
    });
  }

  nombreMed(cod: string) {
    return this.meds.find((m) => m.codigo === cod)?.nombre ?? cod;
  }

  crear() {
    this.error = '';
    if (!this.f.expediente && this.folio) this.f.expediente = this.folio;
    this.api.crearTratamiento(this.f).subscribe({
      next: () => { this.f = {}; this.cargar(); },
      error: (e) => (this.error = e?.error?.message ?? 'Error al guardar'),
    });
  }
}
