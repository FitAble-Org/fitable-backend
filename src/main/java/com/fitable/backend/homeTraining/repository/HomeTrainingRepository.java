package com.fitable.backend.homeTraining.repository;


import com.fitable.backend.homeTraining.entity.HomeTraining;
import com.fitable.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HomeTrainingRepository extends JpaRepository<HomeTraining, Long> {
    List<HomeTraining> findByDatePerformedAndUser(LocalDate date, User user);
}
