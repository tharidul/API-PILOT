package live.lkml.orderservice.dto.response;

import lombok.Data;

@Data
public class ProductResponse {

    private Long id;
    private String name;
    private Double price;
    private String description;
    private Integer stock;
}
