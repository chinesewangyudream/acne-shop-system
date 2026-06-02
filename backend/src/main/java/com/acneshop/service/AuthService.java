package com.acneshop.service;

import com.acneshop.dto.H5RegisterDTO;
import com.acneshop.dto.LoginDTO;
import com.acneshop.vo.H5LoginVO;
import com.acneshop.vo.LoginVO;

public interface AuthService {

    LoginVO login(LoginDTO dto);

    H5LoginVO h5Login(String phone, String password);

    void h5Register(H5RegisterDTO dto);
}
