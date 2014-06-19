package dev.hust.leaf.healthcare.http;

import com.loopj.android.http.*;

public class RestClient {
	private static final String BASE_URL = "http://23.97.75.111:6750/clientservice";
	public static final String URL_LOGIN = "/Login";
	public static final String URL_GET_USER_INFO = "/UserDetail";
	public static final String URL_GET_DEVICE_LIST = "/GetDevicelist";
	public static final String URL_GET_DEVICE_STATE = "/StateQuery";
	public static final String URL_GET_NEWLY_DATA = "/GetNewlyData";
	public static final String URL_GET_DATA_LIST = "/GetDataList";
	public static final String URL_GET_DATA_DETAIL = "/GetDataDetail";
	public static final String URL_GET_EXCEPTION_LIST = "/ExceptionList";
	public static final String URL_GET_CONTACT_LIST = "/ContactQuery";

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}
}