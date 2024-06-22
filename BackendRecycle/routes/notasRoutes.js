
const NotasController = require('../controllers/notasController')

const DownloadController = require('../controllers/downloadController')

const passport = require('passport')

module.exports = (app, upload) => {

    app.get('/api/recycle/notas/descargarArchivo/:id_operario/:id_administrativo/:status', passport.authenticate('jwt', {session: false}), DownloadController.getNotaDownlaod)

    app.get('/api/recycle/notas/getAll', passport.authenticate('jwt', {session: false}), NotasController.getAll)
    app.get('/api/recycle/notas/findByEmpleadoAndStatus/:num_empleado/:status', passport.authenticate('jwt', {session: false}), NotasController.findByEmpleadoAndStatus)
    app.get('/api/recycle/notas/findByAdministrativoAndStatus/:id_administrativo/:status', passport.authenticate('jwt', {session: false}), NotasController.findByAdministrativoAndStatus)
    app.get('/api/recycle/notas/findByStatusAprobada/:id_operario/:id_administrativo/:status', passport.authenticate('jwt', {session: false}), NotasController.findByStatusAprobada)
    
    app.get('/api/recycle/notas/findByStatus/:status', passport.authenticate('jwt', {session: false}), NotasController.findByStatus)

    //GET => Traer Datos
    //app.get('/api/recycle/empleados/findByRol/:id_rol', passport.authenticate('jwt', {session: false}), EmpleadosController.findByRol)
    
    //app.get('/api/recycle/empleados/getAll', EmpleadosController.getAll)

    //POST => Guardar Datos
    app.post('/api/recycle/notas/create', passport.authenticate('jwt', {session: false}), NotasController.create)
    //app.post('/api/recycle/empleados/addUser/:id_rol', passport.authenticate('jwt', {session: false}), upload.array('image', 1), EmpleadosController.addUser)    
    //app.post('/api/recycle/empleados/login', EmpleadosController.login)

    //PUT => ACTUALIZA DATOS
    //401 UNAUTHORIZED
    app.put('/api/recycle/notas/update', passport.authenticate('jwt', {session: false}), NotasController.update)
    app.put('/api/recycle/notas/updateNotaEnviada', passport.authenticate('jwt', {session: false}), NotasController.updateNotaEnviada)
    app.put('/api/recycle/notas/updateNotaPendiente', passport.authenticate('jwt', {session: false}), NotasController.updateNotaPendiente)
    app.put('/api/recycle/notas/updateNotaAprobada', passport.authenticate('jwt', {session: false}), NotasController.updateNotaAprobada)
    app.put('/api/recycle/notas/updateNotaEntregada/:id', passport.authenticate('jwt', {session: false}), NotasController.updateNotaEntregada)

    //DELETE 
    app.delete('/api/recycle/notas/deleteNota/:id', passport.authenticate('jwt', {session: false}), NotasController.deleteNota)

}