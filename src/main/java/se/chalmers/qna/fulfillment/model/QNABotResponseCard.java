package se.chalmers.qna.fulfillment.model;

public class QNABotResponseCard
{
    /*
    "card":
    {
        "send": false,
        "title": "",
        "text": "",
        "url": ""
    }
    */

    public boolean send;
    public String title = "";
    public String text = "";
    public String url = "";
}
