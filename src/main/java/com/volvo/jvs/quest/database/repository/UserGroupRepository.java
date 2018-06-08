package com.volvo.jvs.quest.database.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.volvo.jvs.quest.database.entity.UserGroup;

public interface UserGroupRepository extends CrudRepository<UserGroup, Integer> {

	@Query("SELECT ug FROM UserGroup ug WHERE email=:email")
	UserGroup findByEmail(@Param("email") String email);
}
