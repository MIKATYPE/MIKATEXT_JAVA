/* ************************************************************************** */
/* 美佳の英文タイプトレーナー テキスト練習編 JAVA版ソースコード Ver2.02.01    */
/*                               Copy right 今村二朗 2023/5/3                 */
/*                                                                            */
/* このソースコードは 改変、転載、他ソフトの使用など自由にお使いください      */
/*                                                                            */
/* 注意事項                                                                   */
/*                                                                            */
/* グラフィック表示は640x400ドットの仮想画面に行い実座標に変換して表示してい  */
/* ます。                                                                     */
/*                                                                            */
/* JAVAでは横軸がX座標、縦軸がY座標ですがこのソースコードでは横軸がY座標      */
/* 縦軸がX座標です。                                                          */
/*                                                                            */
/* ************************************************************************** */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Container;
import javax.swing.JOptionPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JFrame;
import java.util.Random;
import java.awt.Insets;
import java.util.TimerTask;
import java.util.Timer;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.awt.Toolkit;
import java.awt.Image;
import java.util.concurrent.Semaphore;
public class MIKATEXT extends JFrame {

	Semaphore MIKA_semaphore=new Semaphore(1); /* セマフォー獲得 */
	String[]	MIKA_t_name={ /*練習テキストファイル名*/
		"MIKA001.MIT",
		"MIKA002.MIT",
		"MIKA003.MIT",
		"MIKA004.MIT",
		"MIKA005.MIT",
		"MIKA006.MIT"
	};
	String[]	MIKA_t_seiseki_name={ /*成績ファイル書き込み用 練習テキストフファイル名 */
		"mika001.mit",
		"mika002.mit",
		"mika003.mit",
		"mika004.mit",
		"mika005.mit",
		"mika006.mit"
	};
	String[]	MIKA_c_line= new String[3000]; /*練習テキスト読み込みエリア */
	int		MIKA_text_flag=1; /* 美佳のタイプトレーナー テキスト練習編フラグ */
	int		MIKA_max_c_line; /* 練習テキスト 行数 */
	int 	MIKA_start_text_line=86; /* 練習テキスト表示 開始 x 座標 */
	int		MIKA_double_text_hight=40; /* 練習テキスト表示の 行間隔 x座標 */
	int		MIKA_rensyu_text_hight=18; /* 文字入力エリアの行間隔 x座標 */
	int		max_text_line=8; /* 練習テキスト表示 最大行数 */
	int		MIKA_kugiri_text_line=83; /* 練習テキストと練習記録表示の区切線の x座標 */
	int		MIKA_current_point_x=1; /* 練習テキストの入力行の位置*/
	int		MIKA_scroll_point=1; /* 画面スクロール表示の開始行位置 */
	int		MIKA_text_point_x=0; /* 文字入力カーソル縦行位置*/
	int		MIKA_text_point_y=0; /* 文字入力カーソル横文字位置 */
	int		MIKA_err_count=0; /* エラー文字表示文字数 */
	long	MIKA_text_time_interval=1000; /* 入力速度表示用のタイマーのミリ秒間隔 */
	char 	MIKA_err_c_table[]=new char[256]; /* エラー文字保存エリア */ 
	int		MIKA_disp_cursor_flag=1; /* カーソル表示フラグ */
	String MIKA_file_name_seiseki="mikatext.sei"; /* 成績ファイル名 読み込み用 */
	String MIKA_file_name_seiseki2="mikatext.sei"; /* 成績ファイル名 書き込み用 */
	String MIKA_file_name_kiroku="mikatext.log"; /* 練習時間記録ファイル名 追記用 */
	String MIKA_file_name_hayasa="mikatext.spd"; /* 最高速度記録ファイル名 追記用 */
	int MIKA_file_error_hayasa=0; /* 最高速度記録ファイル書き込みエラー =0 正常 =1 異常 */
	int MIKA_file_error_kiroku=0; /* 練習時間記録ファイル書き込みエラー =0 正常 =1 異常 */
	int MIKA_file_error_seiseki=0; /* 成績ファイル書き込みエラー =0 正常 =1 異常 */
	Procttimer MIKA_Procttimer; /* 入力速度表示用タイマー */
	Date MIKA_s_date; /* 練習開始日時 プログラム起動時に取得 練習時間記録ファイルに書き込み時使用 */
	Date MIKA_type_kiroku_date; /* 最高速度達成日時 (時分秒を含む)*/
	String MIKA_type_date; /* 最高速度達成日 一時保存エリア MIKA_type_kiroku_dateの年月日のみを保存 */
	long MIKA_st_t; /*  練習時間記録ファイル用練習開始時間ミリ秒 */
	long MIKA_lt_t; /*  練習時間記録ファイル用練習終了時間ミリ秒 */
	long 	MIKA_rt_t=0; /* 成績記録ファイル用合計練習時間  秒 */
	String[] MIKA_seiseki={null,null,null,null,null,null,null}; /* 成績データ読み込みデータ列 */
	String[]	MIKA_t_date= /* 最高速度達成日付 */
	{
		"        ",
		"        ",
		"        ",
		"        ",
		"        ",
		"        "
	};
	double[]	MIKA_t_speed= /* 最高速度記録 文字数／秒 */
	{
		0.0,0.0,0.0,0.0,0.0,0.0
	};
	double[]	MIKA_tw_speed= /* 最高速度記録 ワード／秒 */
	{
		0.0,0.0,0.0,0.0,0.0,0.0
	};
	long[]	MIKA_t_time= /* 累積練習時間 秒 */
	{
		0,0,0,0,0,0
	};
	long[]	MIKA_t_kaisu= /* 練習回数 */
	{
		0,0,0,0,0,0
	};
	long[]	MIKA_t_mojisu= /* 練習テキスト 文字数 */
	{
		0,0,0,0,0,0
	};
	Timer MIKA_timer=new Timer(); /* タイマー取得 */
	long[] MIKA_p_count=null; /* 練習回数配列 アドレス */
	Color MIKA_magenta=new Color(128,32,128); /* 濃いめのマゼンタ */
	Color MIKA_green=new Color(0,128,0); /* 濃いめのグリーン */
	Color MIKA_blue=new Color(0,0,128); /* 濃いめの青 */
	Color MIKA_cyan=new Color(0,128,128); /* 濃いめのシアン */
	Color MIKA_orange=new Color(128,32,0); /* 濃いめのオレンジ */
	Color MIKA_red=new Color(128,0,0); /* 濃いめの赤 */
	Color MIKA_bk_color=Color.white; /* 背景の色 */
	Color MIKA_color_text_under_line=new Color(0,0,255); /* アンダーラインの色 */
	Color MIKA_color_cursor=new Color(0,0,0); /* カーソルの色 */
	Color MIKA_color_text_err=new Color(255,0,0); /* エラー文字の背景色 */
	String MIKA_type_kind_mes=null; /* 練習テキスト表題 */
	double[] MIKA_type_speed_record=null; /* 最高速度記録 文字数／分 配列アドレス */
	double[] MIKA_type_word_speed_record=null; /* 最高速度記録 ワード／分 配列アドレス */
	String[]	MIKA_type_date_record=null; /* 最高速度達成日配列アドレス */
	long[]	MIKA_type_time_record=null; /* 累積練習時間配列 アドレス */
	String	MIKA_menu_kind_mes=null; /* 最高速度記録ファイル書き込み用メッセージ */
	long MIKA_type_start_time=0; /* 練習開始時間 ミリ秒 */
	long MIKA_type_end_time=0; /* 練習終了時間 ミリ秒 */
	double MIKA_type_speed_time=0.0; /* 練習経過時間 秒 */
	double MIKA_ttype_speed_time=0.0; /* 今回 練習経過時間 秒 */
	double MIKA_type_speed=0.0; /* 練習テキストの入力速度  文字数／分 */
	double MIKA_type_word_speed=0.0; /* 練習テキストの入力速度 最高速度記録 ワード／分 */
	int MIKA_utikiri_flag=0; /* 練習テキスト打ち切りフラグ =1 全練習テキスト打ち切りによる終了 =0 入力練習中 */
	int MIKA_utikiri_flag2=0; /*  前回速度表示時の打ち切りフラグの値 */
	int MIKA_type_syuryou_flag=0; /* 練習終了時の記録更新フラグ =0 更新せず =1 前回の入力速度が0.0の時の記録更新 =2 前回の記録が0.0より大きい時の記録更新 */
	char MIKA_key_char=0; /* 練習文字 */
	int	MIKA_type_count=0; /* 入力文字数カウンター */
	int	MIKA_type_err_count=0; /* エラー入力文字数カウンター */
	int MIKA_time_start_flag=0; /* 時間計測開始フラグ =0 開始前 =1 測定中 */
	int MIKA_max_x_flag=0;/* 画面表示 縦行数モード =0 25行 =1 20行 */
	int MIKA_max_y_flag=0;/* 画面表示 横文半角カラム数モード =0 80カラム =1 64カラム */
	int MIKA_width_x=16; /* 全角文字 半角文字 縦方向ドット数 */
	int MIKA_width_y=8; /* 半角文字 横方向ドット数 */
	int MIKA_practice_end_flag=0; /* 練習実行中フラグ =0 練習中 =1 終了中 ESCによる終了も含む */
	String	MIKA_mes0="●●  美佳の英文タイプトレーナ テキスト練習編  ●●";
	String	MIKA_mesta="●● 美佳タイプ テキスト練習編 %s ●●";
	String	MIKA_mest2="練習テキスト                    文字数   最高速度    達成日    累積練習時間 回数";
	String	MIKA_mest3="                                       字(ワード)/分";
	String 	MIKA_mesi1="もう一度練習するときはリターンキーまたは、Enterキーを押してください";
	String	MIKA_mesi2="メニューに戻るときはESCキーを押してください";
	String	MIKA_mesi3="おめでとう、記録を更新しました";
	String	MIKA_return_mes="ESCキーを押すとメニューに戻ります";
	String	MIKA_key_type_mes="のキーを打ちましょうね．．";
	String  MIKA_menu_mes_s[]={ /* 初期メニュー メニュー項目 */
		"練習テキスト１",
		"練習テキスト２",
		"練習テキスト３",
		"練習テキスト４",
		"練習テキスト５",
		"練習テキスト６",
		"成績",
		"終了",
	};
	String MIKA_ti_text[]={ /* 練習テキスト表題 格納エリア */
		"テキスト１",
		"テキスト２",
		"テキスト３",
		"テキスト４",
		"テキスト５",
		"テキスト６",
	};
	int MIKA_menu_cord_s[][]={ /* 初期 メニュー項目表示位置 x座標 y座標 */
		{3*16,16*8},
		{5*16,16*8},
		{7*16,16*8},
		{9*16,16*8},
		{11*16,16*8},
		{13*16,16*8},
		{15*16,16*8},
		{17*16,16*8},
	};
	int MIKA_menu_s_sel_flag[]={ /* 初期メニュー メニュー項目選択フラグ */
		0,0,0,0,0,0,0,0};
	int MIKA_menu_s_function[]={ /* 初期メニュー 機能番号 */
		1101,1102,1103,1104,1105,1106,19,9999};
//	int MIKA_exec_func_no=29;
//	int MIKA_exec_func_no=21;
	int MIKA_exec_func_no=0; /* メニューの機能番号 */
	int	MIKA_type_kind_no=0; /* 練習項目番号 */
	int[]	MIKA_menu_function_table; /* メニューの機能番号テーブルアドレス */
	int[]	MIKA_sel_flag; /* 前回選択メニュー項目選択フラグアドレス */
	Dimension MIKA_win_size; /* ウィンドーサイズ */
	Insets	MIKA_insets; /* ウィンドー表示領域 */
	public static void main(String[] args) {
		new MIKATEXT();
	}

