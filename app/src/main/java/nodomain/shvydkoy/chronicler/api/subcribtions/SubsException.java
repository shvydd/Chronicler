package nodomain.shvydkoy.chronicler.api.subcribtions;

final class UserNotifyingException extends Exception
{
    final private static String exceptionName = "UserNotifyingException: ";
    final private String message;

    UserNotifyingException(String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return exceptionName + message;
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    public String getExceptionName()
    {
        return exceptionName;
    }

}


