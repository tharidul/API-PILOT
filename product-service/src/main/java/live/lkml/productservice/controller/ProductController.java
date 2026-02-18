package live.lkml.productservice.controller;

import live.lkml.productservice.entity.Product;
import live.lkml.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @PostMapping
    public Product create(@RequestBody Product product){
        return productRepository.save(product);
    }

    @GetMapping
    public List<Product> getAll(){
        return productRepository.findAll();
    }
}
