module.exports = (nota, pathExcel) => {
    const xl = require('excel4node')
    const path = require('path')
    
    const date = new Date()  
    const fechaDia = date.getUTCDate()
    const fechaMes = (date.getUTCMonth()) + 1
    const fechaAno =  date.getUTCFullYear()
    const fechaHora =  date.getUTCHours()
    const fechaMinutos =  date.getUTCMinutes()

    //console.log('Nota 1 a escribir: ', nota)

    //console.log('Fecha: ', date)

    const Nota = nota
    const idNota = Nota.id
    const realizo = `${Nota.operario.nombre} ${Nota.operario.apellidopat} ${Nota.operario.apellidomat}`
    const fechaCreacion = Nota.fecha_creacion
    const numPacas = Nota.numero_pacas
    const pesoXPaca = Nota.peso_paca 
    const pesoTotal = Nota.peso_total 
    const comentarios = Nota.comentarios 
    const costoXKilo = Nota.costo_kilo
    const costoTotal = Nota.costo_total
    const asigno = `${Nota.administrativo.nombre} ${Nota.administrativo.apellidopat} ${Nota.administrativo.apellidomat}`
    const cliente = Nota.cliente.razon_social
    const clienteDireccion = Nota.cliente.direccion
    const fechaAprobacion = Nota.fecha_aceptacion
     
    // console.log('objeto NOTA', Nota)
    // console.log('Id nota', Nota.id)
    // console.log('cliente nota', Nota.cliente.razon_social)
    // const idOperario = operario.no_empleado
    // console.log('ID NOTA', idNota)

    var wb = new xl.Workbook()
    let nombreArchivo = `nota#${idNota}_${fechaDia}_${fechaMes}_${fechaAno}_${fechaHora}_${fechaMinutos}.xlsx`
    
    var ws = wb.addWorksheet(`Nota #${idNota}`)


    ws.cell(1,1).string(`Nota #${idNota}`)
    ws.cell(1,3).string('Fecha')
    ws.cell(1,4).string(fechaAprobacion)
    ws.cell(2,1).string('Operario que elaboró:')
    ws.cell(2,2).string(realizo)

    ws.cell(3,1).string('Caracteristicas de las pacas')

    ws.cell(4,1).string('Cantidad de pacas:')
    ws.cell(4,2).string(`${numPacas} unidad(s)`)
    ws.cell(4,3).string('Peso total:')
    ws.cell(4,4).string(`${pesoTotal} Kilogramos`)


    ws.cell(5,1).string('Peso por paca:')
    ws.cell(5,2).string(`${pesoXPaca} Kilogramos`)

    ws.cell(5,3).string('Costo por kilo:')
    ws.cell(5,4).string(`$${costoXKilo}`)
    ws.cell(6,3).string('Costo Total:')
    ws.cell(6,4).string(`$${costoTotal}`)

    ws.cell(7,1).string('Comentarios:')
    ws.cell(7,2).string(comentarios)

    ws.cell(8,1).string('Datos del cliente')

    ws.cell(9,1).string('Razón social:')
    ws.cell(9,2).string(cliente)
    ws.cell(10,1).string('Dirección:')
    ws.cell(10,2).string(clienteDireccion)

    ws.cell(12,1).string('Asigno:')
    ws.cell(12,2).string(asigno)

    ws.cell(12,3).string('Firma:')

    ws.cell(15,1).string('Firma de recibido')
   


    ///ruta del archivo 
    const pathxl = path.join(pathExcel, nombreArchivo )

    wb.write(pathxl, function(err,  stat){
        if(err) console.log(`Error: ${err}`) 

        else{
            console.log('Archvo creado')
            console.log('ruta del archivo ',  pathxl)
        }
    })

    
}



