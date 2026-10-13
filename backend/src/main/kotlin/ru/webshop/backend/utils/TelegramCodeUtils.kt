package ru.webshop.backend.utils

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Component
class TelegramCodeUtils(
    @Value("\${telegram.bot.token}")
    private val botToken: String,
) {

    fun generateSecretKey(): ByteArray {
        val hmac = Mac.getInstance("HmacSHA256")
        hmac.init(SecretKeySpec("WebAppData".toByteArray(StandardCharsets.UTF_8), "HmacSHA256"))
        return hmac.doFinal(botToken.toByteArray(StandardCharsets.UTF_8))
    }

    fun validateTelegramAuth(secretKey : ByteArray, paramMap: Map<String, String>, receivedHash: String): Boolean {
        val dataCheckString = paramMap.entries
            .filter { it.key != "hash" }
            .sortedBy { it.key }
            .joinToString("\n") { "${it.key}=${it.value}" }

        val hmac = Mac.getInstance("HmacSHA256").apply {
            init(SecretKeySpec(secretKey, "HmacSHA256"))
        }
        val calculatedHash = bytesToHex(hmac.doFinal(dataCheckString.toByteArray(StandardCharsets.UTF_8)))
        return calculatedHash.equals(receivedHash, ignoreCase = true)
    }

    fun parseQueryString(queryString: String): Map<String, String> {
        return queryString.split("&").associate {
            val (key, value) = it.split("=", limit = 2)
            URLDecoder.decode(key, StandardCharsets.UTF_8) to
                    URLDecoder.decode(value, StandardCharsets.UTF_8)
        }
    }

    private fun bytesToHex(bytes: ByteArray): String {
        return bytes.joinToString("") { "%02x".format(it) }
    }
}