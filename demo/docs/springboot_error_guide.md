# Spring Boot 掲示板開発：トラブルシューティング・メンターガイド

このドキュメントは、Spring Boot + Thymeleaf を用いた掲示板システム開発において、研修生が直面しやすいエラーとその解決・指導方法をまとめたものです。

---

## 【White Label Error 編】

### 1. 404 Not Found：戻り値のパス・拡張子ミス
**①【エラー名・コード】**
`404 Not Found` / `Whitelabel Error Page`

**②【初心者がやりがちな原因】**
「ファイルを指定するのだから、物理的なパスや拡張子が必要だ」という思い込み。Thymeleafの自動補完（接頭辞 `templates/`、接尾辞 `.html`）を理解していない。

**③【コンソールログ / エラー画面の具体例】**
- **ブラウザ:** `Type=Not Found, Status=404`
- **ログ:** `None of the error pages for this application has an explicit mapping for /error`

**④【正しい修正コード例】**
❌ **ダメな例**
```java
@GetMapping("/")
public String index() {
    // templatesフォルダや .html まで書いてしまっている
    return "templates/bbs.html"; 
}
```
⭕ **正しい例**
```java
@GetMapping("/")
public String index() {
    // 拡張子なし、templates/ 抜きの「論理ビュー名」のみを返す
    return "bbs"; 
}
```

**⑤【レビュアー・メンターの指導のツボ】**
「ブラウザのURLは何になっている？」「Spring Bootの設定で、HTMLを探しに行く場所はどう決まっているかな？」と問いかけます。Thymeleafが「フォルダ名」と「拡張子」を自動でガッチャンコしてくれている魔法を説明しましょう。

---

### 2. @RestControllerの誤用によるテキスト表示
**①【エラー名・コード】**
画面にHTML名がそのまま表示される（生のテキスト表示）

**②【初心者がやりがちな原因】**
ネット上の「Spring Boot 入門」記事でAPI作成用のコードをコピペしてしまい、画面遷移用の `@Controller` とデータ返却用の `@RestController` の違いを理解していない。

**③【コンソールログ / エラー画面の具体例】**
- **ブラウザ:** 真っ白な画面に `bbs` という文字だけが表示される。

**④【正しい修正コード例】**
❌ **ダメな例**
```java
@RestController // データを返すためのアノテーション
public class ArticleController {
    @GetMapping("/")
    public String index() {
        return "bbs"; // "bbs" という「文字列」そのものがブラウザに送られる
    }
}
```
⭕ **正しい例**
```java
@Controller // 画面遷移（HTMLレンダリング）のためのアノテーション
public class ArticleController {
    @GetMapping("/")
    public String index() {
        return "bbs"; // "bbs.html" を探してレンダリングする
    }
}
```

**⑤【レビュアー・メンターの指導のツボ】**
「ブラウザに届いているのはHTMLかな、それともただの単語かな？」と確認させます。`@RestController` = `@Controller` + `@ResponseBody` であることを教え、「ResponseBody（レスポンスの体）」に文字列をそのまま放り込んでいる状態を視覚的に説明します。

---

### 3. 500 Template Processing Error：プロパティ・Getter不在
**①【エラー名・コード】**
`500 Internal Server Error` / `TemplateInputException` / `Property or field 'xxx' cannot be found`

**②【初心者がやりがちな原因】**
HTML側（Thymeleaf）でのスペルミス、またはJavaのドメインクラスでカプセル化（private変数）したのに、Getterメソッドを作り忘れて外部（Thymeleaf）から値が参照できない。

**③【コンソールログ / エラー画面の具体例】**
- **ログ:**
```text
org.attoparser.ParseException: Exception evaluating SpringEL expression: "article.namme"
...
Caused by: org.springframework.expression.spel.SpelEvaluationException: EL1008E: Property or field 'namme' cannot be found on object of type 'com.example.demo.domain.Article'
```

**④【正しい修正コード例】**
❌ **ダメな例**
```html
<!-- HTML側のタイポ -->
<p th:text="${article.namme}"></p> 

<!-- または Java側でGetterがない -->
public class Article {
    private String name;
    // getName() が定義されていない
}
```
⭕ **正しい例**
```java
public class Article {
    private String name;
    public String getName() { return name; } // Getterが必要
}
```
```html
<p th:text="${article.name}"></p> <!-- 正しいプロパティ名 -->
```

**⑤【レビュアー・メンターの指導のツボ】**
「ログの 'Caused by'（原因）を見てごらん。'cannot be found' って書いてあるね。JavaのArticleクラスに、この名前で値を取り出すためのメソッド（Getter）はあるかな？」と誘導します。

---

## 【ビルドエラー・起動失敗 編】

### 4. Bean의 인ジェクション失敗（@Repository忘れ）
**①【エラー名・コード】**
`UnsatisfiedDependencyException` / `Field required a bean that could not be found`

**②【初心者がやりがちな原因】**
クラスは作ったが、Spring Bootに対して「これはSpringが管理する部品（Bean）だよ！」と教えるためのアノテーションを付け忘れている。

**③【コンソールログ / エラー画面の具体例】**
- **ログ:**
```text
Description:
Field articleRepository in com.example.demo.controller.ArticleController required a bean of type 'com.example.demo.repository.ArticleRepository' that could not be found.

Action:
Consider defining a bean of type 'com.example.demo.repository.ArticleRepository' in your configuration.
```

**④【正しい修正コード例】**
❌ **ダメな例**
```java
public class ArticleRepository { // アノテーションがない！
    // ...
}
```
⭕ **正しい例**
```java
@Repository // これが必要
public class ArticleRepository {
    // ...
}
```

