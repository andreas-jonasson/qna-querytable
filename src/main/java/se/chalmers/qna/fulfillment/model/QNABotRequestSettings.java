package se.chalmers.qna.fulfillment.model;

public class QNABotRequestSettings
{
    /*
            "            \"ES_USE_KEYWORD_FILTERS\": \"true\"," +
            "            \"ES_NO_HITS_QUESTION\": \"no_hits\"," +
            "            \"ES_KEYWORD_SYNTAX_TYPES\": \"NOUN,PROPN,VERB,INTJ\"," +
            "            \"ES_SYNTAX_CONFIDENCE_LIMIT\": \".20\"," +
            "            \"ES_MINIMUM_SHOULD_MATCH\": \"2<75%\"," +
            "            \"ES_SCORE_ANSWER_FIELD\": \"true\"," +
            "            \"ERRORMESSAGE\": \"Unfortunately I encountered an error when searching for your answer. Please ask me again later.\"," +
            "            \"EMPTYMESSAGE\": \"You stumped me! Sadly I don't know how to answer your question.\"," +
            "            \"DEFAULT_ALEXA_LAUNCH_MESSAGE\": \"Hello, Please ask a question\"," +
            "            \"DEFAULT_ALEXA_STOP_MESSAGE\": \"Goodbye\"," +
            "            \"SMS_HINT_REMINDER_ENABLE\": \"true\"," +
            "            \"SMS_HINT_REMINDER\": \" (Feedback? Reply THUMBS UP or THUMBS DOWN. Ask HELP ME at any time)\"," +
            "            \"SMS_HINT_REMINDER_INTERVAL_HRS\": \"24\"," +
            "            \"IDENTITY_PROVIDER_JWKS_URLS\": []," +
            "            \"ENABLE_MULTI_LANGUAGE_SUPPORT\": \"false\"," +
            "            \"MINIMUM_CONFIDENCE_SCORE\": 0.6," +
            "            \"ALT_SEARCH_KENDRA_INDEXES\": []," +
            "            \"ELICIT_RESPONSE_MAX_RETRIES\": 3," +
            "            \"ELICIT_RESPONSE_RETRY_MESSAGE\": \"Please try again?\"," +
            "            \"ELICIT_RESPONSE_BOT_FAILURE_MESSAGE\": \"Your response was not understood. Please start again.\"," +
            "            \"ELICIT_RESPONSE_DEFAULT_MSG\": \"Ok. \"," +
            "            \"ENABLE_REDACTING\": \"false\"," +
            "            \"REDACTING_REGEX\": \"\\\\b\\\\d{4}\\\\b(?![-])|\\\\b\\\\d{9}\\\\b|\\\\b\\\\d{3}-\\\\d{2}-\\\\d{4}\\\\b\"," +
            "            \"DEFAULT_USER_POOL_JWKS_URL\": \"https://cognito-idp.eu-west-1.amazonaws.com/eu-west-1_dXtQcUcXb/.well-known/jwks.json\""
     */

    public String      ES_USE_KEYWORD_FILTERS;
    public String      ES_NO_HITS_QUESTION;
    public String      ES_KEYWORD_SYNTAX_TYPES;
    public String      ES_SYNTAX_CONFIDENCE_LIMIT;
    public String      ES_MINIMUM_SHOULD_MATCH;
    public String      ES_SCORE_ANSWER_FIELD;
    public String      ERRORMESSAGE;
    public String      EMPTYMESSAGE;
    public String      DEFAULT_ALEXA_LAUNCH_MESSAGE;
    public String      DEFAULT_ALEXA_STOP_MESSAGE;
    public String      SMS_HINT_REMINDER_ENABLE;
    public String      SMS_HINT_REMINDER;
    public String      SMS_HINT_REMINDER_INTERVAL_HRS;
    public String[]    IDENTITY_PROVIDER_JWKS_URLS;
    public String      ENABLE_MULTI_LANGUAGE_SUPPORT;
    public double      MINIMUM_CONFIDENCE_SCORE;
    public String[]    ALT_SEARCH_KENDRA_INDEXES;
    public int         ELICIT_RESPONSE_MAX_RETRIES;
    public String      ELICIT_RESPONSE_RETRY_MESSAGE;
    public String      ELICIT_RESPONSE_BOT_FAILURE_MESSAGE;
    public String      ELICIT_RESPONSE_DEFAULT_MSG;
    public String      ENABLE_REDACTING;
    public String      REDACTING_REGEX;
    public String      DEFAULT_USER_POOL_JWKS_URL;
}
