package spring.webmvc.presentation.dto.request;

import spring.webmvc.application.dto.command.ProductAttributePutCommand;

public sealed interface ProductAttributePutRequest permits TransportPutRequest, AccommodationPutRequest {
	ProductAttributePutCommand toCommand();
}