**⑤【レビュアー・メンターの指導のツボ】**
「エラーメッセージの 'Action'（対策）を読んでみて。Springが部品を見つけられないと言っているね。Springにこのクラスを見つけてもらうための目印（アノテーション）は何だったかな？」と促します。

---

### 5. データソース構成エラー（application.properties設定漏れ）
**①【エラー名・コード】**
`Failed to configure a DataSource`

**②【初心者がやりがちな原因】**
DBを使うライブラリ（JDBC/PostgreSQL）を入れたものの、接続先のURLやユーザー名をどこにも書いていない。または、`application.properties` のファイル名や配置場所を間違えている。

**③【コンソールログ / エラー画面の具体例】**
- **ログ:**
```text
Description:
Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.

Reason: Failed to determine a suitable driver class
```

**④【正しい修正コード例】**
❌ **ダメな例**
`application.properties` が空、またはファイル名が `application.property`（単数形）になっている。

⭕ **正しい例**
`src/main/resources/application.properties` に記述：
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bbs
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver
```

**⑤【レビュアー・メンターの指導のツボ】**
「Spring Bootくんが、DBに繋ぎたいけど住所（URL）が分からないって泣いているよ。住所を書くための設定ファイルの名前と場所は正しいかな？」と確認させます。

---

## 【実行時・SQLエラー 編】

### 6. 外部キー制約違反（削除順序のミス）
**①【エラー名・コード】**
`DataIntegrityViolationException` / `FK制約違反`

**②【初心者がやりがちな原因】**
コメントが付いている記事を消そうとする際、「記事を消せばコメントも一緒に消える」と勘違いしている。リレーショナルデータベースの整合性を守るための「子（コメント）を先に消さないと、親（記事）は消せない」というルールを忘れている。

**③【コンソールログ / エラー画面の具体例】**
- **ログ:**
```text
ERROR: update or delete on table "articles" violates foreign key constraint "comments_article_id_fkey" on table "comments"
  Detail: Key (id)=(1) is still referenced from table "comments".
```

**④【正しい修正コード例】**
❌ **ダメな例**
```java
public void deleteArticle(Integer id) {
    articleRepository.deleteById(id); // コメントが残っているとエラー！
}
```
⭕ **正しい例**
```java
public void deleteArticle(Integer id) {
    commentRepository.deleteByArticleId(id); // 1. 先に子（コメント）を消す
    articleRepository.deleteById(id);        // 2. 次に親（記事）を消す
}
```

**⑤【レビュアー・メンターの指導のツボ】**
「記事を消したとき、その記事についていたコメントはどうなるべきだと思う？」「DBのルールで、親がいなくなって迷子になる子が残ることは許されないんだ。どっちから先に消すべきかな？」と順序を考えさせます。

---

### 7. 名前付きパラメータのタイポ
**①【エラー名・コード】**
`InvalidDataAccessApiUsageException` / `No value supplied for the SQL parameter`

**②【初心者がやりがちな原因】**
SQLの中の `:名前` と、Java側で値をセットする時の `"名前"` が、大文字・小文字やスペルミスで一致していない。

**③【コンソールログ / エラー画面の具体例】**
- **ログ:**
```text
org.springframework.dao.InvalidDataAccessApiUsageException: No value supplied for the SQL parameter 'articleid': No value registered for key 'articleid'
```

**④【正しい修正コード例】**
❌ **ダメな例**
```java
String sql = "DELETE FROM articles WHERE id = :articleId"; // Iが大文字
SqlParameterSource param = new MapSqlParameterSource().addValue("articleid", id); // iが小文字
```
⭕ **正しい例**
```java
String sql = "DELETE FROM articles WHERE id = :articleId";
SqlParameterSource param = new MapSqlParameterSource().addValue("articleId", id); // 完全に一致させる
```

**⑤【レビュアー・メンターの指導のツボ】**
「Javaの世界とSQLの世界で、受け渡し用の箱の名前がズレているみたいだよ。1文字ずつ指差し確認してみよう。大文字と小文字は区別されているかな？」と指導します。

---

### 8. NumberFormatException：hidden項目の不足
**①【エラー名・コード】**
`java.lang.NumberFormatException: For input string: ""`

**②【初心者がやりがちな原因】**
「どの記事に対するコメントか」を判別するための ID を、HTMLのフォーム内に `hidden`（隠し項目）で含めるのを忘れている。その結果、Java側に空文字が送られ、数値変換に失敗する。

**③【コンソールログ / エラー画面の具体例】**
- **ブラウザ:** `400 Bad Request` または `500 Internal Server Error`
- **ログ:** `java.lang.NumberFormatException: For input string: ""`

**④【正しい修正コード例】**
❌ **ダメな例**
```html
<form th:action="@{/post-comment}" method="post">
    <textarea name="content"></textarea>
    <!-- どの記事へのコメントかを示す ID が足りない！ -->
    <button>投稿</button>
</form>
```
⭕ **正しい例**
```html
<form th:action="@{/post-comment}" method="post">
    <!-- 記事IDを隠し項目として送信する -->
    <input type="hidden" name="articleId" th:value="${article.id}">
    <textarea name="content"></textarea>
    <button>投稿</button>
</form>
```

**⑤【レビュアー・メンターの指導のツボ】**
「コントローラー側では 'articleId' を受け取ろうとしているけど、HTMLのフォームの中にその名前の入力項目（input）はあるかな？」「ブラウザのデベロッパーツールで、送信されるデータの中身を見てごらん。空っぽになっていないかな？」と、データの流れを可視化させます。

---
**作成者:** Gemini CLI 講師モード
**目的:** メンター業務の効率化と受講生の自走力向上
