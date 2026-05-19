package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.CommentRepository;

/**
 * 【初級者課題：手順5】記事とそれに紐づくコメントの削除処理を担当するコントローラー.
 * 
 * おすすめの作成順番：5. 記事とコメントの削除 に対応しています。
 * 既存のクラスとは独立させ、削除ロジックのみを専門に扱います。
 * 
 * @author taku
 */
@Controller // Spring MVCのコンポーネントとして登録します。
public class BbsDeleteController {

    @Autowired // 記事を削除するため、ArticleRepositoryを注入します。
    private ArticleRepository articleRepository;

    @Autowired // 記事に紐づくコメントを削除するため、CommentRepositoryを注入します。
    private CommentRepository commentRepository;

    /**
     * 指定されたIDの記事と、その記事に紐づく全コメントを削除します.
     * 
     * @param articleId 削除対象の記事ID
     * @return 処理完了後のリダイレクト先（トップ画面）
     */
    @PostMapping("/step5/delete-article") // HTMLの削除ボタン（form）からのリクエストを受け取ります。
    public String deleteArticle(Integer articleId) {
        
        // 【思考プロセス：削除処理の順番の重要性について】
        // データベースには「データの整合性を守る」という非常に重要な役割があります。
        // 現在、commentsテーブルのarticle_idは、articlesテーブルのidを参照しています（外部キー）。
        
        // ① まずは「子」であるコメントから削除を実行します。
        // なぜなら、もし先に「親」である記事を削除しようとすると、データベース側が
        // 「まだこの記事を参照しているコメント（子）が残っているから、勝手に親を消してはいけない！」
        // と判断し、エラー（外部キー制約違反）を出して処理を止めてしまうからです。
        commentRepository.deleteByArticleId(articleId); // 関連するコメントを先にすべて消去します。
        
        // ② 子がいなくなったことを確認してから、最後に「親」である記事を削除します。
        // これにより、参照しているデータが一つも存在しない状態になるため、
        // データベースは安全に（整合性を保ったまま）記事を削除することができます。
        articleRepository.deleteById(articleId); // 記事本体を消去します。
        
        // 削除完了後は、二重処理の防止と画面更新のためにトップページへリダイレクトします。
        // Webシステムにおいて「破壊的な処理（削除や更新）」の後はリダイレクトが基本です。
        return "redirect:/"; 
    }
}
