package se.chalmers.qna.fulfillment.model;

public class QNABotRequestEvent
{
    /*
    "_event":
    {
        "messageVersion": "1.0",
        "invocationSource": "FulfillmentCodeHook",
        "userId": "eu-west-1:c245d029-49ce-42ad-9566-7e52400899e3",
        "sessionAttributes": {},
        "requestAttributes": null,
        "bot":
        {
            "name": "QnABot_BotFJDPq",
                "alias": "live",
                "version": "1"
        },
        "outputDialogMode": "Text",
        "currentIntent":
        {
            "name": "fulfilment_IntentSEdMAytAjY",
            "slots":
            {
                "slot": "what do the buss leave"
            },
            "slotDetails":
            {
                "slot":
                {
                    "resolutions": [],
                    "originalValue": "what do the buss leave"
                }
            },
            "confirmationStatus": "None"
        },
        "inputTranscript": "what do the buss leave?",
        "recentIntentSummaryView": null,
        "sentimentResponse": null,
        "kendraResponse": null,
        "errorFound": false
    }

     */

    public String messageVersion;
    public String invocationSource;
    public String userId;
    public QNABotRequestEventSessionAttribute sessionAttributes;
    public QNABotRequestEventRequestAttributes requestAttributes = null;
    public QNABotRequestEventBot bot;
    public String outputDialogMode;
    public QNABotRequestEventCurrentIntent currentIntent;
    public String inputTranscript;
    public QNABotRequestEventRecentIntentSummaryView[] recentIntentSummaryView = null;
    public QNABotRequestEventSentimentResponse sentimentResponse = null;
    public QNABotRequestEventKendraResponse kendraResponse = null;
    public boolean errorFound;
}
