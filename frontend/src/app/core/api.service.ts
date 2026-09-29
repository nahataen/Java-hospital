import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private http = inject(HttpClient);
  private base = 'http://localhost:8080/api';

  // Pacientes (Menu:1)
  pacientes() { return this.http.get<any[]>(`${this.base}/pacientes`); }
  crearPaciente(d: any) { return this.http.post<any>(`${this.base}/pacientes`, d); }
  expedientesDePaciente(n: number) { return this.http.get<any[]>(`${this.base}/pacientes/${n}/expedientes`); }
  /** Ficha ligada: paciente + expedientes + tratamientos + contactos. */
  fichaPaciente(n: number) { return this.http.get<any>(`${this.base}/pacientes/${n}/ficha`); }
  tratamientosDePaciente(n: number) { return this.http.get<any[]>(`${this.base}/pacientes/${n}/tratamientos`); }

  // Expedientes (Menu:2 alta=Menu:4)
  expedientes(paciente?: number) {
    return this.http.get<any[]>(`${this.base}/expedientes${paciente ? `?paciente=${paciente}` : ''}`);
  }
  detalleExpediente(folio: number) { return this.http.get<any>(`${this.base}/expedientes/${folio}/detalle`); }
  crearExpediente(d: any) { return this.http.post<any>(`${this.base}/expedientes`, d); }
  darAlta(folio: number) { return this.http.put<any>(`${this.base}/expedientes/${folio}/alta`, {}); }

  // Tratamientos (Menu:3)
  tratamientos(expediente?: number) {
    return this.http.get<any[]>(`${this.base}/tratamientos${expediente ? `?expediente=${expediente}` : ''}`);
  }
  crearTratamiento(d: any) { return this.http.post<any>(`${this.base}/tratamientos`, d); }

  // Catálogos
  servicios() { return this.http.get<any[]>(`${this.base}/servicios`); }
  habitaciones() { return this.http.get<any[]>(`${this.base}/habitaciones`); }
  medicamentos() { return this.http.get<any[]>(`${this.base}/medicamentos`); }
  especialidades() { return this.http.get<any[]>(`${this.base}/especialidades`); }
  medicos() { return this.http.get<any[]>(`${this.base}/medicos`); }

  // Reportes Cons1-10
  reporte(path: string, params: Record<string, string> = {}) {
    const q = new URLSearchParams(params).toString();
    return this.http.get<any[]>(`${this.base}/reportes/${path}${q ? `?${q}` : ''}`);
  }
}
