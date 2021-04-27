package se.chalmers.qna.fulfillment.model;

public class QNABotRequestInfoES
{
        /*
         "es":
          {
             "address": "search-qnabot-elastic-6wgov9oh92rb-o63t5g7hqjgg76lkdypvxlqguq.eu-west-1.es.amazonaws.com",
             "index": "qnabot",
             "type": "qna",
             "service":
              {
                "qid": "QnABot-ESQidLambda-DSUA9Y63QHS",
                "proxy": "QnABot-ESProxyLambda-10W9L2GGUPZAI"
              }
           }
     */

    public String address;
    public String index;
    public String type;
    public QNABotRequestInfoESService service;
}
