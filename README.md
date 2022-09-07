# 貯金ちゃん
簡単な貯金アプリを制作しました。

期間限定でAWSを用いてネットにアップしておりますので、見ていただければと思います。↓

http://chokinchan.net

# DEMO
Demo動画↓

https://user-images.githubusercontent.com/81454257/188755706-75c6bc98-ae44-4191-aae8-b7f8cf43b5cd.mp4

ライン連携イメージ↓

![IMG_4310](https://user-images.githubusercontent.com/81454257/188752491-96da5ead-7b30-484d-a987-1486d602600b.PNG)

# 制作目的
浪費癖のある友人の「1日の使える金額が視覚的にわかれば浪費癖も治るのに」という言葉にヒントを得て制作しました。

ユーザが貯金を行うために、月毎に「固定収入」、「固定支出」、「目標貯金額」を設定していただくことで、その月の1日に使用できる金額を算出できます。

そしてその1日に使用できる金額を上回らなければ、月の目標貯金額を達成できる仕組みです。

また、ユーザはラインと連携して通知をみることで「1日の使用できる金額」を視覚的に理解し、その金額を超えないよう出費を意識的に抑制することが本アプリの目的です。


# 機能説明
認証機能とLineNotifyを用いてライン連携できる機能を実装しています。

LineNotifyの設定方法は下記のURLから参照して、アクセストークン取得後、設定いただければ毎朝8時に通知が来るよう設定しております。

 https://zenn.dev/protoout/articles/18-line-notify-setup
 
 レスポンシブ対応しておりますので、IPhoneからでも使用できます。
 
 残課題が残っており順に修正対応をおこなっている最中です。
 （emailではなくても登録・ログインできてしまう。etc...）
 
# 使用フレームワーク
 - フロントエンド：React, ChakraUI
 - バックエンド：SpringBoot, Spring Security
 
 ⇨フロントエンドは下記別リポジトリにて管理しています。
 
 https://github.com/shota4869/react-front-app
  
  Qiitaにもまとめています。社内で紹介用に簡単にまとめた記事なので詳細は後日記載予定です。
  
  https://qiita.com/sho0403/items/58a6699672e9cf58f02d
  
# 起動
フロントエンド：　
```
  yarn start
```
バックエンド：
```
 maven install
 java -jar springboot-rest-api-0.0.1-SNAPSHOT.jar
```

# Author
* takase shota
* takase.shota01@gmail.com
