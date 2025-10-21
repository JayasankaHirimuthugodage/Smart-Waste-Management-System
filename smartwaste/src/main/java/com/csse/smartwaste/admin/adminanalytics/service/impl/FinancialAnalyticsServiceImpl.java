package com.csse.smartwaste.admin.adminanalytics.service.impl;

import com.csse.smartwaste.admin.adminanalytics.dto.FinancialAnalyticsDto;
import com.csse.smartwaste.admin.adminanalytics.service.FinancialAnalyticsService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

/**
 * SRP: Handles only financial analytics computation. OCP: Extendable for more
 * KPIs (e.g., “avg payment delay”).
 */
@Service
public class FinancialAnalyticsServiceImpl implements FinancialAnalyticsService {

    @Override
    public FinancialAnalyticsDto generateFinancialAnalytics() {
        FinancialAnalyticsDto dto = new FinancialAnalyticsDto();
        dto.setTotalRevenue(45280);
        dto.setPendingPayments(8450);
        dto.setCollectionCosts(28900);
        dto.setNetProfit(16380);

        // Revenue by area (mocked — can replace with DB query)
        dto.setRevenueByArea(List.of(
                revenue("Downtown", 12450, 27.5, 324),
                revenue("North Residential", 10890, 24.0, 512),
                revenue("South Residential", 9870, 21.8, 468),
                revenue("Industrial Zone", 8120, 17.9, 89),
                revenue("West Zone", 3950, 8.8, 187)
        ));

        // Recent Transactions (mock or from payment table)
        dto.setRecentTransactions(List.of(
                transaction("TXN-1001", "ABC Corporation", 450, "Collection Fee", "paid", LocalDate.now().minusDays(1)),
                transaction("TXN-1002", "John Smith", 85, "Monthly Bill", "paid", LocalDate.now().minusDays(1)),
                transaction("TXN-1003", "Sarah Johnson", 125, "Special Pickup", "pending", LocalDate.now().minusDays(2)),
                transaction("TXN-1004", "Green Markets", 380, "Collection Fee", "paid", LocalDate.now().minusDays(2)),
                transaction("TXN-1005", "Mike Wilson", 95, "Monthly Bill", "overdue", LocalDate.now().minusDays(3))
        ));

        return dto;
    }

    private FinancialAnalyticsDto.RevenueByArea revenue(String area, double rev, double perc, int acc) {
        FinancialAnalyticsDto.RevenueByArea r = new FinancialAnalyticsDto.RevenueByArea();
        r.setArea(area);
        r.setRevenue(rev);
        r.setPercentage(perc);
        r.setAccounts(acc);
        return r;
    }

    private FinancialAnalyticsDto.TransactionSummary transaction(String id, String cust, double amt, String type, String status, LocalDate date) {
        FinancialAnalyticsDto.TransactionSummary t = new FinancialAnalyticsDto.TransactionSummary();
        t.setId(id);
        t.setCustomer(cust);
        t.setAmount(amt);
        t.setType(type);
        t.setStatus(status);
        t.setDate(date.toString());
        return t;
    }
}
