package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.UserEntity;
import com.example.userservice.mq.KafkaProducer;
import com.example.userservice.mq.UserProducer;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.rule.Mode;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/")
public class UserController {

    private final Environment env;
    private final UserService userService;
    private final KafkaProducer kafkaProducer;
    private final UserProducer userProducer;

    @Autowired
    public UserController(Environment env, UserService userService,
                          KafkaProducer kafkaProducer, UserProducer userProducer){
        this.env = env;
        this.userService = userService;
        this.kafkaProducer = kafkaProducer;
        this.userProducer = userProducer;
    }

    @GetMapping("/health_check")
    public String status(HttpServletRequest request){
        return String.format("It's Working in User Service, " +
                "port(local.server.port)=%s, port(server.port)=%s,  " +
                "token_secret=%s, token_expiration_time=%s,  " +
                "gateway.ip=%s", env.getProperty("local.server.port"), env.getProperty("server.port"),
                env.getProperty("token.secret"), env.getProperty("token.expiration_time"), env.getProperty("gateway.ip")) ;
    }

    @GetMapping("/welcome")
    public String welcome(){
        return env.getProperty("greeting.message");
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@RequestBody @Valid RequestUser user){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(user, UserDto.class);
        userService.createUser(userDto);

        // convert UserDto to ResponseUser
        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);
//        userProducer.send("demo-sink-connect", userDto);
//        userProducer.send("demo-source-connect", userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @PutMapping("/users/{Pwd}")
    public void modifiedUser(@PathVariable("Pwd") String Pwd){
        Date today = new Date();
        ModelMapper mapper = new ModelMapper();

        UserDto userDto = userService.getUserByPwd(Pwd);
//        userDto.setModifiedAt(today);
//        userProducer.send("demo-source-connect", userDto);
    }

    /* 전체 사용자 목록 */
    @GetMapping("/users")
    public List<ResponseUser> getUsers(){
        Iterable<UserEntity> userList = userService.getUsersAll();
        List<ResponseUser> result = new ArrayList<>();

        userList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseUser.class));
        });

        System.out.println(result);
        return result;
    }

    /* 사용자 상세 보기(with 주문 목록) */
    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId){
        UserDto userDto = userService.getUserByUserId(userId);

        ResponseUser returnValue = new ModelMapper().map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }
}
