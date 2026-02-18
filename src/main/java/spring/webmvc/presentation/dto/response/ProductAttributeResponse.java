package spring.webmvc.presentation.dto.response;

import spring.webmvc.application.dto.result.AccommodationResult;
import spring.webmvc.application.dto.result.ProductAttributeResult;
import spring.webmvc.application.dto.result.TransportResult;

public sealed interface ProductAttributeResponse permits TransportResponse, AccommodationResponse {
	static ProductAttributeResponse of(ProductAttributeResult result) {
		return switch (result) {
			case TransportResult transportResult -> TransportResponse.of(transportResult);
			case AccommodationResult accommodationResult -> AccommodationResponse.of(accommodationResult);
		};
	}
}
