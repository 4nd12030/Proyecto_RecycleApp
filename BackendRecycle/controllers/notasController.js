
const Nota = require('../models/nota')
const Cliente = require('../models/cliente')
const crearArchivo = require('../utils/crearArchivoNota')
const path = require('path')

module.exports = {


    async getAll(req, res, next) {
        try {
            const data = await Nota.getAll()
            console.log(`Nota: ${data}`)
            return res.status(201).json(data)
        }
        catch (error) {
            console.log(`Error: ${error}`) 
            return res.status(501).json({
                succes: false,
                message: 'Error al obtener las notas'
            })
        }
    },

    async findByEmpleadoAndStatus(req, res, next) {
        try {
            const num_empleado = req.params.num_empleado
            const status = req.params.status
            let data = await Nota.findByEmpleadoAndStatus(num_empleado,status)
            //console.log('Nota encontrada ',json(data))
     
            return res.status(201).json(data)
        }
        catch (error) {
            console.log(`Error: ${error}`) 
            return res.status(501).json({
                succes: false,
                message: 'Error al obtener las notas'
            })
        }
    },
    
    async findByStatusAprobada(req, res, next) {
        try {
            const id_operario = req.params.id_operario
            const id_administrativo = req.params.id_administrativo
            const status = req.params.status
            let data = await Nota.findByStatusAprobada(id_operario, id_administrativo, status)
            console.log('Nota encontrada ',data)
     
            return res.status(201).json(data)
        }
        catch (error) {
            console.log(`Error: ${error}`) 
            return res.status(501).json({
                succes: false,
                message: 'Error al obtener las notas'
            })
        }
    },
    
    async findByAdministrativoAndStatus(req, res, next) {
        try {
            const id_administrativo = req.params.id_administrativo
            const status = req.params.status
            console.log(status)
            console.log(id_administrativo)
            let data = await Nota.findByAdministrativoAndStatus(status,id_administrativo)

            console.log('Data: ', data)
     
            return res.status(201).json(data)
        }
        catch (error) {
            console.log(`Error: ${error}`) 
            return res.status(501).json({
                succes: false,
                message: 'Error al obtener las notas'
            })
        }
    },


    async findByStatus(req, res, next) {
        try {
            const status = req.params.status
            let data = await Nota.findByStatus(status)

            console.log('Notas:', data)
     
            return res.status(201).json(data)
        }
        catch (error) {
            console.log(`Error: ${error}`) 
            return res.status(501).json({
                succes: false,
                message: 'Error al obtener las notas'
            })
        }
    },

   
    async update(req, res, next) {
        try {
            let estado = 'GUARDADA'
            let comentarios = 'Sin comentarios'
            const nota = req.body
            console.log('Nota', nota)

            if( nota.estado == ''  ) {
                nota.estado = estado
            }
            
            if(nota.comentarios == ''){
                nota.comentarios = comentarios
            }
            console.log('Nota - estado', nota)
            
            await Nota.update(nota)
            
            return res.status(201).json({
                succes: true,
                message: 'Las nota ha sido guardada'
            })

        }
        catch (error) {
            console.log(`Error: ${error}`)
            return res.status(501).json({
                success: false,
                message: `Hubo un error al actualizar los datos del empleado`,
                error: error
            })
            
        }
    },

    async create(req, res, next){
        try{
            let estado = 'GUARDADA'
            let comentarios = 'Sin comentarios'
            
            console.log('REQ BODY: ', req.body)
            const nota = req.body
            console.log('Cometario: ', nota.comentarios)
            console.log('Resultado if: ', nota.comentarios)

            
            if( nota.estado == 'ENVIADA' ) {
                mensaje = 'La nota ha sido enviada al administrativo'
            } else {
                nota.estado = estado
                mensaje = 'La nota ha sido guardada'
            }

            if(nota.comentarios == ''){
                nota.comentarios = comentarios
            }
            console.log('Nota-Estado-comentario: ', nota)

            const data = await Nota.create(nota)
            console.log(data)

            return res.status(201).json({
                succes: true,
                message: mensaje,
                data: {
                    'id': data.id
                }
            })
        }
        catch(error){
            console.log(`Error: ${error}`)
            return res.status(501).json({
                success: false,
                message: 'Hubo un error al guardar la nota ',
                error: error
            })
        }
    },

    async updateNotaEnviada(req, res, next){
        try {
            let estado = 'ENVIADA'
            let comentarios = 'Sin comentarios'
            const nota = req.body

            nota.estado = estado
            if(nota.comentarios == ''){
                nota.comentarios = comentarios
            }
            console.log('orden: ', nota)
            
            await Nota.update(nota)

            return res.status(201).json({
                succes: true,
                message: "La nota ha sido enviada al administrativo",
            })


        } catch (error) {
            console.log(error)
            return res.status(501).json({
                succes: false,
                message: 'Hubo un error al actualizar la orden',
                error: error
            })
        }
    },

    
    async updateNotaPendiente(req, res, next){
        try {
            let estado = 'PENDIENTE'
            const nota = req.body
            console.log('orden 1: ', nota)

            if(nota.estado == 'PENDIENTE'){
                await Nota.update(nota)
                await Cliente.updateSetNota(nota) 
            } else {
                nota.estado = estado
                await Nota.update(nota)
                await Cliente.setNota(nota) 
            }
            
            console.log('orden: ', nota)
          
    
            return res.status(201).json({
                succes: true,
                message: "La nota ha sido guardada, puede aprobarla en otro momento",
            })


        } catch (error) {
            console.log(error)
            return res.status(501).json({
                succes: false,
                message: 'Hubo un error al guardar la orden',
                error: error
            })
        }
    },

    async updateNotaAprobada(req, res, next){
        try {
            const nota = req.body           
            console.log('nota: ', nota)
                        
            if(nota.estado == 'ENVIADA' ){
                await Cliente.setNota(nota)                  
            } else {
                await Cliente.updateSetNota(nota)
            }       
         
            nota.estado = 'APROBADA'
            nota.fecha_aprobacion = new Date()
            console.log('nota: ', nota)

            await Nota.update(nota)


            return res.status(201).json({
                succes: true,
                message: "La nota se actualizo correctamente",
            })


        } catch (error) {
            console.log(error)
            return res.status(501).json({
                succes: false,
                message: 'Hubo un error al actualizar la orden',
                error: error
            })
        }
    },

    async updateNotaEntregada(req, res, next){
        try {
            const idNota = req.params.id
            const estadoNuevo = 'ENTREGADA'
            console.log('Id orden: ', idNota)
            await Nota.updateById(idNota, estadoNuevo)

            return res.status(201).json({
                succes: true,
                message: "Entrega de pedido finalizada",
            })


        } catch (error) {
            console.log(error)
            return res.status(501).json({
                succes: false,
                message: 'Hubo un error al actualizar la orden',
                error: error
            })
        }
    },

    async deleteNota(req, res, next) {
        try {
            const id = req.params.id
            await Nota.deleteNota(id)
     
            return res.status(201).json(({
                succes: true,
                message: 'La nota ha sido descartada'
            }))
        }
        catch (error) {
            console.log(`Error: ${error}`) 
            return res.status(501).json({
                succes: false,
                message: 'No se pudo descartar la nota'
            })
        }
    },

    
} 
 



