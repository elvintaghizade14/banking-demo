package com.azercell.banking.account;

import com.azercell.banking.commonlib.util.LogUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableWebMvc
@EnableSwagger2
@SpringBootApplication
@ComponentScan(basePackages = {"com.azercell.banking.commonlib", "com.azercell.banking.account"})
public class MsAccountApplication {

    public static void main(String[] args) {
        final SpringApplication app = new SpringApplication(MsAccountApplication.class);
        final Environment env = app.run(args).getEnvironment();
        LogUtil.logApplicationStartup(env);
    }

}
