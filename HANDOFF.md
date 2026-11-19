# 開発引き継ぎメモ

## 現在の状態

Kotlin／Jetpack ComposeのAndroidアプリとして、編集、Markdownプレビュー、基本的な書式ボタン、DataStoreによる単一文書の保存・復元を実装済み。Markdownの表示にはMarkwonを同梱しているため、アプリ実行中のネット接続は不要。

## ビルド

- JDK 17、Android SDK Platform 34、Build-Tools 34が必要。
- `local.properties`は端末固有のためGit管理せず、Android Studioまたは環境変数`ANDROID_HOME`でSDKの場所を指定する。
- `.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug`でテストとデバッグAPKを作成する。
- APKの出力先は`app/build/outputs/apk/debug/app-debug.apk`。

## 現時点の制限

- 文書は1件のみ。複数文書、ファイルの読み込み／書き出し、共有は未対応。
- APKはデバッグ署名。一般配布やストア公開にはリリース署名が必要。
- 実機での操作確認とオフライン確認は、ビルド環境で引き続き実施する。
