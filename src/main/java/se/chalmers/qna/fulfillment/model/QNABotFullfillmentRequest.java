package se.chalmers.qna.fulfillment.model;

public class QNABotFullfillmentRequest
{
    /*
    {
    "req":
     {
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
            "kendraResponse": null
        }
    },
    "res": {}
}
     */

    public QNABotRequest req;
    public QNABotResponse res;

    public void setMarkupMessage(String markupMessage)
    {
        // Well, there is no guarantee this chain of objects exist.
        res.session.appContext.altMessages.markdown = markupMessage;
    }
}
