Spring MVCチュートリアル
========================

# 概要
*	STEP 00: DBアクセスコードを生成する。
*	STEP 01: ホーム画面を作成する。



# STEP 00: DBアクセスコードを生成する。

DBアクセスにあたっては [MyBatis](http://mybatis.github.io/mybatis-3/ "MyBatis") をメインとして使用します。動的に条件を組立てるSQLを発行するケースでは [Querydsl](http://www.querydsl.com "Querydsl") (特に [Querydsl SQL](https://github.com/querydsl/querydsl/tree/master/querydsl-sql "Querydsl SQL") ) を使用します。
なお、DBアクセス用にコード生成ツールの [MyBatis Generator](http://mybatis.github.io/generator/ "MyBatis Generator") と [Querydsl codegen](https://github.com/querydsl/codegen "Querydsl codegen") を使用します。

```bash:コマンドライン
$ mvn clean compile flyway:migrate
$ mvn mybatis-generator:generate
$ mvn querydsl:export
```


# STEP 01: ホーム画面を作成する。

単純な画面表示から始めましょう。
画面を表示するには「コントローラ」と「JSP」を作成します。これを順に作っていきます。

## 画面の仕様
まず画面の仕様を確認します。ホーム画面は下記の通りです。

*	外部仕様
	*	URIパス: `/secure/`
	*	TODO登録画面、TODO検索画面へのリンクを配置する。
*	内部仕様
	*	コントローラ
		*	インタフェース: `cherry.spring.tutorial.web.secure.home.HomeController`
		*	実装クラス: `cherry.spring.tutorial.web.secure.home.HomeControllerImpl`
	*	JSP: `/WEB-INF/tiles/secure/home/init.jsp`
		*	ビュー名: `secure/home/init`

## コントローラ
### インタフェース
Spring MVC ではコントローラのインタフェースに「当該インタフェースが処理するURIパス」を、アノテーション `@RequestMapping(URIパス)` で定義します。アノテーションは、インタフェースとメソッドの両方に指定することができます。メソッドに指定したパスは、インタフェースに指定したURIパスからの相対パス (サブパス) として扱われます。
アノテーションに指定するURIパスは定数文字列である必要があります。実プロジェクトでは、URIパスは予め用意された定数定義を参照することが基本です。本チュートリアルでは、`PathDef` クラスに定数定義してありますので、これを参照します。

最初に作成する「ホーム画面」はURIパス一つなので、インタフェースに定義するメソッドも一つです。
Spring MVC は、アノテーションで指定することが前提であり、使い方のバリエーションは多岐に亘ります。ただし、それでは自由度が高すぎるため、実際のプロジェクトではクラス＆メソッドの定型パターンを定義し、それ従う形をとります。
本チュートリアルでは、下記をクラス＆メソッドの定型パターンとします。

*	クラス名: `XxxController`(インタフェース)、`XxxControllerImpl`(実装クラス)
*	メソッド名: `init`(初期画面)、`confirm`(確認画面)、`execute`(主処理)、`finish`(完了画面)、`download`(ダウンロード)
*	引数
	*	入力値を受取る画面で指定する
		*	`XxxForm form`
		*	`BindingResult binding`
	*	ページネーションする画面で指定する (ページネーションの情報をセッションに入れる場合は、ここに指定せずに、`form`に入れる)
		*	`int pageNo`
		*	`int pageSz`
	*	認証要画面で必須
		*	`Authentication auth`
	*	全画面で必須
		*	`Locale locale`
		*	`SitePreference sitePref`
		*	`HttpServletRequest request`
	*	ダウンロード機能で指定する
		*	`HttpServletResponse response`
	*	HTTPリダイレクトの元と先で指定する
		*	`RedirectAttributes redirAttr`
*	返却値: `ModelAndView`

これに基づきインタフェースを下記の通り定義します (`package`や`import`は省略しています)。

```Java:HomeController
@RequestMapping(PathDef.URI_HOME)
public interface HomeController {

	@RequestMapping()
	ModelAndView init(Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request);

}
```

### 実装クラス
コントローラインタフェースを実装 (`implements`) します。また、実装クラスにアノテーション `@Controller` を指定することで、Spring MVC が自動的にインスタンス登録します (Java以外に設定ファイルを記述する必要はありません)。
実装クラスでは、業務ロジックを実行し、表示する画面の名前「ビュー名」を`ModelAndView`に入れて返却します。画面に描画するデータがあればそれも`ModelAndView`に入れますが、ホーム画面には描画するデータがありませんので、何も入れません。

これを踏まえ実装クラスを下記の通り定義します。

```Java:HomeControllerImpl
@Controller
public class HomeControllerImpl implements HomeController {

	@Override
	public ModelAndView init(Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(PathDef.VIEW_HOME);
		return mav;
	}

}
```

## JSP
Spring MVC でも、ビューにJSPを使用する場合は、いわゆるJSPの記法でOKです。JSP, EL, [JSTL 1.2](https://jstl.java.net "JSTL 1.2"), Spring Tag, Spring Form Tag, Spring Security Tag が使えます。
また、本チュートリアルでは[Apache Tiles3](https://tiles.apache.org "Apache Tiles3")を使っています。実際に記述するJSPはbody要素の中身(のコンテンツ領域)のみです。

ホーム画面のJSPを下記の通り定義します。(下記の`c:`は JSTL の Core Tag Library です)

```HTML:/WEB-INF/tiles/secure/home/init.jsp
<h2>ホーム</h2>
<ul>
	<li><a href="<c:url value="/secure/todo/create" />">TODO登録</a></li>
	<li><a href="<c:url value="/secure/todo/list" />">TODO検索</a></li>
</ul>
```

以上。
