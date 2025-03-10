package org.traffichunter.javaagent.testai.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.traffichunter.javaagent.testai.core.UserService;
import org.traffichunter.javaagent.testai.core.dto.UserRequest;
import org.traffichunter.javaagent.testai.core.dto.UserResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test/users/")
public class UserApi {

    private final UserService userService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse signUpApi(@RequestBody final UserRequest userRequest) {

        return userService.signUp(
                userRequest.email(),
                userRequest.password(),
                userRequest.name()
        );
    }
}
