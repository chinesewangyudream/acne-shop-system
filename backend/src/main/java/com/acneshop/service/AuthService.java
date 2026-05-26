package com.acneshop.service;

import com.acneshop.dto.LoginDTO;
import com.acneshop.vo.LoginVO;

public interface AuthService {

    LoginVO login(LoginDTO dto);

    LoginVO h5Login(String phone, String code);
}
