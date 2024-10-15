package com.ndc.deliverymanagement.controller;

import com.ndc.deliverymanagement.model.User;
import com.ndc.deliverymanagement.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    // Trang đăng ký
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/login";
    }

    // Trang đăng nhập
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String phoneNumber, @RequestParam String password, HttpSession session, Model model) {
        if (userService.checkLogin(phoneNumber, password)) {
            // Lưu thông tin người dùng vào session sau khi đăng nhập thành công
            session.setAttribute("loggedInUser", userService.findByPhoneNumber(phoneNumber));
            return "redirect:/home";  // Đăng nhập thành công
        } else {
            model.addAttribute("error", "Invalid phone number or password");
            return "login";  // Đăng nhập thất bại
        }
    }

    // Trang home
    @GetMapping("/home")
    public String home() {
        return "home";
    }

    // Trang cập nhật thông tin người dùng
    @GetMapping("/update-user")
    public String showUpdateUserForm(HttpSession session, Model model) {
        // Lấy thông tin người dùng từ session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("user", loggedInUser);
            return "update-user";  // Hiển thị trang cập nhật thông tin
        }
        return "redirect:/login";  // Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
    }

    @PostMapping("/update-user")
    public String updateUser(@ModelAttribute("user") User updatedUser, HttpSession session, Model model) {
        // Lấy thông tin người dùng hiện tại từ session
        User existingUser = (User) session.getAttribute("loggedInUser");

        // Cập nhật họ tên và mật khẩu
        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setPassword(updatedUser.getPassword());

        // Lưu thông tin mới
        userService.save(existingUser);

        model.addAttribute("message", "User information updated successfully!");
        return "update-user";
    }
}
