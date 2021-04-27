package se.chalmers.qna.fulfillment.model;

public class QNABotResponseSessionNavigation
{
    /*
    "navigation":
    {
        "next": "",
        "previous": [],
        "hasParent": false
    }

    "navigation"
     {
       "next": "",
       "previous": ["QnABot_CustomNoMatches" ],
       "hasParent": false
      },
    */

    public String next;
    public String[] previous;
    public boolean hasParent;
}
