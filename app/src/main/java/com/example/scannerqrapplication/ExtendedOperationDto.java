package com.example.scannerqrapplication;

import pl.puretech.scanner.api.response.OperationDto;

public class ExtendedOperationDto extends OperationDto {

    private boolean isExpanded;

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
