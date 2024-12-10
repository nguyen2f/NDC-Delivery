package com.ndc.deliverymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderStatusRequest {
    private String status;      // Trạng thái mới
    private String description; // Mô tả trạng thái
}
