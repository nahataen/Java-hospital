CREATE DATABASE IF NOT EXISTS `hospitalizaciones` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `hospitalizaciones`;

create table contactos(
    numero int primary key,
    nombre varchar(30) not null,
    apellido_paterno varchar (30),
    apellido_materno varchar(30),
    numero_telefono varchar(15) not null,
    correo_electronico varchar (60),
    dir_cp varchar(20) not null,
    dir_colonia varchar(50) not null,
    dir_calle varchar(40) not null,
    dir_num_casa varchar(10) not null 
);
create table servicios(
    numero int primary key,
    nombre varchar (40) not null,
    descripcion varchar(100) not null
);
-- este numero sirve para mostrar el historial del paciente  de cuantos expedientes tiene como si fuera un numero de seguro pero privado
create table pacientes(
    numero int primary key,
    nombre varchar(30)not null,
    apellido_paterno varchar(30),
    apellido_materno varchar(30),
    numero_telefono varchar(15) not null,
    correo_electronico varchar(60),
    dir_cp varchar(20) not null,
    dir_colonia varchar(50) not null,
    dir_calle varchar(40) not null,
    dir_num_casa varchar(10) not null,
    fecha_de_nacimiento date not null
);
create table especialidades(
    codigo varchar(5) primary key, 
    nombre varchar(60) not null
);
create table habitaciones(
    numero int primary key,
    nombre varchar(30) not null,
    numero_de_cama int
);
create table medicos(
    numero int primary key,
    nombre_doctor varchar(30) not null,
    apellidoP varchar(30),
    apellidoM varchar(30),
    numero_tel varchar (15) not null,
    rfc varchar (13) unique not null,
    horario varchar(30) not null    
);
create table ViaAdministraciones(
    codigo varchar(5) primary key,
    descripcion varchar(20) not null
);
create table presentaciones(
    codigo varchar(5) primary key,
    nombre varchar(20) not null,
    ViaAdministracion varchar(5),
    foreign key(ViaAdministracion) references ViaAdministraciones(codigo)
);
create table medicamentos(
    codigo varchar(5) primary key,
    nombre varchar(30) not null,
    unidad_medida varchar(10) not null,
    presentacion varchar(5),
    foreign key(presentacion) references presentaciones(codigo)
);
create table pacientes_contacto(
 paciente int,
 contacto int,
 primary key(paciente,contacto),
 foreign key (paciente) references pacientes(numero),
 foreign key (contacto) references contactos(numero)
);
create table medicos_especialidad(
    medico int,
    especialidad varchar(5),
    primary key(medico,especialidad),
    foreign key(medico) references medicos(numero),
    foreign key(especialidad) references especialidades(codigo)
);
create table expedientes(
folio int primary key auto_increment,
edad int not null,
sintomas varchar(300) not null,
peso double not null,
altura double not null,
diagnosticos varchar(300)not null,
fecha_de_ingreso date not null,
numero_de_cama int  not null,
fecha_de_alta date,
paciente int not null,
habitacion int not null,
medico int not null,
foreign key (paciente) references pacientes(numero),
foreign key (habitacion) references habitaciones(numero),
foreign key (medico) references medicos(numero)
);
-- segundo insert desde java

create table tratamientos(
    numero int primary key auto_increment,
    dosis varchar(35) not null,
    tiempo_dias int not null,
    expediente int,
    medicamento varchar(5),
    foreign key (expediente) references expedientes(folio),
    foreign key (medicamento) references medicamentos (codigo)
);
--tercer insert java

create table exped_servi(
    servicio int,
    expediente int,
    foreign key(servicio) references servicios(numero),
    foreign key(expediente) references expedientes(folio)
);
--dos

create table expedientes_especialidad(
    especialidad varchar(5),
    expediente int,
    primary key(especialidad,expediente),
    foreign key(especialidad) references especialidades(codigo),
    foreign key(expediente) references expedientes(folio)
   
);
-- RELLENADO DE DATOS

-- 🌟🌟🌟 CONTACTOS 🌟🌟🌟 --
 

