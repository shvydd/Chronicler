package nodomain.shvydkoy.chronicler.api.webfeed;

final public class ParsingNotifyingException extends Exception
{
    final private static String exceptionName = "ParsingNotifyingException: ";
    final private String message;

    public ParsingNotifyingException(String message)
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
