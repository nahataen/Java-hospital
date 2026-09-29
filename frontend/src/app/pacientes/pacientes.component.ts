import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../core/api.service';

@Component({
  selector: 'app-pacientes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="card">
      <h2>👥 Pacientes y ficha integral</h2>
      <p class="muted">Selecciona un paciente para ver su <b>ficha ligada</b>: datos personales, expedientes, tratamientos, médico, habitación y servicios.</p>
      <div class="toolbar">
        <input [(ngModel)]="busqueda" name="q" placeholder="🔍 Buscar por nombre, apellido o número…" (input)="filtrada()">
        <button class="ghost" (click)="cargar()">↻ Recargar</button>
      </div>
      <p class="error" *ngIf="error">{{ error }}</p>
      <table *ngIf="vista.length">
        <tr><th>Núm.</th><th>Paciente</th><th>Teléfono</th><th>Nacimiento</th><th></th></tr>
        <tr *ngFor="let p of vista" [class.sel]="sel === p.numero">
          <td>{{ p.numero }}</td>
          <td><b>{{ p.nombre }} {{ p.apellidoPaterno }} {{ p.apellidoMaterno }}</b><br>
            <span class="muted">{{ p.correoElectronico ?? '' }}</span></td>
          <td>{{ p.numeroTelefono }}</td>
          <td>{{ p.fechaNacimiento }}</td>
          <td><button (click)="verFicha(p.numero)">Ver ficha</button></td>
        </tr>
      </table>
      <p class="empty" *ngIf="!vista.length && !error">Sin pacientes. Registra el primero abajo.</p>
    </div>

    <div class="card" *ngIf="ficha">
      <div class="ficha-head">
        <div class="avatar">{{ inicial(ficha.paciente) }}</div>
        <div>
          <h3 style="margin:0">{{ ficha.paciente.nombre }} {{ ficha.paciente.apellidoPaterno }} {{ ficha.paciente.apellidoMaterno }}</h3>
          <span class="muted">Paciente #{{ ficha.paciente.numero }} · {{ ficha.edad ?? '—' }} años · {{ ficha.paciente.numeroTelefono }}</span>
        </div>
        <span style="margin-left:auto" class="toolbar">
          <span class="stat"><b>{{ ficha.totalExpedientes }}</b><span>expedientes</span></span>
          <span class="stat"><b>{{ ficha.expedientesActivos }}</b><span>activos</span></span>
          <span class="stat"><b>{{ ficha.totalTratamientos }}</b><span>tratamientos</span></span>
        </span>
      </div>
      <dl class="kv">
        <dt>Correo</dt><dd>{{ ficha.paciente.correoElectronico ?? '—' }}</dd>
        <dt>Dirección</dt><dd>{{ ficha.paciente.dirCalle ?? '' }} {{ ficha.paciente.dirNumCasa ?? '' }}, {{ ficha.paciente.dirColonia ?? '' }} CP {{ ficha.paciente.dirCp ?? '—' }}</dd>
        <dt>Nacimiento</dt><dd>{{ ficha.paciente.fechaNacimiento }}</dd>
        <dt>Contactos</dt><dd>
          <span *ngIf="!ficha.contactos?.length" class="muted">Sin contactos ligados</span>
          <span class="tag" *ngFor="let c of ficha.contactos">{{ c.nombre }} {{ c.apellidoPaterno }} · {{ c.numeroTelefono }}</span>
        </dd>
      </dl>
      <h3>Expedientes ({{ ficha.expedientes.length }})</h3>
      <p class="empty" *ngIf="!ficha.expedientes.length">Este paciente aún no tiene expediente. Créalo en la pestaña Expedientes con su número.</p>
      <div class="exp" *ngFor="let d of ficha.expedientes">
        <div class="exp-head">
          <b>Folio {{ d.expediente.folio }}</b>
          <span class="badge activo" *ngIf="d.activo">● Activo</span>
          <span class="badge alta" *ngIf="!d.activo">Alta {{ d.expediente.fechaAlta }}</span>
          <span class="muted">Ingreso {{ d.expediente.fechaIngreso }} · Cama {{ d.expediente.numeroDeCama }}</span>
          <button class="ghost" *ngIf="d.activo" (click)="alta(d.expediente.folio)">Dar de alta</button>
        </div>
        <dl class="kv">
          <dt>Médico</dt><dd>{{ d.medicoNombre }}</dd>
          <dt>Habitación</dt><dd>{{ d.habitacionNombre }}</dd>
          <dt>Servicios</dt><dd>
            <span class="tag" *ngFor="let s of d.servicios">{{ s.nombre }}</span>
            <span class="muted" *ngIf="!d.servicios.length">—</span>
          </dd>
          <dt>Síntomas</dt><dd>{{ d.expediente.sintomas ?? '—' }}</dd>
          <dt>Diagnóstico</dt><dd>{{ d.expediente.diagnosticos ?? '—' }}</dd>
        </dl>
        <b>Tratamientos ({{ d.tratamientos.length }})</b>
        <table *ngIf="d.tratamientos.length">
          <tr><th>Núm.</th><th>Medicamento</th><th>Dosis</th><th>Días</th></tr>
          <tr *ngFor="let t of d.tratamientos">
            <td>{{ t.tratamiento.numero }}</td>
            <td>{{ t.medicamentoNombre }} <span class="muted">({{ t.tratamiento.medicamento }})</span></td>
            <td>{{ t.tratamiento.dosis }}</td>
            <td>{{ t.tratamiento.tiempoDias }}</td>
          </tr>
        </table>
        <p class="muted" *ngIf="!d.tratamientos.length">Sin prescripciones en este folio.</p>
      </div>
    </div>

    <div class="card">
      <h3>Nuevo paciente</h3>
      <form class="form" (ngSubmit)="crear()">
        <div class="form-row">
          <input [(ngModel)]="f.nombre" name="nombre" placeholder="Nombre*" required maxlength="30">
          <input [(ngModel)]="f.numeroTelefono" name="tel" placeholder="Teléfono*" required maxlength="15">
        </div>
        <div class="form-row">
          <input [(ngModel)]="f.apellidoPaterno" name="ap" placeholder="Apellido paterno" maxlength="30">
          <input [(ngModel)]="f.apellidoMaterno" name="am" placeholder="Apellido materno" maxlength="30">
        </div>
        <div class="form-row">
          <input [(ngModel)]="f.correoElectronico" name="mail" placeholder="Correo" maxlength="60" type="email">
          <input [(ngModel)]="f.fechaNacimiento" name="fn" type="date" required>
        </div>
        <div class="form-row">
          <input [(ngModel)]="f.dirCp" name="cp" placeholder="CP (solo dígitos)" pattern="[0-9]+">
          <input [(ngModel)]="f.dirColonia" name="col" placeholder="Colonia">
        </div>
        <div class="form-row">
          <input [(ngModel)]="f.dirCalle" name="calle" placeholder="Calle">
          <input [(ngModel)]="f.dirNumCasa" name="num" placeholder="Núm. casa">
        </div>
        <div><button>💾 Guardar paciente</button></div>
      </form>
    </div>
  `,
})
export class PacientesComponent implements OnInit {
  private api = inject(ApiService);
  lista: any[] = [];
  vista: any[] = [];
  busqueda = '';
  error = '';
  f: any = {};
  sel?: number;
  ficha: any = null;

  ngOnInit() { this.cargar(); }

  cargar() {
    this.api.pacientes().subscribe({
      next: (r) => { this.lista = r; this.filtrada(); },
      error: (e) => (this.error = this.msg(e)),
    });
  }

  filtrada() {
    const q = this.busqueda.trim().toLowerCase();
    if (!q) { this.vista = this.lista; return; }
    this.vista = this.lista.filter((p) =>
      `${p.numero} ${p.nombre} ${p.apellidoPaterno ?? ''} ${p.apellidoMaterno ?? ''}`.toLowerCase().includes(q));
  }

  verFicha(n: number) {
    this.sel = n; this.error = '';
    this.api.fichaPaciente(n).subscribe({
      next: (r) => (this.ficha = r),
      error: (e) => (this.error = this.msg(e)),
    });
  }

  alta(folio: number) {
    this.api.darAlta(folio).subscribe({
      next: () => { if (this.sel) this.verFicha(this.sel); },
      error: () => (this.error = 'No se pudo dar de alta'),
    });
  }

  crear() {
    this.error = '';
    this.api.crearPaciente(this.f).subscribe({
      next: (p) => { this.f = {}; this.cargar(); this.verFicha(p.numero); },
      error: (e) => (this.error = this.msg(e)),
    });
  }

  inicial(p: any) { return (p?.nombre?.[0] ?? '?').toUpperCase(); }

  private msg(e: any) {
    return e?.error?.message ?? e?.error ?? e?.message ?? 'Error contra el backend. ¿Corre en :8080?';
  }
}