	public MIKATEXT() {
		int err;
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    addWindowListener(new WindowAdapter() { /* ウィンドーがクローズされた時の処理 を追加 */
            public synchronized void windowClosing(WindowEvent ev) {
//				System.out.printf("window closed\n");
				savekiroku(); /* 練習記録(練習テキスト文字数 累積練習時間 最高入力速度 達成日 練習回数)を保存する */
				procexit(); /* 成績ファイル書き込み 練習時間記録ファイル書き込み */
				System.exit(0); /* プログラム終了 */
            }
});	
	// リスナー
		MyKeyAdapter myKeyAdapter = new MyKeyAdapter(); 
		addKeyListener(myKeyAdapter);/* キー入力処理追加 */
 		MIKA_s_date=new Date(); /* 練習開始日時取得 */
		MIKA_st_t=System.currentTimeMillis(); /*  練習時間記録ファイル用練習開始時間をミリ秒で取得 */
// 		inktable(); /* キーボードの位置テーブル初期化 */
     	read_file_title(); /* 練習テキストファイル 表題読み込み */
		File file = new File(MIKA_file_name_seiseki); /* 成績ファイルオープン */
		try {
	         BufferedReader b_reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"Shift-JIS"));
			err=rseiseki(b_reader,MIKA_seiseki); /* 練習成績ファイル読み込み */
			if(err==0) convseiseki(MIKA_seiseki); /* 練習成績ファイルデータ変換 */
			try{
				b_reader.close(); /* 練習成績ファイルクローズ */
			}
			catch ( IOException e) { 
			    	e.printStackTrace();
			}
		}
		catch (UnsupportedEncodingException | FileNotFoundException e) {
		}
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); /* ウィンドー最大サイズ取得 */
		int w = screenSize.width*4/5; /* ウィンドーサイズ 幅を最大幅の4/5に設定 */
	    int h = screenSize.height*4/5; /* ウィンドーサイズ 高さを最大高さの4/5に設定 */
		setSize(w,h); /* ウィンドーサイズを設定 */
//		Toolkit tk = Toolkit.getDefaultToolkit();
//		Image img = tk.getImage( "mikamtext.gif" ); /* アイコン画像取得 */
//		setIconImage( img ); /* アイコンの設定 */
		setTitle("美佳TEXT"); /* タイトル設定 */
		setLocationRelativeTo(null); /* 中央に表示 */
		setVisible(true); /* ウィンドー表示 */
	}
    void read_file_title() /* 練習テキストファイル 表題読み込み */
	{
		int i=0;
		String a;
		for(i=0;i<6;i++)
		{
			readtextfiles(MIKA_t_name[i],MIKA_ti_text,i,1); /* 各練習テキストの最初の一行を読み込み */
		}
		for(i=0;i<6;i++)
		{
			a=String.format("%s  %s",MIKA_menu_mes_s[i],MIKA_ti_text[i]); /* 練習テキストファイルの表題をメニュー表示項目に追加 */
			MIKA_menu_mes_s[i]=a;
		}
	}
	int	rseiseki(BufferedReader b_reader,String[] seiseki) /* 練習成績ファイル読み込み */
	{
			int i,err;
			err=0;
			String seiseki_line;
			for(i=0;i<7;i++) /* 練習成績ファイルを7行読み込み */
			{
				try {
					seiseki_line=b_reader.readLine(); /* 練習成績ファイルを一行読み込み */
//					System.out.printf("seiseki file line=%s\n",seiseki_line);
					if(seiseki_line==null) err=1; /* ファイルエンドの場合はエラーコード 1 設定 */
					else seiseki[i]=seiseki_line; /* 読み込んだ成績を成績テーブルに保存 */
			 	} catch ( IOException e) {
				err=1;
				}
      			if(err!=0) return(err); 
    		}
			return(err);
	}
	void convseiseki(String[] seiseki) /* 成績テーブルから最高速度 達成日 累積練習時間を取得 */
	{
		MIKA_rt_t=seisekitimeoffset(seiseki[0],0); /* 合計練習時間を取得 */
		convseiseki3(seiseki,1,6,MIKA_t_mojisu,MIKA_t_speed,MIKA_tw_speed,MIKA_t_date,MIKA_t_time,MIKA_t_kaisu); /* 文字数 最高速度 文字数／分 ワード／分 達成日 累積練習時間 練習回数を取得 */
	}
	long seisekitimeoffset(String a,int offset) /* 成績ファイルの一行から練習時間を取得 */
	{
			int i;
			long ii;
			ii=0;
			String b,bb;
			i=a.length(); /* 成績ファイルの一行の長さを取得 */
			if(i>=(13+offset)) /* 成績ファイル一行の長さが13以上の場合 */
			{
				b=a.substring(i-12-offset,i-offset); /* 練習時間文字列を取得 */
				bb=String.format(" %s",b);
//				System.out.printf("時間=(%s)\n",bb);
				ii=ttconv(bb); /* 練習時間文字列を秒に変換 */
			}
			else ii=0;
			return ii;
	}
	void convseiseki3(String[] seiseki,int i,int ii,long[] r_mosjsu,double[] r_speed,double[] w_speed,String[] r_date,long[] r_time,long[] t_kaisu)
	// 練習の成績を取得 */
	{
		int k;
		int j;
		String a,b,c,d,e;
		double speed;
		for(k=0;k<ii;k++)
		{
			j=seiseki[k+i].length(); /* 成績ファイルの一行の長さを取得 */
			if(j>=30)
			{
				a=seiseki[k+i].substring(j-46,j-40); /* 練習テキスト文字数文字列を取得 */
				r_mosjsu[k]=Integer.parseInt(a.trim()); /* 練習テキスト文字数文字列を整数に変換 */
//				System.out.printf("文字数=(%s)\n",a);
				c=seiseki[k+i].substring(j-24,j-16); /* 達成日文字列を取得 */
//			System.out.printf("日付=%s\n",c);
				r_date[k]=c; /* 達成日を保存 */
				b=seiseki[k+i].substring(j-39,j-32); /* 成績ファイルから最高速度 文字数／分 文字列を取得 */
//				System.out.printf("最高速度=(%s)\n",b);
				speed=Double.parseDouble(b.trim()); /* 最高速度 文字数／分 文字列を倍精度実数に変換 */
				r_speed[k]=speed; /* 最高入力速度を保存 */
				d=seiseki[k+i].substring(j-31,j-26); /* 成績ファイルから最高速度 ワード／分 文字列を取得 */
//				System.out.printf("最高速度ワード=(%s)\n",d);
				w_speed[k]=Double.parseDouble(d.trim()); /* 最高速度 ワード／分 文字列を倍精度実数に変換 */
				r_time[k]=seisekitimeoffset(seiseki[k+i],4); /* 累積練習時間を秒に変換して保存 */
 				e=seiseki[k+i].substring(j-4,j); /* 練習回数文字列を取得 */
//				System.out.printf("練習回数=(%s)\n",e);
				t_kaisu[k]=Integer.parseInt(e.trim()); /* 練習回数文字列を整数に変換 */
			}
		}
	}
	String tconv(long time) /* 練習時間秒を文字列に変換 */
	{
		String a;
		a=t0conv(time,0); /* 練習時間秒を "%5d時間%2d分%2d秒"のフォーマットで文字列に変換 */
		return a;
	}
	String t0conv(long time,int flag) /* 練習時間秒をフォーマットを指定して文字列に変換 */
	{
		String a;
		long t1,t2,t3;
		t3=time%60; /* 秒を計算 */
		time=time/60;
		t2=time%60; /* 分を計算 */
		t1=time/60; /* 時間を計算 */
		if(flag==0)	a=String.format("%5d時間%2d分%2d秒",t1,t2,t3); /* 時分秒を文字列に変換 */
		else if(flag==1) a=String.format("%3d時間%2d分%2d秒",t1,t2,t3);
		else a=String.format("%4d時間%2d分%2d秒",t1,t2,t3);
		return a;
	}
long ttconv(String mes) /* 時分秒の文字列を秒に変換 */
	{
		String t1,t2,t3;
		long	i,i1,i2,i3;

//		System.out.printf("練習時間 =%s\n",mes);
		t1=mes.substring(0,5); /* 時間文字列を取得 */
		t2=mes.substring(7,9); /* 分文字列を取得 */
		t3=mes.substring(10,12); /* 秒文字列を取得 */
		i1=Integer.parseInt(t1.trim()); /* 時間文字列を整数に変換 */
		i2=Integer.parseInt(t2.trim()); /* 分文字列を整数に変換 */
		i3=Integer.parseInt(t3.trim()); /* 秒文字列を整数に変換 */
		i=i1*60*60+i2*60+i3; /* 時分秒から秒を算出 */
//		System.out.printf("時間=%s %d 分=%s %d 秒=%s %s\n",t1,i1,t2,i2,t3,i3);
//		System.out.printf("時間=%d 分=%d 秒=%s\n",i1,i2,i3);
		return(i);
	}
	int stringlength(String a) /* 文字列長を半角文字=1 全角文字 =2 で計算する */
	{
		int i,ii,length;
		length=a.length(); /* 文字列長取得 */
		ii=0;
		for(i=0;i<length;i++)
		{
			ii=ii+charlength(a.charAt(i)); /* i番目の文字長を加算 */
		}
		return ii;
	}	
	int charlength(char a) /* 文字が半角文字か全角文字かの判定を行う リターン値 半角=1 全角 =2 */
	{
		int i;
//		System.out.printf("a=%1c code=%d\n",a,(int)a);
		if(a<255) i=1; /* 半角英数の場合 */
		else if(0xff61<=a&&a<=0xff9f) i=1; /* 半角カナ文字の場合 */
		else i=2; /* 半角英数 半角カナ文字以外の場合 */
		return i;
	}
	void cslclr(Graphics g) /* 画面クリア */
	{
		int x,y;
		x=MIKA_win_size.height; /* 画面最大高さ取得 */
		y=MIKA_win_size.width; /* 画面最大幅取得 */
		cslcolor(g,MIKA_bk_color); /* 表示色に背景色を設定 */
		g.fillRect(0,0,y,x); /* 背景色で画面クリア */
	}
	void cslcolor(Graphics g,Color color) /* 表示色を設定 */
	{
		g.setColor(color);
	}
	void cslputscale(Graphics g,int x1,int y1,String a,double scale) /* 仮想座標から実座標に変換して文字列を指定の倍率で表示 */
	{
		Color color1;
		int xx1,yy1;
		int	font_size,font_width,font_hight;
		int ffont_size;
		int i,ii,iii;
		char aa;
	 	Font fg;
		font_size=cslfontsize(scale); /* 文字フォントサイズ取得 */
		ffont_size=(int)(font_size/1.33); /* フォントサイズ補正 */
		font_width=cslfontwidth(1.0); /* 文字表示エリア幅取得 */
		font_hight=cslfonthight(1.0); /* 文字表示エリア高さ取得 */
		fg = new Font("Monospaced" , Font.PLAIN , font_size); /* 文字フォント指定 */
		g.setFont(fg); /* 文字フォント設定 */
		ii=a.length(); /* 表示文字列長取得 */
		iii=0;
		xx1=xcord(x1+MIKA_width_x); /* 表示位置x座標を仮想座標から実座標に変換 */
		for(i=0;i<ii;i++)
		{
			yy1=ycord(y1+MIKA_width_y*iii); /* 表示位置 y座標を仮想座標から実座標に変換 */
			aa=a.charAt(i); /* 文字列からi番目の文字を取り出し */
			g.drawString(String.valueOf(aa),yy1+(font_width-font_size)/2,xx1+(ffont_size-font_hight)/2); /* 表示位置の中央に文字を表示 */
//			if(aa=='ａ') System.out.printf("font_size=%d,font_width%d font hight%d\n",font_size,font_width,font_hight);
//			System.out.printf("x=%d y=%d %s %x \n",yy1,xx1,String.valueOf(aa),(int)aa);
		
			iii=iii+charlength(aa); /* 表示文字位置更新 半角文字は y座標を 1 加算 全角文字は 2加算 */
		}
	}
	void cslput(Graphics g,int x,int y,String mes) /* 文字列を仮想座標で表示 */
	{
		cslputscale(g,x,y,mes,1.0); /* 文字列を等倍の倍率で仮想座標で表示 */
	}
	void cslputu(Graphics g,int x,int y,String mes,int yy,Color color1) /* 文字列の下に下線を表示 */
// x 文字列表示左上仮想x座標
// y 文字列表示左上仮想y座標 
// mes アンダーラインを引く文字列
// yy 文字列下端から下線までのドット数間隔の補正値
// color1 表示色 
	{
		int char_length;
		int x1,x2,xx,y1,y2;
		int font_size,ffont_size;
		int font_hight;
		char_length=stringlength(mes); /* 文字列長取得 半角文字は長さ=1 全角文字は長さ=2で計算*/
		font_size=cslfontsize(1.0); /* 等倍のフォントサイズ取得 */
		ffont_size=(int)(font_size/1.33); /* フォントサイズ補正 */
		font_hight=cslfonthight(1.0); /* 表示エリア高さを取得 */
		x1=xcord(x+MIKA_width_x)+yy+(ffont_size-font_hight)/2+2; /* アンダーラインのx座標設定 */
		x2=xcord(1)-xcord(0); /* アンダーラインの太さを設定 */
		y1=ycord(y); /* アンダーラインの開始y座標設定 */
		y2=ycord(y+char_length*8); /* アンダーラインの終了y座標設定 */
		cslcolor(g,color1); /* アンダーラインの色を設定 */
		for(xx=x1;xx<=x1+x2;xx++) /* 指定の太さのアンダーラインを描画 */
		{
			g.drawLine(y1,xx,y2,xx); /* 直線描画 */
		}
	}
