package com.udacity.orders;

import com.udacity.orders.model.Customer;
import com.udacity.orders.model.OrderStatus;
import com.udacity.orders.model.car.Car;
import com.udacity.orders.model.order.Order;
import com.udacity.orders.model.order.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableJpaAuditing
public class OrdersApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdersApiApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	/**
	 * Web Client for the vehicle API
	 * @param endpoint where to communicate for the vehicle API
	 * @return created vehicles-api endpoint
	 */
	@Bean(name="vehiclesApi")
	public WebClient webClientVehicles(@Value("${vehicles-api.endpoint}") String endpoint) {
		return WebClient.create(endpoint);
	}

	@Bean
	CommandLineRunner initDatabase(OrderRepository repository) {
		return args -> {
			Car car = new Car();
			car.setId(1L);
			Customer customer = new Customer("Spring", "Restful", "Coffe street 20.", "Beanland", "Java", "1122");
			Order order = new Order(1L, car, customer, OrderStatus.OPEN);

			repository.save(order);
		};
	}

}
