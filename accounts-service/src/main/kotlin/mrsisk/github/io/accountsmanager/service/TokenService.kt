package mrsisk.github.io.accountsmanager.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.Serial
import java.io.Serializable
import java.security.Key
import java.util.*
import java.util.function.Function

@Service
class TokenService:  Serializable {

    @Value("\${accounts-manager.security.token-secret}")
    private lateinit var secret: String


    @Value("\${accounts-manager.security.token-validity}")
    private lateinit var validity: String

    @Serial
    private val serialVersionUID = 234234523523L

    fun generateToken(subject: String, claims: Map<String, Any>): String{
        return Jwts.builder()
            .setClaims(claims)
            .setIssuer("task-manager")
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + tokenValidity()))
            .signWith(getSignInKey())
            .compact()
    }


    fun getClaim(key: String, token: String): Any? {
       val claims = getAllClaimsFromToken(token)
        return claims[key]
    }

    fun <T> getClaimFromToken(token: String, claimsResolver: Function<Claims, T>): T {
        val claims: Claims = getAllClaimsFromToken(token)
        return claimsResolver.apply(claims)
    }

    fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun getSignInKey(): Key {
        val keyBytes = Decoders.BASE64.decode(secret)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    private fun tokenValidity(): Long{
        return try {
            validity.toLong()

        }catch (ex: Exception){
            86400000
        }
    }

}