void cslmencenter(Graphics g,int x,String mes) /* 中央にメッセージ文字列を表示 */
// x 文字列表示仮想座標
	{
		int y;
		int k;
		int kk;
		if(MIKA_max_y_flag==0) kk=80; /* 横幅半角80文字モード */
		else kk=64; /* 横幅半角64文字モード */
		k=stringlength(mes); /* 文字列長取得  半角文字は長さ=1 全角文字は長さ=2で計算*/
//		System.out.printf("mes=%s lentgh=%s",mes,k);
		y=((kk-k)*MIKA_width_y)/2; /* 表示開始位置計算 */
		cslput(g,x,y,mes); /* 文字列表示 */
	}
	void cslrectt (Graphics g,int x1,int y1,int x2,int y2,Color color) /* 四角を表示 */
	{
		cslrecttt(g,x1,y1,x2,y2,color,0); /* 境界なしで四角を表示 */
	}
	void cslrecttt (Graphics g,int x1,int y1,int x2,int y2,Color color,int b) /* 四角の内側を境界幅bで塗りつぶす */
	{
		int	xx1,xx2,yy1,yy2;
		int bx,by,bb;
		if(b!=0) /* 境界幅が0で無い場合 */
		{
			bx=xcord(b)-xcord(0); /* 縦方向の境界幅を仮想座標から実座標に変換 */
			by=ycord(b)-ycord(0); /* 横方向の境界幅を仮想座標から実座標に変換 */
			bb=Math.min(bx,by); /* 縦方向の境界幅と横方向の境界幅の小さい方の値を設定 */
			if(bb<=0) bb=1; /* 境界幅がゼロ以下の場合は境界幅を位置に設定 */
		}
		else bb=0;
		xx1=xcord(x1)+bb;	/* 左上 x 座標を 仮想座標から実座標に変換 */ 
		xx2=xcord(x2)-bb;	/* 右下 x 座標を 仮想座標から実座標に変換 */
		yy1=ycord(y1)+bb;	/* 左上 y 座標を 仮想座標から実座標に変換 */
		yy2=ycord(y2)-bb;	 /* 右下 y 座標を 仮想座標から実座標に変換 */
		cslcolor(g,color);  /* 表示色を設定 */
		if(xx1<=xx2&&yy1<=yy2)	g.fillRect(yy1,xx1,yy2-yy1,xx2-xx1);	/*四角を描画 */
	}
	int cslfonthight(double scale) /* 指定倍率で全角文字の表示エリア高さを取得 */
	{
			int font_hight;
			int font_size;
			font_size=(int)(MIKA_width_x*scale); /* 表示エリア高さを仮想座標で計算 */
			font_hight=xcord(font_size)-xcord(0);  /* 表示エリア高さを実座標に変換 */
			return font_hight;
	}
	int cslfontwidth(double scale) /* 指定倍率で全角文字の表示エリア幅を取得 */
	{
			int font_width;
			int font_size;
			font_size=(int)(MIKA_width_y*2*scale); /* 表示エリア幅を仮想座標で計算 */
			font_width=ycord(font_size)-ycord(0); /* 表示エリア幅を実座標に変換 */
			return font_width;
	}
	int cslfontsize(double scale) /* 指定倍率でフォントサイズを取得 */
	{
		int font_hight;
		int font_width;
		int font_size;
		font_hight=cslfonthight(scale); /* 指定倍率で表示エリア高さを取得 */
		font_width=cslfontwidth(scale); /* 指定倍率で表示エリア幅を取得 */
		font_size=Math.min(font_hight,font_width); /* 表示エリア高さと表示エリア幅の小さい方の値をフォントサイズに指定 */
		return font_size;
	}
	int	xcord(int x1){ /* 仮想x座標を 実x座標に変換 */
		int max_x_cord1;
		int x,xx;
		if(MIKA_max_x_flag==0) /* 縦25行モードの設定 */
		{
			max_x_cord1=25*16;
		}
		else /* 縦20行モードの設定 */
		{
			max_x_cord1=20*16;
		}
		x=MIKA_win_size.height-MIKA_insets.top-MIKA_insets.bottom; /* 有効 x表示高さを計算 */
		xx=(x)*(x1)/max_x_cord1; /* 仮想座標を実座標に変換 */
		xx=xx+MIKA_insets.top; /* 表示位置をウィンドー枠内に補正 */ 
		return(xx);
	}
	int ycord(int y1) /* 仮想y座標を 実y座標に変換 */
	{
		int max_y_cord1;
		int y,yy;
		if(MIKA_max_y_flag==0) /* 一行横 80カラムモードの設定 */
		{
			max_y_cord1=80*8;
		}
		else /* 一行横 64カラムモードの設定 */
		{
			max_y_cord1=64*8;
		}
		y=MIKA_win_size.width-MIKA_insets.left-MIKA_insets.right; /* 有効 y表示幅を計算 */
		yy=(y*y1)/(max_y_cord1); /* 仮想座標を実座標に変換 */
		yy=yy+MIKA_insets.left; /* 表示位置をウィンドー枠内に補正 */
		return(yy);
	}
	void disperrorcount(Graphics g,int flag,int flag2,int i,int j) /* エラー入力回数表示 */
// flag=0 表示 flag=1 数値のみ消去 flag=2 メッセージと共に数値を消去
// i 表示位置縦行番号
// j 表示位置横列番号
	{
		String type_mes;
		int offset;
		if(flag==0)
		{
 			cslcolor(g,MIKA_red); /* フラグが=0の時は表示色を赤色に設定 */
			if(flag2==1)
			{
				type_mes=String.format("ミスタッチ%5d回",MIKA_type_err_count); /* エラーカウントメッセージ作成 */
			}
			else
			{
				type_mes=String.format("ミスタッチ%3d回",MIKA_type_err_count); /* エラーカウントメッセージ作成 */
			}
			offset=0;
		}
		else if(flag==1)
		{
			cslcolor(g,MIKA_bk_color); /* フラグが=1の時は数値のみ表示を消去 */
			if(flag2==1)
			{
				type_mes=String.format("%5d",MIKA_type_err_count); /* エラーカウントメッセージ作成 */
			}
			else
			{
				type_mes=String.format("%3d",MIKA_type_err_count); /* エラーカウントメッセージ作成 */
			}
			offset=10;
		}
		else
		{
			cslcolor(g,MIKA_bk_color); /* フラグが=2の時はメッセージを含めて表示を消去 */
			if(flag2==1)
			{
				type_mes=String.format("ミスタッチ%5d回",MIKA_type_err_count); /* エラーカウントメッセージ作成 */
			}
			else
			{
				type_mes=String.format("ミスタッチ%3d回",MIKA_type_err_count); /* エラーカウントメッセージ作成 */
			}
			offset=0;
		}
//		System.out.printf("i=%d j=%d",i,j);
		cslput(g,i*16,(j+offset)*8,type_mes); /* 指定位置にエラーカウント表示 */
	}
	void disperror3(Graphics g,int flag) /* 英文テキスト練習エラー回数表示 */
// flag=0 表示 flag=1 数値のみ消去  flag=2 メッセージと共に数値を消去
	{
		disperrorcount(g,flag,1,3,57); /* 表示位置を指定してエラー回数表示 */
	}
	void disptitle(Graphics g,String mes,String submes) /* 練習項目を画面上部に表示 */
// mes 練習種別メッセージ
// submes 練習項目メッセージ
	{
		String mes0;
		mes0=String.format(mes,submes); /* 表示メッセージを作成 */
		cslcolor(g,MIKA_magenta); /* 表示色をマゼンタに設定 */
		cslmencenter(g,1,mes0); /* 画面上部中央にメッセージを表示 */
//		System.out.printf(mes0);
	}
	void displtitle1(Graphics g) /* テキスト練習画面 上部表示 */
	{
		String a;			
		if(MIKA_p_count[MIKA_type_kind_no]!=0)  /* 練習回数がゼロでない場合 */
		{
			dispkaisu3(g,0); /* 練習回数を表示 */
		}
		try {
			MIKA_semaphore.acquire(); /* セマフォー要求 */
			if(MIKA_type_speed!=0.0) /* 入力速度がゼロでない場合 */
			{
				cslcolor(g,MIKA_blue);
				a=String.format("入力速度%6.1f文字(%5.1fワード)/分",MIKA_type_speed,MIKA_type_word_speed);
				cslput(g,3*16,21*8,a); /* 入力速度を表示 */
			}
			if (MIKA_type_speed_time!=0.0) /* 経過秒がゼロでない場合 */
			{
				disptime2(g,0); /* 経過秒表示 */
			}
			MIKA_semaphore.release(); /* セマフォー解放 */
		}
		catch (Exception ex)
		{	
			ex.printStackTrace();
		}
		if(MIKA_type_err_count!=0) /* エラー回数がゼロで無い場合 */
		{
			disperror3(g,0); /* エラー回数表示 */
		}
		if(MIKA_type_speed_record[MIKA_type_kind_no]!=0.0) /* 最高入力速度がゼロでない場合 */
		{
			cslcolor(g,MIKA_green);
			a=String.format("最高入力速度%6.1f文字(%5.1fワード)/分",MIKA_type_speed_record[MIKA_type_kind_no],MIKA_type_word_speed_record[MIKA_type_kind_no]);
//			a="最高入力速度";
			cslput(g,2*16,17*8,a); /* 最高速度 ワード表示 */
			a=String.format("達成日   %s",MIKA_type_date_record[MIKA_type_kind_no]);
			cslput(g,2*16,57*8,a); /* 達成日表示 */
		}
	}
	void displtitle2(Graphics g) /* テキスト練習画面 メッセージと区切り線を表示 */
	{
		int	x1,x2,y1,y2;
		int i;
		cslcolor(g,MIKA_cyan); /* 表示色をシアンに設定 */
 		cslput(g,4*16,1,"修正はBSを押して下さい、行末ではスペースかEnterを押して下さい、ESCで中断します"); 
		cslcolor(g,Color.black); /* 表示色を黒色に設定 */
		x1=xcord(MIKA_kugiri_text_line); /* 区切り線 x 座標 開始位置取得 */
		x2=xcord(MIKA_kugiri_text_line+1); /* 区切り線 x 座標 終了位置取得 */
		y1=ycord(0); /* 区切り線 y 座標 開始位置取得 */
		y2=ycord(639); /* 区切り線 y 座標 終了位置取得 */
		for(i=x1;i<x2;i++)
		{
			g.drawLine(y1,i,y2,i); /* 区切り線描画 */
		}
	}
	void dispkaisu3(Graphics g,int flag) /* 英文テキス練習 練習回数表示 */
