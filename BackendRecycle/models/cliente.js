const db = require('../config/config')

const  Cliente = {}

Cliente.getAll = (is_available) => {
    const sql = `
    SELECT * 
    FROM
        clientes
    WHERE 
        is_available = $1
    `
    //console.log('Sentencia', sql)
    //console.log('Respuesta de la Sentencia', db.manyOrNone(sql) )

    return db.manyOrNone(sql, is_available)
}


Cliente.getClientesDesactivados = () => {
    const sql = `
    SELECT * 
    FROM
        clientes
    WHERE 
        is_available = false
    `
    //console.log('Sentencia', sql)
    //console.log('Respuesta de la Sentencia', db.manyOrNone(sql) )

    return db.manyOrNone(sql)
}


Cliente.addCliente =  (cliente) =>  {
    const sql = `
    INSERT INTO 
    clientes(
        razon_social,
        direccion, 
        telefono, 
        created_at)
    VALUES ($1,$2,$3,$4);    
    `
    return db.none(sql, [ 
        cliente.razon_social, 
        cliente.direccion, 
        cliente.telefono,
        new Date()
    ])

}


Cliente.setNota =  (nota) =>  {
    const sql = `
    INSERT INTO 
    cliente_tiene_notas(
        id_cliente,
        id_nota, 
        id_operario, 
        id_administrativo, 
        created_at)
    VALUES ($1,$2,$3,$4,$5);    
    `

return db.none(sql, [ 
    nota.id_cliente, 
    nota.id,
    nota.id_operario,
    nota.id_administrativo,
    new Date()
])

}

Cliente.updateSetNota = (nota) =>  {
    const sql = `
    UPDATE cliente_tiene_notas
	SET 
	  id_cliente=$2,  
	  id_operario=$3, 
	  id_administrativo=$4,
      update_at = $5
	WHERE id_nota = $1;    
    `
    return db.none(sql, [ 
        nota.id,
        nota.id_cliente, 
        nota.id_operario,
        nota.id_administrativo,
        new Date()
    ])

}



Cliente.updateCliente =  (cliente)  => {

    const sql = `
    UPDATE clientes
    SET
        razon_social = $2,  
        direccion = $3, 
        telefono = $4,
        update_at = $5
    WHERE id = $1; 
    `

    console.log('cliente', cliente)

    return db.none(sql, [
        cliente.id,
        cliente.razon_social,
        cliente.direccion,
        cliente.telefono,
        new Date()
    ])
}

Cliente.desactivarCliente =  (id_cliente)  => {

    const sql = `
    UPDATE clientes
    SET
        is_available = $2,
        fecha_desactivacion = $3
    WHERE id = $1; 
    `

    console.log('id_cliente:', id_cliente)

    return db.none(sql, [
        id_cliente,
        false,
        new Date()
    ])
}

Cliente.reactivarCliente =  (id_cliente)  => {

    const sql = `
    UPDATE clientes
    SET
        is_available = $2
    WHERE id = $1; 
    `

    console.log('id_cliente:', id_cliente)

    return db.none(sql, [
        id_cliente,
        true
    ])
}



module.exports = Cliente