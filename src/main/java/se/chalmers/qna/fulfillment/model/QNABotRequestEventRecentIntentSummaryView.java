package se.chalmers.qna.fulfillment.model;

public class QNABotRequestEventRecentIntentSummaryView
{
    /*
    "recentIntentSummaryView":
    [
      {
        "intentName": "fulfilment_IntentSEdMAytAjY",
        "checkpointLabel": null,
        "slots":
         {
           "slot": "when do the buss leave"
         },
        "confirmationStatus": "None",
        "dialogActionType": "Close",
        "fulfillmentState": "Fulfilled",
        "slotToElicit": null
      }
    ]
     */

    public String intentName;
    public String checkpointLabel;
    public QNABotRequestEventRecentIntentSummaryViewSlots slots;
    public String confirmationStatus;
    public String dialogActionType;
    public String fulfillmentState;
    public String slotToElicit;
}
