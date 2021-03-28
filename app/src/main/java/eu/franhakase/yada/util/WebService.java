package eu.franhakase.yada.util;

import android.content.Context;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.Locale;
import eu.franhakase.yada.translation.TranslationResponseCallback;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class WebService
{
    private static final String BASE_URL = "https://www2.deepl.com/jsonrpc";


    private static AsyncHttpClient client = new AsyncHttpClient();



    public static void postJ(Context c, String url, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler)
    {
        client.post(c, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl)
    {
        return BASE_URL + relativeUrl;
    }

    public static void GetTranslation(Context ctx, String Input, String InputLanguage, String OutputLanguage, long id)
    {

        final TranslationResponseCallback trc = (TranslationResponseCallback) ctx;
        String _jsonPost = "{"
            + "\"jsonrpc\":\"2.0\","
            + "\"method\": \"LMT_handle_jobs\","
            + "\"params\": {"
            + "\"jobs\":[{"
            + "\"kind\":\"default\","
            + "\"raw_en_sentence\": \""+Input+"\","
            + "\"raw_en_context_before\":[],"
            + "\"raw_en_context_after\":[],"
            + "\"preferred_num_beams\":4,"
            + "\"quality\":\"fast\""
            + "}],"
            + "\"lang\":{"
            + "\"user_preferred_langs\":[\"JA\",\"EN\"],"
            + "\"source_lang_user_selected\":\""+InputLanguage+"\","
            + "\"target_lang\":\""+OutputLanguage+"\""
            + "},"
            + "\"priority\":-1,"
            + "\"commonJobParams\":{},"
            + "\"timestamp\": " + System.currentTimeMillis()
            + "},"
            + "\"id\":" + id
            + "}";
        StringEntity entity =  new StringEntity(_jsonPost, "UTF-8");
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        postJ(ctx, "", entity, "application/json", new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] timeline)
            {
                trc.onTranslationCallBack(new String(timeline));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e)
            {
                trc.onTranslationCallBack(String.format(Locale.getDefault(), "Erro %d", statusCode));
            }
        });
    }

    public static String escape(String raw)
    {
        String escaped = raw;
        escaped = escaped.replace("\\", "\\\\");
        escaped = escaped.replace("\"", "\\\"");
        escaped = escaped.replace("\b", "\\b");
        escaped = escaped.replace("\f", "\\f");
        escaped = escaped.replace("\n", "\\n");
        escaped = escaped.replace("\r", "\\r");
        escaped = escaped.replace("\t", "\\t");
        return escaped;
    }

}

