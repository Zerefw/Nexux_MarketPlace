package application.adapter.in.web;

import application.adapter.in.web.dto.ApiResponse;
import application.adapter.in.web.dto.ProductResponseDTO;
import application.domain.model.entity.Product;
import application.domain.ports.in.catalog.*;
import application.domain.ports.in.command.catalog.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST (Adaptador de entrada) para la administracion completa de productos del catalogo.
 */
@RestController
@RequestMapping("/api/v1/catalog/products")
@RequiredArgsConstructor
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final AddProductVariantUseCase addProductVariantUseCase;
    private final DeactivateProductUseCase deactivateProductUseCase;
    private final FindProductBySkuUseCase findProductBySkuUseCase;
    private final ListActiveProductsUseCase listActiveProductsUseCase;

    /**
     * Crea un nuevo producto en el catalogo.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDTO>> createProduct(@RequestBody CreateProductCommand command) {
        Product createdProduct = createProductUseCase.createProduct(command);
        return ResponseEntity.ok(ApiResponse.ok("Producto agregado al catalogo exitosamente", ProductResponseDTO.fromDomain(createdProduct)));
    }

    /**
     * Actualiza la descripcion y el precio de un producto existente.
     */
    @PutMapping
    public ResponseEntity<ApiResponse<ProductResponseDTO>> updateProduct(@RequestBody UpdateProductCommand command) {
        Product updatedProduct = updateProductUseCase.updateProduct(command);
        return ResponseEntity.ok(ApiResponse.ok("Producto actualizado exitosamente", ProductResponseDTO.fromDomain(updatedProduct)));
    }

    /**
     * Agrega una variante comercial a un producto existente.
     */
    @PostMapping("/variants")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> addVariant(@RequestBody AddProductVariantCommand command) {
        Product updatedProduct = addProductVariantUseCase.addProductVariant(command);
        return ResponseEntity.ok(ApiResponse.ok("Variante agregada exitosamente", ProductResponseDTO.fromDomain(updatedProduct)));
    }

    /**
     * Desactiva logicamente un producto por su SKU.
     */
    @DeleteMapping("/{sku}")
    public ResponseEntity<ApiResponse<String>> deactivateProduct(@PathVariable String sku) {
        deactivateProductUseCase.deactivateProduct(new DeactivateProductCommand(sku));
        return ResponseEntity.ok(ApiResponse.ok("Producto desactivado exitosamente", sku));
    }

    /**
     * Busca un producto especifico por su SKU.
     */
    @GetMapping("/{sku}")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> findBySku(@PathVariable String sku) {
        Product product = findProductBySkuUseCase.findProductBySku(new FindProductBySkuCommand(sku));
        return ResponseEntity.ok(ApiResponse.ok("Producto encontrado", ProductResponseDTO.fromDomain(product)));
    }

    /**
     * Lista todos los productos activos del catalogo.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> listActiveProducts() {
        List<ProductResponseDTO> products = listActiveProductsUseCase
                .listActiveProducts(new ListActiveProductsCommand())
                .stream()
                .map(ProductResponseDTO::fromDomain)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok("Listado de productos activos", products));
    }
}
