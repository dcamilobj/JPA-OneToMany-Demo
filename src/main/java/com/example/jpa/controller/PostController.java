package com.example.jpa.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpa.exception.ResourceNotFoundException;
import com.example.jpa.model.Post;
import com.example.jpa.repository.PostRepository;

@RestController
public class PostController {
	
	@Autowired
	private PostRepository postRepository;
	
	@GetMapping("/posts")
	public List<Post> getAllPosts(){
		return postRepository.findAll();
	}
	
	@PostMapping("/posts")
	public Post createPost(@Valid @RequestBody Post post){
		return postRepository.save(post);
	}
	
	@GetMapping("/posts/{id}")
	public Post getPostById(@RequestParam(value="id") Long idPost){
		return postRepository.findById(idPost).orElseThrow(
				()->new ResourceNotFoundException("PostId " + idPost + " not found"));
	}
	
	@PutMapping("/posts/{id}")
	public Post updatePost(@RequestParam(value="id") Long idPost, @Valid @RequestBody Post postDetails){
		Post post =  postRepository.findById(idPost).orElseThrow(
				()->new ResourceNotFoundException("PostId " + idPost + " not found"));
		
		post.setTitle(postDetails.getTitle());
		post.setDescription(postDetails.getDescription());
		post.setContent(postDetails.getContent());
		
		return postRepository.save(post);
	}
	
	@DeleteMapping("/posts/{id}")
	public ResponseEntity<?> deletePost(@RequestParam(value="id") Long idPost){
		Post post =  postRepository.findById(idPost).orElseThrow(
				()->new ResourceNotFoundException("PostId " + idPost + " not found"));
		postRepository.delete(post);
		return ResponseEntity.ok().build();
	}
	

}
