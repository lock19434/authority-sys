package uestc.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uestc.entity.User;
import uestc.service.UserService;
import uestc.utils.Result;

import java.util.List;

/**
 * @author liulong
 * @since 2025-01-25
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/listAll")
    public Result<List<User>> getAll() {
        return Result.ok(userService.list());
    }
}
