const promise = require('bluebird')
const options = {
    promiseLib: promise, //
    query: (e) => {}
}

const pgp = require('pg-promise')(options)
const types = pgp.pg.types
// console.log('File config- options : ', options)
// console.log('File config- pgp : ', pgp)
// console.log('File config- types : ', types)

types.setTypeParser(1114, function(stringValue){
    return stringValue
})

const databaseConfig = {
    'host' : '127.0.0.1',
    'port' : 5432,
    'database' : 'recycle_db',
    'user' : 'postgres',
    'password' : 'root'
}

const db = pgp(databaseConfig)

module.exports = db