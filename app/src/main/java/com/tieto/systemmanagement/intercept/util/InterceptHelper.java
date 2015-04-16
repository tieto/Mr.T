package com.tieto.systemmanagement.intercept.util;

/**
 * Created by zhaokedong on 4/8/15.
 *
 */
public interface InterceptHelper {

    String INTERCEPT_CONFIGURATION = "intercept_configuration" ;
    String INTERCEPT_ACTION_CALL = "com.tieto.intercept.action.call"  ;
    String INTERCEPT_ACTION_MESSAGE = "com.tieto.intercept.action.message" ;

    interface InterceptCallConfiguration{

        String ENABLE_CALL_INTERCEPT = "enable_call_intercept" ;
        String ENABLE_CALL_INTERCEPT_STRANGE = "enable_call_intercept_strange" ;
        String ENABLE_CALL_INTERCEPT_ANONYMITY = "enable_call_intercept_anonymity" ;
        String ENABLE_CALL_INTERCEPT_CONTRACT = "enable_call_intercept_contract" ;

        boolean DEFAULT_ENABLE_CALL_INTERCEPT = false ;
        boolean DEFAULT_ENABLE_CALL_INTERCEPT_ANONYMITY = false ;
        boolean DEFAULT_ENABLE_CALL_INTERCEPT_CONTRACT = false;
        boolean DEFAULT_ENABLE_CALL_INTERCEPT_STRANGE = false ;
    }

    interface InterceptMessageConfiguration{
        String ENABLE_MESSAGE_INTERCEPT = "enable_message_intercept" ;
        String ENABLE_MESSAGE_INTERCEPT_STRANGE = "enable_message_intercept_strange" ;

        boolean DEFAULT_ENABLE_MESSAGE_INTERCEPT = false ;
        boolean DEFAULT_ENABLE_MESSAGE_INTERCEPT_STRANGE = false ;
    }
}
