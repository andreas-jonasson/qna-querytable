package se.chalmers.qna.fulfillment.model;

public class QNABotResponseResult
{
    /*
      "result":
      {
         "qid": "Studies_application_courses_master_sv_1",
         "quniqueterms": " Villka masters kan man läsa? Vilka masterutbildningar har Chalmers? Lista Chalmers masters / master / masterutbildningar  ",
         "questions":
          [
            {
               "q": "Villka masters kan man läsa?"
            },
            {
               "q": "Vilka masterutbildningar har Chalmers?"
            },
            {
               "q": "Lista Chalmers masters / master / masterutbildningar"
            }
          ],
          "a": "Chalmers har dessa masterutbildningar:\n...",
          "l": "arn:aws:lambda:eu-west-1:11111111111:function:qna-chalmers-courses",
          "args":
           [
             "Master"
           ],
          "type": "qna",
          "autotranslate":
           {
              "a": true
           }
         }
    */

    public String qid;
    public String quniqueterms;
    public QNABotResponseResultQuestions[] questions;
    public String a;
    public String l;
    public String[] args;
    public String type;
    public QNABotResponseResultAutotranslate autotranslate;
}
