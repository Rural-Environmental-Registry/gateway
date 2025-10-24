package gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// @Bean
	// public RouteLocator myRoutes(RouteLocatorBuilder builder) {
	// return builder.routes()
	// .route(p -> p
	// .path("/rectest")
	// .filters(f -> f.addRequestHeader("Hello", "World"))
	// .uri("http://core-frontend"))
	// .build();
	// }
}
