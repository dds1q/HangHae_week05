package springc5.advanced.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springc5.advanced.controller.request.CommentRequestDto;
import springc5.advanced.controller.response.ResponseDto;
import springc5.advanced.service.CommentService;
import springc5.advanced.service.LikeService;

import javax.servlet.http.HttpServletRequest;

@Validated
@RequiredArgsConstructor
@RestController
public class LikeController {

    private final LikeService likeService;

    @RequestMapping(value = "/api/auth/like/post/{id}", method = RequestMethod.POST)
    public ResponseDto<?> doPostLike( @PathVariable Long id, HttpServletRequest request) {
        return likeService.doPostLike( id , request );
    }

//    @RequestMapping(value = "/api/auth/like/post/{id}", method = RequestMethod.DELETE)
//    public ResponseDto<?> cancelPostLike( @PathVariable Long id, HttpServletRequest request) {
//        return likeService.cancelPostLike( id , request );
//    }

    @RequestMapping(value = "/api/auth/like/comment/{id}", method = RequestMethod.POST)
    public ResponseDto<?> doCommentLike( @PathVariable Long id, HttpServletRequest request) {
        return likeService.doCommentLike( id , request );
    }

//    @RequestMapping(value = "/api/auth/like/post/{id}", method = RequestMethod.DELETE)
//    public ResponseDto<?> cancelCommentLike( @PathVariable Long id, HttpServletRequest request) {
//        return likeService.cancelCommentLike( id , request );
//    }

}