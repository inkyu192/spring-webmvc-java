package spring.webmvc.domain.model.entity;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.webmvc.domain.model.enums.Category;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Accommodation extends Product {

    private String location;
    private String accommodationType; // hotel, resort, apartment, etc.
    private int roomCount;
    private int maxOccupancy;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    public static Accommodation create(String name, String description, int price, int quantity, 
                                      String location, String accommodationType, int roomCount, 
                                      int maxOccupancy, LocalDate checkInDate, LocalDate checkOutDate) {
        Accommodation accommodation = new Accommodation();

        accommodation.name = name;
        accommodation.description = description;
        accommodation.price = price;
        accommodation.quantity = quantity;
        accommodation.category = Category.ACCOMMODATION;
        accommodation.location = location;
        accommodation.accommodationType = accommodationType;
        accommodation.roomCount = roomCount;
        accommodation.maxOccupancy = maxOccupancy;
        accommodation.checkInDate = checkInDate;
        accommodation.checkOutDate = checkOutDate;

        return accommodation;
    }

    public void update(String name, String description, int price, int quantity,
                      String location, String accommodationType, int roomCount, 
                      int maxOccupancy, LocalDate checkInDate, LocalDate checkOutDate) {
        super.update(name, description, price, quantity, Category.ACCOMMODATION);
        this.location = location;
        this.accommodationType = accommodationType;
        this.roomCount = roomCount;
        this.maxOccupancy = maxOccupancy;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }
}
