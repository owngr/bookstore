package ch.wngr.bookstore.configurations

import org.springframework.context.annotation.Configuration
import org.springframework.http.CacheControl
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.resource.PathResourceResolver
import java.util.concurrent.TimeUnit


@Configuration
class WebConfig : WebMvcConfigurer {


    // We want to disable cache for index.html as we want to be sure that the clients have the last versions of the frontend
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/index.html")
            .addResourceLocations("classpath:static/index.html")
            .setCacheControl(CacheControl.noStore())
        registry.addResourceHandler("js/**")
            .addResourceLocations("classpath:static/js/")
            .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
        registry.addResourceHandler("css/**")
            .addResourceLocations("classpath:static/css/")
            .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))

    }
}