package com.acneshop.controller.h5;

import com.acneshop.common.Result;
import com.acneshop.dto.H5LoginDTO;
import com.acneshop.service.AuthService;
import com.acneshop.vo.LoginVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/h5/auth")
@RequiredArgsConstructor
public class H5AuthController {

    private final AuthService authService;

    @PostMapping("/send-code")
    public Result<Void> sendCode(@RequestParam String phone) {
        ((com.acneshop.service.impl.AuthServiceImpl) authService).sendSmsCode(phone);
        return Result.success();
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody H5LoginDTO dto) {
        return Result.success(authService.h5Login(dto.getPhone(), dto.getCode()));
    }
}
