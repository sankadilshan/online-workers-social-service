package com.onlineWorkers.onlineWorkersSocialService.repository;

import com.onlineworkers.models.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Likes,Integer> {

    @Query("select l.status from Likes l where l.likeId=?1 and l.likerId=?2")
    Optional<Likes> findByLikerId(long id, long li);

}
