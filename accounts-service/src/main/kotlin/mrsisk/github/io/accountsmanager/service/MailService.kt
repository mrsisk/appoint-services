package mrsisk.github.io.accountsmanager.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class MailService(private val mailSender: JavaMailSender) {

    @Value("\${accounts-manager.mail-service.email}")
    lateinit var sender: String

    fun sendSimpleMessage(to: String, subject: String, text: String) {
        try {
            val message = SimpleMailMessage()
            message.from = sender
            message.setTo(to)
            message.subject = subject
            message.text = text

            mailSender.send(message)


        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }
}