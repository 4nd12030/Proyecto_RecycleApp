const db = require('../config/config')

const  Rol = {}

Rol.getAll = () => {
    const sql = `
    SELECT * 
    FROM
        roles
    `
    //console.log('Sentencia', sql)
    //console.log('Respuesta de la Sentencia', db.manyOrNone(sql) )

    return db.manyOrNone(sql)
}

Rol.create = (id_empleado, id_rol)  => {

    const sql = `
    INSERT INTO 
        empleado_tiene_roles(
            id_empleado,
            id_rol,
            created_at,
            update_at
        )
    VALUES($1,$2,$3,$4)    
    `

    return db.none(sql, [
        id_empleado,
        id_rol,
        new Date(),
        new Date()
    ])
}

Rol.createNewRol = (rol)  => {

    const sql = `
    INSERT INTO 
        roles(
            nombre,
            imagen,
            created_at,
            update_at
        )
    VALUES($1,$2,$3,$4)  
    `

    return db.none(sql, [
        rol.nombre,
        rol.imagen,
        new Date(),
        new Date()
    ])
}

module.exports = Rol