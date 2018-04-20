package com.example.shop.util.alipay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.example.shop.R;

import java.util.List;
import java.util.Map;

/**
 *  重要说明:
 *  
 *  这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
 *  真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
 *  防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险； 
 */
public class PayDemoActivity extends FragmentActivity {
	
	/** 支付宝支付业务：入参app_id */
	public static final String APPID = "2016100900647653";
	
	/** 支付宝账户登录授权业务：入参pid值 */
	public static final String PID = "2088102174810390";
	/** 支付宝账户登录授权业务：入参target_id值 */
	public static final String TARGET_ID = "TEST";
	public String totalPrice,totalNum;
	public List<String> priceList,numList;

	/** 商户私钥，pkcs8格式 */
	/** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
	/** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
	/** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
	/** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
	/** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
	public static final String RSA2_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDwGOaMpi4R6WmIHsW5suu/eRYxRgvd2VVlhNopfv4DZwJg4+y1y+aAayRcKBMJMfMvP/62gE1oM6OZJnT/AqIaRN68eU1IYp63nuUexBA1gW7HE8fxziUVkCxJJ9X6UNjkmuX6qYu9zqGhepksWbu96CqmF5/ajjTdV1HdKHswRhCLDQ8MRcsZ6qFmdHRW7mDGpWuYwxj63aGvtKBwCgajr0JU2Obi7A79l0dWT0Qi6/7T0NgEzfpKEDI0YroWc1qI/X1yhffbXQrfrobmuw/MKJZu7M+nOndKa+tBZDfOe0bvXBVi5IJ9OvE9NEHOvkITCgtaL8w5KYt/O71x3E4bAgMBAAECggEAGq9p0HAX5r2+kPZ6m20bbqiSot9aKkohh6sAZGRM7yvfw80RXhNPhy5hWCzMENWQZcwDD12rPM2gfUEc98rfIXROHTyOLmFnqpjqdkTsWEYOfeng6ysyS/c+NNNMoQmjUaU1smptsabpp/zsUVWX3D+Vneg6SBxOG9u8qYv6RR8NT5w60KASSahGiEaAcosA2u0ncUjbEO9hh+1o+UfoEoC5zwqakk17tEwfITP1qwI8QWwZQZ0tqsy7VvBLP3pY1o9EJetDyM93fVDd7c1yM8aZfu/JtyanqZkXQmrxgeIuho6b3PLmA8ajOhoBVYOJcUCm3AGRpOubPBNtBmi+YQKBgQD9ufEhknc8HkL90IyRuzoGbwvukYgvmlwkTNg1jNy9v+5tJrpWU8OH00D1CnLOZxV0jvOhkJZrU7SBRYFo1SzRJf5Bb4XD0E74HVL0U9aUDUKsc7JHZOKnIwXGDbJMVxBIOz5KxCI2QAarN5qRi3XItiPoKBmYl9QBjoJaSvjCBQKBgQDyP7Fr+Dq0US3jY/Z69mY13RT6D4OUiwQk+/38J5wED3I8hCQhm6sO57UYDH2bTi/55zlXJj+8yvl9WeBBqIwdG0vlpxSrHRNnpK6u4KgIbOkaSaDaZXm8Lq7Lzxn0A07dOBVFgSPB8/AgUeSjfJwoOCIpf6xJDUHaFVKVRnUpnwKBgQCX0h5uN0TvjTAKzjJ+tShYVzQtkZmK2nOOKQkqqdogFGunlofjRgX3DTqOJa1iwh39e9hecbUADbsxqxRvVTeXAoPsRb4l2EyYfHt5xXzdZD3kEOq5e61/0x4OT33D3kjY39I7TLcn/RHAe+oacveMnShJL015mEf8knh/bOMCvQKBgC1yY4+07xZ7EFfTnh8tcLl0M4ZCONY6SlnB6MFQkQV1/GF8DOc9NBdEwwWy+jGTto50oZ5G77g0gB/TJ65Jt5eSUmfQM6Fyf/x2ucrXHM8P15915rZySSdTvrk/rkfIAxifd+YlNxcg85CsrG87ZnRpK4eLiz/dO9h1HZwhrsNTAoGBALugjtwuUS3XCmA/ZCz4uxS7awZX+eZB0rVkzSNR7W970ObxpGAVR92cKEBuvc0ibW1qH7dlGa0wLiB8zFQOYISgqJEvNR9uuhm+ALmHKk8m31VP1pVDIGvynbDJGuIF0VVOL/FMRxsJoC1CYmuJPvLoT5ZlDh6QmLuRTdV5yWMH";
	public static final String RSA_PRIVATE = "";
	
	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_AUTH_FLAG = 2;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				@SuppressWarnings("unchecked")
				PayResult payResult = new PayResult((Map<String, String>) msg.obj);
				/**
				 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为9000则代表支付成功
				if (TextUtils.equals(resultStatus, "9000")) {
					// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
					Toast.makeText(PayDemoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
				} else {
					// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
					Toast.makeText(PayDemoActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
				}
				break;
			}
			case SDK_AUTH_FLAG: {
				@SuppressWarnings("unchecked")
				AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
				String resultStatus = authResult.getResultStatus();

				// 判断resultStatus 为“9000”且result_code
				// 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
				if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
					// 获取alipay_open_id，调支付时作为参数extern_token 的value
					// 传入，则支付账户为该授权账户
					Toast.makeText(PayDemoActivity.this,
							"授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
							.show();
				} else {
					// 其他状态值则为授权失败
					Toast.makeText(PayDemoActivity.this,
							"授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

				}
				break;
			}
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_main);
		Intent intent = new Intent();
		totalPrice = intent.getStringExtra("totalPrice");
		totalNum = intent.getStringExtra("totalNum");
		payV2();
	}
	
	/**
	 * 支付宝支付业务
	 *
	 * @param v
	 */
	public void payV2() {
		if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
			new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i) {
							//
							finish();
						}
					}).show();
			return;
		}
	
		/**
		 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
		 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
		 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险； 
		 * 
		 * orderInfo的获取必须来自服务端；
		 */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
		Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2,totalPrice,totalNum);
		String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

		String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
		String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
		final String orderInfo = orderParam + "&" + sign;
		
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask alipay = new PayTask(PayDemoActivity.this);
				Map<String, String> result = alipay.payV2(orderInfo, true);
				Log.i("msp", result.toString());
				
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * 支付宝账户授权业务
	 * 
	 * @param v
	 */
	public void authV2(View v) {
		if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
				|| (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
				|| TextUtils.isEmpty(TARGET_ID)) {
			new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i) {
						}
					}).show();
			return;
		}

		/**
		 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
		 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
		 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险； 
		 * 
		 * authInfo的获取必须来自服务端；
		 */
		boolean rsa2 = (RSA2_PRIVATE.length() > 0);
		Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
		String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);
		
		String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
		String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
		final String authInfo = info + "&" + sign;
		Runnable authRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造AuthTask 对象
				AuthTask authTask = new AuthTask(PayDemoActivity.this);
				// 调用授权接口，获取授权结果
				Map<String, String> result = authTask.authV2(authInfo, true);

				Message msg = new Message();
				msg.what = SDK_AUTH_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread authThread = new Thread(authRunnable);
		authThread.start();
	}
	
	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
	 * 
	 * @param v
	 */
//	public void h5Pay(View v) {
//		Intent intent = new Intent(this, H5PayDemoActivity.class);
//		Bundle extras = new Bundle();
//		/**
//		 * url 是要测试的网站，在 Demo App 中会使用 H5PayDemoActivity 内的 WebView 打开。
//		 *
//		 * 可以填写任一支持支付宝支付的网站（如淘宝或一号店），在网站中下订单并唤起支付宝；
//		 * 或者直接填写由支付宝文档提供的“网站 Demo”生成的订单地址
//		 * （如 https://mclient.alipay.com/h5Continue.htm?h5_route_token=303ff0894cd4dccf591b089761dexxxx）
//		 * 进行测试。
//		 *
//		 * H5PayDemoActivity 中的 MyWebViewClient.shouldOverrideUrlLoading() 实现了拦截 URL 唤起支付宝，
//		 * 可以参考它实现自定义的 URL 拦截逻辑。
//		 */
//		String url = "http://m.taobao.com";
//		extras.putString("url", url);
//		intent.putExtras(extras);
//		startActivity(intent);
//	}

}