
//Archivo que guarda losdatos en la BD 

const db = require('../config/config')
const bcrypt = require('bcryptjs')

const Empleado = {}


//Retorna todos los usarios activos
Empleado.getAll = (is_available) => {
    const sql = `
    SELECT *
    FROM empleados
    WHERE is_available = $1
    ORDER BY no_empleado ASC `

    return db.manyOrNone(sql, is_available)
}

Empleado.findByNumEmpleado = (no_empleado) => {    
    const sql = `
    SELECT
        E.id,
        E.nombre,
        E.apellidopat,
        E.apellidomat,
        E.imagen,
        E.telefono,
        E.no_empleado,
        E.contrasena,
        E.session_token,
		json_agg(
		   json_build_object(
		       'id', R.id,
			   'nombre', R.nombre,
			   'imagen', R.imagen,
               'ruta', R. ruta
		   )
		) AS roles
    FROM
        empleados AS E
	INNER JOIN 
	    empleado_tiene_roles AS ETR
	ON
	   ETR.id_empleado = E.no_empleado
	INNER JOIN 
	    roles AS R
	ON
	   R.id = ETR.id_rol	  
	WHERE 
	   E.no_empleado = $1
	GROUP BY 
	   E.no_empleado
    `

    return db.oneOrNone(sql,no_empleado)
}

//consulta para obtenr el id del empleado
Empleado.findById = (id, callback) => {
    const sql = `
    SELECT
        id,
        nombre,
        apellidopat,
        apellidomat,
        imagen,
        telefono,
        no_empleado,
        contrasena,
        session_token
    FROM
        empleados
    WHERE
        id = $1
    `

    return db.oneOrNone(sql,id).then(empleado => { callback(null,  empleado) })
}

Empleado.create = async (empleado) => {

    const hash = await bcrypt.hash(empleado.contrasena, 10)

    const sql = `
    INSERT INTO 
    empleados(
        nombre,
        apellidopat,
        apellidomat,
        imagen,
        telefono,
        no_empleado,
        contrasena,
        created_at,
        update_at,
        is_available	
    )
    VALUES($1,$2,$3,$4,$5,$6,$7,$8,$9,$10) RETURNING id, no_empleado`

    return db.oneOrNone(sql, [
        empleado.nombre,
        empleado.apellidopat,
        empleado.apellidomat,
        empleado.imagen,
        empleado.telefono,
        empleado.no_empleado,
        //empleado.contrasena,
        hash,
        new Date(),
        new Date(),
        empleado.is_available
    ])
}

Empleado.update = (empleado) => {
    const sql = `
    UPDATE 
        empleados
    SET
       nombre = $2,
       apellidopat = $3,
       apellidomat = $4,
       imagen = $5,
       telefono = $6,
       update_at = $7
    WHERE
       no_empleado = $1   
    `

    console.log('empleado imagen', empleado.imagen )
    return db.none(sql, [
        empleado.no_empleado,
        empleado.nombre,
        empleado.apellidopat,
        empleado.apellidomat,
        empleado.imagen,
        empleado.telefono,
        new Date()
    ])
}

Empleado.updateSessionToken = (id_empleado, session_token) => {
   
    console.log('Empleado dentro de UpdateSessionToken: ', session_token)
    const sql = `
    UPDATE 
        empleados
    SET
       session_token = $2

    WHERE
       id = $1   
    `
    return db.none(sql, [
        id_empleado,
        session_token        
    ])
}

Empleado.findByRol = (id_rol) => {
    //console.log('Id Rol: ', id_rol)
    const sql = `
    SELECT 
        E.id,
        E.nombre,
        E.apellidopat,
        E.apellidomat,
        E.imagen,
        E.telefono,
        E.no_empleado
    FROM 
        empleados as E
    INNER JOIN 
        empleado_tiene_roles as ER
    ON  
        E.no_empleado = ER.id_empleado
    INNER JOIN
        roles as R
    ON 
        R.id = ER.id_rol
    WHERE 
        R.id = $1 and E.is_available = true
    `
    return db.manyOrNone(sql, id_rol)
} 

// Empleado.desactivarEmpleado = (no_empleado,fechaActualizacion,is_available) => {
Empleado.desactivarEmpleado = (no_empleado) => {

    const sql = `	
    UPDATE empleados
	SET 
        fecha_desactivacion = $2,
        is_available = $3
	WHERE
        no_empleado = $1
    `

    return db.none(sql, [
        no_empleado, 
        new Date(),
        false])
}

Empleado.reactivarEmpleado = (no_empleado) => {

    const sql = `	
    UPDATE empleados
	SET 
        is_available = $2
	WHERE
        no_empleado = $1
    `

    return db.none(sql, [
        no_empleado, 
        true])
}


//Retorna todos los usarios inactivos
Empleado.getAllInactive = () => {
    const sql = `
    SELECT *
    FROM empleados
    WHERE is_available = false`
    return db.manyOrNone(sql)
}



//Empleado.reactivarUser = (id)


module.exports = Empleado