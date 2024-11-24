package com.fitable.backend.calendar.mapper;

import com.fitable.backend.calendar.entity.Calendar;
import com.fitable.backend.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface CalendarMapper {

    Calendar getCalendarById(@Param("id") Long id);

    List<Calendar> findByDatePerformedAndUser(@Param("date") LocalDate date, @Param("userId") Long userId);

    List<Calendar> findByDatePerformedBetweenAndUser(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("userId") Long userId
    );

    int updateDurationById(@Param("id") Long id, @Param("duration") int duration);

    void insertCalendar(Calendar calendar);
}
