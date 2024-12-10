package com.ndc.deliverymanagement.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class NhanhApiService {
    private final RestTemplate restTemplate = new RestTemplate();

    public List<String> getProvinces() {
        String url = "https://api.nhanh.vn/shipping/location";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Chuyển đổi JSON thành danh sách tỉnh
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            return objectMapper.convertValue(
                    rootNode.get("data").get("provinces"),
                    new TypeReference<List<String>>() {}
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Lỗi xử lý JSON", e);
        }
    }
}
