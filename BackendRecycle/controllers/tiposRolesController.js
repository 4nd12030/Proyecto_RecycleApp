
const Rol = require('../models/rol')
const storage = require('../utils/cloud_storage')

module.exports = {

    async getAll(req, res, next) {
        
        try {
           //console.log('RESPUESTA CONSULTA: ', Rol.getAll())
            const data = await Rol.getAll()
            //console.log(`Categoria: ${data}`)
            return res.status(201).json(data)
        }
        catch (error) {
            //console.log(`Err: ${error}`)
            return res.status(501).json({
                succes: false,
                message: 'Error al obtener los roles'
            })
        }
    },

    async createNewRol(req, res, next) {
        
        try {
           
            const rol = JSON.parse(req.body.rol)
            console.log('RESPUESTA CONSULTA: ', rol)

            const files = req.files
            console.log(files)

            if(files.length > 0) {
                const pathImage = `image_${Date.now()}`
                const url = await storage(files[0], pathImage)

                if(url != undefined && url != null){
                    rol.imagen = url
                }
            }
            console.log('RESPUESTA CONSULTA2: ', rol)

            await Rol.createNewRol(rol)
            
            console.log('Info del rol: ', rol)
            //console.log('Rol creado:' , data)

            return res.status(201).json({
                succes: true,
                message: 'El registro se realizo correctamente'
            })
        }
        catch (error) {
            console.log(`Err: ${error}`)
            return res.status(501).json({
                succes: false,
                message: 'Error al obtener los roles'
            })
        }
    },


}