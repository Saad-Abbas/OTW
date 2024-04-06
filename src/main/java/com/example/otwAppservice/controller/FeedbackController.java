package com.example.otwAppservice.controller;


import com.example.otwAppservice.dto.DepartmentDTO;
import com.example.otwAppservice.dto.FeedbackDTO;
import com.example.otwAppservice.entity.Country;
import com.example.otwAppservice.entity.User;
import com.example.otwAppservice.entity.feedback.Feedback;
import com.example.otwAppservice.entity.feedback.FeedbackHistory;
import com.example.otwAppservice.service.feedbackService.FeedbackService;
import com.example.otwAppservice.service.userService.UserService;
import com.example.otwAppservice.utils.Messages;
import com.example.otwAppservice.utils.enums.StatusEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/feedback")
public class FeedbackController {

    private static Logger LOGGER = LogManager.getLogger(FeedbackController.class);
    @Autowired
    FeedbackService feedbackService;
    @Autowired
    UserService userService;

    // Save FeedBack Details
    @PostMapping("/save")
    public ResponseEntity saveFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        LOGGER.info(" ---- ADD USER FEEDBACK  [START] ----");
        ResponseEntity r = null;
        try {
            LOGGER.info("FeedBack Object : [" + feedbackDTO.toString() + "]");
            if (feedbackDTO.getUserId() > 0 &&
                    feedbackDTO.getFeedbackText() != null && !feedbackDTO.getFeedbackText().isEmpty()) {
                Feedback feedback = null;
                User user = userService.getUserById(feedbackDTO.getUserId());

                if (user == null) {
                    LOGGER.info("No User found with this userId : " + feedbackDTO.getUserId() + "");
                    return ResponseEntity.ok().body(new Messages<>().setMessage("No User found with this userId : " + feedbackDTO.getUserId() + "").setData(null).setStatus(HttpStatus.BAD_REQUEST.value()).setCode(String.valueOf(HttpStatus.BAD_REQUEST)));

                }
                feedback = new Feedback();
                String feedbackCode = "FB-" + System.currentTimeMillis() + "-" + user.getId();
                feedback.setFeedbackCode(feedbackCode);
                feedback.setFeedbackText(feedbackDTO.getFeedbackText());
                feedback.setFeedbackType(feedbackDTO.getFeedbackType());
                feedback.setFeedbackStatus(StatusEnum.PENDING.getStatusText());
                if (feedbackDTO.getFeedbackType().equalsIgnoreCase("suggestion")) {
                    feedback.setFeedbackStatus(StatusEnum.APPROVED.getStatusText());
                }

                feedback.setActive(true);
                feedback.setUser(user);
                feedback = feedbackService.saveFeedback(feedback);
                if (feedback != null) {

                    LOGGER.info("Feedback added successfully");
                    FeedbackHistory feedbackHistory = new FeedbackHistory();

                    feedbackHistory.setFeedbackId(feedback.getId());
                    feedbackHistory.setStatus(feedback.getFeedbackStatus());
                    feedbackHistory.setCreatedBy(user.getPhoneNumber());
                    feedbackHistory.setReason(feedback.getFeedbackText());
                    feedbackHistory = feedbackService.updateFeedbackStatus(feedbackHistory);
                    if (feedbackHistory != null) {
                        LOGGER.info("Store Feedback Status updated successfully");
                        r = ResponseEntity.ok().body(new Messages<>().setMessage("Feedback Submit Successfully").setData(feedback).setStatus(HttpStatus.CREATED.value()).setCode(String.valueOf(HttpStatus.CREATED)));

                    }
                } else {
                    r = ResponseEntity.ok().body(new Messages<>().setMessage("Failed to Submit Feedback").setData(null).setStatus(HttpStatus.BAD_REQUEST.value()).setCode(String.valueOf(HttpStatus.BAD_REQUEST)));

                }


            } else {
                r = ResponseEntity.badRequest().body(new Messages<>().setMessage("The request body is missing some required parameters.").setData(null).setStatus(HttpStatus.BAD_REQUEST.value()).setCode(String.valueOf(HttpStatus.BAD_REQUEST)));

            }


        } catch (Exception e) {

            r = ResponseEntity.badRequest().body(new Messages<>().setMessage(e.getMessage()).setData(null).setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));

        }
        LOGGER.info(" ---- ADD USER FEEDBACK  [END] ----");
        return r;
    }

    // Get All Active Feedback List
    @PostMapping("getAllFeedback")
    public ResponseEntity getAllFeedbackList(@RequestBody FeedbackDTO feedbackDTO) {
        ResponseEntity r;
        LOGGER.info("Fetch All Feedback List [START]");
        List<Feedback> feedbackList = feedbackService.getAllFeedbackByUserId(feedbackDTO.getUserId());
        try {
            if (!feedbackList.isEmpty()) {

                r = ResponseEntity.ok().body(new Messages<>().setMessage("Feedback List Fetched Successfully").setData(feedbackList).setStatus(HttpStatus.OK.value()).setCode(String.valueOf(HttpStatus.OK)));
//                r = ResponseEntity.ok().body(departmentByCode);
//
            } else {
                r = ResponseEntity.badRequest().body(new Messages<>().setMessage("Failed to Fetch Feedback List").setData(null).setStatus(HttpStatus.OK.value()).setCode(String.valueOf(HttpStatus.OK)));

            }
        } catch (Exception e) {
            r = ResponseEntity.ok().body(new Messages<>().setMessage("Failed to Fetch Feedback List").setData(null).setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setCode(String.valueOf(HttpStatus.OK)));

        }
        LOGGER.info("Fetch All Feedback List [END]");
        return r;
    }


}
