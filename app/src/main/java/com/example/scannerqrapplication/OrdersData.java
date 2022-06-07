package com.example.scannerqrapplication;

public class OrdersData {
    private String operationName;
    private String productName;
    private String productionOrderNumber;
    private String productionPlanDateTime;
    private String remainingCount;
    private String totalCount;
    private String status;

    public OrdersData(String operationName, String productName, String productionOrderNumber, String productionPlanDateTime, String remainingCount, String totalCount, String status) {
        this.operationName = operationName;
        this.productName = productName;
        this.productionOrderNumber = productionOrderNumber;
        this.productionPlanDateTime = productionPlanDateTime;
        this.remainingCount = remainingCount;
        this.totalCount = totalCount;
        this.status = status;
    }

    public String getOperationName() {
        return operationName;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductionOrderNumber() {
        return productionOrderNumber;
    }

    public String getProductionPlanDateTime() {
        return productionPlanDateTime;
    }

    public String getRemainingCount() {
        return remainingCount;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public String getStatus() {
        return status;
    }
}
