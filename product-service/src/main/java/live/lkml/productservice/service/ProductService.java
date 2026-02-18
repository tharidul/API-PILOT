package live.lkml.productservice.service;

import live.lkml.productservice.entity.Product;
import live.lkml.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product create(Product product){
        return productRepository.save(product);
    }
    public List<Product> getAll(){
        return productRepository.findAll();
    }
    public Product getById(Long id){
        return productRepository.findById(id).orElse(null);
    }
    public void delete(Long id){
        productRepository.deleteById(id);
    }

    public Product update(Long id,Product product){
        Product existingProduct = getById(id);
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        return productRepository.save(existingProduct);
    }

    public void reduceStock(Long id, Integer quantity){
        Product product = getById(id);

        if(product.getStock()<quantity){
            throw new RuntimeException("Insufficient Stock id: "+id);
        }
        product.setStock(product.getStock()-quantity);
        productRepository.save(product);
    }
}