INSERT INTO contactos (numero, nombre, apellido_paterno, apellido_materno, numero_telefono, correo_electronico, dir_cp, dir_colonia, dir_calle,dir_num_casa)
VALUES
(1,'Jorden','Hernandez','Carmona','(664) 855-4334','jorden930@outlook.com','86265-12646','Benjamin','Apdo.:658-2276 Ante. Av.',68755),
(2,'Patience','Garcia','Vazquez','(664) 824-1427','patience@outlook.org','73450-24633','Stephen','Apdo.:191-2111 In C.',61745),
(3,'Isaiah','Rodriguez','Andres','(664) 763-2947','isaiah9239@protonmail.edu','94827-41098','Beverly','7097 Sed Carretera',13383),
(4,'Price','Redondo','Velasco','(664) 256-5626','price9808@yahoo.org','83455-23495','Amity','1079 Iaculis ',45823),
(5,'Rafael','Pardo','Herrera','(664) 626-1152','rafael@google.net','81647-98129',' Mason','Apdo 203-7338 Odio Avenida',49664),
(6,'Pedro','Salas','Gonzales','(664) 696-1852','Pedro@gmail.net','81645-98529','el niño','Velasques','D7'),
(7,'Cristian','Pulido','Sanches','(664) 620-1232','Cristian@gmail.net','91647-78128','Refujio','De las aguas','12');
-- 🌟🌟🌟 INSERTAR VALORES A LA TABLA SERVICIOS 🌟🌟🌟 --

INSERT INTO servicios (numero, nombre, descripcion) VALUES
(1, 'Ecocardiografía', 'Estudio por imágenes del corazón mediante ultrasonido.'),
(2, 'Pediatría Preventiva', 'Atención médica especializada para niños y adolescentes.'),
(3, 'Tratamiento de Cáncer', 'Terapias oncológicas para combatir el cáncer en sus diferentes etapas.'),
(4, 'Ginecología y Obstetricia', 'Cuidado y asesoramiento en salud reproductiva y embarazo.'),
(5, 'Neurocirugía', 'Intervenciones quirúrgicas del sistema nervioso central y periférico.'),
(6, 'Dermatología Estética', 'Procedimientos para mejorar la apariencia y salud de la piel.'),
(7, 'Psiquiatría de Adultos', 'Evaluación y tratamiento de trastornos mentales en adultos.'),
(8, 'Medicina Interna Avanzada', 'Manejo de enfermedades complejas en pacientes adultos.'),
(9, 'Oftalmología Refractiva', 'Cirugías láser para corregir problemas de visión.'),
(10, 'Rehabilitación Ortopédica', 'Programas de terapia física para recuperación de lesiones musculoesqueléticas.');
-- 🌟🌟🌟 INSERTAR VALORES EN TABLA PACIENTES 🌟🌟🌟 --

INSERT INTO pacientes (numero,nombre,apellido_paterno,apellido_materno,numero_telefono,correo_electronico,dir_cp,dir_colonia,dir_calle,dir_num_casa,fecha_de_nacimiento)
VALUES
  (1,'Amanda','Leon','Ruiz','(664) 218-2237','a.leon691@yahoo.com','90175','Manuel','Francisca',5544,'1978-08-26'),
  (2,'Upton','Santana','Ibañez','(664) 471-2155','No tiene','1439','Adrià','Maite',3841,'2008-8-10'),
  (3,'Acton','Vazquez','Hernandez','(664) 966-3073','No tiene','35325','Niño','Antonia',5635,'2013-09-11'),
  (4,'Blossom','Garcia','Vazquez','(664) 347-3422','blossom_garcia9841@icloud.com','2137','Juanma','Mariana',4159,'2000-12-10'),
  (5,'Dara','Ortiz','Medina','(664) 917-2384','ortiz.dara9995@google.com','77116','Napoleon','Consuelo',4709,'1998-10-2'),
  (6,'jessica','martines','peralta','55432344','peralta@hotmail.com','22231','flor','trinidad','43','2011-12-01'),
  (7,'martin','soto','sanches','5433456','martin@gmail.com','45432','refugio','jicama','533','2011-11-02');
-- 🌟🌟🌟 INSERTAR VALORES EN TABLA ESPECIALIDADES 🌟🌟🌟 --

INSERT INTO especialidades (codigo, nombre) VALUES
('ESP01', 'Cardiología'),
('ESP02', 'Pediatría'),
('ESP03', 'Oncología'),
('ESP04', 'Ginecología'),
('ESP05', 'Neurología'),
('ESP06', 'Dermatología'),
('ESP07', 'Psiquiatría'),
('ESP08', 'Medicina Interna'),
('ESP09', 'Oftalmología'),
('ESP10', 'Ortopedia');
-- 🌟🌟🌟 INSERTAR VALORES EN TABLA HABITACIONES 🌟🌟🌟 --

