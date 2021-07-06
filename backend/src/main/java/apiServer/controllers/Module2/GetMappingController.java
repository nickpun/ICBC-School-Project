package apiServer.controllers.Module2;

import database.ModuleTwoQueries;
import database.data_objects.Category;
import database.data_objects.Post;
import database.data_objects.Comment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@Controller("GetModuleTwoController")
public class GetMappingController {

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<Category> getCategory(@PathVariable(name = "categoryId") Integer categoryId) {
        try {
            Category data = ModuleTwoQueries.getCategory(categoryId);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable(name = "postId") Integer postId) {
        try {
            Post data = ModuleTwoQueries.getPost(postId);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/comments/{postId}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable(name = "postId") Integer postId) {
        try {
            List<Comment> data = ModuleTwoQueries.getComments(postId);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getPreviewCategory() {
        try {
            List<Category> data = ModuleTwoQueries.getPreviewCategories();
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
