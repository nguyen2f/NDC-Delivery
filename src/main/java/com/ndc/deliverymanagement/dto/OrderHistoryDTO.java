package com.ndc.deliverymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryDTO {
    
    private String status;        // Trạng thái đơn hàng
    private String description;   // Mô tả trạng thái
    private LocalDateTime timestamp; // Thời gian cập nhật trạng thái
}
