import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../core/api.service';

interface Rep {
  id: string; titulo: string; params: { nombre: string; etiqueta: string }[];
}

@Component({
  selector: 'app-reportes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="card">
    <h2>📊 Reportes (CLI Menu:5, Cons1–Cons10)</h2>
    <div class="toolbar">
    <label>Reporte:
      <select [(ngModel)]="sel" name="sel" (change)="vals={}; filas=[]">
        <option *ngFor="let r of reps" [value]="r.id">{{ r.titulo }}</option>
      </select>
    </label>
    </div>
    <form class="form" (ngSubmit)="buscar()" *ngIf="actual()">
      <div class="form-row">
        <span *ngFor="let p of actual()!.params">
          <input [(ngModel)]="vals[p.nombre]" [name]="p.nombre" [placeholder]="p.etiqueta">
        </span>
      </div>
      <div><button>🔍 Buscar</button></div>
    </form>
    <p class="error" *ngIf="error">{{ error }}</p>
    <table *ngIf="filas.length">
      <tr><th *ngFor="let k of columnas()">{{ k }}</th></tr>
      <tr *ngFor="let f of filas"><td *ngFor="let k of columnas()">{{ f[k] }}</td></tr>
    </table>
    <p class="empty" *ngIf="buscado && !filas.length && !error">Sin resultados.</p>
    </div>
  `,
})
export class ReportesComponent {
  private api = inject(ApiService);
  reps: Rep[] = [
    { id: 'ingreso', titulo: 'Cons1: info de un ingreso', params: [{ nombre: 'folio', etiqueta: 'Folio' }] },
    { id: 'ingresos-paciente', titulo: 'Cons2: ingresos de un paciente', params: [{ nombre: 'paciente', etiqueta: 'Núm. paciente' }] },
    { id: 'misma-habitacion', titulo: 'Cons3: misma habitación', params: [{ nombre: 'habitacion', etiqueta: 'Núm. habitación' }] },
    { id: 'medicos-especialidad', titulo: 'Cons4: médicos por especialidad', params: [{ nombre: 'especialidad', etiqueta: 'Ej. Cardiología' }] },
    { id: 'especialidad-medico', titulo: 'Cons5: especialidad de un médico', params: [{ nombre: 'medico', etiqueta: 'Núm. médico' }] },
    { id: 'medicos-de-paciente', titulo: 'Cons6: médicos que atendieron', params: [{ nombre: 'paciente', etiqueta: 'Núm. paciente' }] },
    { id: 'ingresos-dia', titulo: 'Cons7: ingresos del día (YYYY-MM-DD)', params: [{ nombre: 'fecha', etiqueta: 'Fecha' }] },
    { id: 'menores-activos', titulo: 'Cons8: menores de 13 activos', params: [] },
    { id: 'pacientes-por-medico', titulo: 'Cons9: pacientes por médico', params: [] },
    { id: 'ingresos-especialidad', titulo: 'Cons10: ingresos por especialidad', params: [{ nombre: 'especialidad', etiqueta: 'Nombre o vacío si das código' }, { nombre: 'codigo', etiqueta: 'Código ESPxx (opcional)' }] },
  ];
  sel = 'ingreso';
  vals: Record<string, string> = {};
  filas: any[] = [];
  error = ''; buscado = false;

  actual() { return this.reps.find((r) => r.id === this.sel); }
  columnas() { return this.filas.length ? Object.keys(this.filas[0]) : []; }

  buscar() {
    this.error = ''; this.buscado = false;
    const p: Record<string, string> = {};
    for (const [k, v] of Object.entries(this.vals)) if (v) p[k] = v;
    this.api.reporte(this.sel, p).subscribe({
      next: (r) => { this.filas = r; this.buscado = true; },
      error: (e) => (this.error = e?.error?.message ?? 'Error. ¿Backend en :8080?'),
    });
  }
}
