package com.h5templates.directory.feature.email.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
class EmailService(
    private val emailSender: JavaMailSender,
    private val templateEngine: TemplateEngine,
) {
    fun sendHtmlMessage(to: String, subject: String, templateName: String, context: Context) {
        val mimeMessage = emailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, true)

        helper.setFrom("your-email@gmail.com")
        helper.setTo(to)
        helper.setSubject(subject)

        // Process the Thymeleaf template
        val htmlContent = templateEngine.process(templateName, context)
        helper.setText(htmlContent, true) // Set to true to indicate HTML content

        emailSender.send(mimeMessage)
    }
}