
const Empleado = require('../models/empleado')
const Rol = require('../models/rol')
const bcrypt= require('bcryptjs')
const  jwt = require('jsonwebtoken')
const keys = require('../config/keys')

const storage = require('../utils/cloud_storage')

module.exports = {
    async getAll(req, res, next) {
        try {
            const is_available = req.params.is_available
            const data = await Empleado.getAll(is_available)
            console.log(`Empleados: ${data}`)
            return res.status(201).json(data)
        }
        catch (error) {
            console.log(`Error: ${error}`) 
            return res.status(501).json({
                succes: false,
                message: 'Error al obtener usuarios'
            })
        }
    },

    async register(req, res, next){
        try{
            const empleado = req.body
            const data = await Empleado.create(empleado)

            console.log(data)

            await Rol.create(data.no_empleado, 1)
            console.log('Data id:' ,data.id)  
            console.log('Data no_empleado:' , data.no_empleado)          

            const token = jwt.sign({no_empleado: empleado.no_empleado }, keys.secretOrKey, {
                //expiresIn:
                })

                const mydata = {
                    id: data.id,
                    no_empleado: empleado.no_empleado,
                    nombre: empleado.nombre,
                    apellidopat: empleado.apellidopat,
                    apellidomat: empleado.apellidomat,
                    imagen: empleado.imagen,
                    telefono: empleado.telefono,
                    contrasena: empleado.contrasena,
                    session_token: `JWT ${token}`,
                    is_available: empleado.is_available
                }
                console.log(mydata)

            return res.status(201).json({
                succes: true,
                message: 'El registro se realizo correctamente',
                data: mydata
            })
        }
        catch(error){
            console.log(`Error: ${error}`)
            return res.status(501).json({
                success: false,
                message: `Hubo un error con el registros del empleado`,
                error: error
            })
        }
    },

    async login(req, res, next) {

        try {

            const no_empleado = req.body.no_empleado
            const contrasena = req.body.contrasena
            console.log(req.body)
            console.log(no_empleado)
            console.log(contrasena)

            const myEmpleado = await Empleado.findByNumEmpleado(no_empleado)
            console.log(no_empleado)

            if(!myEmpleado){
                return res.status(401).json({
                    succes: false,
                    message: 'El empleado no ha sido registrado'
                })
            } 

            const isPasswordValid = await bcrypt.compare(contrasena, myEmpleado.contrasena)
            console.log(isPasswordValid)
            if(isPasswordValid){
                const token = jwt.sign({ id: myEmpleado.id, no_empleado: myEmpleado.no_empleado }, keys.secretOrKey, {
                    //expiresIn:
                    })

                    const data = {
                        id: myEmpleado.id,
                        nombre: myEmpleado.nombre,
                        apellidopat: myEmpleado.apellidopat,
                        apellidomat: myEmpleado.apellidomat,
                        imagen: myEmpleado.imagen,
                        telefono: myEmpleado.telefono,
                        no_empleado: myEmpleado.no_empleado,
                        contrasena: myEmpleado.contrasena,
                        session_token: `JWT ${token}`,
                        roles: myEmpleado.roles
                    }

                    await Empleado.updateSessionToken(myEmpleado.id, `JWT ${token}`)
                    console.log('Datos en funcion login', data)

                    return res.status(201).json({
                        succes: true,
                        message: 'El usuario ha  sido autenticado',
                        data: data
                    })
                    
            }else {
                return res.status(401).json({
                    success: false,
                    message: 'La constrasena es incorrecta'
                })
            }
            
        } catch (error) {
            console.log(`Error: ${error}`)
            return res.status(501).json({
                success: false,
                message: `Hubo un error con el login del empleado`,
                error: error
            })
        }

    },

    async update(req, res, next) {
        try {
            console.log('REQ', req.body)
            const empleado = JSON.parse(req.body.empleado)
            console.log('Empleado parseado', empleado)

            const files = req.files
            console.log(files)

            if(files.length > 0) {
                const pathImage = `image_${Date.now()}`
                const url = await storage(files[0], pathImage)

                if(url != undefined && url != null){
                    empleado.imagen = url
                }
            }

            await Empleado.update(empleado)
            return res.status(201).json({
                succes: true,
                message: 'Losa dtaos del usuario han sido actualizados correctamente',
                data: empleado
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

    async updateWithoutImage(req, res, next) {
        try {
            console.log('Empleado', req.body.empleado)
            const empleado =  req.body 
            await Empleado.update(empleado)
            return res.status(201).json({
                succes: true,
                message: 'Los datos del usuario han sido actualizados correctamente',
                data: empleado
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

    async findByRol(req, res, next) {

        try {
            console.log('Inicio funcion:', `${req.param}`)
            console.log('Inicio funcion:', `${req.param.id_rol}`)
            const id_rol = req.params.id_rol
            console.log('Id rol recibido:', id_rol)
            const data = await Empleado.findByRol(id_rol)
            console.log('Empleados:', data)
            return res.status(201).json(data)

        } catch (error) {
            console.log('Error empleadosController ', error)
            return res.status(501).json({
                succes: false,
                message: 'Hubo un error al listar los usuarios por rol',
                error: error
            })
            
        }        
    },


    async addUser(req, res, next){
        try{
            const empleado =  JSON.parse(req.body.empleado)
            console.log('REQ2: ', empleado)

            const files = req.files
            console.log(files)

            if(files.length > 0) {
                const pathImage = `image_${Date.now()}`
                const url = await storage(files[0], pathImage)

                if(url != undefined && url != null){
                    empleado.imagen = url
                }
            }
            const data = await Empleado.create(empleado)
            const rol = req.params.id_rol
            await Rol.create(data.no_empleado, rol)

            console.log('body: ', empleado)
            //console.log('RAWHEADERS 1: ', rawHeaders)
            console.log('rol : ', req.params.id_rol)
            console.log(data)         

            const token = jwt.sign({ id: data.id, no_empleado: empleado.no_empleado }, keys.secretOrKey, {
                //expiresIn:
                })

                const mydata = {
                    id: data.id,
                    nombre: empleado.nombre,
                    apellidopat: empleado.apellidopat,
                    apellidomat: empleado.apellidomat,
                    imagen: empleado.imagen,
                    telefono: empleado.telefono,
                    no_empleado: empleado.no_empleado,
                    contrasena: empleado.contrasena,
                    session_token: `JWT ${token}`,
                    is_available: empleado.is_available
                }
                console.log(mydata)

            return res.status(201).json({
                succes: true,
                message: 'El registro se realizo correctamente',
                data: mydata
            })
        }
        catch(error){
            console.log(`Error: ${error}`)
            return res.status(501).json({
                success: false,
                message: `Hubo un error con el registros del empleado`,
                error: error
            })
        }
    },

    async addUserWithoutImage(req, res, next){
        try{
            const empleado =  req.body
            console.log('REQ: ', empleado)

            const data = await Empleado.create(empleado)
            const rol = req.params.id_rol
            await Rol.create(data.no_empleado, rol)

            //console.log('RAWHEADERS 1: ', rawHeaders)
            console.log('rol : ', req.params.id_rol)
            console.log(data)       

            const token = jwt.sign({ id: data.id, no_empleado: empleado.no_empleado }, keys.secretOrKey, {
                //expiresIn:
                })

                const mydata = {
                    id: data.id,
                    nombre: empleado.nombre,
                    apellidopat: empleado.apellidopat,
                    apellidomat: empleado.apellidomat,
                    imagen: empleado.imagen,
                    telefono: empleado.telefono,
                    no_empleado: empleado.no_empleado,
                    contrasena: empleado.contrasena,
                    session_token: `JWT ${token}`,
                    is_available: empleado.is_available
                }
                console.log(mydata)

            return res.status(201).json({
                succes: true,
                message: 'El registro se realizo correctamente',
                data: mydata
            })
        }
        catch(error){
            console.log(`Error: ${error}`)
            return res.status(501).json({
                success: false,
                message: `Hubo un error con el registros del empleado`,
                error: error
            })
        }
    },

    async desactivarEmpleado(req, res, next) {
        try {
            console.log('Empleado', req.body)
            const empleado =  req.body 
            const no_empleado = empleado.no_empleado

            await Empleado.desactivarEmpleado(no_empleado)

            return res.status(201).json({
                succes: true,
                message: 'El usuario ha sido desactivado correctamente',
                data: empleado
            })

        }
        catch (error) {
            console.log(`Error: ${error}`)
            return res.status(501).json({
                success: false,
                message: `Hubo un error al desactivar al usuario`,
                error: error
            })
            
        }
    },

    async reactivarEmpleado(req, res, next) {
        try {
            console.log('Empleado', req.body)
            const empleado =  req.body 
            const no_empleado = empleado.no_empleado

            await Empleado.reactivarEmpleado(no_empleado)
            
            return res.status(201).json({
                succes: true,
                message: 'El usuario ha sido reactivado correctamente',
                data: empleado
            })

        }
        catch (error) {
            console.log(`Error: ${error}`)
            return res.status(501).json({
                success: false,
                message: `Hubo un error al reactivar al usuario`,
                error: error
            })
            
        }
    },





    
} 
 



