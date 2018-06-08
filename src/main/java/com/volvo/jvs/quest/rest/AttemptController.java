package com.volvo.jvs.quest.rest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.volvo.jvs.quest.database.entity.Answer;
import com.volvo.jvs.quest.database.entity.Attempt;
import com.volvo.jvs.quest.database.entity.Question;
import com.volvo.jvs.quest.database.entity.Response;
import com.volvo.jvs.quest.database.entity.Section;
import com.volvo.jvs.quest.database.entity.SectionMaturity;
import com.volvo.jvs.quest.database.entity.Survey;
import com.volvo.jvs.quest.database.entity.UserGroup;
import com.volvo.jvs.quest.database.repository.AttemptRepository;
import com.volvo.jvs.quest.database.repository.SurveyRepository;
import com.volvo.jvs.quest.database.repository.UserGroupRepository;
import com.volvo.jvs.quest.service.email.EmailService;
import com.volvo.jvs.quest.util.rest.RestUtil;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping(value="/attempts",
                produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
public class AttemptController extends GenericController{

	@Value("${spring.security.secret}")
    private String secret;
	
	@Autowired
	private AttemptRepository attemptRepository;
	
	@Autowired
	private UserGroupRepository userGroupRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired	
	private SurveyRepository surveyRepository;
	
	@Autowired
	private RestUtil restUtil;
		
	@GetMapping("/{attemptId}/summary")
	@JsonView(View.ReportView.class)
	public Resource<Attempt> getAttemptSummary(@PathVariable("attemptId") String attemptId) {
		Attempt attempt = getAttempt(getAttemptRepository().findOne(attemptId));
		if(attempt != null && attempt.getSurvey() != null) {
			for(Section section : attempt.getSurvey().getSections()) {
				int sectionMaturityNumber = 0;
				int correctPercentage = section.getMaxScore() > 0 ? (section.getScore() / section.getMaxScore() * 100) : 0;
				for(SectionMaturity sectionMaturity : section.getMaturities()) {
					if(correctPercentage >= sectionMaturity.getPercentageLevel()) {
						sectionMaturityNumber += 1;
					}
				}
				section.setMaturityNumber(sectionMaturityNumber);
			}
			return getRestUtil().getResource(attempt);
		}
		return null;
	}
	
	@PostMapping("/")
	public Resource<Boolean> createAttempt(@RequestBody Map<String, String> surveyRequest) {
		Integer surveyId = Integer.valueOf(surveyRequest.get("surveyId"));
		String email = surveyRequest.get("email");
		String name = surveyRequest.get("name");
		Boolean exisitingAttempt = Boolean.valueOf(surveyRequest.get("existingAttempt"));
		
		// get user or create new
		UserGroup userGroup = getUserGroupRepository().findByEmail(email);
		if(userGroup == null) {
			userGroup = new UserGroup();
			userGroup.setEmail(email);	
		}		
		if(name != null && !name.isEmpty()) {
			userGroup.setName(name);
		}
		getUserGroupRepository().save(userGroup);
		
		// get survey
		Survey survey = getSurveyRepository().findOne(surveyId);
		if(survey != null) {			
			// get attempts
			int attemptNumber = 1;
			List<Attempt> attempts = getAttemptRepository().findByUserGroupIdSurveyId(userGroup.getUserGroupId(), surveyId);
			if(attempts != null && !attempts.isEmpty()) {
				if(exisitingAttempt) {
					attemptNumber = attempts.get(0).getAttemptNumber();
				}
				else {
					attemptNumber = attempts.get(0).getAttemptNumber() + 1;
				}
			}
			
			// get attempt or create new
			Attempt attempt;
			if(attempts != null && !attempts.isEmpty() && exisitingAttempt) {
				attempt = attempts.get(0);
			}
			else {
				// create attempt id
				String attemptId = Jwts.builder()
		                .setSubject(String.valueOf(survey.getSurveyId()))
		                .setIssuer(userGroup.getEmail())
		                .setAudience(String.valueOf(attemptNumber))
		                .signWith(SignatureAlgorithm.HS512, getSecret().getBytes())
		                .compact();
				int firstPoint = attemptId.lastIndexOf(".") + 1;
				int lastPoint = firstPoint + (attemptId.length() - firstPoint < 100 ? (attemptId.length() - firstPoint) : 100); 
				attemptId = attemptId.substring(firstPoint, lastPoint);
				
				// create new attempt
				attempt = new Attempt();		
				attempt.setAttemptId(attemptId);
				attempt.setSurvey(survey);
				attempt.setUserGroup(userGroup);
				attempt.setAttemptNumber(attemptNumber);
				getAttemptRepository().save(attempt);
			}
			
			// send e-mail with link
			getEmailService().sendSimpleMessage(email, "Start survey: " + survey.getName(), "Start survey: http://localhost:3000/?attemptId="+attempt.getAttemptId());
		}
		else {
			throw new RuntimeException("Survey with id: " + surveyId + " doesn't exist");
		}
		return getRestUtil().getResource(Boolean.TRUE);
	}

	@GetMapping("/{attemptId}")
	@JsonView(View.DetailedView.class)
	public Resource<Attempt> getAttempt(@PathVariable("attemptId") String attemptId) {				
		return getRestUtil().getResource(getAttempt(getAttemptRepository().findOne(attemptId)));
	}
	
	protected Attempt getAttempt(Attempt attempt) {		
		if(attempt != null) {
			Question firstUnansweredQuestion = findUnansweredQuestion(attempt.getSurvey(), attempt.getResponses());
			if(firstUnansweredQuestion != null) {
				return getAttemptForQuestion(attempt, firstUnansweredQuestion.getQuestionId());
			}
		}		
		return null;
	}
	
	@GetMapping("/{attemptId}/sections/{sectionId}")
	@JsonView(View.DetailedView.class)
	public Resource<Attempt> getAttemptForSection(@PathVariable("attemptId") String attemptId, @PathVariable("sectionId") Integer sectionId) {				
		Attempt attempt = getAttemptRepository().findOne(attemptId);
		if(attempt != null && attempt.getSurvey() !=null) {
			Question firstUnansweredQuestionInSection = null;
			for(Section section : attempt.getSurvey().getSections()) {
				if(section.getSectionId().equals(sectionId)) {
					firstUnansweredQuestionInSection = findUnansweredQuestion(section, attempt.getResponses());
				}
			}
			if(firstUnansweredQuestionInSection != null) {
				return getRestUtil().getResource(getAttemptForQuestion(attempt, firstUnansweredQuestionInSection.getQuestionId()));
			}
		}		
		return null;
	}
	
	@GetMapping("/{attemptId}/questions/{questionId}")
	@JsonView(View.DetailedView.class)
	public Resource<Attempt> getAttemptForQuestion(@PathVariable("attemptId") String attemptId, @PathVariable("questionId") Integer questionId){	
		return getRestUtil().getResource(getAttemptForQuestion(getAttemptRepository().findOne(attemptId), questionId));
	}
	
	protected Attempt getAttemptForQuestion(Attempt attempt, Integer questionId){
		if(attempt != null && attempt.getSurvey() != null) {
			// find question, previous question and next question
			// find section for question which was found
			// calculate number of responses for sections and whole survey
			// calculate number of questions for sections and whole survey
			// calculate score for sections
			String attemptId = attempt.getAttemptId();
			Question foundQuestion = null;
			Question previousQuestion = null;
			Question nextQuestion = null;
			Section foundSection = null;
			int surveyResponses = 0;
			int surveyScore = 0;
			int surveyMaxScore = 0;
			for(Section section : attempt.getSurvey().getSections()) {	
				int sectionResponses = 0;
				int sectionScore = 0;
				int sectionMaxScore = 0;
				for(Question question : section.getQuestions()) {	
					boolean foundResponse = false;
					int questionMaxScore = 0;
					if(foundQuestion != null && nextQuestion == null) {
						nextQuestion = question;
					}
					if(question.getQuestionId().equals(questionId)) {
						foundQuestion = question;
						foundSection = section;
					}
					for(Answer answer : question.getAnswers()) {
						if(answer.getScore() > questionMaxScore) {
							questionMaxScore = answer.getScore();
						}
						for(Response response : attempt.getResponses()) {
							if(response.getAnswerId().equals(answer.getAnswerId())) {	
								if(!foundResponse) {
									sectionResponses++;
									foundResponse = true;
								}
								answer.setResponse(response);
								sectionScore += answer.getScore();
								break;
							}
						}
					}
					if(foundQuestion == null) {
						previousQuestion = question;
					}
					sectionMaxScore += questionMaxScore;
				}				
				section.setNumberOfResponses(sectionResponses);	
				section.setMaxScore(sectionMaxScore);
				section.setScore(sectionScore);
				surveyResponses += sectionResponses;
				surveyScore += sectionScore;
				surveyMaxScore += sectionMaxScore;
			}
			attempt.getSurvey().setNumberOfResponses(surveyResponses);	
			attempt.getSurvey().setMaxScore(surveyMaxScore);
			attempt.getSurvey().setScore(surveyScore);
			
			// add links to next and previous questions
			if(foundQuestion != null){
				Resource<Question> resultQuestion = getRestUtil().getResource(foundQuestion);
				if(previousQuestion != null) {
					resultQuestion.add(linkTo(methodOn(AttemptController.class).getAttemptForQuestion(
							attemptId, previousQuestion.getQuestionId())).withRel("previous"));
				}
				if(nextQuestion != null) {
					resultQuestion.add(linkTo(methodOn(AttemptController.class).getAttemptForQuestion(
							attemptId, nextQuestion.getQuestionId())).withRel("next"));
				}
				attempt.getSurvey().setRespondingQuestion(resultQuestion);	
				attempt.getSurvey().setRespondingSection(foundSection);
			}			
			
			return attempt;
		}
		return null;
	}		
	
	protected Question findUnansweredQuestion(Survey survey, List<Response> responses) {
		if(survey != null && responses != null) {
			Question lastQuestion = null;
			for(Section section : survey.getSections()) {	
				for(Question question : section.getQuestions()) {	
					boolean unanswered = true;
					lastQuestion = question;					
					for(Answer answer : question.getAnswers()) {
						for(Response response : responses) {
							if(response.getAnswerId().equals(answer.getAnswerId())) {
								unanswered = false;
								break;
							}
						}
					}
					if(unanswered) {
						return question;
					}											
				}
	
			}
			return lastQuestion;
		}
		return null;
	}
	
	protected Question findUnansweredQuestion(Section section, List<Response> responses) {
		if(section != null && responses != null) {
			Question lastQuestion = null;
			for(Question question : section.getQuestions()) {	
				boolean unanswered = true;
				lastQuestion = question;					
				for(Answer answer : question.getAnswers()) {
					for(Response response : responses) {
						if(response.getAnswerId().equals(answer.getAnswerId())) {
							unanswered = false;
							break;
						}
					}
				}
				if(unanswered) {
					return question;
				}											
	
			}
			return lastQuestion;
		}
		return null;
	}
	
	@PostMapping("/{attemptId}/questions/{questionId}")
	@JsonView(View.DetailedView.class)
	public Resource<Attempt> setResponsesForQuestionFromAttempt(@PathVariable("attemptId") String attemptId, @PathVariable("questionId") Integer questionId, @RequestBody List<Response> responses){
		Attempt attempt = getAttemptRepository().findOne(attemptId);
		List<Response> newResponse = new ArrayList<>();
		if(attempt != null) {
			for(Section section : attempt.getSurvey().getSections()) {
				for(Question question : section.getQuestions()) {
					if(!question.getQuestionId().equals(questionId)) {
						for(Answer answer : question.getAnswers()) {
							for(Response response : attempt.getResponses()) {
								if(response.getAnswerId().equals(answer.getAnswerId())) {
									newResponse.add(response);
								}
							}
						}
					}
				}
			}
			newResponse.addAll(responses);
			attempt.getResponses().clear();
			attempt.getResponses().addAll(newResponse);
			getAttemptRepository().save(attempt);
			return getRestUtil().getResource(getAttempt(attempt));
		}		
		return null;
	}

	protected AttemptRepository getAttemptRepository() {
		return attemptRepository;
	}

	protected RestUtil getRestUtil() {
		return restUtil;
	}

	protected UserGroupRepository getUserGroupRepository() {
		return userGroupRepository;
	}

	protected EmailService getEmailService() {
		return emailService;
	}

	protected SurveyRepository getSurveyRepository() {
		return surveyRepository;
	}

	protected String getSecret() {
		return secret;
	}
}
