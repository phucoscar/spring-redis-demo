package com.aml.controller;

import com.aml.base.Result;
import com.aml.service.SinhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class UserController {

    @Autowired
    private SinhVienService sinhVienService;

    @GetMapping("/users")
    public Result getAll() {
        return sinhVienService.findAll();
    }

    @PostMapping("/add-sinh-vien")
    public Result addNewSinhVien(@RequestBody String dto) {
        return sinhVienService.addSinhVien(dto);
    }

    @PostMapping("/user-info")
    public Result getOne(@RequestParam(value = "id") Integer id) {
        return sinhVienService.getUserInfo(id);
    }

    @PostMapping("/delete-sinh-vien")
    public Result deleteSinhVien(@RequestParam(value = "id") Integer id) {
        return sinhVienService.deleteSinhVien(id);
    }
}
