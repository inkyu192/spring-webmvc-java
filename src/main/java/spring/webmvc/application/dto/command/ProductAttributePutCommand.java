package spring.webmvc.application.dto.command;

public sealed interface ProductAttributePutCommand permits TransportPutCommand, AccommodationPutCommand {
}
