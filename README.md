# ChatApp
このアプリは自身の開発成果物用に作成した、リアルタイムチャットアプリです


![チャット画面](https://user-images.githubusercontent.com/59288180/89121846-9f246b80-d4fd-11ea-91a7-bec202c656be.jpg)
![ログイン画面](https://user-images.githubusercontent.com/59288180/89121930-34bffb00-d4fe-11ea-9308-557cfeacbcd4.jpg)
![チャットルームリスト画面](https://user-images.githubusercontent.com/59288180/89121941-443f4400-d4fe-11ea-8e92-d7d0714ffd6b.jpg)
![着信画面](https://user-images.githubusercontent.com/59288180/89121966-781a6980-d4fe-11ea-8388-40a652e951a5.jpg)

# 主な機能

- メールアドレスによるサインイン・ログイン
- ユーザー検索
- リアルタイムチャット
- ビデオ通話

## メールアドレスによるサインイン・ログイン
FirebaseのFirebaseAuthenticationを使用してユーザー認証を行っています。
ユーザ名とメールアドレス、パスワードを入力するとユーザー登録を行うことができます。
登録したメールアドレスでログインすることができます。

※Ver.1.0.0ではメールアドレスに認証リンクを送信する認証方法は対応していません。
![サインイン画面](https://user-images.githubusercontent.com/59288180/89121906-1954f000-d4fe-11ea-8348-8700ca6abed7.jpg)
## ユーザー検索
登録してあるユーザー名を知っていれば、ユーザー名を入力してチャットルームを作成する事ができます。
![ユーザー検索画面](https://user-images.githubusercontent.com/59288180/89121954-62a53f80-d4fe-11ea-8cc0-72de864056f1.jpg)

## リアルタイムチャット
FirebaseのFirestoreを使用してリアルタイム受信を行っています。
メッセージの表示には[ChatMessageView](https://github.com/bassaer/ChatMessageView)ライブラリを使用しました。
送信した日時がメッセージと同時に表示され、顔文字や絵文字も使用でき多彩なコミュニケーションが行えます。

## ビデオ通話
Skywayを使用して、動画通話機能を行っています。
双方がサーバに接続していれば、着信を受け取って通話を開始できます。
![ビデオ通話画面](https://user-images.githubusercontent.com/59288180/89121986-a26c2700-d4fe-11ea-9346-f286fca8f0b4.jpg)