// flag=0 表示 flag=1 消去 
	{
		String type_mes;
		long count;
		if(MIKA_p_count==null) return; /* 練習回数配列アドレスが空の時はリターン */
		count=MIKA_p_count[MIKA_type_kind_no]; /* 練習項目に対応する練習回数取り出し */
//		System.out.printf("count=%d  MIKA_type_kind_no=%d\n",count,MIKA_type_kind_no);
		if(count==0) return; /* 練習回数がゼロの時はリターン */
		if(flag==0) cslcolor(g,MIKA_green); /* フラグが=0の時は表示色を緑色に設定 */
		else cslcolor(g,MIKA_bk_color); /* フラグが=1の時は表示を消去 */
		type_mes=String.format("練習回数%7d回",count); /* 練習回数メッセージ作成 */
		cslput(g,16,57*8,type_mes); /* 練習回数メッセージ表示 */
	}
	String timemessage(int flag,double t1,double t2) /* 経過時間の表示メッセージを作成 */
	{
		String a;
		if(flag==0) /* 打ち切りでない場合 */
		{
			if(t1<=0.0) /* 一分以下の時は 秒のみ表示 */
			{
				a=String.format("経過時間%3.0f秒",t2); /* 秒のみの経過時間表示メッセージ作成 */
			}
			else
			{
				a=String.format("経過時間%3.0f分%2.0f秒",t1,t2); /* 分と秒の経過時間表示メッセージ作成 */
			}
		}
		else /* 打ち切りの時は小数点以下二桁まで表示 */
		{
			if(t1<=0.0) /* 一分以下の時は 秒のみ表示 */
			{
				a=String.format("経過時間%6.2f秒",t2); /*  小数点二桁まで秒のみの経過時間表示メッセージ作成 */
			}
			else
			{
				a=String.format("経過時間%3.0f分%5.2f秒",t1,t2); /* 小数点二桁まで分と秒の経過時間表示メッセージ作成 */
			}
		}
		return a;
	}
	void disptime2(Graphics g,int flag) /* 経過時間分秒を表示 */
	// flag=0 表示 flag=1 消去 
	{
		String	type_mes;
		double t1;
		double t2;
		t1=roundtime(MIKA_type_speed_time/60.0); /* 経過分を算出 */
		t2=MIKA_type_speed_time-t1*60; /* 経過秒を算出 */
		int	offset;
		if(flag==0) /* 経過時間 表示の場合 */
		{
 			cslcolor(g,MIKA_blue);  /* フラグが=0の時は表示色を青に設定 */
			type_mes=timemessage(MIKA_utikiri_flag,t1,t2); /* 練習時間分秒のメッセージを作成 */
			cslput(g,3*16,1,type_mes); /* 経過時間秒のメッセージを表示 */
		}
		else if(flag==1&&MIKA_utikiri_flag==0)/* 表示消去の場合 */
		{
			cslcolor(g,MIKA_bk_color); /* フラグが=1の時は表示を消去 */
			if(t1<=0.0)  /* 表示消去が秒のみの場合 */
			{
				type_mes=String.format("%3.0f",t2); /* 経過時間秒の表示メッセージ作成 */
				offset=8;
				cslput(g,3*16,1+offset*8,type_mes); /* 経過時間秒のメッセージを消去 */
			}
			else /* 表示消去が分と秒の場合 */
			{
				type_mes=String.format("%3.0f",t1); /* 経過時間分の表示メッセージ作成 */
				offset=8;
				cslput(g,3*16,1+offset*8,type_mes); /* 経過時間分の表示を消去 */
				type_mes=String.format("%2.0f",t2); /* 経過時間秒の表示メッセージ作成 */
				offset=8+5;
				cslput(g,3*16,1+offset*8,type_mes); /* 経過時間秒の表示を消去 */
			}
		}
		else /* 表示メッセージを含んで経過時間を消去する場合 */
		{
			cslcolor(g,MIKA_bk_color); /* フラグが=1の時は表示を消去 */
			type_mes=timemessage(MIKA_utikiri_flag2,t1,t2); /* 経過時間分秒のメッセージを作成 */
			cslput(g,3*16,1,type_mes); /* 経過時間分秒のメッセージを消去 */
		}
	}
	void displinetrain(Graphics g,String mest)
	{
		int i,j,k,l;
		cslclr(g); /* 画面クリア */
		disptitle(g,mest,MIKA_type_kind_mes); /* 練習テキスト表題を表示 */
		displtitle1(g); /* テキスト練習画面 上部表示 */
		displtitle2(g); /* テキスト練習画面 メッセージと区切り線を表示 */
		cslcolor(g,Color.black); /* 表示色を黒色に設定 */
		for(i=0;i<8;i++)
		{
			j=i+MIKA_scroll_point; /* 練習テキスト表示開始行位置取得 */
			if(j<MIKA_max_c_line) /* 最大行まで練習テキストを表示 */
			{
				cslput(g,MIKA_start_text_line+MIKA_double_text_hight*i,0,MIKA_c_line[j]); /*練習テキストを一行表示 */
			}
			else
			{
				break;
			}
		}
		for(i=0;i<MIKA_text_point_x;i++) /* 入力済テキストを表示 */
		{
			j=i+MIKA_scroll_point; /* 入力済テキスト表示開始行取得 */
			if(j<MIKA_max_c_line) /* 一行分入力済のテキストを表示 */
			{
				k=MIKA_start_text_line+MIKA_double_text_hight*i+MIKA_rensyu_text_hight; /* 入力テキスト表示x座標設定 */
				cslcolor(g,Color.blue); /* 表示色を青色に設定 */
				cslput(g,k,0,MIKA_c_line[j]); /* 入力済テキスト表示 */
				l=MIKA_c_line[j].length(); /* 入力済みテキスト文字数取得 */
				cslputu(g,k,0,MIKA_c_line[j],1,MIKA_color_text_under_line); /* 入力済テキストに青色アンダーライン描画 */
				cslputu(g,k,l*8," ",1,MIKA_color_text_under_line); /* 入力済テキスト行末にアンダーラインを一文字分描画 */
			}
			else
			{
				break;
			}
		}
		k=MIKA_start_text_line+MIKA_double_text_hight*MIKA_text_point_x+MIKA_rensyu_text_hight; /* 入力中の行の表示 x座標取得 */
		j=MIKA_text_point_x+MIKA_scroll_point; /* 入力済テキスト行位置取得 */
		for(i=0;i<MIKA_text_point_y;i++) /* 入力済の文字を一文字づつ表示 */
		{
				cslcolor(g,Color.blue); /* 表示色を青色に設定 */
				cslput(g,k,i*8,String.valueOf(MIKA_c_line[j].charAt(i))); /* 入力済の文字を表示 */
				cslputu(g,k,i*8,"a",1,MIKA_color_text_under_line); /* 入力済文字にアンダーラインを描画 */
		}
		if(MIKA_err_count>0) /* エラー入力文字がある場合 */ 
		{
			for(i=0;i<MIKA_err_count;i++)
			{
				l=(MIKA_text_point_y+i)*8; /*エラー文字表示y座標取得 */
				dispbkcharline(g,k,l,MIKA_color_text_err); /* エラー文字の背景を赤色で表示 */
				cslcolor(g,Color.black); /* 表示色を黒に設定 */
				cslput(g,k,l,String.valueOf(MIKA_err_c_table[i])); /*エラー文字を表示 */
			}
		}
		if(MIKA_disp_cursor_flag==1) /* カーソル表示フラグがオンの場合はカーソルを表示 */
		{
			if(MIKA_text_point_y+MIKA_err_count<79) /* カーソル位置が行末でない場合 */
			{
				dispcur(g,scroll_cord_x(),scroll_cord_y(),1,MIKA_color_cursor); /* カーソル表示 */
			}
			else /* カーソル位置が行末の場合 */
			{
			dispcur(g,scroll_cord_x(),scroll_cord_y()-8,1,MIKA_color_cursor); /* カーソル表示 */
			}
		}
		if(MIKA_practice_end_flag==1) /* 練習終了時 */
		{
			cslrectt (g,21*16+8,0,24*16+8,80*8,MIKA_bk_color); /* 表示メッセージの背景を消去 */
			if(MIKA_type_syuryou_flag==2) /* 記録更新時 */
			{
				dispupmes(g,0); /* 記録を更新しましたの表示を行う */
			}
			dispretrymessage(g,0); /* リトライメッセージ表示 */
		}
	}
	void ppseiseki(Graphics g,int i,int j,String[] menu_mes,long[] r_mojisu,double[] r_speed,double[] w_speed,String[] r_date,long[] r_time,long[] t_kaisu) /* 成績表示 */
