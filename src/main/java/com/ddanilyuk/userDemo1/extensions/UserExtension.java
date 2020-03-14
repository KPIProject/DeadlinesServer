package com.ddanilyuk.userDemo1.extensions;

//package com.ddanilyuk.userDemo1.extensions;
//
////public class UserExtension {
////}
////
////package com.mkyong.error;
//
//import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
//import org.springframework.boot.web.reactive.error.ErrorAttributes;
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.context.request.RequestAttributes;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
public class UserExtension extends RuntimeException {

    public UserExtension(String username) {
        super(username);
    }
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
//
//

//@ResponseStatus(HttpStatus.NOT_FOUND)
//public class UserExtension extends RuntimeException {
//    public UserExtension(String message) {
//        super(message);
//    }
//}
//
