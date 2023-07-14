package com.accenture.bars.domain;

import java.time.LocalDate;

public class Request {
    private int billingCycle;
    private LocalDate startDate;
    private LocalDate endDate;

    public Request() {
        //default constructor
    }

    public Request(int billingCycle, LocalDate startDate, LocalDate endDate) {
        this.billingCycle = billingCycle;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(int billingCycle) {
        this.billingCycle = billingCycle;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public int hashCode() {
        final int PRIME_FACTOR = 31;

        int result = 1;
        if (endDate == null) {
            result = PRIME_FACTOR * result;
        } else {
            result = PRIME_FACTOR * result + endDate.hashCode();
        }
        if (startDate == null) {
            result = PRIME_FACTOR * result;
        } else {
            result = PRIME_FACTOR * result + startDate.hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj == null){
            return false;
        }
        if (getClass() != obj.getClass()){
            return false;
        }
        Request other = (Request) obj;

        if (billingCycle != other.billingCycle){
            return false;
        }
        if (endDate == null) {
            if (other.endDate != null){
                return false;
            }
        } else if (!endDate.equals(other.endDate)){
            return false;
        }
        if (startDate == null) {
            if (other.startDate != null){
                return false;
            }
        } else if (!startDate.equals(other.startDate)){
            return false;
        }
        return true;
    }
}
