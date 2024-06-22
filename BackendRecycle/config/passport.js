const JwtStrategy = require('passport-jwt').Strategy
const ExtractJwt = require('passport-jwt').ExtractJwt
const Empleado = require('../models/empleado')
const Keys = require('./keys')

module.exports = function(passport) {

    let opts = {}
    opts.jwtFromRequest = ExtractJwt.fromAuthHeaderWithScheme('jwt')
    opts.secretOrKey = Keys.secretOrKey
    passport.use(new JwtStrategy(opts, (jwt_payload, done) => {
        
        Empleado.findById(jwt_payload.id, (err, empleado)  => {
            if(err) {
                return done(err, false)
            }
            if(empleado){
                return done(null, empleado)
            }
            else {
                return done(null, false)
            }
        })

    }))
}