/* i 表示位置 j 表示個数 menu_mes 練習テキスト表題 r_mojisu 練習テキスト文字数 r_speed 最高速度 文字数／分w_speed 最高速度 ワード／分 r_date 達成日 r_time 累積練習時間 t_kaisu 練習回数 */
	{
		int ii;
		String a,b;
		for(ii=0;ii<j;ii++)
		{
			a=String.format("%1d:%s",ii+1,menu_mes[ii]);
			cslput(g,(i+ii)*16,1,a); /* 練習テキスト表題を表示 */
			if(r_mojisu[ii]!=0) /* 練習テキスト文字数が 0 でない場合 */
			{
				a=String.format("%6d",r_mojisu[ii]); /* 練習テキスト文字数を文字列に変換 */
				cslput(g,(i+ii)*16,32*8,a); /* 練習テキスト文字数を表示 */
				
			}
			if(r_speed[ii]!=0.0) /*最高入力速度が 0.0 でない場合 */
			{
				a=String.format("%6.1f(%5.1f)",r_speed[ii],w_speed[ii]); /* 最高入力速度を文字列に変換 */
				cslput(g,(i+ii)*16,38*8,a); /* 最高入力速度を表示 */
			}
			cslput(g,(i+ii)*16,52*8,r_date[ii]); /* 達成日を表示 */
			b=tconv(r_time[ii]); /* 累積練習時間を文字列に変換 */
			cslput(g,(i+ii)*16,59*8,b); /* 累積練習時間を表示 */
			b=String.format("%4d",t_kaisu[ii]); /* 練習回数を文字列に変換 */
			cslput(g,(i+ii)*16,76*8,b); /* 練習回数を表示 */
		}
	}
	void dispseiseki(Graphics g) /* 成績表示 */
	{
		int i;
		long time_i;
		String a,aa,b;
		cslclr(g); /* 画面クリア */
		a=tconv(MIKA_rt_t); /* 前回までの合計練習時間を文字列に変換 */
		aa=String.format("前回までの練習時間　%s",a); /* 前回までの合計練習時間のメッセージ作成 */
		cslcolor(g,MIKA_green); /* 表示色を緑色に設定 */
		cslput(g,1,1,aa); /* 前回までの合計練習時間を表示 */
		cslcolor(g,MIKA_blue); /* 表示色を青色に設定 */
		cslput(g,1,43*8,MIKA_return_mes); /* エスケープキーを押すとメニューに戻りますのメッセージを表示 */
		MIKA_lt_t=System.currentTimeMillis(); /* 現在時刻をミリ秒で取得 */
		time_i=timeinterval(MIKA_st_t,MIKA_lt_t); /* 今回練習時間を秒で計算 */
		a=tconv(time_i); /* 今回練習時間を文字列に変換 */
		aa=String.format("今回の練習時間　　　%s",a); /* 今回練習時間のメッセージを作成 */
		cslcolor(g,MIKA_green); /* 表示色を緑色に設定 */
		cslput(g,16,1,aa); /* 今回練習時間を表示 */
		cslcolor(g,MIKA_blue); /* 表示色を青色に設定 */
		cslput(g,3*16,1,MIKA_mest2); /* 成績メッセージ表示 */
		cslput(g,4*16,1,MIKA_mest3); /* 成績メッセージ下一行を表示 */
		cslcolor(g,MIKA_orange); /* 表示色をオレンジに設定 */
		ppseiseki(g,6,6,MIKA_ti_text,MIKA_t_mojisu,MIKA_t_speed,MIKA_tw_speed,MIKA_t_date,MIKA_t_time,MIKA_t_kaisu); /* 成績を表示 */
	}
	void dispstart(Graphics g) /* 著作権表示 */
	{
		int i;
		MIKA_max_x_flag=1; /* 縦 20行モードに設定 */
		MIKA_max_y_flag=1;/* 横 64カラムモードに設定 */
		String title_bar="●●●●●●●●●●●●●●●●●●●●●●●●●";
		cslclr(g); /* 画面クリア */
		cslcolor(g,MIKA_magenta); /* 表示色をマゼンタに設定 */
		cslput(g,3*16,7*8,title_bar); /* 表示枠 上端を表示 */
		for (i=4;i<15;i++)
		{
			cslput(g,i*16,7*8,"●"); /* 表示枠 左端を表示 */
			cslput(g,i*16,55*8,"●"); /* 表示枠 右端を表示 */
		}	
		cslput(g,15*16,7*8,title_bar); /* 表示枠 下端を表示 */
		cslcolor(g,MIKA_blue); /* 表示色を青に設定 */
		cslmencenter(g,5*16+8,"美佳の英文タイプトレーナー テキスト練習編");
		cslcolor(g,MIKA_cyan); /* 表示色をシアンに設定 */
		cslmencenter(g,7*16+8,"ＭＩＫＡＴＥＸＴ Ｖer２.０２.０１");
		cslcolor(g,MIKA_orange); /* 表示色をオレンジに設定 */
		cslmencenter(g,9*16+6,"＜＜英文タイプ練習のために＞＞");
		cslcolor(g,MIKA_cyan); /* 表示色をシアンに設定 */
		cslmencenter(g,13*16+8,"Copy right 1992/8/24  今村 二朗");
		cslput(g,17*16,24*8,"キーをどれか押すとメニューを表示します");
		MIKA_max_x_flag=0; /* 縦 25行モードに戻す */
		MIKA_max_y_flag=0; /* 横 80カラムモードに戻す */
	}
	void dispmen(Graphics g) /* メニュー及び練習画面表示 */
	{
		if(MIKA_exec_func_no==0) dispstart(g); /* 著作権表示 */
		else if (MIKA_exec_func_no==1) menexe(g,MIKA_menu_mes_s,MIKA_menu_cord_s,MIKA_menu_s_function,MIKA_menu_s_sel_flag,MIKA_mes0); /* 初期メニュー表示 */
		else if (MIKA_exec_func_no==19) dispseiseki(g); /* 成績表示 */
	else if(MIKA_exec_func_no>1100&&MIKA_exec_func_no<1200) displinetrain(g,MIKA_mesta); /* 英文テキスト練習の各項目の実行画面表示 */
	}
	void menexe(Graphics g,String[] menu_mes,int[][] menu_cord,int[] menu_function,int[] sel_flag,String menut)
	{
		int i,j;
		int x;
		int y;
		char a;
		String	mesi5="番号キーを押して下さい";
		MIKA_max_x_flag=0; /* 縦 25行モードに設定 */
		MIKA_max_y_flag=0; /* 横 80カラムモードに設定 */
		cslclr(g); /* 画面クリア */
		cslcolor(g,MIKA_magenta); /* 表示色をマゼンタに設定 */
		cslmencenter(g,1,menut); /* メニュータイトルを上端の中央に表示 */
		MIKA_max_x_flag=1; /* 縦 20行モードに設定 */
		MIKA_max_y_flag=1; /* 横 64カラムモードに設定 */
		cslcolor(g,MIKA_cyan); /* 表示色をシアンに設定 */
		cslput(g,18*16,29*8,mesi5); /* 番号キーを押して下さいのメッセージを表示 */
		j=menu_mes.length;
		for(i=0;i<j;i++)
		{
			x=menu_cord[i][0]; /* メニュー表示位置 x座標取得 */
			y=menu_cord[i][1]; /* メニュー表示位置 y座標取得 */
			if(sel_flag[i]==1)	cslcolor(g,MIKA_green); /*前回選択メニュー項目は緑色で表示 */
			else 	cslcolor(g,MIKA_blue); /* その他のメニューは青色で表示 */
			cslput(g,x,y,menu_mes[i]); /* メニュー項目表示 */
			a=(char)(i+'１');
			cslput(g,x,y-4*MIKA_width_y,String.valueOf(a)); /* メニュー番号を表示 */
			if(sel_flag[i]==1) cslputu(g,x,y,menu_mes[i],1,MIKA_green); /* 前回選択メニュー項目に下線を表示 */
		}
		MIKA_menu_function_table=menu_function; /* 機能番号テーブル設定 */
		MIKA_sel_flag=sel_flag; /* 前回選択メニュー項目選択フラグアドレス設定 */
		MIKA_max_x_flag=0; /* 縦 25行モードに戻す */
		MIKA_max_y_flag=0; /* 横 80カラムモードに戻す */
}
	int mencom(int[] menu_function_table,int[] sel_flag,char nChar) /* 選択されたメニューの項目に対応する機能番号を取得 */
	{
		int func_no=0;
		int i,ii,iii;
		int sel_flag1=0;
		if(menu_function_table==null) return(0);
		ii=menu_function_table.length;
		if(nChar==0x1b){ /* 入力文字がエスケープの場合 */
			for(i=0;i<ii;i++) /* メニューに戻りますのメニュー項目をサーチ */
			{
				if(menu_function_table[i]>9000&&menu_function_table[i]<9999) /* メニューに戻りますのメニュー項目があった場合 */  
				{
					func_no=menu_function_table[i];
				}
			}
			return(func_no);
		}
		else if(nChar<=0x30||nChar>0x39) return(0); /* 入力文字が1～9の数字以外は処理をしないでリターン */
		else
		{
			iii=nChar-0x31; /* 文字を数字に変換 */
			if(iii<ii) /* 入力された数字に対応するメニューがある場合 */
			{
				func_no=menu_function_table[iii]; /* 対応する機能番号を取り出す */
				for(i=0;i<ii;i++)
				{
						if(sel_flag[i]!=0) sel_flag1=i+1; /* 前回選択メニュー項目番号をサーチ */
				}
				if(0<func_no&&func_no<9000) /* 今回選択メニューがメニューに戻るで無い場合 */
				{
					if(sel_flag1!=0) sel_flag[sel_flag1-1]=0; /*前回選択メニュー番号をクリア */
					sel_flag[iii]=1; /* 今回の選択メニュー番号を前回選択メニュー番号に設定 */
				}
				return(func_no);
			}
			else
			return(0);
		}	
	}	
	int exec_func(Graphics g,char nChar) /* 一文字入力に対応した処理を行う */
	{
		int func_no;
		if(MIKA_exec_func_no==0) /* 最初の初期画面を表示中にキーが押された場合 */
		{
			MIKA_exec_func_no=1; /* 初期画面の表示番号を設定 */
			dispmen(g); /* メニュー表示 */
			return(1);
		}
		func_no=mencom(MIKA_menu_function_table,MIKA_sel_flag,nChar); /* 選択されたメニューの項目に対応する機能番号を取得 */
		if(func_no!=0) /* メニュー表示中に数字キーが押されて対応する機能番号がゼロでない場合 */
		{
			MIKA_menu_function_table=null;
			MIKA_exec_func_no=func_no;
			if(MIKA_exec_func_no==9999){
				procexit(); /* 機能番号が 9999の時は終了 */
				System.exit(0); /* プログラム終了 */
			}
			if (MIKA_exec_func_no>9000) MIKA_exec_func_no=MIKA_exec_func_no-9000; /* 機能番号がメニューに戻るの時は、メニュー番号を取得 */
			if(MIKA_exec_func_no>1100&&MIKA_exec_func_no<1200) /* 機能番号が英文テキスト練習の場合は各練習の項目ごとに前処理を行う */
			{
				preplinetrain(MIKA_exec_func_no); /* 英文テキスト練習の各項目ごとの前処理 */
			}
			dispmen(g); /* メニュー、練習画面表示 */
			return(1);
		}
		else /* 練習の実行中にキーが押された場合 */
		{
			if(nChar==0x1b&&MIKA_exec_func_no==19) /* 成績表示中にエスケープキーが押された場合 */
			{
				MIKA_exec_func_no=1; /* 初期メニューのメニュー番号設定 */
				dispmen(g); /* メニュー表示 */
				return(1);
			}
			if(MIKA_exec_func_no>1100&&MIKA_exec_func_no<1200) /* 英文テキスト練習の実行中の場合 */
			{
				proclinetrain(g,nChar); /* 英文テキスト練習の文字入力処理 */
				return(1);
			}
		}
		return(0);
	}
	String  spacepadding(int i) /* 指定文字数のスペース文字列をリターン */
	{
			String a="                                                  "; /* 50文字のスペース文字列 */
			String b=""; /* 空文字列 */
			String aa;
			if(i<=0) return b; /* 指定文字数がゼロ以下の場合は空文字列を返す */
			else if(i>=50) return a; /* 指定文字数が50以上の場合は50文字のスペースを返す */
			else
			{
				aa=a.substring(0,i); /* 指定文字数のスペース文字列を切り出し */
				return aa;
			}
	}
	long timeinterval(long time_start,long time_end) /* ミリ秒で指定された時間間隔の経過時間を秒に変換 */
	{
			long time_interval;
			time_interval=(time_end-time_start)/1000; /* 開始時間ミリ秒と終了時間ミリ秒の差を秒に変換 */
			if(time_interval<=0) time_interval=1; /* 経過時間がゼロ秒以下の場合は1秒を設定 */
			return time_interval;
	}
	String	stringseiseki3(String[] menu_mes,long[] r_mojisu,double[] r_speed,double[] w_speed,String[] r_date,long[] r_time,long[] t_kaisu,int i) /* 英文テキスト練習の成績を成績ファイルに書き込むための文字列を作成 */
	{
			String a,aa,aaa,sp;
			int	length;
			a=t0conv(r_time[i],2); /* 合計練習時間を文字列に変換 */
			aa=menu_mes[i]; /* 練習テキストファイル名を取得 */
			length=stringlength(aa); /* 練習項目の文字列長を取得（全角文字を二文字に数える) */
			sp=spacepadding(14-length); /* 練習項目の文字列長を揃えるためにスペース文字を追加 */
			aaa=String.format("%s%s%6d%8.1f(%5.1f) %s%s%4d\n",aa,sp,r_mojisu[i],r_speed[i],w_speed[i],r_date[i],a,t_kaisu[i]); /* 練習成績文字列作成 */
//			System.out.printf("成績ファイル=(%s)\n",aaa);
			return aaa;
	}
	int wseiseki() /* 練習終了時に成績ファイルを書き込み */
	{
  	  	String a,aa,aaa,sp;
		long time_i;
		int err;
		BufferedWriter bw;
		OutputStreamWriter filewriter;
		FileOutputStream file;
		int i,length;
//		System.out.printf("proc Exit\n");
		bw=null;
		err=0;
		try {
			file = new FileOutputStream(MIKA_file_name_seiseki2); /* 成績ファイルをオープン */
	  	 	filewriter = new OutputStreamWriter(file,"SHIFT_JIS"); /* 成績ファイルの読み込みフォーマットをシフトJISに指定でオープン */
			bw = new BufferedWriter(filewriter);
// 	  		System.out.printf("file_write\n");
			time_i=timeinterval(MIKA_st_t,MIKA_lt_t); /* 練習開始から終了までの経過秒を取得 */
			a=tconv(MIKA_rt_t+time_i); /* 前回の練習時間に今回の練習時間を加算して文字列に変換 */
			aa=String.format("練習時間　%s\n",a);
			bw.write(aa); /* 成績ファイル書き込み */
			for(i=0;i<MIKA_t_seiseki_name.length;i++) /* 英文テキスト練習成績書き込み */
			{
				aaa=stringseiseki3(MIKA_t_seiseki_name,MIKA_t_mojisu,MIKA_t_speed,MIKA_tw_speed,MIKA_t_date,MIKA_t_time,MIKA_t_kaisu,i); /* 英文テキスト練習の成績を文字列に変換 */
   				bw.write(aaa); /* 成績ファイル書き込み */
	  		}
		} catch (IOException e) { /* 書き込みエラーの場合 */
//        	e.printStackTrace();
			err=1; /* エラーコードを1に設定 */
		} finally { /* 成績ファイルの書き込みが終了した場合 */
			if(bw!=null) /* ファイルがオープンされた場合はクローズ処理を行う */
			{
				try {
					bw.close();
				} catch (IOException e) { /* ファイルクローズ処理がエラーの場合 */
//		           	e.printStackTrace();
					return(1); /* エラーコードを1に設定 */
				}
			}
			return(err); /* エラーコードを返してリターン */
		}
	}
	int wkiroku() /* 練習終了時に練習開始時刻と練習時間を練習時間記録ファイルに書き込む */
	
	{
  	  	String a,aa,aaa,sp;
		int err;
		String type_rensyu_date;
		BufferedWriter bw;
		OutputStreamWriter filewriter;
		FileOutputStream file;
		long time_i;
		int i,length;
//		System.out.printf("proc Exit\n");
		err=0;
		SimpleDateFormat rensyu_date=new SimpleDateFormat("yy/MM/dd HH:mm:ss"); /* 練習開始日時 取り出しフォーマット作成 */
		type_rensyu_date=rensyu_date.format(MIKA_s_date); /* 練習開始日時を文字列を指定フォーマットに従って作成 */
		time_i=timeinterval(MIKA_st_t,MIKA_lt_t); /* 練習時間取得 */
		a=t0conv(time_i,1); /* 練習時間を"%3d時間%2d分%2d秒"のフォーマットで文字列に変換 */
		aa=String.format("%s 練習時間%s\n",type_rensyu_date,a); /* 書き込みメッセージ作成 */
		bw=null;
		try {
			file = new FileOutputStream(MIKA_file_name_kiroku,true); /* 追記モードで練習時間記録ファイルをオープン */
	  	 	filewriter = new OutputStreamWriter(file,"SHIFT_JIS"); /* 練習時間記録ファイルの書き込みモードをシフトJISに指定 */
			bw = new BufferedWriter(filewriter);
// 	  		System.out.printf("file_write\n");
			bw.write(aa); /* 練習時間記録ファイルに練習記録メッセージを書き込み */
		} catch (IOException e) {
//          	e.printStackTrace();
				err=1;
		} finally {
			if(bw!=null)
			{
				try {
					bw.close(); /* 練習時間記録ファイルをクローズ */
				} catch (IOException e) {
//		           	e.printStackTrace();
					return(1);
				}
			}
			return(err);
		}
	}
	int whayasa() /* 最高入力速度を最高速度記録ファイル書き込む */
	{
  	  	String a,aa,aaa,sp;
		int err;
		String type_rensyu_date;
		BufferedWriter bw;
		OutputStreamWriter filewriter;
		FileOutputStream file;
		long time_i;
		int length1,length2;
		length1=0;
		length2=0;
		err=0;
//		System.out.printf("proc Exit\n");
		SimpleDateFormat rensyu_date=new SimpleDateFormat("yy/MM/dd HH:mm:ss");/* 最高記録達成日時 取り出しフォーマット作成 */
		type_rensyu_date=rensyu_date.format(MIKA_type_kiroku_date); /* 最高記録達成時刻文字列を指定フォーマットに従って作成 */
		a=t0conv(MIKA_type_time_record[MIKA_type_kind_no],2); /* 累積練習時間を文字列に変換 */
		aa=String.format("%s %s%5d字%6.1f字(%5.1fﾜｰﾄﾞ)/分%s%3d回\n",type_rensyu_date,MIKA_menu_kind_mes,MIKA_type_count,MIKA_type_speed,MIKA_type_word_speed,a,MIKA_p_count[MIKA_type_kind_no]); /* 書き込みメッセージ作成 */
		bw=null;
		try {
			file = new FileOutputStream(MIKA_file_name_hayasa,true); /* 追記モードで最高速度記録ファイルをオープン */
	  	 	filewriter = new OutputStreamWriter(file,"SHIFT_JIS"); /* 最高速度記録ファイルの書き込みモードをシフトJISに設定 */
			bw = new BufferedWriter(filewriter);
// 	  		System.out.printf("file_write\n");
			bw.write(aa); /* 最高速度記録ファイルに最高速度記録メッセージを書き込み  */
		} catch (IOException e) {
//         	e.printStackTrace();
			err=1;
		} finally {
			if(bw!=null)
			{
				try {
					bw.close(); /* 最高速度記録ファイルクローズ */
				} catch (IOException e) {
//		           	e.printStackTrace();
					return(1);
				}
			}
			return(err);
		}
	}
	void savekiroku() /* プログラムがウィンドーの閉じるボタンにより終了した場合、練習記録を保存する */
	{
		if(1100<MIKA_exec_func_no&&MIKA_exec_func_no<1200) /* 英文テキスト練習中の場合 */
		{
			if(MIKA_practice_end_flag==0&&MIKA_time_start_flag!=0) /* 練習中で練習時間の計測を開始した場合 */
			{
				MIKA_type_end_time=System.currentTimeMillis(); /* 練習終了時間をミリ秒で取得 */
				MIKA_type_time_record[MIKA_type_kind_no]=MIKA_type_time_record[MIKA_type_kind_no]+timeinterval(MIKA_type_start_time,MIKA_type_end_time); /* 練習終了時間をミリ秒で取得 */
			}
			if(MIKA_type_syuryou_flag==1||MIKA_type_syuryou_flag==2) /* 最高記録を更新して練習を終了した場合 */
			{
				MIKA_type_speed_record[MIKA_type_kind_no]=MIKA_type_speed; /* 練習記録 最高入力速度 文字数／分を更新 */
				MIKA_type_word_speed_record[MIKA_type_kind_no]=MIKA_type_word_speed; /* 練習記録 最高入力速度 ワード／分を更新 */
				MIKA_type_date_record[MIKA_type_kind_no]=MIKA_type_date; /* 練習記録 達成日を更新 */
			}
		}
	}
	String 	mesfileerr() /* ファイル書き込みエラーメッセージ作成 */
	{
		String a,a1,a2,a3;
		if(MIKA_file_error_seiseki==1||MIKA_file_error_kiroku==1||MIKA_file_error_hayasa==1)
		{	
			if(MIKA_file_error_seiseki==1) a1=String.format("%s\n",MIKA_file_name_seiseki); /* 成績ファイル名設定 */
			else a1="";
			if(MIKA_file_error_kiroku==1) a2=String.format("%s\n",MIKA_file_name_kiroku); /* 練習時間記録ファイル名設定 */
			else a2="";
			if(MIKA_file_error_hayasa==1) a3=String.format("%s\n",MIKA_file_name_hayasa); /* 最高速度記録ファイル名を設定 */
			else a3="";
			a=String.format("成績ファイル\n%s%s%sの書き込みができませんでした。",a1,a2,a3); /* エラーメッセージ作成 */
		}
		else a=""; /* エラーが無い場合は空メッセージ */
		return a;
	}
	void procexit() /* プログラム終了時の処理 */
	{
		String a;
		Container c;
		MIKA_lt_t=System.currentTimeMillis(); /* 練習時間記録ファイル用練習終了時間をミリ秒で取得 */
		MIKA_file_error_seiseki=wseiseki(); /* 成績ファイル書き込み */
		MIKA_file_error_kiroku=wkiroku(); /* 練習時間記録ファイル書き込み */
//		MIKA_file_error_seiseki=1;
//		MIKA_file_error_kiroku=1;
//		MIKA_file_error_hayasa=1;
		if(MIKA_file_error_seiseki==1||MIKA_file_error_kiroku==1||MIKA_file_error_hayasa==1) /* 成績ファイル書き込みエラーの場合 */
		{
			a=mesfileerr(); /* 成績ファイル書き込みエラーメッセジ作成 */
			c = getContentPane();
			JOptionPane.showMessageDialog(c.getParent(),a,"成績ファイル書き込みエラー",JOptionPane.WARNING_MESSAGE);
			/* 成績ファイル書き込みエラーダイアログ表示 */
		}
	}		
	double ftypespeed(int count,long start_time,long end_time) /* 一分間あたりのタイプ速度を計算 */
