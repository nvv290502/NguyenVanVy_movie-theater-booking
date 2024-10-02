package com.movie_theaters.vnpay;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pub/payment")
@CrossOrigin(origins = "*")
public class Controller {
    @Autowired
    private VNPAYService vnPayService;

    @PostMapping("/submitOrder")
    public ResponseEntity<Map<String, String>> submitOrder(
            @RequestParam("amount") int orderTotal,
            @RequestParam("orderInfo") String orderInfo,
            HttpServletRequest request) {

        String baseUrl = "http://localhost:3000";
        String vnpayUrl = vnPayService.createOrder(request, orderTotal, orderInfo, baseUrl);

        Map<String, String> response = new HashMap<>();
        response.put("paymentUrl", vnpayUrl);

        return ResponseEntity.ok(response);
    }

//    @GetMapping("/vnpay-payment-return")
//    public ResponseEntity<Map<String, String>> paymentCompleted(HttpServletRequest request) {
//        int paymentStatus = vnPayService.orderReturn(request);
//
//        String orderInfo = request.getParameter("vnp_OrderInfo");
//        String paymentTime = request.getParameter("vnp_PayDate");
//        String transactionId = request.getParameter("vnp_TransactionNo");
//        String totalPrice = request.getParameter("vnp_Amount");
//
//        Map<String, String> response = new HashMap<>();
//        response.put("orderId", orderInfo);
//        response.put("totalPrice", totalPrice);
//        response.put("paymentTime", paymentTime);
//        response.put("transactionId", transactionId);
//        response.put("status", paymentStatus == 1 ? "success" : "fail");
//
//        return ResponseEntity.ok(response);
//    }
}
