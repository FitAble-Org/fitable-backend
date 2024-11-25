package com.fitable.backend.calendar.repository;


import com.fitable.backend.calendar.entity.Calendar;
import com.fitable.backend.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findByDatePerformedBetweenAndUser(LocalDateTime startDate, LocalDateTime endDate, User user);

    @Modifying
    @Transactional
    @Query("UPDATE Calendar c SET c.duration = :duration WHERE c.calendarId = :id")
    int updateDurationById(@Param("id") Long id, @Param("duration") int duration);

}
