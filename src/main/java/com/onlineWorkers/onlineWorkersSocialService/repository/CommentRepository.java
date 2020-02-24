package com.onlineWorkers.onlineWorkersSocialService.repository;

import com.onlineworkers.models.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CommentRepository extends JpaRepository<Comments,Integer> {

    Collection<Comments> findByLikeId(long id);


}
