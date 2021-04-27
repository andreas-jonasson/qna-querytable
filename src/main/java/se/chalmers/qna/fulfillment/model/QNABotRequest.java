package se.chalmers.qna.fulfillment.model;

import com.amazonaws.services.lambda.runtime.events.LexEvent;

public class QNABotRequest
{
    public LexEvent _event;
    public QNABotRequestSettings _settings;
    public String _type;
    public String _userId;
    public String question;
    public QNABotRequestSession session;
    public String _preferredResponseType;
    public String _clientType;
    public String sentiment;
    public QNABotRequestUserInfo _userInfo;
    public QNABotRequestInfo _info;
}