// count 文字数
// start_time 開始時間 ミリ秒
// end_time 終了時間 ミリ秒
	{
		double speed_rate;
		double r_count;
		r_count=count;
		if(end_time==start_time) speed_rate=0.0; /* 開始時間と終了時間が一致する場合はタイプ速度をゼロに指定 */
		else
		{
			speed_rate=1000.0*60.0*r_count/(end_time-start_time); /* 一分間あたりのタイプ速度を計算 */
		}
		return speed_rate;
	}
	void dispspeedrate3(Graphics g,int flag) /* 英文テキスト練習 入力速度表示 */
// flag=0 表示 flag=1 消去
	{
		String a,b;
		int offset_a,offset_b;
		if(flag==0)
		{
			cslcolor(g,MIKA_blue); /* flagが=ゼロの時は青色で表示 */
			b=String.format("入力速度%6.1f文字(%5.1fワード)/分",MIKA_type_speed,MIKA_type_word_speed); /* 入力速度を文字列に変換 */
			cslput(g,3*16,21*8,b); /* 入力速度を表示 */
		}
		else
		{
			cslcolor(g,MIKA_bk_color); /* flagが=1の場合は表示消去 */
			a=String.format("%6.1f",MIKA_type_speed); /* 入力速度 文字数／分を文字列に変換 */
			offset_a=8;
			cslput(g,3*16,(21+offset_a)*8,a); /* 入力速度 文字数／分を消去 */
			b=String.format("%5.1f",MIKA_type_word_speed); /* 入力速度 ワード／分を文字列に変換 */
			offset_b=19;
			cslput(g,3*16,(21+offset_b)*8,b); /* 入力速度 ワード／分を消去 */
		}
	}
	void procdispspeed3(Graphics g)  /* 英文テキスト練習 入力速度を計算して再表示 */
	{
			if(MIKA_type_speed_time<60.0&&MIKA_ttype_speed_time>=60.0) disptime2(g,2); /* 前回練習経過時間表示を消去 */
			else disptime2(g,1);
			dispspeedrate3(g,1); /* 前回 入力速度表示を消去 */
			MIKA_type_speed_time=MIKA_ttype_speed_time; /* 練習経過時間を更新 */
 			MIKA_type_speed=ftypespeed(MIKA_type_count,MIKA_type_start_time,MIKA_type_end_time); /* 入力速度  文字数／分を取得 */
	 		MIKA_type_word_speed=MIKA_type_speed/5.0; /* 入力速度 ワード／分を取得 */
			disptime2(g,0); /* 今回練習経過時間を表示 */
			dispspeedrate3(g,0); /* 今回 入力速度を表示 */
	}
	void preplinetrain(int func_no) /* 練習の前処理 */
	{
			if(MIKA_exec_func_no>1100&&MIKA_exec_func_no<1200) /* 英文テキスト練習の前処理 */
			{
					MIKA_type_kind_no=func_no-1101; /* 練習項目番号を取得 */
					MIKA_menu_kind_mes=MIKA_t_seiseki_name[MIKA_type_kind_no]; /* 最高速度記録ファイル書き込み用メッセージ設定 */
					MIKA_type_speed_record=MIKA_t_speed; /* 最高速度記録配列 文字数／分アドレスに 英文テキスト練習 最高速度記録 文字数／分 を設定 */
					MIKA_type_word_speed_record=MIKA_tw_speed; /* 最高速度記録配列 ワード／分アドレスに 英文テキスト練習 最高速度記録 ワード／分 を設定 */
					MIKA_type_date_record=MIKA_t_date; /* 最高速度達成日配列アドレスに 英文テキスト練習 最高速度達成日付 を設定 */
					MIKA_type_time_record=MIKA_t_time; /* 累積練習時間配列アドレスに 英文テキスト練習 累積練習時間 を設定 */
					MIKA_p_count=MIKA_t_kaisu; /* 練習回数配列アドレスに英文テキスト練習 練習回数 を設定 */
					MIKA_type_kind_mes=MIKA_ti_text[MIKA_type_kind_no]; /* 練習テキスト表題を設定 */ 
					prepflagsline(); /* 英文テキスト練習開始時のフラグクリア */
					MIKA_max_c_line=readtextfiles(MIKA_t_name[MIKA_type_kind_no],MIKA_c_line,0,3000); /* 練習テキスト読み込み */
			}
	}
	void prepflagsline() /* 英文テキスト練習開始時のフラグクリア処理 */
	{
		MIKA_practice_end_flag=0; /* 練習実行中フラグ クリア */
		MIKA_time_start_flag=0; /* 時間計測開始フラグ クリア */
		MIKA_current_point_x=1; /* 練習テキストの入力行の位置を1に設定 */
		MIKA_scroll_point=1; /* 画面スクロール表示の開始行の位置を1に設定 */
		MIKA_text_point_x=0; /* 文字入力カーソル縦行位置クリア */
		MIKA_text_point_y=0; /* 文字入力カーソル横文字位置クリア */
		MIKA_err_count=0; /* エラー文字表示文字数クリア */
		MIKA_type_count=0; /* 入力文字数カウンター クリア */
		MIKA_type_speed=0.0; /* 練習テキストの入力速度 文字数／分クリア */
		MIKA_type_word_speed=0.0; /* 練習テキストの入力速度 ワード／分クリア */
		MIKA_type_speed_time=0.0; /* 前回 練習経過時間 クリア */
		MIKA_ttype_speed_time=0.0; /* 今回 練習経過時間 クリア */
		MIKA_type_err_count=0; /* エラー入力文字数カウンター クリア */
		MIKA_utikiri_flag=0; /* 速度表示時の練習テキスト打ち切りフラグ クリア */
		MIKA_utikiri_flag2=0; /* 前回速度表示時の練習テキスト打ち切りフラグ クリア */
		MIKA_type_syuryou_flag=0; /* 練習終了時の記録更新フラグ クリア */
		MIKA_disp_cursor_flag=1; /* カーソル表示フラグを1に設定 */
	}
	int	rtextfile(BufferedReader b_reader,String[] seiseki,int offset_i,int file_length) /* 英文練習テキストファイル読み込み */
	{
			int i,err;
			int f_length;
			String seiseki_line;
			f_length=0;
			err=0;
			for(i=0;i<file_length;i++)/* 英文練習テキストファイルを指定行数読み込み */
			{
				try {
					seiseki_line=b_reader.readLine(); /* 英文練習テキストファイルファイルを一行読み込み */
//					System.out.printf("seiseki file line=%s\n",seiseki_line);
					if(seiseki_line==null) break; /* ファイルエンドの場合は読み込みを終了 */
					else
					{
						seiseki[i+offset_i]=seiseki_line; /* 英文練習テキストファイルの一行を指定エリアに保存 */
			 			f_length=i+1; /* 読み込みファイル行数を保存 */
					}
				}
				 catch ( IOException e) {
				err=1;
				}
      			if(err!=0) return(0);  /* 読み込みエラー時には読み込み行数ゼロでリターン */ 
    		}
			return(f_length); /* 読み込みファイル行数をリターン */
	}
	int readtextfiles(String text_file_name,String[] c_line,int offset_i,int file_length) /* 英文練習テキストファイルをオープンして指定行読み込みクローズする */
	{
     	int err;
		int f_length;
		f_length=0;
		File file = new File(text_file_name); /* 成績ファイルオープン */
		try {
	         BufferedReader b_reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"Shift-JIS"));
			f_length=rtextfile(b_reader,c_line,offset_i,file_length); /* 練習成績ファイル読み込み */
