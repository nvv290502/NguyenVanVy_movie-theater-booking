package com.movie_theaters.util;

public class NativeQuery {
    public static final String MOVIE_REVENUE_QUERY =
            "WITH bill_movie_summary AS (" +
                    "SELECT " +
                    "b.bill_id, " +
                    "m.movie_id, " +
                    "m.name_movie AS movie_name, " +
                    "b.amount_money, " +
                    "COUNT(*) AS tickets_per_bill, " +
                    "b.amount_money / COUNT(*) AS amount_per_ticket " +
                    "FROM " +
                    "bill b " +
                    "JOIN bill_detail bd ON b.bill_id = bd.bill_id " +
                    "JOIN schedules sch ON bd.schedule_id = sch.schedule_id " +
                    "JOIN movies m ON sch.movie_id = m.movie_id " +
                    "WHERE (:startDate IS NULL OR b.created_datetime >= :startDate) " +
                    "AND (:endDate IS NULL OR b.created_datetime <= :endDate) " +
                    "GROUP BY " +
                    "b.bill_id, m.movie_id, m.name_movie, b.amount_money " +
                    ") " +
                    "SELECT " +
                    "movie_id, " +
                    "movie_name, " +
                    "SUM(amount_per_ticket * tickets_per_bill) AS total_revenue, " +
                    "SUM(tickets_per_bill) AS total_tickets, " +
                    "COUNT(DISTINCT bill_id) AS number_of_bills " +
                    "FROM " +
                    "bill_movie_summary " +
                    "GROUP BY " +
                    "movie_id, movie_name " +
                    "ORDER BY " +
                    "total_revenue DESC";

    public static final String CINEMA_REVENUE_QUERY =
            "WITH bill_cinema_summary AS (" +
                    "SELECT " +
                    "b.bill_id, " +
                    "c.cinema_id, " +
                    "c.cinema_name AS cinema_name, " +
                    "b.amount_money, " +
                    "COUNT(*) AS tickets_per_bill, " +
                    "b.amount_money / COUNT(*) AS amount_per_ticket " +
                    "FROM " +
                    "bill b " +
                    "JOIN bill_detail bd ON b.bill_id = bd.bill_id " +
                    "JOIN rooms r ON r.room_id = bd.room_id " +
                    "JOIN cinemas c ON c.cinema_id = r.cinema_id " +
                    "WHERE (:startDate IS NULL OR b.created_datetime >= :startDate) " +
                    "AND (:endDate IS NULL OR b.created_datetime <= :endDate) " +
                    "GROUP BY " +
                    "b.bill_id, c.cinema_id, c.cinema_name, b.amount_money " +
                    ")" +
                    "SELECT " +
                    "cinema_id, " +
                    "cinema_name, " +
                    "SUM(amount_per_ticket * tickets_per_bill) AS total_revenue, " +
                    "SUM(tickets_per_bill) AS total_tickets, " +
                    "COUNT(DISTINCT bill_id) AS number_of_bills " +
                    "FROM " +
                    "bill_cinema_summary " +
                    "GROUP BY " +
                    "cinema_id, cinema_name " +
                    "ORDER BY " +
                    "total_revenue DESC";
}
