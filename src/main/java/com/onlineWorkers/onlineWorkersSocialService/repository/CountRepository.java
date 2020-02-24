package com.onlineWorkers.onlineWorkersSocialService.repository;

import com.onlineworkers.models.Count;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountRepository extends JpaRepository<Count,Integer> {

    @Query("select c from Count c where c.profileOwnerId=?1 ")
    Optional<Count> findByProfileOwnerId(long poi);

    @Query("select c.likes from Count c where c.profileOwnerId=?1 ")
    Object findLikes(long profileOwnerId);

    @Query("select sum(c.likes) from Count c")
    Optional<Integer> getAllLike();
}
