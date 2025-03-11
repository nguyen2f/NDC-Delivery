package com.ndc.deliverymanagement.controller;

import com.ndc.deliverymanagement.model.PaymentRequest;
import com.ndc.deliverymanagement.service.OrderService;
import com.ndc.deliverymanagement.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private OrderService orderService;

    // API để tạo URL thanh toán
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createPayment(HttpServletRequest request,
                                                             @RequestBody Map<String, String> body) {
        try {
            String amount = body.get("amount");
            String orderInfo = body.get("orderInfo");
            String orderId = body.get("orderId"); // Lấy orderId từ body
            if (orderId == null || orderId.isEmpty()) {
                orderId = String.valueOf(System.currentTimeMillis()); // Nếu không có orderId thì sinh tự động
            }
            String paymentUrl = vnPayService.createPaymentUrl(request, Long.parseLong(amount), orderInfo, orderId);

            Map<String, String> response = new HashMap<>();
            response.put("paymentUrl", paymentUrl);
            response.put("orderId", orderId); // Trả về orderId để client biết
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // API để xử lý kết quả trả về từ VNPay
    @GetMapping("/result")
    public ResponseEntity<Map<String, Object>> paymentResult(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        if (vnPayService.validateSignature(request)) {
            String responseCode = request.getParameter("vnp_ResponseCode");
            if ("00".equals(responseCode)) {
                response.put("status", "success");
                response.put("message", "Thanh toán thành công");
            } else {
                response.put("status", "failed");
                response.put("message", "Thanh toán thất bại: " + responseCode);
            }
        } else {
            response.put("status", "failed");
            response.put("message", "Chữ ký không hợp lệ");
        }

        return ResponseEntity.ok(response);
    }
    @GetMapping("/vnpay-return")
    public void vnpayReturn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean isValidSignature = vnPayService.validateSignature(request);
        if (!isValidSignature) {
            response.sendRedirect("http://your-angular-domain/payment-result?status=failed&message=Sai chữ ký");
            return;
        }

        // Lấy thông tin từ request
        String transactionStatus = request.getParameter("vnp_TransactionStatus"); // 00 là thành công
        String orderId = request.getParameter("vnp_TxnRef");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String payDate = request.getParameter("vnp_PayDate");

        try {
            // Chuyển đổi payDate thành Date
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Date paymentTime = formatter.parse(payDate);

            // Cập nhật trạng thái đơn hàng
            orderService.updatePaymentStatus(Long.parseLong(orderId),
                    "00".equals(transactionStatus) ? "SUCCESS" : "FAILED",
                    transactionId, paymentTime);

            // Redirect về Angular với query params
            String redirectUrl = "http://your-angular-domain/payment-result?status=" +
                    ("00".equals(transactionStatus) ? "success" : "failed") +
                    "&message=" + URLEncoder.encode("00".equals(transactionStatus) ? "Thanh toán thành công!" : "Thanh toán thất bại!", "UTF-8");
            response.sendRedirect(redirectUrl);
        } catch (ParseException e) {
            response.sendRedirect("http://your-angular-domain/payment-result?status=failed&message=Lỗi xử lý thời gian thanh toán");
        }
    }
}

