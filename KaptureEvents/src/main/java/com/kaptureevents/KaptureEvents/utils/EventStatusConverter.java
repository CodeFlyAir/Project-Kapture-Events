package com.kaptureevents.KaptureEvents.utils;

import com.kaptureevents.KaptureEvents.model.EventStatusModel;

public class EventStatusConverter {
    public static EventStatusModel.approvalStatus converter(String status){
        if(status.equalsIgnoreCase( "onHold")){
            return EventStatusModel.approvalStatus.onHold;
        }
        else if(status.equalsIgnoreCase("approved")){
            return EventStatusModel.approvalStatus.approved;
        }
        else if(status.equalsIgnoreCase("rejected")){
            return EventStatusModel.approvalStatus.rejected;
        }
        else {
            return EventStatusModel.approvalStatus.unknown;
        }
    }
}
