package com.onlineWorkers.onlineWorkersSocialService.controller;


import com.onlineWorkers.onlineWorkersSocialService.enums.Social;
import com.onlineWorkers.onlineWorkersSocialService.service.SocialService;

import com.onlineworkers.models.PageResponseDto;
import javassist.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;


@RestController
@CrossOrigin(value = "http://localhost:4200", allowedHeaders = "*")
public class SocialController {

    Logger logger = LoggerFactory.getLogger(SocialController.class);

    @Autowired
    private SocialService socialService;

    @RequestMapping("social/health")
    public ResponseEntity<?> health() {
        logger.info("online workers-social service");
        return new ResponseEntity<>("online workers-social service", HttpStatus.OK);
    }

    //click like
    @PutMapping("likes/profileownerid/{profileOwnerId}/likerid/{likerId}")
    public ResponseEntity<?> addLike(
            @PathVariable("profileOwnerId") long profileOwnerId,
            @PathVariable("likerId") long likerId) {
        try {
            return new ResponseEntity<>(socialService.addLike(profileOwnerId, likerId), HttpStatus.OK);
        } catch (HttpClientErrorException.BadRequest e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }

    }

    //add comment
    @PutMapping("comments/profileownerid/{profileOwnerId}/commenterid/{commenterId}")
    public ResponseEntity<?> addComment(
            @PathVariable("profileOwnerId") long profileOwnerId,
            @PathVariable("commenterId") long likerId,
            @RequestParam(required = false, defaultValue = "fantastic") String comment) {
        try {
            return new ResponseEntity<>(socialService.addComment(profileOwnerId, likerId, comment), HttpStatus.OK);
        } catch (HttpClientErrorException.BadRequest e) {
            return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
        }
    }

    //get all likes to  one user
    @GetMapping("profileownerid/{profileOwnerId}/likes")
    public ResponseEntity<?> getLikes(
            @PathVariable("profileOwnerId") long profileOwnerId) {
        try {
            return new ResponseEntity<>(socialService.getLikes(profileOwnerId), HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(Social.Zero.getValue(), HttpStatus.NOT_FOUND);
        }
    }

    //get all comments to one user
    @GetMapping("profileownerid/{profileOwnerId}/comments")
    public ResponseEntity<?> getComments(
            @PathVariable("profileOwnerId") long profileOwnerId) throws NotFoundException {
        return new ResponseEntity<>(socialService.getComments(profileOwnerId), HttpStatus.OK);
    }

    //all likes in online workers
    @GetMapping("/likes")
    public ResponseEntity<?> getAllLike() {
        return new ResponseEntity<>(socialService.getAllLike(), HttpStatus.OK);
    }


    //all comments in online workers
    @GetMapping("/comments")
    public ResponseEntity<?> getAllComments(
            @RequestParam(defaultValue = "100") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "id") String sortBy) {
        PageResponseDto pageResponse = socialService.getAllComments(pageSize, pageNumber, sortBy);
        if (pageResponse != null)
            return new ResponseEntity<>(pageResponse, HttpStatus.OK);
        return new ResponseEntity<>("null", HttpStatus.NO_CONTENT);
    }
       //get uesr count
    @GetMapping("profileOwnerId/{profileownerId}/count")
    public ResponseEntity<?> getUserCount(@PathVariable("profileownerId") long profileOwnerId) throws NotFoundException {
        return new ResponseEntity<>(socialService.getUserCount(profileOwnerId), HttpStatus.OK);

    }


    //get count
    @GetMapping("/count")
    public ResponseEntity<?> getCount() {
        return new ResponseEntity<>(socialService.getCount(), HttpStatus.OK);
    }
}
