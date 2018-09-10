package com.example.wf.demorxjava;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.AndroidCharacter;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    Observer<String> observer;

    Subscriber<String> subscriber;

    Observable<String> observable;

    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(subscriber.isUnsubscribed())
            subscriber.unsubscribe();

    }

    private void init(){

         observer = new Observer<String>(){
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }



        };

         subscriber = new Subscriber<String>(){
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void setProducer(Producer p) {
                super.setProducer(p);
            }
        };

        observable =  Observable.create(new Observable.OnSubscribe<String>(){
            @Override
            public void call(Subscriber<? super String> subscriber) {

                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onNext("3");
                subscriber.onCompleted();
            }
        });
        observable.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        });

        observable = Observable.just("1","2","3");//依次调用123oncompleted()

        String[] words={"1","2","3"};

        observable = Observable.from(words);//依次调用123oncompleted()

        observable.subscribe(observer);//注册

        observable.subscribe(subscriber);//注册订阅

        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i("tag","1");
            }
        };

        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.i("error","2");
            }
        };

        Action0 onCompletedAction = new Action0() {
            @Override
            public void call() {
                Log.i("completed","3");
            }
        };
        //Subscriber观察者 Subscribe订阅 observable被观察者
        //自动创建Subscriber,并且使用 onNextAction 来定义onNext()
        observable.subscribe(onNextAction);
        //自动创建Subscriber,并且使用 onNextAction 和 onErrorAction 来定义onNext()和onError()
        observable.subscribe(onNextAction,onErrorAction);
        //自动创建Subscriber,并且使用 onNextAction 和 onErrorAction 以及 onComletedAction
        //来定义 onNext() onError() onComleted()
        observable.subscribe(onNextAction,onErrorAction,onCompletedAction);

    }

    public void initdate(){
        final String[] names = {"1","2","3","4"};
        Observable.from(names)
                .subscribeOn(Schedulers.newThread())//新现成执行subscribe
                .observeOn(AndroidSchedulers.mainThread())//android主线程执行completed
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String name) {
                        Log.i("tag",name);
                    }
                });
    }
     ImageView imageView;
    public void getBitmap(){
        final int drawableRes = R.mipmap.ic_launcher;
        Observable.create(new Observable.OnSubscribe<Drawable>(){
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = ContextCompat.getDrawable(ctx,drawableRes);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribe(new Observer<Drawable>() {
            @Override
            public void onCompleted() {
                Toast.makeText(ctx,"完成啦",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(ctx,"出错啦",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Drawable drawable) {
                imageView.setImageDrawable(drawable);
            }
        });
    }

    public void getBitmaps(){
        Observable.just("image/logo.png")
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String s) {
                        Bitmap bit = BitmapFactory.decodeFile(s);
                        return bit;
                    }
                }).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                //showbitmap(bitmap);
            }
        });


    }

    class Student{
        String name;
        List<gongke> gongkes;

        public List<gongke> getGongkes() {
            return gongkes;
        }

        public void setGongkes(List<gongke> gongkes) {
            this.gongkes = gongkes;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    class gongke{
        String yuwen;
        String shuxue;
        String english;

        public String getYuwen() {
            return yuwen;
        }

        public void setYuwen(String yuwen) {
            this.yuwen = yuwen;
        }

        public String getShuxue() {
            return shuxue;
        }

        public void setShuxue(String shuxue) {
            this.shuxue = shuxue;
        }

        public String getEnglish() {
            return english;
        }

        public void setEnglish(String english) {
            this.english = english;
        }
    }

    public void logStudentsname(){
        final Student[] students={};
        Subscriber<Student> subscriber = new Subscriber<Student>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Student s) {
                Log.d("tag",s.toString());
                List<gongke> courses = s.getGongkes();
                for (int i=0;i<courses.size();i++){
                    gongke gk=courses.get(i);
                    Log.i("tag",gk.getEnglish());
                }
            }
        };
//        Observable.from(students)
//                .map(new Func1<Student, String>() {
//                    @Override
//                    public String call(Student student) {
//
//                        return student.getName();
//                    }
//                }).subscribe(subscriber);
        Observable.from(students)
                .subscribe(subscriber);

    }

    public void getStudentinfo(){
        final Student[] students={};
        Subscriber<gongke> subscriber = new Subscriber<gongke>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(gongke gongke) {
                Log.i("tag",gongke.getYuwen());
            }
        };
        Observable.from(students)
                .flatMap(new Func1<Student, Observable<gongke>>() {
                    @Override
                    public Observable<gongke> call(Student student) {
                        return Observable.from(student.getGongkes());
                    }
                })
        .subscribe(subscriber);
    }





}
