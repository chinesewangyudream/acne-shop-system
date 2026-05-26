package com.acneshop.controller.admin;

import com.acneshop.common.Result;
import com.acneshop.entity.User;
import com.acneshop.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/page")
    public Result<Page<User>> page(@RequestParam(defaultValue = "1") Integer current,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   @RequestParam(required = false) String realName,
                                   @RequestParam(required = false) String role,
                                   @RequestParam(required = false) Long storeId) {
        Page<User> page = userMapper.selectPage(new Page<>(current, size),
                new LambdaQueryWrapper<User>()
                        .like(realName != null, User::getRealName, realName)
                        .eq(role != null, User::getRole, role)
                        .eq(storeId != null, User::getStoreId, storeId)
                        .orderByDesc(User::getCreateTime));
        return Result.success(page);
    }

    @PostMapping
    public Result<Void> add(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userMapper.updateById(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userMapper.deleteById(id);
        return Result.success();
    }
}
