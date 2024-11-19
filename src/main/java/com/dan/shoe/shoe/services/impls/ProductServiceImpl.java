package com.dan.shoe.shoe.services.impls;

import com.dan.shoe.shoe.dtos.requests.ProductCreationRequest;
import com.dan.shoe.shoe.dtos.responses.ResponseMessage;
import com.dan.shoe.shoe.models.Brand;
import com.dan.shoe.shoe.models.Category;
import com.dan.shoe.shoe.models.Product;
import com.dan.shoe.shoe.models.ProductVariant;
import com.dan.shoe.shoe.repositories.BrandRepository;
import com.dan.shoe.shoe.repositories.CategoryRepository;
import com.dan.shoe.shoe.repositories.ProductRepository;
import com.dan.shoe.shoe.repositories.ProductVariantRepository;
import com.dan.shoe.shoe.services.FileUploadService;
import com.dan.shoe.shoe.services.ProductService;
import com.dan.shoe.shoe.utils.QrCodeGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ResponseMessage createProductWithVariants(ProductCreationRequest productCreationRequest) {
        Brand brand = brandRepository.findById(productCreationRequest.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));
        Category category = categoryRepository.findById(productCreationRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = Product.builder()
                .name(productCreationRequest.getName())
                .description(productCreationRequest.getDescription())
                .price(productCreationRequest.getPrice())
                .brand(brand)
                .category(category)
                .gender(productCreationRequest.getGender())
                .createdAt(LocalDateTime.now())
                .build();

        Product savedProduct = productRepository.save(product);

        if (savedProduct == null) {
            throw new RuntimeException("Tạo sản phẩm thất bại");
        }

        List<ProductVariant> variants = productCreationRequest.getVariants().stream()
                .map(variantDTO -> {
                    ProductVariant variant = ProductVariant.builder()
                            .product(savedProduct)
                            .size(variantDTO.getSize())
                            .color(variantDTO.getColor())
                            .stockQuantity(variantDTO.getStockQuantity())
                            .price(variantDTO.getPrice())
                            .defaultVariant(variantDTO.isDefaultVariant())
                            .imageAvatar(processImageAvatar(variantDTO.getImageAvatarFile()))
                            .imageOthers(processImageOthers(variantDTO.getImageOtherFiles()))
                            .build();

                    ProductVariant savedVariant = productVariantRepository.save(variant);

                    // Tạo QR code dưới dạng MultipartFile
                    try {
                        String qrContent = createQrContent(savedVariant);
                        MultipartFile qrCodeFile = QrCodeGenerator.generateQRCode(qrContent);

                        String qrCodePath = fileUploadService.uploadFile(qrCodeFile).getFileCode();
                        savedVariant.setQrCode(qrCodePath);

                    } catch (WriterException | IOException e) {
                        throw new RuntimeException("Error generating QR code", e);
                    }

                    return productVariantRepository.save(variant);
                })
                .collect(Collectors.toList());

        if (variants.isEmpty()) {
            throw new RuntimeException("Tạo sản phẩm thất bại");
        }

        return new ResponseMessage(200, "Tạo sản phẩm thành công");
    }

    @Override
    public ProductVariant getProductVariantById(Long id) {
        return productVariantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product variant not found"));
    }

    private String processImageAvatar(MultipartFile imageAvatarFile) {
        String imageAvatar = null;
        try {
            imageAvatar = fileUploadService.uploadFile(imageAvatarFile).getFileCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imageAvatar;
    }

    private List<String> processImageOthers(List<MultipartFile> imageOtherFiles) {
        List<String> imageOthers = new ArrayList<>();
        try {
            imageOthers = imageOtherFiles.stream()
                    .map(file -> {
                        try {
                            return fileUploadService.uploadFile(file).getFileCode();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return imageOthers;
    }

    private String createQrContent(ProductVariant variant) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> productInfo = new HashMap<>();
            productInfo.put("productId", variant.getProduct().getId());
            productInfo.put("variantId", variant.getId());
            productInfo.put("productName", variant.getProduct().getName());
            productInfo.put("color", variant.getColor());
            productInfo.put("price", variant.getPrice());
            productInfo.put("size", variant.getSize());
            productInfo.put("price", variant.getPrice());

            return objectMapper.writeValueAsString(productInfo);
        } catch (IOException e) {
            throw new RuntimeException("Error creating QR content", e);
        }
    }
}
