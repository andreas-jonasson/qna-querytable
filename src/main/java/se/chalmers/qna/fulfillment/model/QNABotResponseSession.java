package se.chalmers.qna.fulfillment.model;

public class QNABotResponseSession
{
    /*
    "session":
    {
        "topic": "",
        "appContext":
        {
            "altMessages":
            {
                "markdown": "**Hello** is this the thing I am looking for?  \nAre you at the other line?  \n**Hello?**",
                "ssml": ""
            }
        },
        "previous":
        {
            "qid": "General_question_get_stop_1_en",
            "a": "Sorry can't find any information about busses or trams.",
            "alt":
            {
                "markdown": "**Hello** is this the thing I am looking for?  \nAre you at the other line?  \n**Hello?**",
                "ssml": ""
            },
            "q": "when do the buss leave?"
        },
        "navigation":
        {
            "next": "",
            "previous": [],
            "hasParent": false
        }
    },


    "session":
    {
       "qnabot_qid": "General_tram_stops_1_en",
       "navigation":
       {
          "next": "General_tram_timetable_1_en",
          "previous": [],
          "hasParent": false
       },
       "qnabot_gotanswer": true,
       "previous":
       {
          "qid": "General_tram_stops_1_en",
          "a": "Form \"where\" to \"where\"?",
          "alt": {},
          "q": "tram"
       },
       "qnabotcontext":
        {
           "elicitResponse": {}
        },
        "stops":
        {
          "END_STOP": "landala",
          "START_STOP": "chalmers"
        },
        "undefined": {}
    },
    */

    public String topic;
    public String qnabot_qid;
    public boolean qnabot_gotanswer;
    public QNABotResponseSessionAppContext appContext;
    public QNABotResponseSessionPrevious previous;
    public QNABotResponseSessionNavigation navigation;
    public QNABotResponseSessionContext qnabotcontext;
    public QNABotResponseSessionStops stops;
}
