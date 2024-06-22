
const express = require('express')
const app = express()
const http = require('http')
const server = http.createServer(app)
const logger = require('morgan')
const cors = require('cors')

const passport =require('passport')

const multer = require('multer')
const serviceAccount = require('./serviceAccountKey.json')
const admin = require('firebase-admin')

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
})

const upload = multer({
    storage: multer.memoryStorage()
})


/**
 * Rutas
 */
const empleados = require('./routes/empleadosRoutes')
const tiposRoles = require('./routes/tiposRolesRoutes')
const notas = require('./routes/notasRoutes')
const clientes = require('./routes/clientesRoutes')

//const { upload } = require('@google-cloud/storage/build/cjs/src/resumable-upload')
//const passport = require('./config/passport')


const port = process.env.POR || 3000

app.use(logger('dev'))
app.use(express.json())
app.use(express.urlencoded({
    extended: true
}))

app.use(cors())

app.use(passport.initialize())
app.use(passport.session())
require('./config/passport')(passport)

app.disable('x-powered-by')

app.set('port', port)

/**
 * Llamando a las rutas
 */
empleados(app, upload)
tiposRoles(app, upload)
notas(app, upload)
clientes(app, upload)


server.listen(3000, '192.168.100.92' || 'localhost', function(){
    console.log('Apliacacion de NodeJS Recycle ' + process.pid + ' Puerto: ' + port + ' Iniciada...')
})

app.get('/recycle',  (req, res) => {
    res.send('Ruta raiz del backend')
})

app.get('/recycle/test/', (req,res) => {
    res.send('Esta es la ruta test')
})

//Error handler
app.use((err, req,res, next) =>{
    console.log('Error Handler', err)
    res.status(err.status || 500).send(err.stack)
})

module.exports = {
    app: app,
    server: server
}