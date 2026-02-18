package live.lkml.orderservice.dto.response;

import lombok.Data;

@Data
public class ProductClientResponseDTO {

    private Long id;
    private String name;
    private Double price;
    private String description;
    private Integer stock;
}
