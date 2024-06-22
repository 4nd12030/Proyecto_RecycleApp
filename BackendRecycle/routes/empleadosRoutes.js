
const EmpleadosController = require('../controllers/empleadosController')

const passport = require('passport')

module.exports = (app, upload) => {

    app.get('/api/recycle/empleados/getAll/:is_available', passport.authenticate('jwt', {session: false}), EmpleadosController.getAll)
    //GET => Traer Datos
    app.get('/api/recycle/empleados/findByRol/:id_rol', passport.authenticate('jwt', {session: false}), EmpleadosController.findByRol)
    
    //app.get('/api/recycle/empleados/getAll', EmpleadosController.getAll)

    //POST => Guardar Datos
    app.post('/api/recycle/empleados/create', EmpleadosController.register)
    app.post('/api/recycle/empleados/addUser/:id_rol', passport.authenticate('jwt', {session: false}), upload.array('image', 1), EmpleadosController.addUser)
    app.post('/api/recycle/empleados/addUserWithoutImage/:id_rol', passport.authenticate('jwt', {session: false}), EmpleadosController.addUserWithoutImage)
      

    app.post('/api/recycle/empleados/login', EmpleadosController.login)

    //PUT => ACTUALIZA DATOS
    //401 UNAUTHORIZED
    app.put('/api/recycle/empleados/update', passport.authenticate('jwt', {session: false}), upload.array('image', 1), EmpleadosController.update)
    app.put('/api/recycle/empleados/desactivarEmpleado', passport.authenticate('jwt', {session: false}), EmpleadosController.desactivarEmpleado)
    app.put('/api/recycle/empleados/reactivarEmpleado', passport.authenticate('jwt', {session: false}), EmpleadosController.reactivarEmpleado)
    
    //app.put('/api/recycle/empleados/update', upload.array('image', 1), EmpleadosController.update)

    app.put('/api/recycle/empleados/updateWithoutImage',passport.authenticate('jwt', {session: false}), EmpleadosController.updateWithoutImage)
}