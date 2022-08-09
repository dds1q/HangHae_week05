package springc5.advanced.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springc5.advanced.controller.response.ResponseDto;
import springc5.advanced.domain.*;
import springc5.advanced.jwt.TokenProvider;
import springc5.advanced.repository.LikeCommentRepository;
import springc5.advanced.repository.LikePostRepository;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikePostRepository likePostRepository;
    private final LikeCommentRepository likeCommentRepository;
    private final PostService postService;
    private final TokenProvider tokenProvider;
    private final CommentService commentService;

    @Transactional
    public ResponseDto<?> doPostLike( Long id , HttpServletRequest request ) {

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = postService.isPresentPost( id );
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        LikePost likePost = likePostRepository.findByMemberAndPost( member , post )
                .orElse( null );

        if ( null != likePost ) {
            likePostRepository.delete( likePost );
            return ResponseDto.success( "like cancel success"  );
        }

        LikePost new_likePost = LikePost.builder()
                .member(member)
                .post(post)
                .build();
        likePostRepository.save( new_likePost );
        return ResponseDto.success( "like success"  );
    }

    @Transactional
    public ResponseDto<?> doCommentLike( Long id , HttpServletRequest request ) {

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Comment comment = commentService.isPresentComment( id );
        if (null == comment ) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        LikeComment likecomment = likeCommentRepository.findByMemberAndComment( member, comment )
                .orElse( null );

        if ( null != likecomment ) {
            likeCommentRepository.delete( likecomment );
            return ResponseDto.success( "like cancel success"  );
        }

        LikeComment new_likeComment = LikeComment.builder()
                .member(member)
                .comment(comment)
                .build();
        likeCommentRepository.save( new_likeComment );
        return ResponseDto.success( "like success"  );
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

//    public Object isvalidTokenAndMember(HttpServletRequest request ){
//        if (null == request.getHeader("Refresh-Token")) {
//            return ResponseDto.fail("MEMBER_NOT_FOUND",
//                    "로그인이 필요합니다.");
//        }
//
//        if (null == request.getHeader("Authorization")) {
//            return ResponseDto.fail("MEMBER_NOT_FOUND",
//                    "로그인이 필요합니다.");
//        }
//        Member member = validateMember(request);
//        if (null == member) {
//            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
//        }
//        return member;
//
//    }

}
