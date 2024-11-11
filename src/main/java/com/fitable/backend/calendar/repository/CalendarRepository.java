package com.fitable.backend.calendar.repository;


import com.fitable.backend.calendar.entity.Calendar;
import com.fitable.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findByDatePerformedAndUser(LocalDate date, User user);
}
