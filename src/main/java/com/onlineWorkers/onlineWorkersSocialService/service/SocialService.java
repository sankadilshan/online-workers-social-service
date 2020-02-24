package com.onlineWorkers.onlineWorkersSocialService.service;

import com.onlineworkers.models.CommentResponseDto;
import com.onlineworkers.models.Count;
import com.onlineworkers.models.PageResponseDto;

public interface SocialService {
    Object addLike(long profileOwnerId, long likerId);

    Object getLikes(long profileOwnerId);

    Object addComment(long profileOwnerId, long likerId, String comment);

    Object getComments(long profileOwnerId);

    Object getAllLike();

    PageResponseDto getAllComments(Integer pageSize, Integer pageNumber, String sortBy);

    Object getCount();

    Count getUserCount(long profileOwnerId);
}
