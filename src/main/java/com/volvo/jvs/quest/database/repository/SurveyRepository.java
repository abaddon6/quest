package com.volvo.jvs.quest.database.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.volvo.jvs.quest.database.entity.Survey;

public interface SurveyRepository extends CrudRepository<Survey, Integer> {

	@Query("SELECT new com.volvo.jvs.quest.database.entity.Survey(survey.surveyId, survey.name, survey.description) " 
	     + "FROM Survey survey "
	     + "WHERE survey.publish=:publish "
		 + "ORDER BY survey.name ASC")	
    List<Survey> findAll(@Param("publish") Boolean publish);

	@Query("SELECT DISTINCT survey " 
		     + "FROM Survey survey "
			 + "LEFT JOIN FETCH survey.sections section "
			 + "LEFT JOIN FETCH section.questions question "
			 + "LEFT JOIN FETCH question.answers answer "
			 + "LEFT JOIN FETCH section.maturities maturity "
			 + "WHERE survey.surveyId=:id")		
	Survey findOne(@Param("id") Integer id);
}
