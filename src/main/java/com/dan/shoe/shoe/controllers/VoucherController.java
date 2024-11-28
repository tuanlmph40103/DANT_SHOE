package com.dan.shoe.shoe.controllers;

import com.dan.shoe.shoe.models.Voucher;
import com.dan.shoe.shoe.services.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vouchers")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    @PostMapping("/admin/create")
    public ResponseEntity<?> createVoucher(@RequestBody Voucher voucher) {
        Voucher createdVoucher = voucherService.createVoucher(voucher);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVoucher);
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<Voucher> updateVoucher(@PathVariable Long id, @RequestBody Voucher voucher) {
        Voucher updatedVoucher = voucherService.updateVoucher(id, voucher);
        return ResponseEntity.ok(updatedVoucher);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> deleteVoucher(@PathVariable Long id) {
        voucherService.deleteVoucher(id);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/{code}/validate")
//    public ResponseEntity<Voucher> validateVoucher(@PathVariable String code) {
//        Voucher voucher = voucherService.validateVoucher(code);
//        return ResponseEntity.ok(voucher);
//    }

    @GetMapping("/public/active")
    public ResponseEntity<?> getActiveVouchers() {
        return ResponseEntity.ok(voucherService.getActiveVouchers());
    }
}
