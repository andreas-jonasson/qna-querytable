package se.chalmers.qna.fulfillment.model;

public class QNABotResponse
{
    /*
    "res":
    {
        "type": "PlainText",
        "message": "Lambda Hooks allow you to extend QnABot by returning dynamic answers.",
        "session":
        {
            "appContext":
            {
                "altMessages": {}
            },
            "previous":
            {
                "qid": "GreetingHookExample",
                "a": "Lambda Hooks allow you to extend QnABot by returning dynamic answers.",
                "alt": {},
                "q": "what are lambda hooks?"
            },
            "navigation":
            {
                "next": "",
                "previous": [],
                "hasParent": true
            }
        },
        "card":
        {
            "send": false,
            "title": "",
            "text": "",
            "url": ""
        },
        "_userInfo":
        {
            "UserId": "eu-west-1:bab025e6-e7a7-4a80-a36f-025ede656905",
            "InteractionCount": 1,
            "TimeSinceLastInteraction": 1590163026.726,
            "FirstSeen": "Sat May 23 2020 03:57:06 GMT+0000 (Coordinated Universal Time)",
            "LastSeen": "Sat May 23 2020 03:57:06 GMT+0000 (Coordinated Universal Time)"
        },
        "got_hits": 1,
        "result":
        {
            "qid": "GreetingHookExample",
            "a": "Lambda Hooks allow you to extend QnABot by returning dynamic answers.",
            "l": "QNA:ExampleJSLambdahook",
            "type": "qna",
            "questions":
            [
                {
                    "q": "What are lambda hooks"
                },
                {
                    "q": "What is a lambda hook"
                }
            ],
            "autotranslate":
            {
                "a": true
            }
        },
        "plainMessage": "Lambda Hooks allow you to extend QnABot by returning dynamic answers."
    }
    */


    public String type;
    public String message;
    public QNABotResponseSession session;
    public QNABotResponseCard card;
    public int got_hits = 1;
    public QNABotResponseResult result;
    public String plainMessage;

    public QNABotResponse(String message)
    {
        this.message = message;
        this.plainMessage = message;
        type = "PlainText";
        session = new QNABotResponseSession();
        card = new QNABotResponseCard();
    }

    public QNABotResponse()
    {}
}
