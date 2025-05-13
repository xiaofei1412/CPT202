package cn.tablego.project.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.spring4all.swagger.EnableSwagger2Doc;

/**
 * TableGo-SpringBoot项目启动类<br/>
 * Swagger2访问地址：http://127.0.0.1:8080/tablego-springboot/swagger-ui.html
 *  Knife4j访问地址：http://127.0.0.1:8080/tablego-springboot/doc.html
 * 
 * @author bianj
 * @version 1.0.0 2025-04-23
 */
@SpringBootApplication
@EnableSwagger2Doc
@EnableTransactionManagement
public class TablegoSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(TablegoSpringbootApplication.class, args);
	}
}