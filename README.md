# ChatApp
このアプリは自身の開発成果物用に作成した、リアルタイムチャットアプリです

<img src="https://user-images.githubusercontent.com/59288180/89122762-f7ab3700-d504-11ea-8b41-910efc44fe85.jpg" width="900">


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

<img src="https://user-images.githubusercontent.com/59288180/89121906-1954f000-d4fe-11ea-8348-8700ca6abed7.jpg" width="200">

## ユーザー検索
登録してあるユーザー名を知っていれば、ユーザー名を入力してチャットルームを作成する事ができます。

<img src="https://user-images.githubusercontent.com/59288180/89121954-62a53f80-d4fe-11ea-8cc0-72de864056f1.jpg" width="200">

## リアルタイムチャット
FirebaseのFirestoreを使用してリアルタイム受信を行っています。
メッセージの表示には[ChatMessageView](https://github.com/bassaer/ChatMessageView)ライブラリを使用しました。
送信した日時がメッセージと同時に表示され、顔文字や絵文字も使用でき多彩なコミュニケーションが行えます。

## ビデオ通話
Skywayを使用して、動画通話機能を行っています。
双方がサーバに接続していれば、着信を受け取って通話を開始できます。

<img src="https://user-images.githubusercontent.com/59288180/89121986-a26c2700-d4fe-11ea-9346-f286fca8f0b4.jpg" width="200">
