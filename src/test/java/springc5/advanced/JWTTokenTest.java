package springc5.advanced;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import springc5.advanced.domain.Member;
import springc5.advanced.repository.MemberRepository;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//public class JWTTokenTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    MemberRepository memberRepository;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
//    private static final String BEARER = "Bearer ";
//
//
//    private static String nickname = "nickname123";
//    private static String password = "password123";
//
////    @Before
////    public void init(){
////        memberRepository.save(
////                Member.builder()
////                        .nickname(nickname)
////                        .password(passwordEncoder.encode(password))
////                                .build());
////    }
//
//    private Map<String, String> getAccessAndRefreshToken() throws Exception {
//
//        Map<String, String> map = new HashMap<>();
//        map.put("nickname", nickname);
//        map.put("password", password);
//
//        MvcResult result = mockMvc.perform(
//                        post("/api/member/login")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(map)))
//                .andReturn();
//
//        String accessToken = result.getResponse().getHeader("Authorization");
//        String refreshToken = result.getResponse().getHeader("Refresh-Token");
//
//        Map<String, String> tokenMap = new HashMap<>();
//        tokenMap.put("Authorization",accessToken);
//        tokenMap.put("Refresh-Token",refreshToken);
//
//        return tokenMap;
//    }
//
//    /**
//     * AccessToken : 존재하지 않음,
//     * RefreshToken : 존재하지 않음
//     */
//    @Test
//    public void Access_Refresh_모두_존재_X() throws Exception {
//        //when, then
//        mockMvc.perform(post("/api/auth/post"))//login이 아닌 다른 임의의 주소
//                .andExpect(status().isForbidden());
//    }
//
//    /**
//     * AccessToken : 유효,
//     * RefreshToken : 존재하지 않음
//     */
//    @Test
//    public void AccessToken만_보내서_인증() throws Exception {
//        //given
//        Map<String, String> accessAndRefreshToken = getAccessAndRefreshToken();
//        String accessToken= (String) accessAndRefreshToken.get("Authorization");
//
//        //when, then
//        mockMvc.perform(get("/api/auth/post").header("Authorization",BEARER + accessToken))//login이 아닌 다른 임의의 주소
//                .andExpectAll(status().isNotFound());//없는 주소로 보냈으므로 NotFound
//
//    }
//
//    /**
//     * AccessToken : 유효하지 않음,
//     * RefreshToken : 존재하지 않음
//     */
//    @Test
//    public void 안유효한AccessToken만_보내서_인증X_상태코드는_403() throws Exception {
//        //given
//        Map accessAndRefreshToken = getAccessAndRefreshToken();
//        String accessToken= (String) accessAndRefreshToken.get("Authorization");
//
//        //when, then
//        mockMvc.perform(get("/api/member/login"+"123").header("Authorization",accessToken+"1"))//login이 아닌 다른 임의의 주소
//                .andExpectAll(status().isForbidden());//없는 주소로 보냈으므로 NotFound
//    }
//
//    @Test
//    public void 유효한RefreshToken이랑_유효한AccessToken_같이보냈을때_AccessToken_재발급_200() throws Exception {
//        //given
//        Map<String, String> accessAndRefreshToken = getAccessAndRefreshToken();
//        String accessToken = accessAndRefreshToken.get("Authorization");
//        String refreshToken= accessAndRefreshToken.get("Refresh-Token");
//
//        //when, then
//        MvcResult result = mockMvc.perform(get("/api/auth/post")
//                        .header("Refresh-Token", refreshToken)
//                        .header("Authorization", BEARER + accessToken))
//                .andExpect(status().isOk())
//                .andReturn();
//
////        String responseAccessToken = result.getResponse().getHeader("Authorization");
////        String responseRefreshToken = result.getResponse().getHeader("Refresh-Token");
////
////        String subject = JWT.require(Algorithm.HMAC512(secret)).build().verify(responseAccessToken).getSubject();
////
////        assertThat(subject).isEqualTo(ACCESS_TOKEN_SUBJECT);
////        assertThat(responseRefreshToken).isNull();//refreshToken은 재발급되지 않음
//    }
//
//
//}
