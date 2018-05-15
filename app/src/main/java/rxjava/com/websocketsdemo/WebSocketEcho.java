package rxjava.com.websocketsdemo;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Created by fazal on 5/8/2018.
 */

public class WebSocketEcho extends WebSocketListener {

    private static WebSocketEcho mWebSocketEcho;
    private static WebSocketInteractor mWebSocketInteractor;

    private WebSocketEcho(){}

    public static WebSocketEcho getInstance(WebSocketInteractor webSocketInteractor){
        if(mWebSocketEcho==null){
            mWebSocketInteractor = webSocketInteractor;
            mWebSocketEcho = new WebSocketEcho();
            mWebSocketEcho.run();
        }
        return mWebSocketEcho;
    }

    private void run() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0,  TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url("ws://echo.websocket.org")
                .build();
        client.newWebSocket(request, this);

        // Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
        client.dispatcher().executorService().shutdown();
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        mWebSocketInteractor.onOpen(webSocket);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        mWebSocketInteractor.onGetMessage(text);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(1000, null);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        t.printStackTrace();
    }

    public interface WebSocketInteractor{
        void onOpen(WebSocket webSocket);
        void onGetMessage(String message);
    }
}
