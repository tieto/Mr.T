package com.tieto.systemmanagement.intercept.util;

/**
 * Created by zhaokedong on 4/8/15.
 *
 */
public interface InterceptConfiguration {

    String INTERCEPT_CONFIGURATION = "intercept_configuration" ;
    String INTERCEPT_ACTION = "com.tieto.intercept.ACTION"  ;

    interface InterceptCallConfiguration{
        String ENABLE_CALL_INTERCEPT = "enable_call_intercept" ;
        String ENABLE_CALL_INTERCEPT_ANONYMITY = "enable_call_intercept_anonymity" ;
        String ENABLE_CALL_INTERCEPT_CONTRACT = "enable_call_intercept_contract" ;
        boolean DEFAULT_ENABLE_CALL_INTERCEPT = false ;
        boolean DEFAULT_ENABLE_CALL_INTERCEPT_ANONYMITY = false ;
        boolean DEFAULT_ENABLE_CALL_INTERCEPT_CONTRACT = false;
    }
}
