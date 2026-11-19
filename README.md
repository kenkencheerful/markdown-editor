# Markdown Editor

Android端末で使う、オフライン対応のシンプルなMarkdownエディタです。

## 実装済みの機能

- Markdownテキストと文書名の編集
- 太字・斜体・段落・表ひな形の挿入
- 編集／プレビューの切り替え（表を含むMarkdownを端末内で描画）
- 保存ボタンによる端末内保存と、次回起動時の復元

文書はAndroid DataStoreに保存されます。アプリ実行時のネットワーク接続は使いません。複数文書管理、インポート／エクスポート、アプリストア向けリリース署名は未対応です。

## ビルドとテスト

JDK 17、Android SDK Platform 34、Build-Tools 34が必要です。初回ビルドではGradleと依存ライブラリをダウンロードします。

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug
```

生成されるデバッグAPK:

```text
app/build/outputs/apk/debug/app-debug.apk
```

デバッグAPKは開発用キーで署名され、端末に直接インストールして試せます。配布用APKを作る場合は、別途リリース署名の設定が必要です。
