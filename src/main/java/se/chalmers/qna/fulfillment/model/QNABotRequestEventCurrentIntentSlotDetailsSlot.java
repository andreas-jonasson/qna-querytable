package se.chalmers.qna.fulfillment.model;

public class QNABotRequestEventCurrentIntentSlotDetailsSlot
{
        /*
                  "slot":
                   {
                      "resolutions":
                       [
                         { "value": "yes" }
                       ],
                      "originalValue": "Yes"
                   }
       */

    public QNABotRequestEventCurrentIntentSlotDetailsSlotResolutions[] resolutions;
    public String originalValue;
}
