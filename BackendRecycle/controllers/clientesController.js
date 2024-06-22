
const Cliente = require('../models/cliente')
const storage = require('../utils/cloud_storage')

module.exports = {

    async getAll(req, res, next) {
        
        try {
           //console.log('RESPUESTA CONSULTA: ', Rol.getAll())
           const is_available = req.params.is_available
           console.log(`is_available: ${req.params.is_available}`)
           const data = await Cliente.getAll(is_available)
           
           return res.status(201).json(data)
        }
        catch (error) {
            console.log(`Err: ${error}`)
            return res.status(501).json({
                succes: false,
                message: 'Error al obtener los roles'
            })
        }
    },

    async addCliente(req, res, next) {
        
        try {
           
            const cliente = req.body
            console.log('RESPUESTA CONSULTA: ', cliente)


            await Cliente.addCliente(cliente)
            
            console.log('Info: ', cliente)
            //console.log('Rol creado:' , data)

            return res.status(201).json({
                succes: true,
                message: 'El cliente ha sido guardado'
            })
        }
        catch (error) {
            console.log(`Err: ${error}`)
            return res.status(501).json({
                succes: false,
                message: 'Error al agregar el cliente'
            })
        }
    },

    async updateCliente(req, res, next) {
        //console.log('RESPUESTA : ', req)
        
        try {
            
            console.log('RESPUESTA CONSULTA: ', req.body)
            const cliente = req.body
            console.log('RESPUESTA CONSULTA: ', cliente)


            await Cliente.updateCliente(cliente)
            
            console.log('Info: ', cliente)
            //console.log('Rol creado:' , data)

            return res.status(201).json({
                succes: true,
                message: 'El cliente ha sido actualizado'
            })
        }
        catch (error) {
            console.log(`Err: ${error}`)
            return res.status(501).json({
                succes: false,
                message: 'Error al actualizar al cliente'
            })
        }
    },

    async desactivarCliente(req, res, next) {
        //console.log('RESPUESTA : ', req)
        
        try {
            
            console.log('RESPUESTA CONSULTA: ', req.body)
            const cliente = req.body
            const id_cliente =  cliente.id
            console.log('RESPUESTA CONSULTA: ', cliente)
            await Cliente.desactivarCliente(id_cliente)
            
            console.log('Info: ', cliente)
            //console.log('Rol creado:' , data)

            return res.status(201).json({
                succes: true,
                message: 'El cliente ha sido dado de baja'
            })
        }
        catch (error) {
            console.log(`Err: ${error}`)
            return res.status(501).json({
                succes: false,
                message: 'Error al actualizar al cliente'
            })
        }
    },

    async reactivarCliente(req, res, next) {
        //console.log('RESPUESTA : ', req)
        
        try {
            
            console.log('RESPUESTA CONSULTA: ', req.body)
            const cliente = req.body
            const id_cliente =  cliente.id
            console.log('RESPUESTA CONSULTA: ', cliente)


            await Cliente.reactivarCliente(id_cliente)
            
            console.log('Info: ', cliente)
            //console.log('Rol creado:' , data)

            return res.status(201).json({
                succes: true,
                message: 'El cliente ha sido dado de alta nuevamente'
            })
        }
        catch (error) {
            console.log(`Err: ${error}`)
            return res.status(501).json({
                succes: false,
                message: 'Error al actualizar al cliente'
            })
        }
    },

}