package spring.webmvc.application.service;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import spring.webmvc.application.dto.command.TicketCreateCommand;
import spring.webmvc.application.dto.command.TicketUpdateCommand;
import spring.webmvc.application.dto.result.ProductResult;
import spring.webmvc.application.dto.result.TicketResult;
import spring.webmvc.application.strategy.ProductStrategy;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.enums.Category;
import spring.webmvc.domain.repository.ProductRepository;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	private ProductService productService;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private ProductStrategy productStrategy;

	@BeforeEach
	void setUp() {
		productService = new ProductService(productRepository, List.of(productStrategy));
	}

	@Test
	@DisplayName("findProducts: Product 조회 후 반환한다")
	void findProducts() {
		// Given
		Pageable pageable = PageRequest.of(0, 10);
		String name = "name";
		List<Product> products = List.of(
			Product.create("name1", "description", 1000, 10, Category.ACCOMMODATION),
			Product.create("name2", "description", 2000, 20, Category.FLIGHT),
			Product.create("name3", "description", 3000, 30, Category.TICKET)
		);
		Page<Product> page = new PageImpl<>(products, pageable, products.size());

		Mockito.when(productRepository.findAll(pageable, name)).thenReturn(page);

		// When
		Page<ProductResult> result = productService.findProducts(pageable, name);

		// Then
		Assertions.assertThat(result.getContent()).hasSize(products.size());
	}

	@Test
	@DisplayName("findProduct: Product 없을 경우 EntityNotFoundException 발생한다")
	void findProductCase1() {
		// Given
		Long productId = 1L;
		Category category = Category.TICKET;

		Mockito.when(productStrategy.supports(category)).thenReturn(true);
		Mockito.when(productStrategy.findByProductId(productId)).thenThrow(EntityNotFoundException.class);

		// When & Then
		Assertions.assertThatThrownBy(() -> productService.findProduct(productId, category))
			.isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	@DisplayName("findProduct: Product 있을 경우 조회 후 반환한다")
	void findProductCase2() {
		// Given
		Long productId = 1L;
		Category category = Category.TICKET;

		TicketResult ticketResult = Mockito.mock(TicketResult.class);

		Mockito.when(productStrategy.supports(category)).thenReturn(true);
		Mockito.when(productStrategy.findByProductId(productId)).thenReturn(ticketResult);

		// When
		ProductResult result = productService.findProduct(productId, category);

		// Then
		Assertions.assertThat(result.getName()).isEqualTo(ticketResult.getName());
		Assertions.assertThat(result.getDescription()).isEqualTo(ticketResult.getDescription());
		Assertions.assertThat(result.getPrice()).isEqualTo(ticketResult.getPrice());
		Assertions.assertThat(result.getQuantity()).isEqualTo(ticketResult.getQuantity());
		Assertions.assertThat(result).isInstanceOfSatisfying(TicketResult.class, ticket -> {
			Assertions.assertThat(ticket.getPlace()).isEqualTo(ticketResult.getPlace());
			Assertions.assertThat(ticket.getPerformanceTime()).isEqualTo(ticketResult.getPerformanceTime());
			Assertions.assertThat(ticket.getDuration()).isEqualTo(ticketResult.getDuration());
			Assertions.assertThat(ticket.getAgeLimit()).isEqualTo(ticketResult.getAgeLimit());
		});
	}

	@Test
	@DisplayName("createProduct: Product 저장 후 반환한다")
	void createProduct() {
		// Given
		Category category = Category.TICKET;

		TicketCreateCommand ticketCreateCommand = Mockito.mock(TicketCreateCommand.class);
		Mockito.when(ticketCreateCommand.getCategory()).thenReturn(category);

		TicketResult ticketResult = Mockito.mock(TicketResult.class);

		Mockito.when(productStrategy.supports(category)).thenReturn(true);
		Mockito.when(productStrategy.createProduct(ticketCreateCommand)).thenReturn(ticketResult);

		// When
		ProductResult result = productService.createProduct(ticketCreateCommand);

		// Then
		Assertions.assertThat(result.getName()).isEqualTo(ticketResult.getName());
		Assertions.assertThat(result.getDescription()).isEqualTo(ticketResult.getDescription());
		Assertions.assertThat(result.getPrice()).isEqualTo(ticketResult.getPrice());
		Assertions.assertThat(result.getQuantity()).isEqualTo(ticketResult.getQuantity());
		Assertions.assertThat(result).isInstanceOfSatisfying(TicketResult.class, ticket -> {
			Assertions.assertThat(ticket.getPlace()).isEqualTo(ticketResult.getPlace());
			Assertions.assertThat(ticket.getPerformanceTime()).isEqualTo(ticketResult.getPerformanceTime());
			Assertions.assertThat(ticket.getDuration()).isEqualTo(ticketResult.getDuration());
			Assertions.assertThat(ticket.getAgeLimit()).isEqualTo(ticketResult.getAgeLimit());
		});
	}

	@Test
	@DisplayName("updateProduct: Product 없을 경우 EntityNotFoundException 발생한다")
	void updateProductCase1() {
		// Given
		Long productId = 1L;

		TicketUpdateCommand ticketUpdateCommand = Mockito.mock(TicketUpdateCommand.class);

		Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

		// When & Then
		Assertions.assertThatThrownBy(() -> productService.updateProduct(productId, ticketUpdateCommand))
			.isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	@DisplayName("updateProduct: Product 있을 경우 수정 후 반환한다")
	void updateProductCase2() {
		// Given
		Long productId = 1L;
		Category category = Category.TICKET;

		TicketUpdateCommand ticketUpdateCommand = Mockito.mock(TicketUpdateCommand.class);
		Mockito.when(ticketUpdateCommand.getCategory()).thenReturn(category);

		Product product = Mockito.mock(Product.class);

		TicketResult ticketResult = Mockito.mock(TicketResult.class);

		Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
		Mockito.when(productStrategy.supports(category)).thenReturn(true);
		Mockito.when(productStrategy.updateProduct(productId, ticketUpdateCommand)).thenReturn(ticketResult);

		// When
		ProductResult result = productService.updateProduct(productId, ticketUpdateCommand);

		// Then
		Assertions.assertThat(result.getName()).isEqualTo(ticketResult.getName());
		Assertions.assertThat(result.getDescription()).isEqualTo(ticketResult.getDescription());
		Assertions.assertThat(result.getPrice()).isEqualTo(ticketResult.getPrice());
		Assertions.assertThat(result.getQuantity()).isEqualTo(ticketResult.getQuantity());
		Assertions.assertThat(result).isInstanceOfSatisfying(TicketResult.class, ticket -> {
			Assertions.assertThat(ticket.getPlace()).isEqualTo(ticketResult.getPlace());
			Assertions.assertThat(ticket.getPerformanceTime()).isEqualTo(ticketResult.getPerformanceTime());
			Assertions.assertThat(ticket.getDuration()).isEqualTo(ticketResult.getDuration());
			Assertions.assertThat(ticket.getAgeLimit()).isEqualTo(ticketResult.getAgeLimit());
		});
	}
}
