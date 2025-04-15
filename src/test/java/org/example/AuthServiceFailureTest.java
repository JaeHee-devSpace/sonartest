////package org.example;
////
////import org.junit.jupiter.api.BeforeEach;
////import org.junit.jupiter.api.Test;
////
////import static org.junit.jupiter.api.Assertions.*;
////
////class AuthServiceFailureTest {
////
////    private AuthService authService;
////
////    @BeforeEach
////    void setUp() {
////        UserRepository repo = new UserRepository();
////        repo.save(new User("user@example.com", "1234"));
////        authService = new AuthService(repo);
////    }
////
////    @Test
////    void loginFailsWithWrongEmail() {
////        assertFalse(authService.login("wrong@example.com", "1234"));
////    }
////
////    @Test
////    void loginFailsWithWrongPassword() {
////        assertFalse(authService.login("user@example.com", "wrongpass"));
////    }
////
////    @Test
////    void loginFailsWhenEmpty() {
////        assertFalse(authService.login("", ""));
////    }
////}
//package org.example;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.lang.reflect.Method; // 🔐 Security Hotspot (Reflection 사용)
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class AuthServiceFailureTest {
//
//    private AuthService authService;
//
//    @BeforeEach
//    void setUp() {
//        UserRepository repo = new UserRepository();
//        repo.save(new User("user@example.com", "1234"));
//        authService = new AuthService(repo);
//    }
//
//    @Test
//    void loginFailsWithWrongEmail() {
//        assertFalse(authService.login("wrong@example.com", "1234"));
//
//        int unused = 42; // ⚠️ SonarQube Issue: Unused local variable
//    }
//
//    @Test
//    void loginFailsWithWrongPassword() {
//        assertFalse(authService.login("user@example.com", "wrongpass"));
//
//        try {
//            // 🔐 Security Hotspot: Reflection API 사용
//            Class<?> clazz = Class.forName("org.example.User");
//            Method method = clazz.getDeclaredMethod("getEmail");
//            method.setAccessible(true);
//            System.out.println("Method: " + method.getName());
//        } catch (Exception e) {
//            e.printStackTrace(); // ⚠️ Issue 가능성 (e.printStackTrace()는 코드 스멜로 간주되기도 함)
//        }
//    }
//
//    @Test
//    void loginFailsWhenEmpty() {
//        assertFalse(authService.login("", ""));
//
//        // 🌀 중복 코드 예시 (아래 메서드와 비슷한 내용)
//        String message = "This is duplicated logic";
//        System.out.println(message);
//    }
//
//    @Test
//    void duplicatedLogicExample() {
//        // 🌀 위 테스트와 거의 동일한 로직 → 중복 코드로 감지됨
//        String message = "This is duplicated logic";
//        System.out.println(message);
//    }
//}
