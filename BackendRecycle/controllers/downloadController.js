
const Nota = require('../models/nota')
const path = require('path')
const xl = require('excel4node')
const fs = require('fs')



module.exports = {
   
async getNotaDownlaod(req, res, next){
    try {
        const date = new Date()  
        const fechaDia = date.getUTCDate()
        const fechaMes = (date.getUTCMonth()) + 1
        const fechaAno =  date.getUTCFullYear()
        const fechaHora =  date.getUTCHours()
        const fechaMinutos =  date.getUTCMinutes()

        const id_operario = req.params.id_operario
        const id_administrativo = req.params.id_administrativo
        const status = req.params.status
        const data = await Nota.findByStatusAprobada(id_operario, id_administrativo, status)

        const idNota = data.id
        const realizo = `${data.operario.nombre} ${data.operario.apellidopat} ${data.operario.apellidomat}`
        const fechaCreacion = data.fecha_creacion
        const numPacas = data.numero_pacas
        const pesoXPaca = data.peso_paca 
        const pesoTotal = data.peso_total 
        const comentarios = data.comentarios 
        const costoXKilo = data.costo_kilo
        const costoTotal = data.costo_total
        const asigno = `${data.administrativo.nombre} ${data.administrativo.apellidopat} ${data.administrativo.apellidomat}`
        const cliente = data.cliente.razon_social
        const clienteDireccion = data.cliente.direccion
        const fechaAprobacion = data.fecha_aceptacion


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
        const pathExcel = path.join(__dirname, 'excel', nombreArchivo )
  
        wb.write(pathExcel, function(err,  stat){
           if(err) console.log(`Error: ${err}`) 

          else{
            downloadFile=() => {
                res.download(pathExcel) 
                console.log('Archvo creado')
                console.log('ruta del archivo ',  pathExcel)
            }
            downloadFile()
            
            // fs.rm(pathExcel, function(err){
            //     if(err) console.log(err)
            //     else console.log('Archvio descargado y borrado')
            // })
           }    
           
        })
                     
    }
    catch (error) {
        console.log(`Error: ${error}`) 
        return res.status(501).json({
            succes: false,
            message: 'Error al obtener las notas'
        })
    }

},


}



