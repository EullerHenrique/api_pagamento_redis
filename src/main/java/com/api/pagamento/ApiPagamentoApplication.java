package com.api.pagamento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/*
    @SpringBootApplication = @Configuration + @ComponentScan + @EnableAutoConfiguration
        A anotação @SpringBootApplication é uma combinação das seguintes três anotações Spring e fornece a funcionalidade
        de todas as três com apenas uma linha de código.
    @Configuration
        Esta anotação marca uma classe como uma classe de configuração para configuração baseada em Java. Isso é particularmente
        importante se você prefere a configuração baseada em Java em vez da configuração XML.
    @ComponentScan
        Esta anotação permite a varredura de componentes para que as classes do controlador da web e outros componentes
        que você criar sejam automaticamente descobertos e registrados como beans no contexto de aplicativo do Spring.
    @EnableAutoConfiguration
        Esta anotação ativa o recurso mágico de autoconfiguração do Spring Boot, que pode configurar muitas coisas
        automaticamente para você.
        Por exemplo, se você estiver escrevendo um aplicativo Spring MVC e tiver arquivos JAR do Thymeleaf no classpath do
        aplicativo, a configuração automática do Spring Boot pode configurar automaticamente o resolvedor de modelo
        Thymeleaf, resolvedor de visualização e outras configurações automaticamente.
        Portanto, você pode dizer que @SpringBootApplication é uma anotação 3 em 1 que combina a funcionalidade
        de @Configuration, @ComponentScan e @EnableAutoConfiguration.
        Ele também marca a classe como uma classe BootStrap, o que significa que você pode executá-la como uma classe
        Java normal, por exemplo, executando seu arquivo JAR no prompt de comando, conforme mostrado aqui,
        ou apenas clicando com o botão direito e executando-o como um programa Java no Eclipse IDE.
        Isso iniciará o servidor integrado que acompanha o Spring Boot e executará seu aplicativo da web dentro dele.
        Depois de ver o log sem erros, você pode ir para o navegador e abrir o localhost com a porta do servidor para
        acessar seu aplicativo Spring Boot.
*/
@SpringBootApplication
@EnableWebMvc
//@EnableCaching: Ativa o recurso de gerenciamento de cache orientado a anotações do Spring
@EnableCaching
public class ApiPagamentoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiPagamentoApplication.class, args);
    }

    @Configuration
    @EnableSwagger2
    public class SwaggerConfig {
        @Bean
        public Docket api() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("com.api.pagamento.controller"))
                    .paths(PathSelectors.any())
                    .build()
                    .apiInfo(apiInfo());
        }
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API De Pagamento")
                .description("Data de entrega: 18/04/22")
                .version("1.0.0")
                .license("Apache License Version 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .contact(new Contact("Euller", "https://github.com/EullerHenrique", "euller@email.com.br"))
                .build();
    }
}
