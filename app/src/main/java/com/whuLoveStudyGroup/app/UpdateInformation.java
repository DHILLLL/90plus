package com.whuLoveStudyGroup.app;

/**
 * Created by 635901193 on 2017/10/9.
 */

class UpdateInformation{
    String version;
    String information;

    public UpdateInformation(String version, String information){
        this.information = information;
        this.version = version;
    }

    public String getInformation() {
        return information;
    }

    public String getVersion() {
        return version;
    }
}