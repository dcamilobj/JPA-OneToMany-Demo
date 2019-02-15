package com.example.jpa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpa.exception.ResourceNotFoundException;
import com.example.jpa.model.Comment;
import com.example.jpa.repository.CommentRepository;
import com.example.jpa.repository.PostRepository;

@RestController
public class CommentController {

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@GetMapping("posts/{postId}/comments")
	public Page<Comment> getAllCommentByIdPost(@RequestParam(value="postId") Long postId, Pageable pageable){
		return commentRepository.findByPostId(postId, pageable);
	}
	
	@PostMapping("posts/{postId}/comments")
	public Comment createComment(@RequestParam(value="postId")Long idPost ,@Valid @RequestBody Comment comment){
		 return postRepository.findById(idPost).map(post ->{
			 comment.setPost(post);
			 return commentRepository.save(comment);
		 }).orElseThrow(()-> new ResourceNotFoundException("Post " + idPost + "not found"));
	}
	
	@GetMapping("posts/{postId}/comments/{commentId}")
	public Comment getCommentByIdAndIdPost(@RequestParam(value="postId")Long postId,
			@RequestParam(value="commentId") Long commentId){	 
		return commentRepository.findByIdAndPostId(commentId, postId).orElseThrow(
				()-> new ResourceNotFoundException("Comment " + commentId + "in post " + postId +" not found"));
	}
	
	@PutMapping("posts/{postId}/comments/{commentId}")
	public Comment updateComment(@RequestParam(value="postId") Long postId,
			@RequestParam(value="commentId") Long commentId, Comment commentRequest){
		
			return commentRepository.findByIdAndPostId(commentId, postId).map(comment -> {
				comment.setText(commentRequest.getText());
				return commentRepository.save(commentRequest);
			}).orElseThrow(()-> new ResourceNotFoundException("Comment " + commentId + "in post " + postId +" not found"));
	}
	
	@DeleteMapping("posts/{postId}/comments/{commentId}")
	public ResponseEntity<?> deleteComment(@RequestParam(value="postId") Long postId,
			@RequestParam(value="commentId") Long commentId){
		return commentRepository.findByIdAndPostId(commentId, postId).map(comment -> {
			commentRepository.delete(comment);
			return ResponseEntity.ok().build();
		}).orElseThrow(()-> new ResourceNotFoundException("Comment " + commentId + "in post " + postId +" not found")); 
	}

}
