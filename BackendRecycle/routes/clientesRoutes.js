
const ClientesController = require('../controllers/clientesController')

const passport = require('passport')

module.exports = (app, upload) => {
    //GET => Traer Datos
    //app.get('/api/recycle/empleados/getAll', EmpleadosController.getAll)
    app.get('/api/recycle/clientes/getAll/:is_available', passport.authenticate('jwt', {session: false}), ClientesController.getAll)

    app.post('/api/recycle/clientes/addCliente', passport.authenticate('jwt', {session: false}),  ClientesController.addCliente)

    app.put('/api/recycle/clientes/updateCliente', passport.authenticate('jwt', {session: false}), ClientesController.updateCliente)
    app.put('/api/recycle/clientes/desactivarCliente', passport.authenticate('jwt', {session: false}), ClientesController.desactivarCliente)
    app.put('/api/recycle/clientes/reactivarCliente', passport.authenticate('jwt', {session: false}), ClientesController.reactivarCliente)
    
}