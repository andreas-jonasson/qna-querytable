package se.chalmers.qna.fulfillment.model;

public class QNABotRequestEventCurrentIntent
{
    /*
         "currentIntent":
          {
             "name": "fulfilment_IntentXisRUpvNIZ",
             "slots":
              {
                 "slot": "Yes"
              },
              "slotDetails":
               {
                  "slot":
                   {
                      "resolutions":
                       [
                         { "value": "yes" }
                       ],
                      "originalValue": "Yes"
                   }
               },
              "confirmationStatus": "None",
              "nluIntentConfidenceScore": null
          },
    */

    public String name;
    public QNABotRequestEventCurrentIntentSlots slots;
    public QNABotRequestEventCurrentIntentSlotDetails slotDetails;
    public String confirmationStatus;

}
