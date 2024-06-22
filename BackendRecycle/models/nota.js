
const db = require('../config/config')

const Nota = {}

Nota.getAll = () => {
    const sql = `
    SELECT * 
    FROM
        notas
    `
    //console.log('Sentencia', sql)
    //console.log('Respuesta de la Sentencia', db.manyOrNone(sql) )

    return db.manyOrNone(sql)
}

Nota.findByEmpleadoAndStatus = (num_empleado, status) => {
    const sql = `
    SELECT
       N.id,
       N.id_operario,
       N.fecha_creacion,
       N.numero_pacas,
       N.peso_paca,
       N.peso_total,
       N.comentarios,
       N.id_administrativo,
	   N.id_cliente,
       N.costo_kilo,
	   N.costo_total,
       N.estado,
       N.fecha_entrega,
       JSON_BUILD_OBJECT(
          'no_empleado',E.no_empleado,
          'nombre', E.nombre,
          'apellidopat', E.apellidopat,
          'apellidomat', E.apellidomat
       ) AS operario,
       JSON_BUILD_OBJECT(
            'no_empleado',EA.no_empleado,
            'nombre', EA.nombre,
            'apellidopat', EA.apellidopat,
            'apellidomat', EA.apellidomat
       ) AS administrativo,
       JSON_BUILD_OBJECT(
        'id',C.id,
        'razon_social', C.razon_social,
        'direccion', C.direccion
        ) AS cliente	   
    FROM 
       notas AS N
    INNER JOIN
       empleados AS E
   ON
       N.id_operario = E.no_empleado
    LEFT JOIN
      clientes AS C
    ON
      N.id_cliente = C.id
    LEFT JOIN
        empleados AS EA
    ON
        N.id_administrativo = EA.no_empleado
    WHERE
        N.id_operario = $1 AND N.estado =$2
    ORDER BY
        N.fecha_creacion DESC 
    `
    //console.log('Sentencia', sql)
    //console.log('Respuesta de la Sentencia', db.manyOrNone(sql) )

    return db.manyOrNone(sql, [num_empleado,status])
}


Nota.findByStatusAprobada = (id_operario, id_administrativo, status) => {
    const sql = `
    SELECT
       N.id,
       N.id_operario,
       N.fecha_creacion,
       N.numero_pacas,
       N.peso_paca,
       N.peso_total,
       N.comentarios,
       N.id_administrativo,
	   N.id_cliente,
       N.costo_kilo,
	   N.costo_total,
       N.fecha_aceptacion,
       N.estado,
       N.fecha_entrega,
       JSON_BUILD_OBJECT(
          'no_empleado',E.no_empleado,
          'nombre', E.nombre,
          'apellidopat', E.apellidopat,
          'apellidomat', E.apellidomat
       ) AS operario,
       JSON_BUILD_OBJECT(
            'no_empleado',EA.no_empleado,
            'nombre', EA.nombre,
            'apellidopat', EA.apellidopat,
            'apellidomat', EA.apellidomat
       ) AS administrativo,
       JSON_BUILD_OBJECT(
        'id',C.id,
        'razon_social', C.razon_social,
        'direccion', C.direccion
        ) AS cliente	   
    FROM 
       notas AS N
    INNER JOIN
       empleados AS E
   ON
       N.id_operario = E.no_empleado
    LEFT JOIN
      clientes AS C
    ON
      N.id_cliente = C.id
    LEFT JOIN
        empleados AS EA
    ON
        N.id_administrativo = EA.no_empleado
    WHERE
        N.id_operario = $1 AND N.id_administrativo = $2 AND N.estado = $3 
    ORDER BY
        N.fecha_creacion DESC 
    `
    //console.log('Sentencia', sql)
    //console.log('Respuesta de la Sentencia', db.manyOrNone(sql) )

    return db.oneOrNone(sql, [id_operario, id_administrativo,status])
}


