package live.lkml.productservice.dto.request;

import lombok.Data;

@Data
public class ProductRequestDTO {

    private String name;
    private Double price;
    private String description;
    private Integer stock;
}
