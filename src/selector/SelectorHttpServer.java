package selector;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Rect;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;

import fi.iki.elonen.NanoHTTPD;

public class SelectorHttpServer extends NanoHTTPD {

  public Object waitLock = new Object();

  public static final String CLASS_NAME = "class_name";
  public static final String RESOURCE_ID = "resource_id";

  public static final String MIME_JSON = "application/json";



  public SelectorHttpServer(int port) {
    super(port);
  }

  public static final Response UNSUPPORTED_ERROR =
      new Response(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "Unsuported protocol.");

  public static final Response UI_OBJECT_NOT_FOUND_ERROR =
      new Response(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "Ui object not found.");

  public static final Response JSON_PROCESSING_ERROR =
      new Response(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "Json processing error.");


  Response GetResponseFromSelectedUiObject(UiObject object) {
    JSONObject responseObject = new JSONObject();
    try {
      Rect bound = object.getBounds();
      responseObject.put("top", bound.top);
      responseObject.put("bottom", bound.bottom);
      responseObject.put("left", bound.left);
      responseObject.put("right", bound.right);
      responseObject.put("isClickable", object.isClickable());
      responseObject.put("isScrollable", object.isScrollable());
    } catch (JSONException e) {
      return JSON_PROCESSING_ERROR;
    } catch (UiObjectNotFoundException e) {
      return UI_OBJECT_NOT_FOUND_ERROR;
    }
    return new Response(Response.Status.ACCEPTED, MIME_JSON, responseObject.toString());
  }

  @Override
  public Response serve(IHTTPSession session) {
    if ("/stop".equals(session.getUri())) {
      stop();
      synchronized (waitLock) {
        waitLock.notify();
      }
      return new Response(Response.Status.ACCEPTED, MIME_PLAINTEXT, "Server stopped.");
    } else if ("/select".equals(session.getUri())) {
      UiObject object = null;
      Map<String, String> headers = session.getHeaders();
      String selectorValue;
      if (headers.containsKey(CLASS_NAME)) {
        selectorValue = headers.get(CLASS_NAME);
        object = new UiObject(new UiSelector().className(selectorValue));
      } else if (headers.containsKey(RESOURCE_ID)) {
        selectorValue = headers.get(RESOURCE_ID);
        object = new UiObject(new UiSelector().resourceId(selectorValue));
      } else {
        return UNSUPPORTED_ERROR;
      }
      return GetResponseFromSelectedUiObject(object);
    } else {
      return UNSUPPORTED_ERROR;
    }
  }
}
