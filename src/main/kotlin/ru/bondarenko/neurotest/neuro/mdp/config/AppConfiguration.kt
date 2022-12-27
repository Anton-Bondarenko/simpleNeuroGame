package ru.bondarenko.neurotest.neuro.mdp.config

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import ru.bondarenko.neurotest.neuro.mdp.binance.config.ApiConfig

@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties(ApiConfig::class)
@EnableJpaRepositories(basePackages = ["ru.bondarenko.neurotest.neuro"])
open class AppConfiguration
