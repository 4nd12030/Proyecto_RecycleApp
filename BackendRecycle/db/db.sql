

CREATE TABLE roles(
	id BIGSERIAL PRIMARY KEY,
	nombre VARCHAR(180) NOT NULL,
	imagen VARCHAR(255) NULL,
	ruta VARCHAR (255) NULL,
	created_at TIMESTAMP(0) NOT NULL,
	update_at TIMESTAMP(0) NOT NULL		
);



INSERT INTO roles(
	nombre,
	imagen,
	ruta,
	created_at,
	update_at
)
VALUES(
	'OPERARIO',
	'https://cdn-icons-png.flaticon.com/512/7425/7425445.png',
	'operario/home',
	'2024-02-12',
    '2024-02-12'	
);

INSERT INTO roles(
	nombre,
	imagen,
	ruta,
	created_at,
	update_at
)
VALUES(
	'ADMINISTRATIVO',
	'https://cdn-icons-png.flaticon.com/512/2640/2640747.png',
	'administrativo/home',
	'2024-02-12'
	'2024-02-12'	
);

INSERT INTO roles(
	nombre,
	imagen,
	ruta,
	created_at,
	update_at
)
VALUES(
	'SOPORTE',
	'https://cdn-icons-png.flaticon.com/512/5358/5358679.png',
	'soporte/home',
	'2024-02-12',
	'2024-02-12'	
);

DROP TABLE IF EXISTS clientes CASCADE;
CREATE TABLE clientes(
	id BIGSERIAL PRIMARY KEY,
    razon_social VARCHAR(255) NOT NULL,
	created_at TIMESTAMP(0) NOT NULL,
	update_at TIMESTAMP(0) NOT NULL
);


DROP TABLE IF EXISTS cliente_tiene_notas CASCADE;
CREATE TABLE cliente_tiene_notas(
	id_cliente BIGINT NOT NULL,
	id_nota BIGINT NOT NULL,
	id_operario VARCHAR(255) NOT NULL,
	id_administrativo VARCHAR(255) NOT NULL,
	created_at TIMESTAMP(0) NOT NULL,
	update_at TIMESTAMP(0),
	FOREIGN KEY(id_cliente) REFERENCES clientes(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(id_nota) REFERENCES notas(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(id_operario) REFERENCES empleados(no_empleado) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(id_administrativo) REFERENCES empleados(no_empleado) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY(id_nota, id_operario, id_administrativo)
);




-------------------------------

DROP TABLE IF EXISTS notas CASCADE;
CREATE TABLE notas(
	id BIGSERIAL PRIMARY KEY,
    id_operario VARCHAR(255) NOT NULL,
	fecha_creacion TIMESTAMP(0) NOT NULL,
	numero_pacas BIGINT NULL,
	peso_paca BIGINT NULL,
	peso_total BIGINT NULL,
	comentarios VARCHAR(255) NULL,
	id_administrativo VARCHAR(255) NULL,
	id_cliente BIGINT NULL,
	costo_kilo DECIMAL DEFAULT 0.0,
	costo_total DECIMAL DEFAULT 0.0,
	fecha_aceptacion TIMESTAMP(0) NULL,
	fecha_entrega TIMESTAMP(0) NULL,
	estado  VARCHAR(255) NULL, 
	FOREIGN KEY(id_operario) REFERENCES empleados(no_empleado) ON UPDATE CASCADE ON DELETE CASCADE,	
	FOREIGN KEY(id_administrativo) REFERENCES empleados(no_empleado) ON UPDATE CASCADE ON DELETE CASCADE	
);



DROP TABLE IF EXISTS empleados CASCADE;
CREATE TABLE empleados(
	id BIGSERIAL NOT NULL,
	no_empleado VARCHAR(255) PRIMARY KEY,
	nombre VARCHAR(255) NOT NULL,
	apellidoPat VARCHAR(255) NOT NULL,
    apellidoMat VARCHAR(255) NOT NULL,
	imagen VARCHAR(255) NULL,
    telefono VARCHAR(80) NOT NULL UNIQUE,
	contrasena VARCHAR(255) NOT NULL,
	is_available BOOLEAN NULL,
	session_token VARCHAR(255) NULL,
	created_at TIMESTAMP(0) NOT NULL,
	update_at TIMESTAMP(0) NOT NULL	
);




DROP TABLE IF EXISTS empleado_tiene_roles CASCADE;
CREATE TABLE empleado_tiene_roles(
	id_empleado VARCHAR(255) NOT NULL,
	id_rol BIGINT NOT NULL,
	created_at TIMESTAMP(0) NOT NULL,
	update_at TIMESTAMP(0) NOT NULL,
	FOREIGN KEY(id_empleado) REFERENCES empleados(no_empleado) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(id_rol) REFERENCES roles(id) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY(id_empleado, id_rol)
);



DROP TABLE IF EXISTS cliente_tiene_ordenes CASCADE;
CREATE TABLE cliente_tiene_ordenes(
	id_cliente BIGINT NOT NULL,
	id_nota BIGINT NOT NULL,
	id_operario VARCHAR(255) NOT NULL,
	id_administrativo VARCHAR(255) NOT NULL,
	created_at TIMESTAMP(0) NOT NULL,
	update_at TIMESTAMP(0) NOT NULL,
	FOREIGN KEY(id_cliente) REFERENCES clientes(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(id_nota) REFERENCES notas(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(id_operario) REFERENCES empleados(no_empleado) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(id_administrativo) REFERENCES empleados(no_empleado) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY(id_cliente, id_nota, id_operario, id_administrativo)
);
---------------------------
   INSERT INTO 
    notas( 
        id_operario, 
        fecha_creacion,
        numero_pacas,
        peso_paca,
        peso_total,
        comentarios,
        estado    
    )
	VALUES ('0000', '2023-10-26 19:14:31' ,2, 50, 100, 'Sin comentarios','Guardada');  