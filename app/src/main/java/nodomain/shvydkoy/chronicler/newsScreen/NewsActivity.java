package nodomain.shvydkoy.chronicler.newsScreen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.InputStream;

import nodomain.shvydkoy.chronicler.R;
import nodomain.shvydkoy.chronicler.api.webfeed.Channel;
import nodomain.shvydkoy.chronicler.api.webfeed.Parser;



public class NewsActivity extends AppCompatActivity
{

    private static Channel testChannel;
    private Button buttonLeft;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news);

        buttonLeft = (Button) findViewById(R.id.left_button);
        textView = (TextView) findViewById(R.id.Text_View);

        buttonLeft.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    final InputStream is = null;
                    Parser parser = new Parser();
                    testChannel = parser.parse(is, "http://www.newswise.com/articles/view/702792/?sc=c79");

                    textView.setText(testChannel.toString());

                }
                catch(Exception e)
                {
                    textView.setText(e.toString());
                }
            }
        });






    }
}
