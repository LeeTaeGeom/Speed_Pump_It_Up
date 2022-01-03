package com.studyandroid.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    ImageView[] rndimg= new ImageView[4];
    TextView infotv,countdown;
    ImageButton leftbtn,rightbtn,upbtn,downbtn;
    Button centerbtn,rank;
    boolean gamestart=false;


    CountDownTimer timer1,timer2;

    int howmany;
    int tapcount=0;
    int[] rand= new int[4];
    long start;
    long end;
    int[] clickcheck=new int[10000];
    DBHelper DB;

    //위 오른쪽 아래 왼쪽 순으로 0 1 2 3

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent fromLogin = getIntent();
        String username = fromLogin.getStringExtra("username");

        DB=new DBHelper(this);

        rndimg[0]=findViewById(R.id.rnd1);
        rndimg[1]=findViewById(R.id.rnd2);
        rndimg[2]=findViewById(R.id.rnd3);
        rndimg[3]=findViewById(R.id.rnd4);

        infotv=findViewById(R.id.info);
        countdown=findViewById(R.id.countdown);

        rank=findViewById(R.id.rank);

        centerbtn=findViewById(R.id.standard);
        leftbtn=findViewById(R.id.leftbtn);
        rightbtn=findViewById(R.id.rightbtn);
        upbtn=findViewById(R.id.upbtn);
        downbtn=findViewById(R.id.downbtn);

        rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RankActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        upbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickcheck[tapcount]=0;
                tapcount+=1;
            }
        });
        rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickcheck[tapcount]=1;

                tapcount+=1;
            }
        });

        downbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickcheck[tapcount]=2;

                tapcount+=1;
            }
        });
        leftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickcheck[tapcount]=3;
                tapcount+=1;
            }
        });
        upbtn.setEnabled(false);
        rightbtn.setEnabled(false);
        downbtn.setEnabled(false);
        leftbtn.setEnabled(false);

        centerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gamestart){
                    end=System.currentTimeMillis();//끝

                    howmany=0;
                    for(int i=0;i<4;i++){
                        Log.v("last",clickcheck[i]+"");
                        Log.v("last1",tapcount+"");
                        Log.v("last2",rand[i]+"");


                        if(tapcount>=4){
                            if(clickcheck[tapcount+i-4]==rand[i]){
                                howmany+=1;
                            }
                        }
                        else{
                            Toast.makeText(HomeActivity.this,"4개 누르지않음! 실패!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(howmany==4){
                        countdown.setText((end-start)/1000.0+"초 걸림");
                        Toast.makeText(HomeActivity.this,"성공~!",Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(HomeActivity.this,"잘못 눌렀어요 실패!",Toast.LENGTH_SHORT).show();
                    }



                    gamestart=false;
                    timer2.cancel();
                    centerbtn.setEnabled(false);
//                    Log.v("end",""+end);

                    leftbtn.setEnabled(false);
                    rightbtn.setEnabled(false);
                    upbtn.setEnabled(false);
                    downbtn.setEnabled(false);



                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for(int i=0;i<4;i++){
                                rndimg[i].setImageResource(R.drawable.question);
                            }
//                            Log.v("end",username+"");

                            if(howmany==4){
                                float resulttime= (float) ((end-start)/1000.0);
                                boolean check=DB.newRank(username,resulttime);
                                if(check){ //여기서체크

                                    boolean change=DB.changeRank(username,resulttime); //여기서 변경
                                    Log.v("end1","여기지남1? ");

                                    if(change==true){//변경이 true니ㄲ까
                                        Toast.makeText(HomeActivity.this,"개인 신기록 달성 성공",Toast.LENGTH_SHORT).show();
//
                                    }


                                }
                            }


                            clickcheck= new int[10000];
                            centerbtn.setText("시작");
                            centerbtn.setEnabled(true);
                            infotv.setText("준비하시면 시작버튼을 눌러주세요!");
                            tapcount=0;
                            start=0;
                            end=0;
                            countdown.setText("");
                        }
                    },1000);


                }
                else{

                    gamestart=true;
                    timer1.start();
                    centerbtn.setText("중지");
                    centerbtn.setEnabled(false);
                }
            }
        });

        //3초 카운트다운(1초간격)
        timer1 = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long timeTillEnd1 =(millisUntilFinished/1000)+1;
                leftbtn.setEnabled(true);
                rightbtn.setEnabled(true);
                upbtn.setEnabled(true);
                downbtn.setEnabled(true);
                infotv.setText(timeTillEnd1+"초 뒤 시작");
            }

            @Override
            public void onFinish() {
                infotv.setText("시작하세요!");
                for(int i=0;i<4;i++){
                    rand[i]=(int)(Math.random()*4);
                    Log.v("rand",rand[i]+"");
                    if(rand[i]==0){
                        rndimg[i].setImageResource(R.drawable.up_arrow);

                    }
                    else if(rand[i]==1){
                        rndimg[i].setImageResource(R.drawable.right_arrow);

                    }
                    else if(rand[i]==2){
                        rndimg[i].setImageResource(R.drawable.down_arrow);

                    }
                    else{
                        rndimg[i].setImageResource(R.drawable.left_arrow);
                    }
                }

                start=System.currentTimeMillis(); //시작
//                Log.v("start",""+start);
                timer2.start();

            }
        };

        //10초 카운트다운(1초간격)
        timer2 = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                centerbtn.setEnabled(true);
                long timeTillEnd2 = (millisUntilFinished/1000)+1;
                countdown.setText(timeTillEnd2+"초 후 종료됩니다!");
            }

            @Override
            public void onFinish() {

                infotv.setText("시간 끝!");
                countdown.setText("");
                gamestart=false;
                centerbtn.setEnabled(false);

                leftbtn.setEnabled(false);
                rightbtn.setEnabled(false);
                upbtn.setEnabled(false);
                downbtn.setEnabled(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0;i<4;i++){
                            rndimg[i].setImageResource(R.drawable.question);
                        }
                        clickcheck= new int[10000];
                        centerbtn.setText("시작");
                        centerbtn.setEnabled(true);
                        infotv.setText("준비하시면 시작버튼을 눌러주세요!");
                        tapcount=0;
                    }
                },2000);
            }
        };

    }
}