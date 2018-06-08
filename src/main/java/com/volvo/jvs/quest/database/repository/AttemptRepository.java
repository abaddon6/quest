package com.volvo.jvs.quest.database.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.volvo.jvs.quest.database.entity.Attempt;

public interface AttemptRepository extends CrudRepository<Attempt, String> {

	@Query("SELECT attempt " 
		     + "FROM Attempt attempt "
		     + "LEFT JOIN FETCH attempt.userGroup userGroup "
		     + "LEFT JOIN FETCH attempt.survey survey "
			 + "LEFT JOIN FETCH survey.sections section "
			 + "LEFT JOIN FETCH section.questions question "
			 + "LEFT JOIN FETCH question.answers answer "	
			 + "LEFT JOIN FETCH section.maturities maturity "	
			 + "WHERE attempt.attemptId=:attemptId")		
	Attempt findOne(@Param("attemptId") String attemptId);
	
	@Query("SELECT attempt " 
		     + "FROM Attempt attempt "
		     + "LEFT JOIN FETCH attempt.userGroup userGroup "
		     + "LEFT JOIN FETCH attempt.survey survey "
			 + "LEFT JOIN FETCH survey.sections section "
			 + "LEFT JOIN FETCH section.questions question "
			 + "LEFT JOIN FETCH question.answers answer "	
			 + "LEFT JOIN FETCH section.maturities maturity "
			 + "WHERE attempt.userGroup.userGroupId=:userGroupId AND attempt.survey.surveyId=:surveyId "
			 + "ORDER BY attempt.attemptNumber ASC")
	List<Attempt> findByUserGroupIdSurveyId(@Param("userGroupId") Integer userGroupId, @Param("surveyId") Integer surveyId);
}
