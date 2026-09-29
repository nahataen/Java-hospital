import { Routes } from '@angular/router';
import { InicioComponent } from './inicio/inicio.component';
import { PacientesComponent } from './pacientes/pacientes.component';
import { ExpedientesComponent } from './expedientes/expedientes.component';
import { TratamientosComponent } from './tratamientos/tratamientos.component';
import { ReportesComponent } from './reportes/reportes.component';

export const routes: Routes = [
  { path: '', component: InicioComponent },
  { path: 'pacientes', component: PacientesComponent },
  { path: 'expedientes', component: ExpedientesComponent },
  { path: 'tratamientos', component: TratamientosComponent },
  { path: 'reportes', component: ReportesComponent },
  { path: '**', redirectTo: '' },
];
