package tw.edu.ncu.nos.ncuone;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    private static final String baseUrl = "https://ncu.one/";
    private static final String callback = "ncunos://ncu.one/";
    private ClipboardManager clipboard;
    private boolean needResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String url = baseUrl + "?callback=" + callback;

        WebView webView = (WebView) findViewById(R.id.web_view);
        assert webView != null;
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSaveFormData(false);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                if (url.startsWith(callback)) {
                    Intent intent = new Intent();
                    String shortUrl = Uri.parse(url).getQueryParameter("short_url");
                    if (needResult) {
                        intent.putExtra("short_url", shortUrl);
                        setResult(1, intent);
                        finish();
                    } else {
                        intent.setAction(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, shortUrl);
                        intent.setType("text/plain");
                        startActivity(Intent.createChooser(intent, getString(R.string.share)));
                    }
                } else {
                    webView.loadUrl(url);
                }
                return true;
            }
        });

        String longUrl = "";
        Intent intent = getIntent();
        if (intent.getAction().equals(Intent.ACTION_VIEW)) {
            Uri uri = intent.getData();
            if (uri != null)
                longUrl = uri.getQueryParameter("url");
        } else {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                longUrl = bundle.getString("url", "");
                needResult = true;
            }
        }
        if (longUrl.isEmpty()) {
            needResult = false;
            clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            longUrl = item.getText().toString();
        }
        if (longUrl.isEmpty()) {
            Uri uri = clipboard.getPrimaryClip().getItemAt(0).getUri();
            if (uri != null)
                longUrl = uri.toString();
        }
        if (!longUrl.isEmpty()) {
            String snackBarText = longUrl;
            if (snackBarText.length() > 40)
                snackBarText = snackBarText.substring(0, 40) + "...";
            Snackbar.make(webView, snackBarText, Snackbar.LENGTH_LONG).show();
        }

        webView.loadUrl(url + (longUrl.isEmpty() ? "" : "&url=" + Uri.encode(longUrl)));
    }
}
