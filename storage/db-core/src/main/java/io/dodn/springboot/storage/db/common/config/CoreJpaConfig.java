package io.dodn.springboot.storage.db.common.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "io.dodn.springboot.storage.db")
@EnableJpaRepositories(basePackages = "io.dodn.springboot.storage.db")
class CoreJpaConfig {

}
