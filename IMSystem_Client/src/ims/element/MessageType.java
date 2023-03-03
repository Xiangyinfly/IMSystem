package ims.element;

public interface MessageType {
    String LOGIN = "L";
    String LOGIN_SUCCEED = "1";
    String LOGIN_FAIL = "2";

    String REGISTER = "R";
    String REGISTER_SUCCEED = "3";
    String REGISTER_FAIL = "4";

    String COMMON_MESSAGE = "5";

    String REQUEST_ONLINE_LIST = "6";
    String RETURN_ONLINE_LIST = "7";

    String CLIENT_LOGOUT = "8";

    String REQUEST_EXIST_STATUS = "9";
    String RETURN_EXIST_STATUS = "10";

    String FILE_MESSAGE = "11";
}
