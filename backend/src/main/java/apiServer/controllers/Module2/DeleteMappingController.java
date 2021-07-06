package apiServer.controllers.Module2;

import database.ModuleTwoQueries;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;


@RestController
@Controller("DeleteModuleTwoController")
public class DeleteMappingController {
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable(name = "categoryId") Integer categoryId) {
        String body = "Success";
        HttpStatus status = HttpStatus.OK;
        try {
            ModuleTwoQueries.deleteCategory(categoryId);
        } catch (SQLException e) {
            body = "Failure";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(body, status);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "postId") Integer postId) {
        String body = "Success";
        HttpStatus status = HttpStatus.OK;
        try {
            ModuleTwoQueries.deletePost(postId);
        } catch (SQLException e) {
            body = "Failure";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(body, status);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable(name = "commentId") Integer commentId) {
        String body = "Success";
        HttpStatus status = HttpStatus.OK;
        try {
            ModuleTwoQueries.deleteComment(commentId);
        } catch (SQLException e) {
            body = "Failure";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(body, status);
    }
}
