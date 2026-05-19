# Spring Boot 初学者ドハマりチェックリスト30選

このチェックリストは、Spring Boot（Java / Thymeleaf）学習者が頻繁に直面する「あるあるエラー」をまとめたものです。エラーメッセージから原因を特定する「逆引き辞書」として活用してください。

## 📁 1. 設定ファイル・環境構築の罠（見えないエラー編）
「コードは合ってるのに動かない」が起きる魔境です。

1. **ymlのインデントずれ**：`application.yml` の階層がスペース2個分ずれている。またはTabキーを使ってしまっている。（Springが設定を認識できずデフォルト値で動く）
2. **propertiesのタイポ**：`spring.datasource.url` を `spring.date-source.url` と書くなど。（エラーは出ないがDBに繋がらない）
3. **Port 8080 was already in use**：裏で既にSpring Bootが起動しっぱなしなのに、もう1回起動ボタンを押してしまった。
4. **Mavenの更新忘れ**：`pom.xml` に依存関係を追加したのに、IDE上で「Mavenプロジェクトの再読み込み」をしていない（赤い波線が消えない）。
5. **いつまでも古いコードが動く**：コードを直したのに再起動していない。またはThymeleafのキャッシュが効いていて画面が変わらない。

## 🏷️ 2. アノテーション・DIの罠（コンテナの気持ち編）
Spring Bootの「魔法」が発動しないパターンのほとんどがこれです。

6. **`@Autowired` 忘れ**：フィールドはあるが注入されず、実行時に `NullPointerException`。
7. **`@Controller` / `@Repository` 忘れ**：クラスを作っただけでアノテーションを忘れ、DIコンテナに登録されず「Beanが見つかりません」エラー。
8. **パッケージ迷子**：メインクラス（`DemoApplication.java`）のパッケージより「上」や「外」の階層にクラスを作ってしまい、Springが読み込んでくれない。
9. **Lombokのアノテーション忘れ**：`@Data` や `@Getter` を付け忘れ、プロパティが使えない。
10. **DIの競合**：同じ名前のクラス（例：`ArticleRepository`）を別パッケージに2つ作ってしまい、Springがどちらを注入していいか分からず起動エラー。

## 🌐 3. コントローラー・HTTP通信の罠（交通整理編）
ブラウザとサーバー間の「お約束」に関するミスです。

11. **拡張子つけちゃう問題**：`return "bbs.html";` と書いてしまい、「bbs.html.html が見つかりません」というTemplateInputExceptionが出る。
12. **GETとPOSTの勘違い**：画面のformは `method="post"` なのに、Java側が `@GetMapping` で受け取ろうとして `405 Method Not Allowed`。
13. **リダイレクトの「/」忘れ**：`return "redirect:bbs";` と相対パスで書いてしまい、URLが `localhost:8080/step2/bbs` のようにおかしくなる。（正しくは `redirect:/bbs`）
14. **Formのバインド失敗**：HTMLの `name="userName"` に対して、Formクラスの変数を `private String username;`（Nが小文字）にしてしまい、値がnullになる。
15. **URLの重複**：別々のメソッドに同じ `@PostMapping("/post")` をつけてしまい、起動時に `Ambiguous mapping` エラー。

## 🎨 4. Thymeleaf・画面表示の罠（レンダリング編）
エラー画面がドバッと出て一番パニックになるポイントです。

16. **変数式の書き間違い**：`${article.name}` と書くべきところを `*{article.name}` や `#{article.name}` と書いて表示されない。
17. **th:actionの書き忘れ**：フォームの送信先を指定し忘れ、現在のURLにPOSTしてしまい予期せぬコントローラーが呼ばれる。
18. **閉じタグ忘れ**：`<div>` や `<input>` の閉じタグを忘れ、Thymeleafのパースエラー（500エラー）で画面が真っ白に。
19. **空っぽのリストでループ**：`th:each="item : ${list}"` の `${list}` 自体がControllerから渡されておらず（null）、ぬるぽが発生。
20. **th:fieldの悪夢**：FormオブジェクトをControllerから `Model` に渡し忘れているのに、HTMLで `th:field="*{name}"` を使おうとしてクラッシュ。

## 🗄️ 5. DB・SQLの罠（データ操作編）
JavaとDBという「2つの世界」の橋渡しで起きるミスです。

21. **SQL文末のセミコロン**：JdbcTemplateで実行するSQLの末尾に `;` をつけてしまい構文エラー（Java側のSQLには不要）。
22. **更新系と参照系のメソッド間違い**：`INSERT` や `UPDATE` なのに `template.query()` を使ってしまう（正しくは `update()`）。
23. **プレースホルダの不一致**：`VALUES (:userName)` と書いたのに、Javaオブジェクトのプロパティ名が `name` のため紐付かない。
24. **DBのカラム名タイポ**：DB側は `user_name` なのに、SQLで `SELECT username` と書いて存在しないカラムエラー。
25. **自動インクリメント未設定**：IDを自動採番にしたいのに、テーブル作成時に `SERIAL` や `AUTO_INCREMENT` をつけ忘れ、INSERT時にIDがnullで怒られる。

## ☕ 6. Javaの基礎・オブジェクトの罠（文法編）
フレームワーク以前の、Java自体の基礎で躓くポイントです。

26. **空文字と型の不一致**：Formの年齢項目（`Integer`）に、画面から未入力（空文字 `""`）が送られてきて `TypeMismatchException`（型変換エラー）。
27. **デフォルトコンストラクタ不在**：引数ありのコンストラクタを作ったせいでデフォルトコンストラクタが消滅し、SpringやDBライブラリがインスタンスを作れずエラー。
28. **Listの初期化忘れ**：ドメインクラスの `List<Comment> commentList;` を `new ArrayList<>()` しておらず、コメントを追加しようとしてぬるぽ。
29. **文字列の `==` 比較**：文字の比較で `.equals()` ではなく `==` を使ってしまい、条件分岐が一生 `true` にならない。
30. **getter/setterを作り忘れる**：そもそもフィールドをprivateにしただけでgetterがないため、Thymeleafで値を表示できない。
