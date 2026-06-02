package com.acneshop.controller.h5;

import com.acneshop.common.Result;
import com.acneshop.dto.H5LoginDTO;
import com.acneshop.dto.H5RegisterDTO;
import com.acneshop.service.AuthService;
import com.acneshop.vo.H5LoginVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/h5/auth")
@RequiredArgsConstructor
public class H5AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<H5LoginVO> login(@Valid @RequestBody H5LoginDTO dto) {
        return Result.success(authService.h5Login(dto.getPhone(), dto.getPassword()));
    }

    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody H5RegisterDTO dto) {
        authService.h5Register(dto);
        return Result.success();
    }
}
