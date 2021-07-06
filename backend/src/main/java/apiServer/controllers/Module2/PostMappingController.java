package apiServer.controllers.Module2;

import apiServer.controllers.Module2.JSONRequestSpec.CategoryRequest;
import apiServer.controllers.Module2.JSONRequestSpec.CommentRequest;
import apiServer.controllers.Module2.JSONRequestSpec.ForumPostRequest;
import database.ModuleTwoQueries;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@Controller("PostModuleTwoController")
public class PostMappingController {
    @PostMapping("/categories")
    public ResponseEntity<Integer> addCategory(@RequestBody CategoryRequest request) {
        int catId;
        HttpStatus status;
        try {
            catId = ModuleTwoQueries.addCategory(request.getTitle());
            status = HttpStatus.OK;
        } catch (SQLException e) {
            catId = -1;
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(catId, status);
    }

    @PostMapping("/posts")
    public ResponseEntity<Integer> addPost(@RequestBody ForumPostRequest request) {
        int postId;
        HttpStatus status;
        try {
            postId = ModuleTwoQueries.addPost(request.getUserId(), request.getCategoryId(),
                    request.getTitle(),request.getPostDate(), request.getValue());
            status = HttpStatus.OK;
        } catch (SQLException e) {
            postId = -1;
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(postId, status);
    }

    @PostMapping("/comments")
    public ResponseEntity<Integer> addComment(@RequestBody CommentRequest request) {
        int commentId;
        HttpStatus status;
        try {
            commentId = ModuleTwoQueries.addComment(request.getUserId(), request.getPostId(),
                    request.getCommentDate(), request.getValue());
            status = HttpStatus.OK;
        } catch (SQLException e) {
            commentId = -1;
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(commentId, status);
    }

}
