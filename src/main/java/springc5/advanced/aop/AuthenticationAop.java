//package springc5.advanced.aop;
//
//import lombok.RequiredArgsConstructor;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//import springc5.advanced.controller.response.ResponseDto;
//import springc5.advanced.domain.Member;
//import springc5.advanced.jwt.TokenProvider;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Aspect
//@Component
//@RequiredArgsConstructor
//public class AuthenticationAop {
//
//    private final TokenProvider tokenProvider;
//
//    @Around("execution(public * springc5.advanced.service.CommentService*(..))" +
//            " || execution(public * springc5.advanced.service.LikeService*(..)) " +
//            " || execution(public * springc5.advanced.service.PostService*(..))" +
//            " && args(member)")
//    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
//
//        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//
//        if (null == request.getHeader("Refresh-Token")) {
//            return ResponseDto.fail("MEMBER_NOT_FOUND",
//                    "로그인이 필요합니다.");
//        }
//
//        if (null == request.getHeader("Authorization")) {
//            return ResponseDto.fail("MEMBER_NOT_FOUND",
//                    "로그인이 필요합니다.");
//        }
//
//        Member member = validateMember(request);
//        if (null == member) {
//            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
//        }
//
//        Object result = joinPoint.proceed( new Object[] { member });
//
//        return result;
//
//    }
//
//    @Transactional
//    public Member validateMember(HttpServletRequest request) {
//        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
//            return null;
//        }
//        return tokenProvider.getMemberFromAuthentication();
//    }
//}