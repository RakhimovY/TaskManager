package taskManager.core.resolver

import org.springframework.stereotype.Component
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import java.util.*

@Component
class LocaleResolver : AcceptHeaderLocaleResolver() {
    override fun resolveLocale(request: javax.servlet.http.HttpServletRequest): Locale {
        val acceptLanguage = request.getHeader("Accept-Language")
        return if (acceptLanguage.isNullOrEmpty()) Locale.getDefault() else Locale.forLanguageTag(acceptLanguage)
    }
}
