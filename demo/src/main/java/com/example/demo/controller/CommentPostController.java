package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.domain.Comment;
import com.example.demo.form.CommentPostForm;
import com.example.demo.repository.CommentRepository;

/**
 * 【初級者課題：手順4】コメントの投稿処理のみを担当するコントローラー.
 * 
 * おすすめの作成順番：4. コメントの投稿 に対応しています。
 * 既存のクラス（表示用、記事投稿用）とは別に、独立した処理として作成します。
 * 
 * @author taku
 */
@Controller // Spring MVCのコントローラーとして登録します。
public class CommentPostController {

    @Autowired // コメントを保存するために、CommentRepositoryを注入（DI）します。
    private CommentRepository commentRepository;

    /**
     * コメントの登録を行います.
     * 
     * @param form 画面から送信されたコメント情報（紐付く記事ID、名前、内容）が格納されたフォーム
     * @return 処理完了後のリダイレクト先
     */
    @PostMapping("/step4/post-comment") // HTMLのformタグで指定された送信先URLと一致させます。
    public String postComment(CommentPostForm form) {
        
        // 1. データベース保存用のドメインオブジェクト（Comment）を生成します。
        Comment comment = new Comment();
        
        // 2. フォームからドメインオブジェクトへデータを詰め替えます。（手動マッピング）
        // 型変換（String -> Integer）を行い、確実に正しいデータ型でドメインへ渡します。
        // articleIdをセットすることで、DB上で正しい「親記事」と紐付くようになります。
        comment.setArticleId(Integer.parseInt(form.getArticleId())); // どの記事へのコメントかをセット
        comment.setName(form.getName());                            // 投稿者の名前をセット
        comment.setContent(form.getContent());                      // 本文の内容をセット
        
        // 3. リポジトリを呼び出して、データベースへINSERT処理を実行します。
        commentRepository.insert(comment);
        
        // 4. 処理完了後は、一覧表示画面（トップページ）へリダイレクトします。
        // 前回の記事投稿と同様、二重投稿を防ぐためにPRGパターンを適用しています。
        // これにより、画面更新をしても「最新の一覧を取得して表示する」という安全な動作になります。
        return "redirect:/"; 
    }
}
