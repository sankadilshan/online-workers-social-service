package com.onlineWorkers.onlineWorkersSocialService.enums;

public enum  Social {
    ONE(1),Zero(0);
    private int value;
    private Social(int value){
        this.value=value;
    }

    public int getValue() {
        return value;
    }
}
