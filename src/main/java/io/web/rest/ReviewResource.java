package io.web.rest;

import io.config.Constants;
import io.domain.MatchingEntity;
import io.domain.Review;
import io.domain.User;
import io.repository.MatchingEntityRepository;
import io.repository.MatchingRepository;
import io.repository.ReviewRepository;
import io.service.UserService;
import io.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.domain.Review}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ReviewResource {

    private final Logger log = LoggerFactory.getLogger(ReviewResource.class);

    private static final String ENTITY_NAME = "review";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReviewRepository reviewRepository;

    private final UserService userService;

    private final MatchingRepository matchingRepository;

    private final MatchingEntityRepository matchingEntityRepository;

    public ReviewResource(ReviewRepository reviewRepository, UserService userService, MatchingRepository matchingRepository, MatchingEntityRepository matchingEntityRepository) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.matchingRepository = matchingRepository;
        this.matchingEntityRepository = matchingEntityRepository;
    }

    /**
     * {@code POST  /reviews} : Create a new review.
     *
     * @param review the review to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new review, or with status {@code 400 (Bad Request)} if the review has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reviews")
    public ResponseEntity<Review> createReview(@RequestBody Review review) throws URISyntaxException {
        log.debug("REST request to save Review : {}", review);
        if (review.getId() != null) {
            throw new BadRequestAlertException("A new review cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Optional<User> optLogged =userService.getUserWithAuthorities();
        if(!optLogged.isPresent()){
            throw new BadRequestAlertException("No user logged", ENTITY_NAME, "");
        }
        User logged = optLogged.get();
        review.setReviewer(logged);
        List<MatchingEntity> matchingEntities = matchingEntityRepository.findByForUserIdIsCurrentUser();
        boolean isMatchingBetweenUsers = false;
        for (MatchingEntity me : matchingEntities) {
            if (me.getForUser().getLogin().equals(logged.getLogin()) && me.getItem().getOwner().getLogin().equals(review.getUser().getLogin())) {
                isMatchingBetweenUsers = true;
            }
        }
        if(!isMatchingBetweenUsers) {
            throw new BadRequestAlertException("You have no exchanges with this user", ENTITY_NAME, "");
        }
        Review result = reviewRepository.save(review);
        return ResponseEntity.created(new URI("/api/reviews/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reviews} : Updates an existing review.
     *
     * @param review the review to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated review,
     * or with status {@code 400 (Bad Request)} if the review is not valid,
     * or with status {@code 500 (Internal Server Error)} if the review couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reviews")
    public ResponseEntity<Review> updateReview(@RequestBody Review review) throws URISyntaxException {
        log.debug("REST request to update Review : {}", review);
        if (review.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Review result = reviewRepository.save(review);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, review.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /reviews} : get all the reviews.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reviews in body.
     */
    @GetMapping("/reviews")
    public List<Review> getAllReviews() {
        log.debug("REST request to get all Reviews");
        return reviewRepository.findAll();
    }


    /**
     * {@code GET  /reviews} : get all the reviews of given user.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reviews in body.
     */
    @GetMapping("/reviews/getbyuser/{login:" + Constants.LOGIN_REGEX + "}")
    public List<Review> getReviewsByUser(@PathVariable String login) {
        log.debug("REST request to get all Reviews of given user");
        return reviewRepository.findByUserLogin(login);
    }

    /**
     * {@code GET  /reviews/:id} : get the "id" review.
     *
     * @param id the id of the review to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the review, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reviews/{id}")
    public ResponseEntity<Review> getReview(@PathVariable Long id) {
        log.debug("REST request to get Review : {}", id);
        Optional<Review> review = reviewRepository.findByIdFetch(id);
        return ResponseUtil.wrapOrNotFound(review);
    }

    /**
     * {@code DELETE  /reviews/:id} : delete the "id" review.
     *
     * @param id the id of the review to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        log.debug("REST request to delete Review : {}", id);
        reviewRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
