package com.learn.hogwartsartifactsonline.hogwartsuser;

import com.learn.hogwartsartifactsonline.hogwartsuser.converter.UserDtoToUserConverter;
import com.learn.hogwartsartifactsonline.hogwartsuser.converter.UserToUserDtoConverter;
import com.learn.hogwartsartifactsonline.hogwartsuser.dto.UserDto;
import com.learn.hogwartsartifactsonline.system.Result;
import com.learn.hogwartsartifactsonline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class UserController {

    private final UserService userService;
    private final UserToUserDtoConverter userToUserDtoConverter;
    private final UserDtoToUserConverter userDtoToUserConverter;

    public UserController(UserService userService, UserToUserDtoConverter userToUserDtoConverter, UserDtoToUserConverter userDtoToUserConverter) {
        this.userService = userService;
        this.userToUserDtoConverter = userToUserDtoConverter;
        this.userDtoToUserConverter = userDtoToUserConverter;
    }

    @GetMapping
    public Result findAllUsers(){
        List<HogwartsUser> foundHogwartsUsers = this.userService.findAll();

        List<UserDto> userDtos = foundHogwartsUsers.stream().map(this.userToUserDtoConverter::convert)
                .collect(Collectors.toList());

        return new Result(true, StatusCode.SUCCESS, "users fetched", userDtos);
    }

    @GetMapping("/{userId}")
    public Result findUserById(@PathVariable Integer userId){
        HogwartsUser foundUser = this.userService.findById(userId);
        UserDto userDto = this.userToUserDtoConverter.convert(foundUser);
        return new Result(true, StatusCode.SUCCESS, "user fetched", userDto);
    }

    @PostMapping
    public Result addNewUser(@Valid @RequestBody HogwartsUser hogwartsUser){

        HogwartsUser saved = this.userService.save(hogwartsUser);
        UserDto userDto = this.userToUserDtoConverter.convert(saved);
        return new Result(true, StatusCode.SUCCESS, "new user added", userDto);
    }

    @PutMapping("/{userId}")
    public Result updateUser(@PathVariable Integer userId,@Valid @RequestBody UserDto userDto){
        HogwartsUser updateUser= this.userDtoToUserConverter.convert(userDto);
        HogwartsUser updatedHogwartsUser = this.userService.update(userId, updateUser);
        UserDto userDtoUpdated = this.userToUserDtoConverter.convert(updatedHogwartsUser);
        return new Result(true, StatusCode.SUCCESS, "user updated successfully", userDtoUpdated);
    }

    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable Integer userId){
        this.userService.delete(userId);
        return new Result(true, StatusCode.SUCCESS, "user deleted");
    }

}
