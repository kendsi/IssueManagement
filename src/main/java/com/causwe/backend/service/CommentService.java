package com.causwe.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.causwe.backend.exceptions.UnauthorizedException;
import com.causwe.backend.model.Comment;
import com.causwe.backend.model.Issue;
import com.causwe.backend.model.User;
import com.causwe.backend.repository.CommentRepository;

import java.util.List;

public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private IssueService issueService;

    public List<Comment> getAllComments(Long issueId) {
        Issue issue = issueService.getIssueById(issueId);
        return commentRepository.findByIssue(issue);
    }

    public Comment addComment(Long issueId, Comment commentData, Long memberId) {
        User currentUser = userService.getUserById(memberId);
        if (currentUser == null) {
            throw new UnauthorizedException("User not logged in");
        }

        Issue issue = issueService.getIssueById(issueId);

        if (issue != null) {
            Comment comment = new Comment(issue, currentUser, commentData.getContent());

            return commentRepository.save(comment);
        } else {
            return null;
        }
    }

    // TODO 권한에 따라 삭제
    public boolean deleteComment(Long id, Long memberId) {
        User currentUser = userService.getUserById(memberId);
        if (currentUser == null) {
            throw new UnauthorizedException("User not logged in");
        }

        Optional<Comment> comment = commentRepository.findById(id);

        if (comment.isPresent()) {
            commentRepository.deleteById(id);
            return true;
        }
        else {
            return false;
        }
    }
}