INSERT INTO habitaciones (numero, nombre,numero_de_cama) VALUES
(1, 'Habitación Cardiología 1',3),
(2, 'Habitación Cardiología 2',0),
(3, 'Habitación Pediatría 1',2),
(4, 'Habitación Pediatría 2',3),
(5, 'Habitación Oftalmología 1',3),
(6, 'Habitación Oftalmología 2',3),
(7, 'Habitación Ginecología 1',3),
(8, 'Habitación Dermatología 1',3),
(9, 'Habitación Neurología 1',3),
(10, 'Habitación Oncología 1',2),
(11, 'Habitación Traumatología 1',3),
(12, 'Habitación Traumatología 2',3),
(13, 'Habitación Psiquiatría 1',3),
(14, 'Habitación Psiquiatría 2',3),
(15, 'Habitación Endocrinología 1',3);
--11 y 12 ortopedistas

-- 🌟🌟🌟 INSERTAR VALORES TABLA MEDICOS 🌟🌟🌟 --

INSERT INTO medicos (numero, nombre_doctor, apellidoP, apellidoM, numero_tel, rfc, horario) VALUES
(1, 'Juan', 'García', 'Pérez', '(664)-123-4567', 'ABCD123456EFG', 'Lunes 8:00 AM - 4:00 PM'),  
(2, 'María', 'López', 'Sánchez', '(664)-789-1234', 'HJKL789012MNP', 'Martes 9:00 AM - 5:00 PM'),  
(3, 'Carlos', 'Martínez', 'Gómez', '(663)-456-7890', 'QRST345678UVW', 'Miércoles 7:30 AM - 3:30 PM'),  
(4, 'Laura', 'Rodríguez', 'Romero', '(664)-567-8901', 'XYZW901234RST', 'Jueves 10:00 AM - 6:00 PM'),
(5, 'Alejandro', 'Hernández', 'Ortega', '(664)-321-0987', 'LMNO567890PQR', 'Viernes 8:30 AM - 4:30 PM'),
(6, 'Ana', 'González', 'Castro', '(663)-876-5432', 'STUV123456XYZ', 'Sábado 9:30 AM - 5:30 PM'),
(7, 'Pedro', 'Ramírez', 'Ruiz', '(664)-234-5678', 'EFGH789012ABC', 'Domingo 10:30 AM - 6:30 PM'),
(8, 'Isabel', 'Torres', 'Rivas', '(663)-890-1234', 'IJKL345678MNO', 'Lunes 7:00 AM - 3:00 PM'),
(9, 'Manuel', 'Flores', 'Méndez', '(664)-567-8901', 'PQRW901234XYZ', 'Martes 8:30 AM - 4:30 PM'), 
(10, 'Gabriela', 'Díaz', 'Miranda', '(663)-345-6789', 'MNOP567890LMN', 'Miércoles 9:00 AM - 5:00 PM'), 
(11, 'Andrés', 'Vargas', 'Espinoza', '(663)-234-5678', 'UVWX123456STU', 'Jueves 7:30 AM - 3:30 PM'),
(12, 'Patricia', 'Cruz', 'Camacho', '(664)-456-7890', 'YZAB789012CDE', 'Viernes 10:30 AM - 6:30 PM'),
(13, 'Ricardo', 'Reyes', 'Soto', '(664)-987-6543', 'FGHJ345678KLM', 'Sábado 8:00 AM - 4:00 PM'),
(14, 'Sofía', 'Morales', 'Paredes', '(663)-123-4567', 'NOPQ901234RST', 'Domingo 9:00 AM - 5:00 PM'),
(15, 'Luis', 'Delgado', 'Beltrán', '(663)-789-1234', 'UVWX567890YZA', 'Lunes 7:30 AM - 3:30 PM');
-- 🌟🌟🌟 Insertar datos en la tabla ViaAdministraciones 🌟🌟🌟 --

INSERT INTO ViaAdministraciones (codigo, descripcion) VALUES
    ('VIA01', 'Oral'),
    ('VIA02', 'Intravenosa'),
    ('VIA03', 'Intramuscular'),
    ('VIA04', 'Topica'),
    ('VIA05', 'Subcutanea'),
    ('VIA06', 'Rectal'),
    ('VIA07', 'Inhalada'),
    ('VIA08', 'Intradermal'),
    ('VIA09', 'Intracardiaca'),
    ('VIA10', 'Intratecal');
