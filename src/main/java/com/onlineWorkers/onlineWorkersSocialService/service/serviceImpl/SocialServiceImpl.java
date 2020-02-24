package com.onlineWorkers.onlineWorkersSocialService.service.serviceImpl;

import com.onlineWorkers.onlineWorkersSocialService.enums.Response;
import com.onlineWorkers.onlineWorkersSocialService.enums.Social;
import com.onlineWorkers.onlineWorkersSocialService.repository.CommentRepository;
import com.onlineWorkers.onlineWorkersSocialService.repository.CountRepository;
import com.onlineWorkers.onlineWorkersSocialService.repository.LikeRepository;
import com.onlineWorkers.onlineWorkersSocialService.service.SocialService;
import com.onlineworkers.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;


@Transactional
@Service
public class SocialServiceImpl implements SocialService {
    Logger logger = LoggerFactory.getLogger(SocialServiceImpl.class);
    @Autowired
    private CountRepository countRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${user.url}")
    private String _url;

    @Override
    public Object addLike(long profileOwnerId, long likerId) {
        StatusDto statusDto = checkLikes(profileOwnerId, likerId);
        String username = getUsername(likerId);
        logger.info(statusDto.toString());
        if (!statusDto.isCountStatus()) {
            Count countSave = countRepository.save(new Count(Social.ONE.getValue(), Social.Zero.getValue(), profileOwnerId));
            likeRepository.save(new Likes(likerId, username, countSave.getId(), true));
            return HttpStatus.OK;
        }
        if (statusDto.isCountStatus()) {
            if (statusDto.isLikesStatus()) {
                logger.info("already liked");
                return Response.ALREADY_LIKED;
            } else {
                Optional<Count> count = countRepository.findByProfileOwnerId(profileOwnerId);
                count.get().setLikes(1 + count.get().getLikes());
                countRepository.save(count.get());
                likeRepository.save(new Likes(likerId, username, count.get().getId(), true));
                return HttpStatus.OK;

            }
        }
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public Object getLikes(long profileOwnerId) {
        return countRepository.findLikes(profileOwnerId);
    }


    @Override
    public Object addComment(long profileOwnerId, long commenterId, String comment) {
        Optional<Count> count = countRepository.findByProfileOwnerId(profileOwnerId);
        String username = getUsername(commenterId);
        if (count.isPresent()) {
            count.get().setComments(1 + count.get().getComments());
            countRepository.save(count.get());
            commentRepository.save(new Comments(commenterId, username, comment, count.get().getId()));
            return HttpStatus.OK;
        }
        if (!count.isPresent()) {
            Count countSave = countRepository.save(new Count(Social.Zero.getValue(), Social.ONE.getValue(), profileOwnerId));
            commentRepository.save(new Comments(commenterId, username, comment, countSave.getId()));
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public Object getComments(long profileOwnerId) {
        Optional<Count> count = countRepository.findByProfileOwnerId(profileOwnerId);
        if (count.isPresent()) {
            Collection<Comments> allComments = commentRepository.findByLikeId(count.get().getId());
            return new CommentResponseDto(count.get().getComments(), allComments);
        }
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public Object getAllLike() {
        Optional<Integer> allLike = countRepository.getAllLike();
        return allLike.isPresent()? allLike:0;
    }

    @Override
    public PageResponseDto getAllComments(Integer pageSize, Integer pageNumber, String sortBy) {
        Pageable pageable= PageRequest.of(pageNumber,pageSize,
                Sort.by(sortBy).descending());
        Page<Comments> pagedResults= commentRepository.findAll(pageable);

        if(!pagedResults.getContent().isEmpty()){
            logger.info("re"+pagedResults.toString());
               PageResponseDto pageResponse = new PageResponseDto(
                    pageSize,
                    pageNumber,
                    pagedResults.getTotalPages(),
                    pagedResults.getNumberOfElements(),
                    pagedResults.getTotalElements(),
                    pagedResults.getContent());

            return pageResponse;
        }
        return null;
    }

    @Override
    public Object getCount() {
        return countRepository.findAll();
    }

    @Override
    public Count getUserCount(long profileOwnerId) {
        Optional<Count> count = countRepository.findByProfileOwnerId(profileOwnerId);
        if (count.isPresent()){
            return count.get();
        }
        return new Count(0,0,profileOwnerId);
    }


    private StatusDto checkLikes(long poi, long li) {
        boolean likes_status = false, count_status;
        Optional<Count> count = countRepository.findByProfileOwnerId(poi);
        logger.info(count.toString());
        count_status = (count.isPresent()) ? true : false;
        if (count_status) {
            Optional<Likes> likes = likeRepository.findByLikerId(count.get().getId(), li);
            likes_status = (likes.isPresent()) ? true : false;
            logger.info(likes.toString());
        }
        return new StatusDto(count_status, likes_status);
    }

    private String getUsername(long userId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        String username = restTemplate.exchange(_url + userId, HttpMethod.GET, httpEntity, String.class).getBody();
        logger.info(username);
        return username;
    }
}
