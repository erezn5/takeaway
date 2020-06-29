package com.takeaway.automation.tests.components;

public class GoRest {
    boolean success;
    String message, allow;
    int code, totalCount, currentPage, pageCount;
    RateLimit rateLimit;
    Result result;


    public String getAllow(){
        return allow;
    }

    public RateLimit getRateLimit() {
        return rateLimit;
    }

    public Result getResult() {
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    @Override
    public String toString() {
        return "GoRest{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", allow='" + allow + '\'' +
                ", code=" + code +
                ", totalCount=" + totalCount +
                ", currentPage=" + currentPage +
                ", pageCount=" + pageCount +
                ", rateLimit=" + rateLimit +
                ", result=" + result +
                '}';
    }

    public static class RateLimit {
        int limit, remaining, reset;

        public int getLimit() {
            return limit;
        }

        public int getRemaining() {
            return remaining;
        }

        public int getReset() {
            return reset;
        }

        @Override
        public String toString() {
            return "RateLimit{" +
                    "limit=" + limit +
                    ", remaining=" + remaining +
                    ", reset=" + reset +
                    '}';
        }
    }
}
