import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../core/api.service';

@Component({
  selector: 'app-expedientes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="card">
      <h2>📋 Expedientes</h2>
      <p class="muted">Cada expediente pertenece a un paciente y acumula sus tratamientos. Clic en “Detalle” para ver todo ligado.</p>
      <div class="toolbar">
        <input [(ngModel)]="filtro" name="filtro" type="number" placeholder="Filtrar por núm. de paciente" (change)="cargar()">
        <button class="ghost" (click)="filtro = undefined; cargar()">Limpiar</button>
      </div>
      <p class="error" *ngIf="error">{{ error }}</p>
      <table *ngIf="lista.length">
        <tr><th>Folio</th><th>Paciente</th><th>Ingreso</th><th>Estado</th><th></th></tr>
        <tr *ngFor="let e of lista" [class.sel]="det?.expediente?.folio === e.folio">
          <td><b>{{ e.folio }}</b></td><td>{{ e.paciente }}</td>
          <td>{{ e.fechaIngreso }}</td>
          <td>
            <span class="badge activo" *ngIf="!e.fechaAlta">● Activo</span>
            <span class="badge alta" *ngIf="e.fechaAlta">Alta {{ e.fechaAlta }}</span>
          </td>
          <td>
            <button class="ghost" (click)="verDetalle(e.folio)">Detalle</button>
            <button class="ok" *ngIf="!e.fechaAlta" (click)="alta(e.folio)">Dar de alta</button>
          </td>
        </tr>
      </table>
      <p class="empty" *ngIf="!lista.length && !error">Sin expedientes para ese filtro.</p>
    </div>

    <div class="card" *ngIf="det">
      <h3>Folio {{ det.expediente.folio }} · Paciente #{{ det.expediente.paciente }}</h3>
      <dl class="kv">
        <dt>Médico</dt><dd>{{ det.medicoNombre }}</dd>
        <dt>Habitación</dt><dd>{{ det.habitacionNombre }}</dd>
        <dt>Servicios</dt><dd><span class="tag" *ngFor="let s of det.servicios">{{ s.nombre }}</span></dd>
        <dt>Síntomas</dt><dd>{{ det.expediente.sintomas ?? '—' }}</dd>
        <dt>Diagnóstico</dt><dd>{{ det.expediente.diagnosticos ?? '—' }}</dd>
        <dt>Peso / Altura</dt><dd>{{ det.expediente.peso ?? '—' }} kg · {{ det.expediente.altura ?? '—' }} m</dd>
      </dl>
      <b>Tratamientos ligados ({{ det.tratamientos.length }})</b>
      <table *ngIf="det.tratamientos.length">
        <tr><th>Núm.</th><th>Medicamento</th><th>Dosis</th><th>Días</th></tr>
        <tr *ngFor="let t of det.tratamientos">
          <td>{{ t.tratamiento.numero }}</td>
          <td>{{ t.medicamentoNombre }}</td>
          <td>{{ t.tratamiento.dosis }}</td>
          <td>{{ t.tratamiento.tiempoDias }}</td>
        </tr>
      </table>
    </div>

    <div class="card">
      <h3>Nuevo expediente</h3>
      <form class="form" (ngSubmit)="crear()">
        <div class="form-row">
          <input [(ngModel)]="f.paciente" name="pac" type="number" placeholder="Núm. paciente*" required>
          <input [(ngModel)]="f.edad" name="edad" type="number" placeholder="Edad (opcional, se calcula)">
        </div>
        <div class="form-row">
          <select [(ngModel)]="f.habitacion" name="hab" required>
            <option [ngValue]="undefined">Habitación*</option>
            <option *ngFor="let h of habs" [ngValue]="h.numero">{{ h.numero }} - {{ h.nombre }} (camas: {{ h.numeroDeCama }})</option>
          </select>
          <select [(ngModel)]="f.medico" name="med" required>
            <option [ngValue]="undefined">Médico*</option>
            <option *ngFor="let m of meds" [ngValue]="m.numero">{{ m.numero }} - {{ m.nombreDoctor }} {{ m.apellidoP }}</option>
          </select>
        </div>
        <label class="muted">Servicios (Ctrl/Cmd + clic para varios):</label>
        <select [(ngModel)]="f.servicios" name="srv" multiple size="5" required>
          <option *ngFor="let s of srvs" [ngValue]="s.numero">{{ s.numero }} - {{ s.nombre }}</option>
        </select>
        <input [(ngModel)]="f.sintomas" name="sin" placeholder="Síntomas (máx 280)" maxlength="280">
        <input [(ngModel)]="f.diagnosticos" name="dg" placeholder="Diagnóstico (máx 200)" maxlength="200">
        <div class="form-row">
          <input [(ngModel)]="f.peso" name="peso" type="number" step="0.1" placeholder="Peso (kg)">
          <input [(ngModel)]="f.altura" name="alt" type="number" step="0.1" placeholder="Altura (m)">
        </div>
        <div><button>💾 Guardar expediente</button></div>
      </form>
    </div>
  `,
})
export class ExpedientesComponent implements OnInit {
  private api = inject(ApiService);
  lista: any[] = []; habs: any[] = []; meds: any[] = []; srvs: any[] = [];
  error = ''; filtro?: number;
  det: any = null;
  f: any = { servicios: [] };

  ngOnInit() {
    this.cargar();
    this.api.habitaciones().subscribe({ next: (r) => (this.habs = r) });
    this.api.medicos().subscribe({ next: (r) => (this.meds = r) });
    this.api.servicios().subscribe({ next: (r) => (this.srvs = r) });
  }

  cargar() {
    this.api.expedientes(this.filtro).subscribe({
      next: (r) => (this.lista = r), error: (e) => (this.error = 'Error. ¿Backend en :8080?'),
    });
  }

  verDetalle(folio: number) {
    this.api.detalleExpediente(folio).subscribe({
      next: (r) => (this.det = r), error: () => (this.error = 'No se pudo cargar el detalle'),
    });
  }

  crear() {
    this.error = '';
    this.api.crearExpediente(this.f).subscribe({
      next: () => { this.f = { servicios: [] }; this.cargar(); },
      error: (e) => (this.error = e?.error?.message ?? e?.message ?? 'Error al guardar'),
    });
  }

  alta(folio: number) {
    this.api.darAlta(folio).subscribe({ next: () => { this.cargar(); this.verDetalle(folio); }, error: () => (this.error = 'No se pudo dar de alta') });
  }
}