Nota.findByStatus = (status) => {
    const sql = `
    SELECT
       N.id,
       N.id_operario,
       N.fecha_creacion,
       N.numero_pacas,
       N.peso_paca,
       N.peso_total,
       N.comentarios,
       N.id_administrativo,
	   N.id_cliente,
       N.costo_kilo,
	   N.costo_total,
       N.estado,
       N.fecha_entrega,
       N.estado,
       JSON_BUILD_OBJECT(
          'no_empleado',E.no_empleado,
          'nombre', E.nombre,
          'apellidopat', E.apellidopat,
          'apellidomat', E.apellidomat
       ) AS operario,
       JSON_BUILD_OBJECT(
        'no_empleado',EA.no_empleado,
        'nombre', EA.nombre,
        'apellidopat', EA.apellidopat,
        'apellidomat', EA.apellidomat
     ) AS administrativo,
     JSON_BUILD_OBJECT(
        'id',C.id,
        'razon_social', C.razon_social,
        'direccion', C.direccion
        ) AS cliente	
     FROM 
       notas AS N
    INNER JOIN
        empleados AS E
    ON
        N.id_operario = E.no_empleado
    LEFT JOIN
        empleados AS EA
    ON
        N.id_administrativo = EA.no_empleado
    LEFT JOIN
        clientes AS C
    ON
        N.id_cliente = C.id
    WHERE
        N.estado = $1
    ORDER BY
        N.fecha_creacion DESC 
    `
    //console.log('Sentencia', sql)
    //console.log('Respuesta de la Sentencia', db.manyOrNone(sql) )

    return db.manyOrNone(sql, [status])
}

Nota.findByAdministrativoAndStatus = (status,id_administrativo) => {
    const sql = `

    SELECT
       N.id,
       N.id_operario,
       N.fecha_creacion,
       N.numero_pacas,
       N.peso_paca,
       N.peso_total,
       N.comentarios,
       N.id_administrativo,
	   N.id_cliente,
       N.costo_kilo,
	   N.costo_total,
       N.estado,
       N.fecha_entrega,
       JSON_BUILD_OBJECT(
          'no_empleado',E.no_empleado,
          'nombre', E.nombre,
          'apellidopat', E.apellidopat,
          'apellidomat', E.apellidomat
       ) AS operario,
       JSON_BUILD_OBJECT(
        'no_empleado',EA.no_empleado,
        'nombre', EA.nombre,
        'apellidopat', EA.apellidopat,
        'apellidomat', EA.apellidomat
     ) AS administrativo,
	   JSON_BUILD_OBJECT(
          'id',C.id,
          'razon_social', C.razon_social,
          'direccion', C.direccion
       ) AS cliente	   
    FROM 
        notas AS N
	INNER JOIN
        clientes AS C
    ON
        N.id_cliente = C.id
    INNER JOIN
        empleados AS E
    ON
        N.id_operario = E.no_empleado
	INNER JOIN
        empleados AS EA
    ON
        N.id_administrativo = EA.no_empleado
    WHERE
        N.estado = $1 AND N.id_administrativo = $2
    ORDER BY
        N.fecha_creacion ASC 
    `
    //console.log('Sentencia', sql)
    //console.log('Respuesta de la Sentencia', db.manyOrNone(sql) )

    return db.manyOrNone(sql, [status, id_administrativo])
}



Nota.create = async (nota) => {

    const sql = `
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
        VALUES ($1,$2,$3,$4,$5,$6,$7)  RETURNING id;    
    `

    return db.oneOrNone(sql, [
        nota.id_operario,
        new Date(),
        nota.numero_pacas,
        nota.peso_paca,
        nota.peso_total,
        nota.comentarios,
        nota.estado
    ])
}


Nota.update = async (nota) => {

    const sql = `
    UPDATE 
        notas
    SET 
	    id_operario=$2,
	    fecha_creacion=$3, 
	    numero_pacas=$4, 
	    peso_paca=$5, 
	    peso_total=$6, 
	    comentarios=$7, 
	    id_administrativo=$8, 
	    id_cliente=$9, 
	    costo_kilo=$10, 
	    costo_total=$11, 
        estado = $12,
        fecha_aceptacion= $13
    WHERE
       id = $1  
    `

    return db.none(sql, [
        nota.id,
        nota.id_operario,
        new Date(),
        nota.numero_pacas,
        nota.peso_paca,
        nota.peso_total,
        nota.comentarios,
        nota.id_administrativo,
        nota.id_cliente,
        nota.costo_kilo,
        nota.costo_total,
        nota.estado,
        nota.fecha_aprobacion
    ])
}


Nota.updateById = async (id, estado) => {

    const sql = `
    UPDATE 
        notas
    SET 
        estado = $2,
        fecha_entrega = $3
    WHERE
       id = $1  
    `

    return db.none(sql, [id, estado, new Date()])
}



Nota.deleteNota = (id) => {
    const sql = `
    DELETE FROM
           notas
	WHERE 
           id = $1
    `
    //console.log('Sentencia', sql)
    //console.log('Respuesta de la Sentencia', db.manyOrNone(sql) )

    return db.manyOrNone(sql, [id])
}



module.exports = Nota