-- 🌟🌟🌟 Insertar valores en tabla presentacion 🌟🌟🌟 --

INSERT INTO presentaciones (codigo, nombre, ViaAdministracion) VALUES
('PRE1', 'Tableta', 'VIA01'),
('PRE2', 'Jarabe', 'VIA04'),
('PRE3', 'Crema', 'VIA03'),
('PRE4', 'Inyección', 'VIA02'),
('PRE5', 'Supositorio', 'VIA06'),
('PRE6', 'Solución', 'VIA05'),
('PRE7', 'Aerosol', 'VIA07'),
('PRE8', 'Gel', 'VIA03'),
('PRE9', 'Emulsión', 'VIA05'),
('PRE10', 'Suspensión', 'VIA02');
-- ⭐⭐⭐ Insertar datos en la tabla medicamentos ⭐⭐⭐ --

INSERT INTO medicamentos (codigo, nombre, unidad_medida, presentacion) VALUES
('MD1', 'Paracetamol', '500 mg', 'PRE1'),
('MD2', 'Ibuprofeno', '200 mg', 'PRE2'),
('MD3', 'Amoxicilina', '250 mg', 'PRE3'),
('MD4', 'Loratadina', '10 mg', 'PRE4'),
('MD5', 'Omeprazol', '20 mg', 'PRE1'),
('MD6', 'Dipirona', '500 mg', 'PRE6'),
('MD7', 'Atorvastatina', '40 mg', 'PRE7'),
('MD8', 'Metformina', '850 mg', 'PRE8'),
('MD9', 'Salbutamol', '100 mcg', 'PRE9'),
('MD10', 'Insulina', '50 UI', 'PRE10'),
('MD11', 'Ciprofloxacina', '500 mg', 'PRE1'),
('MD12', 'Dexametasona', '4 mg', 'PRE2'),
('MD13', 'Enalapril', '5 mg', 'PRE3'),
('MD14', 'Paroxetina', '20 mg', 'PRE4'),
('MD15', 'Bromuro de Ipratropio', '500 mcg', 'PRE5'),
('MD16', 'Ranitidina', '150 mg', 'PRE6'),
('MD17', 'Hidroclorotiazida', '25 mg', 'PRE7'),
('MD18', 'Naproxeno', '500 mg', 'PRE8'),
('MD19', 'Aspirina', '25 mcg', 'PRE9'),
('MD20', 'Morfina', '10 mg', 'PRE10'),
('MD21', 'Azilsartán', '80 mg', 'PRE1');
-- ⭐⭐⭐ Insertar datos en la tabla pacientes_contacto ⭐⭐⭐ -- 

INSERT INTO pacientes_contacto (paciente, contacto) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(7, 7);
-- ⭐⭐⭐ Insertar datos en la tabla medicos_especialidad ⭐⭐⭐ --

INSERT INTO medicos_especialidad (medico,especialidad)
VALUES
(1,'ESP01'),
(2,'ESP02'),
(3,'ESP03'),
(4,'ESP04'),
(5,'ESP05'),
(6,'ESP05'),
(7,'ESP06'),
(7,'ESP08'),
(8,'ESP09'),
(9,'ESP10'),
(10,'ESP01'),
(11,'ESP05'),
(12,'ESP02'),
(13,'ESP02'),
(14,'ESP06'),
(15,'ESP04'),
(7,'ESP03');
-- ⭐⭐⭐ Insertar datos en la tabla expedientes ⭐⭐⭐ --

