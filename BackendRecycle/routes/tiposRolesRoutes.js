
const TiposRolesController = require('../controllers/tiposRolesController')

const passport = require('passport')

module.exports = (app, upload) => {
    //GET => Traer Datos
    //app.get('/api/recycle/empleados/getAll', EmpleadosController.getAll)
    app.get('/api/recycle/tiposRoles/getAll', passport.authenticate('jwt', {session: false}), TiposRolesController.getAll)

    app.post('/api/recycle/tiposRoles/addRol', passport.authenticate('jwt', {session: false}),  upload.array('image', 1), TiposRolesController.createNewRol)

    
}