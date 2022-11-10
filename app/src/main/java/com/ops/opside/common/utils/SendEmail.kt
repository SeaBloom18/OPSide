package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.util.Log
import com.ops.opside.common.utils.CalendarUtils
import com.ops.opside.common.utils.FORMAT_DATE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


object EmailSender{
    suspend fun send(body: String, recipient: String, response: (Pair<Boolean,String>) -> Unit = {}) {
        withContext(Dispatchers.IO) {
            try {
                val props = Properties()
                props["mail.smtp.host"] = "smtp.gmail.com"
                props["mail.smtp.socketFactory.port"] = "587"
                props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
                props["mail.smtp.auth"] = "true"
                props["mail.smtp.port"] = "587"
                props["mail.imap.ssl.enable"] = "true" // required for Gmail
                props["mail.imap.auth.mechanisms"] = "XOAUTH2"
                props["mail.smtp.starttls.enable"] = "true"

                val email = "recaudacion_fiscal@ixtlahuacanmembrillo.com"
                val password = "gohufgxgryucedfr"

                val session = Session.getInstance(props, object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(email, password)
                    }
                })

                val mm = MimeMessage(session)
                mm.setFrom(InternetAddress(email))
                mm.addRecipient(Message.RecipientType.TO, InternetAddress(recipient))
                mm.subject = "Recibo ${CalendarUtils.getCurrentTimeStamp(FORMAT_DATE)}"
                mm.setText(body)

                Transport.send(mm)

                response.invoke(Pair(true,"Success"))
            } catch (e: Exception) {
                Log.d("Demo", "sendEmail: ${e.message}")
                response.invoke(Pair(false,e.message.orEmpty()))
            }
        }
    }
}