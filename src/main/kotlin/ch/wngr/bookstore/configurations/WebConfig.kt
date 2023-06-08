package ch.wngr.bookstore.configurations

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class WebConfig : WebMvcConfigurer, ApplicationContextAware {

    private var applicationContext: ApplicationContext? = null
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    // We want to disable cache for index.html as we want to be sure that the clients have the last versions of the frontend
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/")
            .addResourceLocations("classpath:static/index.html")
            .setCachePeriod(0)
        super.addResourceHandlers(registry)
    }
}