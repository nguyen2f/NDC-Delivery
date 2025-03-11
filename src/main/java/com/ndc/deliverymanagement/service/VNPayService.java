package com.ndc.deliverymanagement.service;
import com.ndc.deliverymanagement.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VNPayService {

    @Value("${vnpay.tmnCode}")
    private String vnpTmnCode;

    @Value("${vnpay.hashSecret}")
    private String vnpHashSecret;

    @Value("${vnpay.paymentUrl}")
    private String vnpPaymentUrl;

    @Value("${vnpay.returnUrl}")
    private String vnpReturnUrl;

    public String createPaymentUrl(HttpServletRequest request, long amount, String orderInfo, String orderId) throws Exception {
        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", vnpTmnCode);
        vnpParams.put("vnp_Amount", String.valueOf(amount * 100));
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", orderId);
        vnpParams.put("vnp_OrderInfo", orderInfo);
        vnpParams.put("vnp_OrderType", "billpayment");
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", vnpReturnUrl);
        vnpParams.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(new Date());
        vnpParams.put("vnp_CreateDate", vnpCreateDate);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 15);
        String vnpExpireDate = formatter.format(cal.getTime());
        vnpParams.put("vnp_ExpireDate", vnpExpireDate);

        // Sắp xếp các tham số theo thứ tự alphabet
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnpParams.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                // Build hash data (encode giá trị trước khi thêm vào hashData)
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                if (itr.hasNext()) {
                    hashData.append('&');
                    query.append('&');
                }
            }
        }

        // Tạo chữ ký bằng HMAC-SHA512
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnpHashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnpSecureHash);


        System.out.println("HashData before signing: " + hashData.toString());
        System.out.println("vnp_SecureHash: " + vnpSecureHash);

        return vnpPaymentUrl + "?" + query.toString();
    }

    // Kiểm tra kết quả trả về từ VNPay
    public boolean validateSignature(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        Map<String, String> fields = new HashMap<>();
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            fields.put(entry.getKey(), entry.getValue()[0]);
        }

        String vnpSecureHash = fields.get("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = fields.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                hashData.append(fieldName).append("=").append(fieldValue);
                if (itr.hasNext()) {
                    hashData.append("&");
                }
            }
        }

        String calculatedHash = VNPayUtil.hmacSHA512(vnpHashSecret, hashData.toString());
        return calculatedHash.equals(vnpSecureHash);
    }
}
