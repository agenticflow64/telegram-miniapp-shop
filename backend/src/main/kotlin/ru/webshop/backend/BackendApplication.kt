package ru.webshop.backend

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import ru.webshop.backend.entity.Role
import ru.webshop.backend.repository.RoleRepository
import ru.webshop.backend.service.interfaces.AttributeService
import ru.webshop.backend.service.interfaces.CategoryService

@SpringBootApplication
@EnableJpaAuditing
class BackendApplication {

}
fun main(args: Array<String>) {
    runApplication<BackendApplication>(*args)
}