//			MIKA_c_line[0]="ABCDEFG";
//			MIKA_c_line[1]="HIJKLMN";
			try{
				b_reader.close(); /* 練習成績ファイルクローズ */
			}
			catch ( IOException e) { 
			    	e.printStackTrace();
			}
		}
		catch (UnsupportedEncodingException | FileNotFoundException e) {
		}
		return(f_length);
	};
	void dispretrymessage(Graphics g,int flag) /* リトライメッセージ表示 flag=0 表示を行う flag=1 表示を消去 */
	{
		if(flag==0) cslcolor(g,MIKA_cyan); /* 表示色をシアンに設定 */
		else cslcolor(g,MIKA_bk_color); /* 表示色を背景色に設定 */
		cslput(g,22*16,10*8,MIKA_mesi1); /* 「もう一度練習するときはリターンキーまたは、Enterキーを押してください」のメッセージを表示 */
		cslput(g,23*16,10*8,MIKA_mesi2); /* 「メニューに戻るときはESCキーを押してください」のメッセージを表示 */
	}
	int funcbackmenu(int func_no) /* メニューの階層を一段上に戻る */
	{
		int ffun_no;
		ffun_no=1; /* 初期メニューに戻る */
		return ffun_no;
	}
	void proclinetrain(Graphics g,char nChar) /* 英文テキスト練習の文字入力処理 */
	{
//			System.out.printf("char %x pressed\n",(int) nChar);
		if(nChar==0x1b){ /* エスケープキー入力の場合 */
			if(MIKA_practice_end_flag==0) /* 入力練習実行中の場合 */
			{
				MIKA_practice_end_flag=1; /* 練習実行中フラグを終了にセット */
				if(MIKA_time_start_flag!=0) /* 最初の正解を入力済の場合 */
				{
					MIKA_Procttimer.cancel();		/* 入力速度表示の一秒間隔タイマーをキャンセル */				
					MIKA_type_end_time=System.currentTimeMillis(); /* 練習終了時間をミリ秒で取得 */
					MIKA_type_speed_time=roundtime((MIKA_type_end_time-MIKA_type_start_time)/1000.0); /* 練習経過時間 秒を計算 */
					MIKA_type_time_record[MIKA_type_kind_no]=MIKA_type_time_record[MIKA_type_kind_no]+(long)MIKA_type_speed_time; /* 累積練習時間の記録を加算 */
				}
				cslrectt (g,21*16+8,0,24*16+8,80*8,MIKA_bk_color); /* リトライメッセージ背景クリア */
				dispretrymessage(g,0); /* リトライメッセージ表示 */
			}
			else if(MIKA_practice_end_flag==1) /* 練習終了の場合 */
			{
				if(MIKA_type_syuryou_flag==1||MIKA_type_syuryou_flag==2)	 /* 練習記録を更新した場合 */
				{
					MIKA_type_speed_record[MIKA_type_kind_no]=MIKA_type_speed; /* 練習記録 最高入力速度 文字数／分 を更新 */
					MIKA_type_word_speed_record[MIKA_type_kind_no]=MIKA_type_word_speed; /* 練習記録 最高入力速度 ワード／分 を更新 */
					MIKA_type_date_record[MIKA_type_kind_no]=MIKA_type_date; /* 練習記録 達成日を更新 */
					MIKA_type_syuryou_flag=0; /* 練習終了時の記録更新フラグ クリア */
				}
				MIKA_exec_func_no=funcbackmenu(MIKA_exec_func_no); /* メニューを一階層戻る */
				dispmen(g); /* メニュー表示 */
			}
		}
		else if((nChar==0x0d||nChar==0x0a)&&MIKA_practice_end_flag==1)	 /* 練習の終了時に改行が入力された場合 */
		{
			MIKA_practice_end_flag=0; /* 練習実行中フラグをクリア */
			if(MIKA_type_syuryou_flag==1||MIKA_type_syuryou_flag==2)	 /* 練習記録を更新した場合 */
			{
				MIKA_type_speed_record[MIKA_type_kind_no]=MIKA_type_speed; /* 練習記録 最高入力速度 文字数／分 を更新 */
				MIKA_type_word_speed_record[MIKA_type_kind_no]=MIKA_type_word_speed; /* 練習記録 最高入力速度ワード／分を更新 */
				MIKA_type_date_record[MIKA_type_kind_no]=MIKA_type_date; /* 練習記録 達成日を更新 */
			}
			MIKA_type_syuryou_flag=0; /* 練習終了時の記録更新フラグ クリア */
			prepflagsline(); /* 英文テキスト練習開始時のフラグクリア */
			dispmen(g); /* 英文テキスト練習画面 再表示 */
		}
		else if(MIKA_practice_end_flag==0) /* 練習実行中の場合 */
		{
//			System.out.printf("TYPE char %1c %1c\n",MIKA_key_char,nChar);a.charAt(i)
			if(MIKA_err_count>0&&nChar==0x08) /* エラー文字表示中にバックスペースにより訂正する場合 */
			{
				dispcur(g,scroll_cord_x(),scroll_cord_y(),1,MIKA_bk_color); /* 現在位置カーソル消去 */
				MIKA_err_count--; /* エラー文字表示文字数 デクリメント */
				dispbkcharline(g,scroll_cord_x(),scroll_cord_y(),MIKA_bk_color); /* エラー文字の一番右の端の文字の背景をクリア */
				cslcolor(g,MIKA_bk_color); /* 表示色に背景色を設定 */
				cslput(g,scroll_cord_x(),scroll_cord_y(),String.valueOf(MIKA_err_c_table[MIKA_err_count])); /* エラー文字の一番右端の文字をクリア */
				dispcur(g,scroll_cord_x(),scroll_cord_y(),1,MIKA_color_cursor); /* カーソルを一文字左に戻して表示 */
			}
			else if(MIKA_text_point_y>=MIKA_c_line[MIKA_current_point_x].length()) /* 行末に文字が入力された場合 */
			{
				if((nChar==0x20||nChar==0x0a||nChar==0x0d)&&MIKA_err_count==0) /* 入力された文字がスペースか改行の場合 */
				{
					dispcur(g,scroll_cord_x(),scroll_cord_y(),1,MIKA_bk_color); /* 現在位置カーソル消去 */
					if(MIKA_text_point_y>0)
					{
						cslcolor(g,Color.blue); /* 表示色を青色に設定 */
						cslput(g,scroll_cord_x(),scroll_cord_y()-8,String.valueOf(MIKA_c_line[MIKA_current_point_x].charAt(MIKA_text_point_y-1))); /* 正解入力文字の一つ手前の文字を再表示 */
					}
					cslputu(g,scroll_cord_x(),scroll_cord_y(),"a",1,MIKA_color_text_under_line); /* 行末にアンンダーラインを表示 */
					MIKA_current_point_x++; /* 練習テキストの入力行の位置をインクリメント */
					if(MIKA_current_point_x>=MIKA_max_c_line) /* すべての練習テキストを入力し終わった場合 */
					{
							MIKA_practice_end_flag=1; /* 練習実行中フラグを終了にセット */
							MIKA_Procttimer.cancel();		/* 入力速度表示の一秒間隔タイマーをキャンセル */
							MIKA_disp_cursor_flag=0; /* カーソル表示フラグをクリア */
							MIKA_text_point_x++; /* 文字入力カーソル縦位置インクリメント */
							MIKA_text_point_y=0; /* 文字入力カーソル横位置をゼロに設定 */
							try {
								MIKA_semaphore.acquire(); /* セマフォー要求 */
								MIKA_utikiri_flag=1; /* 練習打ち切りフラグをセット */
								MIKA_utikiri_flag2=0; /* 前回練習速度消去用にフラグをクリア */
								MIKA_type_end_time=System.currentTimeMillis(); /* 現在時刻をミリ秒で取得 */
								MIKA_ttype_speed_time=(MIKA_type_end_time-MIKA_type_start_time)/1000.0; /* 経過秒を実数で計算 */						procdispspeed3(g); /* 入力速度を表示 */
								MIKA_semaphore.release(); /* セマフォー解放 */
							}
							catch (Exception ex)
							{	
								ex.printStackTrace();
							}
							MIKA_type_time_record[MIKA_type_kind_no]=MIKA_type_time_record[MIKA_type_kind_no]+(long)MIKA_ttype_speed_time; /* 累積練習時間の記録を加算 */
							dispkaisu3(g,1); /* 前回練習回数表示クリア */
							MIKA_p_count[MIKA_type_kind_no]++; /* 練習回数インクリメント */
							dispkaisu3(g,0); /* 今回練習回数表示 */
							prockiroku(g); /* 記録を更新時の処理 */
							cslrectt (g,21*16+8,0,24*16+8,80*8,MIKA_bk_color); /* リトライメッセージ背景クリア */
							dispretrymessage(g,0); /* リトライメッセージ表示 */
					}
					else
					{
						if(MIKA_text_point_x==5) /* 五行目の入力で改行したときは練習テキストを二行分上にスクロールする */
						{
							cslscroll(g,MIKA_start_text_line,MIKA_double_text_hight,MIKA_rensyu_text_hight); /* 画面を二行分上方にコピー */
							if(MIKA_current_point_x+2<MIKA_max_c_line) /* 最下行に追加の練習テキスト表示がある場合 */
							{
								cslcolor(g,Color.black); /* 表示色を黒色に設定 */
								cslput(g,MIKA_start_text_line+MIKA_double_text_hight*7,0,MIKA_c_line[MIKA_current_point_x+2]); /* 最下行に練習テキスト表示 */
							}
							MIKA_scroll_point++; /* 画面スクロール表示の開始行位置をインクリメント */
						}
						else /* 行末の改行でスクロールしない場合 */
						{					
							MIKA_text_point_x++; /* 文字入力カーソル縦位置インクリメント */
						}
						MIKA_text_point_y=0; /* 文字入力カーソル横位置をゼロに設定 */
						dispcur(g,scroll_cord_x(),scroll_cord_y(),1,MIKA_color_cursor); /* 文字入力カーソルを表示 */
					}
				}
				else /* 行末の入力文字がエラーの場合 */
				{
					procerrchar(g,nChar); /* エラー入力文字の処理 */
				}
			}
			else
			{	
				MIKA_key_char=MIKA_c_line[MIKA_current_point_x].charAt(MIKA_text_point_y); /* 練習文字を取り出し */
				if	(MIKA_err_count==0&&MIKA_key_char==nChar) /* エラー文字表示がなくて入力が正解の場合  */
				{
				/* 正解の場合 */
					if(MIKA_time_start_flag==0) /* 最初の正解文字入力の場合 */
					{
						MIKA_type_start_time=System.currentTimeMillis();  /* 練習開始時間をミリ秒で取得取得 */
						MIKA_time_start_flag=1; /* 練習時間計測フラグセット */
						MIKA_Procttimer = new Procttimer();/* タイマー取得 */
						MIKA_timer.scheduleAtFixedRate(MIKA_Procttimer,MIKA_text_time_interval,MIKA_text_time_interval); /* タイマーを一秒間隔にセット */
					}
					dispcur(g,scroll_cord_x(),scroll_cord_y(),1,MIKA_bk_color); /* 現在位置のカーソルを消去 */
					cslcolor(g,Color.blue); /* 表示色を青色に設定 */
					if(MIKA_text_point_y>0)
					{
						cslput(g,scroll_cord_x(),scroll_cord_y()-8,String.valueOf(MIKA_c_line[MIKA_current_point_x].charAt(MIKA_text_point_y-1))); /* 正解入力文字の一つ手前の文字を再表示 */
					}
					cslput(g,scroll_cord_x(),scroll_cord_y(),String.valueOf(nChar)); /* 正解入力文字を表示 */
					cslputu(g,scroll_cord_x(),scroll_cord_y(),"a",1,MIKA_color_text_under_line); /* 正解文字入力にアンダーラインを描画 */
					MIKA_type_count++; /* 正解数を加算 */
					MIKA_text_point_y++; /* 文字入力カーソル横位置をインクリメント */
					dispcur(g,scroll_cord_x(),scroll_cord_y(),1,MIKA_color_cursor); /* カーソルを新位置に表示 */
				}
				else /* 入力文字がエラーの場合 */
				{
						procerrchar(g,nChar); /* エラー入力文字の処理 */
				}
			}
		}
	}
	int scroll_cord_x() /* カーソル位置 縦 x 座標算出 */
	{	
		return(MIKA_double_text_hight*MIKA_text_point_x+MIKA_start_text_line+MIKA_rensyu_text_hight);
	}
	int scroll_cord_y() /* カーソル位置 横 y 座標算出 */
	{
		return((MIKA_text_point_y+MIKA_err_count)*8);
	}
	void procerrchar(Graphics g,char nChar) /* エラー入力文字処理 */
	{
		if(0x20<=nChar&&nChar<=0x7E)/* エラー入力文字が表示可能文字の場合 */
		{
			disperror3(g,1); /* 前回エラー入力文数表示を消去 */
			MIKA_type_err_count++; /* エラー入力文字数カウンターをインクリメント */
			disperror3(g,0); /* 今回エラー入力文字数を表示 */		
			if(MIKA_text_point_y+MIKA_err_count<79) /* エラー文字入力位置が 行末でない場合 */
			{
				MIKA_err_c_table[MIKA_err_count]=nChar; /* エラー文字保存エリアにエラー文字を保存 */
				dispcur(g,scroll_cord_x(),scroll_cord_y(),1,MIKA_bk_color); /* 現在カーソルを消去 */
				dispbkcharline(g,scroll_cord_x(),scroll_cord_y(),MIKA_color_text_err); /* エラー文字表示位置の背景を赤色表示 */
				cslcolor(g,Color.black); /* 表示色を黒色に指定 */
				if(MIKA_err_count>0)
				{
					cslput(g,scroll_cord_x(),scroll_cord_y()-8,String.valueOf(MIKA_err_c_table[MIKA_err_count-1])); /* 一つ手前のエラー文字を黒色で再表示 */
				}
				cslput(g,scroll_cord_x(),scroll_cord_y(),String.valueOf(nChar)); /* エラー文字を黒色で表示 */
				MIKA_err_count++; /* エラー文字表示文字数をインクリメント */
				if(MIKA_text_point_y+MIKA_err_count<79) /* 次回表示カーソル位置が行末でない場合 */
				{
					dispcur(g,scroll_cord_x(),scroll_cord_y(),1,MIKA_color_cursor); /* カーソルを一つ進めて表示 */
				}
				else
				{
					dispcur(g,scroll_cord_x(),scroll_cord_y()-8,1,MIKA_color_cursor); /* カーソルを進めずに同じ位置に表示 */
				}
			}
			else /* エラー文字入力位置が行末の場合 */
			{
				cslcolor(g,MIKA_bk_color); /* 表示色に背景色を設定 */
				cslput(g,scroll_cord_x(),scroll_cord_y()-8,String.valueOf(MIKA_err_c_table[MIKA_err_count])); /* 行末のエラー文字表示をクリア */
				MIKA_err_c_table[MIKA_err_count-1]=nChar; /* 行末の位置にエラー文字を保存 */
				dispcur(g,scroll_cord_x(),scroll_cord_y()-8,1,MIKA_bk_color); /* 行末のカーソルを消去 */
				cslcolor(g,MIKA_bk_color); /* 表示色に背景色を設定 */
				dispbkcharline(g,scroll_cord_x(),scroll_cord_y()-8,MIKA_color_text_err); /* エラー文字表示位置の背景を赤色で表示 */
				cslcolor(g,Color.black); /* 表示色を黒色に指定 */
				cslput(g,scroll_cord_x(),scroll_cord_y()-8,String.valueOf(nChar)); /* エラー文字を黒色で表示 */
				dispcur(g,scroll_cord_x(),scroll_cord_y()-8,1,MIKA_color_cursor); /* カーソルを進めずに同じ位置に表示 */
			}
		}
	}
	void dispbkcharline(Graphics g,int x,int y,Color color) /* 入力文字の背景を指定色で塗りつぶす */
	{
		int x1,x2,y1,y2;
		cslcolor(g,color); /* 指定色に色指定 */
		y1=ycord(y); /* 開始 y座標 */
		x1=xcord(x); /* 開始 x座標 */
		y2=ycord(y+8); /* 終了 y座標 */
		x2=xcord(x+16); /* 終了 x座標 */
		g.fillRect(y1,x1,y2-y1,x2-x1); /* 指定色で塗りつぶす */
	}
	void cslscroll(Graphics g,int start_text_line,int double_text_hight,int rensyu_text_hight) /* 英文テキスト練習画面を上方に二行分スクロールする */
	{
		int	i,xx;
		int x1,x2,x3,y1,y2;
		y1=ycord(0); /* スクロール範囲左端y座標 */
		y2=ycord(639); /* スクロール範囲右端y座標 */
		for(i=1;i<8;i++)
		{
			x1=xcord(start_text_line+double_text_hight*i); /* スクロール対象エリア 開始 x 座標 */
			x3=xcord(start_text_line+double_text_hight*(i+1));	/* スクロール対象エリア 終了 x 座標 */
			xx=x1-x3; /* スクロール対象エリア 移動量 x 座標 */
			if(i<7) g.copyArea(y1,x1,y2-y1,x3-x1,0,xx); /* 練習画面二行分を上方に二行スクロール */
			else /* 最下行をスクロール */
			{
				x2=xcord(start_text_line+double_text_hight*i+rensyu_text_hight); /* スクロール対象エリア 終了 x 座標 */
				g.copyArea(y1,x1,y2-y1,x2-x1,0,xx); /* 練習画面一行分を上方に二行スクロール */
				cslcolor(g,MIKA_bk_color);/* 表示色に背景色を設定 */
				g.fillRect(y1,x1,y2-y1,x2-x1); /* 指定色で塗りつぶす */
			}
		}
	}
	void dispcur(Graphics g,int x,int y,int width,Color color) /* 文字入力カーソルを表示、または消去 */
	{ 
		int x1,x2,y1,y2;
		int xx1,yy1;
		int i;
		cslcolor(g,color); /* 指定色に色指定 */
		y1=ycord(y); /* カーソル 左上 y 座標取得 */
		yy1=ycord(y+width)-y1; /* カーソルのy方向太さを取得 */
		x1=xcord(x); /* カーソル 左上 x 座標取得 */
		xx1=xcord(x+width)-x1; /* カーソルのx方向の太さを取得 */ 
		y2=ycord(y+8); /* カーソル 右下 y 座標取得 */
		x2=xcord(x+16); /* カーソル 右下 x 座標取得 */
		for(i=0;i<xx1;i++) /* カーソルの横方向の上下の線を描画 */
		{
			g.drawLine(y1,x1+i,y2,x1+i); /* カーソルの横方向 上の線を描画 */
			g.drawLine(y1,x2-i,y2,x2-i); /* カーソルの横方向の下の線を描画 */
		}
		for(i=0;i<yy1;i++) /* カーソルの縦方向の左右の線を描画 */
		{
			g.drawLine(y1+i,x1,y1+i,x2); /* カーソルの縦方向の左の線を描画 */
			g.drawLine(y2-i,x1,y2-i,x2); /* カーソルの縦方向の右の線を描画 */
		}
	}
	void dispupmes(Graphics g,int flag) /* タイプ速度を更新したときのメッセージを表示 */