INSERT INTO expedientes (folio, edad, sintomas, peso, altura, diagnosticos, fecha_de_ingreso, numero_de_cama, fecha_de_alta, paciente, habitacion, medico) VALUES
  (1, 45, 'Dolor torácico, Dificultad respiratoria', 90, 168, ' Infarto de miocardio', '2023-07-22', 1, '2023-10-11', 1, 1, 1), 
  (2, 15, 'Fiebre alta, Dolor de garganta', 40, 165, 'Faringitis', '2023-07-27', 1, '2023-08-25', 2, 3, 2),
  (3, 10, 'Dolor de garganta, Erupción cutánea', 44, 140, 'Escarlatina', '2023-07-27', 3, null, 3, 3, 2),  
  (4, 23, 'Visión nula', 65, 170, ' Cataratas, requiere cirugía de cornía', '2023-07-11', 1, '2023-11-20', 4, 5, 8),
  (5, 25, 'Dolor en rodilla, Dificultad para caminar', 56, 175.5, 'Lesión de ligamento cruzado requiere cirugía', '2023-07-04', 1, '2023-12-03', 5, 11, 9),
  (6, 12, 'problemas del corazon siente dolor de pecho', 28, 142.5, 'pre infarto', '2023-08-10', 1, null, 6, 2, 12),
  (7, 12, 'presenta sintomas de dolor de pecho nauseas vomito','24','135.5','pre infarto','2023-08-11',2,null,7,2,13),
  (8, 15, 'Dolor en el estomago', 40, 165,'Infección en el estomago','2023-08-11',1,'2023-08-12',2,4,13),
  (9, 25, 'Bultos o masas en el cuerpo', 46, 175.5,'Posible cancer de piel','2023-08-11',1,null,5,10,7),
  (10, 15, 'Dolor de cabeza', 40, 165,'Migraña leve','2023-08-13',3,null,2,2,12);
--Update expedientes set fecha_de_alta=null where folio=3
--1 cardiologia
--1 pediatra
--2 pediatra
--ofmatologia 
-- ortopediatra/traumatología   esto forma parte de la tabla expedientes asi en orden de linea  ^
--dentro de medico va la especialidad⚠️  👈--

-- ⭐⭐⭐ Insertar datos en la tabla tratamientos ⭐⭐⭐ --

--Segun expediente es el folio del expediente. Se relaciona con la tabla expediente---
--Y en medicamento Es el número de identificación del medicamento. Se relaciona con la tabla medicamentos-----
--AHI COMO VEAN SI SE CAMBIAN ESOS DATOS-----








INSERT INTO tratamientos (numero, dosis, tiempo_dias, expediente, medicamento)VALUES
('1', '1 cucharada cada 8 horas', '7', 1, 'MD19'),
('2', '1 tabletas cada 12 horas', '10', 2, 'MD1'),
('3', '1 cucharada cada 12 horas', '10', 2, 'MD3'),
('4', '1 inyección diaria', '5', 3, 'MD18'),
('5', '1 comprimido antes de cada comida', '30', 3, 'MD3'),
('6', '2 tabletas antes de dormir', '14', 3, 'MD4'),
('7', '1 cápsula cada 6 horas', '3', 4, 'MD20'),
('8', '1 cucharadita cada 8 horas', '7', 4, 'MD12'),
('9', '1 tableta diaria', '21', 5, 'MD20'),
('10', '1 inyección semanal', 28, 5, 'MD12'),
('11', '1 tableta cada 4 horas', '5',5, 'MD11'),
('12', '1 cápsula cada 6 horas', '3', 8, 'MD20'),
('13', 'Una tableta cada 8 horas', '3',10,'MD1'),
('14', '2 tabletas cada 8 horas', '7', 6, 'MD19'),
('15', '2 tabletas cada 12 horas', '4', 6, 'MD21'),
('16', '1 tabletas cada 12 horas', '2', 6, 'MD1');
-- ⭐⭐⭐ Insertar datos en la tabla exped_servi ⭐⭐⭐ --

INSERT INTO exped_servi (servicio, expediente) VALUES 
(1,1),
(2,2),
(2,3),
(9,4),
(10,5),
(1,6),
(2,6),
(2,7),
(2,8),
(3,9),
(2,10);
/*LOS DATOS INGRESADOS SON DE ACUERDO A:
   ESPECIALIDAD: Es el código con  el que se identifica la especialidad,
    está ligada a la tabla especialidades.
    EXPEDIENTE: Es el número con el cual se identifica el expediente de un paciente,
     está ligada a  la tabla expediente.
*/
-- ⭐⭐⭐ Insertar datos en la tabla expedientes_especialidad ⭐⭐⭐ --

INSERT INTO expedientes_especialidad (especialidad, expediente)VALUES 
('ESP01', 1),
('ESP02', 2),
('ESP02', 3),
('ESP09', 4),
('ESP10', 5),
('ESP01', 6),
('ESP02', 6),
('ESP02', 7),
('ESP02', 8),
('ESP03',9),
('ESP02', 10);
