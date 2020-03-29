
package com.example.socketblockdemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io. IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import static com.example.socketblockdemo.Const.SOCKET_PORT;
import static com.example.socketblockdemo.Const.SOCKET_SERVER;


public


class MainActivity extends Activity implements OnClickListener
{

	private BroadcastReceiver bcReceiver;


	public static Context s_context;


	TextView		textView;

	EditText		editText;

	Button			btn;

	Button			shutdown_btn;

	Button			shutdown_cancle_btn;

	Button			lockscreen_btn;


	Handler handler = null;

	@Override protected void onCreate(Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		textView			= (TextView) this.findViewById(R.id.res_txt);
		editText			= (EditText) this.findViewById(R.id.socket_txt);
		btn 				= (Button) this.findViewById(R.id.send_btn);
		shutdown_btn 		= (Button) this.findViewById(R.id.shutdown_btn);
		shutdown_cancle_btn 	= (Button) this.findViewById(R.id.shutdown_cancle_btn);
		lockscreen_btn 		= (Button) this.findViewById(R.id.lockscreen_btn);
		btn.setOnClickListener(this);
		shutdown_btn.setOnClickListener(this);
		shutdown_cancle_btn.setOnClickListener(this);
		lockscreen_btn.setOnClickListener(this);

		//editText.setVisibility(View.INVISIBLE);

		textView.setText("运行结果：");


		NetManager.instance().init(this);


		s_context			= this;

		SocketThreadManager.sharedInstance();

		regBroadcast();


		handler 			= new Handler(getMainLooper())
			{
			@Override public void handleMessage(Message msg)
				{
				switch (msg.what)
					{
					case 0:
						showMsg("指令发送失败");
						break;

					case 1:
						showMsg("指令发送成功");
						break;
					}
				}
			};
		}

	@Override public boolean onCreateOptionsMenu(Menu menu)
		{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
		}

	@Override protected void onDestroy()
		{

		if (bcReceiver != null)
			{
			unregisterReceiver(bcReceiver);
			}

		super.onDestroy();
		}


	public void regBroadcast()
		{
		bcReceiver			= new BroadcastReceiver()
			{
			@Override public void onReceive(Context context, Intent intent)
				{
				final String	value = intent.getStringExtra("response");

				runOnUiThread(new Runnable() { @Override public void run() { textView.setText(value); } });

				}
			};

		IntentFilter	intentToReceiveFilter = new IntentFilter();

		intentToReceiveFilter.addAction(Const.BC);
		registerReceiver(bcReceiver, intentToReceiveFilter);
		}


	public void showMsg(String str)
		{

		Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
		}

	@Override public void onClick(View v)
		{
		String			str = editText.getText().toString().toUpperCase();

		if(v.getId() == R.id.send_btn)
		{
			if (!TextUtils.isEmpty(str)) {
				str = "APP_DEV_" + str;
				textView.setText("");
				SocketThreadManager.sharedInstance().sendMsg(str.getBytes(), handler);
			}
			return;
		}

		if (TextUtils.isEmpty(str)) {
			//Const.SOCKET_SERVER = str;
			str = "ZYZ";
		}

		str = "APP_DEV_" + str;

        if(v.getId() == R.id.shutdown_btn)
            str = str + "shutdown -s -t 60";
        if(v.getId() == R.id.shutdown_cancle_btn)
            str = str + "shutdown -a";
        if(v.getId() == R.id.lockscreen_btn)
            str = str + "rundll32.exe user32.dll LockWorkStation";

        textView.setText("");
		SocketThreadManager.sharedInstance().sendMsg(str.getBytes(), handler);

		}
}