// flag=0 表示 flag=1 消去 
	{
		if(flag==0) cslcolor(g,MIKA_green); /* 表示色を緑色に設定 */
		else cslcolor(g,MIKA_bk_color); /* 表示色を背景色に設定 */
		cslput(g,20*16+8,20*8,MIKA_mesi3); /* 指定位置に「おめでとう、記録を更新しました」のメッセージを表示 */
	}
	void prockiroku(Graphics g) /* 英文テキスト練習にてタイプ入力速度が前回までの最高速度を更新したかの比較を行う */
	{
		if((MIKA_type_speed_record[MIKA_type_kind_no]==0.0)||(MIKA_type_speed>MIKA_type_speed_record[MIKA_type_kind_no])) /* 前回までの最高入力速度を更新した場合 */
		{
			if(MIKA_type_speed_record[MIKA_type_kind_no]>0.0) /* 前回の最高入力速度がゼロより大きい場合 */
			{
				dispupmes(g,0); /* 練習記録を更新しましたのメッセージを表示 */
				MIKA_type_syuryou_flag=2; /* 練習記録更新フラグを2にセット */
			}
			else /* 前回の最高入力速度がゼロの場合 */
			{
				MIKA_type_syuryou_flag=1; /* 練習記録更新フラグを1にセット */
			}
			MIKA_type_kiroku_date=new Date(); /* 最高記録達成日の日時を取得 */
			SimpleDateFormat MIKA_date=new SimpleDateFormat("yy/MM/dd"); /* 最高記録達成日時の取り出しフォーマットを作成 */
			MIKA_type_date=MIKA_date.format(MIKA_type_kiroku_date); /* 最高記録達成日時文字列を指定フォーマットに従って作成 */
//			System.out.printf("日付=%s\n",MIKA_type_date);
			MIKA_t_mojisu[MIKA_type_kind_no]=MIKA_type_count; /* 英文練習テキスト文字数保存 */
			MIKA_file_error_hayasa=whayasa(); /* 最高速度記録ファイル書き込み */
		}
	}
	double roundtime(double time) /* 小数点以下 切り捨て */
	{
		long time0;
		time0=(long)time; /* 浮動小数点を整数に変換 */
		time=time0; /* 整数を浮動小数点に変換 */
		return time;
	}
	public synchronized void paint(Graphics g) {
		MIKA_win_size=getSize(); /* 表示画面サイズ取得 */
//		System.out.printf("win size width=%d height=%d\n",MIKA_win_size.width,MIKA_win_size.height);
		MIKA_insets=getInsets(); /* 表示画面外枠サイズ取得 */
//		System.out.printf("Inset left=%d right=%d top=%d bottom=%d \n",MIKA_insets.left,MIKA_insets.right,MIKA_insets.top,MIKA_insets.bottom);
		dispmen(g); /* 画面表示 */
	}
	private class MyKeyAdapter extends KeyAdapter {

	    Graphics g;
		@Override
		public synchronized void  keyPressed(KeyEvent e) {
			int err;
			int keyCode;
			char keyChar;
       		g = getGraphics(); /* Graphics 取得 */
			keyChar=e.getKeyChar(); /* 入力文字取得 */
//			keyCode=(int) keyChar;
//			System.out.printf("KeyChar=%4x\n",KeyCode);			
			if(keyChar!=KeyEvent.CHAR_UNDEFINED) /* 入力されたキーが有効な文字の場合 */
			{
				err=exec_func(g,keyChar); /* 入力文字に対応した処理を実行 */
			}
    	    g.dispose(); /* Graphics 破棄 */
		}

	}
	public class Procttimer extends TimerTask /* 英文テキスト練習用一秒間隔タイマー */
	{
	    Graphics g;
		public synchronized void run(){
			if(MIKA_practice_end_flag==0) /* 練習実行中の場合 */
			{
				try {
// 						System.out.printf("Timer task\n");
				  		g = getGraphics(); /* Graphics 取得 */
						MIKA_semaphore.acquire(); /* セマフォー要求 */
						if(MIKA_practice_end_flag==0)
						{
							MIKA_type_end_time=System.currentTimeMillis(); /* 現在時刻をミリ秒で取得 */
							MIKA_ttype_speed_time=(MIKA_type_end_time-MIKA_type_start_time)/1000.0; /* 経過秒を実数で計算 */
							if(MIKA_type_speed_time!=MIKA_ttype_speed_time)
							{
								procdispspeed3(g); /* 英文テキスト練習の入力速度を表示 */
							}
						}
						MIKA_semaphore.release(); /* セマフォー解放 */
						g.dispose(); /* Graphics 破棄 */
				} catch(Exception ex)
				{	
					ex.printStackTrace();
				}
			}
		}
	}
}
