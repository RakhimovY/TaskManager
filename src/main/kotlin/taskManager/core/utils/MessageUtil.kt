package taskManager.core.utils

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component

@Component
class MessageUtil(private val messageSource: MessageSource) {

    fun getMessage(key: String, vararg args: Any): String {
        val locale = LocaleContextHolder.getLocale()
        return messageSource.getMessage(key, args, locale)
    }
}